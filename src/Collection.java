import java.util.ArrayList;
import java.util.List;

public class Collection {
    private String name;
    private List<Photo> photos;

    public Collection(String name) {
        this.name = name;
        this.photos = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    @Override
    public String toString() {
        return name;
    }
}