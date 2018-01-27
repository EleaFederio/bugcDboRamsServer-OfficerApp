package sample.Utilities;

public class SanctionData {
    private String eventOrFees;
    private String sanction;

    public SanctionData(String eventOrFees, String sanction) {
        this.eventOrFees = eventOrFees;
        this.sanction = sanction;
    }

    public String getEventOrFees() {
        return eventOrFees;
    }

    public void setEventOrFees(String eventOrFees) {
        this.eventOrFees = eventOrFees;
    }

    public String getSanction() {
        return sanction;
    }

    public void setSanction(String sanction) {
        this.sanction = sanction;
    }
}
