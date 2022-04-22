import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimulationController {
   // todo: private PlanGenerator planGenerator;
   private PlanGeneratorTest planGeneratorTest;
    private EntsoeDayAhead entsoeDayAhead;
    private ArrayList<List<Hour>> days = new ArrayList<>();
    private double dliGoal = 12;
    private final double ppdfToDli = 0.0036;
    private final int ppfdFromLed = 100;
    private final double chargePowerW = (1.5*5);
    private final double ledPowerW = (0.265*4);
    // ppfd from the sun for days feb 14 - feb 28 between 07-17 o'clock. 11 values per day.
    private int[] ppfdArr = {2, 79, 45, 135, 148, 192, 162, 110, 68, 33, 3, 11, 53, 52, 62, 81, 82, 50, 58, 52, 28, 3, 7, 22, 72,
            107, 175, 272, 220, 156, 78, 46, 3, 16, 86, 282, 380, 506, 509, 225, 94, 31, 11, 4, 9, 103, 397, 491, 573, 376,
            537, 308, 233, 98, 8, 55, 192, 490, 203, 570, 576, 342, 182, 82, 30, 4, 17, 66, 64, 85, 121, 339, 408, 204, 156,
            81, 5, 41, 114, 192, 414, 550, 536, 475, 349, 239, 107, 7, 37, 158, 320, 486, 463, 287, 220, 303, 226, 109, 16,
            12, 80, 506, 585, 573, 530, 486, 305, 154, 66, 8, 35, 255, 446, 574, 483, 515, 582, 380, 498, 195, 19, 90, 122,
            186, 313, 479, 200, 204, 228, 228, 118, 24, 96, 190, 316, 358, 472, 317, 286, 347, 256, 133, 25, 52, 120, 178,
            346, 546, 186, 196, 225, 223, 133, 34};

    public SimulationController(EntsoeDayAhead entsoeDayAhead) {
        this.entsoeDayAhead = entsoeDayAhead;
        // todo: change to normal plangenerator.
        planGeneratorTest = new PlanGeneratorTest(entsoeDayAhead);

    }

    public ArrayList<List<Hour>> getDays() {
        return days;
    }

    /**
     * Populates the days-list with hour-instances and sets the ppfdSun and hourOfDay variables.
     */
    public void populateDays() {

        int indexPpfdArr = 0;

        for (int i = 0; i < 14; i++) {
            ArrayList<Hour> day = new ArrayList<>();

            for (int HourOfDay = 0; HourOfDay < 24; HourOfDay++) {
                int ppfd = 0;
                if (HourOfDay > 6 && HourOfDay < 18) {
                    ppfd = ppfdArr[indexPpfdArr];
                    indexPpfdArr++;
                }
                Hour hour = new Hour(ppfd, HourOfDay);
                day.add(hour);

            }
            days.add(day);
        }

    }

    /**
     * Loops through the Hour-instances in the Days-list and sets the dliReached variable depending on
     * the ppfd from sun and ppfd from led between 05-21. If the dli goal is reached the led is set to off.
     */
    public void setPpfdAndLed() {

        for (int i = 0; i < 14; i++) {

            for (int HourOfDay = 0; HourOfDay < 23; HourOfDay++) {

                Hour currHour = days.get(i).get(HourOfDay);
                Hour nextHour = days.get(i).get(HourOfDay + 1);

                nextHour.setDliReached(currHour.getDliReached() + (currHour.getPpfdSun() * ppdfToDli));

                if (HourOfDay > 04 && HourOfDay < 21 && currHour.getDliReached() < dliGoal) {

                    nextHour.setDliReached(nextHour.getDliReached() + (ppfdFromLed * ppdfToDli));
                    currHour.setLedOn(true);
                }


            }

        }

    }

    public void runSimulation() {
        boolean start = true;
        boolean start2 = false;
        LocalDateTime daySimulation = LocalDateTime.of(2022, 02, 15, 00, 00);


        /*
        Simulates a day from 14-28 of february

         */
        for (int i = 0; i < 14; i++) {


            for (int hour = 0; hour < 24; hour++) {
                Hour tempHour = days.get(i).get(hour);

                if (i == 0 && hour == 15) {
                    start2 = true;
                }

                if (start2) {
                    if (start) {
                       // todo: tempHour = planGenerator.generatePlan(60, tempHour.getHourOfDay(), (tempHour.getDliReached() > dliGoal), daySimulation);
                        tempHour = planGeneratorTest.generatePlan(60, tempHour.getHourOfDay(), (tempHour.getDliReached() > dliGoal), daySimulation,0.5);
                        start = false;
                        // all other days
                    } else {
                       // todo: tempHour = planGenerator.generatePlan(planGenerator.getBatteryPercent(daySimulation), tempHour.getHourOfDay(), (tempHour.getDliReached() > dliGoal), daySimulation);
                        tempHour = planGeneratorTest.generatePlan(planGeneratorTest.getBatteryPercent(daySimulation), tempHour.getHourOfDay(), (tempHour.getDliReached() > dliGoal), daySimulation,0.5);
                    }
                    days.get(i).get(hour).setPrice(tempHour.getPrice());
                    days.get(i).get(hour).setCharge(tempHour.isCharge());
                    days.get(i).get(hour).setBatteryPercent(tempHour.getBatteryPercent());
                    days.get(i).get(hour).setCo2_gKWh(tempHour.getCo2_gKWh());
                    days.get(i).get(hour).setWeightedCost(tempHour.getWeightedCost());
                    days.get(i).get(hour).setEnvironmentWeight(tempHour.getEnvironmentWeight());
                }
                daySimulation = daySimulation.plusHours(1);
            }


        }

    }



    public void printDays() {
        // total price and emissions per period
        double totPriceOEre = 0;
        double noBatteryPrice = 0;
        double noBattLux = 0;
        double totCo2 = 0;
        double totNoBatteryCo2 = 0;


        for (int i = 0; i < 14; i++) {

            // daily price and emissions
            double dailyGCo2 = 0;
            double dailyPrice = 0;
            double dailyNoBatPrice = 0;
            double dailySmartPrice = 0;
            double dailyNoBatCo2 = 0;


            String dayStr = "DAY: " + String.valueOf(i + 15);
            int CO2weight = (int) (days.get(i).get(0).getEnvironmentWeight()*100);
            String weightRatioStr = " | Co2/Price Weight Ratio: "+CO2weight+"/"+(100-CO2weight);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.println(dayStr+weightRatioStr);
            System.out.println("--------------------------------");
            System.out.printf("%5s | %4s |%5s | %3s | %4s | %7s |%5s | %5s | %4s |", "Hour", "PPFD", "Led on", "DLI","Bat %", "Charge","Price","gCO2/kWh","Cost");
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------");

            for (int hour = 0; hour < 24; hour++) {

                Hour tempHour = days.get(i).get(hour);

                if(tempHour.isCharge()){
                    totPriceOEre += (tempHour.getPrice()*chargePowerW*1031)/1000000; // 1031 öre/Euro, 1/1000000 W/MWh
                    dailyPrice += (tempHour.getPrice()*chargePowerW*1031)/1000000;
                    dailyGCo2 += (tempHour.getCo2_gKWh()*chargePowerW)/1000;
                    totCo2 += (tempHour.getCo2_gKWh()*chargePowerW)/1000;

                }
                if(hour>04 && hour<21){
                    noBatteryPrice += tempHour.getPrice()*ledPowerW*1031/1000000;
                    dailyNoBatPrice += tempHour.getPrice()*ledPowerW*1031/1000000;
                    dailyNoBatCo2 += (tempHour.getCo2_gKWh()*ledPowerW)/1000;
                    totNoBatteryCo2 += (tempHour.getCo2_gKWh()*ledPowerW)/1000;


                }if(tempHour.isLedOn()){
                    noBattLux += tempHour.getPrice()*ledPowerW*1031/1000000;
                    dailySmartPrice += tempHour.getPrice()*ledPowerW*1031/1000000;

                }
                
                String hourStr = String.format("%02d", tempHour.getHourOfDay());
                String ppfdSunStr = String.valueOf(tempHour.getPpfdSun());
                String ledOnStr = String.valueOf(tempHour.isLedOn());
                String dliReachedStr = String.format("%02.2f", tempHour.getDliReached());
                String priceStr = String.valueOf(tempHour.getPrice());
                String chargeStr = String.valueOf(tempHour.isCharge());
                String batteryStr = String.valueOf(tempHour.getBatteryPercent());
                String gCO2Str = String.format("%02.2f",tempHour.getCo2_gKWh());
                String costStr = String.format("%02.2f",tempHour.getWeightedCost());

                System.out.format("%4s %5s %7s %7s %6s %9s %7s %8s %7s", hourStr, ppfdSunStr, ledOnStr, dliReachedStr,batteryStr,chargeStr,priceStr,gCO2Str,costStr);
                System.out.println();
            }
            System.out.println("gCO2: Prototype: "+dailyGCo2+" No Battery: "+dailyNoBatCo2);
            System.out.println("PRICE (öre): Prototype: "+dailyPrice+" No Battery: "+dailyNoBatPrice);
        }
        totPriceOEre /= 1000;
        totPriceOEre /= 0.97;
        noBatteryPrice /= 1000;
        noBattLux /= 1000;

        System.out.println("battery price: " +String.format("%02.2f",totPriceOEre));
        System.out.println("No battery price: "+String.format("%02.2f",noBatteryPrice));
        System.out.println("No battery, smart: "+String.format("%02.2f",noBattLux));
        System.out.println("g C02 prototype: "+String.format("%02.2f",totCo2));
        System.out.println("g CO2 No Battery: "+String.format("%02.2f",totNoBatteryCo2));
    }
}
