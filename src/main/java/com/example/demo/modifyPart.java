package com.example.demo;
/**
 * @author Katelyn Hewitt
 */

import com.example.demo.model.Inhouse;
import com.example.demo.model.Inventory;
import com.example.demo.model.Outsourced;
import com.example.demo.model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * modifyPart controller - links to modifyPart.FXML
 */

public class modifyPart {

    /**
     * variables for modifyPart controller
     */
    //text fields and label for part
    public TextField id;
    public TextField name;
    public TextField price;
    public TextField inv;
    public TextField max;
    public TextField min;
    public TextField machineIdOrCompanyTF;
    public Label machineIdOrCompanyLabel;

    //radio button variables
    public RadioButton outsourcedRB;
    public RadioButton inHouseRB;

    //variable that allows data to be set for the part
    public Part selectedPart;
    //variable gets the index of a selected part
    int selectedIndex;



    /**
     * sendPart method accepts data when a part is selected on the main screen and autopopulates the correct data in the text fields for modification.
     * @param selectedPart
     */
    public void sendPart (Part selectedPart) {

        this.selectedPart = selectedPart;

        selectedIndex = Inventory.getAllParts().indexOf(selectedPart);

        id.setText(String.valueOf(selectedPart.getId()));
        name.setText(selectedPart.getName());
        price.setText(String.valueOf(selectedPart.getPrice()));
        inv.setText(String.valueOf(selectedPart.getStock()));
        max.setText(String.valueOf(selectedPart.getMax()));
        min.setText(String.valueOf(selectedPart.getMin()));

        if(selectedPart instanceof Inhouse){
            inHouseRB.setSelected(true);
            Inhouse inhouse = (Inhouse) selectedPart;
            machineIdOrCompanyLabel.setText("Machine ID");
            machineIdOrCompanyTF.setText(Integer.toString(inhouse.getMachineId()));
        } else {
            outsourcedRB.setSelected(true);
            Outsourced outsourced = (Outsourced) selectedPart;
            machineIdOrCompanyLabel.setText("Company Name");
            machineIdOrCompanyTF.setText(String.valueOf(outsourced.getCompanyName()));
                    }

    }


    /**
     * changes machineIdOrCompanyLabel text to company name when outsourced radio button is selected
     * @param actionEvent
     */
    public void onActionSelectOutsourcedPart(ActionEvent actionEvent) {

        machineIdOrCompanyLabel.setText("Company Name");
    }


    /**
     * changes machineIdOrCompanyLabel text to machine id when inhouse radio button is selected
     * @param actionEvent
     */
    public void onActionSelectInHousePart(ActionEvent actionEvent) {
        machineIdOrCompanyLabel.setText("Machine ID");
    }

    /**
     * method saves the modified part after running logical error checks.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionSavePart(ActionEvent actionEvent) throws IOException {

        if (name.getText().isEmpty() || price.getText().isEmpty() || inv.getText().isEmpty() || max.getText().isEmpty() || min.getText().isEmpty()) {
            infoAlert("Error", "Unable to save part", "All text fields must have a value entered");
            return;
        }
        try {
            //need to add function to update the data.
            int ID = Integer.parseInt(id.getText());
            String nameP = name.getText();
            double priceP = Double.parseDouble(price.getText());
            int stock = Integer.parseInt(inv.getText());
            int maxP = Integer.parseInt(max.getText());
            int minP = Integer.parseInt(min.getText());


            if (maxP < minP) {
                infoAlert("Error", "Unable to save part", "Stock maximum cannot be less than stock minimum.");
                return;
            } else if (stock < minP || maxP < stock) {
                infoAlert("Error", "Unable to save part", "Updated stock cannot be less than min or greater than max.");
                return;
            } else {
                if (inHouseRB.isSelected()) {
                    confirmAlert("Save", "Do you want to save this part?", "Click OK to save the part to inventory.");
                    try {
                        int machineId = Integer.parseInt(machineIdOrCompanyTF.getText());
                        Inhouse tempInH = new Inhouse(ID, nameP, priceP, stock, minP, maxP, machineId);
                        selectedPart = tempInH;
                        Inventory.getAllParts().set(selectedIndex, selectedPart);

                        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                        Scene scene = new Scene(mainMenuScene);
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        stage.setTitle("Inventory Management System");
                        stage.setScene(scene);
                        stage.show();


                    } catch (NumberFormatException e) {
                        infoAlert("Error", "Unable to save part.", "");
                    }
                } else if (outsourcedRB.isSelected()) {
                    confirmAlert("Save", "Do you want to save this part?", "Click OK to save the part to inventory.");
                    String companyName = machineIdOrCompanyTF.getText();
                    Outsourced tempOut = new Outsourced(ID, nameP, priceP, stock, minP, maxP, companyName);
                    selectedPart = tempOut;
                    Inventory.getAllParts().set(selectedIndex, selectedPart);

                    Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                    Scene scene = new Scene(mainMenuScene);
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();

                }


            }
        } catch (NumberFormatException e) {
            infoAlert("Error", "Unable to save part", "Data entered does not match expected input");
        }

        }


    /**
     * method cancels part modification and returns user to main screen without altering data associated to the part.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionCancelPart(ActionEvent actionEvent) throws IOException {
        System.out.println("When I am clicked I send you back to the main menu screen.");

        Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene scene = new Scene(mainMenuScene);
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * standardized info alert to call in other methods for errors.
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
     * standardized confirm alert method to be used in other methods for user input validation.
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
}
