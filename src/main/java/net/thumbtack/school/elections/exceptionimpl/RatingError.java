package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;

public enum RatingError implements ServiceError {
    AUTHOR_CHANGE_RATING("Authors can not change or delete their assessment!"),
    NOT_CORRECT_VALUE("Value is not correct!"),
    RATING_WAS_NOT_SAVED("Error! Rating was not saved!");

    private String errorString;

    RatingError(String message) {
        this.errorString = message;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
