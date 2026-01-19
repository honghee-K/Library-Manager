package thws.librarymanager.application.ports.in;

public interface StatisticsUseCase {

    long getTotalBooks(Long libraryId);

    long getBookCountByGenre(Long libraryId, String genre);

    long getBookCountByAuthor(Long libraryId, String author);

    long getActiveLoanCount();

    long getRegisteredUserCount();
}

