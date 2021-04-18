package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;
import net.thumbtack.school.elections.exception.ServiceException;

public class ElectionException extends RuntimeException implements ServiceException {

    private final ElectionError errorCode;

    public ElectionException(ElectionError errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ServiceError getErrorCode() {
        return errorCode;
    }
}
