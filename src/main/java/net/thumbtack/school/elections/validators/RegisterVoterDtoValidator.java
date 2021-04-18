package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.request.RegisterVoterDtoRequest;
import net.thumbtack.school.elections.server.DataBase;
import org.apache.commons.lang3.StringUtils;

public class RegisterVoterDtoValidator {

    public void validateRegisterDtoRequest(RegisterVoterDtoRequest request) {
        validateFirstName(request.getFullName().getFirstName());
        validateLastName(request.getFullName().getLastName());
        validatePatronymicName(request.getFullName().getPatronymicName());
        validateStreet(request.getAddress().getStreet());
        validateHouse(request.getAddress().getHouse());
        validateFlat(request.getAddress().getFlat());
        validateLogin(request.getAccount().getLogin());
        validatePassword(request.getAccount().getPassword());
    }

    private void validateFirstName(String firstName) {
        if (isNotValidInput(firstName)) {
            throw new VoterException(VoterError.VOTER_WRONG_FIRST_NAME);
        }
    }

    private void validateLastName(String lastName) {
        if (isNotValidInput(lastName)) {
            throw new VoterException(VoterError.VOTER_WRONG_LAST_NAME);
        }
    }

    private void validatePatronymicName(String patronymicName) {
        if (!StringUtils.isBlank(patronymicName) && !StringUtils.isAlpha(patronymicName)) {
            throw new VoterException(VoterError.VOTER_WRONG_PATRONYMIC_NAME);
        }
    }

    private void validateLogin(String login) {
        if (StringUtils.isBlank(login)) {
            throw new VoterException(VoterError.VOTER_EMPTY_LOGIN);
        }
        if (isAlreadyExistLogin(login)) {
            throw new VoterException(VoterError.VOTER_DUPLICATE_LOGIN);
        }
    }

    private void validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new VoterException(VoterError.VOTER_EMPTY_PASSWORD);
        }
        if (password.length() < 6) {
            throw new VoterException(VoterError.VOTER_SHORT_PASSWORD);
        }
    }

    private void validateStreet(String street) {
        if (StringUtils.isBlank(street)) {
            throw new VoterException(VoterError.VOTER_EMPTY_STREET);
        }
    }

    private void validateHouse(String house) {
        if (StringUtils.isBlank(house)) {
            throw new VoterException(VoterError.VOTER_EMPTY_HOUSE);
        }
        if (!StringUtils.isNumeric(house)) {
            throw new VoterException(VoterError.VOTER_NOT_A_NUMBER_HOUSE);
        }
    }

    private void validateFlat(String flat) {
        if (!StringUtils.isBlank(flat) && !StringUtils.isNumeric(flat)) {
            throw new VoterException(VoterError.VOTER_NOT_A_NUMBER_FLAT);
        }
    }

    private boolean isNotValidInput(String inputString) {
        return StringUtils.isBlank(inputString) || !StringUtils.isAlpha(inputString);
    }

    private boolean isAlreadyExistLogin(String login) {
        return DataBase.getInstance().getVoterMap().entrySet().stream()
                .anyMatch(s -> s.getValue().getPerson().getAccount().getLogin().equals(login));
    }
}
