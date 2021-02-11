module InventorySystem {
    requires javafx.fxml;
    requires javafx.controls;

    opens View;
    opens Main;
    opens Controller;
    opens Model;

}