package net.thumbtack.school.elections.dao;

import net.thumbtack.school.elections.model.CandidateNominateRequest;

import java.util.List;

public interface NominateRequestDao {

    String addNominateRequest(CandidateNominateRequest request);

    List<CandidateNominateRequest> getNominateRequestsByVoterId(String voterId);

    String removeNominateRequest(String nominateRequestId);

    boolean isRegisteredNominateRequest(String nomineeId);
}
