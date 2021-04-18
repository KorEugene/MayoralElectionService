package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.OfferDao;
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

public class TestOfferDaoImpl {

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();
    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private OfferDao offerDao = OfferDaoImpl.getInstance();
    private static FullName fullName1 = new FullName("Ivan", "Ivanov", "Ivanovich");
    private static Address address1 = new Address("Fedora Abramova", "15", "27");
    private static Account account1 = new Account("IVA", "123456");
    private static Voter voter1 = new Voter(new Person(fullName1, address1, account1));
    private static String voterId1;
    private static FullName fullName2 = new FullName("Petr", "Petrov", "Petrovich");
    private static Address address2 = new Address("Primorskaya", "35", "4");
    private static Account account2 = new Account("PETYA", "654321");
    private static Voter voter2 = new Voter(new Person(fullName2, address2, account2));
    private static String voterId2;

    @Before
    public void generateData() {
        voter1.setToken(TokenGenerator.generateNewToken());
        voter2.setToken(TokenGenerator.generateNewToken());
        voterId1 = voterDao.addNewVoter(voter1);
        voterId2 = voterDao.addNewVoter(voter2);
    }

    @After
    public void cleanData() {
        DataBase.clearInstance();
    }

    @Test
    public void shouldAddNewOfferToDataBaseWithValidData() throws Exception {
        //arrange
        String content = "TEST";
        Offer offer1 = new Offer(voterId1, content);
        Offer offer2 = new Offer(voterId2, content);
        //act
        String offerId1 = offerDao.addNewOffer(offer1);
        String offerId2 = offerDao.addNewOffer(offer2);
        String correctResponse1 = dbms.getOfferMap().get(offerId1).getId();
        String correctResponse2 = dbms.getOfferMap().get(offerId2).getId();
        //assert
        assertEquals(2, dbms.getOfferMap().size());
        assertEquals(correctResponse1, offerId1);
        assertEquals(correctResponse2, offerId2);
    }

    @Test
    public void shouldReturnOfferByIdFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        Offer offer1 = new Offer(voterId1, content);
        Offer offer2 = new Offer(voterId2, content);
        offerDao.addNewOffer(offer1);
        offerDao.addNewOffer(offer2);
        String offerId1 = offer1.getId();
        String offerId2 = offer2.getId();
        //act
        Offer offer3 = offerDao.getOfferById(offerId1);
        Offer offer4 = offerDao.getOfferById(offerId2);
        //assert
        assertEquals(offer1, offer3);
        assertEquals(offer2, offer4);
    }

    @Test
    public void shouldChangeOfferCopyrights() throws Exception {
        //arrange
        String content = "TEST";
        Offer offer1 = new Offer(voterId1, content);
        Offer offer2 = new Offer(voterId2, content);
        offerDao.addNewOffer(offer1);
        offerDao.addNewOffer(offer2);
        String offerId1 = offer1.getId();
        String offerId2 = offer2.getId();
        //act
        String message1 = offerDao.updateOfferCopyrights(offerId1, false);
        String message2 = offerDao.updateOfferCopyrights(offerId2, false);
        //assert
        assertEquals(2, offerDao.getOffers().size());
        assertFalse(offer1.isCopyrightsActive());
        assertFalse(offer2.isCopyrightsActive());
        assertEquals(ResponseStatusMessage.COPYRIGHTS_CHANGED.getMessage(), message1);
        assertEquals(ResponseStatusMessage.COPYRIGHTS_CHANGED.getMessage(), message2);
    }

    @Test
    public void shouldReturnOfferListFromDataBase() throws Exception {
        //arrange
        String content = "TEST";
        Offer offer1 = new Offer(voterId1, content);
        Offer offer2 = new Offer(voterId2, content);
        offerDao.addNewOffer(offer1);
        offerDao.addNewOffer(offer2);
        String offerId1 = offer1.getId();
        String offerId2 = offer2.getId();
        //act
        List<Offer> offerList1 = offerDao.getOffers();
        //assert
        assertEquals(2, dbms.getOfferMap().size());
        assertEquals(dbms.getOfferMap().get(offerId1), offerList1.get(0));
        assertEquals(dbms.getOfferMap().get(offerId2), offerList1.get(1));
    }

    @Test
    public void shouldReturnOfferListByAuthorId() throws Exception {
        //arrange
        String content = "TEST";
        Offer offer1 = new Offer(voterId1, content);
        Offer offer2 = new Offer(voterId2, content);
        Offer offer3 = new Offer(voterId1, "TEST2");
        offerDao.addNewOffer(offer1);
        offerDao.addNewOffer(offer2);
        offerDao.addNewOffer(offer3);
        //act
        List<Offer> offers1 = offerDao.getOffersByAuthorId(voterId1);
        List<Offer> offers2 = offerDao.getOffersByAuthorId(voterId2);
        //assert
        assertTrue(offers1.contains(offer1));
        assertTrue(offers1.contains(offer3));
        assertTrue(offers2.contains(offer2));
    }
}
