package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.plaf.synth.SynthUI;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageClassController extends App
{
    private int classId = 0;
    private boolean isClicked = false;

    @FXML
    private TilePane tilePane;

    // Xu ly logic
    public void displayClass(String className, String teacherName, int totalStudents)
    {
        VBox vbox = new VBox();
        vbox.setMinSize(240, 150);
        vbox.setPrefSize(240, 150);
        vbox.setStyle("-fx-background-color:  #30475E; -fx-background-radius: 8");

        Label label = new Label(className);
        label.setMinSize(240, 46);
        label.setPrefSize(240, 46);
        label.setFont(new Font("Roboto", 24));
        label.setTextFill(Color.WHITE);
        label.setAlignment(Pos.CENTER);
        label.setCursor(Cursor.HAND);
        label.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClickLoadListLessons);
        label.setStyle("-fx-font-weight: bold; -fx-background-color:  #F05454; -fx-background-radius: 8 8 0 0");

        HBox hbox = new HBox();
        hbox.setMinSize(200, 100);
        hbox.setPrefSize(200, 100);

        VBox vboxText = new VBox();
        vboxText.setMinSize(190, 82);
        vboxText.setPrefSize(190, 82);
        vboxText.setPadding(new Insets(0, 0, 0, 12));
        vboxText.setStyle("-fx-background-color:  #ffffff; -fx-background-radius: 6");
        HBox.setMargin(vboxText, new Insets(10, 0, 10, 10));

        Label teacherNameLabel = new Label();
        teacherNameLabel.setText("Giáo viên: " + teacherName);
        teacherNameLabel.setMinSize(190, 41);
        teacherNameLabel.setFont(new Font(14));

        Label totalStudentsLabel = new Label();
        totalStudentsLabel.setText("Học sinh: " + totalStudents + " em");
        totalStudentsLabel.setMinSize(190, 41);
        totalStudentsLabel.setFont(new Font(14));

        vboxText.getChildren().addAll(teacherNameLabel, totalStudentsLabel);

        VBox vboxIcon = new VBox();
        AnchorPane editIconContainer = new AnchorPane();
        editIconContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClickEditClass);
        editIconContainer.setMinSize(40, 52);
        editIconContainer.setCursor(Cursor.HAND);
        FontIcon editIcon = new FontIcon();
        editIcon.setIconLiteral("fas-pen");
        editIcon.setLayoutX(10);
        editIcon.setLayoutY(35);
        editIcon.setIconColor(Color.WHITE);
        editIcon.setIconSize(20);
        editIconContainer.getChildren().add(editIcon);

        AnchorPane trashIconContainer = new AnchorPane();
        trashIconContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClickRemoveClass);
        trashIconContainer.setMinSize(40, 52);
        trashIconContainer.setCursor(Cursor.HAND);
        FontIcon trashIcon = new FontIcon();
        trashIcon.setIconLiteral("fas-trash-alt");
        trashIcon.setLayoutX(10);
        trashIcon.setLayoutY(30);
        trashIcon.setIconColor(Color.WHITE);
        trashIcon.setIconSize(20);
        trashIconContainer.getChildren().add(trashIcon);

        vboxIcon.getChildren().addAll(editIconContainer, trashIconContainer);

        hbox.getChildren().addAll(vboxText, vboxIcon);

        vbox.getChildren().addAll(label, hbox);

        tilePane.getChildren().add(vbox);
    }

    // Xu ly event
    public void onMouseClickLoadListLessons(MouseEvent event)
    {
        Label classLabel = (Label) event.getSource();
        String className = classLabel.getText();

        Files file = new Files();
        classId = file.getClassId(className);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageDiaryScene.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene indexScene = new Scene(root, 1280, 720);

            ManageDiaryController manageDiaryController = fxmlLoader.getController();
            manageDiaryController.loadLessons(classId);

            stage.setScene(indexScene);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void onMouseClickDisplayClasses()
    {
        if (!isClicked)
        {
            Files file = new Files();
            ResultSet rs = file.listClasses();

            String teacherName = "";
            int totalStudents = 0;

            try {
                while (rs.next())
                {
                    String className = rs.getString("name");
                    classId = rs.getInt("id");

                    ResultSet classInfoRs = file.getClassInfoByClassId(classId);

                    try {
                        while (classInfoRs.next())
                        {
                            teacherName = classInfoRs.getString("teacher_name");
                            totalStudents = classInfoRs.getInt("total_students");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    displayClass(className, teacherName, totalStudents);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        isClicked = true;
    }
    public void onActionAddClass()
    {
        return;
    }
    public void onMouseClickEditClass(MouseEvent event)
    {
        System.out.println("Clicked!");
    }
    public void onMouseClickRemoveClass(MouseEvent event)
    {
        System.out.println("Removed!");
    }
}
