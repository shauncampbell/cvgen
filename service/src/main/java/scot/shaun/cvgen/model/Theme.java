package scot.shaun.cvgen.model;

import java.util.List;
import java.util.Map;

public class Theme
{
    private List<String> imports;
    private Map<String, String> colours;
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public Map<String, String> getColours() {
        return colours;
    }

    public void setColours(Map<String, String> colours) {
        this.colours = colours;
    }
}
