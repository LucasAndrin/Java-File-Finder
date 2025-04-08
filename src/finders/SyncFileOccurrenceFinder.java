package finders;

import artifacts.FileOccurrence;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SyncFileOccurrenceFinder implements OccurrenceFinderContract {
    public final String dir;
    public final Semaphore permission = new Semaphore(2);

    public SyncFileOccurrenceFinder(String dir) {
        this.dir = dir;
    }

    public HashMap<Path, List<FileOccurrence>> execute(String search) throws InterruptedException {
        System.out.println("Searching for " + search);
        permission.acquire();

        try (DirectoryStream<Path> dirEntries = Files.newDirectoryStream(Paths.get(dir))) {
            HashMap<Path, List<FileOccurrence>> dirOccurrences = new HashMap<>();
            dirEntries.forEach(path -> {
                if (!Files.isDirectory(path)) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(path.toString()));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    List<String> lines = br.lines().toList();
                    String lowerCaseSearch = search.toLowerCase();
                    List<FileOccurrence> fileOccurrences = new ArrayList<>();
                    for (int i = 0; i < lines.size(); i++) {
                        String line = lines.get(i);
                        if (line.toLowerCase().contains(lowerCaseSearch)) {
                            fileOccurrences.add(new FileOccurrence(line, i));
                        }
                    }
                    dirOccurrences.put(path, fileOccurrences);
                }
            });
            return dirOccurrences;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            permission.release();
        }
    }
}
