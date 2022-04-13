/*import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StoreData {


    public void writeCsv(TreeMap cheapestPrices) throws IOException {


        File file = new File("./Entsoe_Prices.csv");

        FileWriter outputfile = new FileWriter(file, true);
        CSVWriter writer = new CSVWriter(outputfile);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formatDateTime = LocalDateTime.now().format(format);

        writer.writeNext(new String[]{"Entsoe prices for date " + formatDateTime});


        Set set = cheapestPrices.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();

            String[] data = new String[2];

            data[0] = me.getKey().toString();
            data[1] = me.getValue().toString();

            writer.writeNext(data);


        }
        writer.close();
    }
}
*/