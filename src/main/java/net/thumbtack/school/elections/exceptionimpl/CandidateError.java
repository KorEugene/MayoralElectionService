package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;

public enum CandidateError implements ServiceError {
    CANDIDATE_WAS_NOT_SAVED("Error! Candidate was not saved to DataBase!"),
    CANDIDATE_ALREADY_REGISTERED("Error! Requested voter already registered as candidate!"),
    NOMINATE_REQUEST_WAS_NOT_SAVED("Error! Nominate request was not saved to DataBase!"),
    NOMINATE_REQUEST_ALREADY_REGISTERED("Error! Requested voter already nominated!"),
    CANDIDATE_NOT_REGISTERED("Error! Requested candidate does not exist!"),
    CANDIDATE_PROGRAM_WAS_NOT_SAVED("Error! Offer was not saved to candidate program!"),
    CANDIDACY_ACTIVE("Error! Candidacy active! You cannot log out before you withdraw your candidacy!"),
    AUTHOR_REJECTS_HIS_OFFERS("Error! You can not add or delete your own offers!");

    private String errorString;

    CandidateError(String message) {
        this.errorString = message;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
