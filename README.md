# Application - Petstore Java EE 7

* *Author* : [Antonio Goncalves](http://www.antoniogoncalves.org)
* *Level* : Intermediate
* *Technologies* : Java EE 7 (JPA 2.1, CDI 1.1, Bean Validation 1.1, EJB Lite 3.2, JSF 2.2, JAX-RS 2.0), Twitter Bootstrap (Bootstrap 3.x, JQuery 2.x, PrimeFaces 5.x)
* *Application Servers* : GlassFish 4.x, WildFly 8
* *Summary* : A Petstore-like application using Java EE 7

[Download the code from GitHub](https://github.com/agoncal/agoncal-application-petstore-ee7)

## Purpose of this application

Do you remember the good old Java [Petstore](http://java.sun.com/developer/releases/petstore/) ? It was a sample application created by Sun for its [Java BluePrints](http://www.oracle.com/technetwork/java/javaee/blueprints/index.html) program. The Java Petstore was designed to illustrate how J2EE (and then Java EE) could be used to develop an eCommerce web application. Yes, the point of the Petstore is to sell pets online. The Petstore had a huge momentum and we started to see plenty of Petstore-like applications flourish. The idea was to build an application with a certain technology. Let's face it, the J2EE version was far too complex using plenty of (today outdated) [design patterns](http://java.sun.com/blueprints/corej2eepatterns/). When I wrote my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee5-9782212120387) back in 2006, I decided to write a Petstore-like application but much simpler. But again, it's out-dated today.

What you have here is another Petstore-like application but using [Java EE 7](http://jcp.org/en/jsr/detail?id=342) and all its goodies (CDI, EJB Lite, REST interface). It is based on the Petstore I developed for my [Java EE 5 book](http://www.eyrolles.com/Informatique/Livre/java-ee-5-9782212126587) (sorry, it's written in French). I've updated it based on my [Java EE 6 book](http://www.amazon.com/gp/product/143022889X/ref=as_li_qf_sp_asin_il_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=143022889X&linkCode=as2&tag=antgonblo-20), and now I'm updating it again so it uses some new features of Java EE 7 described on my [Java EE 7 book](http://www.amazon.com/gp/product/143024626X/ref=as_li_qf_sp_asin_il_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=143024626X&linkCode=as2&tag=antgonblo-20). The goals of this sample is to :

* use Java EE 7 and just Java EE 7 : no external framework or dependency (except web frameworks or logging APIs)
* make it simple : no complex business algorithm, the point is to bring Java EE 7 technologies together to create an eCommerce website

If you want to use a different web interface, external frameworks, add some sexy alternative JVM language… feel free to fork the code. But the goal of this EE 7 Petstore is to remain simple and to stick to Java EE 7.

The only external framework used are [Arquillian](http://arquillian.org/), [Twitter Bootstrap](http://twitter.github.io/bootstrap/) and [PrimeFaces](http://www.primefaces.org/). Arquillian is used for integration testing. Using Maven profile, you can test services, injection, persistence... against different application servers. Twitter Bootstrap and PrimeFaces bring a bit of beauty to the web interface.

## Component diagram

## Glassfish 4.x

[Glassfish 4.x](http://glassfish.java.net) is the [Java EE 7](http://jcp.org/en/jsr/detail?id=342) reference implementation.

## Compile and package

Being Maven centric, you can compile and package it without tests using `mvn clean compile -Dmaven.test.skip=true`, `mvn clean package -Dmaven.test.skip=true` or `mvn clean install -Dmaven.test.skip=true`. Once you have your war file, you can deploy it.

### Test with Arquillian

Launching tests under [WildFly](http://www.wildfly.org/) is straight forward. You only have to launch WidlFly and execute the tests using the Maven profile :

    mvn clean test -Parquillian-wildfly-remote

### Deploy in Glassfish

This sample has been tested with GlassFish 4.x in several modes :

* GlassFish runtime : [download GlassFish](http://glassfish.java.net/public/downloadsindex.html), install it, start GlassFish (typing `asadmin start-`domain) and once the application is packaged deploy it (using the admin console or the command line `asadmin deploy target/applicationPetstore.war`)
* GlassFish embedded : use the [GlassFish Maven Plugin](http://maven-glassfish-plugin.java.net/) by running `mvn clean package embedded-glassfish:run` (you might have to increase Perm Gen space with `MAVEN_OPTS` set to `-XX:MaxPermSize=128m`)

## Execute the sample

Once deployed go to the following URL and start buying some pets: [http://localhost:8080/applicationPetstore](http://localhost:8080/applicationPetstore).

The admin [REST interface](rs/application.wadl) allows you to create/update/remove items in the catalog, orders or customers. You can run the following [curl](http://curl.haxx.se/) commands :

* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/categories`
* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/products`
* `curl -X GET http://localhost:8080/applicationPetstore/rs/catalog/items`

You can also get a JSON representation as follow :

* `curl -X GET -H "accept: application/json" http://localhost:8080/applicationPetstore/rs/catalog/items`

## Test this application on CloudBees

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/cyrille-leclerc/agoncal-application-petstore-ee7/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>


## Third Party Tools & Frameworks

### Twitter Bootstrap

When, like me, you have no web designer skills at all and your web pages look ugly, you use [Twitter Bootstrap](http://twitter.github.com/bootstrap/) ;o)

## Silk Icons

I use [Silk Icons](http://www.famfamfam.com/lab/icons/silk/) which are in Creative Commons

### Arquillian

[Arquillian](http://arquillian.org/) for the integration tests.

### Sonar

[Sonar](http://www.sonarsource.org/) provides services for continuous inspection of code quality. I use it to have some metrics on the Yaps Petstore (and produce, hopefully, not too ugly code with good code coverage). You can also use it to get some metrics. [Download](http://www.sonarsource.org/downloads/), [install](http://docs.codehaus.org/display/SONAR/Installing+Sonar) and run Sonar with `mvn -Pjacoco,glassfish3 install sonar:sonar` (or `mvn -Pjacoco,jboss7 install sonar:sonar` to run on JBoss 7). For integration testing we need to use [JaCoCo](http://www.eclemma.org/jacoco/). To view the code coverage of integartion tests, make sure you add the "Integration test coverage" widget to your Sonar dashboard (you need admin privileges).

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
