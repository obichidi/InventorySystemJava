package Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



/**
 * This class is used to represent a product in the inventory

 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;


    /**
     * This method sets up the constructor for the Product with the following parameters:
     * @param id Id of the product
     * @param name Name of the product
     * @param price Price of the product
     * @param stock Inventory level of the product
     * @param min Minimum allowable of the product
     * @param max Maximum allowable of the product
     */
    public Product(int id, String name, double price, int stock, int min,
                   int max){
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * This method sets up the constructor for our Product if no inventory level is provided
     * @param id Id of the product
     * @param name Name of the product
     * @param price Price of the product
     * @param min Minimum allowable of the product
     * @param max Maximum allowable of the product
     */
    public Product(int id, String name, double price, int min, int max){
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = 0;
        this.min = min;
        this.max = max;
    }


    /** This method gets the product id number
     * @param id is the product id number */
    public void setId(int id) {
        this.id = id;
    }

    /** this method sets the product id .
     * @return returns the product id . */
    public int getId() {
        return id;
    }

    /** This method gets the name of a product
     * @return returns the name of the product */
    public String getName() {
        return name;
    }

    /** this method sets the product name .
     * @param name is the product name. */
    public void setName(String name) {
        this.name = name;
    }

    /** This method gets the price of the product in inventory.
     * @return returns the price of the product */
    public double getPrice() {
        return price;
    }

    /**This method sets the price of a part.
     * @param price  is the product of the part */
    public void setPrice(double price) {
        this.price = price;
    }

    /**This method gets the stock integer of a product.
     * @return returns the stock number of a product*/
    public int getStock() {
        return stock;
    }

    /**This Method sets the stock number of a product.
     * @param stock  is the number of stock  of a product */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** This method gets the minimum number of a product.
     * @return  returns  a minimum number of a product */
    public int getMin() {
        return min;
    }

    /**This Method sets the minimum number of  product
     * @param min is the minimum number of  product.*/
    public void setMin(int min) {
        this.min = min;
    }

    /** This method gets the maximum number of  product
     * @return returns the maximum number of  product */
    public int getMax() {
        return max;
    }

    /** This method sets the maximum number of  product
     * @param max is the maximum number of  product */
    public void setMax(int max) {
        this.max = max;
    }


    /**This method adds and associated part to a product
     * @param part  is the part to be added to the product */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**This method is a boolean that deletes an associated part from a product.
     * @param selectedAssociatedPart  is the associated part selected.
     * @return  returns true */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    /**This method gets all associated parts of a product  and puts them in a list.
     * @return returns all associated parts of the product*/
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
