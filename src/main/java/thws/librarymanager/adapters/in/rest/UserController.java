package thws.librarymanager.adapters.in.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import thws.librarymanager.adapters.in.rest.mapper.RestMapper;
import thws.librarymanager.adapters.in.rest.models.UserDTO;
import thws.librarymanager.adapters.in.rest.util.ETagGenerator;
import thws.librarymanager.application.domain.models.User;
import thws.librarymanager.application.ports.in.UserUseCase;

import java.net.URI;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController{

    @Inject UserUseCase userUseCase;
    @Inject RestMapper mapper;
    @Context UriInfo uriInfo;
    @Context Request request;

    @POST
    public Response createUser(UserDTO dto) {
        User user = userUseCase.createUser(dto.getName(), dto.getEmail());
        URI newUserUri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(user.getId()))
                .build();

        Response.ResponseBuilder rb = Response.created(newUserUri);
        addLink(rb, newUserUri, "self");

        return rb.entity(mapper.toUserDTO(user)).build();
    }

    @GET
    public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("size") @DefaultValue("10") int size) {
        List<User> users = userUseCase.getAllUsers(page, size);
        List<UserDTO> dtos = mapper.toUserDTOs(users);

        Response.ResponseBuilder rb = Response.ok(dtos);
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        rb.cacheControl(cc);

        return rb.build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        User user = userUseCase.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Validation
        EntityTag etag = new EntityTag(ETagGenerator.fromUser(user));

        // If-None-Match
        Response.ResponseBuilder rb = request.evaluatePreconditions(etag);

        if (rb != null) {
            return rb.tag(etag).build();
        }

        UserDTO dto = mapper.toUserDTO(user);
        rb = Response.ok(dto);

        URI selfUri = uriInfo.getAbsolutePath();
        addLink(rb, selfUri, "self");
        addLink(rb, selfUri, "update");
        addLink(rb, selfUri, "delete");

        return rb.tag(etag).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserDTO dto) {
        userUseCase.updateUser(id, dto.getName(), dto.getEmail());

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, uriInfo.getAbsolutePath(), "self");

        return rb.build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userUseCase.deleteUser(id);

        URI collectionUri = uriInfo.getBaseUriBuilder()
                .path(UserController.class)
                .build();

        Response.ResponseBuilder rb = Response.noContent();
        addLink(rb, collectionUri, "collection");

        return rb.build();
    }
}