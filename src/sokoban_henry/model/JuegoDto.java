/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.model;

import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import static oracle.jrockit.jfr.events.Bits.intValue;

/**
 *
 * @author Henry
 */
@XmlRootElement(name = "JuegoDto")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class JuegoDto {
    @XmlTransient
    public int Id;
    @XmlTransient
    public int xPosPersonaje;
    @XmlTransient
    public int yPosPersonaje;
    @XmlTransient
    public int xPosCajaUno;
    @XmlTransient
    public int yPosCajaUno;
    @XmlTransient
    public int xPosCajaDos;
    @XmlTransient
    public int yPosCajaDos;
    @XmlTransient
    public int xPosCajaTres;
    @XmlTransient
    public int yPosCajaTres;
    @XmlTransient
    public int nivelAlcanzado;
    @XmlTransient
    public String nombre;
    
    public JuegoDto(){

    }
    
    public JuegoDto(Juego juego){
        this.Id = intValue(juego.getId());
        this.xPosPersonaje=intValue(juego.getXPosPersonaje());
        this.yPosPersonaje=intValue(juego.getYPosPersonaje());
        this.xPosCajaUno=intValue(juego.getXPosCajaUno());
        this.yPosCajaUno=intValue(juego.getYPosCajaUno());
        this.xPosCajaDos=intValue(juego.getXPosCajaDos());
        this.yPosCajaDos=intValue(juego.getYPosCajaDos());
        this.xPosCajaTres=intValue(juego.getXPosCajaTres());
        this.yPosCajaTres=intValue(juego.getYPosCajaTres());
        this.nivelAlcanzado=intValue(juego.getNivelAlcanzado());         
    }

    public Long getId() {
        //if(Id != null)
            return Long.valueOf(Id);
        //else
          //  return null;
    }

    public void setId(Long Id) {
        this.Id = intValue(Id);
    }
    
    public void setNombre(String name){
        this.nombre = name;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setNivel(int lvl){
        this.nivelAlcanzado = lvl;
    }
    public int getNivel(){
        return this.nivelAlcanzado;
    }
    
    //----sets y gets de 'PERSONAJE'----
    public int getXpersonaje(){
        return this.xPosPersonaje;
    }
    public void setXpersonaje(int n){
        this.xPosPersonaje = n;
    } 
    public int getYpersonaje(){
        return this.yPosPersonaje;
    }
    public void setYpersonaje(int n){
        this.yPosPersonaje = n;
    }
    //----sets y gets de 'CAJA_UNO'----
    public int getXcajaUno(){
        return this.xPosCajaUno;
    }
    public void setXcajaUno(int n){
        this.xPosCajaUno = n;
    } 
    public int getYcajaUno(){
        return this.yPosCajaUno;
    }
    public void setYcajaUno(int n){
        this.yPosCajaUno = n;
    } 
    //----sets y gets de 'CAJA_DOS'----
    public int getXcajaDos(){
        return this.xPosCajaDos;
    }
    public void setXcajaDos(int n){
        this.xPosCajaDos = n;
    } 
    public int getYcajaDos(){
        return this.yPosCajaDos;
    }
    public void setYcajaDos(int n){
        this.yPosCajaDos = n;
    } 
    //----sets y gets de 'CAJA_TRES'----
    public int getXcajaTres(){
        return this.xPosCajaTres;
    }
    public void setXcajaTres(int n){
        this.xPosCajaTres = n;
    } 
    public int getYcajaTres(){
        return this.yPosCajaTres;
    }
    public void setYcajaTres(int n){
        this.yPosCajaTres = n;
    } 
}
