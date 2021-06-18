# 01 - Deploy a Java EE application to Azure

__This guide is part of the [migrate Java EE app to Azure training](../README.md)__

Basics on configuring Maven and deploying a Java EE application to Azure.

---

## Verify Azure Subscription and setup development environment

Set environment variables for storing Azure information, 
particularly Azure Resource Group and Web app names. Then, you can 
export them to your local environment. 

You can start setting up environment variables using the supplied
Bash shell script template.

```bash
cp setup-env-variables-template.sh .scripts/setup-env-variables.sh
```

Modify `.scripts/setup-env-variables.sh` and set your Azure Resource Group name, 
Web app name, Azure Region, database name, database admin name and password.

```bash
# Azure Environment
# === Your Azure Subscription ID ===========
export SUBSCRIPTION=your-subscription-id
# === Your Azure Resource Group Name =======
export RESOURCE_GROUP=migrate-java-ee-app-to-azure
# === Your Web App Name, seattle-petstore ==
export WEBAPP=your-web-app-name
# === Your Azure Region for deployment =====
export REGION=westus
# === Your database server name ============
export DATABASE_SERVER=your-database-server-name
# === Your database admin name =============
export DATABASE_ADMIN=selvasingh
# === Your database admin password =========
export DATABASE_ADMIN_PASSWORD=SuperS3cr3t
```

Then, set environment variables:

```bash
source .scripts/setup-env-variables.sh
```

Ensure your Azure CLI is logged into your Azure subscription.

>💡 If using Windows, make sure you enter these commands and all others that follow in Git Bash.

```bash
az login # Sign into an azure account
az account show # See the currently signed-in account.
```

Ensure your default subscription is the one you intend to use for this lab, and if not - 
set the subscription via 
```az account set --subscription ${SUBSCRIPTION}```.

## Build a Java EE application

Build a Java EE application using Maven:

```bash
mvn package -Dmaven.test.skip=true -Ddb=h2

---------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
...
[INFO] --- maven-war-plugin:3.1.0:war (default-war) @ petstoreee7 ---
[INFO] Packaging webapp
[INFO] Assembling webapp [petstoreee7] in [/Users/selvasingh/GitHub/selvasingh/migrate-javaee-app-to-azure/target/applicationPetstore]
[INFO] Processing war project
[INFO] Copying webapp resources [/Users/selvasingh/GitHub/selvasingh/migrate-javaee-app-to-azure/src/main/webapp]
[INFO] Webapp assembled in [224 msecs]
[INFO] Building war: /Users/selvasingh/GitHub/selvasingh/migrate-javaee-app-to-azure/target/applicationPetstore.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 3.736 s
[INFO] Finished at: 2020-12-29T20:54:07-08:00
[INFO] Final Memory: 20M/339M
[INFO] ------------------------------------------------------------------------
```

`pom.xml` in this repo is pre-configured to use the 
[Maven Plugin for Azure App Service](https://github.com/Microsoft/azure-maven-plugins/blob/develop/azure-webapp-maven-plugin/README.md) - see XML fragment below. 

Note - you can also configure the same by executing 
`mvn com.microsoft.azure:azure-webapp-maven-plugin:1.16.1:config`.

```xml    
<plugins> 

  <!--*************************************************-->
  <!-- Deploy to JBoss EAP in App Service Linux           -->
  <!--*************************************************-->

  <plugin>
    <groupId>com.microsoft.azure</groupId>
    <artifactId>azure-webapp-maven-plugin</artifactId>
    <version>1.16.1</version>
    <configuration>
      <schemaVersion>v2</schemaVersion>
      <subscriptionId>${SUBSCRIPTION}</subscriptionId>
      <resourceGroup>${RESOURCE_GROUP}</resourceGroup>
      <appName>${WEBAPP}</appName>
      <pricingTier>P1v2</pricingTier>
      <region>${REGION}</region>
      <runtime>
        <os>Linux</os>
        <javaVersion>Java 8</javaVersion>
        <webContainer>Jbosseap 7.2</webContainer>
      </runtime>
      <deployment>
        <resources>
          <resource>
            <directory>${project.basedir}/target</directory>
            <includes>
              <include>*.war</include>
            </includes>
          </resource>
        </resources>
      </deployment>
    </configuration>
  </plugin>
    ...
</plugins>
```
 
Deploy the Java EE application to App Service Linux:

```bash
mvn azure-webapp:deploy
```

```text
[INFO] Scanning for projects...
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] Building Petstore application using Java EE 7 7.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- azure-webapp-maven-plugin:1.16.1:deploy (default-cli) @ petstoreee7 ---
...
[INFO] Target Web App doesn't exist. Creating a new one...
[INFO] Creating App Service Plan 'ServicePlan96b599bb-a053-4ea6'...
[INFO] Successfully created App Service Plan.
[INFO] Successfully created Web App.
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource to /Users/selvasingh/GitHub/selvasingh/migrate-javaee-app-to-azure/target/azure-webapp/seattle-petstore-3596b742-2cf2-4713-b7a4-b88694754bad
[INFO] Trying to deploy artifact to seattle-petstore...
[INFO] Deploying the war file applicationPetstore.war...
[INFO] Successfully deployed the artifact to https://seattle-petstore.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 02:34 min
[INFO] Finished at: 2020-12-29T20:58:59-08:00
[INFO] Final Memory: 56M/790M
[INFO] ------------------------------------------------------------------------
```

## Open Java EE application running on JBoss EAP in App Service Linux

Open the Java EE application running on JBoss EAP in App Service Linux:
```bash
open https://${WEBAPP}.azurewebsites.net
```
![](./media/YAPS-PetStore-H2.jpg)

You can also `curl` the REST API exposed by the Java EE application. The admin REST 
API allows you to create/update/remove items in the catalog, orders or customers. 
You can run the following curl commands:
```bash
curl -X GET https://${WEBAPP}.azurewebsites.net/rest/categories
curl -X GET https://${WEBAPP}.azurewebsites.net/rest/products
curl -X GET https://${WEBAPP}.azurewebsites.net/rest/items
curl -X GET https://${WEBAPP}.azurewebsites.net/rest/countries
curl -X GET https://${WEBAPP}.azurewebsites.net/rest/customers
```

You can also get a JSON representation:
```bash
curl -X GET -H "accept: application/json" https://${WEBAPP}.azurewebsites.net/rest/items
```

Check the Java EE application's Swagger contract:
```bash
curl -X GET https://${WEBAPP}.azurewebsites.net/swagger.json
```

---

⬅️ Previous guide: [00 - Prerequisites and Setup](../step-00-setup-your-environment/README.md)

➡️ Next guide: [02 - Create a database](../step-02-create-a-database/README.md)
