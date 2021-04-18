package net.thumbtack.school.elections.daoimpl;

import net.thumbtack.school.elections.dao.VoteDao;
import net.thumbtack.school.elections.model.Vote;
import net.thumbtack.school.elections.server.DataBaseManagementSystem;

import java.util.ArrayList;
import java.util.List;

public class VoteDaoImpl implements VoteDao {

    private static volatile VoteDaoImpl instance;

    private DataBaseManagementSystem dbms = DataBaseManagementSystem.getInstance();

    private VoteDaoImpl() {
    }

    static {
        instance = new VoteDaoImpl();
    }

    public static VoteDaoImpl getInstance() {
        return instance;
    }

    @Override
    public String addVote(Vote vote) {
        return dbms.insertVoteToDataBase(vote);
    }

    @Override
    public boolean isExistVote(String voterId) {
        return dbms.getVoteMap()
                .keySet()
                .stream()
                .anyMatch(k -> k.equals(voterId));
    }

    @Override
    public List<Vote> getVotes() {
        return new ArrayList<>(dbms.getVoteMap().values());
    }

    @Override
    public String deleteAllVotes() {
        return dbms.deleteAllVotesFromDataBase();
    }


}
