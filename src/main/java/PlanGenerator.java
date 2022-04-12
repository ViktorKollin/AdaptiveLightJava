import java.time.LocalDateTime;
import java.util.*;

public class PlanGenerator {

    private TreeMap<LocalDateTime, Double> pricesByTime;
    private TreeMap<LocalDateTime, Double> pricesByPrice = new TreeMap<>();
    private ArrayList<Hour> list = new ArrayList();

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

    public void sortMap(TreeMap prices) {
        this.pricesByTime = prices;

        pricesByPrice = (TreeMap<LocalDateTime, Double>) sortByValues(prices);

        Set set = prices.entrySet();
        Iterator i = set.iterator();
        int time = 15;
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            if (time > 5 && time < 21) {
                list.add(new Hour(Double.parseDouble(me.getValue().toString()), true, (LocalDateTime) me.getKey()));
            } else {
                list.add(new Hour(Double.parseDouble(me.getValue().toString()), false, (LocalDateTime) me.getKey()));
            }

            time++;
            if (time == 24) {
                time = 0;
            }

            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }

        System.out.println(" ");

        set = pricesByPrice.entrySet();
        i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }


    }

}
