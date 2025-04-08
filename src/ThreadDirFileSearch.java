public class ThreadDirFileSearch extends Thread {
    public final String search;
    public final AsyncDirFileSearcher searcher;

    public ThreadDirFileSearch(String search, AsyncDirFileSearcher searcher) {
        this.search = search;
        this.searcher = searcher;
    }

    @Override
    public void run() {
        try {
            searcher.execute(search);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
