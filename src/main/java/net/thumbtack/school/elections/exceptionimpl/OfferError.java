package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;

public enum OfferError implements ServiceError {
    OFFER_DOES_NOT_EXIST("Requested offer does not exist!"),
    OFFER_WAS_NOT_SAVED("Error! Offer was not saved to DataBase!");

    private String errorString;

    OfferError(String message) {
        this.errorString = message;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
