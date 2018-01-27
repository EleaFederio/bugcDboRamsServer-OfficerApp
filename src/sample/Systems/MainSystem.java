package sample.Systems;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import sample.Utilities.Database;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainSystem implements Initializable{
    Database database = new Database();
    LoginController login = new LoginController();
    @FXML protected BorderPane mainPane = new BorderPane();

    @FXML private JFXButton attendanceButton = new JFXButton();
    @FXML private JFXButton eventsButton = new JFXButton();
    @FXML private JFXButton recordsButton = new JFXButton();
    @FXML private JFXButton sanctionsButton = new JFXButton();
    @FXML private JFXButton feesButton = new JFXButton();

    @FXML private Label organizationLabel = new Label();
    @FXML private Label usernameLabel = new Label();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        organizationLabel.setText(LoginController.userCourse);
        usernameLabel.setText("User: "+ LoginController.softwareUser);
        selectFees();
    }

    public void selectAttendance(){
        attendanceButton.setStyle("-fx-background-color: #81858c");
        eventsButton.setStyle("-fx-background-color:  #dfe3ee");
        recordsButton.setStyle("-fx-background-color:  #dfe3ee");
        sanctionsButton.setStyle("-fx-background-color:  #dfe3ee");
        feesButton.setStyle("-fx-background-color:  #dfe3ee");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("smsSender.fxml"));
            mainPane.setCenter(new SubScene(root, 800, 800));
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void selectEvents(){
        attendanceButton.setStyle("-fx-background-color:  #dfe3ee");
        eventsButton.setStyle("-fx-background-color: #81858c");
        recordsButton.setStyle("-fx-background-color:  #dfe3ee");
        sanctionsButton.setStyle("-fx-background-color:  #dfe3ee");
        feesButton.setStyle("-fx-background-color:  #dfe3ee");
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `events"+LoginController.userCourse+"` ");
            if (resultSet.next()){
                System.out.println("ok");
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql);
            if (sql.getErrorCode() == 1146){
                System.out.println("--- events table doesn't exist");
                createEventsTable();
            }
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("events.fxml"));
            mainPane.setCenter(new SubScene(root, 800, 800));
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void selectRecords(){
        attendanceButton.setStyle("-fx-background-color:  #dfe3ee");
        eventsButton.setStyle("-fx-background-color:  #dfe3ee");
        recordsButton.setStyle("-fx-background-color: #81858c");
        sanctionsButton.setStyle("-fx-background-color:  #dfe3ee");
        feesButton.setStyle("-fx-background-color:  #dfe3ee");
        try{
            Parent root = FXMLLoader.load(getClass().getResource("records.fxml"));
            mainPane.setCenter(new SubScene(root, 800, 800));
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void selectSanctions(){
        attendanceButton.setStyle("-fx-background-color:  #dfe3ee");
        eventsButton.setStyle("-fx-background-color:  #dfe3ee");
        recordsButton.setStyle("-fx-background-color:  #dfe3ee");
        sanctionsButton.setStyle("-fx-background-color: #81858c");
        feesButton.setStyle("-fx-background-color:  #dfe3ee");
        try{
            Parent root = FXMLLoader.load(getClass().getResource("sanctions.fxml"));
            mainPane.setCenter(new SubScene(root, 800, 800));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void selectFees(){
        attendanceButton.setStyle("-fx-background-color:  #dfe3ee");
        eventsButton.setStyle("-fx-background-color:  #dfe3ee");
        recordsButton.setStyle("-fx-background-color:  #dfe3ee");
        sanctionsButton.setStyle("-fx-background-color:  #dfe3ee");
        feesButton.setStyle("-fx-background-color: #81858c");
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `fees"+LoginController.userCourse+"` ");
            if (resultSet.next()){
                System.out.println("ok");
            }
            database.connection.close();
        }catch (SQLException sql){
            if (sql.getErrorCode() == 1146){
                System.out.println("--- fees Table doesn't exit");
                createFeesTable();
            }
        }catch (Exception ex){
            System.out.println("1 "+ex);
        }
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Fees.fxml"));
            mainPane.setCenter(new SubScene(root,800,800));
        }catch (Exception ex){
            System.out.println("2 "+ex);
        }
    }

    public void quitButton(){
        LoginController.homeWindow.close();
    }


    public void createEventsTable(){
        String createEvents = "CREATE TABLE events"+LoginController.userCourse+" (eventId INT(11) NOT NULL AUTO_INCREMENT, eventName VARCHAR(225) NOT NULL," +
                "eventVenue VARCHAR(225) NOT NULL, eventTime TIME(2) NOT NULL, eventDate DATE NOT NULL, " +
                "eventWillingness VARCHAR(20) NOT NULL, sanction VARCHAR(225), PRIMARY KEY(eventId)) ";
        try {
            System.out.println("--- creating events table...");
            Statement statement = database.connection.createStatement();
            int created = statement.executeUpdate(createEvents);
            if (created == 0){
                System.out.println("--- events table created");
            }
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    //*************Database Tables Create************//
    public void createFeesTable(){
        String createTable = "CREATE TABLE fees"+LoginController.userCourse+" (feesId INT(11) NOT NULL AUTO_INCREMENT, contributionName VARCHAR(225) NOT NULL, " +
             "amount DECIMAL(13,2) NOT NULL, organization VARCHAR(30) NOT NULL, deadline DATE, sanction VARCHAR(225), PRIMARY KEY(feesId))";
        try{
            System.out.println("--- creating fees table...");
            Statement statement = database.connection.createStatement();
            int created = statement.executeUpdate(createTable);
            if (created == 0){
                System.out.println("--- fees table created");
            }
            selectFees();
        }catch(SQLException sql){
            System.out.println("createFeesTable() Error: "+sql.getErrorCode());
            System.out.println(createTable);
        }catch(Exception ex){
            System.out.println("createFeesTable() Error: "+ex.getMessage());
        }
    }

    public void insertFeesMembershipFee(){

    }
}
