package com.sample.starter;

import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourcesTest {
    @Test
    void getResource() {
        var resource = Resources.getResource("com/sample/starter/test.txt");
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    void getExistingFile() {
        var file = Resources.getFile("classpath:com/sample/starter/test.txt");
        assertNotNull(file);
        assertTrue(file.exists());
        assertEquals("test.txt", file.getName());
    }

    @Test
    void getNonExistingFile() {
        assertThrows(IllegalArgumentException.class, () -> Resources.getFile("test.txt"));
    }

    @Test
    void getStream() throws IOException {
        var stream = Resources.getStream("classpath:com/sample/starter/test.txt");
        assertNotNull(stream);
        var content = StreamUtils.copyToString(stream, StandardCharsets.UTF_8);
        assertTrue(content.contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
    }

    @Test
    void getBytes() {
        var bytes = Resources.getBytes("classpath:com/sample/starter/test.jpg");
        assertNotNull(bytes);
        assertEquals(99788, bytes.length);
    }

}
