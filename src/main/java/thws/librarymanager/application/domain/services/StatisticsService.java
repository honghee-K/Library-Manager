package thws.librarymanager.application.domain.services;

import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import thws.librarymanager.application.ports.in.LibraryUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.in.StatisticsUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;
import thws.librarymanager.application.ports.out.repository.LibraryPort;

@ApplicationScoped
public class StatisticsService implements StatisticsUseCase {

    private final LibraryUseCase libraryUseCase;
    private final LoanUseCase loanUseCase;
    private final UserUseCase userUseCase;

    @Inject
    LibraryPort libraryPort;

    @Inject
    public StatisticsService(LibraryUseCase libraryUseCase, LoanUseCase loanUseCase, UserUseCase userUseCase) {
        this.libraryUseCase = libraryUseCase;
        this.loanUseCase = loanUseCase;
        this.userUseCase = userUseCase;
    }

    @Override
    public long getTotalBooks(Long libraryId) {
        return libraryUseCase.getTotalBookCount(libraryId);
    }

    @Override
    public Map<String, Long> getBooksByGenre(Long libraryId) {
        return libraryPort.countBooksGroupedByGenre(libraryId);
    }

    @Override
    public Map<String, Long> getBooksByAuthor(Long libraryId) {
        return libraryPort.countBooksGroupedByAuthor(libraryId);
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
