package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.models.StatisticsDTO;
import thws.librarymanager.application.ports.in.StatisticsUseCase;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatisticsController extends BaseController {

    @Context
    UriInfo uriInfo;

    @Inject
    StatisticsUseCase statisticsUseCase;

    @GET
    public Response getStatistics(
            @QueryParam("libraryId") Long libraryId,
            @QueryParam("genre") String genre,
            @QueryParam("author") String author
    ) {

        if (libraryId == null) {
            throw new BadRequestException("libraryId is required");
        }

        StatisticsDTO dto = new StatisticsDTO();

        dto.setTotalBooks(
                statisticsUseCase.getTotalBooks(libraryId)
        );

        if (genre != null) {
            dto.setBooksByGenre(
                    statisticsUseCase.getBookCountByGenre(libraryId, genre)
            );
        }

        if (author != null) {
            dto.setBooksByAuthor(
                    statisticsUseCase.getBookCountByAuthor(libraryId, author)
            );
        }

        dto.setActiveLoans(
                statisticsUseCase.getActiveLoanCount()
        );

        dto.setRegisteredUsers(
                statisticsUseCase.getRegisteredUserCount()
        );

        Response.ResponseBuilder rb = Response.ok(dto);

        // HATEOAS self link
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        // Cache
        CacheControl cc = new CacheControl();
        cc.setPrivate(true);
        cc.setMaxAge(60);
        rb.cacheControl(cc);

        return rb.build();
    }
}
