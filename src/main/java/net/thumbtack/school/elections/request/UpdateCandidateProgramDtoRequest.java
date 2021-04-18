package net.thumbtack.school.elections.request;

public class UpdateCandidateProgramDtoRequest {

    private String token;
    private String offerId;
    private boolean addition;

    public UpdateCandidateProgramDtoRequest(String token, String offerId, boolean addition) {
        this.token = token;
        this.offerId = offerId;
        this.addition = addition;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public boolean isAddition() {
        return addition;
    }

    public void setAddition(boolean addition) {
        this.addition = addition;
    }
}
