import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;

public class Co2IntensityCalculator {
    private int[][] productionMwArr;
    private int[] co2IntensityArr = {germanyGrCo2,norwayGrCo2,denmarkGrCo2,polandGco2,finlandGco2,lithuaniaGco2,swedenGco2};
    private static final int germanyGrCo2 = 311;
    private static final int norwayGrCo2 = 19;
    private static final int denmarkGrCo2 = 109;
    private static final int polandGco2 = 710;
    private static final int finlandGco2 = 68;
    private static final int lithuaniaGco2 = 45;
    private static final int swedenGco2 = 8;
    private static final String[] countryCodesArr = {};

    public void populateIntensityArr(LocalDateTime time){
        TreeMap<LocalDateTime,Double> map = //get first map from entsoe. With time?;

        productionMwArr = new int[map.size][countryCodesArr.length];

        for(int i = 0; i< countryCodesArr.length; i++){


            // loop through map and populate productionMwArr
            for(int hour = 0;hour< map.size;hour++){
                productionMwArr[i][hour] = map.getInOrder;
            }
            map = // get next country.
        }
    }
    public void getSwedenData(){

    }
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

}


