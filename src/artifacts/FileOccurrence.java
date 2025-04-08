package artifacts;

public class FileOccurrence {
    private String text;
    private int line;

    public FileOccurrence(String text, int line) {
        setText(text);
        setLine(line);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
