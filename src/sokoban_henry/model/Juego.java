/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Henry
 */
@Entity
@Table(name = "Juego", catalog = "", schema = "HENRY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Juego.findAll", query = "SELECT j FROM Juego j"),
    @NamedQuery(name = "Juego.findById", query = "SELECT j FROM Juego j WHERE j.id = :id"),
    @NamedQuery(name = "Juego.findByXPosPersonaje", query = "SELECT j FROM Juego j WHERE j.xPosPersonaje = :xPosPersonaje"),
    @NamedQuery(name = "Juego.findByYPosPersonaje", query = "SELECT j FROM Juego j WHERE j.yPosPersonaje = :yPosPersonaje"),
    @NamedQuery(name = "Juego.findByXPosCajaUno", query = "SELECT j FROM Juego j WHERE j.xPosCajaUno = :xPosCajaUno"),
    @NamedQuery(name = "Juego.findByYPosCajaUno", query = "SELECT j FROM Juego j WHERE j.yPosCajaUno = :yPosCajaUno"),
    @NamedQuery(name = "Juego.findByXPosCajaDos", query = "SELECT j FROM Juego j WHERE j.xPosCajaDos = :xPosCajaDos"),
    @NamedQuery(name = "Juego.findByYPosCajaDos", query = "SELECT j FROM Juego j WHERE j.yPosCajaDos = :yPosCajaDos"),
    @NamedQuery(name = "Juego.findByXPosCajaTres", query = "SELECT j FROM Juego j WHERE j.xPosCajaTres = :xPosCajaTres"),
    @NamedQuery(name = "Juego.findByYPosCajaTres", query = "SELECT j FROM Juego j WHERE j.yPosCajaTres = :yPosCajaTres"),
    @NamedQuery(name = "Juego.findByNombre", query = "SELECT j FROM Juego j WHERE j.nombre = :nombre"),
    @NamedQuery(name = "Juego.findByNivelAlcanzado", query = "SELECT j FROM Juego j WHERE j.nivelAlcanzado = :nivelAlcanzado")})
public class Juego implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "Id", nullable = false, precision = 0, scale = -127)
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "xPosPersonaje", nullable = false)
    private BigInteger xPosPersonaje;
    @Basic(optional = false)
    @Column(name = "yPosPersonaje", nullable = false)
    private BigInteger yPosPersonaje;
    @Basic(optional = false)
    @Column(name = "xPosCajaUno", nullable = false)
    private BigInteger xPosCajaUno;
    @Basic(optional = false)
    @Column(name = "yPosCajaUno", nullable = false)
    private BigInteger yPosCajaUno;
    @Basic(optional = false)
    @Column(name = "xPosCajaDos", nullable = false)
    private BigInteger xPosCajaDos;
    @Basic(optional = false)
    @Column(name = "yPosCajaDos", nullable = false)
    private BigInteger yPosCajaDos;
    @Basic(optional = false)
    @Column(name = "xPosCajaTres", nullable = false)
    private BigInteger xPosCajaTres;
    @Basic(optional = false)
    @Column(name = "yPosCajaTres", nullable = false)
    private BigInteger yPosCajaTres;
    @Basic(optional = false)
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;
    @Basic(optional = false)
    @Column(name = "nivelAlcanzado", nullable = false)
    private BigInteger nivelAlcanzado;

    public Juego() {
    }

    public Juego(BigDecimal id) {
        this.id = id;
    }

    public Juego(BigDecimal id, BigInteger xPosPersonaje, BigInteger yPosPersonaje, BigInteger xPosCajaUno, BigInteger yPosCajaUno, BigInteger xPosCajaDos, BigInteger yPosCajaDos, BigInteger xPosCajaTres, BigInteger yPosCajaTres, String nombre, BigInteger nivelAlcanzado) {
        this.id = id;
        this.xPosPersonaje = xPosPersonaje;
        this.yPosPersonaje = yPosPersonaje;
        this.xPosCajaUno = xPosCajaUno;
        this.yPosCajaUno = yPosCajaUno;
        this.xPosCajaDos = xPosCajaDos;
        this.yPosCajaDos = yPosCajaDos;
        this.xPosCajaTres = xPosCajaTres;
        this.yPosCajaTres = yPosCajaTres;
        this.nombre = nombre;
        this.nivelAlcanzado = nivelAlcanzado;
    }
    
    public void actualizarJuego(JuegoDto juego){
        //this.id = BigDecimal.valueOf(juego.getId());
        this.xPosPersonaje = BigInteger.valueOf(juego.getXpersonaje());
        this.yPosPersonaje = BigInteger.valueOf(juego.getYpersonaje());
        this.xPosCajaUno = BigInteger.valueOf(juego.getXcajaUno());
        this.yPosCajaUno = BigInteger.valueOf(juego.getYcajaUno());
        this.xPosCajaDos = BigInteger.valueOf(juego.getXcajaDos());
        this.yPosCajaDos = BigInteger.valueOf(juego.getYcajaDos());
        this.xPosCajaTres = BigInteger.valueOf(juego.getXcajaTres());
        this.yPosCajaTres = BigInteger.valueOf(juego.getYcajaTres());
        this.nombre = juego.getNombre();
        this.nivelAlcanzado = BigInteger.valueOf(juego.getNivel());

    }
    
    public Juego(JuegoDto juego) {
        //this.id = BigDecimal.valueOf(juego.getId());
        actualizarJuego(juego);
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getXPosPersonaje() {
        return xPosPersonaje;
    }

    public void setXPosPersonaje(BigInteger xPosPersonaje) {
        this.xPosPersonaje = xPosPersonaje;
    }

    public BigInteger getYPosPersonaje() {
        return yPosPersonaje;
    }

    public void setYPosPersonaje(BigInteger yPosPersonaje) {
        this.yPosPersonaje = yPosPersonaje;
    }

    public BigInteger getXPosCajaUno() {
        return xPosCajaUno;
    }

    public void setXPosCajaUno(BigInteger xPosCajaUno) {
        this.xPosCajaUno = xPosCajaUno;
    }

    public BigInteger getYPosCajaUno() {
        return yPosCajaUno;
    }

    public void setYPosCajaUno(BigInteger yPosCajaUno) {
        this.yPosCajaUno = yPosCajaUno;
    }

    public BigInteger getXPosCajaDos() {
        return xPosCajaDos;
    }

    public void setXPosCajaDos(BigInteger xPosCajaDos) {
        this.xPosCajaDos = xPosCajaDos;
    }

    public BigInteger getYPosCajaDos() {
        return yPosCajaDos;
    }

    public void setYPosCajaDos(BigInteger yPosCajaDos) {
        this.yPosCajaDos = yPosCajaDos;
    }

    public BigInteger getXPosCajaTres() {
        return xPosCajaTres;
    }

    public void setXPosCajaTres(BigInteger xPosCajaTres) {
        this.xPosCajaTres = xPosCajaTres;
    }

    public BigInteger getYPosCajaTres() {
        return yPosCajaTres;
    }

    public void setYPosCajaTres(BigInteger yPosCajaTres) {
        this.yPosCajaTres = yPosCajaTres;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public void setNivelAlcanzado(BigInteger nivelAlcanzado) {
        this.nivelAlcanzado = nivelAlcanzado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Juego)) {
            return false;
        }
        Juego other = (Juego) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sokoban_henry.model.Juego[ id=" + id + " ]";
    }
    
}
