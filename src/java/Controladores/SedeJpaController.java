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
import Entidades.Formacion;
import java.util.ArrayList;
import java.util.List;
import Entidades.Aprendiz;
import Entidades.Sede;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Peralta
 */
public class SedeJpaController implements Serializable {

    public SedeJpaController( ) {
         this.emf = Persistence.createEntityManagerFactory("TablasConJSONPU") ;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sede sede) throws PreexistingEntityException, Exception {
        if (sede.getFormacionList() == null) {
            sede.setFormacionList(new ArrayList<Formacion>());
        }
        if (sede.getAprendizList() == null) {
            sede.setAprendizList(new ArrayList<Aprendiz>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Formacion> attachedFormacionList = new ArrayList<Formacion>();
            for (Formacion formacionListFormacionToAttach : sede.getFormacionList()) {
                formacionListFormacionToAttach = em.getReference(formacionListFormacionToAttach.getClass(), formacionListFormacionToAttach.getCodigo());
                attachedFormacionList.add(formacionListFormacionToAttach);
            }
            sede.setFormacionList(attachedFormacionList);
            List<Aprendiz> attachedAprendizList = new ArrayList<Aprendiz>();
            for (Aprendiz aprendizListAprendizToAttach : sede.getAprendizList()) {
                aprendizListAprendizToAttach = em.getReference(aprendizListAprendizToAttach.getClass(), aprendizListAprendizToAttach.getCedula());
                attachedAprendizList.add(aprendizListAprendizToAttach);
            }
            sede.setAprendizList(attachedAprendizList);
            em.persist(sede);
            for (Formacion formacionListFormacion : sede.getFormacionList()) {
                Sede oldSedeCODIGOOfFormacionListFormacion = formacionListFormacion.getSedeCODIGO();
                formacionListFormacion.setSedeCODIGO(sede);
                formacionListFormacion = em.merge(formacionListFormacion);
                if (oldSedeCODIGOOfFormacionListFormacion != null) {
                    oldSedeCODIGOOfFormacionListFormacion.getFormacionList().remove(formacionListFormacion);
                    oldSedeCODIGOOfFormacionListFormacion = em.merge(oldSedeCODIGOOfFormacionListFormacion);
                }
            }
            for (Aprendiz aprendizListAprendiz : sede.getAprendizList()) {
                Sede oldSedeCODIGOOfAprendizListAprendiz = aprendizListAprendiz.getSedeCODIGO();
                aprendizListAprendiz.setSedeCODIGO(sede);
                aprendizListAprendiz = em.merge(aprendizListAprendiz);
                if (oldSedeCODIGOOfAprendizListAprendiz != null) {
                    oldSedeCODIGOOfAprendizListAprendiz.getAprendizList().remove(aprendizListAprendiz);
                    oldSedeCODIGOOfAprendizListAprendiz = em.merge(oldSedeCODIGOOfAprendizListAprendiz);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSede(sede.getCodigo()) != null) {
                throw new PreexistingEntityException("Sede " + sede + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sede sede) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sede persistentSede = em.find(Sede.class, sede.getCodigo());
            List<Formacion> formacionListOld = persistentSede.getFormacionList();
            List<Formacion> formacionListNew = sede.getFormacionList();
            List<Aprendiz> aprendizListOld = persistentSede.getAprendizList();
            List<Aprendiz> aprendizListNew = sede.getAprendizList();
            List<String> illegalOrphanMessages = null;
            for (Formacion formacionListOldFormacion : formacionListOld) {
                if (!formacionListNew.contains(formacionListOldFormacion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Formacion " + formacionListOldFormacion + " since its sedeCODIGO field is not nullable.");
                }
            }
            for (Aprendiz aprendizListOldAprendiz : aprendizListOld) {
                if (!aprendizListNew.contains(aprendizListOldAprendiz)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Aprendiz " + aprendizListOldAprendiz + " since its sedeCODIGO field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Formacion> attachedFormacionListNew = new ArrayList<Formacion>();
            for (Formacion formacionListNewFormacionToAttach : formacionListNew) {
                formacionListNewFormacionToAttach = em.getReference(formacionListNewFormacionToAttach.getClass(), formacionListNewFormacionToAttach.getCodigo());
                attachedFormacionListNew.add(formacionListNewFormacionToAttach);
            }
            formacionListNew = attachedFormacionListNew;
            sede.setFormacionList(formacionListNew);
            List<Aprendiz> attachedAprendizListNew = new ArrayList<Aprendiz>();
            for (Aprendiz aprendizListNewAprendizToAttach : aprendizListNew) {
                aprendizListNewAprendizToAttach = em.getReference(aprendizListNewAprendizToAttach.getClass(), aprendizListNewAprendizToAttach.getCedula());
                attachedAprendizListNew.add(aprendizListNewAprendizToAttach);
            }
            aprendizListNew = attachedAprendizListNew;
            sede.setAprendizList(aprendizListNew);
            sede = em.merge(sede);
            for (Formacion formacionListNewFormacion : formacionListNew) {
                if (!formacionListOld.contains(formacionListNewFormacion)) {
                    Sede oldSedeCODIGOOfFormacionListNewFormacion = formacionListNewFormacion.getSedeCODIGO();
                    formacionListNewFormacion.setSedeCODIGO(sede);
                    formacionListNewFormacion = em.merge(formacionListNewFormacion);
                    if (oldSedeCODIGOOfFormacionListNewFormacion != null && !oldSedeCODIGOOfFormacionListNewFormacion.equals(sede)) {
                        oldSedeCODIGOOfFormacionListNewFormacion.getFormacionList().remove(formacionListNewFormacion);
                        oldSedeCODIGOOfFormacionListNewFormacion = em.merge(oldSedeCODIGOOfFormacionListNewFormacion);
                    }
                }
            }
            for (Aprendiz aprendizListNewAprendiz : aprendizListNew) {
                if (!aprendizListOld.contains(aprendizListNewAprendiz)) {
                    Sede oldSedeCODIGOOfAprendizListNewAprendiz = aprendizListNewAprendiz.getSedeCODIGO();
                    aprendizListNewAprendiz.setSedeCODIGO(sede);
                    aprendizListNewAprendiz = em.merge(aprendizListNewAprendiz);
                    if (oldSedeCODIGOOfAprendizListNewAprendiz != null && !oldSedeCODIGOOfAprendizListNewAprendiz.equals(sede)) {
                        oldSedeCODIGOOfAprendizListNewAprendiz.getAprendizList().remove(aprendizListNewAprendiz);
                        oldSedeCODIGOOfAprendizListNewAprendiz = em.merge(oldSedeCODIGOOfAprendizListNewAprendiz);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sede.getCodigo();
                if (findSede(id) == null) {
                    throw new NonexistentEntityException("The sede with id " + id + " no longer exists.");
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
            Sede sede;
            try {
                sede = em.getReference(Sede.class, id);
                sede.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sede with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Formacion> formacionListOrphanCheck = sede.getFormacionList();
            for (Formacion formacionListOrphanCheckFormacion : formacionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sede (" + sede + ") cannot be destroyed since the Formacion " + formacionListOrphanCheckFormacion + " in its formacionList field has a non-nullable sedeCODIGO field.");
            }
            List<Aprendiz> aprendizListOrphanCheck = sede.getAprendizList();
            for (Aprendiz aprendizListOrphanCheckAprendiz : aprendizListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sede (" + sede + ") cannot be destroyed since the Aprendiz " + aprendizListOrphanCheckAprendiz + " in its aprendizList field has a non-nullable sedeCODIGO field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(sede);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sede> findSedeEntities() {
        return findSedeEntities(true, -1, -1);
    }

    public List<Sede> findSedeEntities(int maxResults, int firstResult) {
        return findSedeEntities(false, maxResults, firstResult);
    }

    private List<Sede> findSedeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sede.class));
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

    public Sede findSede(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sede.class, id);
        } finally {
            em.close();
        }
    }

    public int getSedeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sede> rt = cq.from(Sede.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
