package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.server.DataBase;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;
import net.thumbtack.school.elections.server.TokenGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestVoterDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static Voter voter1 = new Voter(new Person(fullName1, address1, account1));
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static Voter voter2 = new Voter(new Person(fullName2, address2, account2));

    @Before
    public void generateTokens() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldUpdateVoterToken() throws Exception {
        //arrange
        voterDao.addNewVoter(voter1);
        String token1 = voter1.getToken();
        //act
        String newToken1 = TokenGenerator.generateNewToken();
        String message1 = voterDao.updateToken(voter1.getPerson().getId(), newToken1);
        //assert
        assertEquals(1, dbms.getVoterMap().size());
        assertNotEquals(token1, newToken1);
        assertEquals(newToken1, voter1.getToken());
        assertEquals(ResponseStatusMessage.TOKEN_UPDATED.getMessage(), message1);
    }

    @Test
    public void shouldRemoveVoterToken() throws Exception {
        //arrange
        voterDao.addNewVoter(voter1);
        String token1 = voter1.getToken();
        //act
        String message1 = voterDao.removeToken(token1);
        //assert
        assertEquals(0, dbms.getTokenMap().size());
        assertEquals(ResponseStatusMessage.TOKEN_REMOVED.getMessage(), message1);
    }

    @Test
    public void shouldAddNewVoterToDataBase() throws Exception {
        //act
        voterDao.addNewVoter(voter1);
        //assert
        assertEquals(1, dbms.getVoterMap().size());
        assertEquals(1, dbms.getPersonMap().size());
        assertTrue(dbms.getPersonMap().containsValue(voter1.getPerson()));
        assertTrue(dbms.getVoterMap().containsValue(voter1));
    }

    @Test
    public void shouldReturnVoterFromDataBaseById() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        //act
        Voter resultVoter1 = voterDao.getVoterById(voterId1);
        //assert
        assertEquals(1, dbms.getVoterMap().size());
        assertEquals(voter1, resultVoter1);
        assertEquals(voterId1, resultVoter1.getPerson().getId());
    }

    @Test
    public void shouldReturnVoterListFromDataBase() throws Exception {
        //arrange
        voterDao.addNewVoter(voter1);
        voterDao.addNewVoter(voter2);
        //act
        List<Voter> voterList1 = voterDao.getVoters();
        //assert
        assertEquals(2, voterList1.size());
        assertTrue(voterList1.contains(voter1));
        assertTrue(voterList1.contains(voter2));
    }

    @Test
    public void shouldReturnVoterIdFromDataBaseByToken() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        String token1 = dbms.getVoterMap().get(voterId1).getToken();
        //act
        String voter1Id = voterDao.getVoterIdByToken(token1);
        //assert
        assertEquals(voter1.getPerson().getId(), voter1Id);
    }

    @Test
    public void shouldReturnVoterTokenFromDataBaseById() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        //act
        String token1 = voterDao.getVoterTokenById(voterId1);
        //assert
        assertEquals(voter1.getToken(), token1);
    }

    @Test
    public void shouldReturnTrueIfVoterIsExist() throws Exception {
        //arrange
        String voterId1 = voterDao.addNewVoter(voter1);
        String voterId2 = voterDao.addNewVoter(voter2);
        String voterId3 = "3";
        //act
        boolean expression1 = voterDao.isRegisteredVoter(voterId1);
        boolean expression2 = voterDao.isRegisteredVoter(voterId2);
        boolean expression3 = voterDao.isRegisteredVoter(voterId3);
        //assert
        assertTrue(expression1);
        assertTrue(expression2);
        assertFalse(expression3);
    }
}
