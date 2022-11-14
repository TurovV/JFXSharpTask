package com.sharp.jfxsharptask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectoryWatcher {
    private final ArrayList<File> files = new ArrayList<>();
    private File dir;
    private List<String> words;

    DirectoryWatcher(Path path) {
        dir = new File(path.toUri());
        files.addAll(Arrays.asList(Objects.requireNonNull(dir.listFiles(File::isFile))));
        words = null;
    }

    public long lastModified() {
        return this.dir.lastModified();
    }


    public List<File> getFilesWithUsages() {
        return files.stream().filter(f -> {
            var sequences = readFile(f);
            return sequences != null && containsAny(sequences);
        }).toList();
    }

    public void changeWords(List<String> list) {
        this.words = list;
    }
    private boolean containsAny(List<String> list) {
        if (words != null) {
            for (var word : words) {
                if (list.contains(word)) return true;
            }
        }
        return false;
    }

    private List<String> readFile(File file) {
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
                String ls = System.getProperty("line.separator");
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append(ls);
                }

                var text = stringBuilder.toString();
                List<String> allMatches = new ArrayList<String>();
                Matcher m = Pattern.compile("[A-Za-z]+") // TODO REGEX
                        .matcher(text);
                while (m.find()) {
                    allMatches.add(m.group());
                }
                return allMatches;
            }
        } catch (IOException ignored) {
            return null;
        }
    }
}
