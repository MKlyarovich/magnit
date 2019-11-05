package com.company.app.util;

import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Utils {

    private Utils() {
    }

    public static Path loadResourceToString(String path) {
        try {
            return Paths.get(ClassLoader.getSystemResource("xslt/" + path).toURI());
        } catch (URISyntaxException e) {
            String errorMessage = String.format("Could not load resource: %s. %s", path, e);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}