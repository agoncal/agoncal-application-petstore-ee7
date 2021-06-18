# 03C - Bind the application to a SQL database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Bind the application to the petstore database in Azure SQL Database.

---

## Configure SQL Database Data Source

There are 4 steps to configure a data source. These steps are similar to configuring data sources
in any on premise Java EE app servers:

### Step 1: Understand How to configure JBoss EAP

In App Service, each instance of an app server is stateless. Therefore, each instance must be
configured on startup to support a JBoss EAP configuration needed by your application. You can configure at
startup by supplying a startup Bash script that calls [JBoss/WildFly CLI commands](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface) to setup data sources, messaging
 providers and any other dependencies. We will create a startup.sh script and place it in the `/home`
 directory of the Web app. The script will:

Install a JBoss EAP module:

```text
# where resources point to JDBC driver for SQL Database
# and module xml points to module description, see below

module add --name=com.microsoft --resources=/home/site/libs/mssql-jdbc-7.2.1.jre8.jar --module-xml=/home/site/scripts/mssql-module.xml
```
Where `mssql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="com.microsoft">
  <resources>
	<resource-root path="/home/site/libs/mssql-jdbc-7.2.1.jre8.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```

Add a JDBC driver for SQL Database:

```text
/subsystem=datasources/jdbc-driver=sqlserver:add(driver-name="sqlserver",driver-module-name="com.microsoft",driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver,driver-datasource-class-name=com.microsoft.sqlserver.jdbc.SQLServerDataSource)
```

Install a data source by using the data-source shortcut command:

```text
data-source add --name=sqlDS --jndi-name=java:jboss/datasources/sqlDS --driver-name=sqlserver --connection-url=${SQL_CONNECTION_URL,env.SQL_CONNECTION_URL:example} --validate-on-match=true --background-validation=false --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLExceptionSorter
```

A server reload may be required for the changes to take effect:

```text
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for SQL Database and module XML are available in
[initial-sql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-sql/agoncal-application-petstore-ee7/.scripts)

Also, you can directly download [JDBC driver for SQL Database](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server). For example:

### Step 2: Deploy multiple artifacts to App Service linux

Open `pom.xml` and update the `deployment` with the following configuration and run `mvn azure-webapp:deploy` to deploy.

```xml
<deployment>
  <resources>
    <resource>
      <type>war</type>
      <directory>${project.basedir}/target</directory>
      <includes>
        <include>*.war</include>
      </includes>
    </resource>
    <resource>
      <type>lib</type>
      <directory>${project.basedir}/.scripts/3C-sql</directory>
      <includes>
        <include>*.jar</include>
      </includes>
    </resource>
    <resource>
      <type>startup</type>
      <directory>${project.basedir}/.scripts/3C-sql</directory>
      <includes>
        <include>*.sh</include>
      </includes>
    </resource>
    <resource>
      <type>script</type>
      <directory>${project.basedir}/.scripts/3C-sql</directory>
      <includes>
        <include>*.cli</include>
        <include>*.xml</include>
      </includes>
    </resource>
  </resources>
</deployment>
```


### Step 3: Set SQL Database connection info in the Web app environment

Use Azure CLI to set database connection info:

```bash
az webapp config appsettings set  \
    --resource-group ${RESOURCE_GROUP} \
    --name ${WEBAPP}  \
    --settings  SQL_CONNECTION_URL=${SQL_CONNECTION_URL}
```
```text
[
  {
    "name": "WEBSITE_HTTPLOGGING_RETENTION_DAYS",
    "slotSetting": false,
    "value": "3"
  },
  {
    "name": "SQL_CONNECTION_URL",
    "slotSetting": false,
    "value": "======= MASKED ======="
  }
]
```

```bash
az webapp config set --startup-file /home/site/scripts/startup.sh \
    --resource-group ${RESOURCE_GROUP} --name ${WEBAPP}
```

>🚧 - __Preview-specific__. Using Azure CLI to set App Settings and startup batch file
 is only necessary while JBoss EAP on App Service is in preview. Soon, the
[Maven Plugin for Azure App Service](https://github.com/Microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/README.md)
will integrate these operations into the popular one-step deploy, `mvn azure-webapp:deploy`.

### Step 4: Restart the remote JBoss EAP app server

Use Azure CLI to restart the remote JBoss EAP app server:

```bash
az webapp stop -g ${RESOURCE_GROUP} -n ${WEBAPP}
az webapp start -g ${RESOURCE_GROUP} -n ${WEBAPP}
```

For additional info, please refer to:

- [JBoss Data Source Management](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/datasource_management).
- [JBoss/WildFly CLI Guide](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface)
- [Open SSH session from your development machine to App Service Linux](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell)


## Build PetStore to interact with Azure SQL Database

```bash
# Use the Maven profile for Azure SQL Database to build from the project base directory
cd ../..
mvn package -Dmaven.test.skip=true -Ddb=sql
```

Note - the `sql` Maven profile is available [here](../../pom.xml#L471).

## Deploy to App Service Linux

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
```

## Open Pet Store running on App Service Linux and interacting with Azure SQL Database

```bash
open https://${WEBAPP}.azurewebsites.net
```

![](../../step-01-deploy-java-ee-app-to-azure/media/YAPS-PetStore-H2.jpg)

## Log into Azure SQL Database and Validate Tables were Created and Populated

```bash
# Show tables in SQL Database
sqlcmd -S ${SQL_SERVER_FULL_NAME} \
    -d ${SQL_DATABASE_NAME} \
    -U ${SQL_SERVER_ADMIN_FULL_NAME} \
    -P ${SQL_SERVER_ADMIN_PASSWORD} \
    -Q "SELECT name, id, crdate FROM SYSOBJECTS WHERE xtype = 'U'"

--------------------- ----------- -----------------------
Category                 18099105 2019-05-31 14:41:52.280
Country                  50099219 2019-05-31 14:41:52.320
Customer                 82099333 2019-05-31 14:41:52.330
Item                    114099447 2019-05-31 14:41:52.347
order_line              146099561 2019-05-31 14:41:52.373
Product                 194099732 2019-05-31 14:41:52.390
purchase_order          226099846 2019-05-31 14:41:52.427
t_order_order_line      258099960 2019-05-31 14:41:52.483

(8 rows affected)

# Show contents in category table

sqlcmd -S ${SQL_SERVER_FULL_NAME} \
    -d ${SQL_DATABASE_NAME} \
    -U ${SQL_SERVER_ADMIN_FULL_NAME} \
    -P ${SQL_SERVER_ADMIN_PASSWORD} \
    -Q "select name from category"

name
------------------------------
Fish
Dogs
Reptiles
Cats
Birds

(5 rows affected)

```

## Troubleshoot Java EE application on Azure by viewing logs

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP} --resource-group ${RESOURCE_GROUP}
```
---

⬅️ Previous guide: [02 - Create a database](../../step-02-create-a-database/README.md)

➡️ Next guide: [04 - Monitor Java EE application](../../step-04-monitor-java-ee-app/README.md)