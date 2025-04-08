package finders;

import artifacts.FileOccurrence;
import threads.ThreadFileFinder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AsyncFileOccurrenceFinder implements OccurrenceFinderContract {
    public final String dir;
    public final Semaphore permission = new Semaphore(2);

    public AsyncFileOccurrenceFinder(String dir) {
        this.dir = dir;
    }

    public HashMap<Path, List<FileOccurrence>> execute(String search) throws InterruptedException {
        System.out.println("Searching for " + search);
        permission.acquire();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            List<Thread> fileFinders = new ArrayList<>();
            HashMap<Path, List<FileOccurrence>> dirOccurrences = new HashMap<>();
            stream.forEach(path -> {
                if (!Files.isDirectory(path)) {
                    List<FileOccurrence> fileOccurrences = new ArrayList<>();
                    Thread fileFinder = new ThreadFileFinder(path, search, fileOccurrences);

                    fileFinder.start();
                    fileFinders.add(fileFinder);
                    dirOccurrences.put(path, fileOccurrences);
                }
            });

            fileFinders.forEach(finder -> {
                try {
                    finder.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
