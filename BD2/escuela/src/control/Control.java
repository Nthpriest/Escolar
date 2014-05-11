/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Hola IRving
package control;

import interfaces.DlgAdeudo;
import interfaces.DlgAlumnos;
import interfaces.DlgCarrera;
import interfaces.DlgCatalogo;
import interfaces.DlgMaestro;
import interfaces.DlgMateria;
import interfaces.DlgPagos;
import interfaces.JfrmPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import objetosNegocio.Adeudo;
import objetosNegocio.Alumno;
import objetosNegocio.Carrera;
import objetosNegocio.Catalogo;
import objetosNegocio.Maestro;
import objetosNegocio.Materia;
import objetosNegocio.Pago;
import persistencia.Persistencia;

/**
 *
 * @author IVI
 */
public class Control {

    // instaciamos la bariable que controla el comportamiento de las clases
    private Persistencia persistencia;

    /**
     * creamos un contructor que iniiaza la variable persisitencia que controla
     * el comportamiento de la base de datos, como tambien el comportamiento de
     * las ventanas
     */
    public Control() {
        this.persistencia = new Persistencia();
    }

    /**
     * Metodo que agrega una carrera
     *
     * @param frame
     * @return
     * @throws Exception
     */
    public boolean AgregarCarrera(JFrame frame) throws Exception {
        String IDMaestro = null;
        int id = -1;
        Carrera carrera = null;
        DlgCarrera dlgCarrera = null;

        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;

        IDMaestro = JOptionPane.showInputDialog(frame, "Ingrese el ID de la carrera");

        if (IDMaestro == null) {
            return false;
        }
        try {
            id = Integer.parseInt(IDMaestro);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }

        // buscamos un repetido
        carrera = persistencia.obtenCarrera(id);
        if (carrera != null) {
            JOptionPane.showMessageDialog(null, "Repetido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        carrera = new Carrera();
        carrera.setIdCarrera(id);
        dlgCarrera = new DlgCarrera(frame, true, carrera, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.agregaCarrera(carrera);
        return true;
    }

    /**
     * Metodo que agrega un Alumno alal base de datos
     *
     * @param frame
     * @return
     * @throws Exception
     */
    public boolean AgregarAlumno(JFrame frame) throws Exception {
        String idAlumno = null;
        int id = -1;
        Alumno alumno = null;
        DlgAlumnos dlgAlumno = null;

        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;

        idAlumno = JOptionPane.showInputDialog(frame, "Ingrese el ID de la Alumno");

        if (idAlumno == null) {
            return false;
        }

        try {
            id = Integer.parseInt(idAlumno);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }

        alumno = persistencia.obtenAlumno(id);

        if (alumno != null) {
            JOptionPane.showMessageDialog(null, "Repetido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        alumno = new Alumno(id);
        dlgAlumno = new DlgAlumnos(frame, true, alumno, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.agregarAlumno(alumno);
        return true;

    }

    /**
     * Metodo que Agrega una Carrera a un alumno
     *
     * @param frame
     * @return
     */
    public boolean AgregarAlumnoCarrera(JFrame frame) throws Exception {
        Collection<Alumno> listaAlumno = new ArrayList<>();
        Alumno alumno = null;
        Carrera carrera = null;
        int id = -1;
        String idAlumno = null;
        DlgAlumnos dlgAlumno;

        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.DESPLEGAR;

        // obtenemos  la carrera a seleccionar
        String mensaje = "";
        int pos = -1;
        for (Carrera car : persistencia.obtenListaCarrera()) {
            mensaje = mensaje + (car.getIdCarrera()) + "-" + car.getNombre() + "\n";
            pos = 1;
        }

        // confirmamos que ubo  si ubo carrreras
        if (pos == -1) {
            JOptionPane.showMessageDialog(null, "NHO HAY  CARRERAS DISPONIBLEs", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // seleccionas la carrera
        mensaje = JOptionPane.showInputDialog(frame, "Seleciona la carrera\n" + mensaje);

        // verificamos que se ingreso un numero
        try {
            pos = Integer.parseInt(mensaje);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
        // obtenemos la carrera seleccionada
        carrera = persistencia.obtenCarrera(pos);
        // confirmamaos que existe la carrera
        if (carrera == null) {
            return false;
        }

        idAlumno = JOptionPane.showInputDialog(frame, "Ingrese el ID de la Alumno");

        if (idAlumno == null) {
            return false;
        }

        try {
            id = Integer.parseInt(idAlumno);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // obtenemos el alumno
        alumno = persistencia.obtenAlumno(id);
        if (alumno == null) {
            JOptionPane.showMessageDialog(null, "No existe", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        alumno.setIdCarreraFK(carrera);

        dlgAlumno = new DlgAlumnos(frame, true, alumno, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.actualizaAlumno(alumno);
        return true;
    }

    /**
     * Metodo que ejecuta la orden de agregar un Pago
     *
     * @param frame
     * @return
     */
    public boolean agregarPago(JFrame frame) throws Exception {
        String idAlumno = null;
        int id = -1, idPago;
        Alumno alumno = null;
        Pago pago = null;
        DlgPagos dlgPago = null;

        String buscarPago = null;
        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);

        int operacion = ConstantesGUI.AGREGAR;
        idAlumno = JOptionPane.showInputDialog(frame, "Ingrese el ID de la Alumno");

        try {
            id = Integer.parseInt(idAlumno);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // buscamos el alumno
        alumno = persistencia.obtenAlumno(id);
        // si es null, entonce el alumno no existe
        if (alumno == null) {
            JOptionPane.showMessageDialog(null, "No existe Alumno", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        buscarPago = JOptionPane.showInputDialog(frame, "Ingrese el ID del Pago");

        try {
            idPago = Integer.parseInt(buscarPago);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "dato Invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // obtenemos un pago
        pago = persistencia.obtenPago(idPago);
        // si es diferente de null es REPETIDO
        if (pago != null) {
            JOptionPane.showMessageDialog(null, "Pago Repetido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Procedemos ala creacion de la creacion de pago, agregandole un ID
        pago = new Pago();
        pago.setIdPagos(idPago);
        dlgPago = new DlgPagos(frame, true, pago, respuesta, operacion, alumno);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }
        persistencia.agregarPago(pago);
        return true;

    }

    public boolean agregarDeuda(JFrame frame) throws Exception {
        Adeudo deuda = null;
        String idDeuda = null, idAlumno = null;
        int id;
        Alumno alumno = null;
        DlgAdeudo dlgAdeudo = null;
        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;
        // buscamos al alumno correspondiente
        idAlumno = JOptionPane.showInputDialog(frame, "Ingrese el ID de la Alumno");

        try {
            id = Integer.parseInt(idAlumno);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "dato Invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        alumno = persistencia.obtenAlumno(id);

        if (alumno == null) {
            JOptionPane.showMessageDialog(null, "Alumno no existe", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Verificamos existenicia de entidad deuda
        idDeuda = JOptionPane.showInputDialog(frame, "Ingrese el ID de la Alumno");

        id = Integer.parseInt(idDeuda);
        deuda = persistencia.obtenAdeudo(id);

        if (deuda != null) {
            JOptionPane.showMessageDialog(null, "Deuda repetida", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        deuda = new Adeudo();
        deuda.setIdAdeudos(id);
        deuda.setIdAlumnoFK(alumno);

        dlgAdeudo = new DlgAdeudo(frame, true, deuda, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }
        persistencia.agregarAdeudo(deuda);
        return true;

    }

    public boolean AgregaCatalogo(JFrame frame) throws Exception {
        Catalogo catalogo = null;
        String idCatalogo;
        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;
        int buscar = -1;
        DlgCatalogo dlgCatalogo = null;

        idCatalogo = JOptionPane.showInputDialog(frame, "Ingrese el ID del Catalogo nuevo");
        if (idCatalogo == null) {
            return false;
        }
        try {
            buscar = Integer.parseInt(idCatalogo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Dato invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        catalogo = persistencia.obtenCatalog(buscar);

        if (catalogo != null) {
            JOptionPane.showMessageDialog(null, "Catalogo repetido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        catalogo = new Catalogo(buscar);
        dlgCatalogo = new DlgCatalogo(frame, true, catalogo, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.agregaCatalogo(catalogo);
        return true;

    }
    
    public boolean actualizaCatalogo(JFrame frame) throws Exception{
         Catalogo catalogo=null;
         Materia materia=null;
         Maestro maestro=null;
         String cadena=null, cadenaMateria=null, cadenaMaestro=null, pregunta=null;
         
         int numero=0,numeroMateria=0, numeroMaestro=0;
         DlgCatalogo dlgCatalogo=null;
         
         StringBuffer respuesta=new StringBuffer(ConstantesGUI.ACEPTAR);
         int operacion=ConstantesGUI.ACTUALIZAR;
         
           cadena = JOptionPane.showInputDialog(frame, "Ingrese el ID del Catalogo");

         if(cadena==null){return false;}
         
        try {
            numero = Integer.parseInt(cadena);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "dato Invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }
         
        catalogo=persistencia.obtenCatalog(numero);
        if(catalogo==null){
             JOptionPane.showMessageDialog(null, "No se encontro el Catalogo deseado", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
           pregunta=JOptionPane.showInputDialog(frame, "Desea agregarle Materia y Maestro (SI//NO)");
           if(pregunta.equalsIgnoreCase("no")){return false;}
           
           cadenaMateria = JOptionPane.showInputDialog(frame, "Ingrese el ID dela Materia");
           cadenaMaestro = JOptionPane.showInputDialog(frame, "Ingrese el ID del Maestro");
           
           try{
               numeroMaestro=Integer.parseInt(cadenaMaestro);
               numeroMateria=Integer.parseInt(cadenaMateria);
           }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Dato invalio", "Advertencia", JOptionPane.ERROR_MESSAGE);
                return false;
           }
            
           maestro=persistencia.obtenMaestro(numeroMaestro);
           materia=persistencia.obtenMateria(numeroMateria);
           
          if(maestro==null){
              JOptionPane.showMessageDialog(null, "No se encontro el maestro", "Advertencia", JOptionPane.ERROR_MESSAGE);
          }
          if(materia == null){
              JOptionPane.showMessageDialog(null, "No se encontro la materia", "Advertencia", JOptionPane.ERROR_MESSAGE);
          }
           
         catalogo.setIdMaestroFK(maestro);
         catalogo.setIdMateriaFK(materia);
         
        dlgCatalogo=new DlgCatalogo(frame, true, catalogo, respuesta, operacion);
        if(respuesta.toString().equals(ConstantesGUI.CANCELAR)){
            return false;
        }
        
        persistencia.actualiazaCatalogo(catalogo);
        return true;
        
    }

    public boolean consultaCatalogo(JFrame frame){
        Catalogo catalogo=null;
        String cadena=null;
        int numero=0;
        DlgCatalogo dlgCatalogo=null;
        
        StringBuffer respuesta=new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion=ConstantesGUI.DESPLEGAR;
        
        cadena = JOptionPane.showInputDialog(frame, "Ingrese el ID del Catalogo");

         if(cadena==null){return false;}
         
         try{
             numero=Integer.parseInt(cadena);
         }catch(NumberFormatException e){
             JOptionPane.showMessageDialog(null, "DatoInvalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
         }
         
         catalogo=persistencia.obtenCatalog(numero);
        
         if(catalogo==null){
              JOptionPane.showMessageDialog(null, "No existe el catalogo", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
         }
        dlgCatalogo=new DlgCatalogo(frame, true, catalogo, respuesta, operacion);
        return true;
    }
    /**
     * Operacion que agrega una materia ala base de datos de mysql
     * @param frame
     * @return
     * @throws Exception 
     */
    public boolean AgregaMateria(JFrame frame) throws Exception {
        Materia materia = null;
        int idMateria = -1;
        String cadena = null;
        DlgMateria dlgMateria = null;

        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;

        cadena = JOptionPane.showInputDialog(frame, "Ingrese el ID dela Materia nueva");
        if (cadena == null) {
            return false;
        }

        try {
            idMateria = Integer.parseInt(cadena);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Dato invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        materia = persistencia.obtenMateria(idMateria);

        if (materia != null) {
            JOptionPane.showMessageDialog(null, "Materia repetida", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        materia = new Materia(idMateria);
        dlgMateria = new DlgMateria(frame, true, materia, respuesta, operacion);
        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.agregaMateria(materia);
        return true;
    }

    public boolean AgregaMaestro(JFrame frame) throws Exception {
        Maestro maestro = null;
        int idMaestro = -1;
        String cadena = null;
        DlgMaestro dlgMaestro = null;

        StringBuffer respuesta = new StringBuffer(ConstantesGUI.ACEPTAR);
        int operacion = ConstantesGUI.AGREGAR;

        cadena = JOptionPane.showInputDialog(frame, "Ingrese el ID de Maestro nuevo");
        if (cadena == null) {
            return false;
        }

        try {
            idMaestro = Integer.parseInt(cadena);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Dato invalido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        maestro = persistencia.obtenMaestro(idMaestro);

        if (maestro != null) {
            JOptionPane.showMessageDialog(null, "MAestro Repetido", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        maestro = new Maestro(idMaestro);
        dlgMaestro = new DlgMaestro(frame, true, maestro, respuesta, operacion);

        if (respuesta.toString().equals(ConstantesGUI.CANCELAR)) {
            return false;
        }

        persistencia.agregaMaestro(maestro);
        return true;
    }
}
