public class Hour {
    private int price;
    private int batteryPercent;
    private boolean charge;
    private boolean LedOn;

    public Hour(int price, boolean ledOn) {
        this.price = price;
        LedOn = ledOn;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    public void setBatteryPercent(int batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public boolean isLedOn() {
        return LedOn;
    }

    public void setLedOn(boolean ledOn) {
        LedOn = ledOn;
    }


}
