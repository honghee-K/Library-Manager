package thws.librarymanager.adapters.in.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import thws.librarymanager.adapters.in.rest.services.JwtAuthService;
import thws.librarymanager.application.ports.in.AuthUseCase;

@Path("/auth")
public class AuthController {

    @Inject
    private JsonWebToken jwt;

    @Inject
    private AuthUseCase authUseCase;

    public AuthController() {}

    @GET
    @Path("login")
    public Response login(@QueryParam("name") String name, @QueryParam("password") String password) {

        String accessToken = authUseCase.generateAccessToken(name, password);

        return Response.ok(accessToken).build();
    }

    @RolesAllowed(JwtAuthService.USER_ROLE) // TODO später in aderen Methode
    @GET
    @Path("test")
    public Response test() {

        long userId = Long.parseLong(jwt.getClaim(JwtAuthService.USER_ID_CLAIM));

        return Response.ok("Hi " + userId).build();
    }
}
