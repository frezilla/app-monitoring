package eu.frezilla.tools.appmonitoring.repository;

import eu.frezilla.tools.appmonitoring.core.DatabaseManager;
import eu.frezilla.tools.appmonitoring.core.PoolVariables;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.List;

import java.util.Objects;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class CRUDRepository<T, ID> {
    
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;
    
    public CRUDRepository(Class<T> entityClass) {
        DatabaseManager databaseManager = (DatabaseManager) Objects.requireNonNull(
                 PoolVariables.getDefault().getObject("databaseManager"),
                "dataBaseManager ne peut pas être null");
        this.sessionFactory = databaseManager.getSessionFactory();
        this.entityClass = Objects.requireNonNull(entityClass, "entityClass ne peut pas être null");
    }
    
    public final long count() {
        try(Session session = sessionFactory.openSession()) {
            var builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            var root = query.from(entityClass);
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        }
    }
    
    public final T create(T entity) {
        Objects.requireNonNull(entity, "entity ne peut pas être null");
        sessionFactory.inTransaction(session -> session.persist(entity));
        return entity;
    }
    
    public final void delete(T entity) {
        Objects.requireNonNull(entity, "entity ne peut pas être null");
        sessionFactory.inTransaction(session -> {
            T managedEntity = session.contains(entity) ? entity : session.merge(entity);
            session.remove(managedEntity);
        });
    }
    
    public final boolean deleteById(ID id) {
        Objects.requireNonNull(id, "id ne peut pas être null");
        return sessionFactory.fromTransaction(session -> {
           T entity = session.find(entityClass, id);
           if (entity == null) {
               return false;
           }
           session.remove(entity);
           return true;
        });
    }
    
    public final boolean existById(ID id) {
        return findById(id).isEmpty();
    }
    
    public final List<T> findAll() {
        try(Session session = sessionFactory.openSession()) {
            var builder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = builder.createQuery(entityClass);
            query.from(entityClass);
            return session.createQuery(query).getResultList();
        }
    }
            
    public final Optional<T> findById(ID id) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.find(
                            entityClass,
                            id
                    )
            );
        }
    }
    
    protected final Class<T> getEntityClass() {
        return entityClass;
    }

    protected final SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public final T update(T entity) {
        Objects.requireNonNull(entity);
        return sessionFactory.fromTransaction(session -> session.merge(entity));
    }
    
}
