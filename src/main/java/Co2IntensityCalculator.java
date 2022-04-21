import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class Co2IntensityCalculator {
    private static final String[] countryCodesArr = {"10Y1001A1001A83F", "10YNO-0--------C", "10Y1001A1001A65H", "10YPL-AREA-----S", "10YFI-1--------U", "10YLT-1001A0008Q", "10YSE-1--------K"};
    private int[] co2IntensityArr = {germanyGrCo2, norwayGrCo2, denmarkGrCo2, polandGco2, finlandGco2, lithuaniaGco2, swedenGco2};
    private static final int germanyGrCo2 = 311;
    private static final int norwayGrCo2 = 19;
    private static final int denmarkGrCo2 = 109;
    private static final int polandGco2 = 710;
    private static final int finlandGco2 = 68;
    private static final int lithuaniaGco2 = 45;
    private static final int swedenGco2 = 8;
    private double[][] productionMwArr;
    private EntsoeTotalCommercialSchedules commercialSchedules;
    private EntsoeTotalGeneration totalGeneration;


    public Co2IntensityCalculator(EntsoeTotalCommercialSchedules commercialSchedules, EntsoeTotalGeneration totalGeneration) {
        this.commercialSchedules = commercialSchedules;
        this.totalGeneration = totalGeneration;
    }


    public void populateIntensityArr(LocalDateTime time) {
        TreeMap<LocalDateTime, Double> map = commercialSchedules.getTotalGeneration(countryCodesArr[countryCodesArr.length - 1], countryCodesArr[0]);

        productionMwArr = new double[map.size()][countryCodesArr.length - 1];

        for (int i = 0; i < countryCodesArr.length - 1; i++) {
            System.out.println(i);
            System.out.println(countryCodesArr[i]);
            int index = 0;
            for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
                productionMwArr[index][i] = entry.getValue();
                index++;
                System.out.println(entry.getValue());
            }

            map = commercialSchedules.getTotalGeneration(countryCodesArr[countryCodesArr.length - 1], countryCodesArr[i + 1]);
        }
    }

    /*
    public int[] getSwedenNetGeneration(){
        TreeMap<LocalDateTime,Double> map = //get first map from entsoe. sweden generation
        int [] sweGeneration;

        for(int hour = 0;hour< map.size;hour++){
            sweGeneration[hour] = map.getInOrder;
        }

        map = // get first country map

        int [] sweTotalExport = new int[map.size()];
        for(int i = 0; i< countryCodesArr.length - 1; i++){

            // loop through map and populate productionMwArr
            for(int hour = 0;hour< map.size;hour++){
                sweTotalExport[hour] += map.getInOrder;
            }
            map = // get next country.
        }

        int[] sweNetGeneration = new int[map.size()];
        for(int i = 0 ; i<sweNetGeneration.length;i++){
            sweNetGeneration[i] = sweGeneration[i]-sweTotalExport[i];
        }
        return sweNetGeneration;


    }

     */
    /*
    public ArrayList<Hour> calculateCo2Intensity(LocalDateTime time){

        populateIntensityArr(time);

        LocalDateTime currTime = time;
        ArrayList<Hour> returnList = new ArrayList<>();

        for(int i = 0;i<productionMwArr.length;i++){

            int totCo2 = 0;
            int totProductionMw = 0;

            for (int j = 0; j<countryCodesArr.length;j++){
                int productionFromCountry = productionMwArr[j][i];
                totCo2 += productionFromCountry * co2IntensityArr[j];
                totProductionMw += productionFromCountry;
            }
            // currTime++
            double co2intensityG_kwh = (totCo2/totProductionMw)*1000;
            Hour hourToAdd = new Hour(currTime,co2intensityG_kwh);
            returnList.add(hourToAdd);
        }
        return returnList;
    }

     */

}


