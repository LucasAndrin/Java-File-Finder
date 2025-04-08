import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<String> searches = new ArrayList<>();
        searches.add("Gladis");
        searches.add("Laerte");
        searches.add("Giovani");

        AsyncDirFileSearcher searcher = new AsyncDirFileSearcher("files");
        for (String search : searches) {
            ThreadDirFileSearch asyncSearch = new ThreadDirFileSearch(search, searcher);
            asyncSearch.start();
        }
    }
}