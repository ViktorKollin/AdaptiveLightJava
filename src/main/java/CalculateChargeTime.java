import java.time.LocalDateTime;
import java.util.*;

public class CalculateChargeTime {
    private TreeMap<LocalDateTime, Double> prices;
    private TreeMap<LocalDateTime, Double> cheapestPrices = new TreeMap<>();

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


    public TreeMap<LocalDateTime, Double> calculateCheapestChargeTime(int numberOfHours, TreeMap<LocalDateTime, Double> prices) {
        this.prices = prices;


        cheapestPrices = (TreeMap) sortByValues(prices);

        for (int j = 0; j <= 32 - numberOfHours; j++) {
            cheapestPrices.pollLastEntry();
        }


        Set set = cheapestPrices.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }

        return cheapestPrices;
    }


}
