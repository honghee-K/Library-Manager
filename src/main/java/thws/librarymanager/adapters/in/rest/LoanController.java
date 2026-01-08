package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.LoanDTO;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;

import java.net.URI;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanController {

    @Context
    UriInfo uriInfo;

    @Inject
    LoanUseCase loanUseCase;

    @Inject
    BookUseCase bookUseCase;

    @Inject
    UserUseCase userUseCase;

    @Inject
    RestMapper restMapper;

    @POST
    public Response createLoan(LoanDTO loanDTO) {
        User user = userUseCase.getUserById(loanDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Book book = bookUseCase.getBookByIsbn(loanDTO.getIsbn())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        Loan newLoan = loanUseCase.createLoan(user, book);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newLoan.getId()))
                .build();

        return Response.created(location)
                .entity(restMapper.toLoanDTO(newLoan, uriInfo))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getLoanById(@PathParam("id") Long id) {
        Loan loan = loanUseCase.getLoanById(id);
        return Response.ok(restMapper.toLoanDTO(loan, uriInfo)).build();
    }

    //TODO
}
