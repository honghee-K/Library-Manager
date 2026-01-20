package thws.librarymanager.adapters.out.time;

import java.time.LocalDate;

import jakarta.enterprise.context.ApplicationScoped;

import thws.librarymanager.application.ports.out.time.TimeProvider;

@ApplicationScoped
public class SystemTimeProvider implements TimeProvider {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
