---
services: app-service, PostgreSQL, MySQL, azure-sql-database
platforms: java
author: selvasingh, sadigopu
---

# Migrate Java EE App to Azure

You will find here a full training workshop on migrating an existing Java EE application to Azure, 
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

## [00 - Prerequisites and Setup](step-00-setup-your-environment/README.md) (15 minutes)

Prerequisites and environment setup.
 
## [01 - Deploy a Java EE application to Azure](step-01-deploy-java-ee-app-to-azure/README.md) (15 minutes)

Deploy a Java EE application to Azure.

## [02 - Create a database](step-02-create-a-database/README.md) (10 minutes)

Create a database - PostgreSQL or MySQL or SQL Database.

## [03 - Bind Java EE application to the database](step-03-bind-java-ee-app-to-database/README.md) (15 minutes)

Bind the Java EE application to the database.

## [04 - Monitor Java EE application](step-04-monitor-java-ee-app/README.md) (30 minutes)

Configure and monitor Java EE application and its dependencies using Azure Monitor.

## [05 - Continuously Deploy using GitHub Actions](step-05-setup-github-actions/README.md) (15 minutes)

Create a GitHub Actions workflow file to continuously deploy a Java EE application.

## [Conclusion](step-99-conclusion/README.md) (5 minutes)

---

## Petstore is back!

This Java EE Petstore sample is forked from 
[agoncal/agoncal-application-petstore-ee7](https://github.com/agoncal/agoncal-application-petstore-ee7) - see [Petstore README](./README-petstoreee7.md). 

> When you create something and give it away, you lose control of what you've created and then, anything can happen.
> 
> I created this Petstore application years ago to show how Java EE components would work all together. 
> I used this application in many talks, workshops, demos... and then, time passed, I forgot about it and never updated it.
> I've written this code when monoliths were a thing, when the cloud was just starting, and when the only CI/CD tool we had was called Hudson.
> 
> Today, thanks to the Azure team, my Petstore application is built with GitHub actions and deployed to the cloud.
> Without changing the original code, and with only a few commands and some configuration, you can deploy the application to Azure.
> What a journey!
> 
> Thank you,
> 
> [Antonio Goncalves](http://www.antoniogoncalves.org)

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
