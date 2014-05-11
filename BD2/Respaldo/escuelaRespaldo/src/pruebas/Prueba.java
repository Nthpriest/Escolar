/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import objetosNegocio.Adeudo;
import objetosNegocio.Alumno;
import objetosNegocio.Carrera;
import objetosNegocio.Catalogo;
import objetosNegocio.Grupo;
import objetosNegocio.Maestro;
import objetosNegocio.Materia;
import objetosNegocio.Pago;

import persistencia.AlumnoJpaController;
import persistencia.CarreraJpaController;
import persistencia.Persistencia;

/**
 *
 * @author IVI
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here

        Persistencia p = new Persistencia();

//        Alumno alumno_1 = new Alumno(1, "PEPE");
//        p.agregar(alumno_1);
////         editamos al alumno pepe
//        Alumno almx=p.obtenAlumno(1);
//        System.out.println(almx.toString());
        

         //elimina Alumno   
         //p.eliminaAluno(1);
        
        
        // actualiza alumno

//        Carrera mat = new Carrera(50,"ISW");
////        p.agregaCarrera(mat);
//        
//        Alumno almx = p.obtenAlumno(1);
//        almx.setIdCarreraFK(mat);
//        almx.setNombre("IRVING");
//        p.actualizaAlumno(almx);
        
        
        //Agrega Carrera
//        Carrera car=new Carrera(2, "Admin");
//        p.agregaCarrera(car);
        
        // consulta Carrera
//        System.out.println(p.obtenCarrera(1).getNombre());
        
        //Actualia carrera
//       Carrera car=p.obtenCarrera(2);
//       car.setNombre("Diseño");
//       p.actualizaCarrera(car);
       
       //elimina Carrera
//        p.eliminaCarrera(2);
        
        
        //Jugamos con la  entidad Alumno, que tiene listas lista dentro de esta tabla
        // parte de consultar
//        Alumno alumno=new Alumno(1, "Rulo");
//        Pago pagos=new Pago(1,100,new Date());
//        Adeudo adeudo=new Adeudo(1,2000, new Date(), "Atraso", new Date());
//        
//        Collection<Adeudo> colAd= new ArrayList<>();
//        Collection<Pago> colPa= new ArrayList<>();
//        
//  
//        colPa.add(pagos);
//        colAd.add(adeudo);
//        alumno.setAdeudoCollection(colAd);
//        alumno.setPagoCollection(colPa);
//        p.agregarAlumno(alumno);
        
        
        // actualizamos un alumno
//        Alumno alumno=p.obtenAlumno(1);
//        Carrera carrera=p.obtenCarrera(2);
//        System.out.println(carrera.getNombre());
//       
//        alumno.setIdCarreraFK(null);
//        p.actualizaAlumno(alumno);
//   
        
         //----------------------------------------
        //              GRUPO
        // AGREGAR UN GRUPO PASOS Grupos
//         Alumno alumno=p.obtenAlumno(1);
//         Catalogo catalog=new Catalogo(7, new Date(), "lv200", "enero-mayo");
//         p.agregaCatalogo(catalog);
//         
//         
//         Grupo grupo=new Grupo(3);
//         grupo.setIdAlumnoFK(alumno);
//         grupo.setIdCatalogoFK(catalog);
//         
//         p.agregaGrupo(grupo);
        
        //----------------------------------------
        //              GRUPO
        // MODIFICAR UN GRUPO
//        Grupo grupo=p.obtenGrupo(3);
//        Alumno alumno=p.obtenAlumno(2);
//      
//        grupo.setIdAlumnoFK(alumno);
//        grupo.setIdCatalogoFK(null);
//       
//        p.actualizaGrupo(grupo);
        
        // ---------------------------------------------
        //            MAestros
        // jugamos con la tabla Maestros
//       Maestro maestro=new Maestro(1, "masias", "Desarrollo");
//       
//       p.agregaMaestro(maestro);
        
    }

}
