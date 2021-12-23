package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server2 {
    public static int port = 5000;
    public static int numThread = 2;
    private static ServerSocket server = null;
    public static Vector<Worker2> workers = new Vector<>();

    public static void main(String[] args) throws IOException {
        int i = 0;
        ExecutorService executor = Executors.newFixedThreadPool(numThread);

            server = new ServerSocket(port);
            System.out.println("Server binding at port " + port);
            System.out.println("Waiting for client...");
        try {
            while(true) {

                    Socket socket = server.accept();
                    Worker2 client = new Worker2(socket);
                    //workers.add(client);
                    executor.execute(client);

                } }catch (IOException e) {
                    System.out.println(e);
                } finally {
                    if (server != null)
                        server.close();
                }
            }
            }

