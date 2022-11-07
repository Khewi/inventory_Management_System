package com.example.demo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 *
 * @author Katelyn hewitt
 */

/**
 * Inventory class allows parts and product observablelists to be updated.
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * returns the items in the allParts observablelist.
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }


    /**
     * returns the items in the allProducts observablelist.
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * adds an inhouse or outsourced part to the allParts observablelist.
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }


    /**
     * adds a product to the allProducts observablelist.
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }


    /**
     * looks up the part id in the observablelist by int.
     * @param partID
     * @return returns the selected part or returns null id does not match the list.
     */
    public static Part lookUpPart(int partID) {
        for (Part part : Inventory.getAllParts()) {
            while (part.getId() == partID) {
                return part;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Item not found");
        alert.show();
        return null;
    }

    /**
     * looks up the product id in the observablelist by int.
     * @param productID
     * @return returns null if no match or returns matching product.
     */
    public static Product lookUpProduct(int productID) {
        for (Product product : Inventory.getAllProducts()) {
            while (product.getId() == productID) {
                return product;
            }
        }
        return null;
    }

    /**
     * searches the observablelist by part name as string.
     * @param partName
     * @return the part name if match found.
     */
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> PartName = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                PartName.add(part);
            }
        }
        return PartName;
    }

    /**
     * searches the observable list array by product name.
     * @param productName
     * @return match for product if found.
     */
    public static ObservableList<Product> lookUpProduct(String productName) {
        ObservableList<Product> ProductName = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                ProductName.add(product);
            }
        }
        return ProductName;
    }

    /**
     * deletes the selected part from the allParts observablelist.
     * @param selectedPart
     * @return returns true if part deleted and false if unable to remove part.
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }


    /**
     * deletes product from allProducts observablelist.
     * @param selectedProduct
     * @return returns true if product was deleted and false if unable to delete.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }


    /**
     * updates the part by index in alParts observablelist.
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * updates the product by index in allProducts Observablelist.
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }



}




