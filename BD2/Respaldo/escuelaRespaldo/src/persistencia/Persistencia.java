/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import objetosNegocio.Adeudo;
import objetosNegocio.Alumno;
import objetosNegocio.Carrera;
import objetosNegocio.Catalogo;
import objetosNegocio.Grupo;
import objetosNegocio.Maestro;
import objetosNegocio.Materia;
import objetosNegocio.Pago;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Zenujow
 */
public class Persistencia {
    AlumnoJpaController controlAlumno;
    CarreraJpaController controlCarrera;
    AdeudoJpaController controlAdeudo;
    PagoJpaController   controlPago;
    GrupoJpaController controlGrupo;
    MateriaJpaController controlMateria;
    MaestroJpaController controlMaestro;
    CatalogoJpaController controlCatalogo;
    
    public Persistencia(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("escuelaPU");
        controlAlumno = new AlumnoJpaController(factory);
        controlCarrera= new CarreraJpaController(factory);
        controlAdeudo=new AdeudoJpaController(factory);
        controlPago=new PagoJpaController(factory);
        controlGrupo=new GrupoJpaController(factory);
        controlMateria=new MateriaJpaController(factory);
        controlMaestro= new MaestroJpaController(factory);
        controlCatalogo=new CatalogoJpaController(factory);
    }
    
    //--------------------------------------------------------------------------
    // INICIO de laseccion de metodos que controla las funciones de agregar de la tabla alumno
    /**
     * agrega un alumno alal tabla alumno
     * @param alumno
     * @throws Exception 
     */
    public void agregarAlumno(Alumno alumno) throws Exception{
        try{
        controlAlumno.create(alumno);
        }catch(Exception e){
            System.out.println("Posible REPETIDO....."+e.getMessage());
        }
    }
    /**
     * obtiene un alumno de la tabla alumno que coincida con el id
     * @param id
     * @return 
     */
    public Alumno obtenAlumno(int id){
        return controlAlumno.findAlumno(id);
    }
    
    /**
     * Elimina  aun alumno de la tabla alumno
     * @param id
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException 
     */
    public void eliminaAluno(int id) throws IllegalOrphanException, NonexistentEntityException{
        controlAlumno.destroy(id);
    }
    
    /**
     * Metodo que actualiza la tabla alumno
     * @param alm
     * @throws NonexistentEntityException
     * @throws Exception 
     */
    public void actualizaAlumno(Alumno alm) throws NonexistentEntityException, Exception{
    
         EntityManager em = null;
        try {
            em = controlAlumno.getEntityManager();
            em.getTransaction().begin();
            alm = em.merge(alm);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = alm.getIdAlumnos();
                if (controlAlumno.findAlumno(id) == null) {
                    throw new NonexistentEntityException("El alumno con el id:  " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
         
        
        
        
//        // confirmamaos si existe el alumno
//        Alumno aux=controlAlumno.findAlumno(alm.getIdAlumnos());
//        if(aux==null){
//            throw new Exception("Alumno no existe");
//        }
//        controlAlumno.edit(aux);
    }
     // FIN de laseccion de metodos que controla las funciones de agregar de la tabla alumno
    //--------------------------------------------------------------------------
  
    
    //-------------------------------------------------------------------------
    //iNICIO Secion de control de la tabla Carrera
    /**
     * metodo que agrega una carrera en la tabla carrera
     * @param carrera
     * @throws Exception 
     */
    public void agregaCarrera(Carrera carrera) throws Exception{
        controlCarrera.create(carrera);
    }
    
    /**
     * metodo que obtiene una carrera de la tabla carrera
     * @param id
     * @return 
     */
    public Carrera obtenCarrera(int id){
      return   controlCarrera.findCarrera(id);   
    }
    
    public List<Carrera> obtenListaCarrera(){
        return this.controlCarrera.findCarreraEntities();
    }
    
    
    
    /**
     * metodo que actualia una carrera de la tabla carrera, que coincida con elid
     * @param carrera
     * @throws Exception 
     */
    public void actualizaCarrera(Carrera carrera) throws Exception{
        Carrera aux=controlCarrera.findCarrera(carrera.getIdCarrera());
        if(aux==null){
            throw  new Exception ("No existe");
        }
        controlCarrera.edit(carrera);
    }
    
    /**
     * metodo que elimina una carrera de la tabla carrera
     * @param id
     * @throws NonexistentEntityException 
     */
   public void eliminaCarrera(int id) throws NonexistentEntityException{
       controlCarrera.destroy(id);
   }
   //fin de seccion de control de tabla Carrera
   //---------------------------------------------------------------------------
   
   
   
   //--------------------------------------------------------------------------- 
   // secion de control de la tabla adeudo
 /**
  * metodo que agrega un adeudo en la tabla adeudo
  * @param adeudo
  * @throws Exception 
  */
   public void agregarAdeudo(Adeudo adeudo) throws Exception{
       controlAdeudo.create(adeudo);
   }
   
   /**
    * metodo que obtiene un adeudo de la tabla adeudo
    * @param id
    * @return 
    */
    public Adeudo obtenAdeudo(int id){
       return controlAdeudo.findAdeudo(id);
   }
    
    /**
     * metodo que actualiza un adeudo de la tabla adeudo
     * @param adeudo
     * @throws Exception 
     */
   public void actualizaAdeudo(Adeudo adeudo) throws Exception{
       Adeudo aux=controlAdeudo.findAdeudo(adeudo.getIdAdeudos());
       if(aux==null){
           throw new Exception("No existe el adeudo");
       }
       controlAdeudo.edit(adeudo);
   }
   
   /**
    * metodo que elimina un adeudo de la tabla adeudo
    * @param id
    * @throws NonexistentEntityException 
    */
   public void eliminaAdeudo(int id) throws NonexistentEntityException{
       controlAdeudo.destroy(id);
   } 
   // Fin de seccion de  control de la tabla Adeudo
   //---------------------------------------------------------------------------
   
   
   
   
   //---------------------------------------------------------------------------- 
   //INICIO seccion de control de la tabla Pago
   /**
    * metodo que agrega un pago en la tabla Pago
    * @param pago
    * @throws Exception 
    */
   public void agregarPago(Pago pago) throws Exception{
       controlPago.create(pago);
   }
   /**
    * metodo que obtiene un pago de la tabla pago
    * @param id
    * @return
    * @throws Exception 
    */
   public Pago obtenPago(int id) throws Exception{
      return  controlPago.findPago(id);
   }
   
   /**
    * actua liza el Pago de un alumno de la tabla pago
    * @param pago
    * @throws Exception 
    */
   public void actualizaPago(Pago pago) throws Exception{
       Pago aux=controlPago.findPago(pago.getIdPagos());
       if(aux==null){throw new Exception("no existe el Pago");}
       controlPago.edit(pago);
   }
   
   public void eliminaPago(int id) throws NonexistentEntityException{
       controlPago.destroy(id);
   }
   /**
    * obtenemos  una lista de los pagos
    */
   public List<Pago> obtenListaPagos(){
       return controlPago.findPagoEntities();
   }
   // FIN de la seccion control de tabla de PAGO
   //--------------------------------------------------------------------------
   
   
   
   
   //-------------------------------------------------------------------------
   //INICIO Seccion de control de metodos de la tabla Grupos
   /**
    * metodo que obtiene el grupo correspondiente de la tabla Grupo
    * @param id 
    */
   public Grupo obtenGrupo(int id){
       return controlGrupo.findGrupo(id);
   }
   /**
    * agrega un grupo en la tabla Grupos
    * @param grupo
    * @throws Exception 
    */
   public void agregaGrupo(Grupo grupo) throws Exception{
       controlGrupo.create(grupo);
   }
   
   /**
    * actualiza un un grupo de la tabla Grupos que coincida con el id
    * @param grupo
    * @throws Exception 
    */
   public void actualizaGrupo(Grupo grupo) throws Exception{
     
//        EntityManager em = null;
//        try {
//            em = controlGrupo.getEntityManager();
//            em.getTransaction().begin();
//            grupo = em.merge(grupo);
//            em.getTransaction().commit();
//        } catch (Exception ex) {
//            String msg = ex.getLocalizedMessage();
//            if (msg == null || msg.length() == 0) {
//                int id = grupo.getIdGrupos();
//                if (controlAlumno.findAlumno(id) == null) {
//                    throw new NonexistentEntityException("El grupo con el id:  " + id + " no existe.");
//                }
//            }
//            throw ex;
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
     
       
       Grupo aux=controlGrupo.findGrupo(grupo.getIdGrupos());
       if(aux==null){
           throw  new Exception("No existe el grupo");
       }
       controlGrupo.edit(grupo);
   }
   
   /**
    * metodo que elimina el grupo de la base de tos de la tabla Grupos, que
    * coincide  con el id
    * @param id
    * @throws NonexistentEntityException 
    */
   public void eliminagrupo(int id) throws NonexistentEntityException{
       controlGrupo.destroy(id);
   }
   //FIN delos metodos que controlan la  tabla Grupos
   //-------------------------------------------------------------------------
   
   
   
   
   //INICIO Metodos que controlan la tabla Materia
   //----------------------------------------------------------------------
   /**
    * metodo que obtiene una materia del abse de atos de la tabla materia
    * que coincida con el id
    * @param id
    * @return 
    */
   public Materia obtenMateria(int id){
       return controlMateria.findMateria(id);
   }
   
   /**
    * metodo que agrega una materia en la base de datos en la tabla Materia
    * @param materia
    * @throws Exception 
    */
   public void agregaMateria(Materia materia) throws Exception{
       controlMateria.create(materia);
   }
   
   /**
    * metodo que actualiza la materia que coincida con el id en la base de datos
    * de la tabla Materias
    * @param materia
    * @throws Exception 
    */
   public void actualizaMateria(Materia materia) throws Exception{
       Materia aux=controlMateria.findMateria(materia.getIdMateria());
       if(aux==null){
           throw  new Exception("no se encuentra la materia en la base de datos");
       }
       controlMateria.edit(materia);
   }
   
   /**
    * metodo que elimina la materia que coincida con el id, en la base de datos de la
    * tabla Materias
    * @param id
    * @throws NonexistentEntityException 
    */
  public void eliminaMateria(int id) throws NonexistentEntityException{
      controlMateria.destroy(id);
  }
  //FIN DE LOS Metodos que controlan la tabla Materia
  //----------------------------------------------------------------------
  
  
  
  // INICIO de los Metodos que controlan el comportamiento de la tabla MAESTROS
  //----------------------------------------------------------------------
  
  public Maestro obtenMaestro(int id){
      return controlMaestro.findMaestro(id);
  }
  public void agregaMaestro(Maestro maestro) throws Exception{
      controlMaestro.create(maestro);
  }
  public void actualizaMaestro(Maestro maestro) throws Exception{
      Maestro aux=controlMaestro.findMaestro(maestro.getIdMaestros());
      if(aux==null){
          throw  new Exception("NO SE ENCUENTRA EL MAESTRO EN LA BASE DE DATOS");
      }
      controlMaestro.edit(maestro);
  }
  
  public void eliminaMaestro(int id) throws NonexistentEntityException, Exception{
      controlMaestro.destroy(id);
  }
  //FIN delos metodos que controlan ala tabla MAestros
  //--------------------------------------------------------------------
  
  
  //INICIO de los metodos que controlan la tabla de la base de datos
  //CATALOGO
  
  public void agregaCatalogo(Catalogo catalogo) throws Exception{
      controlCatalogo.create(catalogo);
  } 
  public Catalogo obtenCatalog(int id){
      return controlCatalogo.findCatalogo(id);
  }
  public void actualiazaCatalogo(Catalogo catalogo) throws Exception{
      Catalogo aux=controlCatalogo.findCatalogo(catalogo.getIdCatalogo());
      if(aux==null){
          throw new Exception("No existe el catalog en la base de datos");
      }
      controlCatalogo.edit(catalogo);
  }
  
  public void eliminaCatalogo(int id) throws NonexistentEntityException{
      controlCatalogo.destroy(id);
  }
}
