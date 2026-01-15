package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.LoanDTO;
import thws.librarymanager.adapters.in.rest.util.ETagGenerator;
import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.LoanStatus;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.BookUseCase;
import thws.librarymanager.application.ports.in.LoanUseCase;
import thws.librarymanager.application.ports.in.UserUseCase;

import java.net.URI;
import java.util.List;

@Path("/loans")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanController extends BaseController{

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
    @Transactional
    public Response createLoan(LoanDTO dto) {

        User user = userUseCase.getUserById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Book book = bookUseCase.getBookByIsbn(dto.getIsbn())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        Loan loan = loanUseCase.createLoan(user, book);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(loan.getId()))
                .build();

        Response.ResponseBuilder rb = Response.created(location);

        addLink(rb, location, "self");

        return rb.entity(restMapper.toLoanDTO(loan)).build();
    }

    @PUT
    @Transactional
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
        precond = Response.ok(restMapper.toLoanDTO(returned));

        URI selfUri = uriInfo.getBaseUriBuilder()
                .path(LoanController.class)
                .path(id.toString())
                .build();
        addLink(precond, selfUri, "self");

        EntityTag etagAfter = new EntityTag(ETagGenerator.fromLoan(returned));
        return precond.tag(etagAfter).build();
    }

    @GET
    @Transactional
    @Path("{id}")
    public Response getLoan(@PathParam("id") Long id) {

        Loan loan = loanUseCase.getLoanById(id);
        if (loan == null) {
            throw new NotFoundException("Loan not found");
        }

        EntityTag etag =
                new EntityTag(ETagGenerator.fromLoan(loan));

        Response.ResponseBuilder precond =
                request.evaluatePreconditions(etag);

        if (precond != null) {
            return precond.build(); // 304
        }

        LoanDTO dto = restMapper.toLoanDTO(loan);
        precond = Response.ok(dto);

        URI selfUri = uriInfo.getAbsolutePath();
        addLink(precond, selfUri, "self");


        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(3600);
        precond.cacheControl(cc);

        return precond.tag(etag).build();
    }
    @GET
    public Response getAllLoans(
            @QueryParam("userId") Long userId,
            @QueryParam("isbn") Long isbn,
            @QueryParam("status") LoanStatus status,
            @QueryParam("overdue") Boolean overdue,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
    ) {

        List<LoanDTO> dtos = loanUseCase
                .getAllLoans(userId, isbn, status, overdue, page, size)
                .stream()
                .map(restMapper::toLoanDTO)
                .toList();

        Response.ResponseBuilder rb = Response.ok(dtos);

        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(30);

        rb.cacheControl(cc);

        return rb.build();
    }


}
