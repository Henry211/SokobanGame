/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.clases;

/**
 *
 * @author Henry
 */
public class Mapa {

    private Integer mapa1[][]=new Integer[8][12];
    
    private Integer mapa2[][]=new Integer[8][12];
    
    
    public Mapa(){//Inicializo
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                 mapa2[i][j]=0;
            }
        }
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                 mapa1[i][j]=0;
            }
        }
    }
    
    public void setValor(int elemento,int fila, int columna){
        this.mapa1[fila][columna] = elemento;
    }
    
    public int getValor(int fila, int columna){
        return this.mapa1[fila][columna];
    }
    
    public void cargarElemento(int fila,int columna,int elemento){//Recibo el mapa segun boton
      for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                if(i==fila && j==columna){
                    mapa1[i][j]=elemento; 
                }
            }
        }
    }
    
    public int getElemento(int fila, int columna){
        int valor=0;
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                 if(i==fila && j==columna){
//                     if(i-1==4){
                     valor=mapa1[i][j]; 
                 }
            }
        }
        return valor;
    }
    
    public int getPuntaje(){
        int cajasEnMeta=0;
       for(int i=0;i<8;i++){
           for(int j =0;j<12;j++){
               if(mapa1[i][j]==6){
                   cajasEnMeta++;
               }
               if(mapa1[i][j]==10){
                   cajasEnMeta++;
               }
               if(mapa1[i][j]==14){
                   cajasEnMeta++;
               }
           }
       }
       return cajasEnMeta;
    }
    
    public void limpiar(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                 mapa1[i][j]=0;
            }
        }
    }
    
    public Integer[][] getPatrizPrincipal(){
        return this.mapa1;
    }
    
    public Integer[][] getMatrizAnterior(){
        return this.mapa2;
    }
    
    private void guardarMovimientoAnterior(){
        for(int i=0;i<8;i++){
            for(int j=0;j<12;j++){
                 mapa2[i][j]=mapa1[i][j];
            }
        }   
    }
    
    public int actualizar(int fila, int columna,int caja,String direccion){//Metodo para actualizar las posociones de las cajas en la matriz
        
        guardarMovimientoAnterior();    
        //-------------------------
        switch (direccion){
            case "LEFT":
                    //MOVER LA CAJA NORMAL (3,5,7)
                    //MOVER A LA META (6,10,14)
                    //DEJA RASTRO VACÍO (0)
                    if(caja==3||caja==5||caja==7||caja==6||caja==10||caja==14){
                        mapa1[fila][columna]=caja;
                        mapa1[fila][columna+1]=0;
                    }
                    //'CAJA-1' cuando hay otra meta a su izquierda, se puede mover
                    else if(caja==12){
                        mapa1[fila][columna]=6;     //se mantiene la llama
                        mapa1[fila][columna+1]=2;   //se mantiene la estrella en el anterior
                    }
                    //'CAJA-2' cuando hay otra meta a su izquierda, se puede mover
                    else if(caja==20){
                        mapa1[fila][columna]=10;    //se mantiene la llama
                        mapa1[fila][columna+1]=2;   //se mantiene la estrella en el anterior
                    }
                    //'CAJA-3' cuando hay otra meta a su izquierda, se puede mover
                    else if(caja==28){
                        mapa1[fila][columna]=14;    //se mantiene la llama
                        mapa1[fila][columna+1]=2;   //se mantiene la estrella en el anterior
                    }
                    //'CAJA-1' sacarla de la meta
                    else if(caja==9){
                        mapa1[fila][columna]=3;    
                        mapa1[fila][columna+1]=2;   
                    }
                    //'CAJA-2' sacarla de la meta
                    else if(caja==15){
                        mapa1[fila][columna]=5;    
                        mapa1[fila][columna+1]=2;   
                    }
                    //'CAJA-3' sacarla de la meta
                    else if(caja==21){
                        mapa1[fila][columna]=7;     
                        mapa1[fila][columna+1]=2;   
                    }
                    break;
            case "UP":
                    //MOVER LA CAJA NORMAL (3,5,7)
                    //MOVER A LA META (6,10,14)
                    //SIEMRPE DEJA RASTRO VACÍO (0)
                    if(caja==3||caja==5||caja==7||caja==6||caja==10||caja==14){
                        mapa1[fila][columna]=caja;
                        mapa1[fila+1][columna]=0;
                    }
                    //'CAJA-1' cuando hay otra meta arriba, se puede mover
                    else if(caja==12){
                        mapa1[fila][columna]=6;     
                        mapa1[fila+1][columna]=2;   
                    }
                    //'CAJA-2' cuando hay otra meta arriba, se puede mover
                    else if(caja==20){
                        mapa1[fila][columna]=10;    
                        mapa1[fila+1][columna]=2;   
                    }
                    //'CAJA-3' cuando hay otra meta arriba, se puede mover
                    else if(caja==28){
                        mapa1[fila][columna]=14;    
                        mapa1[fila+1][columna]=2; 
                    }
                    //'CAJA-1' sacarla de la meta
                    else if(caja==9){
                        mapa1[fila][columna]=3;
                        mapa1[fila+1][columna]=2;
                    }
                    //'CAJA-2' sacarla de la meta
                    else if(caja==15){
                        mapa1[fila][columna]=5;
                        mapa1[fila+1][columna]=2;
                    }
                    //'CAJA-3' sacarla de la meta
                    else if(caja==21){
                        mapa1[fila][columna]=7;
                        mapa1[fila+1][columna]=2;
                    }
                    break;
            case "RIGHT":
                    //MOVER LA CAJA NORMAL (3,5,7)
                    //MOVER A LA META (6,10,14)
                    //SIEMRPE DEJA RASTRO VACÍO (0)
                    if(caja==3||caja==5||caja==7||caja==6||caja==10||caja==14){//es 'caja' o es 'llama'
                        mapa1[fila][columna]=caja;
                        mapa1[fila][columna-1]=0;
                    }
                    //'CAJA-1' cuando hay otra meta a la derecha
                    else if(caja==12){
                        mapa1[fila][columna]=6;
                        mapa1[fila][columna-1]=2;
                    }
                    //'CAJA-2' cuando hay otra meta a la derecha
                    else if(caja==20){
                        mapa1[fila][columna]=10;
                        mapa1[fila][columna-1]=2;
                    }
                    //'CAJA-3' cuando hay otra meta a la derecha
                    else if(caja==28){
                        mapa1[fila][columna]=14;
                        mapa1[fila][columna-1]=2;
                    }
                    //'CAJA-1' sacarla de la meta
                    else if(caja==9){
                        mapa1[fila][columna]=3;
                        mapa1[fila][columna-1]=2;
                    }
                    //'CAJA-2' sacarla de la meta
                    else if(caja==15){
                        mapa1[fila][columna]=5;
                        mapa1[fila][columna-1]=2;
                    }
                    //'CAJA-3' sacarla de la meta
                    else if(caja==21){
                        mapa1[fila][columna]=7;
                        mapa1[fila][columna-1]=2;
                    }
                    break;
            case "DOWN":
                    //MOVER LA CAJA NORMAL (3,5,7)
                    //MOVER A LA META (6,10,14)
                    //SIEMRPE DEJA RASTRO VACÍO (0)
                    if(caja==3||caja==5||caja==7||caja==6||caja==10||caja==14){
                        mapa1[fila][columna]=caja;
                        mapa1[fila-1][columna]=0;
                    }
                    //'CAJA-1' cuando hay otra meta abajo
                    else if(caja==12){
                        mapa1[fila][columna]=6;
                        mapa1[fila-1][columna]=2;
                    }
                    //'CAJA-2' cuando hay otra meta abajo
                    else if(caja==20){
                        mapa1[fila][columna]=10;
                        mapa1[fila-1][columna]=2;
                    }
                    //'CAJA-3' cuando hay otra meta abajo
                    else if(caja==28){
                        mapa1[fila][columna]=14;
                        mapa1[fila-1][columna]=2;
                    }
                    //'CAJA-1' sacarla de la meta
                    else if(caja==9){
                        mapa1[fila][columna]=3;
                        mapa1[fila-1][columna]=2;
                    }
                    //'CAJA-2' sacarla de la meta
                    else if(caja==15){
                        mapa1[fila][columna]=5;
                        mapa1[fila-1][columna]=2;
                    }
                    //'CAJA-3' sacarla de la meta
                    else if(caja==21){
                        mapa1[fila][columna]=7;
                        mapa1[fila-1][columna]=2;
                    }
                    break;
        }
        return 1;
        
    }
    
}
