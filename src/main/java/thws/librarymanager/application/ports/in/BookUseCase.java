package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Loan;


public interface BookUseCase {
    Book create(Book book);
    void update(Book book);
    void delete(long isbn);
    Book getByIsbn(long isbn);

    void temp();



}
