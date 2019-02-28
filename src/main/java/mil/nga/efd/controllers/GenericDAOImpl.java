package mil.nga.efd.controllers;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.solers.util.dao.ValidationException;

/**
 * GenericHibernateDAO implements the basic CRUD operations for Hibernate/JPA. 
 * This class should be used as a base class for all Hibernate/JPA  
 * implementations of persistent entity DAOs. 
 * 
 * This class was re-written from it's original implementation to support 
 * Spring  and JPA (vice proprietary Hibernate).
 * 
 * @author L. Craig Carpenter
 */
@Repository
public abstract class GenericDAOImpl<T, ID extends Serializable> 
		implements GenericDAO<T, ID> {

	/**
     * Set up the Log4j system for use throughout the class
     */        
    private static final Logger LOGGER = LoggerFactory.getLogger(
    		GenericDAOImpl.class);
    
    /**
     * Injected JPA EntityManager object used to actually obtain a database 
     * connection.
     */
    @PersistenceContext
    protected EntityManager em;
    
    /**
     * Entity class type
     */
    private Class<T> entityClass;

    /**
     * Used to perform validation of Entity classes
     */
    private Validator validator;
    
    /**
     * Default constructor extracting the superclass type.
     */
    @SuppressWarnings("unchecked")
	public GenericDAOImpl() {
    	ParameterizedType genericSuperclass = 
    			(ParameterizedType)getClass().getGenericSuperclass();
        this.entityClass =  (Class<T>)genericSuperclass.getActualTypeArguments()[0];
    }
    
    /**
     * Alternate constructor allowing subclasses to specify the entity class
     * type on construction.
     * 
     * @param entityClass The Entity type.
     */
    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Accessor method for the EntityClass type.
     * @return The EntityClass type.
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
    
    /**
     * Accessor method for the Validation object.
     * @return The object used for Entity validation.
     */
    protected Validator getValidator() {
    	if (this.validator == null) {
    		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            this.validator = factory.getValidator();
    	}
    	return this.validator;
    }

    /**
     * Retrieve an Entity object by it's ID.
     * @param id The ID of the Entity to retrieve from the target DB.
     * @param lock If true the data source is locked for read.
     */
    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity = null;
        if (lock) {
        	entity = em.find(getEntityClass(), id, LockModeType.READ);
        }
        else {
        	entity = em.find(getEntityClass(), id);
        }
        return entity;
    }


    /**
     * Abstract method requiring subclasses to implement a type-safe findAll 
     * method.
     */
    public abstract List<T> findAll();



    public T persist(T entity) {
    	if (em != null) {
    		validateEntity(entity);
    		em.persist(entity);
        }
        else {
        	throw new IllegalStateException("EntityManager was not injected.");
        }
        return entity;
    }

    @Override
    public void remove(T entity) {
    	if (em != null) {
        	em.remove(entity);
        }
        else {
        	throw new IllegalStateException("EntityManager was not injected.");
        }
    }
    
    public void removeById(Collection<ID> ids) {
        for (ID id : ids) {
            T entity = findById(id, false);
            if (entity != null) {
                remove(entity);
            }
        }
    }

    
    /**
     * Flush changes to the target data source.
     */
    public void flush() {
        if (em != null) {
        	em.flush();
        }
        else {
        	throw new IllegalStateException("EntityManager was not injected.");
        }
    }

    /**
     * Clear the <code>EntityManager<code> object.
     */
    public void clear() {
    	if (em != null) {
    		em.clear();
    	}
    	else {
    		LOGGER.error("Unable to clear the EntityManager.  The "
    				+ "EntityManager was not injected.");
    	}
    }
    
    /**
     * This method is the reason that we kept the <code>GenericDAOImpl</code>
     * code.  This method runs the validation checks against any entity before 
     * it goes into the backing data store.
     * 
     * @param entity The entity to validate.
     * @throws com.solers.util.dao.ValidationException Thrown if the input 
     * Entity class fails any validation checks.
     */
    protected void validateEntity(T entity) {
    	if (getValidator() != null) {
    		Set<ConstraintViolation<T>> violations = validator.validate(entity);
    		if (violations.size() > 0) {
    			ValidationException ex = new ValidationException();
    			for (ConstraintViolation<T> v : violations) {
    				ex.addMessage(v.getMessage());
    			}
    			throw ex;
    		}
    	}
    }
}
