package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.Voter;

import java.util.List;

public interface VoterDao {

    String updateToken(String voterId, String token);

    String removeToken(String token);

    String addNewVoter(Voter voter);

    Voter getVoterById(String voterId);

    List<Voter> getVoters();

    String getVoterIdByToken(String token);

    String getVoterTokenById(String voterId);

    boolean isRegisteredVoter(String voterId);
}
