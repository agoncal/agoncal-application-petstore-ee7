# Application - Petstore Java EE 7

* *Author* : [Antonio Goncalves](http://www.antoniogoncalves.org)
* *Level* : Intermediate
* *Technologies* : Java EE 7 (JPA 2.1, CDI 1.1, Bean Validation 1.1, EJB Lite 3.2, JSF 2.2, JAX-RS 2.0), Java SE 7 (because that's the minimum required by Java EE 7), Twitter Bootstrap (Bootstrap 3.x, JQuery 2.x, PrimeFaces 6.x)
* *Application Servers* : From WildFly 10 to WildFly 26 (does not work on Wildfly 27 because it is based on Jakarta EE 10)
* *Summary* : A Petstore-like application using Java EE 7

[Download the code from GitHub](https://github.com/agoncal/agoncal-application-petstore-ee7)

## Purpose of this application

Do you remember the good old Java [Petstore](http://java.sun.com/developer/releases/petstore/) ? It was a sample application created by Sun for its [Java BluePrints](http://www.oracle.com/technetwork/java/javaee/blueprints/index.html) program. The Java Petstore was designed to illustrate how J2EE (and then Java EE) could be used to develop an eCommerce web application. Yes, the point of the Petstore is to sell pets online. The Petstore had a huge momentum and we started to see plenty of Petstore-like applications flourish. The idea was to build an application with a certain technology. Let's face it, the J2EE version was far too complex using plenty of (today outdated) [design patterns](http://java.sun.com/blueprints/corej2eepatterns/). When I wrote my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee5-9782212120387) back in 2006, I decided to write a Petstore-like application but much simpler. But again, it's out-dated today.

What you have here is another Petstore-like application but using [Java EE 7](http://jcp.org/en/jsr/detail?id=342) and all its goodies (CDI, EJB Lite, REST interface). It is based on the Petstore I developed for my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee-5-9782212126587) (sorry, it's written in French). I've updated it based on my [Java EE 6 book](http://www.amazon.com/gp/product/143022889X/ref=as_li_qf_sp_asin_il_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=143022889X&linkCode=as2&tag=antgonblo-20), and now I'm updating it again so it uses some new features of Java EE 7 described on my [Java EE 7 book](http://www.amazon.com/gp/product/143024626X/ref=as_li_qf_sp_asin_il_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=143024626X&linkCode=as2&tag=antgonblo-20). The goals of this sample is to :

* use Java EE 7 and just Java EE 7 : no external framework or dependency (except web frameworks or logging APIs)
* make it simple : no complex business algorithm, the point is to bring Java EE 7 technologies together to create an eCommerce website

If you want to use a different web interface, external frameworks, add some sexy alternative JVM language... feel free to fork the code. But the goal of this EE 7 Petstore is to remain simple and to stick to Java EE 7.

The only external framework used are [Arquillian](http://arquillian.org/), [Twitter Bootstrap](http://twitter.github.io/bootstrap/) and [PrimeFaces](http://www.primefaces.org/). Arquillian is used for integration testing. Using Maven profile, you can test services, injection, persistence... against different application servers. Twitter Bootstrap and PrimeFaces bring a bit of beauty to the web interface.

## Compile and package

Being Maven centric, you can compile and package it without tests using `mvn clean compile -Dmaven.test.skip=true`, `mvn clean package -Dmaven.test.skip=true` or `mvn clean install -Dmaven.test.skip=true`. Once you have your war file, you can deploy it.

### Unit Testing

The application has a few unit tests. You can run them using `mvn clean test`. These tests do not do much, they just test the equals and hashcode methods of the entities.

### Integration testing with Arquillian

Launching tests under [WildFly](http://www.wildfly.org/) is straight forward. You must have a WidlFly up and running, and execute the tests using the 
following Maven profile :

    mvn clean verify -Parquillian-wildfly-remote

Or if you prefer the managed mode (it downloads and starts WildFly for you) :

    mvn clean verify -Parquillian-wildfly-managed

## Execute the sample

To execute the application, you can either use the WildFly admin console or the Maven WildFly plugin.

### Setup WildFly

Before starting Wildfly we need to create a new user. In the `$WILDFLY_HOME/bin` directory, run the following command `./add-user.sh` to create a new _Management User_. Choose a username and password.

### Start WildFly

In the `$WILDFLY_HOME/bin` directory, run the following command `./standalone.sh` to start WildFly. Then go to the 
WildFly admin console on http://localhost:9990 and enter your usename and password.

### Build and Deploy the application using the WildFly admin console

Build the application with `mvn clean package -Dmaven.test.skip=true`. You get the `applicationPetstore.war` war file in the `target` directory.

In the WildFly admin console, deploy the `applicationPetstore.war` file. For that, go to the _Deployments_ tab and click on _Add_.

### Build and Deploy the application using the WildFly Maven Plugin

With WildFly up and running, deploy the application using the Maven plugin `mvn clean wildfly:deploy`

### Run the application

Once deployed go to the following URL and start buying some pets: [http://localhost:8080/applicationPetstore](http://localhost:8080/applicationPetstore).

The admin [REST interface](http://localhost:8080/applicationPetstore/swagger.json) allows you to create/update/remove items in the catalog, orders or customers. You can run the following [curl](http://curl.haxx.se/) commands :

* `curl -X GET http://localhost:8080/applicationPetstore/rest/categories`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/products`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/items`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/countries`
* `curl -X GET http://localhost:8080/applicationPetstore/rest/customers`

You can also get a JSON representation as follow :

* `curl -X GET -H "accept: application/json" http://localhost:8080/applicationPetstore/rest/items`

Check the Swagger contract on : [http://localhost:8080/applicationPetstore/swagger.json]()

## Databases

The `persistence.xml` defines a persistence unit called `applicationPetstorePU` that uses the default JBoss database :

```
<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
```

### H2
 
By default, the application uses the in-memory H2 database. If you log into the WildFly [Admin Console](http://localhost:9990/), go to [http://localhost:9990/console/App.html#profile/datasources;name=ExampleDS]() and you will see the H2 Driver as well as the Connection URL pointing at the in-memory H2 database `jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`

### Postgresql

If instead a H2 in-memory database you want to use PostgreSQL, you need to do the following steps.

#### Install the PostgreSQL driver into Wildfly

This [good article](http://ralph.soika.com/wildfly-install-postgresql-jdbc-driver-as-a-module/) explains you how. 

1) Go to `$WILDFLY_HOME/modules/system/layers/base/` and create the folder `org/postgresql/main`
2) Copy the Postgresql [JDBC driver jar](https://jdbc.postgresql.org/download.html) file (eg. `postgresql-42.1.4.jar`) to the new folder `$WILDFLY_HOME/modules/system/layers/base/org/postgresql/main`
3) Create the file `$WILDFLY_HOME/modules/system/layers/base/org/postgresql/main/module.xml` with the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.1.4.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

4) Reference the module as a driver in WildFly configuration

```
WILDFLY_HOME/bin $ ./jboss-cli.sh
You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
[disconnected /] connect
[standalone@localhost:9990 /] /subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql, driver-class-name=org.postgresql.Driver)
{"outcome" => "success"}
```

#### Modify the default Datasource

In the Wildfly [Admin Console](http://localhost:9990) check the default datasource [ExampleDS](http://localhost:9990/console/App.html#profile/ds-finder/datasources;name=ExampleDS). As you can see, it points to an in-memory H2 database. Make the following changes so it points at Postgres:

1) Attribute Tab: Change the driver to postgresql
2) Connection Tab: Change the Connection URL to `jdbc:postgresql://localhost:5432/postgres`
3) Security Tab: Change User name to `postgres` and no password

Once Postgres is up and running, you can hit the button `Test Connection`. It should be ok.

#### Startup PostgreSQL

The easiest is to use the Docker file to start Postgres

```
$ docker-compose -f src/main/docker/postgresql.yml up -d
```

## Test this application on CloudBees

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/cyrille-leclerc/agoncal-application-petstore-ee7/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

## Third Party Tools & Frameworks

### Twitter Bootstrap

When, like me, you have no web designer skills at all and your web pages look ugly, you use [Twitter Bootstrap](http://twitter.github.com/bootstrap/) ;o)

## Icons

I use:
 
* [Font Awesome](http://fontawesome.io/)
* [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) which are in Creative Commons

### Arquillian

[Arquillian](http://arquillian.org/) for the integration tests.

## Developpers

Some people who worked on this project :

* [Antoine Sabot-Durand](https://twitter.com/#!/antoine_sd)
* [Brice Leporini](https://twitter.com/#!/blep)
* Hervé Le Morvan

## Bugs & Workaround


## Licensing

<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
