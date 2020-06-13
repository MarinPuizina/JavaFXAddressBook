package javafx.xml;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.database.DatabaseHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author marin
 */
public class XMLHandler {

    private final String PERSONS_XML = "persons.xml";

    DatabaseHandler databaseHandler;
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    /**
     * Adding Elements to xml file
     * 
     * @param firstName
     * @param lastName
     * @param email 
     */
    public void addElement(String firstName, String lastName, String email) {

        try {

            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PERSONS_XML);

            NodeList childNodes = doc.getChildNodes();
            Element rootNode = (Element) childNodes.item(0);

            Element personElement = doc.createElement("person");
            // Adding email as an ID attribute so that application
            // doesn't create additional Elements with the same Email
            personElement.setAttribute("email", email);

            // First Name
            Element firstNameElement = doc.createElement("firstName");
            firstNameElement.appendChild(doc.createTextNode(firstName));
            personElement.appendChild(firstNameElement);

            // Last Name
            Element lastNameElement = doc.createElement("lastName");
            lastNameElement.appendChild(doc.createTextNode(lastName));
            personElement.appendChild(lastNameElement);

            rootNode.appendChild(personElement);

            writeContentIntoXml(doc);

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException | SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Writing content into XML file
     * 
     * @param doc
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    private void writeContentIntoXml(Document doc) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(PERSONS_XML);
        transformer.transform(source, result);

        System.out.println("Updated the " + PERSONS_XML);

    }

    /**
     * Looping through the XML Elements Persisting them into a database Deleting
     * them from XML
     */
    public void persistXMLElements() {

        String firstName;
        String lastName;
        String email;

        try {

            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PERSONS_XML);

            NodeList childNodes = doc.getChildNodes();
            Element rootNode = (Element) childNodes.item(0);
            System.out.println("root node --> " + rootNode.getNodeName());

            NodeList personList = rootNode.getElementsByTagName("person");
            Element person;

            for (int i = 0; i < personList.getLength(); i++) {

                person = (Element) personList.item(i);
                email = person.getAttribute("email");

                NodeList firstNameNodes = person.getElementsByTagName("firstName");
                Element firstNameElement = (Element) firstNameNodes.item(0);
                firstName = firstNameElement.getTextContent();

                NodeList lastNameNodes = person.getElementsByTagName("lastName");
                Element lastNameElement = (Element) lastNameNodes.item(0);
                lastName = lastNameElement.getTextContent();

                System.out.printf("Email: %s First Name: %s Last Name: %s", email, firstName, lastName);
                System.out.println();

                databaseHandler = DatabaseHandler.getInstance();

                if (!databaseHandler.findUser(email)) {
                    // Persiting data into database
                    databaseHandler.execAction(firstName, lastName, email);
                }

                deleteElement(doc, rootNode, person);

            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Deleting Element from XML file
     * 
     * @param doc
     * @param rootNode
     * @param person 
     */
    private void deleteElement(Document doc, Element rootNode, Element person) {

        try {
            rootNode.removeChild(person);
            writeContentIntoXml(doc);
        } catch (TransformerFactoryConfigurationError | TransformerException ex) {
            Logger.getLogger(XMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
