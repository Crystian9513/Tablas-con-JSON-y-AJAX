/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Sede;
import Entidades.Aprendiz;
import Entidades.Formacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Peralta
 */
public class FormacionJpaController implements Serializable {

    public FormacionJpaController( ) {
         this.emf = Persistence.createEntityManagerFactory("TablasConJSONPU") ;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Formacion formacion) throws PreexistingEntityException, Exception {
        if (formacion.getAprendizList() == null) {
            formacion.setAprendizList(new ArrayList<Aprendiz>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sede sedeCODIGO = formacion.getSedeCODIGO();
            if (sedeCODIGO != null) {
                sedeCODIGO = em.getReference(sedeCODIGO.getClass(), sedeCODIGO.getCodigo());
                formacion.setSedeCODIGO(sedeCODIGO);
            }
            List<Aprendiz> attachedAprendizList = new ArrayList<Aprendiz>();
            for (Aprendiz aprendizListAprendizToAttach : formacion.getAprendizList()) {
                aprendizListAprendizToAttach = em.getReference(aprendizListAprendizToAttach.getClass(), aprendizListAprendizToAttach.getCedula());
                attachedAprendizList.add(aprendizListAprendizToAttach);
            }
            formacion.setAprendizList(attachedAprendizList);
            em.persist(formacion);
            if (sedeCODIGO != null) {
                sedeCODIGO.getFormacionList().add(formacion);
                sedeCODIGO = em.merge(sedeCODIGO);
            }
            for (Aprendiz aprendizListAprendiz : formacion.getAprendizList()) {
                Formacion oldFormacionCodigoOfAprendizListAprendiz = aprendizListAprendiz.getFormacionCodigo();
                aprendizListAprendiz.setFormacionCodigo(formacion);
                aprendizListAprendiz = em.merge(aprendizListAprendiz);
                if (oldFormacionCodigoOfAprendizListAprendiz != null) {
                    oldFormacionCodigoOfAprendizListAprendiz.getAprendizList().remove(aprendizListAprendiz);
                    oldFormacionCodigoOfAprendizListAprendiz = em.merge(oldFormacionCodigoOfAprendizListAprendiz);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFormacion(formacion.getCodigo()) != null) {
                throw new PreexistingEntityException("Formacion " + formacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Formacion formacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Formacion persistentFormacion = em.find(Formacion.class, formacion.getCodigo());
            Sede sedeCODIGOOld = persistentFormacion.getSedeCODIGO();
            Sede sedeCODIGONew = formacion.getSedeCODIGO();
            List<Aprendiz> aprendizListOld = persistentFormacion.getAprendizList();
            List<Aprendiz> aprendizListNew = formacion.getAprendizList();
            List<String> illegalOrphanMessages = null;
            for (Aprendiz aprendizListOldAprendiz : aprendizListOld) {
                if (!aprendizListNew.contains(aprendizListOldAprendiz)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aprendiz " + aprendizListOldAprendiz + " since its formacionCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sedeCODIGONew != null) {
                sedeCODIGONew = em.getReference(sedeCODIGONew.getClass(), sedeCODIGONew.getCodigo());
                formacion.setSedeCODIGO(sedeCODIGONew);
            }
            List<Aprendiz> attachedAprendizListNew = new ArrayList<Aprendiz>();
            for (Aprendiz aprendizListNewAprendizToAttach : aprendizListNew) {
                aprendizListNewAprendizToAttach = em.getReference(aprendizListNewAprendizToAttach.getClass(), aprendizListNewAprendizToAttach.getCedula());
                attachedAprendizListNew.add(aprendizListNewAprendizToAttach);
            }
            aprendizListNew = attachedAprendizListNew;
            formacion.setAprendizList(aprendizListNew);
            formacion = em.merge(formacion);
            if (sedeCODIGOOld != null && !sedeCODIGOOld.equals(sedeCODIGONew)) {
                sedeCODIGOOld.getFormacionList().remove(formacion);
                sedeCODIGOOld = em.merge(sedeCODIGOOld);
            }
            if (sedeCODIGONew != null && !sedeCODIGONew.equals(sedeCODIGOOld)) {
                sedeCODIGONew.getFormacionList().add(formacion);
                sedeCODIGONew = em.merge(sedeCODIGONew);
            }
            for (Aprendiz aprendizListNewAprendiz : aprendizListNew) {
                if (!aprendizListOld.contains(aprendizListNewAprendiz)) {
                    Formacion oldFormacionCodigoOfAprendizListNewAprendiz = aprendizListNewAprendiz.getFormacionCodigo();
                    aprendizListNewAprendiz.setFormacionCodigo(formacion);
                    aprendizListNewAprendiz = em.merge(aprendizListNewAprendiz);
                    if (oldFormacionCodigoOfAprendizListNewAprendiz != null && !oldFormacionCodigoOfAprendizListNewAprendiz.equals(formacion)) {
                        oldFormacionCodigoOfAprendizListNewAprendiz.getAprendizList().remove(aprendizListNewAprendiz);
                        oldFormacionCodigoOfAprendizListNewAprendiz = em.merge(oldFormacionCodigoOfAprendizListNewAprendiz);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = formacion.getCodigo();
                if (findFormacion(id) == null) {
                    throw new NonexistentEntityException("The formacion with id " + id + " no longer exists.");
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
            Formacion formacion;
            try {
                formacion = em.getReference(Formacion.class, id);
                formacion.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Aprendiz> aprendizListOrphanCheck = formacion.getAprendizList();
            for (Aprendiz aprendizListOrphanCheckAprendiz : aprendizListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Formacion (" + formacion + ") cannot be destroyed since the Aprendiz " + aprendizListOrphanCheckAprendiz + " in its aprendizList field has a non-nullable formacionCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Sede sedeCODIGO = formacion.getSedeCODIGO();
            if (sedeCODIGO != null) {
                sedeCODIGO.getFormacionList().remove(formacion);
                sedeCODIGO = em.merge(sedeCODIGO);
            }
            em.remove(formacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Formacion> findFormacionEntities() {
        return findFormacionEntities(true, -1, -1);
    }

    public List<Formacion> findFormacionEntities(int maxResults, int firstResult) {
        return findFormacionEntities(false, maxResults, firstResult);
    }

    private List<Formacion> findFormacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Formacion.class));
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

    public Formacion findFormacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Formacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Formacion> rt = cq.from(Formacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
