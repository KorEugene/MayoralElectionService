package net.thumbtack.school.elections.server;

import net.thumbtack.school.elections.model.*;

import java.util.HashMap;
import java.util.Map;

public class DataBaseManagementSystem {

    private static volatile DataBaseManagementSystem instance;

    private DataBase db = DataBase.getInstance();

    private DataBaseManagementSystem() {
    }

    static {
        instance = new DataBaseManagementSystem();
    }

    public static DataBaseManagementSystem getInstance() {
        return instance;
    }

    public int getVoterId() {
        return db.getVoterId();
    }

    public int getOfferId() {
        return db.getOfferId();
    }

    public int getRatingId() {
        return db.getRatingId();
    }

    public int getCandidateId() {
        return db.getCandidateId();
    }

    public int getProgramId() {
        return db.getProgramId();
    }

    public int getNominateRequestId() {
        return db.getNominateRequestId();
    }

    public int getVoteId() {
        return db.getVoteId();
    }

    public Map<String, String> getTokenMap() {
        return db.getTokenMap();
    }

    public Map<String, Person> getPersonMap() {
        return db.getPersonMap();
    }

    public Map<String, Voter> getVoterMap() {
        return db.getVoterMap();
    }

    public Map<String, Offer> getOfferMap() {
        return db.getOfferMap();
    }

    public Map<String, Rating> getRatingMap() {
        return db.getRatingMap();
    }

    public Map<String, Candidate> getCandidateMap() {
        return db.getCandidateMap();
    }

    public Map<String, CandidateProgram> getProgramMap() {
        return db.getProgramMap();
    }

    public Map<String, CandidateNominateRequest> getNominateRequests() {
        return db.getNominateRequests();
    }

    public Map<String, Vote> getVoteMap() {
        return db.getVoteMap();
    }

    public String insertNewTokenToDataBase(String voterId, String token) {
        db.getTokenMap().put(token, voterId);
        db.getVoterMap().get(voterId).setToken(token);
        return ResponseStatusMessage.TOKEN_UPDATED.getMessage();
    }

    public String removeTokenFromDataBase(String token) {
        db.getTokenMap().remove(token);
        return ResponseStatusMessage.TOKEN_REMOVED.getMessage();
    }

    public String insertNewVoterToDataBase(Voter voter) {
        voter.getPerson().setId(generateVoterId());
        db.getTokenMap().put(voter.getToken(), voter.getPerson().getId());
        db.getPersonMap().put(voter.getPerson().getId(), voter.getPerson());
        db.getVoterMap().put(voter.getPerson().getId(), voter);
        return db.getVoterMap().get(voter.getPerson().getId()).getPerson().getId();
    }

    public String insertNewOfferToDataBase(Offer offer) {
        offer.setId(generateOfferId());
        db.getOfferMap().put(offer.getId(), offer);
        return offer.getId();
    }

    public String updateOfferCopyrights(String offerId, boolean status) {
        db.getOfferMap().get(offerId).setCopyrightsActive(status);
        return ResponseStatusMessage.COPYRIGHTS_CHANGED.getMessage();
    }

    public String insertNewRatingToDataBase(Rating rating) {
        rating.setId(generateRatingId());
        db.getRatingMap().put(rating.getId(), rating);
        return rating.getId();
    }

    public String deleteRatingFromDataBase(String ratingId) {
        db.getRatingMap().remove(ratingId);
        return ResponseStatusMessage.RATING_STATUS_DELETED.getMessage();
    }

    public String updateRatingInDataBase(String ratingId, int value) {
        Rating sourceRating = db.getRatingMap().get(ratingId);
        sourceRating.setValue(value);
        return ResponseStatusMessage.RATING_STATUS_UPDATED.getMessage();
    }

    public String insertNewCandidateToDataBase(Candidate candidate) {
        candidate.setId(generateCandidateId());
        db.getCandidateMap().put(candidate.getId(), candidate);
        return candidate.getId();
    }

    public String deleteCandidateFromDataBase(String candidateId) {
        db.getCandidateMap().remove(candidateId);
        return ResponseStatusMessage.CANDIDACY_HAS_BEEN_WITHDRAWN.getMessage();
    }

    public String insertNewNominateRequest(CandidateNominateRequest request) {
        request.setId(generateNominateRequestsId());
        db.getNominateRequests().put(request.getId(), request);
        return request.getId();
    }

    public String deleteNominateRequest(String nominateRequestId) {
        db.getNominateRequests().remove(nominateRequestId);
        return ResponseStatusMessage.NOMINATE_REQUEST_REMOVED.getMessage();
    }

    public String insertCandidateProgram(CandidateProgram candidateProgram) {
        candidateProgram.setId(generateProgramId());
        db.getProgramMap().put(candidateProgram.getId(), candidateProgram);
        return candidateProgram.getId();
    }

    public String deleteCandidateProgram(String candidateProgramId) {
        db.getProgramMap().remove(candidateProgramId);
        return ResponseStatusMessage.OFFER_REMOVED_FROM_CANDIDATE_PROGRAM.getMessage();
    }

    public String insertVoteToDataBase(Vote vote) {
        vote.setId(generateVoteId());
        db.getVoteMap().put(vote.getId(), vote);
        return vote.getId();
    }

    public String deleteAllVotesFromDataBase() {
        db.setVoteMap(new HashMap<>());
        return ResponseStatusMessage.ALL_VOTES_HAVE_BEEN_DELETED.getMessage();
    }

    private String generateVoterId() {
        int currentId = getVoterId();
        String id = String.valueOf(currentId);
        db.setVoterId(currentId + 1);
        return id;
    }

    private String generateOfferId() {
        int currentId = getOfferId();
        String id = String.valueOf(currentId);
        db.setOfferId(currentId + 1);
        return id;
    }

    private String generateRatingId() {
        int currentId = getRatingId();
        String id = String.valueOf(currentId);
        db.setRatingId(currentId + 1);
        return id;
    }

    private String generateCandidateId() {
        int currentId = getCandidateId();
        String id = String.valueOf(currentId);
        db.setCandidateId(currentId + 1);
        return id;
    }

    private String generateProgramId() {
        int currentId = getProgramId();
        String id = String.valueOf(currentId);
        db.setProgramId(currentId + 1);
        return id;
    }

    private String generateNominateRequestsId() {
        int currentId = getNominateRequestId();
        String id = String.valueOf(currentId);
        db.setNominateRequestId(currentId + 1);
        return id;
    }

    private String generateVoteId() {
        int currentId = getVoteId();
        String id = String.valueOf(currentId);
        db.setVoteId(currentId + 1);
        return id;
    }
}
