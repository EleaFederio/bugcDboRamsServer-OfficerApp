package sample.Systems;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import sample.Utilities.Database;
import sample.Utilities.Generate;
import sample.Utilities.SmsMessaging;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SmsSender implements Initializable {
    Database database = new Database();
    Generate generate = new Generate();
    SmsMessaging sms = new SmsMessaging();
    @FXML private TextArea messageBox = new TextArea();
    @FXML private Label textCounter = new Label();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void letterCounter(){
        String message = messageBox.getText();
        int charCount = message.length();
        textCounter.setText(Integer.toString(charCount));
    }

    public void sendThisToOfficers(){
        try {
            String query = "SELECT * FROM `students` WHERE `course` = "+LoginController.courseNumber+" AND `position` > 1 ";
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String phoneNumber = resultSet.getString("phoneNumber");
                sms.send(phoneNumber, messageBox.getText() + "%0a%0a%0a-" + LoginController.userCourse + "-");
            }
            messageBox.clear();
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
    }

    public void sendThistoAllOrgMembers(){
        try {
            String query = "SELECT * FROM `students` WHERE `course` = "+LoginController.courseNumber+" ";
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String phoneNumber = resultSet.getString("phoneNumber");
                sms.send(phoneNumber, messageBox.getText() + "%0a%0a%0a-" + LoginController.userCourse + "-");
            }
            messageBox.clear();
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }
    }

    public void sendThisToAll(){
        int studentId = 0;
        String fullname = "";
        String phoneNumber = "";
        try{
            ArrayList fees = new ArrayList();
            ArrayList feesSanction = new ArrayList();
            ArrayList events = new ArrayList();
            ArrayList eventsSanction = new ArrayList();
            database.connect();
            Statement statement = database.connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM `students`");
            while (result.next()){
                studentId = result.getInt("id");
                fullname = result.getString("firstName")+" "+result.getString("lastName");
                phoneNumber = result.getString("phoneNumber");

                //**********************************************************//
                Statement statement1 = database.connection.createStatement();
                ResultSet result1 = statement1.executeQuery("SELECT * FROM `fees"+LoginController.userCourse+"`");
                while (result1.next()){
                    String feesName = result1.getString("contributionName");
                    String feesSanctionString = result1.getString("sanction");
                    Statement statement2 = database.connection.createStatement();
                    ResultSet result2 = statement2.executeQuery("SELECT * FROM `"+generate.feesTableName(feesName)+"` WHERE `student` = "+studentId+" ");
                    if (result2.next()){

                    }else {
                        fees.add(feesName);
                        if (feesSanctionString.equals(null)){
                            System.out.println("no sanction");
                        }else {
                            feesSanction.add(feesSanctionString);
                        }
                    }
                }
                //**********************************************************//

                //**********************************************************//
                Statement statement3 = database.connection.createStatement();
                ResultSet result3 = statement3.executeQuery("SELECT * FROM `events"+LoginController.userCourse+"`");
                while (result3.next()){
                    String eventName = result3.getString("eventName");
                    String eventSanctionString = result3.getString("sanction");
                    Statement statement4 = database.connection.createStatement();
                    ResultSet result4 = statement4.executeQuery("SELECT * FROM `"+generate.eventsTableName(eventName)+"` WHERE `student` = "+studentId+" ");
                    if (result4.next()){

                    }else {
                        events.add(eventName);
                        if (eventSanctionString.equals(null)){
                            System.out.println("no sanction");
                        }else {
                            eventsSanction.add(eventSanctionString);
                        }
                    }
                }
                //**********************************************************//
                System.out.println(studentId+" "+fullname+" "+phoneNumber);
                StringBuffer eventSanctionText = new StringBuffer();
                StringBuffer sanction = new StringBuffer();
                for (int x = 0; x < fees.size(); x++){
                    eventSanctionText.append(fees.get(x) + ",");
                }for (int x = 0; x < feesSanction.size(); x++){
                    sanction.append(feesSanction.get(x) + ",");
                }for (int x = 0; x < events.size(); x++){
                    eventSanctionText.append(events.get(x) + ",");
                }for (int x = 0; x < eventsSanction.size(); x++){
                    sanction.append(eventsSanction.get(x) + ",");
                }
                fees.clear();
                events.clear();
                feesSanction.clear();
                eventsSanction.clear();
                if(eventSanctionText.length() > 0){
                    System.out.println(eventSanctionText);
                    String smsMessage = "You did not pay/attend the following " + eventSanctionText + ". Please bring the following "+sanction+". ";
                    sms.send(phoneNumber,smsMessage.replace(",.", "."));
                    eventSanctionText.delete(0, eventSanctionText.length());
                }
            }
            database.connection.close();
        }catch (SQLException sql){
            sql.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
