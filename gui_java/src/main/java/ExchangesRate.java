public class ExchangesRate {
    private String date;
    private String value;

    public ExchangesRate(String date, String value){
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }
}
