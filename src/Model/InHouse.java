package Model;


/** This class models the InHouse part in the inventory */
public class InHouse extends Part {

    private int machineId;
    /**
     * This method creates the constructor for the InHouse Part with the following parameters.
     * @param id the ID of the part number
     * @param name the Name of the part
     * @param price the Price of the part
     * @param stock the Inventory level of the part
     * @param min the Minimum allowable parts
     * @param max the Maximum allowable parts
     * @param machineID the Machine ID for part
     */
    public InHouse(int id, String name, double price, int stock, int min,
                   int max, int machineID){
        super(id, name, price, stock, min, max);

        this.machineId = machineID;
    }
    /**
     * This method sets the machine Id
     * @param machineId is the ids identifier integer for the inHouse part
     */
    public void setMachineID(int machineId){
        this.machineId = machineId;
    }
    /**
     * this method returns the part machine Id
     * @return returns the machineId of the part .
     */
    public int getMachineId(){
        return this.machineId;
    }
}
