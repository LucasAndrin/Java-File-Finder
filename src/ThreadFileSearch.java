import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;

public class ThreadFileSearch extends Thread {
    private final Path path;
    private final String search;

    public ThreadFileSearch(Path path, String search) {
        super(path.getFileName().toString());
        this.path = path;
        this.search = search;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(path.toString())
            );

            br.lines().forEach(line -> {
                if (line.contains(search)) {
                    System.out.println(getName() + ": " + line);
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
