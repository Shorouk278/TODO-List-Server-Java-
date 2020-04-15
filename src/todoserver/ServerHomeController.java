/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoserver;

import model.DatabaseModel;
import handler.Handler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Fouad
 */
public class ServerHomeController implements Initializable, Runnable {

    private Label label;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button states_btn;
    @FXML
    private Button lists_btn;
    @FXML
    private Button start_btn;
    @FXML
    private BarChart<String, Number> Statistics_graph;
    @FXML
    private NumberAxis y_axis;
    @FXML
    private CategoryAxis x_axis;

    static ServerSocket mySocket;
    static Thread thread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Statistics_graph.setVisible(false);
        states_btn.setVisible(false);
        lists_btn.setVisible(false);

    }

    @FXML
    private void manageConnection(ActionEvent event) {
        if (start_btn.getText().equalsIgnoreCase("start")) {
            startConnection();
            start_btn.setText("stop");
        } else {
            stopConnection();
            start_btn.setText("start");
        }

    }

    /**
     * this method start database and socket connections
     */
    private void startConnection() {
        Statistics_graph.setVisible(false);
        states_btn.setVisible(true);
        lists_btn.setVisible(true);
        thread = new Thread(this);
        thread.start();
    }

    private void stopConnection() {
        try {
            Statistics_graph.setVisible(false);
            states_btn.setVisible(false);
            lists_btn.setVisible(false);
            mySocket.close();
            DatabaseModel.closeConnection();
            for (Handler user : Handler.clientsVector) {
                user.ps.close();
                user.dis.close();
                user.stop();
            }
            Handler.clientsVector.clear();
            thread.stop();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Can Not Close Connection").show();
        }
    }

    @Override
    public void run() {
        try {
            mySocket = new ServerSocket(5005);
            DatabaseModel.connectToDataBase();
            while (true) {
                Socket s = mySocket.accept();
                new Handler(s);
            }
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Can Not Manage Connection");
        }
    }

    @FXML
    private void statesBtnPressed(ActionEvent event) {
        Statistics_graph.getData().clear();
        Statistics_graph.setVisible(true);
        x_axis.setLabel("Status");
        y_axis.setLabel("Count");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("All Users");
        series1.getData().add(new XYChart.Data("All Users", DatabaseModel.getUsersNumber()));
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Online");
        series2.getData().add(new XYChart.Data("Online", DatabaseModel.getUsersNumberBystate("true")));
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Offline");
        series3.getData().add(new XYChart.Data("Offline", DatabaseModel.getUsersNumberBystate("false")));

        Statistics_graph.setTitle("Users Statistics");
        Statistics_graph.getData().addAll(series1, series2, series3);
    }

    @FXML
    private void statisticsBtnPressed(ActionEvent event) {
        Statistics_graph.getData().clear();
        Statistics_graph.setVisible(true);
        x_axis.setLabel("Status");
        y_axis.setLabel("Count");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Todo");
        series1.getData().add(new XYChart.Data("Todo", DatabaseModel.ListsNumberByStatus("todo")));
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Inprogress");
        series2.getData().add(new XYChart.Data("Inprogress", DatabaseModel.ListsNumberByStatus("inprogress")));

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Done");
        series3.getData().add(new XYChart.Data("", DatabaseModel.ListsNumberByStatus("done")));

        Statistics_graph.setTitle("Lists Statistics");
        Statistics_graph.getData().addAll(series1, series2, series3);

    }
}
