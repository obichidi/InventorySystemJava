package Model;


/**
 * This class represents an Outsourced Part in the inventory
 */
public class Outsourced extends Part {
    private String companyName;
    /**
     * This method helps to create  the constructor for the Outsourced Part with the following parameters.
     * @param id is the Id of the part
     * @param name is the Name of the part
     * @param price  is the Price of the part
     * @param stock is the Inventory level of the part
     * @param min is the Minimum allowable parts
     * @param max is the Maximum allowable parts
     * @param companyName is the Name of company the part is outsourced from
     */
    public Outsourced(int id, String name, double price, int stock,
                      int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * This method sets the company name of the outsourced part.
     * @param companyName is the Name of the company  set for the part
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     * This method gets the company name of the outsourced part
     * @return returns the Name of the outsource company
     */
    public String getCompanyName(){
        return this.companyName;
    }
}
