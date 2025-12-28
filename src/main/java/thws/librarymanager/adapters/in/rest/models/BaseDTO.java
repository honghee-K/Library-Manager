package thws.librarymanager.adapters.in.rest.models;

import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serializable;

public abstract class BaseDTO implements Serializable {

    @PositiveOrZero
    protected long id;

    protected Link selfLink;

    protected BaseDTO() {
        this.selfLink = new Link();
    }

    public long getId() { return id; }
    public void setId(final long id) { this.id = id; }

    public Link getSelfLink() { return selfLink; }
    public void setSelfLink(Link selfLink) { this.selfLink = selfLink; }
}