package server;

import util.NetworkUtil;
import util.StopDTO;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class InputThreadServer implements Runnable{
    Server s;
    Thread t;

    public InputThreadServer(Server s){
        this.s = s;
        t = new Thread(this,"Input Thread of Server");
        t.start();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        String next;
        while (true){
            next = scanner.nextLine();
            if (next.strip().equalsIgnoreCase("Stop")){

                var sDTO = new StopDTO(true);

                for(String ss: s.getCompanyNetworkMap().keySet()){
                    try {
                        s.getCompanyNetworkMap().get(ss).write(sDTO);
                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.out.println("Client abruptly closed previously");
                    }
                }

                System.out.println("Thank You");
                System.out.println("All Updates Permanently Saved to Database.");
                System.out.println("All clients informed of the closure.");
                System.out.println("Good Bye");
                try {
                    s.serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }
    }
}
