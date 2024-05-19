/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Aprendiz;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Formacion;
import Entidades.Sede;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Peralta
 */
public class AprendizJpaController implements Serializable {

    public AprendizJpaController( ) {
        this.emf = Persistence.createEntityManagerFactory("TablasConJSONPU") ;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Aprendiz aprendiz) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Formacion formacionCodigo = aprendiz.getFormacionCodigo();
            if (formacionCodigo != null) {
                formacionCodigo = em.getReference(formacionCodigo.getClass(), formacionCodigo.getCodigo());
                aprendiz.setFormacionCodigo(formacionCodigo);
            }
            Sede sedeCODIGO = aprendiz.getSedeCODIGO();
            if (sedeCODIGO != null) {
                sedeCODIGO = em.getReference(sedeCODIGO.getClass(), sedeCODIGO.getCodigo());
                aprendiz.setSedeCODIGO(sedeCODIGO);
            }
            em.persist(aprendiz);
            if (formacionCodigo != null) {
                formacionCodigo.getAprendizList().add(aprendiz);
                formacionCodigo = em.merge(formacionCodigo);
            }
            if (sedeCODIGO != null) {
                sedeCODIGO.getAprendizList().add(aprendiz);
                sedeCODIGO = em.merge(sedeCODIGO);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAprendiz(aprendiz.getCedula()) != null) {
                throw new PreexistingEntityException("Aprendiz " + aprendiz + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Aprendiz aprendiz) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Aprendiz persistentAprendiz = em.find(Aprendiz.class, aprendiz.getCedula());
            Formacion formacionCodigoOld = persistentAprendiz.getFormacionCodigo();
            Formacion formacionCodigoNew = aprendiz.getFormacionCodigo();
            Sede sedeCODIGOOld = persistentAprendiz.getSedeCODIGO();
            Sede sedeCODIGONew = aprendiz.getSedeCODIGO();
            if (formacionCodigoNew != null) {
                formacionCodigoNew = em.getReference(formacionCodigoNew.getClass(), formacionCodigoNew.getCodigo());
                aprendiz.setFormacionCodigo(formacionCodigoNew);
            }
            if (sedeCODIGONew != null) {
                sedeCODIGONew = em.getReference(sedeCODIGONew.getClass(), sedeCODIGONew.getCodigo());
                aprendiz.setSedeCODIGO(sedeCODIGONew);
            }
            aprendiz = em.merge(aprendiz);
            if (formacionCodigoOld != null && !formacionCodigoOld.equals(formacionCodigoNew)) {
                formacionCodigoOld.getAprendizList().remove(aprendiz);
                formacionCodigoOld = em.merge(formacionCodigoOld);
            }
            if (formacionCodigoNew != null && !formacionCodigoNew.equals(formacionCodigoOld)) {
                formacionCodigoNew.getAprendizList().add(aprendiz);
                formacionCodigoNew = em.merge(formacionCodigoNew);
            }
            if (sedeCODIGOOld != null && !sedeCODIGOOld.equals(sedeCODIGONew)) {
                sedeCODIGOOld.getAprendizList().remove(aprendiz);
                sedeCODIGOOld = em.merge(sedeCODIGOOld);
            }
            if (sedeCODIGONew != null && !sedeCODIGONew.equals(sedeCODIGOOld)) {
                sedeCODIGONew.getAprendizList().add(aprendiz);
                sedeCODIGONew = em.merge(sedeCODIGONew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = aprendiz.getCedula();
                if (findAprendiz(id) == null) {
                    throw new NonexistentEntityException("The aprendiz with id " + id + " no longer exists.");
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
            Aprendiz aprendiz;
            try {
                aprendiz = em.getReference(Aprendiz.class, id);
                aprendiz.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The aprendiz with id " + id + " no longer exists.", enfe);
            }
            Formacion formacionCodigo = aprendiz.getFormacionCodigo();
            if (formacionCodigo != null) {
                formacionCodigo.getAprendizList().remove(aprendiz);
                formacionCodigo = em.merge(formacionCodigo);
            }
            Sede sedeCODIGO = aprendiz.getSedeCODIGO();
            if (sedeCODIGO != null) {
                sedeCODIGO.getAprendizList().remove(aprendiz);
                sedeCODIGO = em.merge(sedeCODIGO);
            }
            em.remove(aprendiz);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Aprendiz> findAprendizEntities() {
        return findAprendizEntities(true, -1, -1);
    }

    public List<Aprendiz> findAprendizEntities(int maxResults, int firstResult) {
        return findAprendizEntities(false, maxResults, firstResult);
    }

    private List<Aprendiz> findAprendizEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Aprendiz.class));
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

    public Aprendiz findAprendiz(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Aprendiz.class, id);
        } finally {
            em.close();
        }
    }

    public int getAprendizCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Aprendiz> rt = cq.from(Aprendiz.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
