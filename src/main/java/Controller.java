import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeMap;

public class Controller {
    private AdaptiveLightServer server;
    private EntsoeDayAhead entsoeDayAhead;
    private PlanGenerator planGenerator;
    private final double dliGoal = 10;
    private View view;
    private ArrayList<Hour> days = new ArrayList<>();

    public Controller(EntsoeDayAhead entsoeDayAhead, View view) {
        this.view = view;
        this.entsoeDayAhead = entsoeDayAhead;
        planGenerator = new PlanGenerator(entsoeDayAhead);
        LocalDateTime localDateTime = LocalDateTime.now();

        if (localDateTime.getHour() < 15) {
            localDateTime = localDateTime.minusDays(1);
        }

        entsoeDayAhead.getCostForDayAhead(localDateTime);
        planGenerator.setPrices(entsoeDayAhead.getPrices());

    }

    public void setServer(AdaptiveLightServer server) {
        this.server = server;
    }

    public void newMessage(String message) throws IOException {

        LocalDateTime time = LocalDateTime.now();

        String[] strArr = message.split("_", 3);

        String dliReached = strArr[0];
        String batterLevel = strArr[1];
        String kl = strArr[2];

        double tempDLIGoal = Double.parseDouble(dliReached);
        boolean dliGoalReached = false;

        if (tempDLIGoal >= dliGoal) {
            dliGoalReached = true;
        }

        Hour tempHour = planGenerator.generatePlan(Integer.parseInt(batterLevel), Integer.parseInt(kl), dliGoalReached, time);
        tempHour.setDliReached(tempDLIGoal);
        new StoreData().writeToCSVFile(tempHour);


        String tempCharge = "0";

        if (tempHour.isCharge()) {
            tempCharge = "1";
        }

        tempCharge += "_" + view.getSlider();
        System.out.println(tempCharge);

        server.setMessage(tempCharge);

    }

    public TreeMap<LocalDateTime, Double> getNewEntsoeRequest() {
        return entsoeDayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }

/*
    public void testPlanGeneration(int i, int currentHour) {

        planGenerator = new PlanGenerator(getNewEntsoeRequest());
        // planGenerator.generatePlan(i, currentHour);
    }
*/

}
