/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class InformacionController implements Initializable {

    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnVolver;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void volverOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/portada.fxml"));
            Parent root = loader.load();
            PortadaController  portadaController = loader.getController();
            
            Scene scene = btnVolver.getScene();
            
            root.translateYProperty().set(scene.getHeight());
            stackPane.getChildren().add(root);
            
            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.DISCRETE);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setOnFinished(t -> {
                stackPane.getChildren().remove(anchorPane);
            });
            timeline.play();
        } catch (IOException ex) {
            Logger.getLogger(PortadaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
