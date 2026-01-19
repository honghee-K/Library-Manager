package thws.librarymanager.application.domain.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.ports.in.LibraryUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.in.StatisticsUseCase;

@ApplicationScoped
public class StatisticsService implements StatisticsUseCase {

    private final LibraryUseCase libraryUseCase;
    private final LoanUseCase loanUseCase;
    private final UserUseCase userUseCase;

    @Inject
    public StatisticsService(
            LibraryUseCase libraryUseCase,
            LoanUseCase loanUseCase,
            UserUseCase userUseCase
    ) {
        this.libraryUseCase = libraryUseCase;
        this.loanUseCase = loanUseCase;
        this.userUseCase = userUseCase;
    }

    @Override
    public long getTotalBooks(Long libraryId) {
        return libraryUseCase.getTotalBookCount(libraryId);
    }

    @Override
    public long getBookCountByGenre(Long libraryId, String genre) {
        return libraryUseCase.getBookCountByGenre(libraryId, genre);
    }

    @Override
    public long getBookCountByAuthor(Long libraryId, String author) {
        return libraryUseCase.getBookCountByAuthor(libraryId, author);
    }

    @Override
    public long getActiveLoanCount() {
        return loanUseCase.getActiveLoanCount();
    }

    @Override
    public long getRegisteredUserCount() {
        return userUseCase.getRegisteredUserCount();
    }
}
