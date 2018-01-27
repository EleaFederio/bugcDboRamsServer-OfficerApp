package sample.Systems;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Utilities.Database;
import sample.Utilities.SanctionData;
import sample.Utilities.SanctionDataTwo;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Sanctions implements Initializable{
    Database database = new Database();
    @FXML private TableView<SanctionData> eventSanction = new TableView<>();
    @FXML private TableColumn<SanctionData, String> eventField = new TableColumn<>();
    @FXML private TableColumn<SanctionData, String>  eventSanctionField = new TableColumn<>();
    @FXML private TableView<SanctionDataTwo> feesTable = new TableView<>();
    @FXML private TableColumn<SanctionDataTwo, String> feesField = new TableColumn<>();
    @FXML private TableColumn<SanctionDataTwo, String> feesSanctionField = new TableColumn<>();
    @FXML private JFXTextField eventSanctionNameField = new JFXTextField();
    @FXML private JFXTextField feesSanctionNameField = new JFXTextField();
    String eventName, eventSanctionData, feesName, feesSanctionData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        feesField.setCellValueFactory(new PropertyValueFactory<>("eventOrFees"));
        feesSanctionField.setCellValueFactory(new PropertyValueFactory<>("sanction"));
        feesTable.getColumns().clear();
        feesTable.setItems(getFeesSanction());
        feesTable.getColumns().addAll(feesField, feesSanctionField);

        eventField.setCellValueFactory(new PropertyValueFactory<>("eventOrFees"));
        eventSanctionField.setCellValueFactory(new PropertyValueFactory<>("sanction"));
        eventSanction.getColumns().clear();
        eventSanction.setItems(getEventSanction());
        eventSanction.getColumns().addAll(eventField, eventSanctionField);
    }

    public void eventSanctionSelected(){
        SanctionData eventData =  eventSanction.getSelectionModel().getSelectedItem();
        eventName = eventData.getEventOrFees();
        eventSanctionData = eventData.getSanction();
    }

    public void feesSanctionSelected(){
        SanctionDataTwo feesData = feesTable.getSelectionModel().getSelectedItem();
        feesName = feesData.getEventOrFees();
        feesSanctionData = feesData.getSanction();
    }

    public void setEventSanction(){
        if (eventSanctionNameField.getText().isEmpty()){
            System.out.println("no sanction entered for events");
        }else {
            try {
                database.connect();
                Statement statement = database.connection.createStatement();
                int updated = statement.executeUpdate("UPDATE `events"+LoginController.userCourse+"` SET `sanction` = '"+eventSanctionNameField.getText()+"' " +
                        " WHERE `eventName` = '"+eventName+"' ");
                if (updated == 1){
                    System.out.println("events sanction updated");
                }
                eventSanctionNameField.clear();
                eventSanction.setItems(getEventSanction());
                database.connection.close();
            }catch (SQLException sql){
                sql.printStackTrace();
            }
        }
    }

    public void setFeesSanction(){
        if (feesSanctionNameField.getText().isEmpty()){
            System.out.println("no sanction entered for fees");
        }else {
            try{
                database.connect();
                Statement statement = database.connection.createStatement();
                int updated = statement.executeUpdate("UPDATE `fees"+LoginController.userCourse+"` SET `sanction` = '"+feesSanctionNameField.getText()+"' " +
                        " WHERE `contributionName` = '"+feesName+"' ");
                if (updated == 1){
                    System.out.println("fees sanction updated");
                }
                feesSanctionNameField.clear();
                feesTable.setItems(getFeesSanction());
                database.connection.close();
            }catch (SQLException sql){
                sql.printStackTrace();
            }
        }
    }

    public void resetEventSanction(){
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int resetThis = statement.executeUpdate("UPDATE `events"+LoginController.userCourse+"` SET `sanction` = NULL WHERE `eventName` = '"+eventName+"' ");
            if (resetThis == 1){
                System.out.println(resetThis+" event data reset");
            }
            eventSanction.setItems(getEventSanction());
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
    }

    public void resetFeedSanction(){
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int resetThis = statement.executeUpdate("UPDATE `fees"+LoginController.userCourse+"` SET `sanction` = NULL WHERE `contributionName` = '"+feesName+"' ");
            if (resetThis == 1){
                System.out.println("fees data reset");
            }
            feesTable.setItems(getFeesSanction());
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
    }

    public ObservableList<SanctionData> getEventSanction(){
        ObservableList<SanctionData> sanctionData = FXCollections.observableArrayList();
        String eventName;
        String sanction;
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `events"+LoginController.userCourse+"` ");
            while (resultSet.next()){
                eventName = resultSet.getString("eventName");
                String tempSanctionName = resultSet.getString("sanction");
                if (tempSanctionName == null){
                    sanction = "No Sanction";
                }else{
                    sanction = tempSanctionName;
                }
                sanctionData.add(new SanctionData(eventName,sanction));
            }
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return sanctionData;
    }

    public ObservableList<SanctionDataTwo> getFeesSanction(){
        ObservableList<SanctionDataTwo> sanctionData = FXCollections.observableArrayList();
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `fees"+LoginController.userCourse+"` ");
            while (resultSet.next()){
                String feesName = resultSet.getString("contributionName");
                String tempSanction = resultSet.getString("sanction");
                String sanction;
                if (tempSanction == null){
                    sanction = "No Sanction";
                }else {
                    sanction = tempSanction;
                }
                sanctionData.add(new SanctionDataTwo(feesName, sanction));
            }
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
        return sanctionData;
    }

}
