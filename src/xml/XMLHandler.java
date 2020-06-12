package xml;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    //TODO 
    //void findLastElement()
    public void addElement(String firstName, String lastName, String email) {

        try {

            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(PERSONS_XML);

            NodeList childNodes = doc.getChildNodes();
            Element rootNode = (Element) childNodes.item(0);
            System.out.println("root node --> " + rootNode.getNodeName());

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

    //deleteElement()

    private void writeContentIntoXml(Document doc) throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(PERSONS_XML);
        transformer.transform(source, result);
        
        System.out.println("Added element to the " + PERSONS_XML);
        
    }
    
}
