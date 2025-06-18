import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photo {
    private String title;
    private File imageFile;
    private Date date;
    private String description;
    private List<String> tags;

    public Photo(String title, File imageFile, Date date, String description) {
        this.title = title;
        this.imageFile = imageFile;
        this.date = date;
        this.description = description;
        this.tags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public String toString() {
        return title;
    }
}