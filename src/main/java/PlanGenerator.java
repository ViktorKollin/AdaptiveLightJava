import java.time.LocalDateTime;
import java.util.*;

public class PlanGenerator {

    private TreeMap<LocalDateTime, Double> prices;
    private TreeMap<LocalDateTime, Double> sortedMap = new TreeMap<>();

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
        this.prices = prices;

        sortedMap = (TreeMap<LocalDateTime, Double>) sortByValues(prices);

        Set set = prices.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }

        System.out.println(" ");

        set = sortedMap.entrySet();
        i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }


    }

}
