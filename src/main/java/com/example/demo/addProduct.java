package com.example.demo;
/**
 * @author Katelyn Hewitt
 */

import com.example.demo.model.Inventory;
import com.example.demo.model.Part;
import com.example.demo.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * addProduct constructor - links to addProduct.FXML
 */
public class addProduct implements Initializable {

    /**
     * addProduct variables
     */
    //parts tableview, and columns
    public TableView partTV;
    public TableColumn addProdPartId;
    public TableColumn addProdName;
    public TableColumn addProdInv;
    public TableColumn addProdPrice;


    //associated parts tableview and columns
    public TableView assocPartTV;
    public TableColumn assocPartId;
    public TableColumn assocName;
    public TableColumn assocInv;
    public TableColumn assocPrice;


    //labels and text fields for product information
    public Label addProductIDLabel;
    public Label addProductNameLabel;
    public TextField maxTxt;
    public TextField InvTxt;
    public TextField priceTxt;
    public TextField minTxt;
    public TextField IDTxt;
    public TextField nameTxt;

    //text field for search bar variable
    public TextField addProductSearchTxt;
    //associated parts observable list
    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    //variable to add new products to through Product constructor
    Product newProduct;


    /**
     * Adds the part from the part table view to the associated parts table view. Item is added to the product array.
     * Associated parts table view is updated with the product method .getAllAssociatedParts to return updated list.
     * @param actionEvent
     */

    public void onActionAddProduct(ActionEvent actionEvent) {
        Part selectedPart = (Part) partTV.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            infoAlert("Error", "Unable to save - Part not selected", "Please select a part to save.");
        } else if (selectedPart != null) {
            newProduct.addAssociatedParts(selectedPart);
            assocPartTV.setItems(newProduct.getAllAssociatedPart());
            System.out.println(newProduct.getAllAssociatedPart().stream().toList());
        }
    }

    /**
     * method updates the associated parts tableview
     */
    private void updateAssociatedPTable(){
        assocPartTV.setItems(newProduct.getAllAssociatedPart());

    }

    /**
     * method deletes an associated part from the product. Returns error if a part is not selected.
     * @param actionEvent
     */
    public void onActionRemoveAssociatedPart(ActionEvent actionEvent) {

        Part selectedPart = (Part) partTV.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            confirmAlert("Delete", "Do you want to delete this part from the associated parts list?", "CLick OK to delete");
            newProduct.deleteAssociatedPart(selectedPart);
            updateAssociatedPTable();

        } else {
            infoAlert("Delete Unsuccessful", "No part selected", "Select a part to delete");
        }
    }


    /**
     * Saves a new product to the allProducts observablelist when data is entered and Save button is clicked. method checks for logical errors
     * prior to saving part if data is incomplete or wrong. method will update the Product class associated parts observablelist when product is saved.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionSaveProduct(ActionEvent actionEvent) throws IOException {

        if (nameTxt.getText().isEmpty() || priceTxt.getText().isEmpty() || InvTxt.getText().isEmpty() || maxTxt.getText().isEmpty() || minTxt.getText().isEmpty()) {
            infoAlert("Error", "Invalid entry", "each field must have an entry to save product.");
            return;

        } else {
            try {
                int ID = 0;
                for (Product product : Inventory.getAllProducts()) {
                    if (product.getId() > ID) {
                        ID = product.getId();
                    }
                }
                IDTxt.setText(String.valueOf(++ID));
                System.out.println("ID set");
                String name = nameTxt.getText();
                double price = Double.parseDouble(priceTxt.getText());
                int inv = Integer.parseInt(InvTxt.getText());
                int max = Integer.parseInt(maxTxt.getText());
                int min = Integer.parseInt(minTxt.getText());


                if (max < min) {
                    infoAlert("Error", "Unable to save part", "Stock maximum cannot be less than stock minimum.");
                } else if (inv < min || max < inv) {
                    infoAlert("Error", "Unable to save part", "Stock cannot be less than min or greater than max.");
                } else {
                    try {

                        confirmAlert("Save", "Do you want to save this product?", "Click OK to save.");

                        newProduct.setId(ID);
                        newProduct.setName(name);
                        newProduct.setPrice(price);
                        newProduct.setStock(inv);
                        newProduct.setMin(min);
                        newProduct.setMax(max);
                        System.out.println("id, name, stock, price, min, max is good");
                        Inventory.addProduct(newProduct);


                        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                        Scene scene = new Scene(mainMenuScene);
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        stage.setTitle("Inventory Management System");
                        stage.setScene(scene);
                        stage.show();

                    } catch (NumberFormatException e) {
                        System.out.println("Unable to save product");
                    }
                }
                }catch(NumberFormatException e){
                    infoAlert("Error", "Unable to save product", "Data entered does not match the expected input");
                }
        }
    }


    /**
     * Returns user to the main screen and does not add product when cancel button is clicked.
     * Confirmation box pops us for user validation on action.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Would you like to cancel product?");
        alert.setContentText("Click OK to cancel");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            System.out.println("When I am clicked I send you back to the main menu screen.");

            Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            Scene scene = new Scene(mainMenuScene);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        } else if (answer.get() == ButtonType.CANCEL) {
            return;
        }
    }

    /**
     * Searches by part id and name to find a part match in search bar. Returns error if no part matches input.
     * @param actionEvent
     */
    public void onActionSearch(ActionEvent actionEvent) {
        String partText = addProductSearchTxt.getText();
        ObservableList<Part> Results = Inventory.lookUpPart(partText);

        try{
            if(Results.size() == 0) {
                int partID = Integer.parseInt(partText);
                Part ID = Inventory.lookUpPart(partID);
                if (ID != null) {
                    Results.add(ID);
                }
                else if (ID == null){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Item not found");
                    alert.show();
                }
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Item not found");
            alert.show();
        }
        partTV.setItems(Results);
    }


    /**
     * method was used to create a standardized information alert for errors in logic check code
     * @param title
     * @param header
     * @param content
     */
    private static void infoAlert(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * method was used to create standardized confirmation alert for user validate functions.
     * @param title
     * @param header
     * @param content
     * @return
     */
    private static boolean confirmAlert (String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK){
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Initialize method was used to set the data in both tableviews prior to running any other methods.
     * newProduct is initialized here to allow products and associated parts to be added through the product class methods.
     * The associated parts table view is updated.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Setting data for parts table
        partTV.setItems(Inventory.getAllParts());
        addProdPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdInv.setCellValueFactory( new PropertyValueFactory<>("stock"));
        addProdPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartTV.setItems(associatedParts);
        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInv.setCellValueFactory( new PropertyValueFactory<>("stock"));
        assocPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        newProduct = new Product(0, null, 0, 0, 0, 0);
        assocPartTV.setItems(newProduct.getAllAssociatedPart());


    }
}
