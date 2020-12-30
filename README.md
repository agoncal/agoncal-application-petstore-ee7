---
services: app-service, PostgreSQL, MySQL, azure-sql-database
platforms: java
author: selvasingh, sadigopu
---

# Migrate Java EE App to Azure

You will find here a full workshop on migrating an existing Java EE application to Azure, 
including guides and demos. You will migrate:
 
- Java EE application to App Service Linux and 
- Application's data to Azure Database for PostgreSQL, MySQL and or SQL Database. 

## What you should expect

This is not the official documentation but an opinionated training.

It is a hands-on training, and it will use the command line extensively. 
The idea is to get coding very quickly and play with the platform, 
from a simple demo to far more complex examples.

After completing all the guides, you should have a fairly good understanding of 
everything that Azure offers for running Java EE applications on PaaS without worrying
about the underlying infrastructure or monitoring applications.

You will migrate the famous [Sun's 2003 Java EE Blue Print](https://www.oracle.com/java/technologies/java-blueprint.html) 
sample application. The most recent incarnation of the sample application uses:

- Java SE 8
- Java EE 7
- JSR 338 Java Persistence API (JPA 2.2)
- JSR 346 Context and Dependency Injection (CDI 1.1)
- JSR 345 Enterprise Java Beans 3.2 (EJB 3.2)
- JSR 344 Java Server Faces (JSF 2.2) 
- JSR 339 Java API for RESTful Web Services (JAX-RS 2.0)
- Twitter Bootstrap (Bootstrap 3.x, JQuery 2.x, PrimeFaces 6.x) 

Upon migration, you will power the app using 
Azure Database for PostgreSQL, MySQL and or SQL Database.

## Symbols

>🛑 -  __Manual Modification Required__. When this symbol appears in front of one or 
more commands, you will need to modify the commands as indicated prior to running them.

>🚧 - __Preview-specific__. This symbol indicates steps that are only necessary while 
JBoss EAP on App Service is in preview.

>💡 - __Frustration Avoidance Tip__. These will help you avoid potential pitfalls.

## [00 - Prerequisites and Setup](step-00-setup-your-environment/README.md)

Prerequisites and environment setup.
 
## [01 - Deploy a Java EE application to Azure](step-01-deploy-java-ee-app-to-azure/README.md)

Deploy a Java EE application to Azure.

## [02 - Create a database](step-02-create-a-database/README.md)

Create a database - PostgreSQL or MySQL or SQL Database.

## [03 - Bind Java EE application to the database](step-03-bind-java-ee-app-to-database/README.md)

Bind the Java EE application to the database.

## [Conclusion](99-conclusion/README.md)

---

## Credits

This Java EE Petstore sample is forked from 
[agoncal/agoncal-application-petstore-ee7](https://github.com/agoncal/agoncal-application-petstore-ee7) - see [Petstore README](./README-petstoreee7.md). 

## Contributing

This project welcomes contributions and suggestions.  Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide
a CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/).
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or comments.

---


## Build and Deploy Pet Store Powered Using Azure Database for PostgreSQL

Start your next leg of your journey ... change directory:

```bash
cd ../../initial-postgresql/agoncal-application-petstore-ee7
```

#### Add PostgreSQL Profile

Add a new profile for PostgreSQL in `pom.xml`:

```xml
<profile>
  <id>postgresql</id>
  <activation>
    <property>
      <name>db</name>
      <value>postgresql</value>
    </property>
  </activation>
  <build>

    <plugins>

      <!-- copy the correct persistence.xml file -->
      <plugin>
        <groupId>com.coderplus.maven.plugins</groupId>
        <artifactId>copy-rename-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <id>copy-file</id>
            <phase>validate</phase>
            <goals>
              <goal>copy</goal>
            </goals>
            <configuration>
              <sourceFile>${project.basedir}/main/resources/META-INF/persistence-postgresql.xml</sourceFile>
              <destinationFile>${project.basedir}/src/main/resources/META-INF/persistence.xml</destinationFile>
            </configuration>
          </execution>
        </executions>
      </plugin>

    </plugins>
  </build>
</profile>
``` 

#### Set environment variables for binding secrets at runtime

```bash
cp set-env-variables-template.sh .scripts/set-env-variables.sh
```

Modify `.scripts/set-env-variables.sh` and set Azure Resource Group name, 
App Service Web App Name, Azure Region, FTP details in
 the local machine and PostgreSQL server info. Make sure to pick a password that adheres to the following rules :
 Your password must be at least 8 characters in length.
Your password must contain characters from three of the following categories – English uppercase letters, English lowercase letters, numbers (0-9), and non-alphanumeric characters (!, $, #, %, etc.)
Your password can not contain all or part of the username ( 3 or more consecutive alphanumeric characters) 

 Get the FTP details by using the webapp and resource group created in the previous H2-based lab, with the following command, which displays profile values

```bash
az webapp deployment list-publishing-profiles -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}

{
   ...
   ...
    "profileName": "petstore-java-ee - FTP",
    "publishMethod": "FTP",
    "publishUrl": "ftp://waws-prod-bay-063.ftp.azurewebsites.windows.net/site/wwwroot",
    "userName": "petstore-java-ee\\$petstore-java-ee",
    "userPWD": "============MASKED===========================================",
    "webSystem": "WebSites"
  }
```

Store FTP host name, say `waws-prod-bay-063.ftp.azurewebsites.windows.net`, user name and user password in .scripts/set-env-variables.sh file.
 
 Note the ip of the local machine
```bash
curl ifconfig.me
```

 Then, set environment variables:
 
```bash
source .scripts/set-env-variables.sh
```

#### Create and Configure Petstore DB in Azure Database for PostgreSQL

Create a Petstore DB using Azure CLI and PostgreSQL CLI:

```bash
az postgres server create --resource-group ${RESOURCEGROUP_NAME} \
    --name ${POSTGRES_SERVER_NAME} \
    --location ${REGION} \
    --admin-user ${POSTGRES_SERVER_ADMIN_LOGIN_NAME} \
    --admin-password ${POSTGRES_SERVER_ADMIN_PASSWORD} \
    --sku-name GP_Gen5_2

az postgres server firewall-rule create \
    --resource-group ${RESOURCEGROUP_NAME} \
    --server ${POSTGRES_SERVER_NAME} --name allAzureIPs \
    --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

curl ifconfig.me

az postgres server firewall-rule create \
    --resource-group ${RESOURCEGROUP_NAME} \
    --server ${POSTGRES_SERVER_NAME} --name myDevBox \
    --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS}

psql --host=${POSTGRES_SERVER_FULL_NAME} --port=5432 \
    --username=${POSTGRES_SERVER_ADMIN_FULL_NAME} \
    --dbname=${POSTGRES_DATABASE_NAME} --set=sslmode=require

Password for user postgres@petstore-db:
psql (11.1, server 9.6.10)
SSL connection (protocol: TLSv1.2, cipher: ECDHE-RSA-AES256-SHA384, bits: 256, compression: off)
Type "help" for help.

postgres=> \l
                                                               List of databases
       Name        |      Owner      | Encoding |          Collate           |           Ctype            |          Access privileges          
-------------------+-----------------+----------+----------------------------+----------------------------+-------------------------------------
 azure_maintenance | azure_superuser | UTF8     | English_United States.1252 | English_United States.1252 | azure_superuser=CTc/azure_superuser
 azure_sys         | azure_superuser | UTF8     | English_United States.1252 | English_United States.1252 | 
 postgres          | azure_superuser | UTF8     | English_United States.1252 | English_United States.1252 | 
 template0         | azure_superuser | UTF8     | English_United States.1252 | English_United States.1252 | =c/azure_superuser                 +
                   |                 |          |                            |                            | azure_superuser=CTc/azure_superuser
 template1         | azure_superuser | UTF8     | English_United States.1252 | English_United States.1252 | =c/azure_superuser                 +
                   |                 |          |                            |                            | azure_superuser=CTc/azure_superuser
(5 rows)

postgres=> \q
```

Note - you can install `psql` command line tool using `brew reinstall postgresql`.

When you migrate Java workloads to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.

Also, for your convenience, there is a [cheat sheet for PostgreSQL CLI](http://www.postgresqltutorial.com/postgresql-cheat-sheet/).

#### Configure PostgreSQL Data Source

There are 5 steps to configure a data source. These steps are similar to configuring data sources 
in any on premise Java EE app servers:

##### Step 1: Understand How to configure JBoss EAP

In App Service, each instance of an app server is stateless. Therefore, each instance must be 
configured on startup to support a JBoss EAP configuration needed by your application. You can configure at 
startup by supplying a startup Bash script that calls [JBoss/WildFly CLI commands](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface) to setup data sources, messaging 
 providers and any other dependencies. We will create a startup.sh script and place it in the `/home` 
 directory of the Web app. The script will:
 
Install a JBoss EAP module:

```bash
# where resources point to JDBC driver for PostgreSQL
# and module xml points to module description, see below

module add --name=org.postgres --resources=/home/site/deployments/tools/postgresql-42.2.5.jar --module-xml=/home/site/deployments/tools/postgresql-module.xml
```
Where `postgresql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="org.postgres">
    <resources>
     <!-- ***** IMPORTANT : PATH should point to PostgreSQL Java driver on App Service Linux *******-->
       <resource-root path="/home/site/deployments/tools/postgresql-42.2.5.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```
 
Add a JDBC driver for PostgreSQL:

```bash
/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
```

Install a data source by using the data-source shortcut command:

```bash
data-source add --name=postgresDS --driver-name=postgres --jndi-name=java:jboss/datasources/postgresDS --connection-url=${POSTGRES_CONNECTION_URL,env.POSTGRES_CONNECTION_URL:jdbc:postgresql://db:5432/postgres} --user-name=${POSTGRES_SERVER_ADMIN_FULL_NAME,env.POSTGRES_SERVER_ADMIN_FULL_NAME:postgres} --password=${POSTGRES_SERVER_ADMIN_PASSWORD,env.POSTGRES_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=org.postgresql.Driver --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter --jta=true --use-java-context=true --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker
```

A server reload may be required for the changes to take effect:

```bash
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for PostgreSQL and module XML are available in 
[initial-postgresql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-postgresql/agoncal-application-petstore-ee7/.scripts) 

Also, you can directly download the latest version of [JDBC driver for PostgreSQL](https://jdbc.postgresql.org/download.html)

##### Step 2: Upload data source artifacts to App Service linux

Open an FTP connection to App Service Linux to upload data source artifacts:

```bash
pwd
/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7

cd .scripts

ftp
ftp> open waws-prod-bay-063.drip.azurewebsites.windows.net
Trying 23.99.84.148...
Connected to waws-prod-bay-063.drip.azurewebsites.windows.net.
220 Microsoft FTP Service
Name (waws-prod-bay-063.drip.azurewebsites.windows.net:selvasingh): 
331 Password required
Password: 
230 User logged in.
Remote system type is Windows_NT.
ftp> ascii
200 Type set to A.

ftp> passive

# Upload startup.sh to /home directory
ftp> put startup.sh
local: startup.sh remote: startup.sh
229 Entering Extended Passive Mode (|||10204|)
125 Data connection already open; Transfer starting.
100% |************************************************|   236       39.33 KiB/s    --:-- ETA
226 Transfer complete.
236 bytes sent in 00:00 (5.01 KiB/s)

# Upload CLI Commands, Module XML and JDBC Driver for PostgreSQL to /home/site/deployments/tools
ftp> cd site/deployments/tools
250 CWD command successful.
ftp> put postgresql-datasource-commands.cli 
local: postgresql-datasource-commands.cli remote: postgresql-datasource-commands.cli
229 Entering Extended Passive Mode (|||10205|)
125 Data connection already open; Transfer starting.
100% |************************************************|  1444      234.94 KiB/s    --:-- ETA
226 Transfer complete.
1444 bytes sent in 00:00 (32.31 KiB/s)
ftp> put postgresql-module.xml 
local: postgresql-module.xml remote: postgresql-module.xml
229 Entering Extended Passive Mode (|||10206|)
125 Data connection already open; Transfer starting.
100% |************************************************|   404      192.17 KiB/s    --:-- ETA
226 Transfer complete.
404 bytes sent in 00:00 (5.86 KiB/s)
ftp> binary
200 Type set to I.
ftp> put postgresql-42.2.5.jar 
local: postgresql-42.2.5.jar remote: postgresql-42.2.5.jar
229 Entering Extended Passive Mode (|||10207|)
125 Data connection already open; Transfer starting.
100% |************************************************|   806 KiB  506.52 KiB/s    00:00 ETA
226 Transfer complete.
825943 bytes sent in 00:01 (469.59 KiB/s)
ftp> bye
221 Goodbye.
```

##### Step 3: Set PostgreSQL database connection info in the Web app environment

Use Azure CLI to set database connection info:
   
```bash
az webapp config appsettings set \
 --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME} \
 --settings \
 POSTGRES_CONNECTION_URL=${POSTGRES_CONNECTION_URL} \
 POSTGRES_SERVER_ADMIN_PASSWORD=${POSTGRES_SERVER_ADMIN_PASSWORD} \
 POSTGRES_SERVER_ADMIN_FULL_NAME=${POSTGRES_SERVER_ADMIN_FULL_NAME}

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

az webapp config set --startup-file /home/startup.sh \
    --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME}

```

##### Step 4: Test the JBoss EAP CLI commands to configure data source

You can test Bash script for configuring data source by running them on App Service Linux 
by [opening an SSH connection from your development machine](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell):

```bash
# ======== first terminal window =========
az webapp create-remote-connection --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME} &
[18] 7422
bash-3.2$ Auto-selecting port: 60029
SSH is available { username: root, password: Docker! }
Start your favorite client and connect to port 60029
Websocket tracing disabled, use --verbose flag to enable
Successfully connected to local server..

# ======== second terminal window ========
ssh root@localhost -p 60029
The authenticity of host '[localhost]:60029 ([127.0.0.1]:60029)' can't be established.
ECDSA key fingerprint is SHA256:Lys3Kd4sNJc7X8LVMRP89GKbOzlOGp03tGYj+mY4Kic.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '[localhost]:60029' (ECDSA) to the list of known hosts.
root@localhost's password:
  _____
  /  _  \ __________ _________   ____
 /  /_\  \___   /  |  \_  __ \_/ __ \
/    |    \/    /|  |  /|  | \/\  ___/
\____|__  /_____ \____/ |__|    \___  >
        \/      \/                  \/
A P P   S E R V I C E   O N   L I N U X

Documentation: http://aka.ms/webapp-linux
c315a18b39d2:/home#

# ======== run JBoss/WildFly CLI commands to configure a data source ===========

c315a18b39d2:/home# /opt/eap/bin/jboss-cli.sh --connect
Picked up _JAVA_OPTIONS: -Djava.net.preferIPv4Stack=true

[standalone@localhost:9990 /] module add --name=org.postgres --resources=/home/site/deployments/tools/postgresql-42.2.5.jar --module-xml=/home/site/deployments/tools/postgresql-module.xml
[standalone@localhost:9990 /] /subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
{"outcome" => "success"}

[standalone@localhost:9990 /] data-source add --name=postgresDS --driver-name=postgres --jndi-name=java:jboss/datasources/postgresDS --connection-url=${POSTGRES_CONNECTION_URL,env.POSTGRES_CONNECTION_URL:jdbc:postgresql://db:5432/postgres} --user-name=${POSTGRES_SERVER_ADMIN_FULL_NAME,env.POSTGRES_SERVER_ADMIN_FULL_NAME:postgres} --password=${POSTGRES_SERVER_ADMIN_PASSWORD,env.POSTGRES_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=org.postgresql.Driver --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter --jta=true --use-java-context=true --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker
[standalone@localhost:9990 /] /subsystem=datasources/data-source=postgresDS:test-connection-in-pool()
{
    "outcome" => "success",
    "result" => [true]
}

# ====== run JBoss/WildFly CLI commands to undo data source configuration ========

/subsystem=datasources:read-resource
/subsystem=datasources:installed-drivers-list
module remove --name=org.postgres # --name=com.mysql --name=com.microsoft
data-source remove --name=postgresDS # --name=mysqlDS --name=sqlDS
reload --use-current-server-config=true
/subsystem=datasources/jdbc-driver=postgres:remove # jdbc-driver=mysql jdbc-driver-sqlserver

``` 

##### Step 5: Restart the remote JBoss EAP app server

Use Azure CLI to restart the remote JBoss EAP app server:
   
```bash
az webapp stop -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
az webapp start -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
```

For additional info, please refer to: 

- [JBoss Data Source Management](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/datasource_management).
- [JBoss/WildFly CLI Guide](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface)
- [Open SSH session from your development machine to App Service Linux](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell)


#### Build PetStore to interact with Azure Database for PostgreSQL

```bash
# Use the Maven profile for PostgreSQL to build

mvn package -Dmaven.test.skip=true -Ddb=postgresql
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- copy-rename-maven-plugin:1.0:copy (copy-file) @ petstoreee7 ---
[INFO] Copied /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence-postgresql.xml to /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence.xml
...
...
[INFO] --- maven-war-plugin:3.1.0:war (default-war) @ petstoreee7 ---
[INFO] Packaging webapp
[INFO] Assembling webapp [petstoreee7] in [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7/target/applicationPetstore]
[INFO] Processing war project
[INFO] Copying webapp resources [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7/src/main/webapp]
[INFO] Webapp assembled in [243 msecs]
[INFO] Building war: /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-postgresql/agoncal-application-petstore-ee7/target/applicationPetstore.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.161 s
[INFO] Finished at: 2018-12-21T16:27:42-08:00
[INFO] Final Memory: 31M/513M
[INFO] ------------------------------------------------------------------------
```

#### Deploy to App Service Linux 

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- azure-webapp-maven-plugin:1.12.0:deploy (default-cli) @ petstoreee7 ---
[INFO] Authenticate with Azure CLI 2.0
[INFO] Updating target Web App...
[INFO] Successfully updated Web App.
[INFO] Trying to deploy artifact to petstore-java-ee...
[INFO] Deploying the war file...
[INFO] Successfully deployed the artifact to https://petstore-java-ee.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 04:07 min
[INFO] Finished at: 2018-12-21T16:45:12-08:00
[INFO] Final Memory: 60M/631M
[INFO] ------------------------------------------------------------------------
```

#### Open Pet Store running on App Service Linux and interacting with Azure Database for PostgreSQL

```bash
open https://petstore-java-ee.azurewebsites.net
```

![](./media/YAPS-PetStore-on-h2-small.jpg)

#### Log into Azure Database for PostgreSQL and Validate Tables were Created and Populated

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
bash-3.2$ 

```

#### Trouble Shoot Petstore on Azure by Viewing Logs

Configure logs for the deployed Java Web 
app in App Service Linux:

```bash
az webapp log config --name ${WEBAPP_NAME} \
 --resource-group ${RESOURCEGROUP_NAME} \
  --web-server-logging filesystem
```

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP_NAME} \
 --resource-group ${RESOURCEGROUP_NAME}
```

When you are finished, you can check your results 
against YOUR code in 
[migrate-Java-EE-app-to-azure/initial-mysql](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-mysql).

## Build and Deploy Pet Store Powered Using Azure Database for MySQL

Start your next leg of your journey ... change directory:

```bash
cd ../../initial-mysql/agoncal-application-petstore-ee7
```

#### Add MySQL Profile

Add a new profile for MySQL in `pom.xml`:

```xml
<profile>
  <id>mysql</id>
  <activation>
    <property>
      <name>db</name>
      <value>mysql</value>
    </property>
  </activation>
  <build>

    <plugins>

      <!-- copy the MySQL persistence.xml file -->
      <plugin>
        <groupId>com.coderplus.maven.plugins</groupId>
        <artifactId>copy-rename-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <id>copy-file</id>
            <phase>validate</phase>
            <goals>
              <goal>copy</goal>
            </goals>
            <configuration>
              <sourceFile>${project.basedir}/main/resources/META-INF/persistence-mysql.xml</sourceFile>
              <destinationFile>${project.basedir}/src/main/resources/META-INF/persistence.xml</destinationFile>
            </configuration>
          </execution>
        </executions>
      </plugin>

    </plugins>
  </build>
</profile>
``` 

#### Set environment variables for binding secrets at runtime

```bash
cp set-env-variables-template.sh .scripts/set-env-variables.sh
```

Modify `.scripts/set-env-variables.sh` and set Azure Resource Group name, 
App Service Web App Name, Azure Region, JBoss EAP directory in
 the local machine, FTP deployment credentials and MySQL server info. 
 Then, set environment variables:
 
```bash
source .scripts/set-env-variables.sh
```

#### Create and Configure MySQL DB in Azure Database for MySQL

Create a Petstore DB using Azure CLI and MySQL CLI:

```bash
az mysql server create --resource-group ${RESOURCEGROUP_NAME} \
 --name ${MYSQL_SERVER_NAME}  --location westus2 \
 --admin-user ${MYSQL_SERVER_ADMIN_LOGIN_NAME} \
 --admin-password ${MYSQL_SERVER_ADMIN_PASSWORD} \
 --sku-name GP_Gen5_32 \
 --ssl-enforcement Disabled \
 --version 5.7

// allow access from Azure resources
az mysql server firewall-rule create --name allAzureIPs \
 --server ${MYSQL_SERVER_NAME} \
 --resource-group ${RESOURCEGROUP_NAME} \
 --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

// allow access from your dev machine for testing
az mysql server firewall-rule create --name myDevBox \
 --server ${MYSQL_SERVER_NAME} \
 --resource-group ${RESOURCEGROUP_NAME} \
 --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS}

// increase connection timeout
az mysql server configuration set --name wait_timeout \
 --resource-group ${RESOURCEGROUP_NAME} \
 --server ${MYSQL_SERVER_NAME} --value 2147483

// log into mysql
mysql -u ${MYSQL_SERVER_ADMIN_FULL_NAME}  -h ${MYSQL_SERVER_FULL_NAME} -P 3306 -p
Enter password:
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 64096
Server version: 5.6.39.0 MySQL Community Server (GPL)

Copyright (c) 2000, 2018, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> CREATE DATABASE petstore;
Query OK, 1 row affected (0.05 sec)

mysql> CREATE USER 'root' IDENTIFIED BY 'petstore';
Query OK, 0 rows affected (0.04 sec)

mysql> GRANT ALL PRIVILEGES ON petstore.* TO 'root';
Query OK, 0 rows affected (0.05 sec)

mysql> CALL mysql.az_load_timezone();
Query OK, 3179 rows affected, 1 warning (6.34 sec)
    
mysql> SELECT name FROM mysql.time_zone_name;
...

mysql> quit
Bye

az mysql server configuration set --name time_zone \
     --resource-group ${RESOURCEGROUP_NAME} \
     --server ${MYSQL_SERVER_NAME} --value "US/Pacific"

```

When you migrate Java workloads to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.

Also, for your convenience, there is a [cheat sheet for MySQL CLI](http://www.mysqltutorial.org/mysql-cheat-sheet.aspx).

#### Configure MySQL Data Source

There are 5 steps to configure a data source. These steps are similar to configuring data sources 
in any on premise Java EE app servers:

##### Step 1: Understand How to configure JBoss EAP

In App Service, each instance of an app server is stateless. Therefore, each instance must be 
configured on startup to support a JBoss EAP configuration needed by your application. You can configure at 
startup by supplying a startup Bash script that calls [JBoss/WildFly CLI commands](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface) to setup data sources, messaging 
 providers and any other dependencies. We will create a startup.sh script and place it in the `/home` 
 directory of the Web app. The script will:
 
Install a JBoss EAP module:

```bash
# where resources point to JDBC driver for MySQL
# and module xml points to module description, see below

module add --name=com.mysql --resources=/home/site/deployments/tools/mysql-connector-java-8.0.13.jar --module-xml=/home/site/deployments/tools/mysql-module.xml
```
Where `mysql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="com.mysql">
    <resources>
     <!-- ***** IMPORTANT : REPLACE THIS PLACEHOLDER *******-->
       <resource-root path="/home/site/deployments/tools/mysql-connector-java-8.0.13.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```
 
Add a JDBC driver for MySQL:

```bash
/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)
```

Install a data source by using the data-source shortcut command:

```bash
data-source add --name=mysqlDS --jndi-name=java:jboss/datasources/mysqlDS --connection-url=${MYSQL_CONNECTION_URL,env.MYSQL_CONNECTION_URL:jdbc:mysql://db:3306/petstore} --driver-name=mysql --user-name=${MYSQL_SERVER_ADMIN_FULL_NAME,env.MYSQL_SERVER_ADMIN_FULL_NAME:mysql} --password=${MYSQL_SERVER_ADMIN_PASSWORD,env.MYSQL_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=com.mysql.cj.jdbc.Driver --jta=true --use-java-context=true --exception-sorter-class-name=com.mysql.cj.jdbc.integration.jboss.ExtendedMysqlExceptionSorter
```

A server reload may be required for the changes to take effect:

```bash
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for MySQL and module XML are available in 
[initial-mysql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-mysql/agoncal-application-petstore-ee7/.scripts) 

Also, you can directly download [JDBC driver for MySQL](https://dev.mysql.com/downloads/connector/j/). For example:

```bash
wget -q "http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/8.0.13/mysql-connector-java-8.0.13.jar" -O mysql-connector-java-8.0.13.jar
```

##### Step 2: Upload data source artifacts to App Service linux

Open an FTP connection to App Service Linux to upload data source artifacts:

```bash
pwd
/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7

cd .scripts

ftp
ftp> open waws-prod-bay-063.drip.azurewebsites.windows.net
Trying 23.99.84.148...
Connected to waws-prod-bay-063.drip.azurewebsites.windows.net.
220 Microsoft FTP Service
Name (waws-prod-bay-063.drip.azurewebsites.windows.net:selvasingh): petstore-java-ee\\$petstore-java-ee
331 Password required
Password:
230 User logged in.
Remote system type is Windows_NT.
ftp> ascii
200 Type set to A.
ftp> put startup.sh
local: startup.sh remote: startup.sh
229 Entering Extended Passive Mode (|||10208|)
125 Data connection already open; Transfer starting.
100% |************************************************|   236       40.58 KiB/s    --:-- ETA
226 Transfer complete.
236 bytes sent in 00:00 (5.18 KiB/s)
ftp> cd site/deployments/tools
250 CWD command successful.
ftp> put mysql-datasource-commands.cli
local: mysql-datasource-commands.cli remote: mysql-datasource-commands.cli
229 Entering Extended Passive Mode (|||10209|)
125 Data connection already open; Transfer starting.
100% |************************************************|  1375      226.39 KiB/s    --:-- ETA
226 Transfer complete.
1375 bytes sent in 00:00 (30.81 KiB/s)
ftp> put mysql-module.xml
local: mysql-module.xml remote: mysql-module.xml
229 Entering Extended Passive Mode (|||10210|)
125 Data connection already open; Transfer starting.
100% |************************************************|   411        1.29 MiB/s    --:-- ETA
226 Transfer complete.
411 bytes sent in 00:00 (9.34 KiB/s)
ftp> binary
200 Type set to I.
ftp> put mysql-connector-java-8.0.13.jar
local: mysql-connector-java-8.0.13.jar remote: mysql-connector-java-8.0.13.jar
229 Entering Extended Passive Mode (|||10211|)
125 Data connection already open; Transfer starting.
100% |************************************************|  2082 KiB  622.64 KiB/s    00:00 ETA
226 Transfer complete.
2132635 bytes sent in 00:03 (597.54 KiB/s)
ftp> bye
221 Goodbye.
```

##### Step 3: Set MySQL database connection info in the Web app environment

Use Azure CLI to set database connection info:
   
```bash
az webapp config appsettings set \
  --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME} \
  --settings \
  MYSQL_CONNECTION_URL=${MYSQL_CONNECTION_URL} \
  MYSQL_SERVER_ADMIN_PASSWORD=${MYSQL_SERVER_ADMIN_PASSWORD} \
  MYSQL_SERVER_ADMIN_FULL_NAME=${MYSQL_SERVER_ADMIN_FULL_NAME}

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

##### Step 4: Test the JBoss/WildFly CLI commands to configure data source

You can test Bash script for configuring data source by running them on App Service Linux 
by [opening an SSH connection from your development machine](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell):

```bash
# ======== first terminal window =========
az webapp create-remote-connection --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME} &
[18] 7422
bash-3.2$ Auto-selecting port: 60029
SSH is available { username: root, password: Docker! }
Start your favorite client and connect to port 60029
Websocket tracing disabled, use --verbose flag to enable
Successfully connected to local server..

# ======== second terminal window ========
ssh root@localhost -p 60029
The authenticity of host '[localhost]:60029 ([127.0.0.1]:60029)' can't be established.
ECDSA key fingerprint is SHA256:Lys3Kd4sNJc7X8LVMRP89GKbOzlOGp03tGYj+mY4Kic.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '[localhost]:60029' (ECDSA) to the list of known hosts.
root@localhost's password:
  _____
  /  _  \ __________ _________   ____
 /  /_\  \___   /  |  \_  __ \_/ __ \
/    |    \/    /|  |  /|  | \/\  ___/
\____|__  /_____ \____/ |__|    \___  >
        \/      \/                  \/
A P P   S E R V I C E   O N   L I N U X

Documentation: http://aka.ms/webapp-linux
c315a18b39d2:/home#

# ======== run JBoss/WildFly CLI commands to configure a data source ===========

c315a18b39d2:/home# /opt/eap/bin/jboss-cli.sh -c
Picked up _JAVA_OPTIONS: -Djava.net.preferIPv4Stack=true

[standalone@localhost:9990 /] module add --name=com.mysql --resources=/home/site/deployments/tools/mysql-connector-java-8.0.13.jar --module-xml=/home/site/deployments/tools/mysql-module.xml
[standalone@localhost:9990 /] /subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.cj.jdbc.Driver)
{"outcome" => "success"}

[standalone@localhost:9990 /] data-source add --name=mysqlDS --jndi-name=java:jboss/datasources/mysqlDS --connection-url=${MYSQL_CONNECTION_URL,env.MYSQL_CONNECTION_URL:jdbc:mysql://db:3306/petstore} --driver-name=mysql --user-name=${MYSQL_SERVER_ADMIN_FULL_NAME,env.MYSQL_SERVER_ADMIN_FULL_NAME:mysql} --password=${MYSQL_SERVER_ADMIN_PASSWORD,env.MYSQL_SERVER_ADMIN_PASSWORD:example} --use-ccm=true --max-pool-size=5 --blocking-timeout-wait-millis=5000 --enabled=true --driver-class=com.mysql.cj.jdbc.Driver --jta=true --use-java-context=true --exception-sorter-class-name=com.mysql.cj.jdbc.integration.jboss.ExtendedMysqlExceptionSorter
[standalone@localhost:9990 /] /subsystem=datasources/data-source=mysqlDS:test-connection-in-pool()
{
    "outcome" => "success",
    "result" => [true]
}

# ======== run JBoss/WildFly CLI commands to undo the datasource configuration =========

/subsystem=datasources:read-resource
/subsystem=datasources:installed-drivers-list
module remove --name=com.mysql # --name=org.postgres --name=com.microsoft
data-source remove --name=mysqlDS # --name=postgresDS --name=sqlDS
reload --use-current-server-config=true
/subsystem=datasources/jdbc-driver=mysql:remove # jdbc-driver=postgres jdbc-driver-sqlserver

``` 

##### Step 5: Restart the remote JBoss EAP app server

Use Azure CLI to restart the remote JBoss EAP app server:
   
```bash
az webapp stop -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
az webapp start -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
```

For additional info, please refer to: 

- [JBoss Data Source Management](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/datasource_management).
- [JBoss/WildFly CLI Guide](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface)
- [Open SSH session from your development machine to App Service Linux](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell)


#### Build PetStore to interact with Azure Database for MySQL

```bash
# Use the Maven profile for MySQL to build

bash-3.2$ mvn package -Dmaven.test.skip=true -Ddb=mysql
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- copy-rename-maven-plugin:1.0:copy (copy-file) @ petstoreee7 ---
[INFO] Copied /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence-mysql.xml to /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence.xml
...
...
[INFO] --- maven-war-plugin:3.1.0:war (default-war) @ petstoreee7 ---
[INFO] Packaging webapp
[INFO] Assembling webapp [petstoreee7] in [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7/target/applicationPetstore]
[INFO] Processing war project
[INFO] Copying webapp resources [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7/src/main/webapp]
[INFO] Webapp assembled in [258 msecs]
[INFO] Building war: /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-mysql/agoncal-application-petstore-ee7/target/applicationPetstore.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.199 s
[INFO] Finished at: 2018-12-22T10:47:23-08:00
[INFO] Final Memory: 26M/388M
[INFO] ------------------------------------------------------------------------
```

#### Deploy to App Service Linux 

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- azure-webapp-maven-plugin:1.12.0:deploy (default-cli) @ petstoreee7 ---
[INFO] Authenticate with Azure CLI 2.0
[INFO] Updating target Web App...
[INFO] Successfully updated Web App.
[INFO] Trying to deploy artifact to petstore-java-ee...
[INFO] Deploying the war file...
[INFO] Successfully deployed the artifact to https://petstore-java-ee.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 56.128 s
[INFO] Finished at: 2018-12-22T10:51:13-08:00
[INFO] Final Memory: 60M/626M
[INFO] ------------------------------------------------------------------------
```

#### Open Pet Store running on App Service Linux and interacting with Azure Database for MySQL

```bash
open https://petstore-java-ee.azurewebsites.net
```

![](./media/YAPS-PetStore-on-h2-small.jpg)

#### Log into Azure Database for MySQL and Validate Tables were Created and Populated

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

#### Trouble Shoot Petstore on Azure by Viewing Logs

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP_NAME} \
 --resource-group ${RESOURCEGROUP_NAME}
```

## Build and Deploy Pet Store Powered Using Azure SQL Database

Start your next leg of your journey ... change directory:

```bash
cd ../../initial-sql/agoncal-application-petstore-ee7
```

#### Add SQL Database Profile

Add a new profile for sql in `pom.xml`:

```xml
<profile>
  <id>sql</id>
  <activation>
  <property>
    <name>db</name>
    <value>sql</value>
  </property>
  </activation>
  <build>

    <plugins>

      <!-- copy the correct persistence.xml file -->
      <plugin>
        <groupId>com.coderplus.maven.plugins</groupId>
        <artifactId>copy-rename-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <id>copy-file</id>
            <phase>validate</phase>
            <goals>
              <goal>copy</goal>
            </goals>
            <configuration>
              <sourceFile>${project.basedir}main/resources/META-INF/persistence-sql.xml</sourceFile>
              <destinationFile>${project.basedir}/src/main/resources/META-INF/persistence.xml</destinationFile>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</profile>
``` 

#### Set environment variables for binding secrets at runtime

```bash
cp set-env-variables-template.sh .scripts/set-env-variables.sh
```

Modify `.scripts/set-env-variables.sh` and set Azure Resource Group name, 
App Service Web App Name, Azure Region, JBoss EAP directory in
 the local machine, FTP deployment credentials and SQL Database info. 
 Then, set environment variables:
 
```bash
source .scripts/set-env-variables.sh
```

#### Create and Configure SQL DB in Azure SQL Database

Install the Azure CLI `db-up` extension:
```bash
az extension add --name db-up
```

Create a Petstore DB using Azure CLI and SQL CLI:
```bash

az sql server create --admin-user ${SQL_SERVER_ADMIN_LOGIN_NAME} \
     --admin-password ${SQL_SERVER_ADMIN_PASSWORD} \
     --name ${SQL_SERVER_NAME} \
     --resource-group ${RESOURCEGROUP_NAME}
     
az sql server firewall-rule create --server ${SQL_SERVER_NAME} \
 --name allAzureIPs \
 --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0 \
 --resource-group ${RESOURCEGROUP_NAME}

az sql server firewall-rule create --server ${SQL_SERVER_NAME} \
 --name myDevBox \
 --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS} \
 --resource-group ${RESOURCEGROUP_NAME}

az sql db create --name ${SQL_DATABASE_NAME} \
    --server ${SQL_SERVER_NAME} \
    --resource-group ${RESOURCEGROUP_NAME}

```

When you migrate Java workloads to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.

Also, for your convenience, there is a [cheat sheet for sqlcmd CLI](https://docs.microsoft.com/en-us/sql/tools/sqlcmd-utility).

#### Configure SQL Database Data Source

There are 5 steps to configure a data source. These steps are similar to configuring data sources 
in any on premise Java EE app servers:

##### Step 1: Understand How to configure JBoss EAP

In App Service, each instance of an app server is stateless. Therefore, each instance must be 
configured on startup to support a JBoss EAP configuration needed by your application. You can configure at 
startup by supplying a startup Bash script that calls [JBoss/WildFly CLI commands](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface) to setup data sources, messaging 
 providers and any other dependencies. We will create a startup.sh script and place it in the `/home` 
 directory of the Web app. The script will:
 
Install a JBoss EAP module:

```bash
# where resources point to JDBC driver for SQL Database
# and module xml points to module description, see below

module add --name=com.microsoft --resources=/home/site/deployments/tools/mssql-jdbc-7.2.1.jre8.jar --module-xml=/home/site/deployments/tools/mssql-module.xml
```
Where `mssql-module.xml` describes the module:

```xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="com.microsoft">
  <resources>
	<resource-root path="/home/site/deployments/tools/mssql-jdbc-7.2.1.jre8.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
```
 
Add a JDBC driver for SQL Database:

```bash
/subsystem=datasources/jdbc-driver=sqlserver:add(driver-name="sqlserver",driver-module-name="com.microsoft",driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver,driver-datasource-class-name=com.microsoft.sqlserver.jdbc.SQLServerDataSource)
```

Install a data source by using the data-source shortcut command:

```bash
data-source add --name=sqlDS --jndi-name=java:jboss/datasources/sqlDS --driver-name=sqlserver --connection-url=${SQL_CONNECTION_URL,env.SQL_CONNECTION_URL:example} --validate-on-match=true --background-validation=false --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLExceptionSorter
```

A server reload may be required for the changes to take effect:

```bash
reload --use-current-server-config=true
```

These JBoss CLI commands, JDBC driver for SQL Database and module XML are available in 
[initial-sql/agoncal-application-petstore-ee7/.scripts](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/initial-sql/agoncal-application-petstore-ee7/.scripts) 

Also, you can directly download [JDBC driver for SQL Database](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server). For example:


##### Step 2: Upload data source artifacts to App Service linux

Open an FTP connection to App Service Linux to upload data source artifacts:

```bash
pwd
/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7

cd .scripts

ftp
ftp> open waws-prod-bay-063.ftp.azurewebsites.windows.net
Trying 23.99.87.125...
Connected to waws-prod-bay-063.drip.azurewebsites.windows.net.
220 Microsoft FTP Service
Name (waws-prod-bay-063.ftp.azurewebsites.windows.net:selvasingh):
331 Password required
Password:
230 User logged in.
Remote system type is Windows_NT.
ftp> pwd
Remote directory: /
ftp> ascii
200 Type set to A.
ftp> put startup.sh
local: startup.sh remote: startup.sh
229 Entering Extended Passive Mode (|||10199|)
125 Data connection already open; Transfer starting.
100% |*******************************************************|   125       21.36 KiB/s    --:-- ETA
226 Transfer complete.
125 bytes sent in 00:00 (2.71 KiB/s)
ftp> cd site/deployments/tools
250 CWD command successful.
ftp> put mssql-datasource-commands.cli
local: mssql-datasource-commands.cli remote: mssql-datasource-commands.cli
229 Entering Extended Passive Mode (|||10200|)
125 Data connection already open; Transfer starting.
100% |*******************************************************|  1751      301.42 KiB/s    --:-- ETA
226 Transfer complete.
1751 bytes sent in 00:00 (35.69 KiB/s)
ftp> put mssql-module.xml
local: mssql-module.xml remote: mssql-module.xml
229 Entering Extended Passive Mode (|||10201|)
125 Data connection already open; Transfer starting.
100% |*******************************************************|   305       51.39 KiB/s    --:-- ETA
226 Transfer complete.
305 bytes sent in 00:00 (7.07 KiB/s)
ftp> bin
200 Type set to I.
ftp> put mssql-jdbc-7.2.1.jre8.jar
local: mssql-jdbc-7.2.1.jre8.jar remote: mssql-jdbc-7.2.1.jre8.jar
229 Entering Extended Passive Mode (|||10202|)
125 Data connection already open; Transfer starting.
100% |*******************************************************|  1135 KiB    1.62 MiB/s    00:00 ETA
226 Transfer complete.
1162710 bytes sent in 00:00 (1.48 MiB/s)
ftp> bye
221 Goodbye.
```

##### Step 3: Set SQL Database connection info in the Web app environment

Use Azure CLI to set database connection info:
   
```bash

az webapp config appsettings set  \
    --resource-group ${RESOURCEGROUP_NAME} \
    --name ${WEBAPP_NAME}  \
    --settings  SQL_CONNECTION_URL=${SQL_CONNECTION_URL}

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

##### Step 4: Test the JBoss/WildFly CLI commands to configure data source

You can test Bash script for configuring data source by running them on App Service Linux 
by [opening an SSH connection from your development machine](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell):

```bash
# ======== first terminal window =========
az webapp create-remote-connection --resource-group ${RESOURCEGROUP_NAME} --name ${WEBAPP_NAME} &
[1] 10851
bash-3.2$ Auto-selecting port: 54155
SSH is available { username: root, password: Docker! }
Start your favorite client and connect to port 54155
Websocket tracing disabled, use --verbose flag to enable
Successfully connected to local server..

ssh root@localhost -p 58386
The authenticity of host '[localhost]:58386 ([127.0.0.1]:58386)' can't be established.
ECDSA key fingerprint is SHA256:7EHlhnWmPC600borWmiBMP43SdXIHedlk4sKIfJKu3I.
Are you sure you want to continue connecting (yes/no)? yes
Warning: Permanently added '[localhost]:58386' (ECDSA) to the list of known hosts.
root@localhost's password:
  _____
  /  _  \ __________ _________   ____
 /  /_\  \___   /  |  \_  __ \_/ __ \
/    |    \/    /|  |  /|  | \/\  ___/
\____|__  /_____ \____/ |__|    \___  >
        \/      \/                  \/
A P P   S E R V I C E   O N   L I N U X

Documentation: http://aka.ms/webapp-linux
ee72972be508:/home#

# ======== run JBoss/WildFly CLI commands to configure a data source ===========

ee72972be508:/home# source startup.sh
Picked up _JAVA_OPTIONS: -Djava.net.preferIPv4Stack=true
"Configuring sqlDS ==================="
"Installing MSSQL module"
"Installing MSSQL driver"
"Installing MSSQL datasource"
The batch executed successfully
ee72972be508:/home# /opt/eap/bin/jboss-cli.sh -c
Picked up _JAVA_OPTIONS: -Djava.net.preferIPv4Stack=true

[standalone@localhost:9990 /] /subsystem=datasources/data-source=sqlDS:test-connection-in-pool()
{
    "outcome" => "success",
    "result" => [true]
}

[standalone@localhost:9990 /] exit
``` 

##### Step 5: Restart the remote JBoss EAP app server

Use Azure CLI to restart the remote JBoss EAP app server:
   
```bash
az webapp stop -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
az webapp start -g ${RESOURCEGROUP_NAME} -n ${WEBAPP_NAME}
```

For additional info, please refer to: 

- [JBoss Data Source Management](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/datasource_management).
- [JBoss/WildFly CLI Guide](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface)
- [Open SSH session from your development machine to App Service Linux](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell)


#### Build PetStore to interact with Azure SQL Database

```bash
# Use the Maven profile for Azure SQL Database to build

mvn package -Dmaven.test.skip=true -Ddb=sql
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- copy-rename-maven-plugin:1.0:copy (copy-file) @ petstoreee7 ---
[INFO] Copied /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence-sql.xml to /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7/src/main/resources/META-INF/persistence.xml
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ petstoreee7 ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 14 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ petstoreee7 ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- swagger-maven-plugin:3.1.6:generate (default) @ petstoreee7 ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ petstoreee7 ---
[INFO] Not copying test resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ petstoreee7 ---
[INFO] Not compiling test sources
[INFO] 
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ petstoreee7 ---
[INFO] Tests are skipped.
[INFO] 
[INFO] --- maven-war-plugin:3.1.0:war (default-war) @ petstoreee7 ---
[INFO] Packaging webapp
[INFO] Assembling webapp [petstoreee7] in [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7/target/applicationPetstore]
[INFO] Processing war project
[INFO] Copying webapp resources [/Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7/src/main/webapp]
[INFO] Webapp assembled in [394 msecs]
[INFO] Building war: /Users/selvasingh/migrate-Java-EE-app-to-azure/initial-sql/agoncal-application-petstore-ee7/target/applicationPetstore.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.850 s
[INFO] Finished at: 2019-06-01T17:47:53-07:00
[INFO] Final Memory: 22M/338M
[INFO] ------------------------------------------------------------------------
```

#### Deploy to App Service Linux 

Deploy to JBoss EAP in App Service Linux:

```bash
mvn azure-webapp:deploy
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- azure-webapp-maven-plugin:1.12.0:deploy (default-cli) @ petstoreee7 ---
[INFO] Authenticate with Azure CLI 2.0
[INFO] Updating target Web App...
[INFO] Successfully updated Web App.
[INFO] Trying to deploy artifact to petstore-java-ee...
[INFO] Deploying the war file...
[INFO] Successfully deployed the artifact to https://petstore-java-ee.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 01:25 min
[INFO] Finished at: 2019-06-01T17:54:14-07:00
[INFO] Final Memory: 50M/635M
[INFO] ------------------------------------------------------------------------
```

#### Open Pet Store running on App Service Linux and interacting with Azure SQL Database

```bash
open https://petstore-java-ee.azurewebsites.net
```

![](./media/YAPS-PetStore-on-h2-small.jpg)

#### Log into Azure SQL Database and Validate Tables were Created and Populated

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

#### Trouble Shoot Petstore on Azure by Viewing Logs

Open Java Web app remote log stream from a local machine:

```bash
az webapp log tail --name ${WEBAPP_NAME} \
 --resource-group ${RESOURCEGROUP_NAME}

2018-12-22T00:47:48  Welcome, you are now connected to log-streaming service.
2018-12-22T00:41:45.064280703Z   _____                               
2018-12-22T00:41:45.064325203Z   /  _  \ __________ _________   ____  
2018-12-22T00:41:45.064331403Z  /  /_\  \___   /  |  \_  __ \_/ __ \ 
2018-12-22T00:41:45.064335603Z /    |    \/    /|  |  /|  | \/\  ___/ 
2018-12-22T00:41:45.064339403Z \____|__  /_____ \____/ |__|    \___  >
2018-12-22T00:41:45.064343503Z         \/      \/                  \/ 
2018-12-22T00:41:45.064347403Z A P P   S E R V I C E   O N   L I N U X  
...
...

2019-06-02T00:55:35.520469647Z 00:55:35,520 INFO  [org.jboss.as] (Controller Boot Thread) WFLYSRV0025: WildFly Full 14.0.1.Final (WildFly Core 6.0.2.Final) started in 9816ms - Started 81 of 93 services (31 services are lazy, passive or on-demand)
2019-06-02T00:55:38.548395649Z ***Admin server is ready
2019-06-02T00:55:38.555859867Z STARTUP_FILE=/home/startup.sh
2019-06-02T00:55:38.582558031Z STARTUP_COMMAND=
2019-06-02T00:55:38.587032142Z Copying /home/startup.sh to /tmp/startup.sh and fixing EOL characters in /tmp/startup.sh
2019-06-02T00:55:38.593801458Z Running STARTUP_FILE: /tmp/startup.sh
2019-06-02T00:55:38.628501142Z Picked up JAVA_TOOL_OPTIONS:  -Djava.net.preferIPv4Stack=true
2019-06-02T00:55:42.206161168Z "Configuring sqlDS ==================="
2019-06-02T00:55:42.211727982Z "Installing MSSQL module"
2019-06-02T00:55:42.253643983Z "Installing MSSQL driver"
2019-06-02T00:55:42.284713558Z "Installing MSSQL datasource"
2019-06-02T00:55:42.490971355Z The batch executed successfully
2019-06-02T00:55:42.552288503Z 00:55:42,552 INFO  [org.jboss.as] (MSC service thread 1-1) WFLYSRV0050: WildFly Full 14.0.1.Final (WildFly Core 6.0.2.Final) stopped in 47ms
2019-06-02T00:55:42.564687033Z 00:55:42,564 INFO  [org.jboss.as] (MSC service thread 1-2) WFLYSRV0049: WildFly Full 14.0.1.Final (WildFly Core 6.0.2.Final) starting
...
...
2019-06-02T00:55:44.293778902Z 00:55:44,293 INFO  [org.jboss.as.connector.subsystems.datasources] (ServerService Thread Pool -- 44) WFLYJCA0004: Deploying JDBC-compliant driver class com.microsoft.sqlserver.jdbc.SQLServerDriver (version 7.2)
2019-06-02T00:55:44.594576027Z 00:55:44,594 INFO  [org.jboss.as.connector.deployers.jdbc] (MSC service thread 1-2) WFLYJCA0018: Started Driver service with driver-name = sqlserver
2019-06-02T00:55:44.607447158Z 00:55:44,601 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-2) WFLYJCA0010: Unbound data source [java:jboss/datasources/ExampleDS]
2019-06-02T00:55:44.610591266Z 00:55:44,610 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-2) WFLYJCA0010: Unbound data source [java:jboss/datasources/sqlDS]
...
...
019-06-02T00:55:56.153951398Z 00:55:56,152 INFO  [org.jboss.as.jpa] (ServerService Thread Pool -- 79) WFLYJPA0010: Starting Persistence Unit (phase 2 of 2) Service 'ROOT.war#applicationPetstorePU'
2019-06-02T00:55:57.237113910Z 00:55:57,232 INFO  [org.hibernate.dialect.Dialect] (ServerService Thread Pool -- 79) HHH000400: Using dialect: org.hibernate.dialect.SQLServer2012Dialect

```

When you are finished, you can check your results 
against YOUR code in 
[migrate-Java-EE-app-to-azure/complete](https://github.com/Azure-Samples/migrate-Java-EE-app-to-azure/tree/master/complete).

## Scale out the Pet Store app

Scale out Java Web app using Azure CLI:

```bash
az appservice plan update --number-of-workers 2 \
   --name ${WEBAPP_PLAN_NAME} \
   --resource-group ${RESOURCEGROUP_NAME}
```

## Congratulations!

Congratulations!! You migrated an 
existing Java EE workload to Azure, aka app to App Service Linux and 
app's data to Azure Database for PostgreSQL, MySQL and or SQL Database.

## Resources

- [Java Enterprise Guide for App Service on Linux](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-java-enterprise)
- [Maven Plugin for Azure App Service](https://docs.microsoft.com/en-us/java/api/overview/azure/maven/azure-webapp-maven-plugin/readme?view=azure-java-stable)
- [JBoss Data Source Management](https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.0/html/configuration_guide/datasource_management)
- [JBoss/WildFly CLI Guide](https://docs.jboss.org/author/display/WFLY/Command+Line+Interface)
- [JDBC driver for PostgreSQL](https://jdbc.postgresql.org/download.html)
- [PostgreSQL CLI Cheat Sheet](http://www.postgresqltutorial.com/postgresql-cheat-sheet/)
- [JDBC driver for MySQL](https://dev.mysql.com/downloads/connector/j/)
- [MySQL CLI Cheat Sheet](http://www.mysqltutorial.org/mysql-cheat-sheet.aspx)
- [Cheat sheet for sqlcmd CLI](https://docs.microsoft.com/en-us/sql/tools/sqlcmd-utility)
- [Opening an SSH connection from your development machine](https://docs.microsoft.com/en-us/azure/app-service/containers/app-service-linux-ssh-support#open-ssh-session-from-remote-shell)
- [Azure for Java Developers](https://docs.microsoft.com/en-us/java/azure/)

---

This project has adopted 
the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). 
For more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/) or 
contact [opencode@microsoft.com](mailto:opencode@microsoft.com) 
with any additional 
questions or comments.

---

Here are some useful JBoss CLI commands:

```bash
/opt/eap/bin/jboss-cli.sh -c

/subsystem=datasources:read-resource

/subsystem=datasources:installed-drivers-list

module remove --name=org.postgres # --name=com.mysql --name=com.microsoft

data-source remove --name=postgresDS # --name=mysqlDS --name=sqlDS

reload --use-current-server-config=true

/subsystem=datasources/jdbc-driver=postgres:remove # jdbc-driver=mysql jdbc-driver-sqlserver

```
