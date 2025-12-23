package thws.librarymanager.application.ports.in;

public interface AuthUseCase {

    String generateAccessToken(String name, String password);
}
