package sample.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsMessaging {

    public void send(String phomeNumber, String tempMessage){
        String message = tempMessage.replace(" ", "_");
        String sendThisToThis = "http://localhost/Thesis/sms/send.php?send=send&message="+message+"&phoneNumber="+phomeNumber+" ";
        System.out.println(sendThisToThis);
        //String sendThisToThis = "http://xtraordiyummy.000webhostapp.com/sms/send.php?send=send&message="+message+"&phoneNumber="+phomeNumber+" ";
        try{
            URL url = new URL(sendThisToThis);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }
            bufferedReader.close();
            System.out.println(stringBuffer.toString());
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

}
