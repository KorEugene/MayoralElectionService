package net.thumbtack.school.elections.request;

public class AnswerNominateRequestDtoRequest {

    private String token;
    private String nominateRequestId;
    private boolean consent;

    public AnswerNominateRequestDtoRequest(String token, String nominateRequestId, boolean consent) {
        this.token = token;
        this.nominateRequestId = nominateRequestId;
        this.consent = consent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNominateRequestId() {
        return nominateRequestId;
    }

    public void setNominateRequestId(String nominateRequestId) {
        this.nominateRequestId = nominateRequestId;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }
}
