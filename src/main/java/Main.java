import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Co2IntensityCalculator co2 = new Co2IntensityCalculator();
        LocalDateTime time = LocalDateTime.now().minusDays(6);
        ArrayList<Hour> printList = co2.calculateCo2Intensity(time);
        for (Hour h:printList
             ) {
            System.out.println("Time: " + h.getLocalDateTime().truncatedTo(ChronoUnit.HOURS).plusHours(4) + "Co2: " + h.getCo2_gKWh());

        }

        /*
        EntsoeDayAhead dayAhead = new EntsoeDayAhead("10Y1001A1001A47J", ZoneId.of("Europe/Stockholm"), "6d3ed710-5fbf-4341-9535-e3fe29fc72fa");


        SimulationController sim = new SimulationController(dayAhead);
        sim.populateDays();
        sim.setPpfdAndLed();
        sim.runSimulation();
        sim.printDays();


        EntsoeTotalCommercialSchedules totalCommercialSchedules = new EntsoeTotalCommercialSchedules("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        EntsoeTotalGeneration totalGeneration = new EntsoeTotalGeneration("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");

        Co2IntensityCalculator calculator = new Co2IntensityCalculator();
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(2);
        System.out.println(localDateTime);
        //calculator.populateIntensityArr(localDateTime);
        //calculator.getSwedenNetGeneration(localDateTime);
*/
        /*
        EntsoeTotalCommercialSchedules commercialSchedules = new EntsoeTotalCommercialSchedules("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        Map map = commercialSchedules.getTotalGeneration("10YSE-1--------K","10YFI-1--------U");

        Set set = map.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.println(me.getValue());
        }




        //CalculateChargeTime chargeTime = new CalculateChargeTime();
       /* TreeMap<LocalDateTime, Double> prices = dayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Set set = prices.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.println(me.getValue());
        }

*/
        //View view = new View();
        //Controller controller = new Controller(dayAhead, view);

        //AdaptiveLightServer server = new AdaptiveLightServer(5013, controller);
        // EntsoeTotalGeneration totalGeneration = new EntsoeTotalGeneration("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        //TreeMap treeMap =  totalGeneration.getTotalGeneration("10YSE-1--------K");
        /*
        EntsoeTotalCommercialSchedules commercialSchedules = new EntsoeTotalCommercialSchedules("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        TreeMap treeMap = commercialSchedules.getTotalGeneration("10YSE-1--------K", "10YNO-0--------C");

        Set set = treeMap.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.println(me.getKey() + " " + me.getValue());
        }
        //controller.testPlanGeneration(24,16,false);

        //   StoreData storeData = new StoreData();

        //   EntsoeActualGenerationType generationType = new EntsoeActualGenerationType("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        //    CountryGenerationType poland = new CountryGenerationType("10YPL-AREA-----S", generationType);

        //  poland.makeEntsoeRequest();
        // AdaptiveLightServer adaptiveLightServer = new AdaptiveLightServer(5013, controller);
        //  controller.getNewEntsoeRequest();


        //dayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));


        // TreeMap<LocalDateTime, Double> prices = dayAhead.getPrices();
        //  storeData.writeCsv(chargeTime.calculateCheapestChargeTime(5,prices));

*/
    }
}
