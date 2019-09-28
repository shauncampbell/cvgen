package scot.shaun.cvgen.model;

import java.util.Map;

public class CvModel {
    private Theme theme;
    private CvContent content;

    public CvContent getContent() {
        return content;
    }

    public void setContent(CvContent content) {
        this.content = content;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
