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
import objetosNegocio.Catalogo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import objetosNegocio.Maestro;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author IVI
 */
public class MaestroJpaController implements Serializable {

    public MaestroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Maestro maestro) throws PreexistingEntityException, Exception {
        if (maestro.getCatalogoCollection() == null) {
            maestro.setCatalogoCollection(new ArrayList<Catalogo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Catalogo> attachedCatalogoCollection = new ArrayList<Catalogo>();
            for (Catalogo catalogoCollectionCatalogoToAttach : maestro.getCatalogoCollection()) {
                catalogoCollectionCatalogoToAttach = em.getReference(catalogoCollectionCatalogoToAttach.getClass(), catalogoCollectionCatalogoToAttach.getIdCatalogo());
                attachedCatalogoCollection.add(catalogoCollectionCatalogoToAttach);
            }
            maestro.setCatalogoCollection(attachedCatalogoCollection);
            em.persist(maestro);
            for (Catalogo catalogoCollectionCatalogo : maestro.getCatalogoCollection()) {
                Maestro oldIdMaestroFKOfCatalogoCollectionCatalogo = catalogoCollectionCatalogo.getIdMaestroFK();
                catalogoCollectionCatalogo.setIdMaestroFK(maestro);
                catalogoCollectionCatalogo = em.merge(catalogoCollectionCatalogo);
                if (oldIdMaestroFKOfCatalogoCollectionCatalogo != null) {
                    oldIdMaestroFKOfCatalogoCollectionCatalogo.getCatalogoCollection().remove(catalogoCollectionCatalogo);
                    oldIdMaestroFKOfCatalogoCollectionCatalogo = em.merge(oldIdMaestroFKOfCatalogoCollectionCatalogo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMaestro(maestro.getIdMaestros()) != null) {
                throw new PreexistingEntityException("Maestro " + maestro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Maestro maestro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Maestro persistentMaestro = em.find(Maestro.class, maestro.getIdMaestros());
            Collection<Catalogo> catalogoCollectionOld = persistentMaestro.getCatalogoCollection();
            Collection<Catalogo> catalogoCollectionNew = maestro.getCatalogoCollection();
            Collection<Catalogo> attachedCatalogoCollectionNew = new ArrayList<Catalogo>();
            for (Catalogo catalogoCollectionNewCatalogoToAttach : catalogoCollectionNew) {
                catalogoCollectionNewCatalogoToAttach = em.getReference(catalogoCollectionNewCatalogoToAttach.getClass(), catalogoCollectionNewCatalogoToAttach.getIdCatalogo());
                attachedCatalogoCollectionNew.add(catalogoCollectionNewCatalogoToAttach);
            }
            catalogoCollectionNew = attachedCatalogoCollectionNew;
            maestro.setCatalogoCollection(catalogoCollectionNew);
            maestro = em.merge(maestro);
            for (Catalogo catalogoCollectionOldCatalogo : catalogoCollectionOld) {
                if (!catalogoCollectionNew.contains(catalogoCollectionOldCatalogo)) {
                    catalogoCollectionOldCatalogo.setIdMaestroFK(null);
                    catalogoCollectionOldCatalogo = em.merge(catalogoCollectionOldCatalogo);
                }
            }
            for (Catalogo catalogoCollectionNewCatalogo : catalogoCollectionNew) {
                if (!catalogoCollectionOld.contains(catalogoCollectionNewCatalogo)) {
                    Maestro oldIdMaestroFKOfCatalogoCollectionNewCatalogo = catalogoCollectionNewCatalogo.getIdMaestroFK();
                    catalogoCollectionNewCatalogo.setIdMaestroFK(maestro);
                    catalogoCollectionNewCatalogo = em.merge(catalogoCollectionNewCatalogo);
                    if (oldIdMaestroFKOfCatalogoCollectionNewCatalogo != null && !oldIdMaestroFKOfCatalogoCollectionNewCatalogo.equals(maestro)) {
                        oldIdMaestroFKOfCatalogoCollectionNewCatalogo.getCatalogoCollection().remove(catalogoCollectionNewCatalogo);
                        oldIdMaestroFKOfCatalogoCollectionNewCatalogo = em.merge(oldIdMaestroFKOfCatalogoCollectionNewCatalogo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = maestro.getIdMaestros();
                if (findMaestro(id) == null) {
                    throw new NonexistentEntityException("The maestro with id " + id + " no longer exists.");
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
            Maestro maestro;
            try {
                maestro = em.getReference(Maestro.class, id);
                maestro.getIdMaestros();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The maestro with id " + id + " no longer exists.", enfe);
            }
            Collection<Catalogo> catalogoCollection = maestro.getCatalogoCollection();
            for (Catalogo catalogoCollectionCatalogo : catalogoCollection) {
                catalogoCollectionCatalogo.setIdMaestroFK(null);
                catalogoCollectionCatalogo = em.merge(catalogoCollectionCatalogo);
            }
            em.remove(maestro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Maestro> findMaestroEntities() {
        return findMaestroEntities(true, -1, -1);
    }

    public List<Maestro> findMaestroEntities(int maxResults, int firstResult) {
        return findMaestroEntities(false, maxResults, firstResult);
    }

    private List<Maestro> findMaestroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Maestro.class));
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

    public Maestro findMaestro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Maestro.class, id);
        } finally {
            em.close();
        }
    }

    public int getMaestroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Maestro> rt = cq.from(Maestro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
