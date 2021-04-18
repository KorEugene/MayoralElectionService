package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;
import net.thumbtack.school.elections.exception.ServiceException;

public class OfferException extends RuntimeException implements ServiceException {

    private final OfferError errorCode;

    public OfferException(OfferError errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public ServiceError getErrorCode() {
        return errorCode;
    }
}
