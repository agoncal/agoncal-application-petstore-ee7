# 02A - Create a PostgreSQL Database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Create a PostgreSQL database using commandline tools.

---

## Create and configure Petstore database in Azure Database for PostgreSQL
  
Create a Petstore database using Azure CLI and PostgreSQL CLI:
  
```bash
az postgres server create --resource-group ${RESOURCE_GROUP} \
    --name ${POSTGRES_SERVER_NAME} \
    --location ${REGION} \
    --admin-user ${POSTGRES_SERVER_ADMIN_LOGIN_NAME} \
    --admin-password ${POSTGRES_SERVER_ADMIN_PASSWORD} \
    --sku-name GP_Gen5_2

az postgres server firewall-rule create \
    --resource-group ${RESOURCE_GROUP} \
    --server ${POSTGRES_SERVER_NAME} --name allAzureIPs \
    --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

az postgres server firewall-rule create \
    --resource-group ${RESOURCE_GROUP} \
    --server ${POSTGRES_SERVER_NAME} --name myDevBox \
    --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS}

psql --host=${POSTGRES_SERVER_FULL_NAME} --port=5432 \
    --username=${POSTGRES_SERVER_ADMIN_FULL_NAME} \
    --dbname=${POSTGRES_DATABASE_NAME} --set=sslmode=require
```
```text
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
  
>💡 - you can reinstall `psql` command line tool using `brew reinstall postgresql`.
  
When you migrate Java applications to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.
  
Also, for your convenience, there is a [cheat sheet for PostgreSQL CLI](http://www.postgresqltutorial.com/postgresql-cheat-sheet/).
  
---
  
⬅️ Previous guide: [01 - Deploy a Java EE application to Azure](../../step-01-deploy-java-ee-app-to-azure/README.md)
  
➡️ Next guide: [03 - Bind Java EE application to the database](../../step-03-bind-java-ee-app-to-database/README.md)