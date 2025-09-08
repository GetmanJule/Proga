package org.managerData;

import org.classes.*;
import org.enums.Color;
import org.enums.Country;
import org.enums.MpaaRating;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * MovieCollectionManager — класс для управления коллекцией объектов Movie.
 * Совмещает логику управления коллекцией в памяти и логику сохранения/загрузки в XML-файл.
 * Структурно является аналогом класса StudyGroupCollection из примера на Kotlin.
 */
public class MovieCollectionManager {

    private ArrayList<Movie> movies; // Коллекция инкапсулирована внутри этого класса
    private final String filePath;   // Путь к файлу с данными
    private long nextId = 1;         // Счетчик для генерации уникальных ID

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Конструктор менеджера. При создании экземпляра сразу загружает коллекцию из файла.
     * @param path Путь к XML-файлу с данными.
     */
    public MovieCollectionManager(String path) {
        this.filePath = path;
        try {
            this.movies = loadData(filePath);
            updateNextId();
            System.out.println("Коллекция успешно загружена. Загружено " + movies.size() + " элементов.");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.err.println("Критическая ошибка при загрузке коллекции из файла: " + e.getMessage());
            System.out.println("Будет создана новая пустая коллекция.");
            this.movies = new ArrayList<>();
        }
    }

    // --- ПУБЛИЧНЫЕ МЕТОДЫ ДЛЯ УПРАВЛЕНИЯ КОЛЛЕКЦИЕЙ ---

    /**
     * Добавляет новый фильм в коллекцию.
     * Автоматически присваивает уникальный ID, устанавливает текущую дату создания и сортирует коллекцию.
     * @param movie Объект Movie для добавления (без установленных id и creationDate).
     */
    public Movie add(Movie movie) {
        // Присваиваем следующий уникальный ID и увеличиваем счетчик.
        movie.setId(this.nextId++);

        // Присваиваем текущую дату создания.
        movie.setCreationDate(LocalDateTime.now());

        // Добавляем полностью сформированный объект в коллекцию.
        this.movies.add(movie);

        // Сортируем коллекцию по имени после добавления.
        this.movies.sort(Comparator.comparing(Movie::getNameUpperCase));

        // Возвращаем тот же объект, но уже с присвоенными системными полями.
        return movie;
    }

    /**
     * Удаляет фильм из коллекции по его ID.
     * @param id ID фильма для удаления.
     * @return true, если элемент был найден и удален, иначе false.
     */
    public boolean removeById(long id) {
        return movies.removeIf(movie -> movie.getId() == id);
    }

    /**
     * Очищает всю коллекцию и сбрасывает счетчик ID.
     */
    public void clear() {
        movies.clear();
        nextId = 1;
    }

    /**
     * Возвращает защищенную от изменений копию списка всех фильмов.
     * @return Новый список, содержащий все фильмы из коллекции.
     */
    public List<Movie> getAll() {
        return new ArrayList<>(movies);
    }

    /**
     * Сохраняет текущее состояние коллекции в XML-файл, указанный при создании менеджера.
     */
    public void saveCollection() {
        try {
            writeToFile(this.movies, this.filePath);
            System.out.println("Коллекция успешно сохранена в файл.");
        } catch (ParserConfigurationException | TransformerException | IOException e) {
            System.err.println("Ошибка при сохранении коллекции в файл: " + e.getMessage());
        }
    }

    // --- ВНУТРЕННИЕ (PRIVATE) МЕТОДЫ ---

    /**
     * Обновляет счетчик nextId на основе максимального ID в текущей коллекции.
     */
    private void updateNextId() {
        if (movies.isEmpty()) {
            nextId = 1;
        } else {
            nextId = movies.stream()
                    .mapToLong(Movie::getId)
                    .max()
                    .orElse(0L) + 1;
        }
    }



    private ArrayList<Movie> loadData(String path) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(path);
        if (!file.exists() || file.length() == 0) {
            System.out.println("Файл данных не найден или пуст. Будет создана новая коллекция.");
            return new ArrayList<>();
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        ArrayList<Movie> loadedMovies = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("Movie");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    Element eElement = (Element) node;
                    Movie movie = parseMovieFromElement(eElement);
                    loadedMovies.add(movie);
                } catch (Exception e) {
                    System.err.println("Ошибка при парсинге элемента Movie #" + i + ": " + e.getMessage() + ". Элемент будет пропущен.");
                }
            }
        }

        Set<Long> ids = new HashSet<>();
        ArrayList<Movie> validMovies = new ArrayList<>();
        for (Movie movie : loadedMovies) {
            if (ids.contains(movie.getId())) {
                System.err.println("Ошибка валидации: обнаружен дубликат ID " + movie.getId() + ". Элемент будет пропущен.");
            } else {
                ids.add(movie.getId());
                validMovies.add(movie);
            }
        }
        return validMovies;
    }

    private void writeToFile(ArrayList<Movie> movies, String path) throws ParserConfigurationException, TransformerException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        if (movies.isEmpty()) {
            new FileWriter(path, false).close();
            return;
        }

        for (Movie movie : movies) {
            Element movieElement = createMovieElement(doc, movie);
            rootElement.appendChild(movieElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult file = new StreamResult(new File(path));

        transformer.transform(source, file);
    }

    private Movie parseMovieFromElement(Element eElement) {
        long id = Long.parseLong(getTagValue("id", eElement));
        String name = getTagValue("name", eElement);

        Element coordinatesElement = (Element) eElement.getElementsByTagName("coordinates").item(0);
        float coordX = Float.parseFloat(getTagValue("x", coordinatesElement));
        Long coordY = Long.parseLong(getTagValue("y", coordinatesElement));
        Coordinates coordinates = new Coordinates(coordX, coordY);

        LocalDateTime creationDate = LocalDateTime.parse(getTagValue("creationDate", eElement), dateTimeFormatter);
        Long oscarsCount = Long.parseLong(getTagValue("oscarsCount", eElement));
        float budget = Float.parseFloat(getTagValue("budget", eElement));
        double usaBoxOffice = Double.parseDouble(getTagValue("usaBoxOffice", eElement));
        MpaaRating mpaaRating = MpaaRating.valueOf(getTagValue("mpaaRating", eElement));

        Element personElement = (Element) eElement.getElementsByTagName("person").item(0);
        Person operator = parsePersonFromElement(personElement);

        return new Movie(id, name, coordinates, creationDate, oscarsCount, budget, usaBoxOffice, mpaaRating, operator);
    }

    private Person parsePersonFromElement(Element personElement) {
        String personName = getTagValue("name", personElement);
        String passportID = getTagValue("passportID", personElement);

        String eyeColorStr = getTagValue("eyeColor", personElement);
        Color eyeColor = "null".equals(eyeColorStr) ? null : Color.getColor(eyeColorStr);

        Country nationality = Country.valueOf(getTagValue("nationality", personElement));

        Location location = null;
        NodeList locationNodes = personElement.getElementsByTagName("location");
        if (locationNodes.getLength() > 0) {
            Element locationElement = (Element) locationNodes.item(0);
            String locXStr = getTagValue("x", locationElement);
            String locYStr = getTagValue("y", locationElement);
            String locName = getTagValue("name", locationElement);

            if (!"null".equals(locXStr) && !"null".equals(locYStr)) {
                Float locX = Float.parseFloat(locXStr);
                Double locY = Double.parseDouble(locYStr);
                location = new Location(locX, locY, "null".equals(locName) ? null : locName);
            }
        }

        return new Person(personName, passportID, eyeColor, nationality, location);
    }

    private Element createMovieElement(Document doc, Movie movie) {
        Element movieElement = doc.createElement("Movie");

        movieElement.appendChild(createNode(doc, "id", String.valueOf(movie.getId())));
        movieElement.appendChild(createNode(doc, "name", movie.getName()));

        Element coordinatesElement = doc.createElement("coordinates");
        coordinatesElement.appendChild(createNode(doc, "x", String.valueOf(movie.getCoordinates().getX())));
        coordinatesElement.appendChild(createNode(doc, "y", String.valueOf(movie.getCoordinates().getY())));
        movieElement.appendChild(coordinatesElement);

        movieElement.appendChild(createNode(doc, "creationDate", movie.getCreationDate().format(dateTimeFormatter)));
        movieElement.appendChild(createNode(doc, "oscarsCount", String.valueOf(movie.getOscarsCount())));
        movieElement.appendChild(createNode(doc, "budget", String.valueOf(movie.getBudget())));
        movieElement.appendChild(createNode(doc, "usaBoxOffice", String.valueOf(movie.getUsaBoxOffice())));
        movieElement.appendChild(createNode(doc, "mpaaRating", movie.getMpaaRating().toString()));

        Element personElement = createPersonElement(doc, movie.getOperator());
        movieElement.appendChild(personElement);

        return movieElement;
    }

    private Element createPersonElement(Document doc, Person person) {
        Element personElement = doc.createElement("person");

        personElement.appendChild(createNode(doc, "name", person.getName()));
        personElement.appendChild(createNode(doc, "passportID", person.getPassportID()));
        personElement.appendChild(createNode(doc, "eyeColor", person.getEyeColor() != null ? person.getEyeColor().toString() : "null"));
        personElement.appendChild(createNode(doc, "nationality", person.getNationality().toString()));

        Element locationElement = doc.createElement("location");
        if (person.getLocation() != null) {
            locationElement.appendChild(createNode(doc, "x", String.valueOf(person.getLocation().getX())));
            locationElement.appendChild(createNode(doc, "y", String.valueOf(person.getLocation().getY())));
            locationElement.appendChild(createNode(doc, "name", person.getLocation().getName() != null ? person.getLocation().getName() : "null"));
        } else {
            locationElement.appendChild(createNode(doc, "x", "null"));
            locationElement.appendChild(createNode(doc, "y", "null"));
            locationElement.appendChild(createNode(doc, "name", "null"));
        }
        personElement.appendChild(locationElement);

        return personElement;
    }

    private String getTagValue(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    private Node createNode(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}