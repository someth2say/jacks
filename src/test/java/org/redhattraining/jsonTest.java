package org.redhattraining;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.redhattraining.yqException;

import io.quarkus.test.junit.QuarkusTest;
import static org.redhattraining.convert.*;

@QuarkusTest
public class jsonTest {

    @Test
    public void jsonPath() throws IOException, yqException {
        
        final String jsonPath="$.chapters[*].chapter_word";
        final InputStream json = convertYamlToJson(Files.newInputStream(Paths.get("src/test/resources/dco.yml")));

		final InputStream chapters = org.redhattraining.json.jsonPath(jsonPath, json);
        chapters.transferTo(System.out);
        System.out.flush();
    }

    
}