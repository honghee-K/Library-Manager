package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import thws.librarymanager.adapters.in.rest.models.StatisticsDTO;
import thws.librarymanager.application.ports.in.StatisticsUseCase;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsController {

    @Inject
    StatisticsUseCase statisticsUseCase;

    @GET
    public Response getStatistics(@QueryParam("libraryId") Long libraryId) {

        if (libraryId == null) {
            throw new BadRequestException("libraryId is required");
        }

        StatisticsDTO dto = new StatisticsDTO();

        StatisticsDTO.BooksStatistics books = new StatisticsDTO.BooksStatistics();
        books.setTotal(statisticsUseCase.getTotalBooks(libraryId));
        books.setBooksByGenre(statisticsUseCase.getBooksByGenre(libraryId));
        books.setBooksByAuthor(statisticsUseCase.getBooksByAuthor(libraryId));

        StatisticsDTO.UsersStatistics users = new StatisticsDTO.UsersStatistics();
        users.setRegistered(statisticsUseCase.getRegisteredUserCount());

        StatisticsDTO.LoansStatistics loans = new StatisticsDTO.LoansStatistics();
        loans.setActive(statisticsUseCase.getActiveLoanCount());

        dto.setBooks(books);
        dto.setUsers(users);
        dto.setLoans(loans);

        CacheControl cacheControl = new CacheControl();
        cacheControl.setPrivate(true);
        cacheControl.setMaxAge(30);

        return Response.ok(dto).cacheControl(cacheControl).build();
    }
}
