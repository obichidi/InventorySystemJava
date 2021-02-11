package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 This is the class that constructs the functionality  of the inventory for the Inventory
 * System.
 *
 */


public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method adds a part to  inventory
     * @param newPart is the Part to be added to the list
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * This method adds a product to the inventory
     * @param newProduct Product  is the product to be added to the list
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * this method checks for a part according to the part id
     * @param partId Integer  Id number of the part
     * @return Returns a list of parts that match the id number
     */

    public static Part lookupPart(int partId){
       // for loop that searches through the id's to find part matched based on the part id.
        for(int i = 0; i < allParts.size(); i++){
            if(allParts.get(i).getId() == partId){
                return allParts.get(i);
            }
        }

        //alerts for if there is no id match
        Alert notFound = new Alert(Alert.AlertType.ERROR);
        notFound.setTitle("ERROR!");
        notFound.setHeaderText("Not found!");
        notFound.setContentText("No part found!");
        notFound.showAndWait();
        return null;
    }

    /**
     * this method checks for a product according to and Id number.
     * @param productId Integer of the product
     * @return Returns a list of products that match the id number.
     */

    public static Product lookupProduct(int productId){
// for loop that searches through the id's to find products that match product id.
        for(int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productId){
                return allProducts.get(i);
            }
        }
        //alerts for if there is no id match
        Alert notFound = new Alert(Alert.AlertType.ERROR);
        notFound.setTitle("ERROR!");
        notFound.setHeaderText("Not found!");
        notFound.setContentText("No product found!");
        notFound.showAndWait();
        return null;
    }

    /**
     * This method searches for  a part based on the string partName.
     * @param partName String of the part name
     * @return Returns the list of parts that match
     */
    public static ObservableList<Part> lookupPart(String partName) {


        ObservableList<Part> partsReturned = FXCollections.observableArrayList();

        if(allParts.isEmpty()){
            return null;
        }



        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                partsReturned.add(part);
            }
        }

        if (partsReturned.isEmpty()) {
            System.out.println("returned part is empty");
            Alert notFound = new Alert(Alert.AlertType.ERROR);
            notFound.setTitle("ERROR!");
            notFound.setHeaderText("Not found!");
            notFound.setContentText("No part found!");
            notFound.showAndWait();

        }
     return partsReturned;
    }

    /**
     * This method searches for a product based on the string productName.
     * @param productName String of the product name
     * @return Returns the list of products that match
     */
    public static ObservableList<Product> lookupProduct(String productName){
        if(allProducts.isEmpty()){
            return null;
        }

        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        for(Product product: allProducts){
            if(product.getName().contains(productName)){
                returnedProducts.add(product);
            }
        }
        if(returnedProducts.isEmpty()){
            Alert notFound = new Alert(Alert.AlertType.ERROR);
            notFound.setTitle("ERROR!");
            notFound.setHeaderText("Not found!");
            notFound.setContentText("No product found!");
            notFound.showAndWait();
        }
        return returnedProducts;
    }

    /**
     * This method updates the part selected
     * @param index the index of the part being updated
     * @param selectedPart the part selected
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * This method updates a product selected
     * @param index the index level of the product being updated
     * @param newProduct the new product selected that is updated
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * This method deletes a selected part from the list of parts
     * @param selectedPart Part to delete
     * @return Boolean returns true
     */
    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * This method deletes a selected product from the list of products.
     * @param selectedProduct Product to delete
     * @return Boolean returns true
     */
    public static boolean deleteProduct(Product selectedProduct){

        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * This method returns a list of all the parts in inventory
     * @return returns a list of all the parts.
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * This method returns a list of all the products in inventory
     * @return returns a list of all the products.
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    /**
     * this method gets the  Id number  of the last part in inventory.
     * @return returns the ID for the last part in the list
     */
    public static int getLastPartId(){
        int maxId = 0;
        for(Part part: allParts){
            if(part.getId() > maxId){
                maxId = part.getId();
            }
        }
        return maxId;
    }
    /**
     * this method gets the  Id number  of the last product in inventory.
     * @return returns the ID for the last product in the list
     */
    public static int getLastProductId(){
        int maxId = 0;
        for(Product product: allProducts){
            if(product.getId() > maxId){
                maxId = product.getId();
            }
        }
        return maxId;
    }
}
