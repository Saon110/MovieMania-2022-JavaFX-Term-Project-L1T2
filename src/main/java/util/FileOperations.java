package util;

import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class FileOperations {

    private static final String INPUT_FILE_NAME = "movies.txt";
    private static final String OUTPUT_FILE_NAME = "movies.txt";
    private static final String PASSWORD_FILE_NAME = "passwords.txt";
    private static final String TRAILER_FILE_NAME = "trailers.txt";

    public static List<Movie> readMoviesFromFile() throws IOException{

        List<Movie>extractedMovies = new ArrayList();

        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));

        while (true) {
            String line = br.readLine();
            if (line == null) break;

            String title;
            int releaseYear;
            List<String> genres = new ArrayList();
            int runningTime;
            String productionCompany;
            int budget;
            int revenue;

            String[] properties = line.split(",");

            title = properties[0];
            releaseYear = Integer.parseInt(properties[1]);
            int i = 2;
            while(!(properties[i].length()==0)){
                genres.add(properties[i]);
                i++;
                if(i==5) {
                    break;
                }
            }
            runningTime = Integer.parseInt(properties[5]);
            productionCompany = properties[6];
            budget = Integer.parseInt(properties[7]);
            revenue = Integer.parseInt(properties[8]);

            Movie m = new Movie(title,releaseYear,genres,runningTime,productionCompany,budget,revenue);
            extractedMovies.add(m);

        }
        br.close();

        return extractedMovies;
    }

    public static void writeAllMoviesToFile(MovieList movieDataBase) throws IOException{


        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));

        for(Movie m: movieDataBase.getMovieList()) {

            String genreText = "";
            int i = 0;
            for (String s : m.getGenres()) {
                genreText = genreText + s + ",";
                i++;
            }
            for (int j = i; j < 3; j++) {
                genreText = genreText + ",";
            }

            String text = m.getTitle() + "," + m.getReleaseYear() + "," + genreText + m.getRunningTime() + "," + m.getProductionCompany() + "," + m.getBudget() + "," + m.getRevenue();

            //System.out.println(text);

            bw.write(text);
            bw.write(System.lineSeparator());

        }

        bw.close();
    }

    public static ConcurrentHashMap<String,String> readPasswordsFromFile() throws IOException{

        ConcurrentHashMap<String,String> passwordList = new ConcurrentHashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE_NAME));

        while (true) {
            String line = br.readLine();
            if (line == null) break;

            String name;
            String password;

            String[] properties = line.split(",");

            name = properties[0];
            password = properties[1];

            if(name.length()>0) {
                passwordList.put(name, password);
            }
        }
        br.close();

        return passwordList;
    }

    public static void writePasswordsToFile(ConcurrentHashMap<String,String> passwordList) throws IOException{

        BufferedWriter bw = new BufferedWriter(new FileWriter(PASSWORD_FILE_NAME));

        for(String key : passwordList.keySet()){
            String text = key + "," + passwordList.get(key);
            //System.out.println(text);
            bw.write(text);
            bw.write(System.lineSeparator());
        }

        bw.close();

    }

    public static ConcurrentHashMap<String,String> readTrailersFromFile() throws IOException {

        ConcurrentHashMap<String,String>trailerMap = new ConcurrentHashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(TRAILER_FILE_NAME));

        while (true) {
            String line = br.readLine();
            if (line == null) break;

            String title;
            String link;

            String[] properties = line.split(",");

            title = properties[0];
            link = properties[1];

            trailerMap.put(title,link);

        }
        br.close();

        return trailerMap;
    }
}
