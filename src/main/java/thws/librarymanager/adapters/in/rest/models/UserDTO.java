package thws.librarymanager.adapters.in.rest.models;

public class UserDTO extends BaseDTO {
    private String name;
    private String email;
    public UserDTO() { super(); }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}