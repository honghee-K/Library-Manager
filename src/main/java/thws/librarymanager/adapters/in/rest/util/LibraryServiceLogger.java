package thws.librarymanager.adapters.in.rest.util;

import java.time.LocalDateTime;

public class LibraryServiceLogger {

    public static void logGetAll() {
        log("GET /libraries");
    }

    public static void logGetById(Long id) {
        log("GET /libraries/" + id);
    }

    private static void log(String message) {
        System.out.println("[" + LocalDateTime.now() + "] " + message);
    }
}

