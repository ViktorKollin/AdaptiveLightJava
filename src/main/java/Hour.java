import java.time.LocalDateTime;

public class Hour {
    private double price;
    private int batteryPercent;
    private boolean charge;
    private boolean LedOn;
    private LocalDateTime localDateTime;
    private int ppfdSun;
    private int hourOfDay;
    private double dliReached = 0;

    public Hour(int ppfdSun, int hourOfDay) {
        this.ppfdSun = ppfdSun;
        this.hourOfDay = hourOfDay;
    }

    public Hour(double price, int batteryPercent, boolean ledOn, LocalDateTime localDateTime) {
        this.price = price;
        this.batteryPercent = batteryPercent;
        LedOn = ledOn;
        this.localDateTime = localDateTime;
    }

    public Hour(double price, LocalDateTime localDateTime) {
        this.price = price;
        this.localDateTime = localDateTime;
    }

    public double getDliReached() {
        return dliReached;
    }

    public void setDliReached(double dliReached) {
        this.dliReached = dliReached;
    }

    public Hour(double price, boolean ledOn, LocalDateTime localDateTime) {
        this.price = price;
        this.LedOn = ledOn;
        this.localDateTime = localDateTime;
    }


    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
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


    public int getPpfdSun() {
        return ppfdSun;
    }

    public void setPpfdSun(int ppfdSun) {
        this.ppfdSun = ppfdSun;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }
}
