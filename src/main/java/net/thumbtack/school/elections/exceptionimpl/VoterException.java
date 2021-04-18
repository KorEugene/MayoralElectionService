package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;
import net.thumbtack.school.elections.exception.ServiceException;

public class VoterException extends RuntimeException implements ServiceException {

    private final VoterError errorCode;

    public VoterException(VoterError errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ServiceError getErrorCode() {
        return errorCode;
    }
}
