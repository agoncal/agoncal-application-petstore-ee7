# 02C - Create a SQL Database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Create a SQL database using commandline tools.

---

## Create and Configure SQL database in Azure SQL Database

Create a Petstore database using Azure CLI:
```bash

az sql server create --admin-user ${SQL_SERVER_ADMIN_LOGIN_NAME} \
    --admin-password ${SQL_SERVER_ADMIN_PASSWORD} \
    --name ${SQL_SERVER_NAME} \
    --resource-group ${RESOURCE_GROUP} --location ${REGION}
     
az sql server firewall-rule create --server ${SQL_SERVER_NAME} \
    --name allAzureIPs \
    --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0 \
    --resource-group ${RESOURCE_GROUP}

az sql server firewall-rule create --server ${SQL_SERVER_NAME} \
    --name myDevBox \
    --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS} \
    --resource-group ${RESOURCE_GROUP}

az sql db create --name ${SQL_DATABASE_NAME} \
    --server ${SQL_SERVER_NAME} \
    --resource-group ${RESOURCE_GROUP}

```

When you migrate Java applications to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.

Also, for your convenience, there is a [cheat sheet for sqlcmd CLI](https://docs.microsoft.com/en-us/sql/tools/sqlcmd-utility).

---
  
⬅️ Previous guide: [01 - Deploy a Java EE application to Azure](../../step-01-deploy-java-ee-app-to-azure/README.md)
  
➡️ Next guide: [03 - Bind Java EE application to the database](../../step-03-bind-java-ee-app-to-database/README.md)
