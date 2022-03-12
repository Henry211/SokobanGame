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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import sokoban_henry.model.JuegoDto;
import sokoban_henry.service.JuegoService;
import sokoban_henry.util.AppContext;
import sokoban_henry.util.Mensaje;
import sokoban_henry.util.Respuesta;

/**
 * FXML Controller class
 *
 * @author Henry
 */
public class CargarController implements Initializable {

    @FXML
    private Button btnCargarUno;
    @FXML
    private Button btnCargarDos;
    @FXML
    private Button btnCargarTres;
    @FXML
    private Label txtUno;
    @FXML
    private Label txtDos;
    @FXML
    private Label txtTres;
    @FXML
    private ImageView imgFondo;
    Image imagen = new Image("/sokoban_henry/resource/cargarPartida.jpg");
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    JuegoDto juego;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.imgFondo.setImage(imagen);
    }    

    @FXML
    private void cargarUnoOnAction(ActionEvent event) {
    /*    JuegoService service = (JuegoService) AppContext.getInstance().get("JuegoService");
        Respuesta respuesta = service.getJuego(id);

        if (respuesta.getEstado()) {
            juego = (JuegoDto) respuesta.getResultado("Juego");
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Cargar empleado", respuesta.getMensaje());
        }
        
        cargarJuegoView(juego);*/
    }

    @FXML
    private void cargarDosOnAction(ActionEvent event) {
    /*    JuegoService service = (JuegoService) AppContext.getInstance().get("JuegoService");
        Respuesta respuesta = service.getJuego(id);

        if (respuesta.getEstado()) {
            juego = (JuegoDto) respuesta.getResultado("Juego");
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Cargar empleado", respuesta.getMensaje());
        }
        
        cargarJuegoView(juego);*/
    }

    @FXML
    private void cargarTresOnAction(ActionEvent event) {
    /*    JuegoService service = (JuegoService) AppContext.getInstance().get("JuegoService");
        Respuesta respuesta = service.getJuego(id);

        if (respuesta.getEstado()) {
            juego = (JuegoDto) respuesta.getResultado("Juego");
        } else {
            new Mensaje().show(Alert.AlertType.CONFIRMATION, "Cargar empleado", respuesta.getMensaje());
        }
        
        cargarJuegoView(juego);*/
    }
    
    private void cargarJuegoView(JuegoDto juego){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/juego.fxml"));
            Parent root = loader.load();
            juegoController  juegoController = loader.getController();
            juegoController.setJuego(juego);
            
            Scene scene = btnCargarUno.getScene();
            
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
