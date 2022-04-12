public class Controller {
    private AdaptiveLightServer server;

    public Controller() {

    }

    public void setServer(AdaptiveLightServer server) {
        this.server = server;
    }

    public void newMessage(String message) {
        String[] strArr = message.split("_", 3);

        String kl = strArr[0];
        String dliGoal = strArr[1];
        String batteryLevel = strArr[2];


        server.setMessage(batteryLevel);

    }

    public void generatePlant() {

    }


}
