package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.*;

import static java.util.Objects.isNull;

public class ObjectSavedToDataBaseValidator {

    public void validateVoterId(String id) {
        if (isNull(id)) {
            throw new VoterException(VoterError.VOTER_WAS_NOT_SAVED);
        }
    }

    public void validateOfferId(String id) {
        if (isNull(id)) {
            throw new OfferException(OfferError.OFFER_WAS_NOT_SAVED);
        }
    }

    public void validateRatingId(String id) {
        if (isNull(id)) {
            throw new RatingException(RatingError.RATING_WAS_NOT_SAVED);
        }
    }

    public void validateCandidateId(String id) {
        if (isNull(id)) {
            throw new CandidateException(CandidateError.CANDIDATE_WAS_NOT_SAVED);
        }
    }

    public void validateNominateRequestId(String id) {
        if (isNull(id)) {
            throw new CandidateException(CandidateError.NOMINATE_REQUEST_WAS_NOT_SAVED);
        }
    }

    public void validateCandidateProgramId(String candidateProgramId) {
        if (isNull(candidateProgramId)) {
            throw new CandidateException(CandidateError.CANDIDATE_PROGRAM_WAS_NOT_SAVED);
        }
    }

    public void validateVoteId(String id) {
        if (isNull(id)) {
            throw new ElectionException(ElectionError.VOTE_WAS_NOT_SAVED);
        }
    }
}
