package org.agoncal.application.petstore.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Temporary hack to create <code>cb_users</code> and <code>cb_groups</code> views.
 *
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
@Singleton
@Startup
@DependsOn("DBPopulator")
public class CloudBeesAuthRealmConfigurer {
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Resource(lookup = "jdbc/petstore")
    private DataSource dataSource;

    @PostConstruct
    public void postConstruct() {
        Connection cnn = null;

        try {
            cnn = dataSource.getConnection();
            testDatabaseConnectivity(cnn);

            if (tableOrViewExists("cb_users", cnn)) {
                logger.info("table/view 'cb_users' already exists");
            } else {
                logger.info("create view 'cb_users'");
                String sql = "create view cb_users as \n" +
                        "select LOGIN as `username`,PASSWORD as `password` from CUSTOMER";
                executeStatement(sql, cnn);
            }

            if (tableOrViewExists("cb_groups", cnn)) {
                logger.info("table/view 'cb_users' already exists");
            } else {
                logger.info("create view 'cb_groups'");

                String sql = "create or replace view cb_groups as \n" +
                        "select LOGIN as `username`, 'user' as `groupname` from CUSTOMER";
                executeStatement(sql, cnn);

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception initializing auth-realm", e);
        } finally {
            closeQuietly(cnn);
        }
    }

    private void executeStatement(String sql, Connection cnn) {
        Statement stmt = null;
        try {
            stmt = cnn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Exception executing '" + sql + "'", e);
        } finally {
            closeQuietly(stmt);
        }
    }

    private void testDatabaseConnectivity(Connection cnn) {
        try {
            executeStatement("select 1", cnn);
        } catch (RuntimeException e) {
            throw new RuntimeException("Database connectivity failure. " +
                    "Please ensure that the 'jdbc/petstore' JNDI DataSource has been configured in GlassFish. " +
                    "On CloudBees platform, please use 'bees app:bind -a myapp -db mydb -as petstore' to link your DB", e);
        }
    }

    private boolean tableOrViewExists(String tableName, Connection cnn) {
        Statement stmt = null;
        try {
            stmt = cnn.createStatement();
            stmt.execute("select * from " + tableName + " where 0=1");
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            closeQuietly(stmt);
        }
    }

    private void closeQuietly(Statement stmt) {
        if (stmt == null) {
            return;
        }
        try {
            stmt.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Ignore exception closing quietly statement", e);
        }
    }

    private void closeQuietly(Connection cnn) {
        if (cnn == null) {
            return;
        }
        try {
            cnn.close();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Ignore exception closing quietly connection", e);
        }
    }

}
