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

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class plainTest {

    @Test
    public void transformTest() throws yqException {
        Plain plain = new Plain();
        String plainString = "One\nTwo\nThree";
        InputStream is = new ByteArrayInputStream(plainString.getBytes());
        Object plainObject = plain.inputStreamToObject(is);
        InputStream plainIs = plain.objectToInputStream(plainObject);
        String newPlainString = yq.convertStreamToString(plainIs);
        assertEquals(plainString, newPlainString, ()->"Plain does not transforms to stream correctly.");
    }

    @Test
    public void serializeTest() throws yqException, IOException {
        final Object yamlObj = new Yaml().inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.yaml")));
        Object array = JsonPath.query("$.menu.popup.menuitem[*].value", yamlObj);
        InputStream plainIs = new Plain().objectToInputStream(array);
        String newPlainString = yq.convertStreamToString(plainIs);

        assertTrue(newPlainString.contains("New"));
        assertTrue(newPlainString.contains("Open"));
        assertTrue(newPlainString.contains("Close"));
    }

    @Test
    public void deserializeTest() throws yqException, IOException {
        Path path= Paths.get("src/main/resources/application.properties");
        InputStream newInputStream = Files.newInputStream(path);
        final Object plainObj = new Plain().inputStreamToObject(newInputStream);
        InputStream plainIs = new Plain().objectToInputStream(plainObj);
        String newPlainString = yq.convertStreamToString(plainIs);

        byte[] readAllBytes = Files.readAllBytes(path);
        assertEquals(newPlainString+System.lineSeparator(), new String(readAllBytes), ()->"Transforming plain to plain should be the same.");
        
    }


}