package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.LevelLayout;
import ru.mipt.bit.platformer.model.LevelSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/** Читает расстановку уровня из текстового файла в ресурсах. */
public class FileLevelSource implements LevelSource {
    private final String resourceName;
    private final TextLevelParser parser;

    public FileLevelSource(String resourceName) {
        this(resourceName, new TextLevelParser());
    }

    public FileLevelSource(String resourceName, TextLevelParser parser) {
        this.resourceName = resourceName;
        this.parser = parser;
    }

    @Override
    public LevelLayout load() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new IllegalStateException("Level file not found: " + resourceName);
            }
            List<String> lines = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.toList());
            return parser.parse(lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read level file: " + resourceName, e);
        }
    }
}