package sample.Utilities;

public class MySanctions {
    private String sanctionFor;
    private String sanction;

    public MySanctions(String sanctionFor, String sanction) {
        this.sanctionFor = sanctionFor;
        this.sanction = sanction;
    }

    public String getSanctionFor() {
        return sanctionFor;
    }

    public void setSanctionFor(String sanctionFor) {
        this.sanctionFor = sanctionFor;
    }

    public String getSanction() {
        return sanction;
    }

    public void setSanction(String sanction) {
        this.sanction = sanction;
    }
}
