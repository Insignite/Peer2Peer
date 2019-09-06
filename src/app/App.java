package app;

import scripts.*;

public class App {
    public static void main(String[] args) throws Exception {
        //String sourceDirectory = ;
        //String destinationDirectory = ;

        Server server = new Server();
        Client client = new Client();

        
        server.receive();
        System.out.println("==============================");
        client.connect();
    }
}