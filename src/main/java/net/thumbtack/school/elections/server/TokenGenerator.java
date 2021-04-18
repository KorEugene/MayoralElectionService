package net.thumbtack.school.elections.server;

import java.util.UUID;

public class TokenGenerator {

    private TokenGenerator() {
    }

    public static String generateNewToken() {
        return UUID.randomUUID().toString();
    }
}
