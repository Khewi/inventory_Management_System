package com.example.demo;
/**
 *
 * @author Katelyn hewitt
 */
import javafx.collections.ObservableList;
import com.example.demo.model.Inventory;
import com.example.demo.model.Part;
import com.example.demo.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
 * mainMenu controller - links to mainMenu.FXML
 */
public class mainMenu implements Initializable {

    /**
     * variables for mainMenu controller
     */
    //PartTV
    @FXML
    public TableView<Part> partTV;
    @FXML
    public TableColumn<Part, Integer> partIDCol;
    @FXML
    public TableColumn<Part, String> partNameCol;
    @FXML
    public TableColumn<Part, Integer> partInvCol;
    @FXML
    public TableColumn<Part, Double> partPriceCol;
    @FXML


    //ProductTV
    public TextField searchProductsTV;
    @FXML
    public TableView<Product> productTV;
    @FXML
    public TableColumn<Product, Integer> productsIDCol;
    @FXML
    public TableColumn<Product, String> productsNameCol;
    @FXML
    public TableColumn<Product, Integer> productsInvCol;
    @FXML
    public TableColumn<Product, Double> productsPriceCol;
    public TextField searchPartsTV;

    @FXML
    public Button closeButton;



    /**
     * method sends user to the add product screen when add button under product tableview is clicked.
      * @param actionEvent
     * @throws IOException
     */
    public void onActionGoToAddProductScreen(ActionEvent actionEvent) throws IOException {
        System.out.println("When I am clicked I send you to the Add Product screen.");

        Parent addProductScene = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        Scene scene = new Scene(addProductScene);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * method sends user to modify product screen when a part is selected.
     * Uses sendProduct method from modifyProduct controller to send product data for modification.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionGoToModifyProductScreen(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyProduct.fxml"));
        loader.load();

        modifyProduct mPrController = loader.getController();
        mPrController.sendProduct(productTV.getSelectionModel().getSelectedItem());

        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Modify Part");
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.showAndWait();

        System.out.println("When I am clicked I send you back to the Modify Product screen.");

    }

    /**
     * deletes selected part from parts tableview.
     * @param actionEvent
     */
    public void onActionDeletePart(ActionEvent actionEvent) {
        Part part = partTV.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you sure you want to delete this part?");
        alert.setContentText("Click OK to delete part.");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            ObservableList<Part> allParts, singlePart;
            allParts = partTV.getItems();
            singlePart = partTV.getSelectionModel().getSelectedItems();
            singlePart.forEach(allParts::remove);
        } else {
            return;
        }
    }

    /**
     * Standardized info alert used in other methods for logical error checks.
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
     * method exits the application when Exit button is clicked.
     * @param actionEvent
     */
    public void onActionExitProgram(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

        System.out.println("Yay! I worked by closing the program!");

    }

    /**
     * method redirects user to the add part screen when add button is selected under part tableview.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionGoToAddPartScreen(ActionEvent actionEvent) throws IOException {
        System.out.println("When I am clicked I send you to the Add Part screen.");

        Parent addPartScene = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        Scene scene = new Scene(addPartScene);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method sends user to modify part screen and uses the sendPart method from modifyPart controller.
     * Auto-populates data on selected part for modification.
      * @param actionEvent
     * @throws IOException
     */
    public void onActionGoToModifyPartScreen(ActionEvent actionEvent) throws IOException {
        System.out.println("When I am clicked I send you to the Modify Part screen.");


        try{
            Part selectedPart = partTV.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("modifyPart.fxml"));
            loader.load();

            modifyPart mpController = loader.getController();
            mpController.sendPart(partTV.getSelectionModel().getSelectedItem());

            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e) {

        }

    }

    /**
     * method searched the search bar over the parts tableview by part id or part name.
      * @param actionEvent
     */
    public void onActionSearchPartsTV(ActionEvent actionEvent) {
        String partText = searchPartsTV.getText();
        ObservableList<Part> Results = Inventory.lookUpPart(partText);

        try {
            if (Results.size() == 0) {

                int partID = Integer.parseInt(partText);
                Part ID = Inventory.lookUpPart(partID);
                if (ID != null) {
                    Results.add(ID);
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
     * method searched products in product tableview through search bar by product id or name.
     * @param actionEvent
     */
    public void onActionSearchProductsTV(ActionEvent actionEvent) {
        String productText = searchProductsTV.getText();
        ObservableList<Product> Results = Inventory.lookUpProduct(productText);

        try{
            if(Results.size() == 0) {
                int productID = Integer.parseInt(productText);
                Product ID = Inventory.lookUpProduct(productID);
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
        productTV.setItems(Results);
    }

    /**
     * method deletes a selected product for the allProducts observablelist and updates the products tableview.
     * method will not allow products with associated parts to be deleted. Error will display.
     * @param actionEvent
     */
    public void onActionDeleteProduct(ActionEvent actionEvent) {
        Product product = productTV.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Are you sure you want to delete this part?");
        alert.setContentText("Click OK to delete part.");
        Optional<ButtonType> answer = alert.showAndWait();
        try {
            if (answer.get() == ButtonType.OK) {
                if (product.getAllAssociatedPart().isEmpty()) {
                    ObservableList<Product> allProducts, singleProduct;
                    allProducts = productTV.getItems();
                    singleProduct = productTV.getSelectionModel().getSelectedItems();
                    singleProduct.forEach(allProducts::remove);

                    System.out.println("Product did not have associated parts and was deleted from inventory.");
                } else if (product.getAllAssociatedPart().size() != 0) {
                    infoAlert("Error", "Unable to delete product", "Product has associated parts and cannot be deleted.");
                }
            }
        }
        catch (NumberFormatException e) {
            infoAlert("Error", "Unable to delete selected product or products.", "");
        }

    }


    /**
     * initialize method sets the items in both tableviews
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Parts Table View set to pull data from Parts Observablelist
        partTV.setItems(Inventory.getAllParts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTV.setItems(Inventory.getAllProducts());

        productsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


}
