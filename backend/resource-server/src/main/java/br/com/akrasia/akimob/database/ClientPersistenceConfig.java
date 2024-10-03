package br.com.akrasia.akimob.database;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.akrasia.akimob.commons.core.client.context.CurrentClientIdentifierResolver;
import br.com.akrasia.akimob.commons.core.client.context.MultiClientConnectionProvider;
import jakarta.persistence.EntityManagerFactory;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = {
        "br.com.akrasia.akimob.app",
        "br.com.akrasia.akimob.commons.app" }, entityManagerFactoryRef = "clientEntityManagerFactory", transactionManagerRef = "clientTransactionManager")
public class ClientPersistenceConfig {

    private final JpaProperties jpaProperties;
    private final String[] entityPackages;
    private final CurrentClientIdentifierResolver currentClientIdentifierResolver;
    private final MultiClientConnectionProvider multiClientConnectionProvider;

    public ClientPersistenceConfig(
            @Value("${akimob.client.repository.packages}") String[] entityPackages,
            JpaProperties jpaProperties,
            CurrentClientIdentifierResolver currentClientIdentifierResolver,
            MultiClientConnectionProvider multiClientConnectionProvider) {

        this.entityPackages = entityPackages;
        this.jpaProperties = jpaProperties;
        this.currentClientIdentifierResolver = currentClientIdentifierResolver;
        this.multiClientConnectionProvider = multiClientConnectionProvider;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean clientEntityManagerFactory(
            @Qualifier("client") DataSource clientDataSource) {

        Map<String, Object> properties = getJpaProperties();

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, properties, null);

        return builder.dataSource(clientDataSource).packages(entityPackages).persistenceUnit("client-persistence-unit")
                .build();
    }

    @Bean
    public PlatformTransactionManager clientTransactionManager(
            @Qualifier("clientEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Map<String, Object> getJpaProperties() {

        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.remove(AvailableSettings.DEFAULT_SCHEMA);
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiClientConnectionProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, currentClientIdentifierResolver);

        return properties;
    }

}
