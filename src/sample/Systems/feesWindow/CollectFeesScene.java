package sample.Systems.feesWindow;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.Systems.LoginController;
import sample.Utilities.Database;
import sample.Utilities.Generate;
import sample.Utilities.SmsMessaging;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CollectFeesScene implements Initializable{
    Generate generate = new Generate();
    Database database = new Database();
    @FXML private JFXTextField studentSearchField = new JFXTextField();
    @FXML private ComboBox<String> feesList = new ComboBox<>();
    @FXML private Label firstNameLabel, lastNameLabel, courseLabel, yearLabel, blockLabel = new Label();
    final ObservableList feesListItems = FXCollections.observableArrayList();
    String firstName, lastName, course, block, choosenFee, tableName;
    int year, contributionId;
    static int studentId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getFeesList();
        feesList.setItems(feesListItems);
        reset();
    }

    public void reset(){
        firstNameLabel.setText("");
        lastNameLabel.setText("");
        courseLabel.setText("");
        yearLabel.setText("");
        blockLabel.setText("");
    }

    public void getFeesList(){
        String selectFees = "SELECT * FROM `fees"+LoginController.userCourse+"` ";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFees);
            while (resultSet.next()){
                feesListItems.add(resultSet.getString("contributionName"));
            }
            database.connection.close();
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void selectFees(){
        choosenFee = feesList.getValue();
        try {
            String selectFee = "SELECT * FROM fees"+LoginController.userCourse+" WHERE `contributionName` = '"+choosenFee+"' ";
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFee);
            while (resultSet.next()){
                contributionId = resultSet.getInt("feesId");
                System.out.println(contributionId);
            }
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode());
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void pay(){
        selectFees();
        //**************************************************************May Error pa dini****************************************************************//

        String orgList = "INSERT INTO `"+generate.feesTableName(feesList.getValue())+"`(`date`, `time`, `payFor`, `student`, `organizationId`) VALUES " +
                "(NOW(), NOW(), "+contributionId+", "+studentId+", "+LoginController.courseNumber+") ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int dataAdded = statement.executeUpdate(orgList);
            if (dataAdded == 1){
                JOptionPane.showMessageDialog(null, "Payment Success!");
            }
            database.connection.close();
            sendReciept();
            reset();
            feesList.setValue("");
        }catch (SQLException sql){
            System.out.println(sql.getErrorCode()+"\n"+sql);
            if (sql.getErrorCode() == 1146){
                createFeesListTable();
            }
            System.out.println(orgList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sendReciept(){
        SmsMessaging smsMessaging = new SmsMessaging();
        String selectPaidStudent = "SELECT * FROM `students` JOIN  `"+generate.feesTableName(feesList.getValue())+"` ON " +
                " `students`.`id` = `"+generate.feesTableName(feesList.getValue())+"`.`student` WHERE `id` = "+studentId+" ";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectPaidStudent);
            if (resultSet.next()){
                String paymentName = "";
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                int tempPaymentName = resultSet.getInt("payFor");
                String date = resultSet.getString("date");
                String time = resultSet.getString("time");
                try {
                    Statement statement1 = database.connection.createStatement();
                    ResultSet resultSet1 = statement1.executeQuery("SELECT * FROM `fees"+LoginController.userCourse+"` WHERE `feesId` = "+tempPaymentName+" ");
                    System.out.println("SELECT * FROM `fees` WHERE `feesId` = "+tempPaymentName+" ");
                    if (resultSet1.next()){
                        paymentName = resultSet1.getString("contributionName");
                    }
                }catch (SQLException sql){
                    sql.printStackTrace();
                }
                String smsMessage = "Thank you "+firstName+" "+lastName+" for paying ORACLE "+paymentName+" this date: "+date+" time: "+time+" ";
                String phoneNumber = resultSet.getString("phoneNumber");
                if (phoneNumber == null){
                    System.out.println("no phone Number/ not yet registered");
                }else {
                    smsMessaging.send(phoneNumber.replace("+", ""), smsMessage);
                }
            }
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
    }

    public void createFeesListTable(){
        selectFees();
        String createThisTable = "CREATE TABLE `"+generate.feesTableName(feesList.getValue())+"` (feeId INT(11) NOT NULL AUTO_INCREMENT, date DATE NOT NULL, time TIME(2) NOT NULL, " +
                "payFor INT(11) NOT NULL, student INT(11) NOT NULL, organizationId INT(11) NOT NULL, PRIMARY KEY(feeId))";
        try {
            database.connect();
            Statement statement = database.connection.createStatement();
            int create = statement.executeUpdate(createThisTable);
            System.out.println(create+" created");
            pay();
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void searchStudent(){
        String find = "SELECT * FROM `students` JOIN `courses` ON `students`.`course` = `courses`.`courseId` WHERE `studentId` = '"+studentSearchField.getText()+"' AND `organization` = '"+ LoginController.userCourse+"' ";
        try{
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(find);
            if (resultSet.next()){
                studentId = resultSet.getInt("id");
                firstName = resultSet.getString("firstName");
                lastName = resultSet.getString("lastName");
                course = resultSet.getString("courseName");
                year = resultSet.getInt("year");
                block = resultSet.getString("block");

                //**********+++++++++**********//
                firstNameLabel.setText(firstName);
                lastNameLabel.setText(lastName);
                courseLabel.setText(course);
                yearLabel.setText(Integer.toString(year));
                blockLabel.setText(block);
                studentSearchField.clear();
            }else {
                reset();
                studentSearchField.clear();
            }
        }catch (SQLException sql){
            System.out.println(sql);
        }catch (Exception ex){

        }
    }
}
