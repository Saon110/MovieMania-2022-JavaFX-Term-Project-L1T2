package server;

import models.MovieList;
import util.FileOperations;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FileWriteThreadServer implements Runnable{
    Server server;
    Thread t;
    MovieList movieDataBase;
    ConcurrentHashMap<String,String> passwordList;
    int seenCount = 0;

    public FileWriteThreadServer(Server server){
        this.server = server;
        movieDataBase = server.getMovieDataBase();
        passwordList = server.getPasswordList();
        t = new Thread(this,"File Write Thread of Server");
        t.start();
    }

    public void run(){
        //System.out.println("In Write Thread");
        while(true){
            try {
                Thread.sleep(1000);  // Write updates to database
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("Checking for updates done by clients...");
            //System.out.println(server.getUpdateCount());
            if(server.getUpdateCount()>seenCount){

                seenCount = server.getUpdateCount();
                try {
                    FileOperations.writeAllMoviesToFile(movieDataBase);
                    FileOperations.writePasswordsToFile(passwordList);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Update found and written to Database");
                //System.out.println("Update written to Database by Write Thread of Server...");
            }
            else{
                //System.out.println("No update found");
            }
        }
    }
}
