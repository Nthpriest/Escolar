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
import objetosNegocio.Catalogo;
import objetosNegocio.Alumno;
import objetosNegocio.Grupo;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author IVI
 */
public class GrupoJpaController implements Serializable {

    public GrupoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupo grupo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalogo idCatalogoFK = grupo.getIdCatalogoFK();
            if (idCatalogoFK != null) {
                idCatalogoFK = em.getReference(idCatalogoFK.getClass(), idCatalogoFK.getIdCatalogo());
                grupo.setIdCatalogoFK(idCatalogoFK);
            }
            Alumno idAlumnoFK = grupo.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK = em.getReference(idAlumnoFK.getClass(), idAlumnoFK.getIdAlumnos());
                grupo.setIdAlumnoFK(idAlumnoFK);
            }
            em.persist(grupo);
            if (idCatalogoFK != null) {
                idCatalogoFK.getGrupoCollection().add(grupo);
                idCatalogoFK = em.merge(idCatalogoFK);
            }
            if (idAlumnoFK != null) {
                idAlumnoFK.getGrupoCollection().add(grupo);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupo(grupo.getIdGrupos()) != null) {
                throw new PreexistingEntityException("Grupo " + grupo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupo grupo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupo persistentGrupo = em.find(Grupo.class, grupo.getIdGrupos());
            Catalogo idCatalogoFKOld = persistentGrupo.getIdCatalogoFK();
            Catalogo idCatalogoFKNew = grupo.getIdCatalogoFK();
            Alumno idAlumnoFKOld = persistentGrupo.getIdAlumnoFK();
            Alumno idAlumnoFKNew = grupo.getIdAlumnoFK();
            if (idCatalogoFKNew != null) {
                idCatalogoFKNew = em.getReference(idCatalogoFKNew.getClass(), idCatalogoFKNew.getIdCatalogo());
                grupo.setIdCatalogoFK(idCatalogoFKNew);
            }
            if (idAlumnoFKNew != null) {
                idAlumnoFKNew = em.getReference(idAlumnoFKNew.getClass(), idAlumnoFKNew.getIdAlumnos());
                grupo.setIdAlumnoFK(idAlumnoFKNew);
            }
            grupo = em.merge(grupo);
            if (idCatalogoFKOld != null && !idCatalogoFKOld.equals(idCatalogoFKNew)) {
                idCatalogoFKOld.getGrupoCollection().remove(grupo);
                idCatalogoFKOld = em.merge(idCatalogoFKOld);
            }
            if (idCatalogoFKNew != null && !idCatalogoFKNew.equals(idCatalogoFKOld)) {
                idCatalogoFKNew.getGrupoCollection().add(grupo);
                idCatalogoFKNew = em.merge(idCatalogoFKNew);
            }
            if (idAlumnoFKOld != null && !idAlumnoFKOld.equals(idAlumnoFKNew)) {
                idAlumnoFKOld.getGrupoCollection().remove(grupo);
                idAlumnoFKOld = em.merge(idAlumnoFKOld);
            }
            if (idAlumnoFKNew != null && !idAlumnoFKNew.equals(idAlumnoFKOld)) {
                idAlumnoFKNew.getGrupoCollection().add(grupo);
                idAlumnoFKNew = em.merge(idAlumnoFKNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grupo.getIdGrupos();
                if (findGrupo(id) == null) {
                    throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.");
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
            Grupo grupo;
            try {
                grupo = em.getReference(Grupo.class, id);
                grupo.getIdGrupos();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupo with id " + id + " no longer exists.", enfe);
            }
            Catalogo idCatalogoFK = grupo.getIdCatalogoFK();
            if (idCatalogoFK != null) {
                idCatalogoFK.getGrupoCollection().remove(grupo);
                idCatalogoFK = em.merge(idCatalogoFK);
            }
            Alumno idAlumnoFK = grupo.getIdAlumnoFK();
            if (idAlumnoFK != null) {
                idAlumnoFK.getGrupoCollection().remove(grupo);
                idAlumnoFK = em.merge(idAlumnoFK);
            }
            em.remove(grupo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupo> findGrupoEntities() {
        return findGrupoEntities(true, -1, -1);
    }

    public List<Grupo> findGrupoEntities(int maxResults, int firstResult) {
        return findGrupoEntities(false, maxResults, firstResult);
    }

    private List<Grupo> findGrupoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupo.class));
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

    public Grupo findGrupo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupo.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupo> rt = cq.from(Grupo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
