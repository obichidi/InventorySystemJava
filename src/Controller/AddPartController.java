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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.InHouse;
import Model.Inventory;
import static Model.Inventory.lookupPart;
import Model.Outsourced;
import Model.Part;


/** This is the controller class for the AddPart.fxml. */
public class AddPartController implements  Initializable {

    /**
     * This is the initialization of the fxml buttons and text fields.
     */
    @FXML private RadioButton inHouseRadioButton;
    @FXML private RadioButton outsourcedRadioButton;
    @FXML private Label partSwappableLabel;
    @FXML private TextField partIdTextField;
    @FXML private TextField partNameTextField;
    @FXML private TextField partInvTextField;
    @FXML private TextField partPriceTextField;
    @FXML private TextField partMaxTextField;
    @FXML private TextField partSwappableTextField;
    @FXML private TextField partMinTextField;
    @FXML private Button partSaveButton;
    @FXML private Button partCancelButton;

    int errCde;

    boolean isErr;
    Inventory inv;


    /** This gets all parts from inventory and puts them in a list. */
    ObservableList<Part> allParts = FXCollections.observableArrayList();




    /**
     * This initializes the addPart Controller.
     *
     * @param inv is to to pass in the data from the Inventory model.
     */
    public AddPartController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * This is for when the inHouse radio button is selected,
     * the outsourced radio button will be deselected and a label will be changed to Machine Id.
     *
     * @param event is for when the radio button is selected.
     */
    @FXML
    public void inHouseRadioButtonSelected(ActionEvent event) {
        outsourcedRadioButton.setSelected(false);
        partSwappableLabel.setText("Machine ID");
    }

    /**
     * This is for when the Outsourced radio button is selected,
     * the inHouse radio button will be deselected and a label will be changed to Company Name.
     *
     * @param event is for when the radio button is selected.
     */
    @FXML
    public void outsourcedRadioButtonSelected(ActionEvent event) {
        inHouseRadioButton.setSelected(false);
        partSwappableLabel.setText("Company Name");
    }

    /**
     * This is where the controller class is initialized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /** This Initializes the observable lists and calls all parts from the list. */
        allParts.setAll(Inventory.getAllParts());

        inHouseRadioButton.setSelected(true);

        /** This  is to Generates a unique Id number. */
        nextId();
    }

    /**
     * This is the method that exits from the current scene to the mainScreen.fxml.
     *
     * @param event is for when the cancel button is pressed.
     */
    @FXML
    public void cancel(ActionEvent event) {
        try {
            //alert for confirmation
            Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
            cancelAlert.setTitle("CANCEL");
            cancelAlert.setHeaderText("Are you sure you want to cancel?");
            cancelAlert.setContentText("Click 'OK' to confirm.");
            Optional<ButtonType> decision = cancelAlert.showAndWait();

            //if the users presses ok?
            if (decision.get() == ButtonType.OK) {

                //gets and loads and changes the screen to the MainScreen.fxml.
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
     * This method is for saving the  inHouse or outsourced part added to the inventory.
     * @param event triggers the event.
     */
    @FXML
    public void save(ActionEvent event) {
        try {

            this.errCde = 0;
            errChk();
            showAlert(errCde);

            if (this.errCde == 0) {
                if (outsourcedRadioButton.isSelected()) {
                    addOutsourcedPart();
                }
                if (inHouseRadioButton.isSelected()) {
                    addInHousePart();
                }


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
     * This gets all the parts in the inventory. The size of an observable list.
     * @return returns allParts length.
     */
    public int partsSize() {
        return allParts.size();
    }

    /**
     * This generates a unique id number for each  part
     */
    public void nextId() {
        int size = partsSize();

        if (size == 0) {
            partIdTextField.setText("1");
        } else {
            for (int i = 0; i <= size; i++) {
                if (i == 0) {
                    continue;
                }
                if (lookupPart(i) == null) {
                    partIdTextField.setText(String.valueOf(i));
                    break;
                } else if (lookupPart(i) != null) {
                    if (i == size) {

                        partIdTextField.setText(String.valueOf(Inventory.getLastPartId() + 1));
                    }
                    continue;
                }
            }
        }
    }


    /**
     * This checks for errors by error code.
     */
    public void errChk() {

        if (inHouseRadioButton.isSelected() && partSwappableTextField.getText().matches("[a-zA-Z]+")) {
            isErr = true;
            this.errCde = 1;
            return;
        }


        if (outsourcedRadioButton.isSelected() && partSwappableTextField.getText().matches("^[0-9]*$")) {
            isErr = true;
            this.errCde = 2;
            return;
        }
        if (partNameTextField.getText().isEmpty() ||
                partInvTextField.getText().isEmpty() ||
                partPriceTextField.getText().isEmpty() ||
                partMaxTextField.getText().isEmpty() ||
                partMinTextField.getText().isEmpty() ||
                partSwappableTextField.getText().isEmpty()
        ) {
            isErr = true;
            this.errCde = 3;
            return;
        }
        if (Integer.valueOf(partInvTextField.getText()) < 0 ||
                Integer.valueOf(partMaxTextField.getText()) < 0 ||
                Integer.valueOf(partMinTextField.getText()) < 0 ||
                Double.valueOf(partPriceTextField.getText()) < 0) {
            isErr = true;
            this.errCde = 4;
            return;
        } else if (Integer.valueOf(partInvTextField.getText()) >
                Integer.valueOf(partMaxTextField.getText())) {
            isErr = true;
            this.errCde = 5;
            return;
        } else if (Integer.valueOf(partInvTextField.getText()) <
                Integer.valueOf(partMinTextField.getText())) {
            isErr = true;
            this.errCde = 6;
            return;
        } else if (Integer.valueOf(partMinTextField.getText()) >
                Integer.valueOf(partMaxTextField.getText())) {
            isErr = true;
            this.errCde = 7;
            return;
        }
        if (!outsourcedRadioButton.isSelected() && !inHouseRadioButton.isSelected()) {
            isErr = true;
            this.errCde = 8;
            return;
        }

    }


    /**
     * This method determines the user error message based on an error number.
     * @param errNmb  is the number of the user generated error.
     */
    public void showAlert(int errNmb) {
        //Initialize an alert instance
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR");
        error.setHeaderText("CANNOT SAVE");

        if(errNmb == 0){
            return;
        }
        if(errNmb == 1){
            error.setContentText("Machine ID must be a number!");
        }
        if(errNmb == 2){
            error.setContentText("Company name must include text, not just numbers!");
        }
        if(errNmb == 3){
            error.setContentText("All fields must have content!");
        }
        if(errNmb == 4){
            error.setContentText("Number cannot be negative!");
        }
        if(errNmb == 5){
            error.setContentText("Inventory cannot be greater than max field!");
        }
        if(errNmb == 6){
            error.setContentText("Inventory cannot be less than min field!");
        }
        if(errNmb == 7){
            error.setContentText("Min cannot be greater than max!");
        }
        if(errNmb == 8){
            error.setContentText("InHouse or Outsource button must be selected!");
        }
        error.showAndWait();
        return;
    }





    /** This adds an inHouse part tho the observable list.  */
        public void addInHousePart () {
            inv.addPart(
                    new InHouse(
                            Integer.valueOf(partIdTextField.getText()),
                            partNameTextField.getText(),
                            Double.valueOf(partPriceTextField.getText()),
                            Integer.valueOf(partInvTextField.getText()),
                            Integer.valueOf(partMinTextField.getText()),
                            Integer.valueOf(partMaxTextField.getText()),
                            Integer.valueOf(partSwappableTextField.getText())
                    ));
        }


    /** This adds an Outsourced part to the observable list. */
        public void addOutsourcedPart () {
            inv.addPart(
                    new Outsourced(
                            Integer.valueOf(partIdTextField.getText()),
                            partNameTextField.getText(),
                            Double.valueOf(partPriceTextField.getText()),
                            Integer.valueOf(partInvTextField.getText()),
                            Integer.valueOf(partMinTextField.getText()),
                            Integer.valueOf(partMaxTextField.getText()),
                            partSwappableTextField.getText()
                    ));
        }

    }