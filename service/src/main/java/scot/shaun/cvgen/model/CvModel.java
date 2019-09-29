package scot.shaun.cvgen.model;

public class CvModel {
    private Theme theme;
    private CurriculumVitae content;

    public CurriculumVitae getContent() {
        return content;
    }

    public void setContent(CurriculumVitae content) {
        this.content = content;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }
}
