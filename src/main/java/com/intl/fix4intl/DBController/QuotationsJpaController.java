/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.DBController;

import com.intl.fix4intl.Model.Quotations;
import com.intl.fix4intl.exceptions.NonexistentEntityException;
import com.intl.fix4intl.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author mar
 */
public class QuotationsJpaController implements Serializable {

    private EntityManagerFactory emf = null;
    public QuotationsJpaController() {
        this.emf = Persistence.createEntityManagerFactory("intl_fix_connection");
//        this.emf = Persistence.createEntityManagerFactory("fix_mysql");
    } 

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Quotations quotations) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(quotations);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findQuotations(quotations.getSymbol()) != null) {
                throw new PreexistingEntityException("Quotations " + quotations + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void createIfnotExist(Quotations quotations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            if (findQuotations(quotations.getSymbol()) != null) {
                edit(quotations);
            }else{
                create(quotations);
            }           
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());        
        } 
    }
    
    public void edit(Quotations quotations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            quotations = em.merge(quotations);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = quotations.getSymbol();
                if (findQuotations(id) == null) {
                    throw new NonexistentEntityException("The quotations with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Quotations quotations;
            try {
                quotations = em.getReference(Quotations.class, id);
                quotations.getSymbol();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The quotations with id " + id + " no longer exists.", enfe);
            }
            em.remove(quotations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Quotations> findQuotationsEntities() {
        return findQuotationsEntities(true, -1, -1);
    }

    public List<Quotations> findQuotationsEntities(int maxResults, int firstResult) {
        return findQuotationsEntities(false, maxResults, firstResult);
    }

    private List<Quotations> findQuotationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Quotations.class));
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

    public Quotations findQuotations(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Quotations.class, id);
        } finally {
            em.close();
        }
    }

    public int getQuotationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Quotations> rt = cq.from(Quotations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
