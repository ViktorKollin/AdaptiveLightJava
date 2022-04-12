import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello");
        EntsoeDayAhead dayAhead = new EntsoeDayAhead("10Y1001A1001A47J", ZoneId.of("Europe/Stockholm"), "6d3ed710-5fbf-4341-9535-e3fe29fc72fa");
        //CalculateChargeTime chargeTime = new CalculateChargeTime();
        TreeMap<LocalDateTime, Double> prices = dayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        PlanGenerator planGenerator = new PlanGenerator();

        planGenerator.sortMap(prices);

        //  Controller controller = new Controller(dayAhead);
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
