package org.springframework.beans.factory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Anton Salnikov
 */
public class PropertiesFileInitializer {
    public java.util.Map<String, String> initialize(String fileName) {
        String path = ClassLoader.getSystemClassLoader().getResource(fileName).getPath();
        try {
            Stream<String> lines = new BufferedReader(new FileReader(path)).lines();
            return lines.map(line -> line.split("="))
                    .collect(Collectors.toMap(arr -> arr[0].trim(), arr -> arr[1].trim()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
