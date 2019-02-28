package mil.nga.efd.controllers;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mil.nga.efd.domain.AllowedHost;

import com.solers.util.dao.ValidationException;

/**
 * Completely re-wrote to remove dependencies on outdated Hibernate.
 * 
 * @author L. Craig Carpenter
 */
@Repository
public class AllowedHostDAOImpl 
		extends GenericDAOImpl<AllowedHost, Long> implements AllowedHostDAO {

	/**
     * Set up the Log4j system for use throughout the class
     */        
    private static final Logger LOGGER = LoggerFactory.getLogger(
    		AllowedHostDAOImpl.class);
    
	/**
	 * Inject the EntityManager object.
	 */
	@Autowired
	private EntityManager em;
	
	/**
	 * Default no-arg constructor.
	 */
	public AllowedHostDAOImpl() {}
	
	
    /**
     * Retrieve a list of all <code>AllowedHost</code> entities from the target data
     * source.  This method is required by the superclass.
     * 
     * @return A list of all <code>AllowedHost</code> entities in the data source.
     */
    public List<AllowedHost> findAll() {
    	
    	List<AllowedHost> results = null;
    	
    	if (em != null) {
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<AllowedHost> cq = cb.createQuery(AllowedHost.class);
    		Root<AllowedHost> root = cq.from(AllowedHost.class);
    		CriteriaQuery<AllowedHost> all = cq.select(root);
    		TypedQuery<AllowedHost> allQuery = em.createQuery(all);
    		results = allQuery.getResultList();
    	}
    	else {
    		LOGGER.error("The EntityManager object was not injected.  Unable "
				+ "to connect to the target database.  No records selected "
    			+ "from the Alert table.");
    	}
    	return results;
    }
    
	/**
	 * Retrieve the <code>AllowedHost</code> record from the target data 
	 * source that matches the incoming alias.
	 * 
	 * @param alias <code>AllowedHost</code> alias String.
	 */
	@Override
	@SuppressWarnings("unchecked")
    public AllowedHost getByAlias(String alias) {
    	
    	AllowedHost host = null;
    	
    	// Ensure the EntityManager is injected before proceeding
    	if (em != null) {
    		
    		CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AllowedHost> cq = cb.createQuery(AllowedHost.class);
            Root<AllowedHost> root = cq.from(AllowedHost.class);
            
            // Add the "where" clause
            cq.where(
                cb.like(
                    root.get("alias"), 
                    cb.parameter(String.class, "alias")));
            
            // Create the query
            Query query = em.createQuery(cq);
            
            // Set the parameter value for the where clause
            query.setParameter("alias", alias);
            
            List<AllowedHost> results = query.getResultList();
            if (!results.isEmpty()) {
            	host = results.get(0);
            }
            else {
            	if (LOGGER.isDebugEnabled()) { 
            		LOGGER.debug("No results obtained in search for "
            				+ "AllowedHost with alias => [ "
            				+ alias
            				+ " ].");
            	}
            }
    	}
    	else {
    		LOGGER.error("The EntityManager object was not injected.  Unable "
    				+ "to connect to the target database.");
    	}
    	return host;
    }
    
	/**
	 * Check to see if the incoming <code>AllowedHost</code> object already
	 * exists in the target data store.
	 * 
	 * @param allowedHost <code>AllowedHost</code> object to validate.
	 * @throws ValidationException
	 */
    protected void validate(AllowedHost allowedHost) 
    		throws ValidationException {
        
    	ValidationException result = null;
        
        AllowedHost existing = getByAlias(allowedHost.getAlias());
        if (existing != null ) {
            // Original EFD had a bug in this test statement 
        	// (was testing for NOT equals)
        	if (equals(allowedHost.getId(), existing.getId())) {
                if (result == null) {
                    result = new ValidationException();
                }
                result.addMessage("An Allowed host with the given alias already exists");
            }
        }
      
        if (result != null) {
            throw result;
        }
    }
    
    /**
     * Simple method to compare if two IDs are equal.
     * 
     * @param one ID
     * @param two ID to compare.
     * @return True if the IDs are equal.  False otherwise.
     */
    public boolean equals(Long one, Long two) {
        return one != null && two != null && one.equals(two);
    }
}
