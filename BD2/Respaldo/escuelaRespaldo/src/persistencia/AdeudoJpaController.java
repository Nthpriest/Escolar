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
import objetosNegocio.Adeudo;
import objetosNegocio.Alumno;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author IVI
 */
public class AdeudoJpaController implements Serializable {

    public AdeudoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Adeudo adeudo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumnoFK = adeudo.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK = em.getReference(idAlumnoFK.getClass(), idAlumnoFK.getIdAlumnos());
                adeudo.setIdAlumnoFK(idAlumnoFK);
            }
            em.persist(adeudo);
            if (idAlumnoFK != null) {
                idAlumnoFK.getAdeudoCollection().add(adeudo);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdeudo(adeudo.getIdAdeudos()) != null) {
                throw new PreexistingEntityException("Adeudo " + adeudo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Adeudo adeudo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Adeudo persistentAdeudo = em.find(Adeudo.class, adeudo.getIdAdeudos());
            Alumno idAlumnoFKOld = persistentAdeudo.getIdAlumnoFK();
            Alumno idAlumnoFKNew = adeudo.getIdAlumnoFK();
            if (idAlumnoFKNew != null) {
                idAlumnoFKNew = em.getReference(idAlumnoFKNew.getClass(), idAlumnoFKNew.getIdAlumnos());
                adeudo.setIdAlumnoFK(idAlumnoFKNew);
            }
            adeudo = em.merge(adeudo);
            if (idAlumnoFKOld != null && !idAlumnoFKOld.equals(idAlumnoFKNew)) {
                idAlumnoFKOld.getAdeudoCollection().remove(adeudo);
                idAlumnoFKOld = em.merge(idAlumnoFKOld);
            }
            if (idAlumnoFKNew != null && !idAlumnoFKNew.equals(idAlumnoFKOld)) {
                idAlumnoFKNew.getAdeudoCollection().add(adeudo);
                idAlumnoFKNew = em.merge(idAlumnoFKNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = adeudo.getIdAdeudos();
                if (findAdeudo(id) == null) {
                    throw new NonexistentEntityException("The adeudo with id " + id + " no longer exists.");
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
            Adeudo adeudo;
            try {
                adeudo = em.getReference(Adeudo.class, id);
                adeudo.getIdAdeudos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adeudo with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumnoFK = adeudo.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK.getAdeudoCollection().remove(adeudo);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.remove(adeudo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Adeudo> findAdeudoEntities() {
        return findAdeudoEntities(true, -1, -1);
    }

    public List<Adeudo> findAdeudoEntities(int maxResults, int firstResult) {
        return findAdeudoEntities(false, maxResults, firstResult);
    }

    private List<Adeudo> findAdeudoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Adeudo.class));
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

    public Adeudo findAdeudo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adeudo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdeudoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Adeudo> rt = cq.from(Adeudo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
