package threads;

import artifacts.FileOccurrence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

public class ThreadFileFinder extends Thread {
    private final Path path;
    private final String search;
    private final List<FileOccurrence> occurrences;

    public ThreadFileFinder(Path path, String search, List<FileOccurrence> occurrences) {
        this.path = path;
        this.search = search;
        this.occurrences = occurrences;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path.toString()));
            List<String> lines = br.lines().toList();
            String lowerCaseSearch = search.toLowerCase();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.toLowerCase().contains(lowerCaseSearch)) {
                    occurrences.add(new FileOccurrence(line, i));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
