# 00 - Setup your environment

__This guide is part of the [migrate Java EE app to Azure training](../README.md)__

Setting up all the necessary prerequisites in order to expeditiously complete the lab.

---

## Prerequisites

In order to deploy a Java Web app to cloud, you need 
an Azure subscription. If you do not already have an Azure subscription, you can activate your 
[MSDN subscriber benefits](https://azure.microsoft.com/pricing/member-offers/msdn-benefits-details/) 
or sign up for a 
[free Azure account]((https://azure.microsoft.com/pricing/free-trial/)).

This training lab requires the following to be installed on your development machine:

* [JDK 1.8](https://www.azul.com/downloads/azure-only/zulu/?&version=java-8-lts&architecture=x86-64-bit&package=jdk)
* A text editor or an IDE. If you do not already have an IDE for Java development, 
we recommend using [Visual Studio Code](https://code.visualstudio.com/) 
with the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
* [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli?view=azure-cli-latest) 
* The Bash shell. While Azure CLI should behave identically on all environments, some semantics may need to be modified if
 you use other shells. To complete this training on Windows, you can 
 use [Git Bash that accompanies the Windows distribution of Git](https://git-scm.com/download/win)
* [Maven](http://maven.apache.org/)
* In section 3, you will create a database - PostgreSQL, MySQL or SQL Database. Based on the 
database of your choice, you will require the corresponding database's commandline tool:
  * [PostgreSQL CLI](https://www.postgresql.org/docs/current/app-psql.html)
  * [MySQL CLI](https://dev.mysql.com/downloads/shell/)
  * [SQLCMD CLI](https://cloudblogs.microsoft.com/sqlserver/2017/05/16/sql-server-command-line-tools-for-macos-released/).
* The [`jq` utility](https://stedolan.github.io/jq/download/). On Windows, download [this Windows port of JQ](https://github.com/stedolan/jq/releases) and add the following to the `~/.bashrc` file: 
   ```bash
   alias jq=<JQ Download location>/jq-win64.exe
   ```

The environment variable `JAVA_HOME` should be set to the path of `javac` in the JDK installation.

---

➡️ Next guide: [01 - Deploy a Java EE application to Azure](../step-01-deploy-java-ee-app-to-azure/README.md)