package br.com.akrasia.akimob.database.flyway;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FlywayService {

    private final DataSource clienDataSource;

    public FlywayService(@Qualifier("client") DataSource clientDataSource) {
        this.clienDataSource = clientDataSource;
    }

    public void initNewClientSchema(String schema) {

        Flyway clientMigration = Flyway.configure()
                .dataSource(clienDataSource)
                .locations("classpath:db/migration/client")
                .target(MigrationVersion.LATEST)
                .baselineOnMigrate(true)
                .schemas(schema)
                .load();

        clientMigration.migrate();

    }

}
