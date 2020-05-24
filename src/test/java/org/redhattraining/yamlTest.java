package org.redhattraining;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.redhattraining.yqException;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class yamlTest {

    @Test
    public void yamlPath() throws IOException, yqException {
        final String jsonPath="$.chapters[*].chapter_word";
        final InputStream yaml = Files.newInputStream(Paths.get("src/test/resources/dco.yml"));
		final InputStream chapters = org.redhattraining.yaml.yamlPath(jsonPath, yaml);
        chapters.transferTo(System.out);
        System.out.flush();
    }


    
}