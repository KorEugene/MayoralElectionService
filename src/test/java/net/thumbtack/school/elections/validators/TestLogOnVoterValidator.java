package net.thumbtack.school.elections.validators;

import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.exceptionimpl.VoterError;
import net.thumbtack.school.elections.exceptionimpl.VoterException;
import net.thumbtack.school.elections.model.Account;
import net.thumbtack.school.elections.model.Address;
import net.thumbtack.school.elections.model.FullName;
import net.thumbtack.school.elections.model.Person;
import net.thumbtack.school.elections.model.Voter;
import net.thumbtack.school.elections.request.LogOnDtoRequest;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestLogOnVoterValidator {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private LogOnVoterValidator validator = new LogOnVoterValidator();
    private static FullName fullName = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address = new Address("Fedora Abramova", "15", "27");
    private static Account account = new Account("IVA", "123456");
    private static Voter voter = new Voter(new Person(fullName, address, account));

    @Before
    public void generateTokens() {
        voter.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldReturnVoterExceptionWhenReturnVoterByInvalidLoginFromRequestFromDataBase() throws Exception {
        //arrange
        voterDao.addNewVoter(voter);
        String incorrectLogin = "VIVA";
        String correctPassword = "123456";
        //act
        LogOnDtoRequest request = new LogOnDtoRequest(incorrectLogin, correctPassword);
        Voter voter = voterDao.getVoters().stream().filter(v -> v.getPerson().getAccount().getLogin().equals(request.getLogin())).findFirst().orElse(null);
        try {
            validator.validateLogOnData(voter, request.getPassword());
        } catch (VoterException ex) {
            //assert
            assertEquals(1, dbms.getVoterMap().size());
            assertEquals(VoterError.VOTER_NOT_CORRECT_LOGIN, ex.getErrorCode());
        }
    }

    @Test
    public void shouldReturnVoterExceptionWhenValidateVoterInvalidPasswordFromRequest() throws Exception {
        //arrange
        voterDao.addNewVoter(voter);
        String correctLogin = "IVA";
        String incorrectPassword = "123456789";
        //act
        LogOnDtoRequest request = new LogOnDtoRequest(correctLogin, incorrectPassword);
        Voter voter = voterDao.getVoters().stream().filter(v -> v.getPerson().getAccount().getLogin().equals(request.getLogin())).findFirst().orElse(null);
        try {
            validator.validateLogOnData(voter, request.getPassword());
        } catch (VoterException ex) {
            //assert
            assertEquals(1, dbms.getVoterMap().size());
            assertEquals(VoterError.VOTER_NOT_CORRECT_PASSWORD, ex.getErrorCode());
        }
    }
}
