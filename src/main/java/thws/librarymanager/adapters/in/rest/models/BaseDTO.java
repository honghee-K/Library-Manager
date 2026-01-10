package thws.librarymanager.adapters.in.rest.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.PositiveOrZero;

public abstract class BaseDTO implements Serializable {

    @PositiveOrZero
    protected long id;

    protected BaseDTO() {}

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

}
