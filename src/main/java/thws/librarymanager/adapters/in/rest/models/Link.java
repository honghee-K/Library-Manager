package thws.librarymanager.adapters.in.rest.models;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 Portfolio 3 - Nr.6
 */
@XmlRootElement(name = "link")
public class Link {

    // 1. URL
    private String href;

    // 2. Relationship - self, delete
    private String rel;

    // 3. Media Type - application/json
    private String type;

    public Link() {
    }

    public Link(String href, String rel, String type) {
        this.href = href;
        this.rel = rel;
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                ", rel='" + rel + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}