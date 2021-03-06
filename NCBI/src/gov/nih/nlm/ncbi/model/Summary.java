package gov.nih.nlm.ncbi.model;

public class Summary {

    private Long id = null;
    private String data = null;

    public Summary(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    public Summary() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}