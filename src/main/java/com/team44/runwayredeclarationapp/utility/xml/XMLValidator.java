package com.team44.runwayredeclarationapp.utility.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * The XML validator class for validating an XML file against an XSD schema
 */
public class XMLValidator {

    private static final Logger logger = LogManager.getLogger(XMLValidator.class);

    /**
     * Schema factory for XML
     */
    private final SchemaFactory factory = SchemaFactory.newInstance(
        XMLConstants.W3C_XML_SCHEMA_NS_URI);


    /**
     * Validate an XML file against an XSD schema file
     *
     * @param xsdFile the XSD schema file
     * @param xmlFile the XML file
     * @throws SAXException when there is a parsing error
     * @throws IOException  when there are other input/output errors from reading the file
     */
    public void validateWithSchema(URL xsdFile, File xmlFile) throws SAXException, IOException {
        // Try validate
        try {
            // Create the validator with the schema
            Validator validator = factory.newSchema(xsdFile).newValidator();

            // Validate the XML file
            var xmlStreamSource = new StreamSource(xmlFile);
            validator.validate(xmlStreamSource);

        } catch (SAXException e) {
            // Parsing error
            logger.error("Parsing error: " + e.getMessage());
            throw e;

        } catch (IOException e) {
            // Input output error
            logger.error("IO error on validating: " + e.getMessage());
            throw e;
        }
    }
}
