package org.redhattraining;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yqTest {

    @Test
    public void runTest() {
        int exitcode = new yq().run("-f", "src/test/resources/dco.yml", "-q", "$.chapters[*].chapter_word");
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