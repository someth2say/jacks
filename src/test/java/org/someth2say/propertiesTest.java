package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.someth2say.formats.Properties;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class propertiesTest {

    @Test
    public void transformTest() throws yqException {
        Properties props = new Properties();
        String propsString = "1=One\n2=Two\n3=Three";
        InputStream is = new ByteArrayInputStream(propsString.getBytes());
        Object propsObject = props.inputStreamToObject(is);
        InputStream propsIs = props.objectToInputStream(propsObject);
        String newPropsString = yq.convertStreamToString(propsIs);
        assertEquals(propsString.trim(), newPropsString.trim(), () -> "Properties does not transforms to stream correctly.");
    }

    @Test
    public void serializeTest() throws yqException, IOException {
        final Object propsObj = new Properties()
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.props")));
        Object array = JsonPath.query("$.menu.popup.menuitem[*].value", propsObj);
        InputStream propsIs = new Properties().objectToInputStream(array);
        String newPropsString = yq.convertStreamToString(propsIs);

        assertTrue(newPropsString.contains("New"));
        assertTrue(newPropsString.contains("Open"));
        assertTrue(newPropsString.contains("Close"));
    }

    @Test
    public void deserializeTest() throws yqException, IOException {
        Path path = Paths.get("src/test/resources/menu.props");
        final Object propsObj = new Properties().inputStreamToObject(Files.newInputStream(path));
        InputStream propsIs = new Properties().objectToInputStream(propsObj);
        java.util.Properties p1 = new java.util.Properties();
        p1.load(propsIs);
        java.util.Properties p2 = new java.util.Properties();
        p2.load(Files.newInputStream(path));
        assertEquals(p1, p2, ()->"Properties loaded should match");
    }

}