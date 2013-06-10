# Application - Petstore Java EE 7

* *Author* : [Antonio Goncalves](http://www.antoniogoncalves.org)
* *Level* : Intermediate
* *Technologies* : Java EE 7 (JPA 2.1, CDI 1.1, Bean Validation 1.1, EJB Lite 3.2, JSF 2.2, JAX-RS 2.0), Twitter Bootstrap (Bootstrap 2.3.1, JQuery 1.9.1)
* *Application Servers* : GlassFish 4.x
* *Summary* : A Petstore-like application using Java EE 7

[Download the code from GitHub](https://github.com/agoncal/agoncal-application-petstore-ee7)

## Test this application on CloudBees

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/cyrille-leclerc/agoncal-application-petstore-ee7/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

## Purpose of this application

Do you remember the good old Java [Petstore](http://java.sun.com/developer/releases/petstore/) ? It was a sample application created by Sun for its [Java BluePrints](http://www.oracle.com/technetwork/java/javaee/blueprints/index.html) program. The Java Petstore was designed to illustrate how J2EE (and then Java EE) could be used to develop an eCommerce web application. Yes, the point of the Petstore is to sell pets online.

The Petstore had a huge momentum and we started to see plenty of Petstore-like applications flourish. The idea was to build an application with a certain technology. Let's face it, the J2EE version was far too complex using plenty of (today outdated) [design patterns](http://java.sun.com/blueprints/corej2eepatterns/). When I wrote my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee5-9782212120387) back in 2006, I decided to write a Petstore-like application but much simpler. But again, it's out-dated today.

So what you have here is another Petstore-like application but using [Java EE 7](http://jcp.org/en/jsr/detail?id=342) and all its goodies (CDI, EJB Lite, REST interface). It is based on the Petstore I developped for my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee-5-9782212126587) (sorry, it's written in French). I've updated it based on my [Java EE 6 book](http://www.apress.com/9781430228899/), and now I'm updating it again so it uses some new features of Java EE 7. The goals of this sample is to :

* use Java EE 7 and just Java EE 7 : no external framework or dependency, it even use the `java.util.logging` API ;o)
* make it simple : no complex business algorithm, the point is to bring Java EE 7 technologies together to create an eCommerce website

If you want to use a different web interface, external frameworks, add some sexy alternative JVM language… feel free to fork the code. But the goal of this EE 7 Petstore is to remain simple and to stick to Java EE 7.

The only external framework used are [Arquillian](http://arquillian.org/) and [Twitter Bootstrap](http://twitter.github.io/bootstrap/). Arquillian is used for integration testing. Using Maven profile, you can test services, injection, persistence... against different application servers. Twitter Bootstrap brings a bit of beauty to the web interface.

## Component diagram

## Glassfish 4.x

[Glassfish 4.x](http://glassfish.java.net) is the [Java EE 7](http://jcp.org/en/jsr/detail?id=342) reference implementation.

## Compile and package

Being Maven centric, you can compile and package it with `mvn clean compile`, `mvn clean package` or `mvn clean install`. The `package` and `install` phase will automatically trigger the unit tests. Once you have your war file, you can deploy it.

GlassFish is the default deployment application server, so you don't need to use any Maven profile. But if you wanted you could do `mvn -Pglassifh-embedded clean install`.

### Test with Glassfish embedded

Launching tests under [Glassfish](http://glassfish.java.net/public/downloadsindex.html) is straight forward. You only have to lauch :

    mvn clean install -Pglassfish4-embedded

Glassfish will launch during the build and tests will be executed in it. Other profiles will run the integration tests using other application servers or management modes :

    mvn clean install -Pglassfish4-embedded
    mvn clean install -Pglassfish4-managed
    mvn clean install -Pwildfly-embedded

### Deploy in Glassfish

This sample has been tested with GlassFish 4.x in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/applicationPetstore.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run` (you might have to increase Perm Gen space with `MAVEN_OPTS` set to `-XX:MaxPermSize=128m`)

## Execute the sample

Once deployed go to the following URL and start buying some pets: [http://localhost:8080/applicationPetstore](http://localhost:8080/applicationPetstore).
You can use these login/passwd :
marc/marc
bill/bill
jobs/jobs

The admin [REST interface](rs/application.wadl) allows you to create/update/remove items in the catalog, orders or customers. You can run the following [curl](http://curl.haxx.se/) commands :

* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/categories`
* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/products`
* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/items`

You can also get a JSON reprensetation as follow :

* `curl -X GET -H "accept: application/json" http://localhost:8080/applicationPetstore/rs/catalog/items`

## Third Party Tools & Frameworks

### Twitter Bootstrap

When, like me, you have no web designer skills at all and your web pages look ugly, you use [Twitter Bootstrap](http://twitter.github.com/bootstrap/) ;o)

## Silk Icons

I use [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) which are in Creative Commons

### Arquillian

### JRebel

[JRebel](http://zeroturnaround.com/software/jrebel/) is a JVM-plugin that makes it possible for Java developers to instantly see any code change made to an app without redeploying. It is very useful when you develop in a managed environment like application servers. If you need/want to use JRebel, just follow the [manual](http://zeroturnaround.com/software/jrebel/documentation/). To generate a rebel.xml file just do  `mvn jrebel:generate`

### Sonar

[Sonar](http://www.sonarsource.org/) provides services for continuous inspection of code quality. I use it to have some metrics on the Yaps Petstore (and produce, hopefully, not too ugly code with good code coverage). You can also use it to get some metrics. [Download](http://www.sonarsource.org/downloads/), [install](http://docs.codehaus.org/display/SONAR/Installing+Sonar) and run Sonar with `mvn -Pjacoco,glassfish3 install sonar:sonar` (or `mvn -Pjacoco,jboss7 install sonar:sonar` to run on JBoss 7). For integration testing we need to use [JaCoCo](http://www.eclemma.org/jacoco/). To view the code coverage of integartion tests, make sure you add the "Integration test coverage" widget to your Sonar dashboard (you need admin priviliges).

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
