package thws.librarymanager.application.ports.out.time;

import java.time.LocalDate;

public interface TimeProvider {
    LocalDate today();
}
