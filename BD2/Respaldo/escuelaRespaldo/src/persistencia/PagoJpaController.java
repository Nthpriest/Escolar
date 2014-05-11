/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import objetosNegocio.Alumno;
import objetosNegocio.Pago;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author IVI
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumnoFK = pago.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK = em.getReference(idAlumnoFK.getClass(), idAlumnoFK.getIdAlumnos());
                pago.setIdAlumnoFK(idAlumnoFK);
            }
            em.persist(pago);
            if (idAlumnoFK != null) {
                idAlumnoFK.getPagoCollection().add(pago);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPago(pago.getIdPagos()) != null) {
                throw new PreexistingEntityException("Pago " + pago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getIdPagos());
            Alumno idAlumnoFKOld = persistentPago.getIdAlumnoFK();
            Alumno idAlumnoFKNew = pago.getIdAlumnoFK();
            if (idAlumnoFKNew != null) {
                idAlumnoFKNew = em.getReference(idAlumnoFKNew.getClass(), idAlumnoFKNew.getIdAlumnos());
                pago.setIdAlumnoFK(idAlumnoFKNew);
            }
            pago = em.merge(pago);
            if (idAlumnoFKOld != null && !idAlumnoFKOld.equals(idAlumnoFKNew)) {
                idAlumnoFKOld.getPagoCollection().remove(pago);
                idAlumnoFKOld = em.merge(idAlumnoFKOld);
            }
            if (idAlumnoFKNew != null && !idAlumnoFKNew.equals(idAlumnoFKOld)) {
                idAlumnoFKNew.getPagoCollection().add(pago);
                idAlumnoFKNew = em.merge(idAlumnoFKNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getIdPagos();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getIdPagos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumnoFK = pago.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK.getPagoCollection().remove(pago);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
