package org.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/** [SerializationUtils] — это утилитарный класс (аналог синглтона) для сериализации и десериализации
 * Java-объектов, реализующих интерфейс Serializable.
 * <p>
 * Использует стандартный механизм Java-сериализации для преобразования объектов в байты и обратно.
        * Обеспечивает корректную обработку данных переменной длины в неблокирующем режиме
 * путем добавления 4-байтовой длины объекта перед его данными.
        */
public final class SerializationUtils {
    private static final Logger logger = Logger.getLogger(SerializationUtils.class.getName());

    // Приватный конструктор, чтобы предотвратить создание экземпляров утилитарного класса.
    private SerializationUtils() {
    }

    /** Сериализует любой объект, реализующий Serializable, в ByteBuffer.
     * <p>
     * Сначала в буфер записывается 4-байтная длина сериализованного объекта (в формате Big-Endian),
     * а затем сами байты объекта.
     *
             * @param obj Объект для сериализации.
            * @return ByteBuffer, содержащий сначала длину, а затем байтовое представление объекта,
            * или null в случае ошибки сериализации.
            */
    public static ByteBuffer objectToByteBuffer(Serializable obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            // Сериализуем объект в массив байт.
            oos.writeObject(obj);
            byte[] objectBytes = bos.toByteArray();
            int objectLength = objectBytes.length;

            // Выделяем буфер: 4 байта для длины + количество байт самого объекта.
            ByteBuffer buffer = ByteBuffer.allocate(4 + objectLength);
            buffer.order(ByteOrder.BIG_ENDIAN); // Устанавливаем порядок байт для длины.
            buffer.putInt(objectLength);        // Записываем длину.
            buffer.put(objectBytes);            // Записываем сам объект.
            buffer.flip();                      // Переводим буфер в режим чтения.
            return buffer;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при сериализации объекта " + obj.getClass().getSimpleName(), e);
            return null; // В случае ошибки возвращаем null.
        }
    }

    /* Внутренний класс [ObjectReaderState] предназначен для пошагового чтения (десериализации)
     * объектов из ByteBuffer в неблокирующем режиме.
            * <p>
     * Это необходимо, когда данные приходят по сети фрагментами. Класс позволяет сначала собрать
     * 4 байта длины объекта, затем собрать все байты самого объекта, прежде чем пытаться его десериализовать.
            * Каждый экземпляр этого класса соответствует состоянию чтения одного объекта.
     */
    public static class ObjectReaderState {
        // Буфер для временного хранения 4 байт, представляющих длину следующего объекта.
        private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        // Буфер для хранения всех байт самого объекта после того, как его длина известна.
        private ByteBuffer objectBuffer = null;

        // Ожидаемая длина объекта, полученная из lengthBuffer.
        private int expectedLength = -1;
        // Флаг, указывающий, была ли уже прочитана длина объекта.
        private boolean isLengthRead = false;

        /* Читает байты из входящего dataBuffer и пытается собрать полную длину следующего объекта.
                *
                * @param dataBuffer Буфер, содержащий входящие данные с сетевого канала.
         * @return true, если полная длина объекта была успешно прочитана, false в противном случае.
         * @throws IOException Если получена некорректная (слишком большая или отрицательная) длина объекта.
                */
        public boolean readLengthFromBuffer(ByteBuffer dataBuffer) throws IOException {
            if (isLengthRead) return true; // Длина уже прочитана, ничего не делаем.
            // Перемещаем байты из входящего буфера в lengthBuffer.
            while (dataBuffer.hasRemaining() && lengthBuffer.hasRemaining()) {
                lengthBuffer.put(dataBuffer.get());
            }

            // Если lengthBuffer заполнен (то есть мы собрали 4 байта длины).
            if (!lengthBuffer.hasRemaining()) {
                lengthBuffer.flip(); // Переключаем lengthBuffer в режим чтения.
                expectedLength = lengthBuffer.getInt(); // Извлекаем целое число - длину объекта.
                isLengthRead = true; // Устанавливаем флаг, что длина прочитана.
                lengthBuffer.clear(); // Готовим lengthBuffer для следующей длины.

                // Базовая валидация длины, чтобы предотвратить DoS-атаки или ошибки.
                if (expectedLength < 0 || expectedLength > 10 * 1024 * 1024) { // Максимальный размер 10 MB.
                    reset();
                    throw new IOException("Получена неверная длина объекта: " + expectedLength + ". Макс. размер: " + (10 * 1024 * 1024));
                }
                // Выделяем буфер нужного размера для самого объекта.
                objectBuffer = ByteBuffer.allocate(expectedLength);
                return true;
            }
            return false;
        }

        /**
         * Читает байты из входящего dataBuffer и заполняет objectBuffer до тех пор,
         * пока объект не будет полностью собран.
         *
         * @param dataBuffer Буфер, содержащий входящие данные с сетевого канала.
         * @return `true`, если все байты объекта были успешно прочитаны, `false` в противном случае.
         */
        public boolean readObjectBytesFromBuffer(ByteBuffer dataBuffer) {
            if (!isLengthRead || null == objectBuffer) {
                return false;
            }

            // Перемещаем байты из входящего буфера в objectBuffer.
            while (dataBuffer.hasRemaining() && objectBuffer.hasRemaining()) {
                objectBuffer.put(dataBuffer.get());
            }
            // Возвращаем true, если objectBuffer полностью заполнен.
            return !objectBuffer.hasRemaining();
        }

        /**
         * Десериализует собранные байты объекта из objectBuffer.
         *
         * @param <T> Ожидаемый тип десериализованного объекта.
         * @return Десериализованный объект типа T, или null в случае ошибки или неполных данных.
         */
        @SuppressWarnings("unchecked")
        public <T> T deserializeObject() {
            // Проверяем, что длина прочитана, буфер объекта существует и он полностью заполнен.
            if (!isLengthRead || objectBuffer == null || objectBuffer.hasRemaining()) {
                return null;
            }
            objectBuffer.flip(); // Переключаем objectBuffer в режим чтения.
            byte[] objectBytes = new byte[objectBuffer.remaining()];
            objectBuffer.get(objectBytes); // Копируем байты объекта в массив.

            try (ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                // Десериализуем объект из массива байт.
                return (T) ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Ошибка при десериализации объекта.", e);
                return null; // Возвращаем null при ошибке.
            } finally {
                reset(); // В любом случае сбрасываем состояние для следующего объекта.
            }
        }

        /**
         * Сбрасывает состояние чтения, готовя ObjectReaderState к приему нового объекта.
         */
        public void reset() {
            lengthBuffer.clear();
            objectBuffer = null;
            expectedLength = -1;
            isLengthRead = false;
        }

        // Геттеры для доступа к состоянию
        public boolean isLengthRead() {
            return isLengthRead;
        }
    }
}