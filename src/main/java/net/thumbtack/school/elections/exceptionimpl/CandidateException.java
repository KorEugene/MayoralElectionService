package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;
import net.thumbtack.school.elections.exception.ServiceException;

public class CandidateException extends RuntimeException implements ServiceException {

    private final CandidateError errorCode;

    public CandidateException(CandidateError errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ServiceError getErrorCode() {
        return errorCode;
    }
}
