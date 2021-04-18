package net.thumbtack.school.elections.response;

public class AddCandidateDtoResponse {

    private String type;
    private String id;

    public AddCandidateDtoResponse(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
