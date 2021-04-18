package net.thumbtack.school.elections.model;

public class Vote {

    private String id;
    private String voterId;
    private String candidateId;

    public Vote(String voterId, String candidateId) {
        this.voterId = voterId;
        this.candidateId = candidateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id='" + id + '\'' +
                ", voterId='" + voterId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote vote = (Vote) o;

        if (id != null ? !id.equals(vote.id) : vote.id != null) return false;
        if (voterId != null ? !voterId.equals(vote.voterId) : vote.voterId != null) return false;
        return candidateId != null ? candidateId.equals(vote.candidateId) : vote.candidateId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (voterId != null ? voterId.hashCode() : 0);
        result = 31 * result + (candidateId != null ? candidateId.hashCode() : 0);
        return result;
    }
}
