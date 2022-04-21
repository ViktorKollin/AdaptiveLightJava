import java.time.LocalDateTime;
import java.util.*;

public class PlanGenerator {


    private TreeMap<LocalDateTime, Double> prices;
    private ArrayList<Hour> co2IntensityList;

    //private TreeMap<LocalDateTime, Double> pricesByPrice = new TreeMap<>();
    private ArrayList<Hour> dailyPlan = new ArrayList();
    private ArrayList<Hour> pricesByPrice = new ArrayList();
    private int iteration = 0;
    private EntsoeDayAhead entsoeDayAhead;
    private boolean dailyPlanExist = false;
    private final int batteryThreshhold = 20;
    private Co2IntensityCalculator co2calculator;


    public PlanGenerator(EntsoeDayAhead entsoeDayAhead) {
        this.entsoeDayAhead = entsoeDayAhead;
        co2calculator = new Co2IntensityCalculator();
    }

    public void setPrices(TreeMap<LocalDateTime, Double> prices) {
        this.prices = prices;
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {

        Comparator<K> valueComparator = new Comparator<K>() {
            public int compare(K k1, K k2) {
                int compare =
                        map.get(k1).compareTo(map.get(k2));
                if (compare == 0)
                    return 1;
                else
                    return compare;
            }
        };
        TreeMap<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);


        return sortedByValues;
    }

    public TreeMap sortMap(TreeMap prices) {
        this.prices = prices;

        return (TreeMap<LocalDateTime, Double>) sortByValues(prices);

    }

    // Sorts TreeMap of time-price by price and adds to Arraylist "PriceByPrice"
    private ArrayList putInArraylist(TreeMap sortedByValues) {

        Set set = sortedByValues.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            pricesByPrice.add(new Hour(Double.parseDouble(me.getValue().toString()), (LocalDateTime) me.getKey()));
        }
        return pricesByPrice;
    }

    // Generates the first optimal plan
    public Hour generatePlan(int battery, int currentHour, boolean dli_reached, LocalDateTime date) {

        dailyPlan.clear();
        pricesByPrice.clear();
        dailyPlanExist = true;

        if (currentHour == 15) {
            prices = entsoeDayAhead.getCostForDayAhead(date);
            co2IntensityList = co2calculator.calculateCo2Intensity(date);
            System.out.println("New Plan");

        }


        // Sets number of hours to charge based on battery%
        int chargingHours = (((100 - battery) * 8) / 100) + 1;


        putInArraylist(sortMap(prices));


        Set set = prices.entrySet();
        Iterator i = set.iterator();
        int time = 15;


        // Populates dailyPlan list with Hour-objects and gives them price and LedOn status.
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            //System.out.println(me.getKey()+" "+ me.getValue());

            if (time >= 5 && time < 21 && !dli_reached) {
                dailyPlan.add(new Hour(Double.parseDouble(me.getValue().toString()), true, (LocalDateTime) me.getKey()));
            } else {
                dailyPlan.add(new Hour(Double.parseDouble(me.getValue().toString()), false, (LocalDateTime) me.getKey()));
            }

            time++;
            if (time == 24) {
                time = 0;
                dli_reached = false;
            }
        }
        // Sets ChargeOn for the Hour objects with the lowest price.
        for (int j = 0; j < chargingHours; j++) {

            for (int k = 0; k < dailyPlan.size(); k++) {

                if (pricesByPrice.get(j).getLocalDateTime().equals(dailyPlan.get(k).getLocalDateTime())) {
                    dailyPlan.get(k).setCharge(true);
                }
            }

        }

        /////////////// PRINTS FIRST PLAN ///////////////
        // printPlan();

        // First Plan is checked. This is done recursively until optimum plan is found.
        checkPlan(currentHour, battery);


        int retIndex = 0;


        for (int hour = 0; hour < 24; hour++) {
            if (currentHour == dailyPlan.get(hour).getLocalDateTime().getHour()) {
                retIndex = hour;
            }
        }
        /////// PRINTS FINAL PLAN AFTER RECURSION ENDS /////////////////

        printPlan();

        return dailyPlan.get(retIndex);


    }

    /// Checks if current dailyPlan is possible to use without running out of battery
    //  Runs recursively together with updatePlan() until a working plan is found
    public boolean checkPlan(int currentHour, int battery) {
        int batteryNow = battery;
        //// Debug print to not get stack overflow when endless loop
        //System.out.println(iteration++);
        /*
        iteration++;

        if (iteration == 400) {
            System.exit(0);
        }


         */

        int indexCurrHour = 0;
        // finds indexCurrHour of hour that matches current hour.
        for (int i = 0; i < 24; i++) {
            if (currentHour == dailyPlan.get(i).getLocalDateTime().getHour()) {
                indexCurrHour = i;
            }
        }
        dailyPlan.get(indexCurrHour).setBatteryPercent(battery); // sets battery level of first Hour

        // Sets battery level depending on LedOn and Charge on
        // If level ever drops below 20% runs updatePlan()
        for (int i = indexCurrHour; i < dailyPlan.size() - 1; i++) {

            if (dailyPlan.get(i).isCharge()) {

                batteryNow += 13;

                if(batteryNow > 100){
                    batteryNow = 100;
                }

                dailyPlan.get(i + 1).setBatteryPercent(batteryNow);

            } else if (dailyPlan.get(i).isLedOn()) {
                batteryNow -= 4;
                dailyPlan.get(i + 1).setBatteryPercent(batteryNow);
            } else {
                dailyPlan.get(i + 1).setBatteryPercent(batteryNow);
            }

            if (batteryNow <= batteryThreshhold) {
                updatePlan(indexCurrHour, i, battery, currentHour);
                break;
            }
        }
        return true;
    }

    /**
     * @param indexOfcurrentTIme Index of Hour where plan should start.
     * @param drainIndex         Index of Hour where battery went under 20%
     * @param battery            Battery percent of current hour
     * @param currentHour        Updates dailyPlan by finding the worst chosen charge hour after the battery was drained and
     *                           exchanges it for the cheapest non-chosen charge hour before battery drain. Runs checkPlan()
     *                           afterwards to confirm.
     */
    public void updatePlan(int indexOfcurrentTIme, int drainIndex, int battery, int currentHour) {

        ////////// PRINT ////////////
        //  printPlan();
        // Finds most expensive charge-hour after drain-time and removes the charge-status
        double mostExpensiveHour = Integer.MIN_VALUE;
        int index = 0;
        for (int i = drainIndex; i < dailyPlan.size(); i++) {

            if (dailyPlan.get(i).isCharge()) {

                if (dailyPlan.get(i).getPrice() > mostExpensiveHour) {
                    mostExpensiveHour = dailyPlan.get(i).getPrice();
                    index = i;
                }
            }
        }

        dailyPlan.get(index).setCharge(false);

        // Finds ands sets the cheapest non-charge hour before drain-time
        double cheapestHour = Integer.MAX_VALUE;

        for (int i = 0; i < dailyPlan.size(); i++) {
            Hour tempHour = dailyPlan.get(i);

            if (i >= indexOfcurrentTIme && i <= drainIndex && !tempHour.isCharge()) {
                if (dailyPlan.get(i).getPrice() < cheapestHour) {
                    cheapestHour = dailyPlan.get(i).getPrice();
                    index = i;
                }
            }
        }
        dailyPlan.get(index).setCharge(true);

        checkPlan(currentHour, battery);
    }

    public void printPlan() {
        for (int j = 0; j < dailyPlan.size(); j++) {

            Hour tempHour = dailyPlan.get(j);
            String chargeStr = "-";
            String ledStr = "-";
            if (tempHour.isCharge()) {
                chargeStr = "X";
            }
            if (tempHour.isLedOn()) {
                ledStr = "X";
            }
            System.out.println(tempHour.getLocalDateTime() + " | LED: " + ledStr + " | Batt: " + tempHour.getBatteryPercent() + " | Chrg: " + chargeStr + " | Price: " + tempHour.getPrice());
        }
        System.out.println("---------------------------------------------------------------------------------------------------------");

    }

    public int getBatteryPercent(LocalDateTime time) {
        for (int i = 0; i < dailyPlan.size(); i++) {
            LocalDateTime tempTime = dailyPlan.get(i).getLocalDateTime();
            //  System.out.println(tempTime.getHour() + " " +tempTime.getDayOfMonth() );
            if (tempTime.getHour() == time.getHour() && tempTime.getDayOfMonth() == time.getDayOfMonth()) {
                return dailyPlan.get(i).getBatteryPercent();
            }
        }
        return -1;
    }


    public boolean isDailyPlanExist() {
        return dailyPlanExist;
    }
}
