package com.example.demo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

/**
 *
 * @author Katelyn hewitt
 */

/**
 * Product class that contains getters, setters, and delete functions for products and associated parts
 */

public class Product {


    /**
     *   Private Variables for product class
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;

    private String name;

    private double price;

    private int stock;

    private int min;

    private int max;



    /**
     *Product class constructor
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }


    /**
     * getter for product id
     * @return id
     */
    public int getId() {
        return id;
    }



    /**
     * setter for product id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }



    /**
     * getter for product name
     * @return product name
     */
    public String getName() {
        return name;
    }



    /**
     * setter for product name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * getter for product price
     * @return product price
     */
    public double getPrice() {
        return price;
    }



    /**
     * setter for product price
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }



    /**
     * getter for product stock level
     * @return product current stock
     */
    public int getStock() {
        return stock;
    }



    /**
     * setter for product stock
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }



    /**
     * getter for product stock minimum
     * @return product minimum
     */
    public int getMin() {
        return min;
    }



    /**
     * setter product stock minimum
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }




    /**
     * getter for product stock maximum
     * @return product max
     */
    public int getMax() {
        return max;
    }




    /**
     * setter for product stock maximum
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }



    /**
     * adds associated product parts to the associatedparts observablelist
     * @param associatedPartsList
     */
    public void addAssociatedParts(Part associatedPartsList) {
        associatedParts.addAll(associatedPartsList);
    }



    /**
     * returns list of all associated parts in the associatedParts observablelist
     * @return
     */
    public ObservableList<Part> getAllAssociatedPart(){
        return associatedParts;
    }



    /**
     * deletes an associated part from the associatedParts observablelist
     * @param selectedPart
     * @return true if part is removed and false if part does not exist
     */
    public boolean deleteAssociatedPart(Part selectedPart){
        if (associatedParts.contains(selectedPart)){
            associatedParts.remove(selectedPart);

            return true;
        }
        else {
            return false;
        }
    }

    }
