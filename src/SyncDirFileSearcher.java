import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SyncDirFileSearcher {
    public final String dir;
    public final Semaphore permission = new Semaphore(2);

    public SyncDirFileSearcher(String dir) {
        this.dir = dir;
    }

    public void execute(String search) throws InterruptedException {
        System.out.println("Searching for " + search);
        permission.acquire();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            stream.forEach(path -> {
                if (!Files.isDirectory(path)) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(path.toString()));

                        List<String> lines = br.lines().toList();
                        IntStream.range(0, lines.size()).forEach(index -> {
                            String line = lines.get(index);
                            if (line.toLowerCase().contains(search.toLowerCase())) {
                                System.out.printf("%s - row: %s - %s", path.getFileName(), index, line);
                            }
                        });
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            permission.release();
        }
    }
}
