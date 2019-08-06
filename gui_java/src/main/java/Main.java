import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Main extends Application {
    private final TableView<ExchangesRate> tableView = new TableView<>();
    private final XYChart.Series series = new XYChart.Series();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Exchanges Rates");
        Button btn = new Button("Refresh");
        btn.setOnAction(event -> refresh());



        TableColumn<ExchangesRate, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<ExchangesRate, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.getColumns().addAll(dateCol, valueCol);
        tableView.setColumnResizePolicy(tableView.CONSTRAINED_RESIZE_POLICY);


        //tableView.getItems().addAll(new ExchangesRate("dateCol", "valueCol"));

        //ObservableList<ExchangesRate> list = FXCollections.observableArrayList(new ExchangesRate("dateCol", "valueCol"));
//        tableView.setItems(list);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Value $ rub");
        //creating the chart
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setTitle("Exchanges Rate");

        series.setName("Course of $");
        lineChart.getData().add(series);

        HBox hBox1 = new HBox();
        hBox1.getChildren().add(btn);
        HBox.setHgrow(btn, Priority.ALWAYS);
        btn.setMaxWidth(Double.MAX_VALUE);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(tableView, lineChart);
        HBox.setHgrow(tableView, Priority.ALWAYS);
        HBox.setHgrow(lineChart, Priority.ALWAYS);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox1, hBox);
        //vBox.setFillWidth(true);
        VBox.setVgrow(hBox, Priority.ALWAYS);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(vBox);

//        root.setMar
        primaryStage.setScene(new Scene(root, 300*2, 250*2));
        primaryStage.show();

        refresh();
    }

    void refresh() {
        tableView.getItems().clear();
        series.getData().clear();
        try {
            Class.forName("org.sqlite.JDBC");
            try (
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:../ExchangesRate.db");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT date, value FROM ExchangesRate")
            ) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("date") + " - " + resultSet.getString("value"));

                    tableView.getItems().add(
                            new ExchangesRate(resultSet.getString("date"), resultSet.getString("value"))
                    );

                    series.getData().add(new XYChart.Data(
                            resultSet.getString("date"),
                            Float.valueOf(resultSet.getString("value").replace(",", "."))
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

