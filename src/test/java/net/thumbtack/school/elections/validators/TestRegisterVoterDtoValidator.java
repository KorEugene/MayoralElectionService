package net.thumbtack.school.elections.validators;

import com.google.gson.Gson;
import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.request.RegisterVoterDtoRequest;
import net.thumbtack.school.elections.server.Server;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestRegisterVoterDtoValidator {

    private static RegisterVoterDtoValidator validator = new RegisterVoterDtoValidator();
    private static FullName fullName;
    private static Address address;
    private static Account account;

    @Before
    public void initializeData() {
        fullName = new FullName("Ivan", "Ivanov", "Ivanovich");
        address = new Address("Fedora Abramova", "15", "27");
        account = new Account("IVA", "123456");
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidFirstName() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(new FullName(null, "Ivanov"), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_FIRST_NAME, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(new FullName("", "Ivanov"), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_FIRST_NAME, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request3 = new RegisterVoterDtoRequest(new FullName("Iv@n", "Ivanov"), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request3);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_FIRST_NAME, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidLastName() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(new FullName("Ivan", null), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_LAST_NAME, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(new FullName("Ivan", ""), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_LAST_NAME, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request3 = new RegisterVoterDtoRequest(new FullName("Ivan", "Ivan0v"), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request3);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_LAST_NAME, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidPatronymicName() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(new FullName("Ivan", "Ivanov", "Iv_3455"), address, account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_WRONG_PATRONYMIC_NAME, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidStreet() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(fullName, new Address(null, "15"), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_STREET, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(fullName, new Address("", "15"), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_STREET, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidHouse() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(fullName, new Address("Fedora Abramova", null), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_HOUSE, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(fullName, new Address("Fedora Abramova", ""), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_HOUSE, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request3 = new RegisterVoterDtoRequest(fullName, new Address("Fedora Abramova", "3f"), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request3);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_NOT_A_NUMBER_HOUSE, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidFlat() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(fullName, new Address("Fedora Abramova", "15", "abc"), account);
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_NOT_A_NUMBER_FLAT, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidLogin() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(fullName, address, new Account(null, "123456"));
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_LOGIN, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(fullName, address, new Account("", "123456"));
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_LOGIN, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithDuplicatedLogin() throws Exception {
        //arrange
        Gson gson = new Gson();
        String jsonString1 = "{\"fullName\":{\"firstName\":\"Ivan\",\"lastName\":\"Ivanov\",\"patronymicName\":\"Ivanovich\"}," +
                "\"address\":{\"street\":\"Fedora Abramova\",\"house\":\"15\",\"flat\":\"27\"}," +
                "\"account\":{\"login\":\"IVA\",\"password\":\"123456\"}}";
        Server server = Server.getInstance();
        server.startServer(null);
        server.registerVoter(jsonString1);
        String jsonString2 = "{\"fullName\":{\"firstName\":\"Petr\",\"lastName\":\"Petrov\",\"patronymicName\":\"Petrovich\"}," +
                "\"address\":{\"street\":\"Primorskaya\",\"house\":\"7\",\"flat\":\"35\"}," +
                "\"account\":{\"login\":\"IVA\",\"password\":\"654321\"}}";
        RegisterVoterDtoRequest request = gson.fromJson(jsonString2, RegisterVoterDtoRequest.class);
        try {
            //act
            validator.validateRegisterDtoRequest(request);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_DUPLICATE_LOGIN, ex.getErrorCode());
        }
        //remove
        server.stopServer(null);
    }

    @Test
    public void shouldReturnVoterExceptionWhenCreatedDtoWithInvalidPassword() throws Exception {
        //arrange
        RegisterVoterDtoRequest request1 = new RegisterVoterDtoRequest(fullName, address, new Account("IVA", null));
        try {
            //act
            validator.validateRegisterDtoRequest(request1);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_PASSWORD, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request2 = new RegisterVoterDtoRequest(fullName, address, new Account("IVA", ""));
        try {
            //act
            validator.validateRegisterDtoRequest(request2);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_EMPTY_PASSWORD, ex.getErrorCode());
        }
        //arrange
        RegisterVoterDtoRequest request3 = new RegisterVoterDtoRequest(fullName, address, new Account("IVA", "12345"));
        try {
            //act
            validator.validateRegisterDtoRequest(request3);
            fail();
        } catch (VoterException ex) {
            //assert
            assertEquals(VoterError.VOTER_SHORT_PASSWORD, ex.getErrorCode());
        }
    }
}
