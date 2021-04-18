package net.thumbtack.school.elections.model;

public class CandidateNominateRequest {

    private String id;
    private String nomineeId;
    private String initiatorId;

    public CandidateNominateRequest() {
    }

    public CandidateNominateRequest(String nomineeId, String initiatorId) {
        this.nomineeId = nomineeId;
        this.initiatorId = initiatorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomineeId() {
        return nomineeId;
    }

    public void setNomineeId(String nomineeId) {
        this.nomineeId = nomineeId;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    @Override
    public String toString() {
        return "CandidateNominateRequest{" +
                "id='" + id + '\'' +
                ", nomineeId='" + nomineeId + '\'' +
                ", initiatorId='" + initiatorId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateNominateRequest request = (CandidateNominateRequest) o;

        if (id != null ? !id.equals(request.id) : request.id != null) return false;
        if (nomineeId != null ? !nomineeId.equals(request.nomineeId) : request.nomineeId != null) return false;
        return initiatorId != null ? initiatorId.equals(request.initiatorId) : request.initiatorId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nomineeId != null ? nomineeId.hashCode() : 0);
        result = 31 * result + (initiatorId != null ? initiatorId.hashCode() : 0);
        return result;
    }
}
