package thws.librarymanager.adapters.in.rest.services;

import java.time.Duration;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import thws.librarymanager.application.domain.models.Librarian;
import thws.librarymanager.application.ports.in.AuthUseCase;
import thws.librarymanager.application.ports.out.repository.LibrarianPort;

@ApplicationScoped
public class JwtAuthService implements AuthUseCase {

    public static final String Librarian_ROLE = "librarian";

    public static final String Librarian_ID_CLAIM = "librarian_id";

    @Inject
    private LibrarianPort librarianPort;

    public JwtAuthService() {}

    private String generateToken(long librarianId) {
        String id = "" + librarianId;
        return Jwt.upn(id)
                .groups(Set.of(Librarian_ROLE))
                .claim(Librarian_ID_CLAIM, id)
                .expiresIn(Duration.ofHours(8))
                .sign();
    }

    @Override
    public String generateAccessToken(String name) {
        Librarian librarian = librarianPort.findByName(name).orElseThrow(() -> new NotAuthorizedException(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Bearer")
                        .entity("Invalid Librarian Name")
                        .build()
                ));
        return generateToken(librarian.getId());
    }
}
