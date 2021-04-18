package net.thumbtack.school.elections.serializerimpl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.elections.model.*;
import net.thumbtack.school.elections.serializer.DataBaseReader;
import net.thumbtack.school.elections.server.DataBase;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class DataBaseJsonReader implements DataBaseReader {

    private DataBase db = DataBase.getInstance();
    private Gson gson = new Gson();

    @Override
    public void loadDataBase(String filePath) throws IOException {
        try (FileReader fileReader = new FileReader(filePath)) {

            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);

            db.setVoterId(Integer.parseInt(jsonObject.get(FieldType.VOTER_ID.getType()).getAsString()));
            db.setOfferId(Integer.parseInt(jsonObject.get(FieldType.OFFER_ID.getType()).getAsString()));
            db.setRatingId(Integer.parseInt(jsonObject.get(FieldType.RATING_ID.getType()).getAsString()));
            db.setCandidateId(Integer.parseInt(jsonObject.get(FieldType.CANDIDATE_ID.getType()).getAsString()));
            db.setProgramId(Integer.parseInt(jsonObject.get(FieldType.PROGRAM_ID.getType()).getAsString()));
            db.setNominateRequestId(Integer.parseInt(jsonObject.get(FieldType.NOMINATE_REQUEST_ID.getType()).getAsString()));
            db.setVoteId(Integer.parseInt(jsonObject.get(FieldType.VOTE_ID.getType()).getAsString()));

            JsonElement jsonElement;

            jsonElement = jsonObject.get(FieldType.TOKEN_MAP.getType());
            Type tokenMapType = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> loadedTokenMap = gson.fromJson(jsonElement, tokenMapType);
            db.setTokenMap(loadedTokenMap);

            jsonElement = jsonObject.get(FieldType.PERSON_MAP.getType());
            Type personMapType = new TypeToken<Map<String, Person>>() {
            }.getType();
            Map<String, Person> loadedPersonMap = gson.fromJson(jsonElement, personMapType);
            db.setPersonMap(loadedPersonMap);

            jsonElement = jsonObject.get(FieldType.VOTER_MAP.getType());
            Type voterMapType = new TypeToken<Map<String, Voter>>() {
            }.getType();
            Map<String, Voter> loadedVoterMap = gson.fromJson(jsonElement, voterMapType);
            db.setVoterMap(loadedVoterMap);

            jsonElement = jsonObject.get(FieldType.OFFER_MAP.getType());
            Type offerMapType = new TypeToken<Map<String, Offer>>() {
            }.getType();
            Map<String, Offer> loadedOfferMap = gson.fromJson(jsonElement, offerMapType);
            db.setOfferMap(loadedOfferMap);

            jsonElement = jsonObject.get(FieldType.RATING_MAP.getType());
            Type ratingMapType = new TypeToken<Map<String, Rating>>() {
            }.getType();
            Map<String, Rating> loadedRatingMap = gson.fromJson(jsonElement, ratingMapType);
            db.setRatingMap(loadedRatingMap);

            jsonElement = jsonObject.get(FieldType.CANDIDATE_MAP.getType());
            Type candidateMapType = new TypeToken<Map<String, Candidate>>() {
            }.getType();
            Map<String, Candidate> loadedCandidateMap = gson.fromJson(jsonElement, candidateMapType);
            db.setCandidateMap(loadedCandidateMap);

            jsonElement = jsonObject.get(FieldType.PROGRAM_MAP.getType());
            Type programMapType = new TypeToken<Map<String, CandidateProgram>>() {
            }.getType();
            Map<String, CandidateProgram> loadedProgramMap = gson.fromJson(jsonElement, programMapType);
            db.setProgramMap(loadedProgramMap);

            jsonElement = jsonObject.get(FieldType.NOMINATE_REQUESTS.getType());
            Type nominateRequestsMapType = new TypeToken<Map<String, CandidateNominateRequest>>() {
            }.getType();
            Map<String, CandidateNominateRequest> loadedNominateRequestsMap = gson.fromJson(jsonElement, nominateRequestsMapType);
            db.setNominateRequests(loadedNominateRequestsMap);

            jsonElement = jsonObject.get(FieldType.VOTE_MAP.getType());
            Type voteMapType = new TypeToken<Map<String, Vote>>() {
            }.getType();
            Map<String, Vote> loadedVoteMap = gson.fromJson(jsonElement, voteMapType);
            db.setVoteMap(loadedVoteMap);
        }
    }
}
