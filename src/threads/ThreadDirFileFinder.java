package threads;

import finders.OccurrenceFinderContract;

public class ThreadDirFileFinder extends Thread {
    public final String search;
    public final OccurrenceFinderContract finder;

    public ThreadDirFileFinder(String search, OccurrenceFinderContract finder) {
        this.search = search;
        this.finder = finder;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            var occurrences = finder.execute(search);
            occurrences.forEach((path, fileOccurrences) ->
                fileOccurrences.forEach(fileOccurrence ->
                    System.out.printf("%s - %s (%s): %s\n",
                            search,
                            path.getFileName(),
                            fileOccurrence.getLine(),
                            fileOccurrence.getText()
                    )
                )
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("Search for %s was executed in %sms!\n", search, endTime - startTime);
    }
}
