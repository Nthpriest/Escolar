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
import objetosNegocio.Materia;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author Adrian
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) throws PreexistingEntityException, Exception {
        if (materia.getCatalogoCollection() == null) {
            materia.setCatalogoCollection(new ArrayList<Catalogo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Catalogo> attachedCatalogoCollection = new ArrayList<Catalogo>();
            for (Catalogo catalogoCollectionCatalogoToAttach : materia.getCatalogoCollection()) {
                catalogoCollectionCatalogoToAttach = em.getReference(catalogoCollectionCatalogoToAttach.getClass(), catalogoCollectionCatalogoToAttach.getIdCatalogo());
                attachedCatalogoCollection.add(catalogoCollectionCatalogoToAttach);
            }
            materia.setCatalogoCollection(attachedCatalogoCollection);
            em.persist(materia);
            for (Catalogo catalogoCollectionCatalogo : materia.getCatalogoCollection()) {
                Materia oldIdMateriaFKOfCatalogoCollectionCatalogo = catalogoCollectionCatalogo.getIdMateriaFK();
                catalogoCollectionCatalogo.setIdMateriaFK(materia);
                catalogoCollectionCatalogo = em.merge(catalogoCollectionCatalogo);
                if (oldIdMateriaFKOfCatalogoCollectionCatalogo != null) {
                    oldIdMateriaFKOfCatalogoCollectionCatalogo.getCatalogoCollection().remove(catalogoCollectionCatalogo);
                    oldIdMateriaFKOfCatalogoCollectionCatalogo = em.merge(oldIdMateriaFKOfCatalogoCollectionCatalogo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateria(materia.getIdMateria()) != null) {
                throw new PreexistingEntityException("Materia " + materia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            Collection<Catalogo> catalogoCollectionOld = persistentMateria.getCatalogoCollection();
            Collection<Catalogo> catalogoCollectionNew = materia.getCatalogoCollection();
            Collection<Catalogo> attachedCatalogoCollectionNew = new ArrayList<Catalogo>();
            for (Catalogo catalogoCollectionNewCatalogoToAttach : catalogoCollectionNew) {
                catalogoCollectionNewCatalogoToAttach = em.getReference(catalogoCollectionNewCatalogoToAttach.getClass(), catalogoCollectionNewCatalogoToAttach.getIdCatalogo());
                attachedCatalogoCollectionNew.add(catalogoCollectionNewCatalogoToAttach);
            }
            catalogoCollectionNew = attachedCatalogoCollectionNew;
            materia.setCatalogoCollection(catalogoCollectionNew);
            materia = em.merge(materia);
            for (Catalogo catalogoCollectionOldCatalogo : catalogoCollectionOld) {
                if (!catalogoCollectionNew.contains(catalogoCollectionOldCatalogo)) {
                    catalogoCollectionOldCatalogo.setIdMateriaFK(null);
                    catalogoCollectionOldCatalogo = em.merge(catalogoCollectionOldCatalogo);
                }
            }
            for (Catalogo catalogoCollectionNewCatalogo : catalogoCollectionNew) {
                if (!catalogoCollectionOld.contains(catalogoCollectionNewCatalogo)) {
                    Materia oldIdMateriaFKOfCatalogoCollectionNewCatalogo = catalogoCollectionNewCatalogo.getIdMateriaFK();
                    catalogoCollectionNewCatalogo.setIdMateriaFK(materia);
                    catalogoCollectionNewCatalogo = em.merge(catalogoCollectionNewCatalogo);
                    if (oldIdMateriaFKOfCatalogoCollectionNewCatalogo != null && !oldIdMateriaFKOfCatalogoCollectionNewCatalogo.equals(materia)) {
                        oldIdMateriaFKOfCatalogoCollectionNewCatalogo.getCatalogoCollection().remove(catalogoCollectionNewCatalogo);
                        oldIdMateriaFKOfCatalogoCollectionNewCatalogo = em.merge(oldIdMateriaFKOfCatalogoCollectionNewCatalogo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            Collection<Catalogo> catalogoCollection = materia.getCatalogoCollection();
            for (Catalogo catalogoCollectionCatalogo : catalogoCollection) {
                catalogoCollectionCatalogo.setIdMateriaFK(null);
                catalogoCollectionCatalogo = em.merge(catalogoCollectionCatalogo);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
