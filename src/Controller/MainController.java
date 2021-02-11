package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller class  that controls the MainScreen.fxml. */
public class MainController implements Initializable {


    @FXML private TableView<Part> partTableView;
    @FXML private TableColumn<Part, Integer> partIdColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInventoryLevelColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn;
    @FXML private TextField partSearchBar;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    @FXML private TableView<Product> productTableView;
    @FXML private TableColumn<Product, Integer> productIdColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInventoryLevelColumn;
    @FXML private TableColumn<Product, Double> productPriceColumn;
    @FXML private TextField productSearchBar;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;
    @FXML private Button exitButton;

    Inventory inv;
    int errNmb;
    boolean isErr;
    Part partSelected;


    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearch = FXCollections.observableArrayList();
    private ObservableList<Product> productSearch = FXCollections.observableArrayList();


    /**
     * This method constructs the constructor taking in an instance of the inventory
     *
     * @param inv is the inventory
     */
    public MainController(Inventory inv) {
        this.inv = inv;
    }


    /**
     * This method checks for errors and shows an alert error message based on an error code.
     * @param errNmb is the error number of the user generated error.
     */
    public void showAlert(int errNmb) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Error ");

        if (errNmb == 0) {
            return;
        }
        if (errNmb == 1) {
            alert.setContentText(" The List is empty!");
        }
        if (errNmb == 2) {
            alert.setContentText("Nothing is selected!");
        }
        alert.showAndWait();

    }


    /**
     * This method is for when the add part button is pressed
     *
     * @param event is triggered and the AddPart.fxml screen is opened.
     *
     */
    @FXML
    public void addPart(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddPart.fxml"));
        AddPartController controller = new AddPartController(inv);
        loader.setController(controller);

        Parent addPartParent = loader.load();
        Scene addPartScene = new Scene(addPartParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addPartScene);
        window.show();


    }

    /**
     * This method is triggered when the user presses the add product button it opens the AddProduct.fxml scene.
     * @param event triggers the method.
     */
    @FXML
    public void addProduct(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddProduct.fxml"));
        AddProductController controller = new AddProductController(inv);
        loader.setController(controller);

        Parent addProductParent = loader.load();
        Scene addProductScene = new Scene(addProductParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addProductScene);
        window.show();


    }

    /**
     * This method is triggered when the delete part button is pushed.
     * It checks for errors via error number.
     * @param event triggers the method
     */
    @FXML
    public void deletePart(ActionEvent event) {
        this.errNmb = 0;
        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (partInventory.isEmpty()) {
            isErr = true;
            this.errNmb = 1;
            showAlert(errNmb);
        }
        if (selectedPart == null && !partInventory.isEmpty()) {
            isErr = true;
            this.errNmb = 2;
            showAlert(errNmb);
        }

        if (this.errNmb == 0) {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("CONFIRM DELETE");
            confirmDelete.setHeaderText("Are you sure you would like to delete this part?");
            Optional<ButtonType> confirm = confirmDelete.showAndWait();
            if (confirm.get() == ButtonType.OK) {
                partInventory.remove(selectedPart);
                inv.deletePart(selectedPart);
            } else {
                return;
            }
        }
    }

    /**
     * This method is triggered when the delete product button is pushed.
     * @param event triggers the method
     */
    @FXML
    public void deleteProduct(ActionEvent event) {
        this.errNmb = 0;
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        deleteAllAssociatedParts();

        if (productInventory.isEmpty()) {
            isErr = true;
            this.errNmb = 1;
            showAlert(errNmb);
        }
        if (selectedProduct == null && !productInventory.isEmpty()) {
            isErr = true;
            this.errNmb = 2;
            showAlert(errNmb);
        }
        if (selectedProduct.getAllAssociatedParts() != null) {
            Alert hasAssociatedPart = new Alert(Alert.AlertType.ERROR);
            hasAssociatedPart.setTitle("ERROR!");
            hasAssociatedPart.setHeaderText("Product has associated part!");
            hasAssociatedPart.setContentText("Product still has associated part!");
            hasAssociatedPart.showAndWait();
            return;
        }
        if (this.errNmb == 0) {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setTitle("CONFIRM DELETE");
            confirmDelete.setHeaderText("Are you sure you would like to delete this product?");
            Optional<ButtonType> confirm = confirmDelete.showAndWait();
            if (confirm.get() == ButtonType.OK &&  selectedProduct.getAllAssociatedParts().size() < 1) {
                productInventory.remove(selectedProduct);
                inv.deleteProduct(selectedProduct);
            } else {
                return;
            }
        }
    }


    @FXML
    public void deleteAllAssociatedParts(){
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
      Product partsToDelete = selectedProduct;
     System.out.println(partsToDelete);
    }


    /**
     * This method exits the entire program.
     *
     * @param event triggers the event
     */
    @FXML
    public void exit(ActionEvent event) {

        Platform.exit();
    }


    /**
     * This method opens the ModifyPart.fxml scene  and allows the user to modify and save
     * changes to a part.
     * @param event triggers the method
     */
    @FXML
    public void modifyPart(ActionEvent event) throws IOException {

        try {


            Part partSelected = partTableView.getSelectionModel().getSelectedItem();


            if (partSelected == null) {
                Alert noPartSelected = new Alert(Alert.AlertType.ERROR);
                noPartSelected.setTitle("ERROR!");
                noPartSelected.setHeaderText("NO PART SELECTED!");
                noPartSelected.setContentText("A part needs to be selected !");
                noPartSelected.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyPart.fxml"));
            ModifyPartController controller = new ModifyPartController(inv, partSelected);
            loader.setController(controller);


            Parent modifyPartParent = loader.load();
            Scene modifyPartScene = new Scene(modifyPartParent);


            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(modifyPartScene);
            window.show();
        } catch (IOException e) {

        }
    }


    /**
     * This method opens the ModifyProduct.fxml scene and allows the user to modify and save
     * a product.
     * @param event triggers the method
     */
    @FXML
    public void modifyProduct(ActionEvent event) {
        try {

            Product productSelected = productTableView.getSelectionModel().getSelectedItem();


            if (productSelected == null) {
                Alert noPartSelected = new Alert(Alert.AlertType.ERROR);
                noPartSelected.setTitle("ERROR!");
                noPartSelected.setHeaderText("NO PRODUCT SELECTED!");
                noPartSelected.setContentText("A product needs to be selected!");
                noPartSelected.showAndWait();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ModifyProduct.fxml"));
            ModifyProductController controller = new ModifyProductController(inv, productSelected);
            loader.setController(controller);


            Parent modifyProductParent = loader.load();
            Scene modifyProductScene = new Scene(modifyProductParent);


            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(modifyProductScene);
            window.show();
        } catch (IOException e) {

        }
    }


    /**
     * This method resets the part table view.
     *
     * @param event  triggers the method.
     */
    @FXML
    public void resetPart(MouseEvent event) {

        partSearchBar.setText("");
        partTableView.setItems(partInventory);
        partTableView.refresh();


    }

    /**
     * This method resets the product table after a search.
     *
     * @param event is triggered by a mouseclick.
     */

    @FXML
    public void resetProduct(MouseEvent event) {

        productSearchBar.setText("");
        productTableView.setItems(productInventory);
        productTableView.refresh();
    }



    /** This method  searches for a part by name of id number
     *@param event  triggers this method
     * RUNTIME ERROR:
     * The runtime error I was having with this search part function was that It was returning a null
     * from the first if statement. If the id or letter was not found. The program would crash.
     *  I removed the return null statement and that fixed it.
     *
     *
     */

    @FXML
   public void searchPart(ActionEvent event) {

        if (partSearchBar.getText().isEmpty()) {
            return;
        }


        if (partSearchBar.getText().matches("[a-zA-Z]+")) {

            partSearch.clear();
            partSearch = inv.lookupPart(partSearchBar.getText().trim());

            partTableView.setItems(partSearch);
            partTableView.refresh();

        }


        if (partSearchBar.getText().matches("^[0-9]*$")) {
            int id = Integer.valueOf(partSearchBar.getText());
            Part returnedPart;
            partSearch.clear();
            returnedPart = inv.lookupPart(id);
            partSearch.add(returnedPart);
            partTableView.setItems(partSearch);
            partTableView.refresh();

        }

    }




    /** This method searches for a product by id or letters in the product name.
     * @param event  triggers the method */
    @FXML public void searchProduct(ActionEvent event) {
        if(productSearchBar.getText().isEmpty()){
            return;
        }


        if(productSearchBar.getText().matches("[a-zA-Z]+")){
            productSearch.clear();
            productSearch = inv.lookupProduct(productSearchBar.getText().trim());
            productTableView.setItems(productSearch);
            productTableView.refresh();
        }

        if(productSearchBar.getText().matches("^[0-9]*$")){
            int id = Integer.valueOf(productSearchBar.getText());
            Product returnedProduct;
            productSearch.clear();
            returnedProduct = inv.lookupProduct(id);
            productSearch.add(returnedProduct);
            productTableView.setItems(productSearch);
            productTableView.refresh();
        }
    }


    /** This method initializes the controller class. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        partInventory.setAll(Inventory.getAllParts());
        partTableView.setItems(partInventory);
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productInventory.setAll(Inventory.getAllProducts());
        productTableView.setItems(productInventory);
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productInventoryLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }



}



