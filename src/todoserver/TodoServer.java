/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todoserver;

import handler.Handler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DatabaseModel;

/**
 *
 * @author Fouad
 */
public class TodoServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ServerHome.fxml"));
               
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.setOnCloseRequest((event) -> {
            try {
                ServerHomeController.mySocket.close();
                ServerHomeController.thread.stop();
                DatabaseModel.closeConnection();
            for (Handler user : Handler.clientsVector) {
                user.ps.close();
                user.dis.close();
                user.stop();
            }
            
            } catch (IOException ex) {
                Logger.getLogger(TodoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

      
        launch(args);
    }
    
}
