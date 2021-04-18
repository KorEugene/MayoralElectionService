package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.Candidate;

import java.util.List;

public interface CandidateDao {

    String addNewCandidate(Candidate candidate);

    Candidate getCandidateById(String candidateId);

    Candidate getCandidateByToken(String token);

    String deleteCandidate(String candidateId);

    List<Candidate> getCandidates();

    boolean isRegisteredCandidate(String voterId);
}
