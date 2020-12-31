# 02B - Create a MySQL Database

__This guide is part of the [migrate Java EE app to Azure training](../../README.md)__

Create a MySQL database using commandline tools.

---

## Create and Configure MySQL database in Azure Database for MySQL

Create a Petstore database using Azure CLI and MySQL CLI:

```bash
az mysql server create --resource-group ${RESOURCE_GROUP} \
    --name ${MYSQL_SERVER_NAME}  --location ${REGION} \
    --admin-user ${MYSQL_SERVER_ADMIN_LOGIN_NAME} \
    --admin-password ${MYSQL_SERVER_ADMIN_PASSWORD} \
    --sku-name GP_Gen5_32 \
    --ssl-enforcement Disabled \
    --version 5.7

// allow access from Azure resources
az mysql server firewall-rule create --name allAzureIPs \
    --server ${MYSQL_SERVER_NAME} \
    --resource-group ${RESOURCE_GROUP} \
    --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0

// allow access from your dev machine for testing
az mysql server firewall-rule create --name myDevBox \
    --server ${MYSQL_SERVER_NAME} \
    --resource-group ${RESOURCE_GROUP} \
    --start-ip-address ${DEVBOX_IP_ADDRESS} --end-ip-address ${DEVBOX_IP_ADDRESS}

// increase connection timeout
az mysql server configuration set --name wait_timeout \
    --resource-group ${RESOURCE_GROUP} \
    --server ${MYSQL_SERVER_NAME} --value 2147483

// log into mysql
mysql -u ${MYSQL_SERVER_ADMIN_FULL_NAME}  -h ${MYSQL_SERVER_FULL_NAME} -P 3306 -p
```
```text
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
```
```bash
az mysql server configuration set --name time_zone \
    --resource-group ${RESOURCE_GROUP} \
    --server ${MYSQL_SERVER_NAME} --value "US/Pacific"
```

When you migrate Java applications to cloud, you will be considering moving data to cloud. 
To accelerate your transition to cloud, 
Azure offers plenty of options to [migrate your data](https://azure.microsoft.com/en-us/services/database-migration/) 
to cloud.

Also, for your convenience, there is a [cheat sheet for MySQL CLI](http://www.mysqltutorial.org/mysql-cheat-sheet.aspx).

---
  
⬅️ Previous guide: [01 - Deploy a Java EE application to Azure](../../step-01-deploy-java-ee-app-to-azure/README.md)
  
➡️ Next guide: [03 - Bind Java EE application to the database](../../step-03-bind-java-ee-app-to-database/README.md)