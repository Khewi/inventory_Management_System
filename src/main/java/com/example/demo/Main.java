package com.example.demo;
/**
 *
 * @author Katelyn hewitt
 */
import com.example.demo.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * main method that launches the application main screen.
 */
public class Main extends Application implements Initializable {

    /**
     * FUTURE ENHANCEMENT
     * Future updates would remove the logical error check that prevents the inventory from being outside the numerical bounds of the mina and max.
     * Instead the user would be allowed to add or update a part with the accurate inventory stock of the part or product. If the stock exceeded the max,
     * selected item will display green text to indicate there is a surplus of stock. If the stock is less than the minimum value,
     * the text for the item would display as red to indicate low stock for the purposes of reordering or replenishing.
     * This enhancement would help support what realistically happens when items logged into the inventory system in real life environments.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * main method which sets test data for program.
     * @param args
     */
    public static void main(String[] args) {
    //In-House Part data
        Inventory.addPart(new Inhouse(1011, "Ball Bearing", 249.99, 5, 2, 7, 7266));
        Inventory.addPart(new Inhouse(1012, "Roller Bearing", 149.99, 7, 2, 7, 7264));
        Inventory.addPart(new Inhouse(1013, "O Ring", 2.99, 105, 25, 200, 9256));
    //Outsourced Part Data
        Inventory.addPart(new Outsourced(1014, "Bolt", 1.99, 50, 25, 75, "Bolts R Us"));
        Inventory.addPart(new Outsourced(1015, "Nut", 1.99, 40, 25, 75, "Nuts R Us"));
        Inventory.addPart(new Outsourced(1016, "Red Packing Grease", 35.99, 5, 7, 15, "Great Grease, Co."));
        Inventory.addPart(new Outsourced(1017, "Corrosion X", 19.99, 6, 3, 10, "Corrosion Experts, Inc"));
    //Product test data
        Inventory.addProduct(new Product(1001, "Transmission Case", 799.99, 2, 2, 10));
        Inventory.addProduct(new Product(1002, "Gearbox", 1999.99, 7, 2, 10));
        Inventory.addProduct(new Product(1003, "Tail Rotor Gearbox Box", 699.99, 2, 1, 5));


        launch();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}