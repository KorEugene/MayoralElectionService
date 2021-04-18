package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

public class TokenValidator {

    public void validateToken(String inputString) {
        if (isNotValidToken(inputString)) {
            throw new VoterException(VoterError.VOTER_NOT_VALID_TOKEN);
        }
    }

    private boolean isNotValidToken(String token) {
        return DataBaseManagementSystem.getInstance().getTokenMap()
                .keySet()
                .stream()
                .noneMatch(k -> k.equals(token));
    }
}
