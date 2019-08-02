import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> {
            System.out.println("Hello World!");

            foo();
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    void foo() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:../ExchangesRate.db");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT date, value FROM ExchangesRate")
            ) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("date") + " - " + resultSet.getString("value"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
