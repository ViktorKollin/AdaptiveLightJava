import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Controller {
    private AdaptiveLightServer server;
    private EntsoeDayAhead entsoeDayAhead;

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
            getNewEntsoeRequest();
        }

        server.setMessage(batteryLevel);

    }

    public void getNewEntsoeRequest() {
        entsoeDayAhead.getCostForDayAhead(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }

    public void generatePlant() {


    }


}
