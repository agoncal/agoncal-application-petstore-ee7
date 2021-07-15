# 03B - Bind the application to a MySQL database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Bind the application to the petstore database in Azure Database for MySQL.

---

## Configure MySQL Data Source

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
# where resources point to JDBC driver for MySQL
# and module xml points to module description, see below

module add --name=com.mysql --resources=/home/site/libs/mysql-connector-java-8.0.13.jar --module-xml=/home/site/scripts/mysql-module.xml
```
Where `mysql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="com.mysql">
    <resources>
     <!-- ***** IMPORTANT : REPLACE THIS PLACEHOLDER *******-->
       <resource-root path="/home/site/libs/mysql-connector-java-8.0.13.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

Add a JDBC driver for MySQL:

```text
/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)
```

Install a data source by using the data-source shortcut command:

```text
data-source add --name=mysqlDS --jndi-name=java:jboss/datasources/mysqlDS --connection-url=${MYSQL_CONNECTION_URL,env.MYSQL_CONNECTION_URL:jdbc:mysql://db:3306/petstore} --driver-name=mysql --user-name=${MYSQL_SERVER_ADMIN_FULL_NAME,env.MYSQL_SERVER_ADMIN_FULL_NAME:mysql} --password=${MYSQL_SERVER_ADMIN_PASSWORD,env.MYSQL_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=com.mysql.cj.jdbc.Driver --jta=true --use-java-context=true --exception-sorter-class-name=com.mysql.cj.jdbc.integration.jboss.ExtendedMysqlExceptionSorter
```

A server reload may be required for the changes to take effect:

```text
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for MySQL and module XML are available in
[initial-mysql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-mysql/agoncal-application-petstore-ee7/.scripts)

Also, you can directly download [JDBC driver for MySQL](https://dev.mysql.com/downloads/connector/j/). For example:

```text
wget -q "http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/8.0.13/mysql-connector-java-8.0.13.jar" -O mysql-connector-java-8.0.13.jar
```

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
      <directory>${project.basedir}/.scripts/3B-mysql</directory>
      <includes>
        <include>*.jar</include>
      </includes>
    </resource>
    <resource>
      <type>startup</type>
      <directory>${project.basedir}/.scripts/3B-mysql</directory>
      <includes>
        <include>*.sh</include>
      </includes>
    </resource>
    <resource>
      <type>script</type>
      <directory>${project.basedir}/.scripts/3B-mysql</directory>
      <includes>
        <include>*.cli</include>
        <include>*.xml</include>
      </includes>
    </resource>
  </resources>
</deployment>
```

### Step 3: Set MySQL database connection info in the Web app environment

Use Azure CLI to set database connection info:

```bash
az webapp config appsettings set \
    --resource-group ${RESOURCE_GROUP} --name ${WEBAPP} \
    --settings \
    MYSQL_CONNECTION_URL=${MYSQL_CONNECTION_URL} \
    MYSQL_SERVER_ADMIN_PASSWORD=${MYSQL_SERVER_ADMIN_PASSWORD} \
    MYSQL_SERVER_ADMIN_FULL_NAME=${MYSQL_SERVER_ADMIN_FULL_NAME}
```
```text
[
 {
   "name": "WEBSITE_HTTPLOGGING_RETENTION_DAYS",
   "slotSetting": false,
   "value": "3"
 },
 {
   "name": "MYSQL_CONNECTION_URL",
   "slotSetting": false,
   "value": "jdbc:mysql://petstore-db1221.mysql.database.azure.com:3306/petstore?ssl=true"
 },
 {
   "name": "MYSQL_SERVER_ADMIN_PASSWORD",
   "slotSetting": false,
   "value": "======= MASKED ======="
 },
 {
   "name": "MYSQL_SERVER_ADMIN_FULL_NAME",
   "slotSetting": false,
   "value": "selvasingh@petstore-db1221"
 }
]
```

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


## Build PetStore to interact with Azure Database for MySQL

```bash
# Use the Maven profile for MySQL to build from the project base directory
cd ../..
mvn package -Dmaven.test.skip=true -Ddb=mysql
```

Note - the `mysql` Maven profile is available [here](../../pom.xml#L435).

## Deploy to App Service Linux

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
```

## Open Pet Store running on App Service Linux and interacting with Azure Database for MySQL

```bash
open https://${WEBAPP}.azurewebsites.net
```

![](../../step-01-deploy-java-ee-app-to-azure/media/YAPS-PetStore-H2.jpg)

## Log into Azure Database for MySQL and validate tables were created and populated

```bash
mysql -u ${MYSQL_SERVER_ADMIN_FULL_NAME}  -h ${MYSQL_SERVER_FULL_NAME} -P 3306 -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 64167
Server version: 5.6.39.0 MySQL Community Server (GPL)

Copyright (c) 2000, 2018, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> use petstore;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+--------------------+
| Tables_in_petstore |
+--------------------+
| category           |
| country            |
| customer           |
| hibernate_sequence |
| item               |
| order_line         |
| product            |
| purchase_order     |
| t_order_order_line |
+--------------------+
9 rows in set (0.03 sec)

mysql> select name from category;
+----------+
| name     |
+----------+
| Fish     |
| Dogs     |
| Reptiles |
| Cats     |
| Birds    |
+----------+
5 rows in set (0.03 sec)

mysql> quit
Bye
```

## Troubleshoot Java EE application on Azure by viewing logs

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP} --resource-group ${RESOURCE_GROUP}
```

---

⬅️ Previous guide: [02 - Create a database](../../step-02-create-a-database/README.md)

➡️ Next guide: [04 - Monitor Java EE application](../../step-04-monitor-java-ee-app/README.md)
