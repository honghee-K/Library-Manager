package thws.librarymanager.adapters.in.rest.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public abstract class BaseDTO implements Serializable {

    @PositiveOrZero
    protected long id;

    protected List<Link> links = new ArrayList<>();

    protected BaseDTO() {}

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public List<Link> getLinks() { return links; }

    public void setLinks(List<Link> links) { this.links = links; }

    public void addLink(String href, String rel, String type) {
        this.links.add(new Link(href, rel, type));
    }
}
