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
import objetosNegocio.Materia;
import objetosNegocio.Maestro;
import objetosNegocio.Grupo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import objetosNegocio.Catalogo;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author Adrian
 */
public class CatalogoJpaController implements Serializable {

    public CatalogoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Catalogo catalogo) throws PreexistingEntityException, Exception {
        if (catalogo.getGrupoCollection() == null) {
            catalogo.setGrupoCollection(new ArrayList<Grupo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia idMateriaFK = catalogo.getIdMateriaFK();
            if (idMateriaFK != null) {
                idMateriaFK = em.getReference(idMateriaFK.getClass(), idMateriaFK.getIdMateria());
                catalogo.setIdMateriaFK(idMateriaFK);
            }
            Maestro idMaestroFK = catalogo.getIdMaestroFK();
            if (idMaestroFK != null) {
                idMaestroFK = em.getReference(idMaestroFK.getClass(), idMaestroFK.getIdMaestros());
                catalogo.setIdMaestroFK(idMaestroFK);
            }
            Collection<Grupo> attachedGrupoCollection = new ArrayList<Grupo>();
            for (Grupo grupoCollectionGrupoToAttach : catalogo.getGrupoCollection()) {
                grupoCollectionGrupoToAttach = em.getReference(grupoCollectionGrupoToAttach.getClass(), grupoCollectionGrupoToAttach.getIdGrupos());
                attachedGrupoCollection.add(grupoCollectionGrupoToAttach);
            }
            catalogo.setGrupoCollection(attachedGrupoCollection);
            em.persist(catalogo);
            if (idMateriaFK != null) {
                idMateriaFK.getCatalogoCollection().add(catalogo);
                idMateriaFK = em.merge(idMateriaFK);
            }
            if (idMaestroFK != null) {
                idMaestroFK.getCatalogoCollection().add(catalogo);
                idMaestroFK = em.merge(idMaestroFK);
            }
            for (Grupo grupoCollectionGrupo : catalogo.getGrupoCollection()) {
                Catalogo oldIdCatalogoFKOfGrupoCollectionGrupo = grupoCollectionGrupo.getIdCatalogoFK();
                grupoCollectionGrupo.setIdCatalogoFK(catalogo);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
                if (oldIdCatalogoFKOfGrupoCollectionGrupo != null) {
                    oldIdCatalogoFKOfGrupoCollectionGrupo.getGrupoCollection().remove(grupoCollectionGrupo);
                    oldIdCatalogoFKOfGrupoCollectionGrupo = em.merge(oldIdCatalogoFKOfGrupoCollectionGrupo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCatalogo(catalogo.getIdCatalogo()) != null) {
                throw new PreexistingEntityException("Catalogo " + catalogo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Catalogo catalogo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalogo persistentCatalogo = em.find(Catalogo.class, catalogo.getIdCatalogo());
            Materia idMateriaFKOld = persistentCatalogo.getIdMateriaFK();
            Materia idMateriaFKNew = catalogo.getIdMateriaFK();
            Maestro idMaestroFKOld = persistentCatalogo.getIdMaestroFK();
            Maestro idMaestroFKNew = catalogo.getIdMaestroFK();
            Collection<Grupo> grupoCollectionOld = persistentCatalogo.getGrupoCollection();
            Collection<Grupo> grupoCollectionNew = catalogo.getGrupoCollection();
            if (idMateriaFKNew != null) {
                idMateriaFKNew = em.getReference(idMateriaFKNew.getClass(), idMateriaFKNew.getIdMateria());
                catalogo.setIdMateriaFK(idMateriaFKNew);
            }
            if (idMaestroFKNew != null) {
                idMaestroFKNew = em.getReference(idMaestroFKNew.getClass(), idMaestroFKNew.getIdMaestros());
                catalogo.setIdMaestroFK(idMaestroFKNew);
            }
            Collection<Grupo> attachedGrupoCollectionNew = new ArrayList<Grupo>();
            for (Grupo grupoCollectionNewGrupoToAttach : grupoCollectionNew) {
                grupoCollectionNewGrupoToAttach = em.getReference(grupoCollectionNewGrupoToAttach.getClass(), grupoCollectionNewGrupoToAttach.getIdGrupos());
                attachedGrupoCollectionNew.add(grupoCollectionNewGrupoToAttach);
            }
            grupoCollectionNew = attachedGrupoCollectionNew;
            catalogo.setGrupoCollection(grupoCollectionNew);
            catalogo = em.merge(catalogo);
            if (idMateriaFKOld != null && !idMateriaFKOld.equals(idMateriaFKNew)) {
                idMateriaFKOld.getCatalogoCollection().remove(catalogo);
                idMateriaFKOld = em.merge(idMateriaFKOld);
            }
            if (idMateriaFKNew != null && !idMateriaFKNew.equals(idMateriaFKOld)) {
                idMateriaFKNew.getCatalogoCollection().add(catalogo);
                idMateriaFKNew = em.merge(idMateriaFKNew);
            }
            if (idMaestroFKOld != null && !idMaestroFKOld.equals(idMaestroFKNew)) {
                idMaestroFKOld.getCatalogoCollection().remove(catalogo);
                idMaestroFKOld = em.merge(idMaestroFKOld);
            }
            if (idMaestroFKNew != null && !idMaestroFKNew.equals(idMaestroFKOld)) {
                idMaestroFKNew.getCatalogoCollection().add(catalogo);
                idMaestroFKNew = em.merge(idMaestroFKNew);
            }
            for (Grupo grupoCollectionOldGrupo : grupoCollectionOld) {
                if (!grupoCollectionNew.contains(grupoCollectionOldGrupo)) {
                    grupoCollectionOldGrupo.setIdCatalogoFK(null);
                    grupoCollectionOldGrupo = em.merge(grupoCollectionOldGrupo);
                }
            }
            for (Grupo grupoCollectionNewGrupo : grupoCollectionNew) {
                if (!grupoCollectionOld.contains(grupoCollectionNewGrupo)) {
                    Catalogo oldIdCatalogoFKOfGrupoCollectionNewGrupo = grupoCollectionNewGrupo.getIdCatalogoFK();
                    grupoCollectionNewGrupo.setIdCatalogoFK(catalogo);
                    grupoCollectionNewGrupo = em.merge(grupoCollectionNewGrupo);
                    if (oldIdCatalogoFKOfGrupoCollectionNewGrupo != null && !oldIdCatalogoFKOfGrupoCollectionNewGrupo.equals(catalogo)) {
                        oldIdCatalogoFKOfGrupoCollectionNewGrupo.getGrupoCollection().remove(grupoCollectionNewGrupo);
                        oldIdCatalogoFKOfGrupoCollectionNewGrupo = em.merge(oldIdCatalogoFKOfGrupoCollectionNewGrupo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = catalogo.getIdCatalogo();
                if (findCatalogo(id) == null) {
                    throw new NonexistentEntityException("The catalogo with id " + id + " no longer exists.");
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
            Catalogo catalogo;
            try {
                catalogo = em.getReference(Catalogo.class, id);
                catalogo.getIdCatalogo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalogo with id " + id + " no longer exists.", enfe);
            }
            Materia idMateriaFK = catalogo.getIdMateriaFK();
            if (idMateriaFK != null) {
                idMateriaFK.getCatalogoCollection().remove(catalogo);
                idMateriaFK = em.merge(idMateriaFK);
            }
            Maestro idMaestroFK = catalogo.getIdMaestroFK();
            if (idMaestroFK != null) {
                idMaestroFK.getCatalogoCollection().remove(catalogo);
                idMaestroFK = em.merge(idMaestroFK);
            }
            Collection<Grupo> grupoCollection = catalogo.getGrupoCollection();
            for (Grupo grupoCollectionGrupo : grupoCollection) {
                grupoCollectionGrupo.setIdCatalogoFK(null);
                grupoCollectionGrupo = em.merge(grupoCollectionGrupo);
            }
            em.remove(catalogo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Catalogo> findCatalogoEntities() {
        return findCatalogoEntities(true, -1, -1);
    }

    public List<Catalogo> findCatalogoEntities(int maxResults, int firstResult) {
        return findCatalogoEntities(false, maxResults, firstResult);
    }

    private List<Catalogo> findCatalogoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Catalogo.class));
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

    public Catalogo findCatalogo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Catalogo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Catalogo> rt = cq.from(Catalogo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
