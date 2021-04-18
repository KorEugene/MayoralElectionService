package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.model.Voter;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;

public class VoterDaoImpl implements VoterDao {

    private static volatile VoterDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private VoterDaoImpl() {
    }

    static {
        instance = new VoterDaoImpl();
    }

    public static VoterDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String updateToken(String voterId, String token) {
        return dbms.insertNewTokenToDataBase(voterId, token);
    }

    @Override
    public String removeToken(String token) {
        return dbms.removeTokenFromDataBase(token);
    }

    @Override
    public String addNewVoter(Voter voter) {
        return dbms.insertNewVoterToDataBase(voter);
    }

    @Override
    public Voter getVoterById(String voterId) {
        return dbms.getVoterMap().get(voterId);
    }

    @Override
    public List<Voter> getVoters() {
        return new ArrayList<>(dbms.getVoterMap().values());
    }

    @Override
    public String getVoterIdByToken(String token) {
        return dbms.getTokenMap().get(token);
    }

    @Override
    public String getVoterTokenById(String voterId) {
        return dbms.getVoterMap().get(voterId).getToken();
    }

    @Override
    public boolean isRegisteredVoter(String voterId) {
        return dbms.getVoterMap()
                .keySet()
                .stream()
                .anyMatch(s -> s.equals(voterId));
    }
}
