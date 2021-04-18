package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.Vote;

import java.util.List;

public interface VoteDao {

    String addVote(Vote vote);

    boolean isExistVote(String voterId);

    List<Vote> getVotes();

    String deleteAllVotes();
}
