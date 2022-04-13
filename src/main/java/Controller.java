import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class Controller {
    private AdaptiveLightServer server;
    private EntsoeDayAhead entsoeDayAhead;
    private PlanGenerator planGenerator;

    public Controller(EntsoeDayAhead entsoeDayAhead) {
        this.entsoeDayAhead = entsoeDayAhead;

    }

    public void setServer(AdaptiveLightServer server) {
        this.server = server;
    }

    public void newMessage(String message) {
        String[] strArr = message.split("_", 3);

        String kl = strArr[0];
        String dliGoal = strArr[1];
        String batteryLevel = strArr[2];


        if (Integer.parseInt(kl) == 15) {
            planGenerator = new PlanGenerator(getNewEntsoeRequest());
            planGenerator.generatePlan(Integer.parseInt(batteryLevel));
        }

        server.setMessage(batteryLevel);

    }

    public TreeMap<LocalDateTime, Double> getNewEntsoeRequest() {
        return entsoeDayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }

    public void generatePlant() {


    }

    public void testPlanGeneration(int i) {

        planGenerator = new PlanGenerator(getNewEntsoeRequest());
        planGenerator.generatePlan(i);
    }


}
