import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AsyncDirFileSearcher {
    public final String dir;
    public final Semaphore permission = new Semaphore(2);

    public AsyncDirFileSearcher(String dir) {
        this.dir = dir;
    }

    public void execute(String search) throws InterruptedException {
        System.out.println("Searching for " + search);
        permission.acquire();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            List<ThreadFileSearch> searches = new ArrayList<>();
            stream.forEach(path -> {
                if (!Files.isDirectory(path)) {
                    searches.add(new ThreadFileSearch(path, search));
                }
            });

            searches.forEach(ThreadFileSearch::run);
            searches.forEach(thread -> {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            permission.release();
        }
    }
}
