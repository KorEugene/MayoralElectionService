package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;
import net.thumbtack.school.elections.exception.ServiceException;

public class RatingException extends RuntimeException implements ServiceException {

    private final RatingError errorCode;

    public RatingException(RatingError errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ServiceError getErrorCode() {
        return errorCode;
    }
}
