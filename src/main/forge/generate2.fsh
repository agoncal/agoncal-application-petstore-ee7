@/* Forge 2.x Script */ ;
@/* Generates the draft of the Petstore application */ ;

@/* ================= */ ;
@/* ==   Plugins   == */ ;
@/* ================= */ ;
@/* If the following plugins are not installed */ ;
@/* forge install-plugin arquillian */ ;
@/* forge install-plugin jrebel */ ;
@/* forge install-plugin primefaces */ ;


clear ;
export ACCEPT_DEFAULTS=true ;


@/* ========================== */;
@/* == Creating the project == */;
@/* ========================== */;

project-new --named agoncal-application-petstore-ee7 --topLevelPackage org.agoncal.application.petstore --type war ; @/* --finalName applicationPetstore */;


@/* ================================ */;
@/* == Setting up the persistence == */;
@/* ================================ */;

@/* Setup JPA */;
@/* persistence setup --provider ECLIPSELINK --container GLASSFISH_3 --named applicationPetstorePU */;


@/* ================================ */;
@/* == Setting up Bean Validation == */;
@/* ================================ */;

@/* Setup Bean Validation */;
validation setup ;


@/* ======================= */;
@/* == Creating Entities == */;
@/* ======================= */;

@/* Country */;
jpa-new-entity --named Country ;
jpa-new-field --named isoCode --length 2 ;
jpa-new-field --named name --length 80 ;
jpa-new-field --named printableName --length 80 ;
jpa-new-field --named iso3 --length 3 ;
jpa-new-field --named numcode --length 3 ;

@/*
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
*/;

@/* Customer */;
jpa-new-entity --named Customer ;
jpa-new-field --named login --length 10 ;
jpa-new-field --named password --length 256 ;
jpa-new-field --named firstname --length 50 ;
jpa-new-field --named lastname --length 50 ;
jpa-new-field --named telephone ;
jpa-new-field --named email ;
jpa-new-field --named street1 --length 50 ;
jpa-new-field --named street2 ;
jpa-new-field --named city --length 50 ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --length 10 ;
@/* field temporal --named dateOfBirth --type DATE ; */;
jpa-new-field --named country --entity org.agoncal.application.petstore.model.Country --relationshipType Many-to-One

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
jpa-new-entity  --named Category ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;
constraint NotNull --onProperty description ;


@/* Product */;
jpa-new-entity --named Product ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description ;
jpa-new-field --named category --entity org.agoncal.application.petstore.model.Category --relationshipType Many-to-One ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;


@/* Item */;
jpa-new-entity --named Item ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description --length 3000 ;
jpa-new-field --named imagePath ;
jpa-new-field --named unitCost --typeName float ;
jpa-new-field --named product --entity org.agoncal.application.petstore.model.Product --relationshipType Many-to-One ;

constraint NotNull --onProperty name ;
constraint Size --min 1 --max 30 --onProperty name ;
constraint Size --min 1 --max 3000 --onProperty description ;
constraint NotNull --onProperty unitCost ;
constraint NotNull --onProperty imagePath ;


@/* CreditCardType */;
java new-enum-type --named CreditCardType --package org.agoncal.application.petstore.model ;
java new-enum-const VISA ;
java new-enum-const AMERICAN_EXPRESS ;
java new-enum-const MASTER_CARD ;


@/* OrderLine */;
jpa-new-entity --named OrderLine ;
jpa-new-field --named quantity --typeName int
jpa-new-field --named item --entity org.agoncal.application.petstore.model.Item --relationshipType One-to-One ;


@/* PurchaseOrder */;
jpa-new-entity --named PurchaseOrder ;
field temporal --type DATE --named orderDate ;
field manyToOne --named customer --fieldType org.agoncal.application.petstore.model.Customer.java ;
field oneToMany --named orderLines --fieldType org.agoncal.application.petstore.model.OrderLine.java ;
jpa-new-field --named street1 --length 50 ;
jpa-new-field --named street2 ;
jpa-new-field --named city --length 50 ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --length 10 ;
jpa-new-field --named creditCardNumber --length 30 ;
field custom --named creditCardType --type org.agoncal.application.petstore.model.CreditCardType.java ;
jpa-new-field --named creditCardExpDate --length 5 ;

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
scaffold from-entity ~.model.* --targetDir admin;


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
