package thws.librarymanager.application.ports.in;

import java.util.Map;
import java.util.Collections;

/**
 An Immutable Data Transfer Object (DTO) to hold book statistics data.
 */
public final class BookStatistics {

    private final long totalBooks;
    private final Map<String, Long> countByGenre;
    private final Map<String, Long> countByAuthor;

    public BookStatistics(long totalBooks, Map<String, Long> countByGenre, Map<String, Long> countByAuthor) {
        this.totalBooks = totalBooks;
        // Maps are copied and wrapped to maintain immutability.
        this.countByGenre = Map.copyOf(countByGenre);
        this.countByAuthor = Map.copyOf(countByAuthor);
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public Map<String, Long> getCountByGenre() {
        return Collections.unmodifiableMap(countByGenre);
    }

    public Map<String, Long> getCountByAuthor() {
        return Collections.unmodifiableMap(countByAuthor);
    }

}
