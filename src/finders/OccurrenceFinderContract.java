package finders;

import artifacts.FileOccurrence;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public interface OccurrenceFinderContract {
    public HashMap<Path, List<FileOccurrence>> execute(String search) throws InterruptedException;
}
