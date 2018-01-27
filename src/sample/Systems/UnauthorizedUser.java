package sample.Systems;

import javafx.application.Platform;
import javafx.scene.Node;

public class UnauthorizedUser {

    public void close(){
        Platform.exit();
    }
}
