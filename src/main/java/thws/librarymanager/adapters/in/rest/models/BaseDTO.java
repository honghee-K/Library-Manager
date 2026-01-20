package thws.librarymanager.adapters.in.rest.models;

import java.io.Serializable;

import jakarta.validation.constraints.PositiveOrZero;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public abstract class BaseDTO implements Serializable {

    @Schema(hidden = true)
    @PositiveOrZero
    protected Long id;

    protected BaseDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
