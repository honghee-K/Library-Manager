package thws.librarymanager.adapters.in.rest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import thws.librarymanager.application.domain.models.Loan;

public class ETagGenerator {

    public static String fromLoan(Loan loan) {
        String value = loan.getId() + "-" +
                loan.getStatus() + "-" +
                loan.getDueDate();

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
