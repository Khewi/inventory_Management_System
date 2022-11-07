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
 * modifyProduct controller - links to modifyProduct.FXML.
 */
public class modifyProduct implements Initializable {

    /**
     * variables for modifyProduct controller.
     */
    //text fields for product data
    public TextField modProductIDTxt;
    public TextField modProductNameTxt;
    public TextField modProductInvtxt;
    public TextField modProductPricetxt;
    public TextField modProductMaxtxt;
    public TextField modProductMinTxt;
    public TextField modProductSearchTxt;

    //associated parts observablelist
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    //parts list tableview and column variables
    public TableView partListTable;
    public TableColumn<Part, Integer> partIDCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partInvCol;
    public TableColumn<Part, Double> partPriceCol;

    //associated parts tableview and columns
    public TableView associatePartListTable;
    public TableColumn<Part, Integer> assocPartIDCol;
    public TableColumn<Part, String> assocPartNameCol;
    public TableColumn<Part, Integer> assocPartInvCol;
    public TableColumn<Part, Double> assocPartPriceCol;

    //variables that allow product to be updated
    Product modifiedProduct;
    Product selectedProduct;
    int selectedIndex;


    /**
     * method receives data on a selected product from the main screen and auto-populates data into text fields for modification.
     * @param selectedProduct
     */
    public void sendProduct(Product selectedProduct) {

        this.selectedProduct = selectedProduct;

        selectedIndex = Inventory.getAllProducts().indexOf(selectedProduct);

        modProductIDTxt.setText(String.valueOf(selectedProduct.getId()));
        modProductNameTxt.setText(selectedProduct.getName());
        modProductPricetxt.setText(String.valueOf(selectedProduct.getPrice()));
        modProductInvtxt.setText(String.valueOf(selectedProduct.getStock()));
        modProductMaxtxt.setText(String.valueOf(selectedProduct.getMax()));
        modProductMinTxt.setText(String.valueOf(selectedProduct.getMin()));
        associatedPartsList.addAll(selectedProduct.getAllAssociatedPart());

    }

    /**
     * method searches the search bar for the parts tableview.
     * @param actionEvent
     */
    public void onActionSearch(ActionEvent actionEvent) {
        String partText = modProductSearchTxt.getText();
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
        partListTable.setItems(Results);
    }

    /**
     * method adds a part to the associated parts table for the product.
     * @param actionEvent
     */
    public void onActionAddProduct(ActionEvent actionEvent) {
        //add button for adding parts to associated parts list

        Part selectedPart = (Part) partListTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            infoAlert("Error", "Unable to save - Part not selected", "Please select a part to save.");
        } else if (selectedPart != null) {
            selectedProduct.addAssociatedParts(selectedPart);
            associatePartListTable.setItems(selectedProduct.getAllAssociatedPart());
            
        }
    }

    /**
     * method removes an associated part from the product when the Remove Associated Part button is clicked.
     * @param actionEvent
     */
    public void onActionRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) associatePartListTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete;");
        alert.setHeaderText("Are you sure you want to delete associated part?");
        alert.setContentText("Click OK to delete part");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            selectedProduct.deleteAssociatedPart(selectedPart);
            associatePartListTable.setItems(selectedProduct.getAllAssociatedPart());
        } else if (answer.get() == ButtonType.CANCEL) {
            return;
        }
    }


    /**
     * method updates a modified product after running logical error checks.
     * Product data must pass logical error checks to successfully update product.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionSaveProduct(ActionEvent actionEvent) throws IOException {
        /**
         * RUNTIME ERROR
         * Issue: When attempting to update the product, the method would overwrite the first position in the index instead of updating the selected product.
         * Solution: The selected part needed to have the variable 'selectedIndex' initialized in the sendProduct method. When this was initialized to the
         * product tableview in the main screen, the selectedIndex variable was able to find the index of the selected product and would update the product
         * in its intended manner when the save button was clicked. This fixed the issue of the onActionSaveProduct method over writing the incorrect index position.
         */
        if (modProductNameTxt.getText().isEmpty() ||modProductPricetxt.getText().isEmpty() || modProductInvtxt.getText().isEmpty() || modProductMaxtxt.getText().isEmpty() || modProductMinTxt.getText().isEmpty()){
            infoAlert("Error", "Unable to save product", "All text fields must have a value");
            return;
        }
        try {
            int stock = Integer.parseInt(modProductInvtxt.getText());
            int max = Integer.parseInt(modProductMaxtxt.getText());
            int min = Integer.parseInt(modProductMinTxt.getText());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Product;");
            alert.setHeaderText("Do you want to save your changes?");
            alert.setContentText("Click OK to save product");
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.get() == ButtonType.OK) {
                if (max < min) {
                    infoAlert("Error", "Unable to save part", "Stock maximum cannot be less than stock minimum.");
                } else if (stock < min || max < stock) {
                    infoAlert("Error", "Unable to save part", "Updated stock cannot be less than min or greater than max.");
                } else {
                    this.modifiedProduct = selectedProduct;
                    selectedProduct.setId(Integer.parseInt(modProductIDTxt.getText()));
                    selectedProduct.setName(modProductNameTxt.getText());
                    selectedProduct.setStock(Integer.parseInt(modProductInvtxt.getText()));
                    selectedProduct.setPrice(Double.parseDouble(modProductPricetxt.getText()));
                    selectedProduct.setMax(Integer.parseInt(modProductMaxtxt.getText()));
                    selectedProduct.setMin(Integer.parseInt(modProductMinTxt.getText()));

                    Inventory.getAllProducts().set(selectedIndex, selectedProduct);


                    Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                    Scene scene = new Scene(mainMenuScene);
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();

                }
            } else if (answer.get() == ButtonType.CANCEL) {
                return;
            }
        }catch (NumberFormatException e) {
            infoAlert("Error", "Unable to save product", "Data entered does not match the expected input");
        }
    }


    /**
     * method cancels the product modification when clicked and redirects to the main screen without modifying the displayed product.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        confirmAlert("Cancel", "Are you sure you want to cancel?", "");

        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene scene = new Scene(mainMenuScene);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Standardized info alert used in other methods for logical error alerts.
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
     * standardized confirmation alert for other methods when user input validation is required.
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
     * method updates the parts tableview.
     */
    public void updatePartTV() {
        partListTable.setItems(Inventory.getAllParts());
    }

    /**
     * method updates the associated parts tableview
     */
    private void updateAssocPartTable () {
        associatePartListTable.setItems(associatedPartsList);
    }

    /**
     * initialize method is used to set and update the data in the part and associated parts tableview.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Setting data for Parts table
        partListTable.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatePartListTable.setItems(associatedPartsList);
        assocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        updatePartTV();
        updateAssocPartTable();

    }



}
