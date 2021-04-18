package net.thumbtack.school.elections.model;

public enum FieldType {
    TOKEN("token"),
    VOTER_ID("voterId"),
    OFFER_ID("offerId"),
    RATING_ID("ratingId"),
    CANDIDATE_ID("candidateId"),
    PROGRAM_ID("programId"),
    NOMINATE_REQUEST_ID("nominateRequestId"),
    VOTE_ID("voteId"),
    TOKEN_MAP("tokenMap"),
    PERSON_MAP("personMap"),
    VOTER_MAP("voterMap"),
    OFFER_MAP("offerMap"),
    RATING_MAP("ratingMap"),
    CANDIDATE_MAP("candidateMap"),
    PROGRAM_MAP("programMap"),
    NOMINATE_REQUESTS("nominateRequests"),
    VOTE_MAP("voteMap"),
    ID("id"),
    LOGIN("login"),
    PASSWORD("password"),
    AUTHOR_ID("authorId"),
    NOMINEE_ID("nomineeId"),
    ERROR("error"),
    CONTENT("content"),
    VALUE("value"),
    MESSAGE("message"),
    CONSENT("consent"),
    ADDITION("addition");

    private String type;

    FieldType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
