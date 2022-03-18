package org.someth2say.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.someth2say.JacksException;
import org.someth2say.StreamUtils;
import org.someth2say.format.Txt;
import org.someth2say.format.Yaml;

import io.quarkus.test.junit.QuarkusTest;
import org.someth2say.query.QueryLang;

@QuarkusTest
public class txtTest {

    @Test
    public void transformTest() throws JacksException {
        Txt plain = new Txt();
        String plainString = "One\nTwo\nThree";
        InputStream is = new ByteArrayInputStream(plainString.getBytes());
        Object plainObject = plain.inputStreamToObject(is);
        InputStream plainIs = plain.objectToInputStream(plainObject);
        String newPlainString = StreamUtils.convertStreamToString(plainIs);
        assertEquals(plainString, newPlainString, () -> "Plain does not transforms to stream correctly.");
    }

    @Test
    public void serializeTest() throws JacksException, IOException {
        final Object yamlObj = new Yaml()
                .inputStreamToObject(Files.newInputStream(Paths.get("src/test/resources/menu.yaml")));
        Object array = QueryLang.JSONPATH.getSolver().query("$.menu.popup.menuitem[*].value", yamlObj);
        InputStream plainIs = new Txt().objectToInputStream(array);
        String newPlainString = StreamUtils.convertStreamToString(plainIs);

        assertTrue(newPlainString.contains("New"));
        assertTrue(newPlainString.contains("Open"));
        assertTrue(newPlainString.contains("Close"));
    }

    @Test
    public void deserializeTest() throws JacksException, IOException {
        Path path = Paths.get("src/test/resources/menuItems.txt");
        InputStream newInputStream = Files.newInputStream(path);
        final Object plainObj = new Txt().inputStreamToObject(newInputStream);
        InputStream plainIs = new Txt().objectToInputStream(plainObj);
        String newPlainString = StreamUtils.convertStreamToString(plainIs);

        byte[] readAllBytes = Files.readAllBytes(path);
        assertEquals(newPlainString.trim(), new String(readAllBytes).trim(),
                () -> "Transforming plain to plain should be the same.");

    }

}