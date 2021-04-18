package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;

public enum ElectionError implements ServiceError {
    VOTING_ALREADY_STARTED("Error! Voting already started!"),
    VOTING_NOT_STARTED("Error! Voting has not started yet"),
    NO_CANDIDATES_FOR_MAYOR("Error! No candidates for mayor!"),
    ALREADY_VOTED("Error! You have already voted!"),
    VOTE_WAS_NOT_SAVED("Error! Vote was not saved to DataBase!"),
    DATABASE_WAS_NOT_CLEANED("Error! The database was not cleaned!");

    private String errorString;

    ElectionError(String message) {
        this.errorString = message;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
