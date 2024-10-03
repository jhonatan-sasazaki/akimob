package br.com.akrasia.akimob.commons.core.client.context;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MultiClientConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;

    public MultiClientConnectionProvider(@Qualifier("client") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection("public");
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        log.debug("Getting connection for schema {}", tenantIdentifier);
        Connection connection = dataSource.getConnection();
        connection.setSchema(tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        log.debug("Releasing connection for schema {}", tenantIdentifier);
        connection.setSchema("public");
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

}
