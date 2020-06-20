package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yqTest {

    @Test
    public void runTestYaml() throws yqException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        int exitcode = new yq().run("-f", "src/test/resources/menu.yaml", "-q", "$.menu.popup.menuitem[*].value");
        assertEquals(0, exitcode, () -> "Basic run failed.");

        List<?> list = new Yaml().stringToObject(baos.toString(), List.class);
                assertTrue(list.containsAll(List.of("New","Open","Close")),()->"JsonPath results on yaml file unexpected");
    }

    @Test
    public void runTestJson() throws yqException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        int exitcode = new yq().run("-f", "src/test/resources/menu.json", "-q", "$.menu.popup.menuitem[*].value");
        assertEquals(0, exitcode, () -> "Basic run failed.");

        List<?> list = new Json().stringToObject(baos.toString(), List.class);
        assertTrue(list.containsAll(List.of("New","Open","Close")),()->"JsonPath results on json file unexpected");
    }

    @Test
    public void runMissingFile() {
        int run = new yq().run("-f", "src/test/resources/missing.yml", "-q", "$.chapters[*].chapter_word");
        assertNotEquals(0, run, ()->"Running on a missing file must return non-zero");
    }

    @Test
    public void runInvalidFile() {
        int run = new yq().run("-f", "src/main/resources/application.properties", "-q", "$.chapters[*].chapter_word");
        assertNotEquals(0, run, ()->"Running on a unknown-format file must return non-zero");
    }

    @Test
    public void invalidJsonPath() {
        int run = new yq().run("-f", "src/main/resources/application.properties", "-q","not.json");
        assertNotEquals(0, run, ()->"Unparseable JsonPath should return non-zero");
    }
}