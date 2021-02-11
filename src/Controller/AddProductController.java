package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** This is the controller class for the AddProduct.fxml. */
public class AddProductController implements Initializable {


    /** This is the initialization of the fxml buttons and text fields.  */
    @FXML private TextField productIdTextField;
    @FXML private TextField productNameTextField;
    @FXML private TextField productInvTextField;
    @FXML private TextField productPriceTextField;
    @FXML private TextField productMaxTextField;
    @FXML private TextField productMinTextField;
    @FXML private TextField productSearchTextField;
    @FXML private TableView<Part> addProductTopTableView;
    @FXML private TableColumn<Part,Integer > productIdTopColumn;
    @FXML private TableColumn<Part, String> productNameTopColumn;
    @FXML private TableColumn<Part, Integer> productInventoryTopColumn;
    @FXML private TableColumn<Part, Double> productPriceTopColumn;
    @FXML private Button productAddButton;
    @FXML private Button productRemoveButton;
    @FXML private Button productSaveButton;
    @FXML private Button productCancelButton;
    @FXML private TableView<Part> addProductBottomTableView;
    @FXML private TableColumn<Part, Integer> productIdBottomColumn;
    @FXML private TableColumn<Part, String> productNameBottomColumn;
    @FXML private TableColumn<Part, Integer> productInventoryBottomColumn;
    @FXML private TableColumn<Part, Double> productPriceBottomColumn;

    Inventory inv;
    Part partSelected;
    int errNmb;
    boolean isError = false;
    Product newProduct;

    private ObservableList<Part> partInventorySearchList = FXCollections.observableArrayList();
    private ObservableList<Part> partInventoryList = FXCollections.observableArrayList();
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    private ObservableList<Part> partsSelectedList = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /** This method  initializes the addProduct Controller.
     * @param inv to pass in the data from the Inventory model. */
    public AddProductController(Inventory inv){
        this.inv = inv;
    }


    /** This method initializes the controller class. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partInventoryList.setAll(Inventory.getAllParts());
        allProducts.setAll(Inventory.getAllProducts());
        nextId();
        setAllPartsTable();
    }

        /** this method gets all parts and populates the top table of the inventory list. */
    public void setAllPartsTable(){

            //add all the parts to the table view
        allParts.setAll(Inventory.getAllParts());
        addProductTopTableView.setItems(allParts);

        productIdTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        productInventoryTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        productNameTopColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productPriceTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }

    /** This method displays all of the associated parts of the product on the bottom table. */
    @FXML void addAssociatedPart() {

        if (partSelected == null) {
            this.errNmb = 1;
            isError = true;
            return;
        }

        if(this.errNmb == 1){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error adding associated part!");
            error.setContentText("A part must be selected or a part must be added!");
        }



        this.errNmb = 0;

        partSelected = addProductTopTableView.getSelectionModel().getSelectedItem();
        if(partSelected == null){
            Alert nullPart = new Alert(Alert.AlertType.ERROR);
            nullPart.setTitle("ERROR!");
            nullPart.setHeaderText("No part selected!");
            nullPart.setContentText("A part must be selected to add it!");
            nullPart.showAndWait();
            return;
        }

        if(errNmb == 0){


            partsSelectedList.add(partSelected);
            associatedPartsList.setAll(partsSelectedList);
            addProductBottomTableView.setItems(associatedPartsList);

            allParts.remove(partSelected);
            addProductTopTableView.refresh();

            productIdBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
            productInventoryBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
            productNameBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
            productPriceBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        }
    }
    /** This is the method that exits from the current scene to the mainScreen.fxml. */
        @FXML void cancel(ActionEvent event) throws IOException{
              try{

            Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
            cancelAlert.setTitle("CANCEL");
            cancelAlert.setHeaderText("Are you sure you want to cancel?");
            cancelAlert.setContentText("Click 'OK' to confirm.");
            Optional<ButtonType> decision = cancelAlert.showAndWait();


            if(decision.get() == ButtonType.OK){

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                MainController controller = new MainController(inv);
                loader.setController(controller);


                Parent mainScreenParent = loader.load();
                Scene mainScreenScene = new Scene(mainScreenParent);


                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(mainScreenScene);
                window.show();
            }else{
                return;
            }
        }catch(IOException e){

        }

    }

    /** This method removes the selected associated part from the table. */
    @FXML void removeAssociatedPart() {

        if (partSelected == null) {
            this.errNmb = 1;
            isError = true;
            return;
        }

        if (associatedPartsList.isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error removing associated part");
            error.setContentText("an associated part cannot be removed if the it is not selected!");
            error.showAndWait();
        }

        if(!associatedPartsList.isEmpty()) {
            Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            removeAlert.setTitle("REMOVE");
            removeAlert.setHeaderText("Are you sure you want to remove the associated part?");
            removeAlert.setContentText("Click 'OK' to confirm.");
            Optional<ButtonType> decision = removeAlert.showAndWait();


            if (decision.get() == ButtonType.OK) {

                this.errNmb = 0;
                partSelected = addProductBottomTableView.getSelectionModel().getSelectedItem();
                errCheck();

                partsSelectedList.remove(partSelected);
                allParts.add(partSelected);

                addProductTopTableView.setItems(allParts);
                associatedPartsList.setAll(partsSelectedList);
                addProductBottomTableView.setItems(associatedPartsList);


                addProductTopTableView.refresh();
                addProductBottomTableView.refresh();

                productIdBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
                productInventoryBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
                productNameBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
                productPriceBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
            }
        }
    }
            /** This method saves the created product checking for errors. */
    @FXML void save(ActionEvent event)  {

        try {


            this.errNmb = 0;
            errCheck();
            showAlert(errNmb);
                if (errNmb == 0) {
                    newProduct = new Product(
                            Integer.valueOf(productIdTextField.getText()),
                            productNameTextField.getText(),
                            Double.valueOf(productPriceTextField.getText()),
                            Integer.valueOf(productInvTextField.getText()),
                            Integer.valueOf(productMinTextField.getText()),
                            Integer.valueOf(productMaxTextField.getText())

                    );

                    for (Part part : associatedPartsList) {
                        newProduct.addAssociatedPart(part);
                    }

                    inv.addProduct(newProduct);


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                    MainController controller = new MainController(inv);
                    loader.setController(controller);


                    Parent mainScreenParent = loader.load();
                    Scene mainScreenScene = new Scene(mainScreenParent);


                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(mainScreenScene);
                    window.show();
                }
            }catch(IOException e){

            }

    }
    /** This method checks for user errors when adding a new part or product using an error number. */
    public void errCheck() {



        if (productNameTextField.getText().isEmpty() ||
                productInvTextField.getText().isEmpty() ||
                productPriceTextField.getText().isEmpty() ||
                productMaxTextField.getText().isEmpty() ||
                productMinTextField.getText().isEmpty()) {
            this.errNmb = 2;
            isError = true;
            return;
        }



        if (
                productInvTextField.getText().matches("[a-zA-Z]+") ||
                        productPriceTextField.getText().matches("[a-zA-Z]+") ||
                        productMaxTextField.getText().matches("[a-zA-Z]+") ||
                        productMinTextField.getText().matches("[a-zA-Z]+")) {

            isError = true;
            this.errNmb = 5;
            return;
        }

        if (productNameTextField.getText().matches("^[0-9]*$")) {
            isError = true;
            this.errNmb = 6;
            return;
        }

        if (Integer.valueOf(productInvTextField.getText()) < 0 ||
                Double.valueOf(productPriceTextField.getText()) < 0 ||
                Integer.valueOf(productMaxTextField.getText()) < 0 ||
                Integer.valueOf(productMinTextField.getText()) < 0) {
            this.errNmb= 4;
            isError = true;
            return;
        }

        double sumOfPartsPrices = 0;
        for (Part part : associatedPartsList) {
            sumOfPartsPrices += part.getPrice();
        }
        if (Double.valueOf(productPriceTextField.getText()) < sumOfPartsPrices) {
            this.errNmb = 7;
            isError = true;
            return;
        }
    }


    /** This method searches for a part via id or by the string name. */

    @FXML void searchPart(ActionEvent event) {
        if (productSearchTextField.getText().isEmpty()) {
            return;
        }


        if (productSearchTextField.getText().matches("[a-zA-Z]+")) {
            partInventorySearchList.clear();
            partInventorySearchList = inv.lookupPart(productSearchTextField.getText().trim());
            addProductTopTableView.setItems(partInventorySearchList);
            addProductTopTableView.refresh();
        }


        if (productSearchTextField.getText().matches("^[0-9]*$")) {
            int id = Integer.valueOf(productSearchTextField.getText());
            Part returnedPart;
            partInventorySearchList.clear();
            returnedPart = inv.lookupPart(id);
            partInventorySearchList.add(returnedPart);
            addProductTopTableView.setItems(partInventorySearchList);
            addProductTopTableView.refresh();
        }

    }
   /**This method resets the part search bar with a mouse click
    * @param event triggers the method */
    @FXML
    public void resetPart(MouseEvent event) {

        productSearchTextField.setText("");
            addProductTopTableView.setItems(partInventoryList);



    }



    /** This checks for errors and shows an  error message  according to a specific error code.
     * @param errCde  is the error code that triggers the error message alert. */
    public void showAlert(int errCde){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR!");
        error.setHeaderText("Error adding product!");


        if(this.errNmb == 0){
            return;
        }

        if(this.errNmb == 2){
            error.setContentText("All fields must have content!");
        }

        if(this.errNmb == 4){
            error.setContentText("Values of inv, price, max, and min must not be less than zero!");
        }
        if(this.errNmb == 5){
            error.setContentText("Values of inv, price, max, and min must not contain letters");
        }
        if(this.errNmb == 6){
            error.setContentText("Name of product can't contain numbers!");
        }
        if(this.errNmb == 7){
            error.setContentText("Price of product cannot be less than sum of associated parts!");
        }
        error.showAndWait();
        return;
    }

    /** This gets all the parts in the inventory. The size of an observable list.
     * @return returns all parts from inventory */

    public int getSizeOfAllParts(){
        return allProducts.size();
    }


    /** This gets all the products in the inventory. The size of an observable list.
     * @return returns all products from inventory */
    public int getSizeOfAllProducts(){
        return allProducts.size();
    }

        /** This method creates a unique id for all products */
    public void nextId(){
        int size = getSizeOfAllProducts();

        if(size == 0){
            productIdTextField.setText("1");
        }else{
            for(int i = 0; i <=size; i++){
                if(i == 0){
                    continue;
                }
                if(Inventory.lookupProduct(i) == null){
                    productIdTextField.setText(String.valueOf(i));
                    break;
                }else if(Inventory.lookupProduct(i) != null){
                    if(i == size){

                        productIdTextField.setText(String.valueOf(Inventory.getLastProductId() + 1));
                    }
                    continue;
                }
            }
        }
    }
}
