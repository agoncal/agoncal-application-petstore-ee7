# 03A - Bind the application to a PostgreSQL database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Bind the application to the petstore database in Azure Database for PostgreSQL.

---

## Configure PostgreSQL Data Source

There are 4 steps to configure a data source. These steps are similar to configuring data sources
in any on premise Java EE app servers:

### Step 1: Understand how to configure JBoss EAP

In App Service, each instance of an app server is stateless. Therefore, each instance must be
configured on startup to support a JBoss EAP configuration needed by your application. You can configure at
startup by supplying a startup Bash script that calls [JBoss/WildFly CLI commands](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface) to setup data sources, messaging
 providers and any other dependencies. We will create a startup.sh script and place it in the `/home`
 directory of the Web app. The script will:

Install a JBoss EAP module:

```text
# where resources point to JDBC driver for PostgreSQL
# and module xml points to module description, see below

module add --name=org.postgres --resources=/home/site/libs/postgresql-42.2.5.jar --module-xml=/home/site/scripts/postgresql-module.xml
```
Where `postgresql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="org.postgres">
    <resources>
     <!-- ***** IMPORTANT : PATH should point to PostgreSQL Java driver on App Service Linux *******-->
       <resource-root path="/home/site/libs/postgresql-42.2.5.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

Add a JDBC driver for PostgreSQL:

```text
/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
```

Install a data source by using the data-source shortcut command:

```text
data-source add --name=postgresDS --driver-name=postgres --jndi-name=java:jboss/datasources/postgresDS --connection-url=${POSTGRES_CONNECTION_URL,env.POSTGRES_CONNECTION_URL:jdbc:postgresql://db:5432/postgres} --user-name=${POSTGRES_SERVER_ADMIN_FULL_NAME,env.POSTGRES_SERVER_ADMIN_FULL_NAME:postgres} --password=${POSTGRES_SERVER_ADMIN_PASSWORD,env.POSTGRES_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=org.postgresql.Driver --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter --jta=true --use-java-context=true --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker
```

A server reload may be required for the changes to take effect:

```text
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for PostgreSQL and module XML are available in
[initial-postgresql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-postgresql/agoncal-application-petstore-ee7/.scripts)

Also, you can directly download the latest version of [JDBC driver for PostgreSQL](https://jdbc.postgresql.org/download.html)

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
      <directory>${project.basedir}/.scripts/3A-postgresql</directory>
      <includes>
        <include>*.jar</include>
      </includes>
    </resource>
    <resource>
      <type>startup</type>
      <directory>${project.basedir}/.scripts/3A-postgresql</directory>
      <includes>
        <include>*.sh</include>
      </includes>
    </resource>
    <resource>
      <type>script</type>
      <directory>${project.basedir}/.scripts/3A-postgresql</directory>
      <includes>
        <include>*.cli</include>
        <include>*.xml</include>
      </includes>
    </resource>
  </resources>
</deployment>
```

### Step 3: Set PostgreSQL database connection info in the Web app environment

Use Azure CLI to set database connection info:

```bash
az webapp config appsettings set \
    --resource-group ${RESOURCE_GROUP} --name ${WEBAPP} \
    --settings \
    POSTGRES_CONNECTION_URL=${POSTGRES_CONNECTION_URL} \
    POSTGRES_SERVER_ADMIN_PASSWORD=${POSTGRES_SERVER_ADMIN_PASSWORD} \
    POSTGRES_SERVER_ADMIN_FULL_NAME=${POSTGRES_SERVER_ADMIN_FULL_NAME}
```
```text
[
 {
   "name": "WEBSITE_HTTPLOGGING_RETENTION_DAYS",
   "slotSetting": false,
   "value": "3"
 },
 {
   "name": "POSTGRES_CONNECTION_URL",
   "slotSetting": false,
   "value": "jdbc:postgresql://petstore-db.postgres.database.azure.com:5432/postgres?ssl=true"
 },
 {
   "name": "POSTGRES_SERVER_ADMIN_PASSWORD",
   "slotSetting": false,
   "value": "======= MASKED ======="
 },
 {
   "name": "POSTGRES_SERVER_ADMIN_FULL_NAME",
   "slotSetting": false,
   "value": "postgres@petstore-db"
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


## Build Java EE application to interact with Azure Database for PostgreSQL

```bash
# Use the Maven profile for PostgreSQL to build from the project base directory
cd ../..
mvn package -Dmaven.test.skip=true -Ddb=postgresql
```

Note - the `postgresql` Maven profile is available [here](../../pom.xml#L399).

## Deploy to App Service Linux

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
```

## Open Java EE application running on App Service Linux and interacting with Azure Database for PostgreSQL

```bash
open https://${WEBAPP}.azurewebsites.net
```

![](../../step-01-deploy-java-ee-app-to-azure/media/YAPS-PetStore-H2.jpg)

## Log into Azure Database for PostgreSQL and validate tables were created and populated

```bash
psql --host=${POSTGRES_SERVER_FULL_NAME} --port=5432 \
    --username=${POSTGRES_SERVER_ADMIN_FULL_NAME} \
    --dbname=${POSTGRES_DATABASE_NAME} --set=sslmode=require

Password for user postgres@petstore-db:
psql (11.1, server 9.6.10)
SSL connection (protocol: TLSv1.2, cipher: ECDHE-RSA-AES256-SHA384, bits: 256, compression: off)
Type "help" for help.

postgres=> \l
postgres=> \dt
               List of relations
 Schema |        Name        | Type  |  Owner
--------+--------------------+-------+----------
 public | category           | table | postgres
 public | country            | table | postgres
 public | customer           | table | postgres
 public | item               | table | postgres
 public | order_line         | table | postgres
 public | product            | table | postgres
 public | purchase_order     | table | postgres
 public | t_order_order_line | table | postgres
(8 rows)

postgres=> select name from category;
   name
----------
 Fish
 Dogs
 Reptiles
 Cats
 Birds
(5 rows)

postgres=> \q
```

## Troubleshoot Java EE application on Azure by viewing logs

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP} --resource-group ${RESOURCE_GROUP}
```

---

⬅️ Previous guide: [02 - Create a database](../../step-02-create-a-database/README.md)

➡️ Next guide: [04 - Monitor Java EE application](../../step-04-monitor-java-ee-app/README.md)
