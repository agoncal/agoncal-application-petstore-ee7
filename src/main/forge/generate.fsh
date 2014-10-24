#  ##############  #
#  Install Addons  #
#  ##############  #
#  If the following plugins are not installed
#  forge install-plugin arquillian



#  #####################  #
#  Creates a new project  #
#  #####################  #

project-new --named agoncal-application-petstore-ee7 --topLevelPackage org.agoncal.application.petstore --type war --finalName applicationPetstore ;


#  Setup the deployment descriptors to Java EE 7
#  ############
jpa-setup --persistenceUnitName injectionPU --jpaVersion 2.1 ;
cdi-setup --cdiVersion 1.1 ;
ejb-setup --ejbVersion 3.2 ;
faces-setup --facesVersion 2.2 ;
servlet-setup --servletVersion 3.1 ;



#  ###################  #
#  Creates constraints  #
#  ###################  #

constraint-new-annotation --named Email ;
constraint-new-annotation --named Login ;
constraint-new-annotation --named NotEmpty ;
constraint-new-annotation --named Price ;



#  ########################  #
#  Creates the domain model  #
#  ########################  #

#  TODO Command java-new-package-info to create a new package-info.java class [FORGE-2071]
#  Package Vetoed
#  ############
# java-new-package-info --targetPackage org.agoncal.application.petstore.model ;
# java-add-annotation --annotation javax.enterprise.inject.Vetoed --targetClass org.agoncal.application.petstore.model.package-info ;


#  Country entity
#  ############
jpa-new-entity --named Country ;
jpa-new-field --named isoCode --length 2 ;
jpa-new-field --named name --length 80 ;
jpa-new-field --named printableName --length 80 ;
jpa-new-field --named iso3 --length 3 ;
jpa-new-field --named numcode --length 3 ;

constraint-add --onProperty isoCode --constraint NotNull ;
constraint-add --onProperty isoCode --constraint NotNull Size --min 2 --max 2 ;
constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --min 2 --max 80 ;
constraint-add --onProperty printableName --constraint NotNull ;
constraint-add --onProperty printableName --constraint Size --min 2 --max 80 ;
constraint-add --onProperty iso3 --constraint NotNull ;
constraint-add --onProperty iso3 --constraint Size --min 3 --max 3 ;
constraint-add --onProperty numcode --constraint NotNull ;
constraint-add --onProperty numcode --constraint Size --min 3 --max 3 ;


#  Address embeddable
#  ############
jpa-new-embeddable --named Address ;
jpa-new-field --named street1 ;
jpa-new-field --named street2 ;
jpa-new-field --named city ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --columnName zip_code ;
# Relationships
jpa-new-field --named country --type org.agoncal.application.petstore.model.Country --relationshipType Many-to-One

constraint-add --onProperty street1 --constraint Size --min 5 --max 50 ;
constraint-add --onProperty street1 --constraint NotNull ;
constraint-add --onProperty city --constraint Size --min 2 --max 50 ;
constraint-add --onProperty city --constraint NotNull ;
constraint-add --onProperty zipcode --constraint Size --min 1 --max 10 ;
constraint-add --onProperty zipcode --constraint NotNull ;
constraint-add --onProperty country --constraint Size --min 2 --max 50 ;
constraint-add --onProperty country --constraint NotNull ;

#  Customer entity
#  ############
jpa-new-entity --named Customer ;
jpa-new-field --named login --length 10 ;
jpa-new-field --named password --length 256 ;
jpa-new-field --named firstName --length 50 --columnName first_name ;
jpa-new-field --named lastName --length 50 --columnName last_name ;
jpa-new-field --named telephone ;
jpa-new-field --named email ;
jpa-new-field --named dateOfBirth --type java.util.Date --temporalType DATE --columnName date_of_birth ;
jpa-new-field --named age --type java.lang.Integer --transient ;
# Address embeddable
jpa-new-field --named street1 --length 50 ;
jpa-new-field --named street2 ;
jpa-new-field --named city --length 50 ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --length 10 ;

constraint-add --onProperty login --constraint NotNull ;
constraint-add --onProperty password --constraint NotNull ;
constraint-add --onProperty password --constraint Size --min 1 --max 256 ;
constraint-add --onProperty firstName --constraint NotNull ;
constraint-add --onProperty firstName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty lastName --constraint NotNull ;
constraint-add --onProperty lastName --constraint Size --min 2 --max 50 ;
constraint-add --onProperty dateOfBirth --constraint Past ;


#  Category entity
#  ############
jpa-new-entity  --named Category ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description ;

constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --min 1 --max 30 ;
constraint-add --onProperty description --constraint NotNull ;


#  Product entity
#  ############
jpa-new-entity --named Product ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description ;
# Relationships
jpa-new-field --named category --type org.agoncal.application.petstore.model.Category --relationshipType Many-to-One ;

constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --min 1 --max 30 ;


#  Item entity
#  ############
jpa-new-entity --named Item ;
jpa-new-field --named name --length 30 ;
jpa-new-field --named description --length 3000 ;
jpa-new-field --named imagePath ;
jpa-new-field --named unitCost --typeName float ;
# Relationships
jpa-new-field --named product --type org.agoncal.application.petstore.model.Product --relationshipType Many-to-One ;

constraint-add --onProperty name --constraint NotNull ;
constraint-add --onProperty name --constraint Size --min 1 --max 30 ;
constraint-add --onProperty description --constraint Size --max 30 ;
constraint-add --onProperty unitCost --constraint NotNull ;
constraint-add --onProperty unitCost --constraint Min --value 1
constraint-add --onProperty imagePath --constraint NotNull ;


#  CreditCardType enumeration
#  ############
java-new-enum --named CreditCardType --targetPackage org.agoncal.application.petstore.model ;
java-new-enum-const VISA ;
java-new-enum-const MASTER_CARD ;
java-new-enum-const AMERICAN_EXPRESS ;


#  OrderLine entity
#  ############
jpa-new-entity --named OrderLine ;
jpa-new-field --named quantity --typeName int
# Relationships
jpa-new-field --named item --type org.agoncal.application.petstore.model.Item --relationshipType Many-to-One ;


#  PurchaseOrder entity
#  ############
jpa-new-entity --named PurchaseOrder ;
jpa-new-field --named orderDate --type java.util.Date --temporalType DATE --columnName order_date ;
# Address embeddable
jpa-new-field --named street1 --length 50 ;
jpa-new-field --named street2 ;
jpa-new-field --named city --length 50 ;
jpa-new-field --named state ;
jpa-new-field --named zipcode --length 10 ;
# Relationships
jpa-new-field --named customer --type org.agoncal.application.petstore.model.Customer --relationshipType Many-to-One ;
jpa-new-field --named orderLines --type org.agoncal.application.petstore.model.OrderLine --relationshipType One-to-Many ;



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
