/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban_henry.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import sokoban_henry.model.Juego;
import sokoban_henry.model.JuegoDto;
import sokoban_henry.util.EntityManagerHelper;
import sokoban_henry.util.Respuesta;

/**
 *
 * @author Henry
 */
public class JuegoService {
    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;
   
    public Respuesta getJuego(Long id) {
        try {
            Query qryJuego = em.createNamedQuery("Juego.findById", Juego.class);
            qryJuego.setParameter("Id", id);

            return new Respuesta(true, "", "", "Juego", new JuegoDto((Juego) qryJuego.getSingleResult()));

        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un juego con el código ingresado.", "getEmpleado NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(JuegoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el juego.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el juego.", "getEmpleado NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(JuegoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el juego.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el juego.", "getEmpleado " + ex.getMessage());
        }
    }
    
    public Respuesta guardarJuego(JuegoDto juegoDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Juego juego;
        
                juego = new Juego(juegoDto);
                em.persist(juego);
         
            et.commit();
            return new Respuesta(true, "", "", "Juego", new JuegoDto(juego));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(JuegoService.class.getName()).log(Level.SEVERE, "Ocurrio un error al guardar el juego.", ex);
            return new Respuesta(false, "Ocurrio un error al guardar el juego.", "guardarJuego " + ex.getMessage());
        }
    }
}
