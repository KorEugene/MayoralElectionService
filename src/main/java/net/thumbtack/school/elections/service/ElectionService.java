package net.thumbtack.school.elections.service;

import net.thumbtack.school.elections.dao.CandidateDao;
import net.thumbtack.school.elections.dao.CandidateProgramDao;
import net.thumbtack.school.elections.dao.VoteDao;
import net.thumbtack.school.elections.dao.VoterDao;
import net.thumbtack.school.elections.daoimpl.CandidateDaoImpl;
import net.thumbtack.school.elections.daoimpl.CandidateProgramDaoImpl;
import net.thumbtack.school.elections.daoimpl.VoteDaoImpl;
import net.thumbtack.school.elections.daoimpl.VoterDaoImpl;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.request.StartVotingDtoRequest;
import net.thumbtack.school.elections.request.StopVotingDtoRequest;
import net.thumbtack.school.elections.request.VoteInElectionsDtoRequest;
import net.thumbtack.school.elections.response.StartVotingDtoResponse;
import net.thumbtack.school.elections.response.StopVotingDtoResponse;
import net.thumbtack.school.elections.response.VoteInElectionsDtoResponse;
import net.thumbtack.school.elections.server.Server;
import net.thumbtack.school.elections.validators.CandidateValidator;
import net.thumbtack.school.elections.validators.ElectionValidator;
import net.thumbtack.school.elections.validators.ObjectSavedToDataBaseValidator;
import net.thumbtack.school.elections.validators.TokenValidator;

import java.util.*;
import java.util.stream.Collectors;

public class ElectionService {

    private static final String ZERO = "0";

    private VoterDao voterDao = VoterDaoImpl.getInstance();
    private VoteDao voteDao = VoteDaoImpl.getInstance();
    private CandidateDao candidateDao = CandidateDaoImpl.getInstance();
    private CandidateProgramDao candidateProgramDao = CandidateProgramDaoImpl.getInstance();
    private TokenValidator tokenValidator = new TokenValidator();
    private ElectionValidator electionValidator = new ElectionValidator();
    private CandidateValidator candidateValidator = new CandidateValidator();
    private ObjectSavedToDataBaseValidator objectSavedToDataBaseValidator = new ObjectSavedToDataBaseValidator();

    public StartVotingDtoResponse startVoting(StartVotingDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        List<Candidate> candidates = candidateDao.getCandidates();
        electionValidator.validateCandidateList(candidates);
        List<CandidateProgram> programs = candidateProgramDao.getPrograms();
        for (Candidate candidate : candidates) {
            List<CandidateProgram> currentCandidatePrograms = programs
                    .stream()
                    .filter(p -> p.getCandidateId().equals(candidate.getId()))
                    .collect(Collectors.toList());
            if (currentCandidatePrograms.isEmpty()) {
                candidateDao.deleteCandidate(candidate.getId());
            }
        }
        Server.getInstance().setVotingStatus(true);
        return new StartVotingDtoResponse(ResponseStatusMessage.VOTING_STARTED.getMessage());
    }

    public VoteInElectionsDtoResponse voteInElections(VoteInElectionsDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        String voterId = voterDao.getVoterIdByToken(request.getToken());
        String candidateId = request.getCandidateId();
        boolean alreadyVoted = voteDao.isExistVote(voterId);
        electionValidator.validateVote(alreadyVoted);
        Vote vote = new Vote(voterId, candidateId);
        VoteInElectionsDtoResponse response;
        if (candidateId.equals(ZERO)) {
            String voteId = voteDao.addVote(vote);
            objectSavedToDataBaseValidator.validateVoteId(voteId);
            response = new VoteInElectionsDtoResponse(ResponseStatusMessage.VOTED.getMessage());
        } else {
            Candidate candidate = candidateDao.getCandidateById(candidateId);
            candidateValidator.validateCandidateIsExist(candidate);
            String voteId = voteDao.addVote(vote);
            objectSavedToDataBaseValidator.validateVoteId(voteId);
            response = new VoteInElectionsDtoResponse(ResponseStatusMessage.VOTED.getMessage());
        }
        return response;
    }

    public StopVotingDtoResponse stopVoting(StopVotingDtoRequest request) {
        tokenValidator.validateToken(request.getToken());
        Map<String, Long> results = new HashMap<>();
        List<Candidate> candidates = candidateDao.getCandidates();
        List<Vote> votes = voteDao.getVotes();
        for (Candidate candidate : candidates) {
            long numberOfVotesForCandidate = votes
                    .stream()
                    .filter(v -> v.getCandidateId().equals(candidate.getId()))
                    .count();
            results.put(candidate.getId(), numberOfVotesForCandidate);
        }
        long numberOfVotesAgainstAll = votes
                .stream()
                .filter(v -> v.getCandidateId().equals(ZERO))
                .count();
        results.put(ZERO, numberOfVotesAgainstAll);
        String winnerId = Collections
                .max(results.entrySet(), Comparator.comparingLong(Map.Entry::getValue))
                .getKey();
        StopVotingDtoResponse response;
        if (winnerId.equals(ZERO)) {
            response = new StopVotingDtoResponse(ResponseStatusMessage.NO_WINNERS.getMessage());
        } else {
            FullName fullName = candidateDao.getCandidateById(winnerId).getVoter().getPerson().getFullName();
            response = new StopVotingDtoResponse(fullName + ResponseStatusMessage.WINNER.getMessage());
        }
        Server.getInstance().setVotingStatus(false);
        String message = voteDao.deleteAllVotes();
        electionValidator.validateDataBaseCleanup(message);
        return response;
    }
}
