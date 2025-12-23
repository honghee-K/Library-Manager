package thws.librarymanager.adapters.in.rest.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import thws.librarymanager.application.domain.model.User;
import thws.librarymanager.application.ports.in.AuthUseCase;
import thws.librarymanager.application.ports.out.repository.UserRepository;

import java.time.Duration;
import java.util.Set;

@ApplicationScoped
public class JwtAuthService implements AuthUseCase {

    public static final String USER_ROLE = "user";

    public static final String USER_ID_CLAIM = "user_id";

    @Inject
    private UserRepository userRepository;

    public JwtAuthService() {}

    private String generateToken(long userId) {
        String id = "" + userId;
        return Jwt.upn(id)
                .groups(Set.of(USER_ROLE))
                .claim(USER_ID_CLAIM, id)
                .expiresIn(Duration.ofHours(8))
                .sign();
    }

    @Override
    public String generateAccessToken(String name, String password) {
        User user = userRepository.findByName(name).orElseThrow();
        return generateToken(user.getId());
    }
}
