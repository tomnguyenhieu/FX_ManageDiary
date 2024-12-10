package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.plaf.ListUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SideBarController extends App implements Initializable
{
    @FXML
    private VBox sbContainer;

    // Xu ly logic
    public void resetAllSidebarItems()
    {
        for (int i = 1; i < 6; i++)
        {
            HBox hbox = (HBox) sbContainer.getChildren().get(i);
            hbox.setStyle("-fx-background-color: #30475E;");
            Label labelTest = (Label) hbox.getChildren().getFirst();
            labelTest.setTextFill(Color.WHITE);
        }
    }

    // Xu ly event
    public void onMouseClickSetActivate(MouseEvent event)
    {
        resetAllSidebarItems();
        HBox hbox = (HBox) event.getSource();
        hbox.setStyle("-fx-background-color: #F05454; -fx-background-radius: 5px;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetAllSidebarItems();
    }
}
