import java.io.IOException;
import java.time.ZoneId;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        EntsoeDayAhead dayAhead = new EntsoeDayAhead("10Y1001A1001A47J", ZoneId.of("Europe/Stockholm"), "6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        SimulationController sim = new SimulationController(dayAhead);
        sim.populateDays();
        sim.setPpfdAndLed();
        sim.printDays();
        sim.runSimulation();

        //CalculateChargeTime chargeTime = new CalculateChargeTime();
       /* TreeMap<LocalDateTime, Double> prices = dayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Set set = prices.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.println(me.getValue());
        }


        Controller controller = new Controller(dayAhead);
        controller.testPlanGeneration(24,16,false);
*/
        //   StoreData storeData = new StoreData();

        //   EntsoeActualGenerationType generationType = new EntsoeActualGenerationType("6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        //    CountryGenerationType poland = new CountryGenerationType("10YPL-AREA-----S", generationType);

        //  poland.makeEntsoeRequest();
        // AdaptiveLightServer adaptiveLightServer = new AdaptiveLightServer(5013, controller);
        //  controller.getNewEntsoeRequest();


        //dayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));


        // TreeMap<LocalDateTime, Double> prices = dayAhead.getPrices();
        //  storeData.writeCsv(chargeTime.calculateCheapestChargeTime(5,prices));


    }
}
