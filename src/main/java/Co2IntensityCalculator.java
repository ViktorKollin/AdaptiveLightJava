import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Co2IntensityCalculator {
    private static final String[] countryCodesArr = {"10YFI-1--------U", "10YNO-0--------C", "10YPL-AREA-----S", "10YLT-1001A0008Q", "10YSE-1--------K"};
    private static final String[] regionCodeArr = {"10YDK-2--------M", "10Y1001A1001A82H", "10Y1001A1001A47J"};

    private int[] co2IntensityArr = {finlandGco2, norwayGrCo2, polandGco2, lithuaniaGco2, denmarkGrCo2, germanyGrCo2, swedenGco2};

    private static final int germanyGrCo2 = 311;
    private static final int norwayGrCo2 = 19;
    private static final int denmarkGrCo2 = 109;
    private static final int polandGco2 = 710;
    private static final int finlandGco2 = 68;
    private static final int lithuaniaGco2 = 45;
    private static final int swedenGco2 = 8;
    private double[][] productionMwArr;
    private final String tokenEntsoe = "6d3ed710-5fbf-4341-9535-e3fe29fc72fa";
    private EntsoeTotalCommercialSchedules commercialSchedules;
    private EntsoeTotalGeneration totalGeneration;


    public Co2IntensityCalculator() {

        this.commercialSchedules = new  EntsoeTotalCommercialSchedules(tokenEntsoe);
        this.totalGeneration = new EntsoeTotalGeneration(tokenEntsoe);
    }


    public void populateIntensityArr(LocalDateTime time) {
        int index2 = 0;
        //get value from enstoe
        TreeMap<LocalDateTime, Double> map = commercialSchedules.getTotalGeneration(countryCodesArr[countryCodesArr.length - 1], countryCodesArr[0], time);

        // initiate 2-dim array with number of hours from entsoe and number of counties
        productionMwArr = new double[map.size()][co2IntensityArr.length];

        for (int i = 0; i < countryCodesArr.length - 1; i++) {
            int index = 0;
            for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
                productionMwArr[index][i] = entry.getValue();
                index++;

            }
            //keeps track on index in array for nex loop
            index2 = i;

            map = commercialSchedules.getTotalGeneration(countryCodesArr[countryCodesArr.length - 1], countryCodesArr[i + 1], time);
        }


        for (int i = 0; i < regionCodeArr.length - 1; i++) {
            map = commercialSchedules.getTotalGeneration(regionCodeArr[regionCodeArr.length - 1], regionCodeArr[i], time);
            int index = 0;
            for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
                productionMwArr[index][i + (index2 + 1)] = entry.getValue();
                index++;
            }


        }
        addSwedenNetGeneration(time);
/*
        for (int i = 0; i < co2IntensityArr.length; i++){
            System.out.println("Ny lista");
            for(int j = 0; j<map.size();j++){
                System.out.println(productionMwArr[j][i]);

            }
        }

 */





    }


    public void addSwedenNetGeneration(LocalDateTime time) {
        TreeMap<LocalDateTime, Double> map = totalGeneration.getTotalGeneration("10YSE-1--------K", time);
        double[] sweGeneration = new double[map.size()];

        int index = 0;
        for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
            sweGeneration[index] = entry.getValue();
            index++;
        }

        double[] sweTotalExport = new double[map.size()];

        for (int i = 0; i < countryCodesArr.length - 1; i++) {
            index = 0;
            /*
            System.out.println("Number of interation " + i);
            System.out.println("-------------------------");
            */

            map = commercialSchedules.getTotalGeneration(countryCodesArr[i], countryCodesArr[countryCodesArr.length - 1], time);
            for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
                sweTotalExport[index] += entry.getValue();
                index++;
              //  System.out.println(entry.getValue());
            }

        }
        for (int i = 0; i < regionCodeArr.length - 1; i++) {
            map = commercialSchedules.getTotalGeneration(regionCodeArr[i], regionCodeArr[regionCodeArr.length - 1], time);
            index = 0;
            for (Map.Entry<LocalDateTime, Double> entry : map.entrySet()) {
                sweTotalExport[index] += entry.getValue();
                index++;
            }


        }
/*
        for (double d : sweTotalExport
        ) {
            System.out.println(d);

        }

 */

        for (int i = 0; i < productionMwArr.length; i++) {
            productionMwArr[i][co2IntensityArr.length - 1] = (sweGeneration[i] - sweTotalExport[i]);
        }


    }



    public ArrayList<Hour> calculateCo2Intensity(LocalDateTime time){

        populateIntensityArr(time);

        LocalDateTime currTime = time;
        ArrayList<Hour> returnList = new ArrayList<>();
    /*
        for (int i = 0; i < productionMwArr.length; i++) {
            System.out.println("Ny timme: ");
            for (int j = 0; j < productionMwArr[0].length; j++) {
                System.out.println(productionMwArr[i][j]);

            }
        }

     */


        for (int i = 0; i < productionMwArr.length; i++) {

            double totCo2 = 0;
            double totProductionMw = 0;
            System.out.println("--------------------------");

            for (int j = 0; j < co2IntensityArr.length; j++) {
                double productionFromCountry = productionMwArr[i][j];
                totCo2 += (productionFromCountry * 1000) * co2IntensityArr[j];
                System.out.println("prod from c: " + productionFromCountry);
                totProductionMw += productionFromCountry;
            }
            System.out.println("----");
            System.out.println(totCo2);
            System.out.println(totProductionMw);

            double co2intensityG_kwh = (totCo2 / totProductionMw) / 1000;
            Hour hourToAdd = new Hour(currTime, co2intensityG_kwh);
            returnList.add(hourToAdd);
            currTime = currTime.plusHours(1);
        }
        return returnList;
    }


}


