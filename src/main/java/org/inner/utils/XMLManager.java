package org.inner.utils;

import org.data.Coordinates;
import org.data.Location;
import org.data.Movie;
import org.data.Person;
import org.data.enums.Color;
import org.data.enums.Country;
import org.data.enums.MpaaRating;
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
        if(checknull.length() == 0){//пропустить пустой файл
            return;
        }

        Document doc = null;
        String allData = "";

        try {

            Reader in = new InputStreamReader(new FileInputStream(path));//чтение файла
            int chr = 0;

            while (true){//читает посимвольно, записывает в переменную
                chr = in.read();
                if(chr == -1){
                    break;
                }
                allData+=(char) chr;
            }

            /*System.out.println(allData);*/

        }catch (Exception e){
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
                if(!location[0].equals( "null" )) {
                    ll.setX(Float.parseFloat(location[0]));
                }else{
                    ll.setX(null);
                }
                if(!location[1].equals( "null")) {
                    ll.setY(Double.parseDouble(location[1]));
                }else{
                    ll.setY(null);
                }
                if(!location[2].equals( "null")) {
                    ll.setName(location[2]);
                }else{
                    ll.setName(null);
                }

                if(location[0].equals( "null" ) && location[1].equals( "null" ) && location[2].equals( "null" )){
                    ll = null;
                }

                Color eyeClr = Color.BLUE;
                if (person[2].equals("null")){
                    eyeClr = null;
                }else{
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
            if(data.get(i).getOscarsCount() <= 0){
                System.out.println(data.get(i).getId() + " Oscar count must be > 0");
            }
            if(data.get(i).getBudget() <= 0){
                System.out.println(data.get(i).getId() + " Budget must be > 0");
            }
            if(data.get(i).getUsaBoxOffice() <= 0){
                System.out.println(data.get(i).getId() + " Usa Box Office must be > 0");
            }

            if(ids.contains(data.get(i).getId())){
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
     * @param flat Flat object
     */
    public static void addElement(Movie flat){
        XMLManager.data.add(flat);
    }
    public static void changeElement(int flatid, Movie flat){
        for(Movie f : XMLManager.data){
            if (f.getId() == flatid){
                flat.setId(f.getId());
                flat.setCreationDate(f.getCreationDate());
                XMLManager.data.remove(f);
                break;
            }
        }
        XMLManager.data.add(flat);
    }

    public static void dropAll(){
        XMLManager.data.clear();
    }
    /*
    wRITEtOfILE -
    * */
    /// Java parsint to create XML
    public static boolean writeToFile(String path) throws TransformerException, ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element rootElement = doc.createElementNS(null, "data");//корень data
        doc.appendChild(rootElement);//добавляет в конец списка, корневой элемент единственный

        ArrayList<Movie> flats = XMLManager.getData();

        if(flats.isEmpty()){//очистка файла, если нет данных
            try {
                new FileWriter(path, false).close();
            }
            catch (IOException io){
                System.err.println("зменения не сохранены. Ошибка доступа к файлу, проверьте файл и попробуйте сохранить ещё раз.");
                return false;
            }
        }

        for(Movie flat: flats) {

            Element nodee = doc.createElement("Movie");
            Node q = rootElement.appendChild(nodee);

            q.appendChild(parseNode(doc, "id", String.valueOf(flat.getId())));
            q.appendChild(parseNode(doc, "name", flat.getName()));

            Element corelem = doc.createElement("coordinates");
            Node coordinates = q.appendChild(corelem);
            coordinates.appendChild(parseNode(doc, "x", String.valueOf(flat.getCoordinates().getX())));
            coordinates.appendChild(parseNode(doc, "y", String.valueOf(flat.getCoordinates().getY())));

            Element corelem2 = doc.createElement("person");
            Node person = q.appendChild(corelem2);
            person.appendChild(parseNode(doc, "name", String.valueOf(flat.getOperator().getName())));
            person.appendChild(parseNode(doc, "passportID", String.valueOf(flat.getOperator().getPassportID())));
            if(flat.getOperator().getEyeColor() != null) {
                person.appendChild(parseNode(doc, "eyeColor", String.valueOf(flat.getOperator().getEyeColor().toString())));
            }else{
                person.appendChild(parseNode(doc, "eyeColor", "null"));
            }
            person.appendChild(parseNode(doc, "nationality", String.valueOf(flat.getOperator().getNationality().toString())));

            Element corelem1 = doc.createElement("location");
            Node loc = person.appendChild(corelem1);
            if(flat.getOperator().getLocation() != null) {
                loc.appendChild(parseNode(doc, "x", String.valueOf(flat.getOperator().getLocation().getX())));
                loc.appendChild(parseNode(doc, "y", String.valueOf(flat.getOperator().getLocation().getY())));
                loc.appendChild(parseNode(doc, "name", String.valueOf(flat.getOperator().getLocation().getName())));
            }
            else{
                loc.appendChild(parseNode(doc, "x","null"));
                loc.appendChild(parseNode(doc, "y", "null"));
                loc.appendChild(parseNode(doc, "name", "null"));
            }

            /*person.appendChild(parseNode(doc, "location", String.valueOf(flat.getOperator().getLocation())));
*/

            q.appendChild(parseNode(doc, "creationDate", String.valueOf(flat.getCreationDate().format(dateTimeFormatter))));
            q.appendChild(parseNode(doc, "oscarsCount", String.valueOf(flat.getOscarsCount())));
            q.appendChild(parseNode(doc, "budget", String.valueOf(flat.getBudget())));
            q.appendChild(parseNode(doc, "usaBoxOffice", String.valueOf(flat.getUsaBoxOffice())));
            q.appendChild(parseNode(doc, "mpaaRating", String.valueOf(flat.getMpaaRating().toString())));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();//преобразование для XML
            Transformer transformer = transformerFactory.newTransformer();//объект для преобразования

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//клюс с отступами
            DOMSource source = new DOMSource(doc);//досткп для трансформера к xml

            try {
                /*StreamResult file = new StreamResult(new PrintWriter(path));
                transformer.transform(source, file);//xml в файл**/

                String all = doc.getTextContent();

                try(BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(path))){ //открывает файл
                    byte[] byts = all.getBytes();//читает из переменной, записывает по байтайм в файл
                    buf.write(byts);
                    buf.flush();
                }catch (Exception e){
                    /*System.out.println("Write in file Error!");*/
                }

                StreamResult file = new StreamResult(new PrintWriter(path));
                transformer.transform(source, file);

            }
            catch (Exception jxtt){//нет файла или прав
                System.err.println("File Error or Access file error!");
                return false;
            }

        }
        return true;
    }

    /**
     * Parses node.
     * @param doc
     * @param name
     * @param value
     * @return node
     *
     */
    private static Node parseNode(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}