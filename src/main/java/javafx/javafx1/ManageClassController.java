package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.swing.plaf.synth.SynthUI;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageClassController extends App
{
    private int classId = 0;
    private boolean isClicked = false;
    private AnchorPane contentPane;

    @FXML
    private TilePane tilePane;

    // Xu ly logic
    public void initClassBox(String className, String teacherName, int totalStudents)
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
    public void getContentPane(AnchorPane pane)
    {
        this.contentPane = pane;
    }
    public void displayClasses()
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

                initClassBox(className, teacherName, totalStudents);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Xu ly event
    public void onMouseClickLoadListLessons(MouseEvent event)
    {
        Label classLabel = (Label) event.getSource();
        String className = classLabel.getText();

        System.out.println(className);

        Files file = new Files();
        classId = file.getClassId(className);

        System.out.println(classId);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageDiaryScene.fxml"));
            Parent root = fxmlLoader.load();

            ManageDiaryController manageDiaryController = fxmlLoader.getController();
            manageDiaryController.loadLessons(classId);

            contentPane.getChildren().removeAll();
            contentPane.getChildren().setAll(root);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void onMouseClickDisplayClasses()
    {
        if (!isClicked)
        {
            displayClasses();
        }
        isClicked = true;
    }
    public void onActionAddClass()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddClassScene.fxml"));
            Parent root = loader.load();

            AddClassController addClassController = loader.getController();
            addClassController.setEdit(false);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest((event) -> {
                tilePane.getChildren().clear();
                displayClasses();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void onMouseClickEditClass(MouseEvent event)
    {
        AnchorPane iconContainer = (AnchorPane) event.getSource();
        VBox vboxIcons = (VBox) iconContainer.getParent();
        HBox hbox = (HBox) vboxIcons.getParent();
        VBox vboxParent = (VBox) hbox.getParent();
        Label classLabel = (Label) vboxParent.getChildren().getFirst();

        VBox vboxText = (VBox) hbox.getChildren().getFirst();
        Label teacherLabel = (Label) vboxText.getChildren().getFirst();

        Files files = new Files();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddClassScene.fxml"));
            Parent root = loader.load();

            AddClassController addClassController = loader.getController();
            addClassController.setUpClassForm(classLabel.getText(), teacherLabel.getText());
            addClassController.setEdit(true);
            addClassController.setClassIdGlobal(files.getClassId(classLabel.getText()));

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            stage.setOnCloseRequest((event1) -> {
                tilePane.getChildren().clear();
                displayClasses();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public void onMouseClickRemoveClass(MouseEvent event)
    {
        Files files = new Files();

        AnchorPane iconContainer = (AnchorPane) event.getSource();
        VBox vboxIcons = (VBox) iconContainer.getParent();
        HBox hbox = (HBox) vboxIcons.getParent();
        VBox vboxParent = (VBox) hbox.getParent();
        Label classLabel = (Label) vboxParent.getChildren().getFirst();

        System.out.println(classLabel.getText());

        int classId = files.getClassId(classLabel.getText());

        files.deleteClass(classId);

        tilePane.getChildren().clear();
        displayClasses();
    }
}
