package javafx.javafx1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;

import javafx.scene.control.cell.PropertyValueFactory;

public class ManageDiaryController extends App
{
    private String title = "";
    private String content = "";
    private String teacherName = "";
    private String className = "";
    private String commentStudentName = "";
    private String commentStudentComment = "";
    public Files uploadFile = new Files();
    private boolean isConfirm = false;
    private int countGlobal = 0;
    private int classIdGlobal = 0;

    @FXML
    private TableView<Comment> tblComment1;
    @FXML
    private TableColumn<Comment, String> tblCol1Title;
    @FXML
    private TableColumn<Comment, String> tblCol1Content;
    @FXML
    private TableColumn<Comment, String> tblCol1Class;
    @FXML
    private TableColumn<Comment, String> tblCol1Teacher;

    @FXML
    private TableView<Comment> tblComment2;
    @FXML
    private TableColumn<Comment, String> tblCol2StudentName;
    @FXML
    private TableColumn<Comment, String> tblCol2StudentComment;

    @FXML
    private VBox lessonsContainer;

    @FXML
    private Button btnBack;

    // Xu ly logic
    public void loadTable1Comment(int lessonId)
    {
        Files files = new Files();

        ResultSet classInfo = files.getClassInfo(lessonId);
        try {
            while (classInfo.next())
            {
                title = classInfo.getString("title");
                content = classInfo.getString("content");
                className = classInfo.getString("class_name");
                teacherName = classInfo.getString("teacher_name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        final ObservableList<Comment> data =
            FXCollections.observableArrayList(
                    new Comment(title, content, className, teacherName)
            );

        tblCol1Title.setCellValueFactory(new PropertyValueFactory<>("commentTitle"));
        tblCol1Content.setCellValueFactory(new PropertyValueFactory<>("commentContent"));
        tblCol1Class.setCellValueFactory(new PropertyValueFactory<>("commentClassName"));
        tblCol1Teacher.setCellValueFactory(new PropertyValueFactory<>("commentTeacherName"));

        tblComment1.setItems(data);
    }
    public void loadTable2Comment(int lessonId)
    {
        final ObservableList<Comment> data = FXCollections.observableArrayList();

        Files files = new Files();

        ResultSet classComments = files.getClassComments(lessonId);
        try {
            while (classComments.next())
            {
                commentStudentName = classComments.getString("student_name");
                commentStudentComment = classComments.getString("comment");

                data.add(new Comment(commentStudentName, commentStudentComment));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tblCol2StudentName.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentStudentName"));
        tblCol2StudentComment.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentStudentComment"));

        tblComment2.setItems(data);
    }
    public List<String> readExcelInfo(File file)
    {
        List<String> list = new ArrayList<>();
        try
        {
            FileInputStream fs = new FileInputStream(file.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow titleRow = sheet.getRow(0);
            XSSFRow contentRow = sheet.getRow(1);
            XSSFRow classRow = sheet.getRow(2);

            XSSFCell titleCell = titleRow.getCell(1);
            XSSFCell contentCell = contentRow.getCell(1);
            XSSFCell classCell = classRow.getCell(1);

            String titleValue = titleCell.getStringCellValue();
            String contentValue = contentCell.getStringCellValue();
            String classValue = classCell.getStringCellValue();

            list.add(titleValue);
            list.add(contentValue);
            list.add(classValue);

            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<List<String>> readExcelComment(File file)
    {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            FileInputStream fs = new FileInputStream(file.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fs);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getLastRowNum();
            for (int i = 4; i <= rows; i++)
            {
                List<String> list = new ArrayList<>();

                XSSFRow studentCommentRow = sheet.getRow(i);
                XSSFCell studentIdCell = studentCommentRow.getCell(0);
                XSSFCell studentNameCell = studentCommentRow.getCell(1);
                XSSFCell studentCommentCell = studentCommentRow.getCell(2);

                int tmp = (int) studentIdCell.getNumericCellValue();
                String studentIdValue = Integer.toString(tmp);
                String studentNameValue = studentNameCell.getStringCellValue();
                String studentCommentValue = studentCommentCell.getStringCellValue();

                list.add(studentIdValue);
                list.add(studentNameValue);
                list.add(studentCommentValue);

                arrayList.add(list);
            }
            return arrayList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public File uploadFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Excel file to upload");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel file", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(stage);
        return file;
    }
    public boolean checkDuplicate(List<String> listInfo)
    {
        Files file = new Files();
        ResultSet rs = file.listLessons();

        String tmpLessonTitle = listInfo.get(0);
        int tmpClassId = file.getClassId(listInfo.get(2));

        boolean isDuplicated = false;

        try {
            while (rs.next())
            {
                String lessonTitle = rs.getString("title");
                int classId = rs.getInt("class_id");

                if (tmpLessonTitle.equals(lessonTitle) && tmpClassId == classId)
                {
                    isDuplicated = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isDuplicated;
    }
    public void addLessonBtn(int count)
    {
        String lessonName = "Lesson";

        Button lessonBtn = new Button(lessonName + " " + count);
        lessonBtn.setId("lessonBtnTest");
        lessonBtn.setMinSize(260, 55);
        lessonBtn.setPrefSize(200, 55);
        lessonBtn.setCursor(Cursor.HAND);
        lessonBtn.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 20");
        lessonBtn.setTextFill(Color.BLACK);

        lessonBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClickGetTblCommentsByLessonName);

        lessonsContainer.getChildren().add(lessonBtn);
    }
    public int displayLessons(int classId)
    {
        Files file = new Files();
        ResultSet rs = file.listLessonsByClassId(classId);
        int count = 0;

        try {
            while (rs.next())
            {
                count += 1 ;
                addLessonBtn(count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return count;
    }
    public void resetAllBtn(int countGlobal)
    {
        int tmpCount = countGlobal - 1;
        for (int i = 0; i < tmpCount; i++)
        {
            Button btn = (Button)lessonsContainer.getChildren().get(i);

            btn.setTextFill(Color.BLACK);
            btn.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 20");
        }

        if (!lessonsContainer.getChildren().isEmpty())
        {
            Button btn = (Button) lessonsContainer.getChildren().getLast();

            btn.setTextFill(Color.BLACK);
            btn.setStyle("-fx-background-color: #D9D9D9; -fx-font-size: 20");
        }
    }
    public void loadLessons(int classId)
    {
        this.classIdGlobal = classId;
        countGlobal = displayLessons(classId) + 1;
        resetAllBtn(countGlobal);
    }
    public void reloadListLessons()
    {
        lessonsContainer.getChildren().clear();
    }

    // Xu ly event
    public void onActionBtnUploadFile()
    {
        File file = uploadFile();
        List<String> listExcelContent = readExcelInfo(file);

        String titleValue = listExcelContent.get(0);
        String contentValue = listExcelContent.get(1);
        String classValue = listExcelContent.get(2);

        ArrayList<List<String>> listExcelComment = readExcelComment(file);

        int classId = uploadFile.getClassId(classValue);
        String fileName = file.getName();
        Stage stage = new Stage();
        FXMLLoader loader;
        ConfirmUploadController confirmUploadController;
        try {
            loader = new FXMLLoader(getClass().getResource("ConfirmUploadScene.fxml"));
            Parent root = loader.load();

            confirmUploadController = loader.getController();
            confirmUploadController.parseFileName(fileName);
            confirmUploadController.parseCountGlobal(countGlobal);
            confirmUploadController.parseLessonsContainerGlobal(lessonsContainer);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // check file bi trung
        if (!checkDuplicate(listExcelContent))
        {
            stage.show();
            stage.setOnCloseRequest((event) -> {
                isConfirm = confirmUploadController.getConfirm();
                System.out.println(isConfirm);
                if (isConfirm)
                {
                    uploadFile.storeExcelInfo(classId, titleValue, contentValue);

                    for (List<String> item : listExcelComment)
                    {
                        int studentId = Integer.parseInt(item.get(0));
                        String studentComment = item.get(2);

                        uploadFile.storeExcelComment(studentId, titleValue, studentComment);
                    }

                    addLessonBtn(countGlobal);
                    reloadListLessons();
                    displayLessons(classIdGlobal);
                    loadTable1Comment(uploadFile.findLessonByTitle(titleValue));
                    loadTable2Comment(uploadFile.findLessonByTitle(titleValue));
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("File bi trung!");
            alert.show();
        }
    }
    public void onMouseClickGetTblCommentsByLessonName(MouseEvent event)
    {
        Files file = new Files();

        Button btn = (Button)event.getSource();
        String text = btn.getText();

        int btnLessonId = Integer.parseInt(text.substring(7));

        List<Integer> listLessonId = new ArrayList<>();

        ResultSet rs = file.listLessonsByClassId(classIdGlobal);

        try {
            while (rs.next())
            {
                int lessonId = rs.getInt("id");
                listLessonId.add(lessonId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i <= listLessonId.size(); i++)
        {
            if (i == (btnLessonId - 1))
            {
                loadTable1Comment(listLessonId.get(i));
                loadTable2Comment(listLessonId.get(i));
            }
        }
        resetAllBtn(countGlobal);

        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #F05454; -fx-font-size: 20");
    }
}