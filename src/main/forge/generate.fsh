@/* Generates the draft of the Petstore application */ ;
@/* ================= */ ;
@/* ==   Plugins   == */ ;
@/* ================= */ ;
@/* If the following plugins are not installed */ ;
@/* forge install-plugin arquillian */ ;
@/* forge install-plugin jrebel */ ;
@/* forge install-plugin primefaces */ ;


clear ;
set ACCEPT_DEFAULTS true ;


@/* ========================== */;
@/* == Creating the project == */;
@/* ========================== */;

new-project --named agoncal-application-petstore-ee7 --topLevelPackage org.agoncal.application.petstore --type war --finalName applicationPetstore ;


@/* ================================ */;
@/* == Setting up the persistence == */;
@/* ================================ */;

@/* Setup JPA */;
persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named applicationPetstorePU ;


@/* ================================ */;
@/* == Setting up Bean Validation == */;
@/* ================================ */;

@/* Setup Bean Validation */;
validation setup ;


@/* ======================= */;
@/* == Creating Entities == */;
@/* ======================= */;

@/* Country */;
entity --named Country ;
field string --named isoCode --length 2 ;
field string --named name --length 80 ;
field string --named printableName --length 80 ;
field string --named iso3 --length 3 ;
field string --named numcode --length 3 ;

constraint NotNull --onProperty isoCode ;
constraint Size --min 2 --max 2 --onProperty isoCode ;
constraint NotNull --onProperty name ;
constraint Size --min 2 --max 80 --onProperty name ;
constraint NotNull --onProperty printableName ;
constraint Size --min 2 --max 80 --onProperty printableName ;
constraint NotNull --onProperty iso3 ;
constraint Size --min 3 --max 3 --onProperty iso3 ;
constraint NotNull --onProperty numcode ;
constraint Size --min 3 --max 3 --onProperty numcode ;


@/* Customer */;
entity --named Customer ;
field string --named login --length 10 ;
field string --named password --length 256 ;
field string --named firstname --length 50 ;
field string --named lastname --length 50 ;
field string --named telephone ;
field string --named email ;
field temporal --type DATE --named dateOfBirth ;
field string --named street1 --length 50 ;
field string --named street2 ;
field string --named city --length 50 ;
field string --named state ;
field string --named zipcode --length 10 ;
field oneToOne --named country --fieldType org.agoncal.application.petstore.model.Country.java ;

constraint NotNull --onProperty login ;
constraint NotNull --onProperty password ;
constraint Size --min 1 --max 256 --onProperty password ;
constraint NotNull --onProperty firstname ;
constraint Size --min 2 --max 50 --onProperty firstname ;
constraint NotNull --onProperty lastname ;
constraint Size --min 2 --max 50 --onProperty lastname ;
constraint NotNull --onProperty street1 ;
constraint Size --min 5 --max 50 --onProperty street1 ;
constraint NotNull --onProperty city ;
constraint Size --min 5 --max 50 --onProperty city ;
constraint NotNull --onProperty zipcode ;
constraint Size --min 1 --max 10 --onProperty zipcode ;


@/* Category */;
entity --named Category ;
field string --named name --length 30 ;
field string --named description ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;
constraint NotNull --onProperty description ;


@/* Product */;
entity --named Product ;
field string --named name --length 30 ;
field string --named description ;
field manyToOne --named category --fieldType org.agoncal.application.petstore.model.Category ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;


@/* Item */;
entity --named Item ;
field string --named name --length 30 ;
field string --named description --length 3000 ;
field manyToOne --named category --fieldType org.agoncal.application.petstore.model.Category ;
field custom --named unitCost --type java.lang.Float ;
field string --named imagePath ;
field manyToOne --named product --fieldType org.agoncal.application.petstore.model.Product ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;
constraint Size --min 1 --max 3000 --onProperty description ;
constraint NotNull --onProperty unitCost ;
constraint NotNull --onProperty imagePath ;

@/* Category, Product & Item relation */;
cd ../Category.java ;
field oneToMany --named products --fieldType org.agoncal.application.petstore.model.Product ;
cd ../Product.java ;
field oneToMany --named items --fieldType org.agoncal.application.petstore.model.Item ;


@/* OrderLine */;
entity --named OrderLine ;
field int --named quantity ;
field oneToOne --named item --fieldType org.agoncal.application.petstore.model.Item.java ;

@/* CreditCardType */;
java new-enum-type --named CreditCardType --package org.agoncal.application.petstore.model ;
java new-enum-const VISA ;
java new-enum-const AMERICAN_EXPRESS ;
java new-enum-const MASTER_CARD ;


@/* Order */;
entity --named Order ;
field temporal --type DATE --named orderDate ;
field manyToOne --named customer --fieldType org.agoncal.application.petstore.model.Customer.java ;
field oneToMany --named orderLines --fieldType org.agoncal.application.petstore.model.OrderLine.java ;
field string --named street1 --length 50 ;
field string --named street2 ;
field string --named city --length 50 ;
field string --named state ;
field string --named zipcode --length 10 ;
field string --named creditCardNumber --length 30 ;
field custom --named creditCardType --type org.agoncal.application.petstore.model.CreditCardType.java ;
field string --named creditCardExpDate --length 5 ;

constraint NotNull --onProperty street1 ;
constraint Size --min 5 --max 50 --onProperty street1 ;
constraint NotNull --onProperty city ;
constraint Size --min 5 --max 50 --onProperty city ;
constraint NotNull --onProperty zipcode ;
constraint Size --min 1 --max 10 --onProperty zipcode ;
constraint NotNull --onProperty creditCardNumber ;
constraint Size --min 1 --max 30 --onProperty creditCardNumber ;
constraint NotNull --onProperty creditCardType ;
constraint NotNull --onProperty creditCardExpDate ;
constraint Size --min 5 --max 5 --onProperty creditCardExpDate ;


@/* ===================== */;
@/* == Setting up REST == */;
@/* ===================== */;

@/* Setup JAX-RS */;
rest setup ;


@/* ============================= */;
@/* == Creating REST endpoints == */;
@/* ============================= */;

@/* Generate CRUD endpoints for all the @Entities */;
rest endpoint-from-entity ~.model.* ;


@/* ==================== */;
@/* == Setting up JSF == */;
@/* ==================== */;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA */;
scaffold setup --scaffoldType faces ;

@/* Generate the UI for all the @Entities */ ;
scaffold from-entity ~.model.* ;


@/* ========================== */;
@/* == Building the project == */;
@/* ========================== */;

build --notest ;


@/* ================ */;
@/* == Arquillian == */;
@/* ================ */;

arquillian setup --containerType EMBEDDED --containerName GLASSFISH_EMBEDDED_3.1 ;
arquillian create-test --class org.agoncal.application.petstore.rest.CategoryEndpoint.java ;


@/* =================================== */;
@/* == Returning to the project root == */;
@/* =================================== */;

set ACCEPT_DEFAULTS false ;
cd ~~ ;
