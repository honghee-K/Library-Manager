package thws.librarymanager.adapters.in.rest.models;

public class LibraryDTO extends BaseDTO {

    private String name;
    private String location;

    public LibraryDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

