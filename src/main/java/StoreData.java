import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class StoreData {
    public void writeToCSVFile(Hour hour) throws IOException {
        try {
            FileWriter fstream = new FileWriter("Hourlog.csv", true);
            BufferedWriter out = new BufferedWriter(fstream);

            StringBuilder builder = new StringBuilder();
            builder.append(hour.getLocalDateTime());
            builder.append(",");
            builder.append(hour.getPrice());
            builder.append(",");
            builder.append(hour.getBatteryPercent());
            builder.append(",");
            builder.append(hour.getDliReached());
            builder.append(",");
            if (hour.isCharge()) {
                builder.append(1);
            } else {
                builder.append(0);
            }
            builder.append(",");
            if (hour.isLedOn()) {
                builder.append(1);
            } else {
                builder.append(0);
            }
            out.write(builder.toString());
            out.newLine();
            out.flush();

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}

