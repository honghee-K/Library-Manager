package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.LoanDTO;
import thws.librarymanager.adapters.in.rest.util.ETagGenerator;
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

    @Context
    Request request;

    @Inject
    LoanUseCase loanUseCase;

    @Inject
    UserUseCase userUseCase;

    @Inject
    BookUseCase bookUseCase;

    @Inject
    RestMapper restMapper;

    @POST
    public Response createLoan(LoanDTO dto) {

        User user = userUseCase.getUserById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Book book = bookUseCase.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        Loan loan = loanUseCase.createLoan(user, book);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(loan.getId()))
                .build();

        return Response.created(location)
                .entity(restMapper.toLoanDTO(loan, uriInfo))
                .build();
    }

    @PUT
    @Path("{id}/return")
    public Response returnLoan(@PathParam("id") Long id) {

        Loan loan = loanUseCase.getLoanById(id);

        EntityTag etag =
                new EntityTag(ETagGenerator.fromLoan(loan));

        Response.ResponseBuilder precond =
                request.evaluatePreconditions(etag);

        if (precond != null) {
            return precond.build(); // 412
        }

        Loan returned = loanUseCase.returnLoan(id);

        return Response.noContent()
                .tag(new EntityTag(ETagGenerator.fromLoan(returned)))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getLoan(@PathParam("id") Long id) {

        Loan loan = loanUseCase.getLoanById(id);

        EntityTag etag =
                new EntityTag(ETagGenerator.fromLoan(loan));

        Response.ResponseBuilder precond =
                request.evaluatePreconditions(etag);

        if (precond != null) {
            return precond.build(); // 304
        }

        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(3600);

        return Response.ok(restMapper.toLoanDTO(loan, uriInfo))
                .tag(etag)
                .cacheControl(cc)
                .build();
    }
}
