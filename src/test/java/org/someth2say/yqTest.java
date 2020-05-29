package org.someth2say;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yqTest {

    @Test
    public void runTestYaml() {
        int exitcode = new yq().run("-f", "src/test/resources/menu.yaml", "-q", "$.menu.popup.menuitem[*].value");
        assertEquals(0, exitcode, () -> "Basic run failed.");
    }


    @Test
    public void runTestJson() {
        int exitcode = new yq().run("-f", "src/test/resources/menu.json", "-q", "$.menu.popup.menuitem[*].value");
        assertEquals(0, exitcode, () -> "Basic run failed.");
    }

    @Test
    public void runMissingFile() {
        new yq().run("-f", "src/test/resources/missing.yml", "-q", "$.chapters[*].chapter_word");
    }

    @Test
    public void runInvalidFile() {
        new yq().run("-f", "src/main/resources/application.properties", "-q", "$.chapters[*].chapter_word");
    }

    @Test
    public void invalidJsonPath() {
        new yq().run("-f", "src/main/resources/application.properties", "-q",
                "not.json");
    }



}