package net.thumbtack.school.elections.exceptionimpl;

import net.thumbtack.school.elections.exception.ServiceError;

public enum VoterError implements ServiceError {
    VOTER_WRONG_FIRST_NAME("The entered first name is not valid"),
    VOTER_WRONG_LAST_NAME("The entered second name is not valid"),
    VOTER_WRONG_PATRONYMIC_NAME("The entered patronymic name is not valid"),
    VOTER_EMPTY_STREET("Street can not be empty"),
    VOTER_EMPTY_HOUSE("House can not be empty"),
    VOTER_NOT_A_NUMBER_HOUSE("Number must consists of digits"),
    VOTER_NOT_A_NUMBER_FLAT("Number must consists of digits"),
    VOTER_EMPTY_LOGIN("Login can not be empty!"),
    VOTER_DUPLICATE_LOGIN("This login is already exist! Please, choose another one"),
    VOTER_EMPTY_PASSWORD("Password can not be empty!"),
    VOTER_SHORT_PASSWORD("Password can not be less than 6 characters!"),
    VOTER_NOT_VALID_TOKEN("This token is not valid!"),
    VOTER_WAS_NOT_SAVED("Error! Voter was not saved to DataBase!"),
    VOTER_DOES_NOT_EXIST("Error! Voter does not exist!"),
    VOTER_NOT_CORRECT_LOGIN("Invalid login!"),
    VOTER_NOT_CORRECT_PASSWORD("Invalid password!");

    private String errorString;

    VoterError(String message) {
        this.errorString = message;
    }

    @Override
    public String getErrorString() {
        return errorString;
    }
}
