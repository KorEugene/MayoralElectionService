package net.thumbtack.school.elections.request;

public class GetCandidateListWithProgramsDtoRequest {

    private String token;

    public GetCandidateListWithProgramsDtoRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
