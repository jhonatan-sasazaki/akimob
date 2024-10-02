package br.com.akrasia.akimob.commons.database;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = {
        "${akimob.main.repository.packages}" }, entityManagerFactoryRef = "mainEntityManagerFactory", transactionManagerRef = "mainTransactionManager")
public class MainPersistenceConfig {

    private final JpaProperties jpaProperties;
    private final String entityPackages;

    public MainPersistenceConfig(JpaProperties jpaProperties,
            @Value("${akimob.main.repository.packages}") String entityPackages) {
        this.jpaProperties = jpaProperties;
        this.entityPackages = entityPackages;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(DataSource mainDataSource) {
        Map<String, String> properties = getJpaProperties();
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter, properties, null);
        return builder.dataSource(mainDataSource).packages(entityPackages).persistenceUnit("main-persistence-unit")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager mainTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Map<String, String> getJpaProperties() {
        Map<String, String> properties = jpaProperties.getProperties();
        return properties;
    }
}
