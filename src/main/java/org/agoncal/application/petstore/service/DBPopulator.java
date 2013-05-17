package org.agoncal.application.petstore.service;

import org.agoncal.application.petstore.domain.*;
import org.agoncal.application.petstore.util.Loggable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Singleton
@Startup
@Loggable
@DataSourceDefinition(
        className = "org.apache.derby.jdbc.EmbeddedDataSource",
        name = "java:global/jdbc/applicationPetstoreDS",
        user = "app",
        password = "app",
        databaseName = "applicationPetstoreDB",
        properties = {"connectionAttributes=;create=true"}
)
public class DBPopulator {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Category fish;
    private Category dog;
    private Category reptile;
    private Category cat;
    private Category bird;
    private Customer marc;
    private Customer bill;
    private Customer steve;
    private Customer user;
    private Customer admin;

    @Inject
    private CatalogService catalogService;

    @Inject
    private CustomerService customerService;

    // ======================================
    // =          Lifecycle Methods         =
    // ======================================

    @PostConstruct
    private void populateDB() {
        initCatalog();
        initCustomers();
    }

    @PreDestroy
    private void clearDB() {
        catalogService.removeCategory(fish);
        catalogService.removeCategory(dog);
        catalogService.removeCategory(reptile);
        catalogService.removeCategory(cat);
        catalogService.removeCategory(bird);
        customerService.removeCustomer(marc);
        customerService.removeCustomer(bill);
        customerService.removeCustomer(steve);
        customerService.removeCustomer(user);
        customerService.removeCustomer(admin);
    }

    // ======================================
    // =           Private Methods          =
    // ======================================

    private void initCatalog() {

        // Categories
        fish = new Category("Fish", "Any of numerous cold-blooded aquatic vertebrates characteristically having fins, gills, and a streamlined body");
        dog = new Category("Dogs", "A domesticated carnivorous mammal related to the foxes and wolves and raised in a wide variety of breeds");
        reptile = new Category("Reptiles", "Any of various cold-blooded, usually egg-laying vertebrates, such as a snake, lizard, crocodile, turtle");
        cat = new Category("Cats", " Small carnivorous mammal domesticated since early times as a catcher of rats and mice and as a pet and existing in several distinctive breeds and varieties");
        bird = new Category("Birds", "Any of the class Aves of warm-blooded, egg-laying, feathered vertebrates with forelimbs modified to form wings");

        // Products
        Product angelfish = new Product("Angelfish", "Saltwater fish from Australia", fish);
        Product tigerShark = new Product("Tiger Shark", "Saltwater fish from Australia", fish);
        Product koi = new Product("Koi", "Freshwater fish from Japan", fish);
        Product goldfish = new Product("Goldfish", "Freshwater fish from China", fish);
        fish.addProduct(angelfish);
        fish.addProduct(tigerShark);
        fish.addProduct(koi);
        fish.addProduct(goldfish);

        Product bulldog = new Product("Bulldog", "Friendly dog from England", dog);
        Product poodle = new Product("Poodle", "Cute dog from France", dog);
        Product dalmation = new Product("Dalmation", "Great dog for a fire station", dog);
        Product goldenRetriever = new Product("Golden Retriever", "Great family dog", dog);
        Product labradorRetriever = new Product("Labrador Retriever", "Great hunting dog", dog);
        Product chihuahua = new Product("Chihuahua", "Great companion dog", dog);
        dog.addProduct(bulldog);
        dog.addProduct(poodle);
        dog.addProduct(dalmation);
        dog.addProduct(goldenRetriever);
        dog.addProduct(labradorRetriever);
        dog.addProduct(chihuahua);

        Product rattlesnake = new Product("Rattlesnake", "Doubles as a watch dog", reptile);
        Product iguana = new Product("Iguana", "Friendly green friend", reptile);
        reptile.addProduct(rattlesnake);
        reptile.addProduct(iguana);

        Product manx = new Product("Manx", "Great for reducing mouse populations", cat);
        Product persian = new Product("Persian", "Friendly house cat, doubles as a princess", cat);
        cat.addProduct(manx);
        cat.addProduct(persian);

        Product amazonParrot = new Product("Amazon Parrot", "Great companion for up to 75 years", bird);
        Product finch = new Product("Finch", "Great stress reliever", bird);
        bird.addProduct(amazonParrot);
        bird.addProduct(finch);

        // Items
        Item largeAngelfish = new Item("Large", 10.00f, "fish1.jpg", angelfish, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum velit ante, malesuada porta condimentum eget, tristique id magna. Donec ac justo velit. Suspendisse potenti. Donec vulputate vulputate molestie. Quisque vitae arcu massa, dictum sodales leo. Sed feugiat elit vitae ante auctor ultrices. Duis auctor consectetur arcu id faucibus. Curabitur gravida.");
        Item thootlessAngelfish = new Item("Thootless", 10.00f, "fish1.jpg", angelfish, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla pharetra dignissim. In imperdiet, lacus a vehicula dignissim, ante ligula euismod leo, non lobortis orci quam a nisl. Aliquam risus eros, molestie sit amet interdum nec, convallis malesuada leo. Quisque bibendum facilisis erat eget tincidunt. Phasellus pharetra gravida purus. Maecenas.");
        angelfish.addItem(largeAngelfish);
        angelfish.addItem(thootlessAngelfish);
        Item spottedTigerShark = new Item("Spotted", 12.00f, "fish4.jpg", tigerShark, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque dictum iaculis sapien, eu fermentum eros feugiat a. Pellentesque ultricies mauris orci. Mauris interdum hendrerit felis vel dignissim. Phasellus ac sem sit amet ante laoreet volutpat. Sed sagittis venenatis ullamcorper. Vivamus non mollis nunc. Etiam mauris odio, tristique sed porta in.");
        Item spotlessTigerShark = new Item("Spotless", 12.00f, "fish4.jpg", tigerShark, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In hendrerit ultricies bibendum. Vestibulum vitae dui porttitor nibh dignissim pretium eu at odio. Proin ac nibh eget erat ullamcorper consequat ac cursus est. Donec sollicitudin interdum elit sed gravida. Integer lacus lacus, gravida eget vehicula ac, sagittis et dui. In et.");
        tigerShark.addItem(spottedTigerShark);
        tigerShark.addItem(spotlessTigerShark);
        Item maleKoi = new Item("Male Adult", 12.00f, "fish3.jpg", koi, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi consectetur, ligula eu malesuada tempus, risus tellus varius ligula, id auctor magna tellus quis dui. Integer ut neque ut libero aliquet hendrerit. Maecenas bibendum, magna sed vulputate tempor, tortor neque consequat nunc, id consectetur neque odio eget augue. Ut consectetur, nisl.");
        Item femaleKoi = new Item("Female Adult", 12.00f, "fish3.jpg", koi, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec et porta eros. Aliquam neque arcu, sodales eget rutrum a, luctus sit amet sem. Vestibulum ultricies dictum mi, eu sagittis lacus ultrices sit amet. Mauris nec interdum ipsum. Maecenas semper, magna sit amet commodo tempus, purus lectus pretium dui, sit amet.");
        koi.addItem(maleKoi);
        koi.addItem(femaleKoi);
        Item maleGoldfish = new Item("Male Puppy", 12.00f, "fish2.jpg", goldfish, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ac nunc mauris. Proin augue sem, imperdiet quis imperdiet vitae, egestas vitae quam. Nam id lectus nisi. In hac habitasse platea dictumst. Proin ullamcorper eros non diam accumsan ornare. Fusce posuere, nulla vel tempor molestie, lectus dui aliquet orci, in volutpat.");
        Item femaleGoldfish = new Item("Female Puppy", 12.00f, "fish2.jpg", goldfish, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc pretium ornare est ullamcorper porta. Nullam eleifend tincidunt justo nec ultrices. In vehicula pharetra turpis, nec consequat sapien tempus sit amet. Donec quis urna in odio luctus rhoncus. In metus lorem, ultricies vel vestibulum non, laoreet ac neque. Duis posuere, tortor.");
        goldfish.addItem(maleGoldfish);
        goldfish.addItem(femaleGoldfish);

        Item maleBulldog = new Item("Spotless Male Puppy", 22.00f, "dog1.jpg", bulldog, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce et lorem vel tellus aliquet pretium ut nec libero. Cras euismod tincidunt rutrum. Suspendisse nisl justo, vestibulum et commodo vel, ultricies placerat quam. Sed nisi orci, rhoncus ac accumsan eget, pretium ac purus. Nam et scelerisque mi. Vivamus luctus, massa eget.");
        Item femaleBulldog = new Item("Spotless Female Puppy", 22.00f, "dog1.jpg", bulldog, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam rhoncus arcu sed sapien interdum porttitor. Nulla nunc magna, egestas sed laoreet nec, congue et felis. Donec rhoncus, est vitae tincidunt posuere, dolor nunc fermentum orci, ut varius velit ipsum a massa. Pellentesque habitant morbi tristique senectus et netus et malesuada.");
        bulldog.addItem(maleBulldog);
        bulldog.addItem(femaleBulldog);
        Item malePoodle = new Item("Spotted Male Puppy", 32.00f, "dog2.jpg", poodle, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis ipsum erat, tincidunt sit amet lacinia non, vestibulum elementum odio. Donec id lacus ante, id bibendum est. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Etiam eu suscipit mauris. Vivamus dolor diam, pulvinar a consectetur at.");
        Item femalePoodle = new Item("Spotted Female Puppy", 32.00f, "dog2.jpg", poodle, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi nec justo orci, in faucibus lectus. Proin feugiat faucibus pellentesque. Etiam nec dolor justo, non egestas nisl. Etiam convallis orci nec felis pretium malesuada. Maecenas nec tortor erat. Cras accumsan eros sit amet nibh fringilla molestie. Suspendisse potenti. Nulla vulputate neque.");
        poodle.addItem(malePoodle);
        poodle.addItem(femalePoodle);
        Item tailedDalmation = new Item("Tailed", 62.00f, "dog3.jpg", dalmation, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ante massa, semper non tempor at, faucibus nec est. Aliquam aliquet, tortor ut egestas blandit, nisi urna elementum lectus, a porta dolor leo quis massa. Aliquam erat volutpat. Fusce sed eros et enim varius consequat. Nam molestie, neque quis commodo rhoncus.");
        Item tailessDalmation = new Item("Tailless", 62.00f, "dog3.jpg", dalmation, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus ac adipiscing nulla. Proin risus lectus, convallis eu sagittis scelerisque, fringilla ut odio. Suspendisse ultrices ullamcorper adipiscing. Proin ac suscipit tellus. Vivamus tempus nibh interdum ipsum ullamcorper at suscipit nibh mattis. Vivamus elementum volutpat ipsum eu tempus. Proin velit ligula, fringilla.");
        dalmation.addItem(tailedDalmation);
        dalmation.addItem(tailessDalmation);
        Item tailedGoldenRetriever = new Item("Tailed", 82.00f, "dog4.jpg", goldenRetriever, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin sit amet velit id augue pellentesque tempor suscipit eu nisi. Nulla facilisi. Sed ultrices lectus in ligula viverra lacinia. Quisque et leo nisl. Suspendisse potenti. Donec semper malesuada ullamcorper. Vivamus fringilla nunc eget tellus condimentum ut dictum nisi euismod. Pellentesque habitant.");
        Item tailessGoldenRetriever = new Item("Tailless", 82.00f, "dog4.jpg", goldenRetriever, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In eget justo odio. Phasellus suscipit auctor lectus eget luctus. Nam ultricies auctor augue vel feugiat. Nulla odio lectus, volutpat sit amet vestibulum id, convallis sit amet tellus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Quisque.");
        goldenRetriever.addItem(tailedGoldenRetriever);
        goldenRetriever.addItem(tailessGoldenRetriever);
        Item tailedLabradorRetriever = new Item("Tailed", 100.00f, "dog5.jpg", labradorRetriever, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent lobortis volutpat nunc, in sodales felis condimentum a. Quisque quis neque commodo elit consequat porttitor. Integer nec scelerisque nisi. Aliquam velit lorem, egestas sit amet sodales sit amet, gravida ut lorem. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices.");
        Item tailessLabradorRetriever = new Item("Tailless", 100.00f, "dog5.jpg", labradorRetriever, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin tortor mauris, sodales sodales pretium vitae, egestas eget mi. Ut hendrerit, libero et feugiat tristique, ligula nunc varius sem, non tristique mi ante a turpis. Suspendisse potenti. Nunc fringilla imperdiet nibh, eu sodales nisl pellentesque eu. Curabitur dictum vestibulum elit ut.");
        labradorRetriever.addItem(tailedLabradorRetriever);
        labradorRetriever.addItem(tailessLabradorRetriever);
        Item maleChihuahua = new Item("Male Adult", 100.00f, "dog6.jpg", chihuahua, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere porta risus, a bibendum enim pellentesque sit amet. Mauris imperdiet suscipit lectus, sed molestie orci posuere a. Fusce eleifend interdum nisi, nec vulputate velit rutrum ut. Nulla turpis ligula, fermentum ac tincidunt at, porttitor sit amet sem. Curabitur eget eros.");
        Item femaleChihuahua = new Item("Female Adult", 100.00f, "dog6.jpg", chihuahua, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras in diam sapien. Etiam sed dapibus velit. Phasellus gravida egestas congue. Etiam nec nunc non arcu facilisis ultrices. Curabitur et diam sed neque facilisis dignissim. Vestibulum accumsan viverra nunc, ac tincidunt nisi placerat sit amet. Nulla ac pellentesque justo. Aliquam pellentesque.");
        chihuahua.addItem(maleChihuahua);
        chihuahua.addItem(femaleChihuahua);

        Item femaleRattlesnake = new Item("Female Adult", 20.00f, "reptile1.jpg", rattlesnake, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent lobortis ante et nunc scelerisque aliquet. Phasellus sed auctor purus. Cras tempus lacus eget felis viverra scelerisque. Sed ac tellus vitae nisl vehicula feugiat ac vitae dolor. Duis interdum lorem quis risus ullamcorper id cursus magna pharetra. Sed et nisi odio.");
        Item maleRattlesnake = new Item("Male Adult", 20.00f, "reptile1.jpg", rattlesnake, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris pharetra tempus vulputate. Proin at nibh at felis feugiat fringilla. Fusce suscipit malesuada urna posuere suscipit. Integer non quam orci, vel adipiscing odio. Aenean at turpis nisi, a ullamcorper massa. Integer consectetur libero a lorem blandit pretium. Curabitur a sodales justo.");
        rattlesnake.addItem(femaleRattlesnake);
        rattlesnake.addItem(maleRattlesnake);
        Item femaleIguana = new Item("Female Adult", 150.00f, "lizard1.jpg", iguana, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin dictum, nisi vitae fringilla ultrices, est ipsum faucibus ipsum, sit amet dapibus erat ipsum et arcu. Sed euismod, mauris suscipit placerat semper, tortor magna cursus nulla, id elementum dui dolor sit amet nunc. Pellentesque a interdum lectus. Mauris in augue eu.");
        Item maleIguana = new Item("Male Adult", 160.00f, "lizard1.jpg", iguana, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus at dapibus arcu. Nunc at dui sem, in fringilla velit. Suspendisse mauris felis, molestie scelerisque viverra sit amet, dapibus eu diam. Curabitur egestas lectus et ligula pharetra in sollicitudin neque tristique. Nunc suscipit scelerisque nunc, vitae consectetur justo sodales ullamcorper. Nulla.");
        iguana.addItem(femaleIguana);
        iguana.addItem(maleIguana);

        Item maleManx = new Item("Male Adult", 120.00f, "cat1.jpg", manx, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis scelerisque urna. Sed id nunc quis nisl scelerisque scelerisque sit amet id lorem. Sed rutrum arcu sed sem semper id eleifend nulla feugiat. Praesent faucibus dignissim lectus tincidunt lacinia. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per.");
        Item femaleManx = new Item("Female Adult", 120.00f, "cat1.jpg", manx, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus viverra nunc vitae libero ultricies lobortis. Duis magna nunc, tincidunt sit amet sagittis et, lobortis volutpat risus. Sed gravida turpis sit amet arcu tincidunt convallis. Nunc vulputate commodo mi non blandit. Etiam eu libero id libero aliquet pretium. Lorem ipsum dolor.");
        manx.addItem(maleManx);
        manx.addItem(femaleManx);
        Item malePersian = new Item("Male Adult", 70.00f, "cat2.jpg", persian, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut sed est in tortor pharetra fermentum. Pellentesque nulla augue, venenatis ut viverra vel, dignissim sit amet ante. Aliquam erat volutpat. Aenean lectus odio, blandit aliquam sollicitudin a, pulvinar a felis. Phasellus vitae libero et lacus volutpat tristique. Aliquam tortor lacus, pulvinar.");
        Item femalePersian = new Item("Female Adult", 90.00f, "cat2.jpg", persian, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam fringilla iaculis nunc et hendrerit. Curabitur malesuada felis non velit ultrices lacinia. Vivamus hendrerit tortor et tortor faucibus vehicula. Pellentesque pellentesque, quam at viverra tristique, lacus nibh euismod erat, vel vestibulum purus turpis eget nisi. Donec suscipit ligula tortor, a suscipit.");
        persian.addItem(malePersian);
        persian.addItem(femalePersian);

        Item maleAmazonParrot = new Item("Male Adult", 120.00f, "bird2.jpg", amazonParrot, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In justo ligula, volutpat ut adipiscing sed, lobortis vel lacus. Etiam commodo aliquet libero, sit amet pretium risus scelerisque luctus. Suspendisse sit amet nulla nibh, in mollis risus. Curabitur convallis mattis felis, non malesuada justo pretium sed. Nam vestibulum, urna in consequat.");
        Item femaleAmazonParrot = new Item("Female Adult", 120.00f, "bird2.jpg", amazonParrot, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse vitae turpis ut erat hendrerit sollicitudin. Curabitur auctor neque a enim scelerisque mattis. Mauris in mi nibh, et placerat lorem. Nunc semper, quam vitae semper condimentum, odio arcu sagittis ligula, eu posuere arcu nibh a quam. Aliquam porta dictum eros auctor.");
        amazonParrot.addItem(maleAmazonParrot);
        amazonParrot.addItem(femaleAmazonParrot);
        Item maleFinch = new Item("Male Adult", 75.00f, "bird1.jpg", finch, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum quis lectus sit amet augue mattis malesuada. Maecenas justo justo, auctor sed consectetur et, pulvinar et diam. Nam felis mi, auctor ornare accumsan sed, pharetra nec arcu. Aliquam tincidunt nisi feugiat dui commodo dapibus. Nullam eget augue odio. Duis mauris nibh.");
        Item femaleFinch = new Item("Female Adult", 80.00f, "bird1.jpg", finch, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus dignissim vehicula tellus. Vestibulum id diam eros. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nam sit amet sem at ligula pretium fermentum. Suspendisse potenti. Phasellus rhoncus consequat augue, ac feugiat felis gravida nec. Aliquam at.");
        finch.addItem(maleFinch);
        finch.addItem(femaleFinch);

        catalogService.createCategory(fish);
        catalogService.createCategory(dog);
        catalogService.createCategory(reptile);
        catalogService.createCategory(cat);
        catalogService.createCategory(bird);
    }

    private void initCustomers() {
        marc = new Customer("Marc", "Fleury", "marc", "marc", "marc@jboss.org", new Address("65 Ritherdon Road", "Los Angeles", "56421", "USA"));
        bill = new Customer("Bill", "Gates", "bill", "bill", "bill.gates@microsoft.com", new Address("27 West Side", "Alhabama", "8401", "USA"));
        steve = new Customer("Steve", "Jobs", "jobs", "jobs", "steve.jobs@apple.com", new Address("154 Star Boulevard", "San Francisco", "5455", "USA"));
        user = new Customer("User", "User", "user", "user", "user@petstore.org", new Address("Petstore", "Land", "666", "Nowhere"));
        admin = new Customer("Admin", "Admin", "admin", "admin", "admin@petstore.org", new Address("Petstore", "Land", "666", "Nowhere"));

        customerService.createCustomer(marc);
        customerService.createCustomer(bill);
        customerService.createCustomer(steve);
        customerService.createCustomer(user);
        customerService.createCustomer(admin);
    }

}
