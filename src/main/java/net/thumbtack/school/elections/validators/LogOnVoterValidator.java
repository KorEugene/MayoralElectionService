package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.model.Voter;

import static java.util.Objects.isNull;

public class LogOnVoterValidator {

    public void validateLogOnData(Voter voter, String requestPassword) {
        validateLogin(voter);
        validatePassword(voter.getPerson().getAccount().getPassword(), requestPassword);
    }

    private void validateLogin(Voter voter) {
        if (isNull(voter)) {
            throw new VoterException(VoterError.VOTER_NOT_CORRECT_LOGIN);
        }
    }

    private void validatePassword(String voterPassword, String requestPassword) {
        if (!voterPassword.equals(requestPassword)) {
            throw new VoterException(VoterError.VOTER_NOT_CORRECT_PASSWORD);
        }
    }
}
