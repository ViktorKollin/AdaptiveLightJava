import java.time.LocalDateTime;

public class Hour {
    private double price;
    private int batteryPercent;
    private boolean charge;
    private boolean LedOn;
    private LocalDateTime localDateTime;

    public Hour(double price, boolean ledOn, LocalDateTime localDateTime) {
        this.price = price;
        this.LedOn = ledOn;
        this.localDateTime = localDateTime;
    }


    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
