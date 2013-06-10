package org.agoncal.application.petstore.util;

import javax.annotation.sql.DataSourceDefinition;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
@DataSourceDefinition(
        className = "org.apache.derby.jdbc.EmbeddedDataSource",
        name = "jdbc/petstoreDS",
        user = "app",
        password = "app",
        databaseName = "applicationPetstoreDB",
        properties = {"connectionAttributes=;create=true"}
)
public class DerbyDataSourceProducer {
}
