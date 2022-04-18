import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TreeMap;

public class Controller {
    private AdaptiveLightServer server;
    private EntsoeDayAhead entsoeDayAhead;
    private PlanGenerator planGenerator;
    private double dliGoal;

    public Controller(EntsoeDayAhead entsoeDayAhead) {
        this.entsoeDayAhead = entsoeDayAhead;

    }

    public void setServer(AdaptiveLightServer server) {
        this.server = server;
    }

    public void newMessage(String message) {
        String[] strArr = message.split("_", 3);

        String kl = strArr[0];
        String dliReachedStr = strArr[1];
        String batteryLevel = strArr[2];

        boolean dli_reached = false;

        if (Double.parseDouble(dliReachedStr) >= dliGoal){
            dli_reached = true;
        }


        if (Integer.parseInt(kl) == 15) {
            planGenerator = new PlanGenerator(getNewEntsoeRequest());
            planGenerator.generatePlan(Integer.parseInt(batteryLevel), Integer.parseInt(kl),dli_reached);
        }

        server.setMessage(batteryLevel);

    }

    public TreeMap<LocalDateTime, Double> getNewEntsoeRequest() {
        return entsoeDayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }


    public void testPlanGeneration(int batteryPercentage,int currentHour,boolean dli) {

        planGenerator = new PlanGenerator(getNewEntsoeRequest());
        planGenerator.generatePlan(batteryPercentage, currentHour,dli);
    }


}
