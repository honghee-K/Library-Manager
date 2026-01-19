package thws.librarymanager.application.ports.in;

import java.util.Map;

public interface StatisticsUseCase {

    long getTotalBooks(Long libraryId);

    Map<String, Long> getBooksByGenre(Long libraryId);

    Map<String, Long> getBooksByAuthor(Long libraryId);

    long getRegisteredUserCount();

    long getActiveLoanCount();
}


