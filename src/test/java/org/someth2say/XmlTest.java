package org.someth2say;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.someth2say.formats.Xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class XmlTest {

    @Test
    public void transformTest() throws JacksException {
        Xml xml = new Xml();
        String xmlString = "<root>\n<name>value</name>\n</root>";
        InputStream is = new ByteArrayInputStream(xmlString.getBytes());
        Object xmlObject = xml.inputStreamToObject(is);
        InputStream xmlIs = xml.objectToInputStream(xmlObject);
        String newXmlString = StreamUtils.convertStreamToString(xmlIs);
        assertEquals(linearizeXML(xmlString), linearizeXML(newXmlString), () -> "XML does not transforms to stream correctly.");
    }

    @Test
    public void rootless_objects_serialize_with_root_xml() throws JacksException {
        Xml xml = new Xml();
        InputStream xmlIs = xml.objectToInputStream(Map.of("name", "value"));
        String newXmlString = StreamUtils.convertStreamToString(xmlIs);
        assertEquals(linearizeXML("<xml><name>value</name></xml>"), linearizeXML(newXmlString), () -> "Object without root element does not use `xml` as root");
    }

    @Test
    public void deserialization_infers_arrays() throws JacksException {
        Xml xml = new Xml();
        String xmlString = "<root>\n<name>value</name>\n<name>value2</name>\n</root>";
        Object xmlObject = xml.stringToObject(xmlString);
        InputStream xmlIs = xml.objectToInputStream(xmlObject);
        String newXmlString = StreamUtils.convertStreamToString(xmlIs);
        assertEquals(linearizeXML(xmlString), linearizeXML(newXmlString), () -> "XML does not transforms to stream correctly.");
    }

    String linearizeXML(final String xml) {
        return xml.lines().map(String::trim).collect(Collectors.joining());
    }
}