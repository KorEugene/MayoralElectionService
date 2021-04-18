package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.CandidateError;
import net.thumbtack.school.elections.exceptionimpl.CandidateException;
import net.thumbtack.school.elections.model.Candidate;

import static java.util.Objects.isNull;

public class CandidateValidator {

    public void validateCandidateIsExist(Candidate candidate) {
        if (isNull(candidate)) {
            throw new CandidateException(CandidateError.CANDIDATE_NOT_REGISTERED);
        }
    }

    public void validateAuthorForCandidateProgramModification(String voterId, String authorId) {
        if (voterId.equals(authorId)) {
            throw new CandidateException(CandidateError.AUTHOR_REJECTS_HIS_OFFERS);
        }
    }

    public void validateCandidacyIsActive(boolean candidacyStatus) {
        if (candidacyStatus) {
            throw new CandidateException(CandidateError.CANDIDACY_ACTIVE);
        }
    }
}
