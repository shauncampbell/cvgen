package scot.shaun.cvgen.model;

import java.util.Map;

public class CvModel {
    private Theme theme;
    private Map<String, Object> content;

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
