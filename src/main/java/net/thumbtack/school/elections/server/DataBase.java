package net.thumbtack.school.elections.server;

import net.thumbtack.school.elections.model.*;

import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static volatile DataBase instance;

    private int voterId;
    private int offerId;
    private int ratingId;
    private int candidateId;
    private int programId;
    private int nominateRequestId;
    private int voteId;

    private Map<String, String> tokenMap;
    private Map<String, Person> personMap;
    private Map<String, Voter> voterMap;
    private Map<String, Offer> offerMap;
    private Map<String, Rating> ratingMap;
    private Map<String, Candidate> candidateMap;
    private Map<String, CandidateProgram> programMap;
    private Map<String, CandidateNominateRequest> nominateRequests;
    private Map<String, Vote> voteMap;

    private DataBase() {
        voterId = 1;
        offerId = 1;
        ratingId = 1;
        candidateId = 1;
        programId = 1;
        nominateRequestId = 1;
        voteId = 1;
        tokenMap = new HashMap<>();
        personMap = new HashMap<>();
        voterMap = new HashMap<>();
        offerMap = new HashMap<>();
        ratingMap = new HashMap<>();
        candidateMap = new HashMap<>();
        programMap = new HashMap<>();
        nominateRequests = new HashMap<>();
        voteMap = new HashMap<>();
    }

    static {
        instance = new DataBase();
    }

    public static DataBase getInstance() {
        return instance;
    }

    public static void clearInstance() {
        instance.setTokenMap(new HashMap<>());
        instance.setPersonMap(new HashMap<>());
        instance.setVoterMap(new HashMap<>());
        instance.setOfferMap(new HashMap<>());
        instance.setRatingMap(new HashMap<>());
        instance.setCandidateMap(new HashMap<>());
        instance.setProgramMap(new HashMap<>());
        instance.setNominateRequests(new HashMap<>());
        instance.setVoteMap(new HashMap<>());
        instance.setVoterId(1);
        instance.setOfferId(1);
        instance.setRatingId(1);
        instance.setCandidateId(1);
        instance.setProgramId(1);
        instance.setNominateRequestId(1);
        instance.setVoteId(1);
    }

    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getNominateRequestId() {
        return nominateRequestId;
    }

    public void setNominateRequestId(int nominateRequestId) {
        this.nominateRequestId = nominateRequestId;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public static void setInstance(DataBase instance) {
        DataBase.instance = instance;
    }

    public Map<String, String> getTokenMap() {
        return tokenMap;
    }

    public void setTokenMap(Map<String, String> tokenMap) {
        this.tokenMap = tokenMap;
    }

    public Map<String, Person> getPersonMap() {
        return personMap;
    }

    public void setPersonMap(Map<String, Person> personMap) {
        this.personMap = personMap;
    }

    public Map<String, Voter> getVoterMap() {
        return voterMap;
    }

    public void setVoterMap(Map<String, Voter> voterMap) {
        this.voterMap = voterMap;
    }

    public Map<String, Offer> getOfferMap() {
        return offerMap;
    }

    public void setOfferMap(Map<String, Offer> offerMap) {
        this.offerMap = offerMap;
    }

    public Map<String, Rating> getRatingMap() {
        return ratingMap;
    }

    public void setRatingMap(Map<String, Rating> ratingMap) {
        this.ratingMap = ratingMap;
    }

    public Map<String, Candidate> getCandidateMap() {
        return candidateMap;
    }

    public void setCandidateMap(Map<String, Candidate> candidateMap) {
        this.candidateMap = candidateMap;
    }

    public Map<String, CandidateProgram> getProgramMap() {
        return programMap;
    }

    public void setProgramMap(Map<String, CandidateProgram> programMap) {
        this.programMap = programMap;
    }

    public Map<String, CandidateNominateRequest> getNominateRequests() {
        return nominateRequests;
    }

    public void setNominateRequests(Map<String, CandidateNominateRequest> nominateRequests) {
        this.nominateRequests = nominateRequests;
    }

    public Map<String, Vote> getVoteMap() {
        return voteMap;
    }

    public void setVoteMap(Map<String, Vote> voteMap) {
        this.voteMap = voteMap;
    }
}
