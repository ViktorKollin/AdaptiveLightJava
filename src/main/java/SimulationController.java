import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SimulationController {
    private PlanGenerator planGenerator;
    private EntsoeDayAhead entsoeDayAhead;
    private ArrayList<List<Hour>> days = new ArrayList<>();
    private double dliGoal = 12;
    private final double ppdfToDli = 0.0036;
    private final int ppfdFromLed = 100;
    // ppfd from the sun for days feb 14 - feb 28 between 07-17 o'clock. 11 values per day.
    private int[] ppfdArr = {2,79,45,135,148,192,162,110,68,33,3,11,53,52,62,81,82,50,58,52,28,3,7,22,72,
            107,175,272,220,156,78,46,3,16,86,282,380,506,509,225,94,31,11,4,9,103,397,491,573,376,
            537,308,233,98,8,55,192,490,203,570,576,342,182,82,30,4,17,66,64,85,121,339,408,204,156,
            81,5,41,114,192,414,550,536,475,349,239,107,7,37,158,320,486,463,287,220,303,226,109,16,
            12,80,506,585,573,530,486,305,154,66,8,35,255,446,574,483,515,582,380,498,195,19,90,122,
            186,313,479,200,204,228,228,118,24,96,190,316,358,472,317,286,347,256,133,25,52,120,178,
            346,546,186,196,225,223,133,34};
/*
    LocalDateTime daySimulation = LocalDateTime.of(2021,12,1,00,00);
    ZonedDateTime timeLocalSimulation = daySimulation.atZone(TimeUtils.UTC).withZoneSameInstant(timezone);
    LocalDateTime dayStartSimulation = timeLocalSimulation.truncatedTo(ChronoUnit.DAYS).toLocalDateTime();
    LocalDateTime dayEndSimulation = timeLocalSimulation.truncatedTo(ChronoUnit.DAYS).plusDays(1).toLocalDateTime();
*/
    public SimulationController(EntsoeDayAhead entsoeDayAhead) {
        this.entsoeDayAhead = entsoeDayAhead;
    }

    public ArrayList<List<Hour>> getDays() {
        return days;
    }

    /**
     * Populates the days-list with hour-instances and sets the ppfdSun and hourOfDay variables.
     */
    public void populateDays(){

        int indexPpfdArr = 0;

        for(int i =0;i<14;i++){
            ArrayList<Hour> day = new ArrayList<>();

            for(int HourOfDay = 0; HourOfDay<24;HourOfDay++){
                int ppfd = 0;
                if(HourOfDay>6 && HourOfDay<18) {
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
    public void setPpfdAndLed(){

        for(int i =0;i<14;i++){

            for(int HourOfDay = 0; HourOfDay<23;HourOfDay++){

                Hour currHour = days.get(i).get(HourOfDay);
                Hour nextHour = days.get(i).get(HourOfDay+1);

                nextHour.setDliReached(currHour.getDliReached()+(currHour.getPpfdSun() * ppdfToDli));

                if(HourOfDay>04 && HourOfDay <22 && currHour.getDliReached() < dliGoal){

                    nextHour.setDliReached(nextHour.getDliReached() + (ppfdFromLed * ppdfToDli));
                    currHour.setLedOn(true);
                }


            }

        }

    }

    public void runSimulation(){
        /*
        PlanGenerator: ta en DateTime parameter + decoupla entsoe från controller
        SimulationController:
            gå genom days-listan och sätt batterinivå beroende på ledOn och Charge On.
            request plan för varje timme. -> sätt chargeOn i "days" beroende på boolean[] retunt value.

         */

        for(int i = 0 ; i<14;i++){

        }

    }
    public void printDays(){
        for(int i = 0; i<14;i++){

            String dayStr = "Day: "+String.valueOf(i+15);
            System.out.println("---------------------------------------------------------------------------------------------");
            System.out.printf("%7s |%5s |%7s | %5s  | %8s |","Hour", "PPFD Sun", "Led On", "DLI reached",dayStr);
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------");

            for(int hour = 0;hour<24;hour++){
                Hour tempHour = days.get(i).get(hour);
                String hourStr = String.format("%02d",tempHour.getHourOfDay());
                String ppfdSunStr = String.valueOf(tempHour.getPpfdSun());
                String ledOnStr = String.valueOf(tempHour.isLedOn());
                String dliReachedStr = String.format("%02.2f",tempHour.getDliReached());

                System.out.format("%5s %7s %10s %7s",hourStr,ppfdSunStr,ledOnStr,dliReachedStr);
                System.out.println();
            }
        }
    }
}
