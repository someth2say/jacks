package org.someth2say.query;

import org.someth2say.JacksException;
import org.someth2say.format.Format;
import org.someth2say.format.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;

public class XPath implements QuerySolver{
    @Override
    public Object query(final String query, final Object obj) throws JacksException {
        return null;
    }

    @Override
    public <T> T query(final String query, final Object obj, final Class<T> valueType) throws JacksException {
        // 1) Obtain the XML representation of the object
        InputStream xmlStr = Format.XML.getMapper().objectToInputStream(obj);
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlDocument = builder.parse(xmlStr);
            javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
            final Object nodeSet = xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList nodeList = (NodeList) nodeSet;



        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }


        return null;


    }
}
