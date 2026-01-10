package thws.librarymanager.adapters.in.rest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import thws.librarymanager.application.domain.models.Book;
import thws.librarymanager.application.domain.models.Loan;
import thws.librarymanager.application.domain.models.User;

public class ETagGenerator {

    public static String fromBook(Book book) {
        String combined = book.getTitle() + book.getAuthor() + book.getGenre();
        return Integer.toHexString(combined.hashCode());
    }
    public static String fromLoan(Loan loan) {
        String value = loan.getId() + "-" +
                loan.getStatus() + "-" +
                loan.getDueDate();

        return sha256(value);
    }

    public static String fromUser(User user) {
        String value = user.getId() + "-" +
                user.getName() + "-" +
                user.getEmail();
        return sha256(value);
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("ETag generation failed", e);
        }
    }
}
