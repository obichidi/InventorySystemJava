package Controller;

import Model.Part;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;



import Model.Inventory;
import Model.Part;
import Model.Product;
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


/** This is the controller class for the ModifyProduct.fxml. */
public class ModifyProductController  implements Initializable {

    @FXML private TextField productIdTextField;
    @FXML private TextField productNameTextField;
    @FXML private TextField productInvTextField;
    @FXML private TextField productPriceTextField;
    @FXML private TextField productMaxTextField;
    @FXML private TextField productMinTextField;
    @FXML private TextField productSearchTextField;
    @FXML private TableView<Part> modifyProductTopTableView;
    @FXML private TableColumn<Part, Integer> productIdTopColumn;
    @FXML private TableColumn<Part, String> productNameTopColumn;
    @FXML private TableColumn<Part, Integer> productInventoryTopColumn;
    @FXML private TableColumn<Part, Double> productPriceTopColumn;
    @FXML private Button productAddButton;
    @FXML private TableView<Part> modifyProductBottomTableView;
    @FXML private TableColumn<Part, Integer> productIdBottomColumn;
    @FXML private TableColumn<Part, String> productNameBottomColumn;
    @FXML private TableColumn<Part, Integer> productInventoryBottomColumn;
    @FXML private TableColumn<Part, Double> productPriceBottomColumn;
    @FXML private Button productRemoveButton;
    @FXML private Button productSaveButton;
    @FXML private Button productCancelButton;

    Inventory inv;
    Product productSelected;
    Part partSelected;
    int errNmbr;
    boolean isErr = false;

    ObservableList<Part> allParts = FXCollections.observableArrayList();
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    ObservableList<Part> partInventorySearchList = FXCollections.observableArrayList();

    /**
     * This method initializes the ModifyProductController constructor
     * @param inv Inventory hold the lists of parts and products
     * @param selectedProduct is the Product selected by user
     */
    public ModifyProductController(Inventory inv, Product selectedProduct) {
        this.inv = inv;
        this.productSelected = selectedProduct;
    }

    /** This method initializes the controller class */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        associatedParts.setAll(productSelected.getAllAssociatedParts());
        productSearchTextField.setText("");

        setUpProductInfo();
        setAllPartsTable();
        setAssociatedPartsTable();
    }

    /**
     * This method sets up the bottom table that shows all of the parts currently
     * associated with the product.
     * @param event triggers the method
     */
    @FXML public void addAssociatedPart(ActionEvent event) {
        this.errNmbr = 0;
        partSelected = modifyProductTopTableView.getSelectionModel().getSelectedItem();
        if (partSelected == null) {
            Alert nullPart = new Alert(Alert.AlertType.ERROR);
            nullPart.setTitle("ERROR!");
            nullPart.setHeaderText("No part selected!");
            nullPart.setContentText("A part must be selected to add it!");
            nullPart.showAndWait();
            return;
        }

        if (errNmbr == 0) {


            associatedParts.add(partSelected);
            modifyProductBottomTableView.setItems(associatedParts);


            productIdBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
            productInventoryBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
            productNameBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
            productPriceBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        }

    }


    /**
     * This method changes the screen to the main screen if the cancel button is pushed.
     * It also prompts to make sure the user wants to cancel.  it is triggered by
     * @param event triggers the method
     */
    @FXML public void cancelButtonPushed(ActionEvent event) {
        try {


            Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
            cancelAlert.setTitle("CANCEL");
            cancelAlert.setHeaderText("Are you sure you want to cancel?");
            cancelAlert.setContentText("Click 'OK' to confirm.");
            Optional<ButtonType> decision = cancelAlert.showAndWait();


            if (decision.get() == ButtonType.OK) {


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                MainController controller = new MainController(inv);
                loader.setController(controller);


                Parent mainScreenParent = loader.load();
                Scene mainScreenScene = new Scene(mainScreenParent);


                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScreenScene);
                window.show();
            } else {
                return;
            }
        } catch (IOException e) {

        }

    }

    /**
     * This method removes  the associated parts that were  added to the product
     * RUNTIME ERROR:
     * there was an error where the user would have to fill in the product information before being able to
     * remove the associated part, so i had to remove that error alert from this particular method.
     */
    @FXML void removeAssociatedPart() {

        if (associatedParts.isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error removing associated part!");
            error.setContentText("A Part Must be Selected in order to be deleted!");
            error.showAndWait();
        }


        if (!associatedParts.isEmpty()) {
            Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            removeAlert.setTitle("REMOVE");
            removeAlert.setHeaderText("Are you sure you want to remove the associated part?");
            removeAlert.setContentText("Click 'OK' to confirm.");
            Optional<ButtonType> decision = removeAlert.showAndWait();


            if (decision.get() == ButtonType.OK) {


                this.errNmbr = 0;
                partSelected = modifyProductBottomTableView.getSelectionModel().getSelectedItem();
                // errChck();
                showAlert(errNmbr);
            }
            if (errNmbr == 0) {
                // Add our selected part back to our allparts list
                // Remove our selected part from our associated parts list
                associatedParts.remove(partSelected); // Removing from selected parts

                modifyProductBottomTableView.setItems(associatedParts); //Refresh bottom table

                // Remove selected part from the parts available and refresh
                modifyProductTopTableView.refresh();
                modifyProductBottomTableView.refresh();
                // Set up columns
                productIdBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
                productInventoryBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
                productNameBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
                productPriceBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
            }
        }
    }




    /**
     * This method updates the modified product with the new information and associated
     * parts
     */
    public void updateProduct() {
        inv.updateProduct(
                productSelected.getId() - 1,

                new Product(
                        Integer.valueOf(productIdTextField.getText()),
                        productNameTextField.getText(),
                        Double.valueOf(productPriceTextField.getText()),
                        Integer.valueOf(productInvTextField.getText()),
                        Integer.valueOf(productMinTextField.getText()),
                        Integer.valueOf(productMaxTextField.getText()))
        );
    }


    /** This method sets up the product info for the  product view */
    public void setUpProductInfo() {

        this.productIdTextField.setText(String.valueOf(productSelected.getId()));
        this.productNameTextField.setText(productSelected.getName());
        this.productInvTextField.setText(String.valueOf(productSelected.getStock()));
        this.productPriceTextField.setText(String.valueOf(productSelected.getPrice()));
        this.productMaxTextField.setText(String.valueOf(productSelected.getMax()));
        this.productMinTextField.setText(String.valueOf(productSelected.getMin()));
    }


    /**
     * This method is triggered by
     * @param event
     * when the part search bar is clicked it is cleared
     * Event that is captured to detect if mouse is clicked
     */
    @FXML void resetPartTableAfterSearch(MouseEvent event) {

        productSearchTextField.setText("");
        modifyProductTopTableView.setItems(allParts);
        modifyProductTopTableView.refresh();

    }

    /** This method is triggered by
     * @param event it saves the modified information and sends it to the MainScreen it generates alerts via error code determination. */
    @FXML void saveButtonPushed(ActionEvent event) throws IOException {

        try {
            this.errNmbr = 0;
            errChck();
            showAlert(errNmbr);

            if (errNmbr == 0) {
                for (Part part : associatedParts) {
                    productSelected.addAssociatedPart(part);
                }
                updateProduct();


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainScreen.fxml"));
                MainController controller = new MainController(inv);
                loader.setController(controller);


                Parent mainScreenParent = loader.load();
                Scene mainScreenScene = new Scene(mainScreenParent);


                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(mainScreenScene);
                window.show();
            }
        } catch (IOException e) {

        }
    }


    /**
     * This method sets up the top table that shows all of the parts currently
     * in the inventory
     */
    public void setAllPartsTable() {

        allParts.setAll(Inventory.getAllParts());

        modifyProductTopTableView.setItems(allParts);


        productIdTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        productInventoryTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        productNameTopColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productPriceTopColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }


    /**
     * This method sets up the bottom table that shows all of the associated parts
     * in the inventory
     */
    public void setAssociatedPartsTable() {

        modifyProductBottomTableView.setItems(associatedParts);


        productIdBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        productInventoryBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        productNameBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productPriceBottomColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }


    /**   RUNTIME ERROR:
     * The runtime error I was having with this search part function was that It was returning a null
     * for the first if statement. If the id or letter was not found. The program would crash.
     * so I removed the return null statement and that fixed it.
     *
     * This method searches for a part via the id or name
     * @event triggers the method */

    @FXML void searchPart(ActionEvent event) {
        if (productSearchTextField.getText().isEmpty()) {
            return;
        }


        if (productSearchTextField.getText().matches("[a-zA-Z]+")) {
            partInventorySearchList.clear();
            partInventorySearchList = inv.lookupPart(productSearchTextField.getText().trim());
            modifyProductTopTableView.setItems(partInventorySearchList);
            modifyProductTopTableView.refresh();
        }


        if (productSearchTextField.getText().matches("^[0-9]*$")) {
            int id = Integer.valueOf(productSearchTextField.getText());
            Part returnedPart;
            partInventorySearchList.clear();
            returnedPart = inv.lookupPart(id);
            partInventorySearchList.add(returnedPart);
            modifyProductTopTableView.setItems(partInventorySearchList);
            modifyProductTopTableView.refresh();
        }

    }
    /** This method checks for user errors when adding a new part or product using an error number. */
    public void errChck() {

        if (inv == null) {
            this.errNmbr = 1;
            isErr = true;
            return;
        }
        if (productNameTextField.getText().isEmpty() ||
                productInvTextField.getText().isEmpty() ||
                productPriceTextField.getText().isEmpty() ||
                productMaxTextField.getText().isEmpty() ||
                productMinTextField.getText().isEmpty()) {
            this.errNmbr = 2;
            isErr = true;
            return;
        }

        if (
                productInvTextField.getText().matches("[a-zA-Z]+") ||
                        productPriceTextField.getText().matches("[a-zA-Z]+") ||
                        productMaxTextField.getText().matches("[a-zA-Z]+") ||
                        productMinTextField.getText().matches("[a-zA-Z]+")) {

            isErr = true;
            this.errNmbr = 5;
            return;
        }
        if (productNameTextField.getText().matches("^[0-9]*$")) {
            isErr = true;
            this.errNmbr = 6;
            return;
        }
        if (Integer.valueOf(productInvTextField.getText()) < 0 ||
                Double.valueOf(productPriceTextField.getText()) < 0 ||
                Integer.valueOf(productMaxTextField.getText()) < 0 ||
                Integer.valueOf(productMinTextField.getText()) < 0) {
            this.errNmbr = 4;
            isErr = true;
            return;
        }
        if (Integer.valueOf(productInvTextField.getText()) > Integer.valueOf(productMaxTextField.getText())) {
            this.errNmbr = 7;
            isErr = true;
            return;
        }
        if (Integer.valueOf(productInvTextField.getText()) < Integer.valueOf(productMinTextField.getText())) {
            this.errNmbr = 8;
            isErr = true;
            return;
        }
        if (Integer.valueOf(productMinTextField.getText()) > Integer.valueOf(productMaxTextField.getText())) {
            this.errNmbr = 9;
            isErr = true;
            return;
        }

    }
    /** This checks for errors and show error message alerts according to
     * @param errCde  . */

    public void showAlert(int errCde){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR!");
        error.setHeaderText("Error adding product!");


        if(this.errNmbr == 0){
            return;
        }
        if(this.errNmbr == 1){
            error.setHeaderText("Error adding associated part!");
            error.setContentText("A part must be selected or a part must be added!");
        }
        if(this.errNmbr == 2){
            error.setContentText("All fields must have content!");
        }

        if(this.errNmbr == 4){
            error.setContentText("Values of inv, price, max, and min must not be less than zero!");
        }
        if(this.errNmbr == 5){
            error.setContentText("Values of inv, price, max, and min must not contain letters");
        }
        if(this.errNmbr == 6){
            error.setContentText("Name of product can't contain numbers!");
        }
        if(this.errNmbr == 7){
            error.setContentText("Inventory cannot be greater than max!");
        }
        if(this.errNmbr == 8){
            error.setContentText("Inventory cannot be less than min!");
        }
        if(this.errNmbr == 9){
            error.setContentText("Min cannot be greater than max!");
        }

        error.showAndWait();
        return;

    }

}
