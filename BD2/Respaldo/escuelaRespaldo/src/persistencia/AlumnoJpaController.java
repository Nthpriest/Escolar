/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import objetosNegocio.Carrera;
import objetosNegocio.Pago;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import objetosNegocio.Adeudo;
import objetosNegocio.Alumno;
import objetosNegocio.Grupo;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author IVI
 */
public class AlumnoJpaController implements Serializable {

    public AlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) throws PreexistingEntityException, Exception {
        if (alumno.getPagoCollection() == null) {
            alumno.setPagoCollection(new ArrayList<Pago>());
        }
        if (alumno.getAdeudoCollection() == null) {
            alumno.setAdeudoCollection(new ArrayList<Adeudo>());
        }
        if (alumno.getGrupoCollection() == null) {
            alumno.setGrupoCollection(new ArrayList<Grupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carrera idCarreraFK = alumno.getIdCarreraFK();
            if (idCarreraFK != null) {
                idCarreraFK = em.getReference(idCarreraFK.getClass(), idCarreraFK.getIdCarrera());
                alumno.setIdCarreraFK(idCarreraFK);
            }
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : alumno.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPagos());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            alumno.setPagoCollection(attachedPagoCollection);
            Collection<Adeudo> attachedAdeudoCollection = new ArrayList<Adeudo>();
            for (Adeudo adeudoCollectionAdeudoToAttach : alumno.getAdeudoCollection()) {
                adeudoCollectionAdeudoToAttach = em.getReference(adeudoCollectionAdeudoToAttach.getClass(), adeudoCollectionAdeudoToAttach.getIdAdeudos());
                attachedAdeudoCollection.add(adeudoCollectionAdeudoToAttach);
            }
            alumno.setAdeudoCollection(attachedAdeudoCollection);
            Collection<Grupo> attachedGrupoCollection = new ArrayList<Grupo>();
            for (Grupo grupoCollectionGrupoToAttach : alumno.getGrupoCollection()) {
                grupoCollectionGrupoToAttach = em.getReference(grupoCollectionGrupoToAttach.getClass(), grupoCollectionGrupoToAttach.getIdGrupos());
                attachedGrupoCollection.add(grupoCollectionGrupoToAttach);
            }
            alumno.setGrupoCollection(attachedGrupoCollection);
            em.persist(alumno);
            if (idCarreraFK != null) {
                idCarreraFK.getAlumnoCollection().add(alumno);
                idCarreraFK = em.merge(idCarreraFK);
            }
            for (Pago pagoCollectionPago : alumno.getPagoCollection()) {
                Alumno oldIdAlumnoFKOfPagoCollectionPago = pagoCollectionPago.getIdAlumnoFK();
                pagoCollectionPago.setIdAlumnoFK(alumno);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldIdAlumnoFKOfPagoCollectionPago != null) {
                    oldIdAlumnoFKOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldIdAlumnoFKOfPagoCollectionPago = em.merge(oldIdAlumnoFKOfPagoCollectionPago);
                }
            }
            for (Adeudo adeudoCollectionAdeudo : alumno.getAdeudoCollection()) {
                Alumno oldIdAlumnoFKOfAdeudoCollectionAdeudo = adeudoCollectionAdeudo.getIdAlumnoFK();
                adeudoCollectionAdeudo.setIdAlumnoFK(alumno);
                adeudoCollectionAdeudo = em.merge(adeudoCollectionAdeudo);
                if (oldIdAlumnoFKOfAdeudoCollectionAdeudo != null) {
                    oldIdAlumnoFKOfAdeudoCollectionAdeudo.getAdeudoCollection().remove(adeudoCollectionAdeudo);
                    oldIdAlumnoFKOfAdeudoCollectionAdeudo = em.merge(oldIdAlumnoFKOfAdeudoCollectionAdeudo);
                }
            }
            for (Grupo grupoCollectionGrupo : alumno.getGrupoCollection()) {
                Alumno oldIdAlumnoFKOfGrupoCollectionGrupo = grupoCollectionGrupo.getIdAlumnoFK();
                grupoCollectionGrupo.setIdAlumnoFK(alumno);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
                if (oldIdAlumnoFKOfGrupoCollectionGrupo != null) {
                    oldIdAlumnoFKOfGrupoCollectionGrupo.getGrupoCollection().remove(grupoCollectionGrupo);
                    oldIdAlumnoFKOfGrupoCollectionGrupo = em.merge(oldIdAlumnoFKOfGrupoCollectionGrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlumno(alumno.getIdAlumnos()) != null) {
                throw new PreexistingEntityException("Alumno " + alumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getIdAlumnos());
            Carrera idCarreraFKOld = persistentAlumno.getIdCarreraFK();
            Carrera idCarreraFKNew = alumno.getIdCarreraFK();
            Collection<Pago> pagoCollectionOld = persistentAlumno.getPagoCollection();
            Collection<Pago> pagoCollectionNew = alumno.getPagoCollection();
            Collection<Adeudo> adeudoCollectionOld = persistentAlumno.getAdeudoCollection();
            Collection<Adeudo> adeudoCollectionNew = alumno.getAdeudoCollection();
            Collection<Grupo> grupoCollectionOld = persistentAlumno.getGrupoCollection();
            Collection<Grupo> grupoCollectionNew = alumno.getGrupoCollection();
            List<String> illegalOrphanMessages = null;
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoCollectionOldPago + " since its idAlumnoFK field is not nullable.");
                }
            }
            for (Adeudo adeudoCollectionOldAdeudo : adeudoCollectionOld) {
                if (!adeudoCollectionNew.contains(adeudoCollectionOldAdeudo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Adeudo " + adeudoCollectionOldAdeudo + " since its idAlumnoFK field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCarreraFKNew != null) {
                idCarreraFKNew = em.getReference(idCarreraFKNew.getClass(), idCarreraFKNew.getIdCarrera());
                alumno.setIdCarreraFK(idCarreraFKNew);
            }
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPagos());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            alumno.setPagoCollection(pagoCollectionNew);
            Collection<Adeudo> attachedAdeudoCollectionNew = new ArrayList<Adeudo>();
            for (Adeudo adeudoCollectionNewAdeudoToAttach : adeudoCollectionNew) {
                adeudoCollectionNewAdeudoToAttach = em.getReference(adeudoCollectionNewAdeudoToAttach.getClass(), adeudoCollectionNewAdeudoToAttach.getIdAdeudos());
                attachedAdeudoCollectionNew.add(adeudoCollectionNewAdeudoToAttach);
            }
            adeudoCollectionNew = attachedAdeudoCollectionNew;
            alumno.setAdeudoCollection(adeudoCollectionNew);
            Collection<Grupo> attachedGrupoCollectionNew = new ArrayList<Grupo>();
            for (Grupo grupoCollectionNewGrupoToAttach : grupoCollectionNew) {
                grupoCollectionNewGrupoToAttach = em.getReference(grupoCollectionNewGrupoToAttach.getClass(), grupoCollectionNewGrupoToAttach.getIdGrupos());
                attachedGrupoCollectionNew.add(grupoCollectionNewGrupoToAttach);
            }
            grupoCollectionNew = attachedGrupoCollectionNew;
            alumno.setGrupoCollection(grupoCollectionNew);
            alumno = em.merge(alumno);
            if (idCarreraFKOld != null && !idCarreraFKOld.equals(idCarreraFKNew)) {
                idCarreraFKOld.getAlumnoCollection().remove(alumno);
                idCarreraFKOld = em.merge(idCarreraFKOld);
            }
            if (idCarreraFKNew != null && !idCarreraFKNew.equals(idCarreraFKOld)) {
                idCarreraFKNew.getAlumnoCollection().add(alumno);
                idCarreraFKNew = em.merge(idCarreraFKNew);
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Alumno oldIdAlumnoFKOfPagoCollectionNewPago = pagoCollectionNewPago.getIdAlumnoFK();
                    pagoCollectionNewPago.setIdAlumnoFK(alumno);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldIdAlumnoFKOfPagoCollectionNewPago != null && !oldIdAlumnoFKOfPagoCollectionNewPago.equals(alumno)) {
                        oldIdAlumnoFKOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldIdAlumnoFKOfPagoCollectionNewPago = em.merge(oldIdAlumnoFKOfPagoCollectionNewPago);
                    }
                }
            }
            for (Adeudo adeudoCollectionNewAdeudo : adeudoCollectionNew) {
                if (!adeudoCollectionOld.contains(adeudoCollectionNewAdeudo)) {
                    Alumno oldIdAlumnoFKOfAdeudoCollectionNewAdeudo = adeudoCollectionNewAdeudo.getIdAlumnoFK();
                    adeudoCollectionNewAdeudo.setIdAlumnoFK(alumno);
                    adeudoCollectionNewAdeudo = em.merge(adeudoCollectionNewAdeudo);
                    if (oldIdAlumnoFKOfAdeudoCollectionNewAdeudo != null && !oldIdAlumnoFKOfAdeudoCollectionNewAdeudo.equals(alumno)) {
                        oldIdAlumnoFKOfAdeudoCollectionNewAdeudo.getAdeudoCollection().remove(adeudoCollectionNewAdeudo);
                        oldIdAlumnoFKOfAdeudoCollectionNewAdeudo = em.merge(oldIdAlumnoFKOfAdeudoCollectionNewAdeudo);
                    }
                }
            }
            for (Grupo grupoCollectionOldGrupo : grupoCollectionOld) {
                if (!grupoCollectionNew.contains(grupoCollectionOldGrupo)) {
                    grupoCollectionOldGrupo.setIdAlumnoFK(null);
                    grupoCollectionOldGrupo = em.merge(grupoCollectionOldGrupo);
                }
            }
            for (Grupo grupoCollectionNewGrupo : grupoCollectionNew) {
                if (!grupoCollectionOld.contains(grupoCollectionNewGrupo)) {
                    Alumno oldIdAlumnoFKOfGrupoCollectionNewGrupo = grupoCollectionNewGrupo.getIdAlumnoFK();
                    grupoCollectionNewGrupo.setIdAlumnoFK(alumno);
                    grupoCollectionNewGrupo = em.merge(grupoCollectionNewGrupo);
                    if (oldIdAlumnoFKOfGrupoCollectionNewGrupo != null && !oldIdAlumnoFKOfGrupoCollectionNewGrupo.equals(alumno)) {
                        oldIdAlumnoFKOfGrupoCollectionNewGrupo.getGrupoCollection().remove(grupoCollectionNewGrupo);
                        oldIdAlumnoFKOfGrupoCollectionNewGrupo = em.merge(oldIdAlumnoFKOfGrupoCollectionNewGrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = alumno.getIdAlumnos();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getIdAlumnos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pago> pagoCollectionOrphanCheck = alumno.getPagoCollection();
            for (Pago pagoCollectionOrphanCheckPago : pagoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Pago " + pagoCollectionOrphanCheckPago + " in its pagoCollection field has a non-nullable idAlumnoFK field.");
            }
            Collection<Adeudo> adeudoCollectionOrphanCheck = alumno.getAdeudoCollection();
            for (Adeudo adeudoCollectionOrphanCheckAdeudo : adeudoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Alumno (" + alumno + ") cannot be destroyed since the Adeudo " + adeudoCollectionOrphanCheckAdeudo + " in its adeudoCollection field has a non-nullable idAlumnoFK field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Carrera idCarreraFK = alumno.getIdCarreraFK();
            if (idCarreraFK != null) {
                idCarreraFK.getAlumnoCollection().remove(alumno);
                idCarreraFK = em.merge(idCarreraFK);
            }
            Collection<Grupo> grupoCollection = alumno.getGrupoCollection();
            for (Grupo grupoCollectionGrupo : grupoCollection) {
                grupoCollectionGrupo.setIdAlumnoFK(null);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Alumno findAlumno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
