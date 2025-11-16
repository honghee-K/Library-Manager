package thws.librarymanager.application.ports.in;

import thws.librarymanager.application.domain.model.Book;
import thws.librarymanager.application.domain.model.Loan;


public interface BookUseCase {
    Book getById(long id);
    //CRUD Operation
    //TODO

    boolean isOnLoan(Loan loan);


}
