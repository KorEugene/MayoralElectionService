package net.thumbtack.school.elections.model;

public enum ResponseStatusMessage {
    TOKEN_UPDATED("Token updated!"),
    TOKEN_REMOVED("Token removed!"),
    COPYRIGHTS_CHANGED("Copyrights changed!"),
    RATING_STATUS_CREATED("New rating has been added!"),
    RATING_STATUS_UPDATED("Existing rating has been updated!"),
    RATING_STATUS_DELETED("Existing rating has been deleted!"),
    NOMINATE_REQUEST_REMOVED("Request successfully removed!"),
    OFFER_ADDED_TO_CANDIDATE_PROGRAM("New offer was added to candidate program!"),
    OFFER_REMOVED_FROM_CANDIDATE_PROGRAM("Offer has been removed from candidate program!"),
    CANDIDACY_HAS_BEEN_WITHDRAWN("Candidacy has been withdrawn!"),
    SUCCESSFULLY_LOGGED_ON("Logged on!"),
    SUCCESSFULLY_LOGGED_OUT("Logged out!"),
    VOTING_STARTED("Voting started!"),
    VOTED("You voted!"),
    NO_WINNERS("The mayor was not elected!"),
    WINNER(" becomes the new mayor!"),
    ALL_VOTES_HAVE_BEEN_DELETED("All votes have been deleted from DataBase!");

    private String message;

    ResponseStatusMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
