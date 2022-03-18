package org.someth2say.query;

import org.junit.jupiter.api.Test;
import org.someth2say.JacksException;
import org.someth2say.StreamUtils;
import org.someth2say.format.Xml;
import org.someth2say.format.Yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XPathTest {
    @Test
    public void simple_jsonpath() throws JacksException {
        final Xml xml = new Xml();
        final Object xmlObject = xml.stringToObject("""
                <xml>
                  <name>jacks</name>
                  <version>1.0</version>
                </xml>
                """);
        final Object result = QueryLang.XPATH.getSolver().query("/xml/name", xmlObject);

        final String resultString = StreamUtils.convertStreamToString(xml.objectToInputStream(result));

        assertEquals("\"jacks\"\n", resultString, ()->"Xpath extraction not successful.");
    }

}