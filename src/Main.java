import finders.AsyncFileOccurrenceFinder;
import finders.OccurrenceFinderContract;
import finders.SyncFileOccurrenceFinder;
import threads.ThreadDirFileFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String folder = "files";
        List<String> searches = new ArrayList<>();
        searches.add("Gladis Lopes");
        searches.add("Laerte Michels");
        searches.add("Giovani Correa");

        System.out.println("Async search!");
        search(searches, new AsyncFileOccurrenceFinder(folder));

        System.out.println("#############################");

        System.out.println("Sync search!");
        search(searches, new SyncFileOccurrenceFinder(folder));
    }

    public static void search(List<String> searches, OccurrenceFinderContract finder) {
        List<Thread> threads = new ArrayList<>();
        searches.forEach(search -> threads.add(new ThreadDirFileFinder(search, finder)));
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}