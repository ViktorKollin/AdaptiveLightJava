import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AdaptiveLightServer extends Thread {
    private final ServerSocket serverSocket;
    private String message;
    private String returnMessage;
    private Controller controller;

    public AdaptiveLightServer(int port, Controller controller) throws IOException {
        serverSocket = new ServerSocket(port);
        this.controller = controller;
        controller.setServer(this);
        this.start();
        System.out.println("Server started on port " + port);
    }

    public void setMessage(String msg) {
        returnMessage = msg;
    }


    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connection detected, initiating handler...");
                new ClientHandler(socket);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public class ClientHandler extends Thread {
        private Socket socket;
        private InputStreamReader isr;
        private OutputStreamWriter osw;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("Handler and socket established.");

            isr = new InputStreamReader(socket.getInputStream());
            osw = new OutputStreamWriter(socket.getOutputStream());
            start();
        }

        public void run() {
            BufferedReader br = new BufferedReader(isr);
            BufferedWriter bw = new BufferedWriter(osw);


            try {
                while (!interrupted()) {

                    sleep(10);
                    if (message != null) {
                        bw.write(returnMessage);
                        bw.flush();
                    }


                    String tempmessage = br.readLine();
                    if (tempmessage != null) {
                        message = tempmessage;
                        controller.newMessage(message);

                    }


                    sleep(100);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println();
            }
            try {
                socket.close();
                System.out.println("Socket closed.");
            } catch (Exception e) {
                System.err.println();
            }
        }
    }
}

