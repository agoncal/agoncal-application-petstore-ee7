# 03 - Bind Java EE application to database

__This guide is part of the [migrate Java EE app to Azure training](../README.md)__

Configure JBoss EAP datasource and bind the Java EE application to the 
database of your choice.

---

## Get Web application's FTP publishing coordinates and credentials

To configure datasource and bind the Java EE application running in JBoss EAP in App Service 
Linux, we must get the FTP publishing coordinates and credentials.

```bash
az webapp deployment list-publishing-profiles --resource-group ${RESOURCE_GROUP} \
    --name ${WEBAPP}

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

Store FTP host name, say `waws-prod-bay-063.ftp.azurewebsites.windows.net`, 
user name and user password in `.scripts/setup-env-variables.sh` file.

>🚧 - __Preview-specific__. Using FTP file transfer to upload drivers, modules, CLI commands and 
startup batch file is only necessary while JBoss EAP on App Service is in preview. Soon, the 
[Maven Plugin for Azure App Service](https://github.com/Microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/README.md)
will integrate these file transfer into the popular one-step deploy, `mvn azure-webapp:deploy`.

## Bind application to database

Depending on your choice of the database that you created, you can pick one of the three paths
to bind the Java EE application to the database:

### [03A - PostgreSQL](step-03A-bind-app-to-postgresql/README.md)

Bind the application to the petstore database in Azure Database for PostgreSQL.
  
### [03B - MySQL](step-03B-bind-app-to-mysql/README.md)

Bind the application to the petstore database in Azure Database for MySQL.

### [03C - SQL Database](step-03C-bind-app-to-sql-database/README.md)

Bind the application to the petstore database in Azure SQL Database.

---
  
⬅️ Previous guide: [02 - Deploy a Java EE application to Azure](../step-02-create-a-database/README.md)
  
➡️ Next guide: Bind application to [03A - PostgreSQL](step-03A-bind-app-to-postgresql/README.md), [03B - MySQL](step-03B-bind-app-to-mysql/README.md), or [03C - SQL Database](step-03C-bind-app-to-sql-database/README.md)
