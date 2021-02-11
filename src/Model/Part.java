package Model;


/** this method creates an abstract class for Part */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * This method sets up the constructor for the Part with the following parameters.
     * @param id Id of the part
     * @param name Name of the part
     * @param price Price of the part
     * @param stock Inventory level of the part
     * @param min Minimum allowable of the part
     * @param max Maximum allowable of the part
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


   /** This method gets the part id number
    * @return returns the part id*/
   public int getId() {
        return id;
    }

    /** this method sets the part id .
    * @param id is the part id number. */

    public void setId(int id) {
        this.id = id;
    }

    /** This method gets the name of a part
    * @return returns the name of the part */
    public String getName() {
        return name;
    }

    /** This method sets the name of a part in inventory.
  * @param name  is the name of the part */
    public void setName(String name) {
        this.name = name;
    }

    /** This method gets the price of the part in inventory.
     * @return returns the price of the part */
    public double getPrice() {
        return price;
    }
    /**This method sets the price of a part.
     * @param price  is the price of the part */
    public void setPrice(double price) {
        this.price = price;
    }

    /**This method gets the stock integer of a part.
     * @return returns the stock number of a part */
    public int getStock() {
        return stock;
    }

    /**This Method sets the stock number of a part.
     * @param stock  is the number of stock of a part */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** This method gets the min number of a part.
     * @return  returns  a minimum number  */
    public int getMin() {
        return min;
    }

    /**This Method sets the min num of a part
     * @param min  is the  minimum number of parts.*/
    public void setMin(int min) {
        this.min = min;
    }

    /** This method gets the max number of a part
     * @return returns the maximum number of parts */
    public int getMax() {
        return max;
    }

    /** This method sets the max number of  parts
     * @param max is the maximum number of the parts */
    public void setMax(int max) {
        this.max = max;
    }
}
