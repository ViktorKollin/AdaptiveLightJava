import java.time.LocalDateTime;
import java.util.*;

public class PlanGenerator {

    private TreeMap<LocalDateTime, Double> prices;
    //private TreeMap<LocalDateTime, Double> pricesByPrice = new TreeMap<>();
    private ArrayList<Hour> dailyPlan = new ArrayList();
    private ArrayList<Hour> pricesByPrice = new ArrayList();

    public PlanGenerator(TreeMap<LocalDateTime, Double> pricesByTime) {
        this.prices = pricesByTime;
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

    private ArrayList putInArraylist(TreeMap sortedByValues) {

        Set set = sortedByValues.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            pricesByPrice.add(new Hour(Double.parseDouble(me.getValue().toString()), (LocalDateTime) me.getKey()));
        }
        return pricesByPrice;
    }

    public void generatePlan(int battery) {

        int chargingHours = ((100 - battery) * 8) / 100;
        putInArraylist(sortMap(prices));


        Set set = prices.entrySet();
        Iterator i = set.iterator();
        int time = 15;


        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            if (time == 15) {
                dailyPlan.add(new Hour(Double.parseDouble(me.getValue().toString()), battery, true, (LocalDateTime) me.getKey()));
            } else if (time >= 5 && time < 21) {
                dailyPlan.add(new Hour(Double.parseDouble(me.getValue().toString()), true, (LocalDateTime) me.getKey()));
            } else {
                dailyPlan.add(new Hour(Double.parseDouble(me.getValue().toString()), false, (LocalDateTime) me.getKey()));
            }

            time++;
            if (time == 24) {
                time = 0;
            }
        }

        for (int j = 0; j < chargingHours; j++) {

            for (int k = 0; k < dailyPlan.size(); k++) {
                if (pricesByPrice.get(j).getLocalDateTime().equals(dailyPlan.get(k).getLocalDateTime())) {
                    dailyPlan.get(k).setCharge(true);
                }
            }

        }


        for (int j = 0; j < dailyPlan.size(); j++) {
            System.out.println(dailyPlan.get(j).getLocalDateTime() + " Charge status " + dailyPlan.get(j).isCharge() + " Price " + dailyPlan.get(j).getPrice());
        }
        System.out.println(dailyPlan.get(2).getLocalDateTime().getHour());


    }

    private boolean checkPlan(int currentHour, int battery) {
        int batteryNow = battery;
        //BLir fel om kl inte är 15
        //Tror jag löst det
        int index = 0;
        for (int i = 0; i < 24; i++) {
            if (currentHour == dailyPlan.get(i).getLocalDateTime().getHour()) {
                index = i;
            }
        }

        for (int i = index; i < dailyPlan.size(); i++) {
            if (i == index) {
                dailyPlan.get(i).setBatteryPercent(battery);
            } else {
                if (dailyPlan.get(i).isCharge()) {
                    batteryNow += 13;
                    dailyPlan.get(i).setBatteryPercent(batteryNow);
                } else if (dailyPlan.get(i).isLedOn()) {
                    batteryNow = -4;
                    dailyPlan.get(i).setBatteryPercent(batteryNow);
                }

            }
            if (batteryNow <= 20) {
                updatePlan(currentHour, i, battery);
                break;
            }

        }
        return true;
    }

    public void updatePlan(int currentHour, int drainIndex, int battery) {
        // Kan bli LOOp här TÄNK PÅ DET IMORGON
        // KAN lösas om man kollar om kl är mer än 5 men mindre än 21
        // ELler om man undersöker att den borttagna tiden inte är i intervallet

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

        double chepestHour = Integer.MAX_VALUE;

        for (int i = 0; i < dailyPlan.size(); i++) {
            Hour tempHour = dailyPlan.get(i);

            int hour = tempHour.getLocalDateTime().getHour();

            if (hour >= currentHour && i < drainIndex && !tempHour.isCharge()) {
                if (dailyPlan.get(i).getPrice() < chepestHour) {
                    chepestHour = dailyPlan.get(i).getPrice();
                    index = i;
                }

            }

        }
        dailyPlan.get(index).setCharge(true);

        checkPlan(currentHour, battery);


    }

}
