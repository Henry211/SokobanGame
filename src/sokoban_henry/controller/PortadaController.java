/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sokoban_henry.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Henry
 */
public class PortadaController implements Initializable {

    @FXML
    private ImageView imgPersonaje;
    @FXML
    private Button btnSiguiente;
    
    Image p1 = new Image("/sokoban_henry/resource/pj.png");
    Image p1Giro = new Image("/sokoban_henry/resource/pjGiro.png");
    Image p2 = new Image("/sokoban_henry/resource/pj2.png");
    Image p2Giro = new Image("/sokoban_henry/resource/pj2Giro.png");
    Image p3 = new Image("/sokoban_henry/resource/pj3.png");
    Image p3Giro = new Image("/sokoban_henry/resource/pj3Giro.png");
    Image p4 = new Image("/sokoban_henry/resource/pj4.png");
    Image p4Giro = new Image("/sokoban_henry/resource/pj4Giro.png");
    public  ArrayList<Image> personajes = new ArrayList<Image>();
    public  ArrayList<Image> personajesGiro = new ArrayList<Image>();
    int pj;
    AudioClip musica = new AudioClip(this.getClass().getResource("/sokoban_henry/resource/epic.mp3").toString());
    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnJugar;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnIfo;
    @FXML
    private Button btnCargar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // AGREGA LAS IMAGENES DE PERSONAJES AL  'vector' DE PERSONAJES
    musica.stop();
    musica.play();
    personajes.add(null);
    personajes.add(p1);
    personajes.add(p2);
    personajes.add(p3);
    personajes.add(p4);
    personajesGiro.add(null);
    personajesGiro.add(p1Giro);
    personajesGiro.add(p2Giro);
    personajesGiro.add(p3Giro);
    personajesGiro.add(p4Giro);
    this.imgPersonaje.setImage(p1);
    pj=0;// inicia el personaje '0'
    }    

    @FXML
    private void siguienteOnAction(ActionEvent event) { //CAMBIAR DE PERSONAJE
        if(pj==4){
            pj=0;
        }
        pj=pj+1;
        this.imgPersonaje.setImage(personajes.get(pj));
    }

    @FXML
    private void jugarOnAction(ActionEvent event) throws IOException {
        if(this.txtNombre.getText().isEmpty()){ //EVALÚA QUE EL NOMBRE ESTÉ ESCRITO
            Mensaje msj = new Mensaje();
            msj.show(Alert.AlertType.INFORMATION, "REQUERIMIENTOS", "Porfavor escriba su nombre de usuario o  nombre de partida");
        }else{
        musica.stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/juego.fxml"));
            Parent root = loader.load();
            juegoController  juegoController = loader.getController();
            juegoController.setPersonaje(personajes.get(pj),personajesGiro.get(pj),txtNombre.getText());
            
            Scene scene = btnJugar.getScene();

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
        }
    }

    @FXML
    private void informacionOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/informacion.fxml"));
            Parent root = loader.load();
            InformacionController  informacionController = loader.getController();
            
            Scene scene = btnJugar.getScene();
            
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

    @FXML
    private void cargarOnAction(ActionEvent event) {
        try {
            this.musica.stop();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/cargar.fxml"));
            Parent root = loader.load();
            CargarController  cargarController = loader.getController();
            
            Scene scene = btnJugar.getScene();
            
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
