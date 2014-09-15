/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.dedup.xml;

import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.xml.sax.*;
import org.w3c.dom.*;

/**
 *
 * @author ajuhe
 */
public class parser {

    String pathxsl = "C:\\projectes\\duplicator\\resources\\xsl\\texte_uoc.xsl";
    Document doc;
    InputStream streamXML;

    public parser(InputStream streamXML, String xsl) {
        this.pathxsl = xsl;
        this.streamXML = streamXML;
    }

    public parser(InputStream streamXML) {
        this.streamXML = streamXML;
    }

    public String extreuDades() {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!

        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            this.doc = builder.parse(this.streamXML);
        } catch (ParserConfigurationException e) {
            System.out.println("Error de transformació xml." + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error de transformació xml." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de transformació xml." + e.getMessage());
        }
        return this.executeXSL();
    }

    private String executeXSL() {
        File f = new File(this.pathxsl);
        if (f.exists()) {
            DOMSource xmlSource = new DOMSource(this.doc);

            Source xsltSource = new StreamSource(f);
            StringWriter resultatTransformacio = new StringWriter();
            Result bufferResultado = new StreamResult(resultatTransformacio);
            try {
                TransformerFactory factoriaTrans = TransformerFactory.newInstance();
                javax.xml.transform.Transformer transformador = factoriaTrans.newTransformer(xsltSource);
                transformador.transform(xmlSource, bufferResultado);
            } catch (TransformerConfigurationException e) {
                System.out.println("Error de transformació de fragments." + e.getMessage());
            } catch (TransformerException e) {
                System.out.println("Error de transformació de fragments." + e.getMessage());
            }
            return resultatTransformacio.toString();
        } else {
            System.out.println("No trobo la xsl");
            return "Error d'extracció text.";

        }
    }

    public InputStream StringToStream(String text) {
        try {
            InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));
            return is;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
