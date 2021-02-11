package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Controller.MainController;
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
import Model.Outsourced;
import Model.Part;

/** This is the controller class for the ModifyPart.fxml. */
public class ModifyPartController implements Initializable {

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

    Inventory inv;
    Part partSelected;
    int errCde = 0;
    boolean isErr = false;


    /**
     * This method initializes the ModifyPartController constructor.
     *
     * @param inv  Inventory that we send from screen to screen
     * @param partSelected Part selected by user on the MainScreenController
     */


    public ModifyPartController(Inventory inv, Part partSelected) {
        this.inv = inv;
        this.partSelected = partSelected;
    }


    /**
     * This method initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (partSelected instanceof Outsourced) {


            Outsourced outsourced = (Outsourced) this.partSelected;
            this.outsourcedRadioButton.setSelected(true);
            this.partIdTextField.setText(String.valueOf(outsourced.getId()));
            this.partNameTextField.setText(outsourced.getName());
            this.partInvTextField.setText(String.valueOf(outsourced.getStock()));
            this.partPriceTextField.setText(String.valueOf(outsourced.getPrice()));
            this.partMaxTextField.setText(String.valueOf(outsourced.getMax()));
            this.partMinTextField.setText(String.valueOf(outsourced.getMin()));
            this.partSwappableLabel.setText("Company Name");
            this.partSwappableTextField.setText(outsourced.getCompanyName());

        }


        if (partSelected instanceof InHouse) {
            InHouse inHouse = (InHouse) this.partSelected;
            this.inHouseRadioButton.setSelected(true);
            this.partIdTextField.setText(String.valueOf(inHouse.getId()));
            this.partNameTextField.setText(inHouse.getName());
            this.partInvTextField.setText(String.valueOf(inHouse.getStock()));
            this.partPriceTextField.setText(String.valueOf(inHouse.getPrice()));
            this.partMaxTextField.setText(String.valueOf(inHouse.getMax()));
            this.partMinTextField.setText(String.valueOf(inHouse.getMin()));
            this.partSwappableLabel.setText("Machine ID");
            this.partSwappableTextField.setText(String.valueOf(inHouse.getMachineId()));
        }
    }

    /**
     * This method is for when the inHouse radio button is selected,
     * the outsourced radio button will be deselected and  a label will be changed to Machine Id.
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
     * This method sends the user to the MainScreen.fxml screen.
     *
     * @param event triggers the method
     */
    @FXML
    public void cancelButtonPushed(ActionEvent event) {
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
     * This method saves the modified information and sends
     * it to the MainScreen. It generates alerts via error code.
     *
     * @param event triggers the method
     */
    @FXML
    public void saveButtonPushed(ActionEvent event) {
        try {

            this.errCde = 0;
            errChck();
            showAlert(errCde);

            if (errCde == 0) {
                if (inHouseRadioButton.isSelected() && (partSelected instanceof Outsourced)) {
                    updateToInHouse();
                }


                if (inHouseRadioButton.isSelected() && (partSelected instanceof InHouse)) {
                    updateToInHouse();
                }


                if (outsourcedRadioButton.isSelected() && (partSelected instanceof InHouse)) {
                    updateToOutsourced();
                }


                if (outsourcedRadioButton.isSelected() && (partSelected instanceof Outsourced)) {
                    updateToOutsourced();
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
     * This method updates the part to an InHouse Part or re-initializes a part as InHouse.
     */
    public void updateToInHouse() {
        inv.updatePart(
                partSelected.getId() - 1,
                new InHouse(
                        Integer.valueOf(partIdTextField.getText()),
                        partNameTextField.getText(),
                        Double.valueOf(partPriceTextField.getText()),
                        Integer.valueOf(partInvTextField.getText()),
                        Integer.valueOf(partMinTextField.getText()),
                        Integer.valueOf(partMaxTextField.getText()),
                        Integer.valueOf(partSwappableTextField.getText()))
        );
    }

    /**
     * This method updates the part to an outsourced Part or re-initializes a part as outsourced.
     */
    public void updateToOutsourced() {
        inv.updatePart(
                partSelected.getId() - 1,
                new Outsourced(
                        Integer.valueOf(partIdTextField.getText()),
                        partNameTextField.getText(),
                        Double.valueOf(partPriceTextField.getText()),
                        Integer.valueOf(partInvTextField.getText()),
                        Integer.valueOf(partMinTextField.getText()),
                        Integer.valueOf(partMaxTextField.getText()),
                        partSwappableTextField.getText())
        );
    }

   /**This checks for errors and shows an error message alert according to
    * an error code. */
    public void errChck() {


        if (inHouseRadioButton.isSelected() && partSwappableTextField.getText().matches("[a-zA-Z]+")) {
            isErr = true;
            this.errCde = 1;
            return;
        }

        /*
        If the outsourced radio button is selected and the text field contains numbers
        an error is thrown.
        */
        if (outsourcedRadioButton.isSelected() && partSwappableTextField.getText().matches("^[0-9]*$")) {
            isErr = true;
            this.errCde = 2;
            return;
        }

        /*
        If the content of any text field is empty an error is thrown.
        */
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

        /*
        If inventory, max, or min field are less than zero an error is thrown.
        */
        if (Integer.valueOf(partInvTextField.getText()) < 0 ||
                Integer.valueOf(partMaxTextField.getText()) < 0 ||
                Integer.valueOf(partMinTextField.getText()) < 0 ||
                Double.valueOf(partPriceTextField.getText()) < 0) {
            isErr = true;
            this.errCde = 4;
            return;
        }

        /**
         * If the value of the inventory is greater than the  max allowable an error is thrown.
         */
        else if (Integer.valueOf(partInvTextField.getText()) >
                Integer.valueOf(partMaxTextField.getText())) {
            isErr = true;
            this.errCde = 5;
            return;
        }

        /**
         * If the value of the inventory is less than the min allowable an error is thrown.
         */
        else if (Integer.valueOf(partInvTextField.getText()) <
                Integer.valueOf(partMinTextField.getText())) {
            isErr = true;
            this.errCde = 6;
            return;
        }

        /**
         * If the value of our min field is greater than the max field an error is thrown.
         */
        else if (Integer.valueOf(partMinTextField.getText()) >
                Integer.valueOf(partMaxTextField.getText())) {
            isErr = true;
            this.errCde = 7;
            return;
        }
    }

    /**
     * This method shows a message alert based on and error number.
     *
     * @param errNmb is the number that generates the error message
     */

    public void showAlert(int errNmb) {

        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR");
        error.setHeaderText("CANNOT SAVE");



        if (errNmb == 0) {
            return;
        }
        if (errNmb == 1) {
            error.setContentText("Machine ID must be a number!");
        }
        if (errNmb == 2) {
            error.setContentText("Company name must not include text, nut just numbers!");
        }
        if (errNmb == 3) {
            error.setContentText("All fields must have content!");
        }
        if (errNmb == 4) {
            error.setContentText("Number cannot be negative!");
        }
        if (errNmb == 5) {
            error.setContentText("Inventory cannot be greater than max field!");
        }
        if (errNmb == 6) {
            error.setContentText("Inventory cannot be less than min field!");
        }
        if (errNmb == 7) {
            error.setContentText("Min cannot be greater than max!");
        } else {
            error.showAndWait();
            return;
        }
    }
}