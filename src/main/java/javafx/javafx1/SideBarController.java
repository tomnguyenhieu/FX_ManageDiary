package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.plaf.ListUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

public class SideBarController extends App implements Initializable
{
    private List<HBox> listHboxs = new ArrayList<>();

    @FXML
    private AnchorPane contentPane;

    @FXML
    private VBox sbContainer;

    @FXML
    private VBox tiledPaneContainer;

    // Xu ly logic
    public void resetAllSidebarItems()
    {
//        List<HBox> listHboxs = new ArrayList<>();

        for (int i = 0; i < 4; i++)
        {
            listHboxs.add((HBox) tiledPaneContainer.getChildren().get(i));
        }

        for (int i = 1; i < 6; i++)
        {
            listHboxs.add((HBox) sbContainer.getChildren().get(i));
        }

        for (HBox item : listHboxs)
        {
            item.setStyle("-fx-background-color: #30475E;");
            Label labelTest = (Label) item.getChildren().getFirst();
            labelTest.setTextFill(Color.WHITE);
        }

    }
    public String getSceneName(String hboxId)
    {
        String sceneName = "";
        switch (hboxId)
        {
            case "classBtn":
                sceneName = "ManageClassScene";
                break;
            case "studentBtn":
                sceneName = "ManageStudentScene";
                break;
            case "teacherBtn":
                sceneName = "ManageTeacherScene";
                break;
            case "employeeBtn":
                sceneName = "ManageEmployeeScene";
                break;
            case "dbTeachersStaffsBtn":
                sceneName = "DBTeachersStaffsScene";
                break;
            case "dbChiBtn":
                sceneName = "DBChiScene";
                break;
            case "dbStudentsBtn":
                sceneName = "DashboardStudentScene";
                break;
        }
        return sceneName;
    }

    // Xu ly event
    public void onMouseClickSetActivate(MouseEvent event)
    {
        resetAllSidebarItems();
        HBox hbox = (HBox) event.getSource();
        hbox.setStyle("-fx-background-color: #F05454; -fx-background-radius: 5px;");
        String hboxId = hbox.getId();
        String sceneName = getSceneName(hboxId);

        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(sceneName + ".fxml"));
            Parent root = fxmlLoader.load();
            contentPane.getChildren().removeAll();
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        switch (sceneName)
        {
            case "ManageClassScene":
                ManageClassController manageClassController = fxmlLoader.getController();
                manageClassController.onMouseClickDisplayClasses();
                manageClassController.getContentPane(contentPane);
                break;
        }
    }
    public void onMouseClickGetTitledPaneChildren(MouseEvent event)
    {
        HBox hbox = (HBox) event.getSource();
        resetAllSidebarItems();
        hbox.setStyle("-fx-background-color: #F05454; -fx-background-radius: 5px;");

        String hboxId = hbox.getId();
        System.out.println(hboxId);
        String sceneName = getSceneName(hboxId);

        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource(sceneName + ".fxml"));
            Parent root = fxmlLoader.load();
            contentPane.getChildren().removeAll();
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetAllSidebarItems();
    }
}
