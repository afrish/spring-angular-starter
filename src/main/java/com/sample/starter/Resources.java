package com.sample.starter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Resources {

    private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public static Resource getResource(String path) {
        return resolver.getResource(path);
    }

    public static InputStream getStream(String path) {
        try {
            return getResource(path).getInputStream();
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource cannot be read: " + path, e);
        }
    }

    public static byte[] getBytes(String path) {
        try {
            return StreamUtils.copyToByteArray(getStream(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource cannot be read: " + path, e);
        }
    }

    public static File getFile(String path) {
        try {
            return getResource(path).getFile();
        } catch (IOException e) {
            throw new IllegalArgumentException("File was not found: " + path, e);
        }
    }
}
