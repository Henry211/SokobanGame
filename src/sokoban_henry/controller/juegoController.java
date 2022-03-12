/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.controller;


import com.jfoenix.controls.JFXButton;
import static java.awt.Color.red;
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
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import sokoban_henry.clases.Mapa;
import sokoban_henry.model.JuegoDto;
import sokoban_henry.service.JuegoService;
import sokoban_henry.util.AppContext;
import sokoban_henry.util.Mensaje;
import sokoban_henry.util.Respuesta;

/**
 *
 * @author Henry
 */
public class juegoController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;            
    @FXML
    private JFXButton lvl2Btn;
    @FXML
    private JFXButton lvl3Btn;
    @FXML
    private JFXButton lvl4Btn;
    @FXML
    private JFXButton lvl5Btn;
    @FXML
    private JFXButton lvl1Btn;
    @FXML
    private JFXButton btnDeshacer;
    @FXML
    private JFXButton btnReiniciar;
    @FXML
    private Label txtUsuario;
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private Label txtMovimientos;
    @FXML
    private Label txtMovimientosLibres;
    @FXML
    private ImageView imgHasGanado;
    @FXML
    private Button btnHasGanado;
    @FXML
    private StackPane stackPane;
    
    //--boolean's--
    boolean juegoParaCargar;
    
    //--int's---------------------------------
    int gameover =0;
    int lvlCompletado[] = new int[5];
    int lvlCompletadoIndice=0;
    int numeroDeMapa;
    int haMovido;
    int movimientosPersonaje;
    int movimientosCajas;
    protected Integer deshacerMatrix[][]=new Integer[8][12];
    Integer [][] nivelUno;
    Integer [][] nivelDos;
    Integer [][] nivelTres;
    Integer [][] nivelCuatro;
    Integer [][] nivelCinco;
    
    //--ImageView's--------------------------
    public ImageView Personaje = new ImageView();
    public ImageView cajaUno = new ImageView("/sokoban_henry/resource/bloque.png");
    public ImageView cajaDos = new ImageView("/sokoban_henry/resource/bloque.png");
    public ImageView cajaTres = new ImageView("/sokoban_henry/resource/bloque.png");
    ImageView ganador = new ImageView("/sokoban_henry/resource/Victoria.jpg");
    ImageView over = new ImageView("/sokoban_henry/resource/gameOver.jpg");
    
    //--Image's-------------------------------
    public Image pjGiro;
    public Image pj;  
    public Image win=new Image("/sokoban_henry/resource/llama.png");
    public Image bloque=new Image("/sokoban_henry/resource/bloque.png");
    Image texturaUno = new Image("/sokoban_henry/resource/textura1.jpg");
    Image texturaDos= new Image("/sokoban_henry/resource/textura2.png");
    Image texturaTres= new Image("/sokoban_henry/resource/textura3.png");
    Image texturaCuatro= new Image("/sokoban_henry/resource/textura4.png");
    Image texturaCinco= new Image("/sokoban_henry/resource/textura5.png");
    
    //--Clases--------------------------------
    Mapa mapa;
    GridPane grid;
    JuegoDto juego;
    Mensaje msg = new Mensaje();    
    AudioClip movimiento = new AudioClip(this.getClass().getResource("/sokoban_henry/resource/movimiento.mp3").toString());
    AudioClip sonido;
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgHasGanado.setVisible(false);
        btnHasGanado.setVisible(false);
        //--CARGA TODOS LOS NIVELES--
        cargarNiveles();      
    }   
    
    private void cargarMapa(Integer matrixMapa[][], int numeroMapa, Image textura){
        grid = new GridPane(); 
        grid.getChildren().clear();

        colocarPersonaje(numeroMapa);
        
        matrixMapa = evaluaBaseDeDatos(matrixMapa);
        
        setMatrizLógica(matrixMapa);
        
        imprimirEstrellas();       
        imprimirMuros(textura);       
        imprimirCajas();
        
        eventosJava(textura);
        
        grid.setLayoutX(150);
        grid.setLayoutY(130);
        anchorPane.getChildren().add(grid);   
    }
    
   
    public void setPersonaje(Image image, Image imageGiro, String nombre) {
        txtUsuario.setText(nombre);
        pj = image;
        pjGiro = imageGiro;
        Personaje.setImage(image);
    }    
    //MÉTODO SOBRECARGADO
    //  "Coloca el personaje  con coordenadas (x,y)"
    private void colocarPersonaje(int posX, int posY){
        grid.add(Personaje, posX, posY);
    }
    //MÉTODO SOBRECARGADO
    //  "Coloca el personaje dependiendo del Nivel"
    private void colocarPersonaje(int nivel){
        Personaje.setFitHeight(55);
        Personaje.setFitWidth(55);
        switch (nivel) {
            case 1:
                grid.add(Personaje, 4, 5); break;
            case 2:
                grid.add(Personaje, 1, 1); break;
            case 3:
                grid.add(Personaje, 3, 5); break;
            case 4:
                grid.add(Personaje, 1, 2); break;
            case 5:   
                grid.add(Personaje, 2, 2); break;
        }
    }
    // SET MATRIZ 'Integer' (matriz lógica)
    private void setMatrizLógica(Integer matriz[][]){
        mapa = new Mapa();
        int elemento;
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                elemento = matriz[i][j];
                mapa.cargarElemento(i,j,elemento);
            }
        }
    }
    //  imprime los 'ImageView' de las ESTRELLAS, en el gridPane
    private void imprimirEstrellas(){
        for(int i = 0;i<8;i++){
            for(int j=0;j<12;j++){
                int valor = mapa.getElemento(i,j); 
                if(valor==2){
                    ImageView estrella = new ImageView("/sokoban_henry/resource/estrellaGreen.png");
                    estrella.setFitHeight(55);
                    estrella.setFitWidth(55);
                    GridPane.setVgrow(estrella, Priority.ALWAYS);
                    GridPane.setHgrow(estrella, Priority.ALWAYS);
                    grid.add(estrella, j, i);
                }
            }
        }
    }
    // imprime los 'ImageVew' de las PAREDES, en el gridPane
    private void imprimirMuros(Image textura){
        for(int i = 0;i<8;i++){
            for(int j=0;j<12;j++){         
                if(mapa.getElemento(i,j)==1){
                    ImageView muro = new ImageView(textura);
                    muro.setFitHeight(55);
                    muro.setFitWidth(55);   
                    GridPane.setVgrow(muro, Priority.ALWAYS);
                    GridPane.setHgrow(muro, Priority.ALWAYS);
                    grid.add(muro, j, i);
                }
            }
        }
    }
    // imprime los 'ImageVew' de las Cajas, en el gridPane
    private void imprimirCajas(){
        for(int i = 0;i<8;i++){
            for(int j=0;j<12;j++){       
                if(mapa.getElemento(i,j)==3 ){//ASIGNA LA CAJA 1
                    cajaUno.setFitHeight(55);
                    cajaUno.setFitWidth(55);
                    GridPane.setVgrow(cajaUno, Priority.ALWAYS);
                    GridPane.setHgrow(cajaUno, Priority.ALWAYS);
                    grid.add(cajaUno, j, i);
                }else if(mapa.getElemento(i,j)==5){//ASIGNA LA CAJA 2
                    cajaDos.setFitHeight(55);
                    cajaDos.setFitWidth(55);
                    GridPane.setVgrow(cajaDos, Priority.ALWAYS);
                    GridPane.setHgrow(cajaDos, Priority.ALWAYS);
                    grid.add(cajaDos, j, i);
                }else if(mapa.getElemento(i,j)==7){//ASIGNA LA CAJA 3
                    cajaTres.setFitHeight(55);
                    cajaTres.setFitWidth(55);
                    GridPane.setVgrow(cajaTres, Priority.ALWAYS);
                    GridPane.setHgrow(cajaTres, Priority.ALWAYS);
                    grid.add(cajaTres, j, i);
                }
            }
        }    
    }
    
    // Se implementan los eventos:
    // 'setOnKeyPressed' -> para detectar las teclas de flechas.
    // 'setOnMouseClicked' -> para deshacer un movimiento y para intentarlo denuevo luego de perder
    private void eventosJava(Image textura){
        anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                String teclaDirección = event.getCode().toString();
                //------ X y Y de la posición del Personaje -----
                int x = GridPane.getRowIndex(Personaje);
                int y = GridPane.getColumnIndex(Personaje);
                                                            //  ---------VALIDACIONES LEFT------------
                int valorUnoLeft = mapa.getElemento(x,y-1);//    lo que hay '1' cuadro a la izquierda
                int valorDosLeft = mapa.getElemento(x,y-2);//    lo que hay '2' cuadros a la izquierda
                                                           //  ---------VALIDACIONES UP---------------
                int valorUnoUp = mapa.getElemento(x-1,y);//    lo que hay '1' cuadro a arriba
                int valorDosUp = mapa.getElemento(x-2,y);//    lo que hay '2' cuadros a arriba
                                                           //  ---------VALIDACIONES DOWN-------------                
                int valorUnoDown = mapa.getElemento(x+1,y);//    lo que hay '1' cuadro a abajo
                int valorDosDown = mapa.getElemento(x+2,y);//    lo que hay '2' cuadros a abajo
                                                            //  ---------VALIDACIONES RIGHT------------              
                int valorUnoRight = mapa.getElemento(x,y+1);//    lo que hay '1' cuadro a la derecha
                int valorDosRight = mapa.getElemento(x,y+2);//    lo que hay '2' cuadros a la derecha
                
                
                if(null!=teclaDirección)switch (teclaDirección) {
                    case "LEFT":
                        movimientosPersonaje++;
                        txtMovimientosLibres.setText(String.valueOf(movimientosPersonaje));
                        movimiento.play();
                        Personaje.setImage(pjGiro);
                        
                        // ------'Personaje' camina Libre----
                        if(valorUnoLeft == 0 || valorUnoLeft == 2){   //puede moverse (si 'está vacio' ó 'hay estrella')
                            GridPane.setColumnIndex(Personaje,y-1);
                        }       
                        // ------'Personaje' empuja 'Cajas'------
                        validarCajasLeft(valorUnoLeft,valorDosLeft,mapa);//--AQUÍ HACE TODAS LAS VALIDACIONES PARA MOVER CAJAS A LA ISQUIERDA. 
                        break;
                    case "UP":
                        movimientosPersonaje++;
                        txtMovimientosLibres.setText(String.valueOf(movimientosPersonaje));
                        movimiento.play();
                        
                        // ------'Personaje' camina Libre----
                        if(valorUnoUp == 0 || valorUnoUp == 2){ //puede moverse si 'está vacio' ó 'hay estrella'
                            GridPane.setRowIndex(Personaje,x-1);
                        }       
                        // ------'Personaje' empuja 'Cajas'------
                        validarCajasUp(valorUnoUp,valorDosUp,mapa);//--AQUÍ HACE TODAS LAS VALIDACIONES PARA MOVER CAJAS A ARRIBA. 
                        break;
                    case "DOWN":
                        movimientosPersonaje++;
                        txtMovimientosLibres.setText(String.valueOf(movimientosPersonaje));
                        movimiento.play();
                        
                        // ------'Personaje' camina Libre----
                        if(valorUnoDown == 0 || valorUnoDown == 2){ //puede moverse si 'está vacio' ó 'hay estrella'
                            GridPane.setRowIndex(Personaje,x+1);
                        }       
                        // ------'Personaje' empuja 'Cajas'------
                        validarCajasDown(valorUnoDown,valorDosDown,mapa);//--AQUÍ HACE TODAS LAS VALIDACIONES PARA MOVER CAJAS A ABAJO. 
                        break;
                    case "RIGHT":
                        movimientosPersonaje++;
                        txtMovimientosLibres.setText(String.valueOf(movimientosPersonaje));
                        Personaje.setImage(pj);
                        movimiento.play();
                        
                        // ------'Personaje' camina Libre----
                        if(valorUnoRight == 0 || valorUnoRight == 2){   //puede moverse si 'está vacio' ó 'hay estrella'
                            GridPane.setColumnIndex(Personaje,y+1);
                        }       
                        // ------'Personaje' empuja 'Cajas'------
                        validarCajasRight(valorUnoRight,valorDosRight,mapa);//--AQUÍ HACE TODAS LAS VALIDACIONES PARA MOVER CAJAS A LA DERECHA. 
                        break;
                    default:
                        break;
                }
        //---VALIDACIONES PARA DETECTAR LA VICTORIA---      
        encontrarGanador(); //detecta las 3 cajas en la meta
        
        //***---VALIDACIONES PARA ERRORES---***
        //Una evaluación x caja        
        detectarCajaUnoEncerrada();
        detectarCajaDosEncerrada();
        detectarCajaTresEncerrada();
        
                btnDeshacer.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if(haMovido == 1){
                            haMovido = 0;
                            anchorPane.getChildren().remove(grid);      
                            cargarMapa(mapa.getMatrizAnterior(),numeroDeMapa,textura);
                            //FOR para cambiar de 'llama' a 'caja'----
                            for(int i=0;i<8;i++){
                                for(int j =0; j<12;j++){
                                    if(mapa.getMatrizAnterior()[i][j]==3){
                                        cajaUno.setImage(bloque);
                                    }
                                    if(mapa.getMatrizAnterior()[i][j]==5){
                                        cajaDos.setImage(bloque);
                                    }
                                    if(mapa.getMatrizAnterior()[i][j]==7){
                                        cajaTres.setImage(bloque);
                                    }
                                }
                            }    
                        }else{  
                            msg.show(Alert.AlertType.CONFIRMATION, "Deshacer Movimiento", "No puedes deshacer más de un movimiento");
                        }
                    }   
                });    
              }
           });
    }
    
    private void encontrarGanador(){
        int puntos = mapa.getPuntaje();
        if (puntos==3){
            ganador.setFitHeight(330);
            ganador.setFitWidth(570);
            ganador.setLayoutX(185);
            ganador.setLayoutY(150);
            lvlCompletado[lvlCompletadoIndice] = 1;
            if(lvlCompletado[4] == 1){
                anchorPane.getChildren().remove(grid);
                imgHasGanado.setVisible(true);
                btnHasGanado.setVisible(true);
            }else{
            lvlCompletadoIndice++;
            anchorPane.getChildren().add(ganador);
            grid.getChildren().clear();
            mapa.limpiar();
            }
        }
    }
    
    private void detectarCajaUnoEncerrada(){
        //--guarda la fila y la columna de la caja.---
        int rowUno = GridPane.getRowIndex(cajaUno);
        int columnUno = GridPane.getColumnIndex(cajaUno);
        //--guarda el valor de la caja---
        int elementoCajaUno = mapa.getElemento(rowUno,columnUno);
        //-- Guarda el valor de la isquierda--
        int elementoLeftCajaUno = mapa.getElemento(rowUno, columnUno-1);
        // -- Guarda el valor de arriba--
        int elementoUpCajaUno = mapa.getElemento(rowUno-1, columnUno);
        // --Guarda el valor de la derecha--
        int elementoRightCajaUno = mapa.getElemento(rowUno, columnUno+1);
        // --Guardar el valor de abajo --
        int elementoDownCajaUno = mapa.getElemento(rowUno+1, columnUno);
        
        //--Evalua las formas de encerrarse--
        if(elementoCajaUno!=6 && elementoLeftCajaUno==1 && elementoUpCajaUno==1  ||//esquina superior izquierda
           elementoCajaUno!=6 && elementoRightCajaUno==1 && elementoUpCajaUno==1 ||//esquina superior derecha
           elementoCajaUno!=6 && elementoLeftCajaUno==1 && elementoDownCajaUno==1||//esquina inferior izquierda
           elementoCajaUno!=6 && elementoRightCajaUno==1 && elementoDownCajaUno==1){//esquina inferior derecha
            // ---IMPRIME 'GameOver' y recibe el 'Click' para limpiar--
                    over.setFitHeight(373);
                    over.setFitWidth(584);
                    over.setLayoutX(150);
                    over.setLayoutY(130);
                    grid.getChildren().clear();
                    anchorPane.getChildren().add(over);
                    over.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            anchorPane.getChildren().remove(over);
                            mapa.limpiar();
                                                                
                        }
                    });
                    audioGameOver();
                    gameover=1; 
        }
    }
    private void detectarCajaDosEncerrada(){
        //--guarda la fila y la columna de la caja.---
        int rowDos = GridPane.getRowIndex(cajaDos);
        int columnDos = GridPane.getColumnIndex(cajaDos);
        //--guarda el valor de la caja---
        int elementoCajaDos = mapa.getElemento(rowDos,columnDos);
        //-- Guarda el valor de la isquierda--
        int elementoLeftCajaDos = mapa.getElemento(rowDos, columnDos-1);
        // -- Guarda el valor de arriba--
        int elementoUpCajaDos = mapa.getElemento(rowDos-1, columnDos);
        // --Guarda el valor de la derecha--
        int elementoRightCajaDos = mapa.getElemento(rowDos, columnDos+1);
         // --Guardar el valor de abajo --
        int elementoDownCajaDos = mapa.getElemento(rowDos+1, columnDos);
        if(elementoCajaDos!=10 && elementoLeftCajaDos==1 && elementoUpCajaDos==1  ||//esquina superior izquierda
           elementoCajaDos!=10 && elementoRightCajaDos==1 && elementoUpCajaDos==1 ||//esquina superior derecha
           elementoCajaDos!=10 && elementoLeftCajaDos==1 && elementoDownCajaDos==1||//esquina inferior izquierda
           elementoCajaDos!=10 && elementoRightCajaDos==1 && elementoDownCajaDos==1){//esquina inferior derecha
                    over.setFitHeight(370);
                    over.setFitWidth(570);
                    over.setLayoutX(150);
                    over.setLayoutY(130);
                    grid.getChildren().clear();
                    anchorPane.getChildren().add(over);
                    over.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            anchorPane.getChildren().remove(over);
                            mapa.limpiar();
                        }
                    });
                    audioGameOver();
                    gameover=1; 
        }
    }
    private void detectarCajaTresEncerrada(){
        int rowTres = GridPane.getRowIndex(cajaTres);
        int columnTres = GridPane.getColumnIndex(cajaTres);
        int elementoCajaTres = mapa.getElemento(rowTres,columnTres);
        int elementoLeftCajaTres = mapa.getElemento(rowTres, columnTres-1);
        int elementoUpCajaTres = mapa.getElemento(rowTres-1, columnTres);
        int elementoRightCajaTres = mapa.getElemento(rowTres, columnTres+1);
        int elementoDownCajaTres = mapa.getElemento(rowTres+1, columnTres);
        if(elementoCajaTres!=14 && elementoLeftCajaTres==1 && elementoUpCajaTres==1  ||//esquina superior izquierda
           elementoCajaTres!=14 && elementoRightCajaTres==1 && elementoUpCajaTres==1 ||//esquina superior derecha
           elementoCajaTres!=14 && elementoLeftCajaTres==1 && elementoDownCajaTres==1||//esquina inferior izquierda
           elementoCajaTres!=14 && elementoRightCajaTres==1 && elementoDownCajaTres==1){//esquina inferior derecha
                    over.setFitHeight(373);
                    over.setFitWidth(584);
                    over.setLayoutX(150);
                    over.setLayoutY(130);
                    grid.getChildren().clear();
                    anchorPane.getChildren().add(over);
                    over.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            anchorPane.getChildren().remove(over);
                            mapa.limpiar();
                        }
                    });
                    audioGameOver();
                    gameover=1; 
        }
    }
    
    
    private void validarCajasLeft(int valorUno, int valorDos,Mapa mapa){ // '4' VALIDACIONES X CAJA
                //-----------VALIDACIONES 'CAJA #1'------------
                //si cuadro izquierdo tiene caja(3) y segundo izquierdo está vacio(0)
                if(valorUno == 3 && valorDos==0){
                        moverCajaNormalLeft(mapa,3,cajaUno);// MUEVE LA CAJA NORMAL (3=caja)
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // si cuadro izquierdo tiene caja(3) y segundo izquierdo tiene estrella(2)
                else if(valorUno == 3 && valorDos == 2){
                        moverCajaEnMetaLeft(mapa,6,cajaUno);// MUEVE LA CAJA A LA META (6=cajaEnMeta)
                        audioMeta();
                }
              //si cuadro izquierdo tiene meta(6) y segundo izquierdo tiene estrella(2)
                else if(valorUno == 6 && valorDos == 2){
                        moverCajaEnMetaDelLadoLeft(mapa,12,cajaUno);
                }
                //Personaje saca la 'Caja 1' de la Meta --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 6 && valorDos == 0){
                        moverCajaFueraDeMetaLeft(mapa,9,cajaUno);// SACA LA CAJA DE LA META (9)
                }
                
                //-----------------VALIDACIONES 'CAJA #2'----------------
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 5 && valorDos==0){
                        moverCajaNormalLeft(mapa,5,cajaDos);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 5 && valorDos == 2){
                        moverCajaEnMetaLeft(mapa,10,cajaDos);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 10 && valorDos == 2){
                        moverCajaEnMetaDelLadoLeft(mapa,20,cajaDos);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 10 && valorDos == 0){
                        moverCajaFueraDeMetaLeft(mapa,15,cajaDos);
                }
                
                //------------------VALIDACIONES 'CAJA #3'---------------
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 7 && valorDos==0){
                        moverCajaNormalLeft(mapa,7,cajaTres);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 7 && valorDos == 2){
                        moverCajaEnMetaLeft(mapa,14,cajaTres);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 14 && valorDos == 2){
                        moverCajaEnMetaDelLadoLeft(mapa,28,cajaTres);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 14 && valorDos == 0){
                        moverCajaFueraDeMetaLeft(mapa,21,cajaTres);
                }
    }
    
    private void moverCajaNormalLeft(Mapa mapa, int id, ImageView caja){
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)-1);// mueve la caja
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)-1);// mueve al Personaje
        haMovido = mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id, "LEFT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaLeft(Mapa mapa, int id, ImageView caja){
        caja.setImage(win);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)-1);
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)-1);
        haMovido = mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"LEFT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaDelLadoLeft(Mapa mapa, int id, ImageView caja){
        //mgC1.setImage(win);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)-1);
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)-1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"LEFT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaFueraDeMetaLeft(Mapa mapa, int id, ImageView caja){
        caja.setImage(bloque);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)-1);//coloca la CAJA un cuadro a la izquierda
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)-1);//coloca a MARIO un cuadro a la izquierda
        // mapa.actualizar(fila,columna,tipo,dirección);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"LEFT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
       movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void validarCajasUp(int valorUno, int valorDos,Mapa mapa){
                //VALIDACIONES 'CAJA #1'---
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 3 && valorDos==0){
                        moverCajaNormalUp(mapa,3,cajaUno);
                }
                //Mario introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Personaje' en 'Meta'  'Caja' = 6
                else if(valorUno == 3 && valorDos == 2){
                        moverCajaEnMetaUp(mapa,6,cajaUno);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 6 && valorDos == 2){
                        moverCajaEnMetaDelLadoUp(mapa,12,cajaUno);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 6 && valorDos == 0){
                        moverCajaFueraDeMetaUp(mapa,9,cajaUno);
                }
                
                //-----VALIDACIONES 'CAJA #2'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 5 && valorDos==0){
                        moverCajaNormalUp(mapa,5,cajaDos);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 5 && valorDos == 2){
                        moverCajaEnMetaUp(mapa,10,cajaDos);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 10 && valorDos == 2){
                        moverCajaEnMetaDelLadoUp(mapa,20,cajaDos);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 10 && valorDos == 0){
                        moverCajaFueraDeMetaUp(mapa,15,cajaDos);
                }
                
                //-----VALIDACIONES 'CAJA #3'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 7 && valorDos==0){
                        moverCajaNormalUp(mapa,7,cajaTres);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 7 && valorDos == 2){
                        moverCajaEnMetaUp(mapa,14,cajaTres);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 14 && valorDos == 2){
                        moverCajaEnMetaDelLadoUp(mapa,28,cajaTres);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 14 && valorDos == 0){
                        moverCajaFueraDeMetaUp(mapa,21,cajaTres);
                }
    }
    
    private void moverCajaNormalUp(Mapa mapa,int id, ImageView caja){
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)-1);// mueve la caja
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)-1);// mueve a Mario
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id, "UP");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaUp(Mapa mapa, int id, ImageView caja){
        caja.setImage(win);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)-1);
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)-1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id, "UP");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaDelLadoUp(Mapa mapa, int id, ImageView caja){
        //mgC1.setImage(win);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)-1);
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)-1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id, "UP");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaFueraDeMetaUp(Mapa mapa, int id, ImageView caja){
        caja.setImage(bloque);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)-1);
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)-1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id, "UP");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void validarCajasDown(int valorUno, int valorDos,Mapa mapa){
                //VALIDACIONES 'CAJA #1'---
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 3 && valorDos==0){
                        moverCajaNormalDown(mapa,3,cajaUno);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 3 && valorDos == 2){
                        moverCajaEnMetaDown(mapa,6,cajaUno);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 6 && valorDos == 2){
                        moverCajaEnMetaDelLadoDown(mapa,12,cajaUno);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 6 && valorDos == 0){
                        moverCajaFueraDeMetaDown(mapa,9,cajaUno);
                }
                
                //-----VALIDACIONES 'CAJA #2'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 5 && valorDos==0){
                        moverCajaNormalDown(mapa,5,cajaDos);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 5 && valorDos == 2){
                        moverCajaEnMetaDown(mapa,10,cajaDos);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 10 && valorDos == 2){
                        moverCajaEnMetaDelLadoDown(mapa,20,cajaDos);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 10 && valorDos == 0){
                        moverCajaFueraDeMetaDown(mapa,15,cajaDos);
                }
                
                //-----VALIDACIONES 'CAJA #3'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 7 && valorDos==0){
                        moverCajaNormalDown(mapa,7,cajaTres);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 7 && valorDos == 2){
                        moverCajaEnMetaDown(mapa,14,cajaTres);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 14 && valorDos == 2){
                        moverCajaEnMetaDelLadoDown(mapa,28,cajaTres);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 14 && valorDos == 0){
                        moverCajaFueraDeMetaDown(mapa,21,cajaTres);
                }
    }
    
    private void moverCajaNormalDown(Mapa mapa, int id, ImageView caja){
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)+1);// mueve la caja
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)+1);// mueve a Mario
        haMovido = mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"DOWN");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaDown(Mapa mapa, int id, ImageView caja){
        caja.setImage(win);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)+1);     //--mover la caja '1' abajo
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)+1);   //--mover a Mario '1' abajo
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"DOWN");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaDelLadoDown(Mapa mapa, int id, ImageView caja){
        //mgC1.setImage(win);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)+1);
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)+1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"DOWN");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaFueraDeMetaDown(Mapa mapa, int id, ImageView caja){
        caja.setImage(bloque);
        GridPane.setRowIndex(caja,GridPane.getRowIndex(caja)+1);
        GridPane.setRowIndex(Personaje,GridPane.getRowIndex(Personaje)+1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"DOWN");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }

    private void validarCajasRight(int valorUno, int valorDos,Mapa mapa){
                //VALIDACIONES 'CAJA #1'---
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 3 && valorDos==0){
                        moverCajaNormalRight(mapa,3,cajaUno);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 3 && valorDos == 2){
                        moverCajaEnMetaRight(mapa,6,cajaUno);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 6 && valorDos == 2){
                        moverCajaEnMetaDelLadoRight(mapa,12,cajaUno);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 6 && valorDos == 0){
                        moverCajaFueraDeMetaRight(mapa,9,cajaUno);
                }
                
                //-----VALIDACIONES 'CAJA #2'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 5 && valorDos==0){
                        moverCajaNormalRight(mapa,5,cajaDos);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 5 && valorDos == 2){
                        moverCajaEnMetaRight(mapa,10,cajaDos);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 10 && valorDos == 2){
                        moverCajaEnMetaDelLadoRight(mapa,20,cajaDos);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 10 && valorDos == 0){
                        moverCajaFueraDeMetaRight(mapa,15,cajaDos);
                }
                
                //-----VALIDACIONES 'CAJA #3'-----
                //'Personaje' mueve la caja 1 -> left se mantiene el valor 3 en la matriz
                if(valorUno == 7 && valorDos==0){
                        moverCajaNormalRight(mapa,7,cajaTres);
                }
                //Personaje introduce la caja 1 en la meta ---- La caja ahora va a tener el valor de 6 en la matriz
                // 'Caja' en 'Meta'  'Caja' = 6
                else if(valorUno == 7 && valorDos == 2){
                        moverCajaEnMetaRight(mapa,14,cajaTres);
                        audioMeta();
                }
                //Personaje mueve la caja 1 a la meta que esta al lado
                else if(valorUno == 14 && valorDos == 2){
                        moverCajaEnMetaDelLadoRight(mapa,28,cajaTres);
                }
                //Personaje saca de la Meta la 'Caja 1' --- Se reconoce el numero 9 y por esto se vuelve al valor inicial de la caja
                else if(valorUno == 14 && valorDos == 0){
                        moverCajaFueraDeMetaRight(mapa,21,cajaTres);
                }
    }
    
    private void moverCajaNormalRight(Mapa mapa, int id, ImageView caja){
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)+1);// mueve la caja
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)+1);// mueve a Mario
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"RIGHT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaRight(Mapa mapa, int id, ImageView caja){
        caja.setImage(win);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)+1);
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)+1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"RIGHT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
       movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaEnMetaDelLadoRight(Mapa mapa, int id, ImageView caja){
        //mgC1.setImage(win);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)+1);
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)+1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"RIGHT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }
    
    private void moverCajaFueraDeMetaRight(Mapa mapa, int id, ImageView caja){
        caja.setImage(bloque);
        GridPane.setColumnIndex(caja,GridPane.getColumnIndex(caja)+1);
        GridPane.setColumnIndex(Personaje,GridPane.getColumnIndex(Personaje)+1);
        haMovido =mapa.actualizar(GridPane.getRowIndex(caja),GridPane.getColumnIndex(caja), id,"RIGHT");//SE ACTUALIZAN LAS UBICACIONES DE LAS CAJAS
        movimientosCajas++;
        this.txtMovimientos.setText(String.valueOf(movimientosCajas));
    }

    @FXML
    private void lvl1OnAction(ActionEvent event) {
        anchorPane.getChildren().remove(grid);
        anchorPane.getChildren().remove(ganador);  
        numeroDeMapa = 1;
        cargarMapa(nivelUno, 1, texturaUno); 
    }
    
    @FXML
    private void lvl2OnAction(ActionEvent event) {
        if(lvlCompletado[0]==1){
        cajaUno.setImage(bloque);
        cajaDos.setImage(bloque);
        cajaTres.setImage(bloque);
        this.txtMovimientos.setText("0");
        this.txtMovimientosLibres.setText("0");
        movimientosCajas=0;
        movimientosPersonaje=0;
        anchorPane.getChildren().remove(grid);
        anchorPane.getChildren().remove(ganador);  
         cargarMapa(nivelDos, 2, texturaDos);
        }else{
            msg.show(Alert.AlertType.CONFIRMATION, "Jugar Nivel 2", "Debes haber completado el nivel '1' para jugar este nivel.");
        }
    }

    @FXML
    private void lvl3OnAction(ActionEvent event) {
        if(lvlCompletado[1]==1){
        cajaUno.setImage(bloque);
        cajaDos.setImage(bloque);
        cajaTres.setImage(bloque);
        this.txtMovimientos.setText("0");
        this.txtMovimientosLibres.setText("0");
        movimientosCajas=0;
        movimientosPersonaje=0;
        anchorPane.getChildren().remove(grid);
        anchorPane.getChildren().remove(ganador);  
         cargarMapa(nivelTres, 3, texturaTres);
        }else{
            msg.show(Alert.AlertType.CONFIRMATION, "Jugar Nivel 3", "Debes haber completado el nivel '2' para jugar este nivel.");
        }
    }

    @FXML
    private void lvl4OnAction(ActionEvent event) {
    if(lvlCompletado[2]==1){
        cajaUno.setImage(bloque);
        cajaDos.setImage(bloque);
        cajaTres.setImage(bloque);
        this.txtMovimientos.setText("0");
        this.txtMovimientosLibres.setText("0");
        movimientosCajas=0;
        movimientosPersonaje=0;
        anchorPane.getChildren().remove(grid);
        anchorPane.getChildren().remove(ganador);  
         cargarMapa(nivelCuatro, 4, texturaCuatro);
        }else{
            msg.show(Alert.AlertType.CONFIRMATION, "Jugar Nivel 4", "Debes haber completado el nivel '3' para jugar este nivel.");
        }
    }

    @FXML
    private void lvl5OnAction(ActionEvent event) {
        if(lvlCompletado[3]==1){
            cajaUno.setImage(bloque);
            cajaDos.setImage(bloque);
            cajaTres.setImage(bloque);
            this.txtMovimientos.setText("0");
            this.txtMovimientosLibres.setText("0");
            movimientosCajas=0;
            movimientosPersonaje=0;
            anchorPane.getChildren().remove(grid);
        anchorPane.getChildren().remove(ganador);  
         cargarMapa(nivelCinco, 5, texturaCinco);
        }else{
            msg.show(Alert.AlertType.CONFIRMATION, "Jugar Nivel 5", "Debes haber completado el nivel '4' para jugar este nivel.");
        }
    }
    
    private void audioGameOver(){
        sonido = new AudioClip(this.getClass().getResource("/sokoban_henry/resource/songGameOver.mp3").toString());
        sonido.play();
    }
    private void audioMeta(){
        sonido = new AudioClip(this.getClass().getResource("/sokoban_henry/resource/fuego.mp3").toString());
        sonido.play();
    }

    @FXML 
    private void reiniciarOnAction(ActionEvent event) {
        //Limpia el vector de niveles superados.
        for(int i=0;i<4;i++){
            lvlCompletado[i]=0;
        }
        cajaUno.setImage(bloque);
        cajaDos.setImage(bloque);
        cajaTres.setImage(bloque);
        lvlCompletadoIndice=0;
        anchorPane.getChildren().remove(grid);
        //Carga mapa Nivel 1
        cargarMapa(nivelUno, 1, texturaUno);
    }
    
    private void cargarNiveles(){
        nivelUno = new Integer[][]{{0,0,0,1,1,1,1,0,0,0,0,0},
                                    {0,0,0,1,2,0,1,0,0,0,0,0},
                                    {1,1,1,1,0,0,1,0,0,0,0,0},
                                    {1,0,0,0,5,0,1,1,1,1,1,1},
                                    {1,1,1,3,0,7,0,0,0,0,2,1},   
                                    {0,0,1,0,0,1,1,1,1,1,1,1},
                                    {0,0,1,2,0,1,0,0,0,0,0,0},
                                    {0,0,1,1,1,1,0,0,0,0,0,0}};
        nivelDos = new Integer[][]{{1,1,1,1,1,1,1,1,1,0,1,1},
                                    {1,0,0,0,0,0,0,0,0,1,2,1},
                                    {1,0,5,7,1,0,0,0,0,1,2,1},
                                    {1,0,3,0,1,1,1,1,1,1,2,1},
                                    {1,1,1,0,0,0,0,0,0,0,0,1},
                                    {0,1,0,0,0,0,0,0,1,0,0,1},
                                    {0,1,0,0,0,0,0,0,1,1,1,1},
                                    {0,1,1,1,1,1,1,1,1,0,0,0}};
        nivelTres = new Integer[][]{{0,1,1,1,1,1,1,1,1,0,0,0},
                               {0,1,0,0,0,0,0,0,0,1,0,0},
                               {1,1,3,1,1,1,0,0,0,0,1,0},
                               {1,0,0,0,5,0,0,7,0,0,0,1},
                               {1,0,2,2,1,0,0,0,0,0,1,0},
                               {1,0,2,0,1,0,0,0,0,1,0,0},
                               {1,1,1,1,1,0,0,0,1,0,0,0},
                               {0,0,0,0,0,1,1,1,1,0,0,0} };
        nivelCuatro = new Integer[][]{{1,1,1,1,1,1,1,1,1,1,1,1},
                               {1,1,0,0,0,0,0,0,0,0,0,1},
                               {1,0,3,0,1,2,0,0,1,0,0,1},
                               {1,1,5,0,1,1,0,0,1,1,0,1},
                               {1,1,0,7,0,1,0,0,2,1,0,1},
                               {1,2,0,0,0,0,0,0,0,0,0,1},
                               {1,0,0,0,0,1,0,0,0,0,0,1},
                               {1,1,1,1,1,1,1,1,1,1,1,1}};
        nivelCinco = new Integer[][]{{0,1,1,1,0,0,0,0,0,0,0,0},
                                    {0,1,0,0,1,1,1,1,1,1,1,0},
                                    {0,1,0,5,0,0,0,0,0,0,1,0},
                                    {1,1,1,0,1,0,0,1,0,0,1,0},
                                    {1,2,1,0,1,0,0,1,0,0,1,0},
                                    {1,2,3,0,0,1,0,0,1,0,1,0},
                                    {1,2,0,0,0,7,0,0,1,0,1,0},
                                    {1,1,1,1,1,1,1,1,1,1,1,0}};
    }

    @FXML
    private void guardarOnAction(ActionEvent event) {
        //Se debe guardar la 'posiciónDePersonaje' y 'posiciónDeLasCajas'
        int xPj = GridPane.getRowIndex(Personaje);
        int yPj = GridPane.getColumnIndex(Personaje);
        int xCajaUno = GridPane.getRowIndex(cajaUno);
        int yCajaUno = GridPane.getColumnIndex(cajaUno);
        int xCajaDos = GridPane.getRowIndex(cajaDos);
        int yCajaDos = GridPane.getColumnIndex(cajaDos);
        int xCajaTres = GridPane.getRowIndex(cajaTres);
        int yCajaTres = GridPane.getColumnIndex(cajaTres);
        int nivel=0;
        for(int i=0;i<4;i++){
            if(this.lvlCompletado[i] == 0){
                 nivel = i-1;
            }
        }
        String nombre = this.txtUsuario.getText();
        System.out.println("Nivel Guardado:" + nivel);
        try {
                JuegoService service = (JuegoService) AppContext.getInstance().get("JuegoService");
                Respuesta respuesta = service.guardarJuego(juego);//   <--- Guardar Juego---
                if (!respuesta.getEstado()) {
                    new Mensaje().show(Alert.AlertType.CONFIRMATION, "Guardar juego", respuesta.getMensaje());
                } else {
                    unbindJuego();
                    juego = (JuegoDto) respuesta.getResultado("Juego");
                    bindJuego(false);
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Guardar juego", "Juego actualizado correctamente.");
                }
            } catch (Exception ex) {
            Logger.getLogger(juegoController.class.getName()).log(Level.SEVERE, "Error guardando el juego.", ex);
            new Mensaje().show(Alert.AlertType.ERROR, "Guardar juego", "Ocurrio un error guardando el juego.");
        }
    }
    
    public void setJuego(JuegoDto juego){
        juegoParaCargar = true;
        unbindJuego();
        this.juego = juego;
        bindJuego(false);
    }
    
    private void nuevoJuego() {
        unbindJuego();
        juego = new JuegoDto();
        bindJuego(true);
        }
    
    private void bindJuego(boolean nuevo){
        //txtNombre.textProperty().bindBidirectional(empleado.empNombre);
        
    }
    
    private void unbindJuego(){
        //txtCedula.textProperty().unbindBidirectional(empleado.empCedula);
        
    }
    
    private Integer[][] evaluaBaseDeDatos(Integer matrixMapa[][]){
        if(juegoParaCargar){
            colocarPersonaje(juego.getXpersonaje(),juego.getYpersonaje());
            for(int i=0;i<8;i++){
                for(int j=0;j<12;j++){
                    //----CAJA UNO----
                    if(matrixMapa[i][j] == 3){
                        matrixMapa[i][j] = 0;
                    }
                    if(i==juego.getXcajaUno() && j==juego.getYcajaUno()){
                        matrixMapa[i][j] = 3;
                    }
                    //----CAJA DOS----
                    if(matrixMapa[i][j] == 5){
                        matrixMapa[i][j] = 0;
                    }
                    if(i==juego.getXcajaDos() && j==juego.getYcajaDos()){
                        matrixMapa[i][j] = 5;
                    }
                    //----CAJA TRES----
                    if(matrixMapa[i][j] == 7){
                        matrixMapa[i][j] = 0;
                    }
                    if(i==juego.getXcajaUno() && j==juego.getYcajaUno()){
                        matrixMapa[i][j] = 7;
                    }
                }    
            }
            this.txtUsuario.setText(juego.getNombre());
            return matrixMapa;
        }else{
            return matrixMapa;
        }
    }

    @FXML
    private void btnHasGanadoOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sokoban_henry/portada.fxml"));
            Parent root = loader.load();
            PortadaController  portadaController = loader.getController();
            
            Scene scene = btnHasGanado.getScene();

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
            Logger.getLogger(juegoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
