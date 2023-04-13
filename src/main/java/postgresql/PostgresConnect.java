package postgresql;

import java.sql.*;
import java.io.*;

public class PostgresConnect {

    private final String jdbcURL;
    private final String username;
    private final String password;
    private Connection connection;
    public PostgresConnect(String u, String p) {
        jdbcURL = "jdbc:postgresql://localhost:5432/";
        username = u;
        password = p;
    }

    public void connect(){
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to PostgreSQL server");

        } catch (SQLException e){
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
        }
    }

    public void connectDatabase(){
        try {
            connection = DriverManager.getConnection(jdbcURL + "eHotel", username, password);
            System.out.println("Connected to eHotel database");

        } catch (SQLException e){
            System.out.println("Error in connecting to eHotel database");
            e.printStackTrace();
        }
    }


    public void create() throws SQLException{
        String sql = readSQL("src/main/resources/create.sql");
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        System.out.println("eHotel database successfully created");
        connection.close();
        connection = null;
        connection = DriverManager.getConnection(jdbcURL + "eHotel", username, password);
        System.out.println("Connected to eHotel database");
    }
    public void initialize(){
        try{
            String sql = readSQL("src/main/resources/table.sql");
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("eHotel database successfully initialized");
        } catch (SQLException e){
            System.out.println("eHotel database already initialized");
        }
    }

    public void populate(){
        try{
            String sql = readSQL("src/main/resources/populate.sql");
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("eHotel database successfully populated");
        } catch (SQLException e){
            System.out.println("eHotel database already populated");
        }
    }

    public void createIndices(){
        try{
            String sql = readSQL("src/main/resources/indices.sql");
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("eHotel database indices successfully created");
        } catch (SQLException e){
            System.out.println("eHotel database indices already created");
        }
    }

    public void disconnect(){
        try{
            connection.close();
            System.out.println("Disconnected from PostgreSQL server");
        } catch (SQLException e){
            System.out.println("Error in disconnecting from PostgreSQL server");
            e.printStackTrace();
        }
    }

    private String readSQL(String fileName){
        try {
            StringBuilder str = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null){
                str.append(line);
            }
            return str.toString();
        } catch (FileNotFoundException e) {
            System.out.println("fuck off");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("piss off");
            e.printStackTrace();
        }
        return "";
    }

    public ResultSet runSQL(String sql) throws SQLException{
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }


}
