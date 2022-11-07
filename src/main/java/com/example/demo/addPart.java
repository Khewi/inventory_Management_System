package com.example.demo;

import com.example.demo.model.Inhouse;
import com.example.demo.model.Inventory;
import com.example.demo.model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * addPart controller - linked to addPart.FXML.
 */
public class addPart {

    /**
     * variables for addPart class.
     */
    public RadioButton outsourcedRB;
    public RadioButton inhouseRB;
    public Label partId;
    public Label partName;
    public Label partInv;
    public Label partPrice;
    public Label partMax;
    public Label partMin;
    public Label machineIdOrCompanyLabel;
    public TextField idTxt;
    public TextField nametxt;
    public TextField invtxt;
    public TextField pricetxt;
    public TextField maxtxt;
    public TextField mintxt;
    public TextField machineIDOrCompanyTxt;


    /**
     * generates unique id for part.
     * @return unique id
     */
    private int generatePartID() {
        int uniqueId = 1017;
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            uniqueId++;
        }
        return uniqueId;
    }

    /**
     * Saves the part when clicking the save button.
     * Runs logical errors prior to saving part.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionSavePartButton(ActionEvent actionEvent) throws IOException {
        if (nametxt.getText().isEmpty() || pricetxt.getText().isEmpty() || invtxt.getText().isEmpty() || mintxt.getText().isEmpty() || maxtxt.getText().isEmpty()) {
            confirmAlert("Error", "Unable to save part", "All fields must have a value");
            return;
        }
        try {
            int id = generatePartID();
            String name = nametxt.getText();
            int stock = Integer.parseInt(invtxt.getText());
            double price = Double.parseDouble(pricetxt.getText());
            int max = Integer.parseInt(maxtxt.getText());
            int min = Integer.parseInt(mintxt.getText());


            if (max < min) {
                confirmAlert("Error", "Unable to save part", "Stock maximum cannot be less than stock minimum.");
                return;
            } else if (max < stock || min > stock) {
                confirmAlert("Error", "Unable to save part", "Stock cannont be less than the min or greater than the max.");
                return;
            } else {

                if (outsourcedRB.isSelected()) {
                    //outsourced part selected
                    String companyName = machineIDOrCompanyTxt.getText();
                    Outsourced tempPart = new Outsourced(id, name, price, stock, min, max, companyName);
                    Inventory.addPart(tempPart);

                    Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                    Scene scene = new Scene(mainMenuScene);
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Part was saved and user is returned to main screen. Part should display in Parts tableview.");
                } else {
                    //inhouse RB selected
                    int machineId = Integer.parseInt(machineIDOrCompanyTxt.getText());
                    Inhouse tempPart = new Inhouse(id, name, price, stock, min, max, machineId);
                    Inventory.addPart(tempPart);

                    Parent mainMenuScene = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                    Scene scene = new Scene(mainMenuScene);
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    stage.setTitle("Inventory Management System");
                    stage.setScene(scene);
                    stage.show();
                    System.out.println("Part was saved and user is returned to main screen. Part should display in Parts tableview.");
                }
            }
        } catch (NumberFormatException e) {
            infoAlert("Error", "Unable to save part", "Data entered does not match expected input");
        }
    }


    /**
     * returns user to main screen when cancel button is clicked on screen.
     * uses confirmation box to redirect user depending on input selection.
     * @param actionEvent
     * @throws IOException
     */
    public void onActionCancelPartButton(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Would you like to cancel product?");
        alert.setContentText("Click OK to cancel");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK){
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
     * Sets machineIdOrCompanyLabel text to Company Name when outsourced radio button is selected.
     * @param actionEvent
     */
    public void onActionSelectOutsourcedPart(ActionEvent actionEvent) {
        machineIdOrCompanyLabel.setText("Company Name");
    }



    /**
     * Sets machineIdOrCompanyLabel text to Machine ID when inhouse radio button is selected.
     * @param actionEvent
     */
    public void onActionSelectInHousePart(ActionEvent actionEvent) {
        machineIdOrCompanyLabel.setText("Machine ID");
    }



    /**
     * Alert methof for information. Used for logical errors in other methods in addPart controller.
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
     * Alert methods for user confirmation. Used for user input actions in other methods in addPart controller.
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
