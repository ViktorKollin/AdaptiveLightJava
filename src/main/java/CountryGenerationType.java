import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CountryGenerationType {

    private TreeMap<LocalDateTime, Double> generationTypeMap = new TreeMap<>();
    private ArrayList<TreeMap> list = new ArrayList<>();
    private String zoneCode;
    private int coalCo2Factor = 820;
    private int fossilOilCo2Factor = 650;
    private int gasCo2Factor = 490;
    private int biomassCo2Factor = 230;
    private int GeothermalCo2Factor = 38;
    private int hydropowerCo2Factor = 24;
    private int nuclearCo2Factor = 12;
    private int solarCo2Factor = 45;
    private int windCo2Facotor = 12;


    private String[] psrType = {"B01", "B02", "B03", "B04", "B05", "B06", "B09", "B10", "B11", "B12", "B14", "B16", "B18", "B19"};
    private String[] psrTypeString = {"Biomass", "Fossil Brown coal/Lignite", "Fossil Coal-derived gas", "Fossil Gas", "\t\n" +
            "Fossil Hard coal", "Fossil Oil", "Geothermal", "Hydro Pumped Storage", "Hydro Run-of-river and poundage",
            "Hydro Water Reservoir", "Nuclear", "Solar", "Wind Offshore", "Wind Onshore"};

    private EntsoeActualGenerationType generationType;


    CountryGenerationType(String zoneCode, EntsoeActualGenerationType generationType) {
        this.zoneCode = zoneCode;
        this.generationType = generationType;
    }

    public String getZoneCode() {
        return zoneCode;
    }


    public void makeEntsoeRequest() throws IOException {

        for (int i = 0; i < psrType.length; i++) {

            generationTypeMap.clear();

            generationTypeMap = generationType.getGenerationType(psrType[i], zoneCode);

            if (generationTypeMap.size() > 0) {
                list.add((TreeMap) generationTypeMap.clone());
            } else if (generationTypeMap.size() == 0) {
                fillMap(i);
                list.add((TreeMap) generationTypeMap.clone());
            }

        }

        storeAsCsv();
    }

    public void fillMap(int j) {

        TreeMap<LocalDateTime, Double> temp = list.get(j - 1);

        Set set = temp.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {

            Map.Entry me = (Map.Entry) i.next();

            generationTypeMap.put((LocalDateTime) me.getKey(), 0.0);

        }

    }

    private void storeAsCsv() throws IOException {

        File file = new File("./GenerationType.csv");

        FileWriter outputfile = new FileWriter(file, true);
        CSVWriter writer = new CSVWriter(outputfile);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formatDateTime = LocalDateTime.now().format(format);

        writer.writeNext(new String[]{"Generation per Production Type" + formatDateTime});


        for (int j = 0; j < list.size(); j++) {

            writer.writeNext(new String[]{psrTypeString[j]});
            TreeMap tempList = list.get(j);

            Set set = tempList.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {

                Map.Entry me = (Map.Entry) i.next();

                String[] data = new String[2];

                data[0] = me.getKey().toString();
                data[1] = me.getValue().toString();


                writer.writeNext(data);

            }


        }


        writer.close();

    }
}
