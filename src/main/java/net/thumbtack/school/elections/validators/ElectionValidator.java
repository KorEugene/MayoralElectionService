package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.ElectionError;
import net.thumbtack.school.elections.exceptionimpl.ElectionException;
import net.thumbtack.school.elections.model.Candidate;
import net.thumbtack.school.elections.model.ResponseStatusMessage;

import java.util.List;

public class ElectionValidator {

    public void validateVotingStarted(boolean votingStatus) {
        if (votingStatus) {
            throw new ElectionException(ElectionError.VOTING_ALREADY_STARTED);
        }
    }

    public void validateVotingNotStarted(boolean votingStatus) {
        if (!votingStatus) {
            throw new ElectionException(ElectionError.VOTING_NOT_STARTED);
        }
    }

    public void validateCandidateList(List<Candidate> candidates) {
        if (candidates.isEmpty()) {
            throw new ElectionException(ElectionError.NO_CANDIDATES_FOR_MAYOR);
        }
    }

    public void validateVote(boolean voted) {
        if (voted) {
            throw new ElectionException(ElectionError.ALREADY_VOTED);
        }
    }

    public void validateDataBaseCleanup(String message) {
        if (!message.equals(ResponseStatusMessage.ALL_VOTES_HAVE_BEEN_DELETED.getMessage())) {
            throw new ElectionException(ElectionError.DATABASE_WAS_NOT_CLEANED);
        }
    }
}
