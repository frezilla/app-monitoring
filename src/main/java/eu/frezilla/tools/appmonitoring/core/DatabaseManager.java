package eu.frezilla.tools.appmonitoring.core;

import eu.frezilla.tools.appmonitoring.domain.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseManager implements AutoCloseable {
    
    private static final int TIMEOUT = 10;
    
    private HikariDataSource dataSource;
    
    private SessionFactory sessionFactory;

    private StandardServiceRegistry registry;

    public void connect(ConnectionParameters parameters) throws SQLException {
        close();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(parameters.getJdbcUrl());
        config.setUsername(parameters.username());
        config.setPassword(parameters.password());
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(5000);
        config.setValidationTimeout(3000);
        config.setPoolName("MyPool");
        dataSource = new HikariDataSource(config);
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(TIMEOUT)) {
                throw new SQLException("La connexion n'est pas valide.");
            }
        }
        
        // Hibernate
        registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.datasource", dataSource)
                .applySetting("hibernate.show_sql", false)
                .applySetting("hibernate.format_sql", true)
                .applySetting("hibernate.hbm2ddl.auto", "validate")
                .build();
        
        try {
            sessionFactory = 
                    new MetadataSources(registry)
                            .addAnnotatedClass(User.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            registry = null;
            dataSource.close();
            dataSource = null;
            throw e;
        }        
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("Hibernate n'est pas initialisé.");
        }
        return sessionFactory;
    }

    @Override
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }

        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
            registry = null;
        }

        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }
}
