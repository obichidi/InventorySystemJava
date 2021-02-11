package Main;

import Controller.MainController;
import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;





/**
 * This is the main program class that runs the entire inventory system.
 *  FUTURE ENHANCEMENT:
 *  I would expand the inventory system by adding a file parser where it can read information from
 *   cvs files or json files and parse the information into the inventory
 *  */
public class Main extends Application {


    /**
     * @author Obiajulu Chidi
     * This method launches the main program
     *   @param args are the main arguments
     */

    public static void main(String[] args) {
        launch(args);
    }

    /** This method initializes the beginning stage and adds test data to the inventory. It also throws an exception if there is an error
     * RUNTIME ERROR:
     * The runtime error I was having for this is that I was declaring the fxml file controller in the .fxml
     * file because of an error. The program would not run.  the controller was already declared.
     * I removed the controller declarations in the .fxml file and that enabled the program to run*/
    @Override
    public void start(Stage stage) throws Exception {

        Inventory inv = new Inventory();
        addTestData(inv);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
      Controller.MainController controller = new MainController(inv);
        loader.setController(controller);

        Parent root = loader.load();
        Scene mainScreenScene = new Scene(root);
        stage.setScene(mainScreenScene);


        stage.show();

    }

    /** This method adds the test data into the inventory
     * @param inv is the inventory*/

    public static void addTestData(Inventory inv) {


        Part testPartOne = new InHouse(1, "graphics card", 399.00, 6, 1, 10, 901);
        Part testPartTwo = new InHouse(2, "memory chip", 100.00, 4, 1, 20, 302);
        Part testPartThree = new InHouse(3, "fan", 99.00, 8, 1, 40, 703);
        inv.addPart(testPartOne);
        inv.addPart(testPartTwo);
        inv.addPart(testPartThree);


        Part testPartFour = new Outsourced(4, "talon MotherBoard", 500.00, 3, 1, 20, "Talon");
        Part testPartFive = new Outsourced(5, "falco Memory chip", 200.00, 4, 1, 15, "Falco");
        Part testPartSix = new Outsourced(6, "falco Fan", 100.00, 2, 1, 10, "Falco");
        inv.addPart(testPartFour);
        inv.addPart(testPartFive);
        inv.addPart(testPartSix);


        Product productOne = new Product(1, "bare Bones Pc", 500.00, 3, 1, 20);
        Product productTwo = new Product(2, "hackintosh", 700.00, 4, 1, 15);
        Product productThree = new Product(3, "rasberry Pi", 1000.00, 5, 1, 10);



        productOne.addAssociatedPart(testPartOne);
        productOne.addAssociatedPart(testPartTwo);
        productOne.addAssociatedPart(testPartThree);

        productTwo.addAssociatedPart(testPartOne);
        productTwo.addAssociatedPart(testPartFive);
        productTwo.addAssociatedPart(testPartSix);

        productThree.addAssociatedPart(testPartFour);




        inv.addProduct(productOne);
        inv.addProduct(productTwo);
        inv.addProduct(productThree);

    }
}

