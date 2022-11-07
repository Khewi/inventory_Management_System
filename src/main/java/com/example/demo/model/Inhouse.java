package com.example.demo.model;
/**
 *
 * @author Katelyn hewitt
 */


 /**
    *Inhouse class which extends part.
     */

public class Inhouse extends Part{

    private int machineId;

     /**
      * InHouse construct which allows objects to be instantiated as extension of Part class.
      */
    public Inhouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

     /**
      * setter for Inhouse machine id.
      * @param machineId
      */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

     /**
      *getter for Inhouse machine id.
      * @return machine id
      */
    public int getMachineId() {
        return machineId;
    }
}

