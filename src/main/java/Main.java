import postgresql.PostgresConnect;
import swinger.UserView;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        PostgresConnect connection = new PostgresConnect("postgres", "1strawberry");
        connection.connect();

        try {
            connection.create();
            connection.initialize();
            connection.populate();
        } catch (SQLException e) {
            System.out.println("eHotel database already exists");
            connection.disconnect();
            connection.connectDatabase();
        }

        new UserView(connection).setVisible(true);//connection disconnects on application close
    }
}
