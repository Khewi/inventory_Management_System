package com.example.demo.model;

/**
 *
 * @author Katelyn hewitt
 */

/**
 * Outsourced class which extend the part class.
 */
public class Outsourced extends Part{

    private String companyName;

    /**
     *Outsourced constructor.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * sets the company name for the outsourced part.
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    /**
     * returns the company name for Outsourced part.
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }

}
