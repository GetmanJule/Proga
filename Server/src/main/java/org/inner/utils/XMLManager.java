package org.inner.utils;

import org.data.inner.Coordinates;
import org.data.inner.Location;
import org.data.inner.Movie;
import org.data.inner.Person;
import org.data.inner.enums.Color;
import org.data.inner.enums.Country;
import org.data.inner.enums.MpaaRating;
import org.main.ServerMain;
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
import java.util.LinkedList;

/**
 * XMLManager for managing XML DB
 */
public class XMLManager {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static ArrayList<Movie> getData() {
        return data;
    }

    public void setData(ArrayList<Movie> data) {
        XMLManager.data = data;
    }

    public static ArrayList<Movie> data = new ArrayList<>();


    public static void loadData(String path) throws ParserConfigurationException {//передаём переменную(путь)

        File checknull = new File(path);//обработка файла
        if (checknull.length() == 0) {//пропустить пустой файл
            return;
        }

        Document doc = null;
        String allData = "";

        try {

            Reader in = new InputStreamReader(new FileInputStream(path));//чтение файла
            int chr;

            while (true) {//читает посимвольно, записывает в переменную
                chr = in.read();
                if (chr == -1) {
                    break;
                }
                allData += (char) chr;
            }
        } catch (Exception e) {
            System.out.println("File input read error!");
        }

        try {
            DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();//перевод в формат для парсера XML
            DocumentBuilder dBuilt = dbFact.newDocumentBuilder();
            doc = dBuilt.parse(new File(path));//парсинг


        } catch (SAXException sax) {
            System.err.println("Неверная конфигурация xml файла, проверьте исходный файл.");
            System.exit(-1);//прекращение работы программы
        } catch (IOException io) {
            System.err.println("Ошибка доступа к базе, проверьте и повторите заного.");
            System.exit(-1);
        }

        ArrayList<Movie> data = new ArrayList<>();

        NodeList nodeList = doc.getElementsByTagName("Movie");

        /// XML parsint to create Java

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i); //элемент для XML
            if (node.getNodeType() == Node.ELEMENT_NODE) {//проверка на элемент
                Element eElement = (Element) node; //тэги

                String[] coordinates = eElement.getElementsByTagName("coordinates").item(0).getTextContent().trim().split("\\s+");
                //извлечение: тег "coordinates", первый эл, текст, разбивает строчку на массив -> преобразует в массив
                String[] location = eElement.getElementsByTagName("location").item(0).getTextContent().trim().split("\\s+");
                String[] person = eElement.getElementsByTagName("person").item(0).getTextContent().trim().split("\\s+");
                String crdate1 = String.valueOf(eElement.getElementsByTagName("creationDate").item(0).getTextContent());
                LocalDateTime cdate = LocalDateTime.parse(crdate1, dateTimeFormatter);
                //дата в нужный формат

                Location ll = new Location();
                //создание локации(x,y,название)
                if (!location[0].equals("null")) {
                    ll.setX(Float.parseFloat(location[0]));
                } else {
                    ll.setX(null);
                }
                if (!location[1].equals("null")) {
                    ll.setY(Double.parseDouble(location[1]));
                } else {
                    ll.setY(null);
                }
                if (!location[2].equals("null")) {
                    ll.setName(location[2]);
                } else {
                    ll.setName(null);
                }

                if (location[0].equals("null") && location[1].equals("null") && location[2].equals("null")) {
                    ll = null;
                }

                Color eyeClr = Color.BLUE;
                if (person[2].equals("null")) {
                    eyeClr = null;
                } else {
                    eyeClr = Color.getColor(person[2]);
                }

                Movie flat = new Movie(
                        //создание Movie
                        Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()),
                        eElement.getElementsByTagName("name").item(0).getTextContent(),
                        new Coordinates(Float.parseFloat(coordinates[0]), Long.parseLong(coordinates[1])),
                        cdate,
                        Long.parseLong(eElement.getElementsByTagName("oscarsCount").item(0).getTextContent()),
                        Float.parseFloat(eElement.getElementsByTagName("budget").item(0).getTextContent()),
                        Double.valueOf(eElement.getElementsByTagName("usaBoxOffice").item(0).getTextContent()),
                        MpaaRating.valueOf(eElement.getElementsByTagName("mpaaRating").item(0).getTextContent()),

                        new Person(
                                person[0],
                                person[1],
                                eyeClr,
                                Country.valueOf(person[3]),
                                ll
                        )
                );

                data.add(flat);//добавление в коллекцию

            }
        }

        LinkedList<Long> ids = new LinkedList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getOscarsCount() <= 0) {
                System.out.println(data.get(i).getId() + " Oscar count must be > 0");
            }
            if (data.get(i).getBudget() <= 0) {
                System.out.println(data.get(i).getId() + " Budget must be > 0");
            }
            if (data.get(i).getUsaBoxOffice() <= 0) {
                System.out.println(data.get(i).getId() + " Usa Box Office must be > 0");
            }

            if (ids.contains(data.get(i).getId())) {
                //We can delete:
                data.remove(i);
                //data.get(i).setId(data.get(i).hashCode() + 1);

                //or say the Error!
                System.out.println("Data ids Error! Id must be unique!");
                //throw new IdException("Duplicate ID");
            }
            ids.add(data.get(i).getId());
        }

        XMLManager.data = data;//обновление коллекции на данном этапе, без сохранения
    }

    /**
     * Adds new Flat element to the collection.
     *
     * @param flat Flat object
     */
    public static void addElement(Movie flat) {
        XMLManager.data.add(flat);
    }

    public static void changeElement(int flatid, Movie flat) {
        for (Movie f : XMLManager.data) {
            if (f.getId() == flatid) {
                flat.setId(f.getId());
                flat.setCreationDate(f.getCreationDate());
                XMLManager.data.remove(f);
                break;
            }
        }
        XMLManager.data.add(flat);
    }

    public static void dropAll() {
        data.clear();
        try {
            writeToFile(ServerMain.filePath);
        } catch (Exception e) {
            System.err.println("Не удалось очистить файл: " + e.getMessage());
        }
    }
    /*
    wRITEtOfILE -
    * */

    /// Java parsint to create XML
    public static boolean writeToFile(String path) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        for (Movie flat : data) {
            Element movieElem = doc.createElement("Movie");
            rootElement.appendChild(movieElem);

            movieElem.appendChild(parseNode(doc, "id", String.valueOf(flat.getId())));
            movieElem.appendChild(parseNode(doc, "name", flat.getName()));

            Element coordsElem = doc.createElement("coordinates");
            coordsElem.appendChild(parseNode(doc, "x", String.valueOf(flat.getCoordinates().getX())));
            coordsElem.appendChild(parseNode(doc, "y", String.valueOf(flat.getCoordinates().getY())));
            movieElem.appendChild(coordsElem);

            Element personElem = doc.createElement("person");
            personElem.appendChild(parseNode(doc, "name", flat.getOperator().getName()));
            personElem.appendChild(parseNode(doc, "passportID", flat.getOperator().getPassportID()));
            personElem.appendChild(parseNode(doc, "eyeColor", flat.getOperator().getEyeColor() != null
                    ? flat.getOperator().getEyeColor().toString() : "null"));
            personElem.appendChild(parseNode(doc, "nationality", flat.getOperator().getNationality().toString()));

            Element locationElem = doc.createElement("location");
            if (flat.getOperator().getLocation() != null) {
                locationElem.appendChild(parseNode(doc, "x", String.valueOf(flat.getOperator().getLocation().getX())));
                locationElem.appendChild(parseNode(doc, "y", String.valueOf(flat.getOperator().getLocation().getY())));
                locationElem.appendChild(parseNode(doc, "name", flat.getOperator().getLocation().getName()));
            } else {
                locationElem.appendChild(parseNode(doc, "x", "null"));
                locationElem.appendChild(parseNode(doc, "y", "null"));
                locationElem.appendChild(parseNode(doc, "name", "null"));
            }
            personElem.appendChild(locationElem);
            movieElem.appendChild(personElem);

            movieElem.appendChild(parseNode(doc, "creationDate", flat.getCreationDate().format(dateTimeFormatter)));
            movieElem.appendChild(parseNode(doc, "oscarsCount", String.valueOf(flat.getOscarsCount())));
            movieElem.appendChild(parseNode(doc, "budget", String.valueOf(flat.getBudget())));
            movieElem.appendChild(parseNode(doc, "usaBoxOffice", String.valueOf(flat.getUsaBoxOffice())));
            movieElem.appendChild(parseNode(doc, "mpaaRating", flat.getMpaaRating().toString()));
        }

        // Сохраняем XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);

        return true;
    }

    /**
     * Parses node.
     *
     * @param doc
     * @param name
     * @param value
     * @return node
     */
    private static Node parseNode(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}