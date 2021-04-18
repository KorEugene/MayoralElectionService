package net.thumbtack.school.elections.response;

import net.thumbtack.school.elections.model.CandidateNominateRequest;

import java.util.List;

public class CheckRequestsDtoResponse {

    private List<CandidateNominateRequest> requests;

    public CheckRequestsDtoResponse(List<CandidateNominateRequest> requests) {
        this.requests = requests;
    }

    public List<CandidateNominateRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<CandidateNominateRequest> requests) {
        this.requests = requests;
    }
}
