package javafx.javafx1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.ColorConverter;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.TextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.control.cell.PropertyValueFactory;

public class ManageDiaryController extends App
{
    private String title = "";
    private String content = "";
    private String teacherName = "";
    private String className = "";
    private String commentStudentName = "";
    private String commentStudentComment = "";

    private int countGlobal = 0;

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

        tblCol1Title.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentTitle"));
        tblCol1Content.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentContent"));
        tblCol1Class.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentClassName"));
        tblCol1Teacher.setCellValueFactory(new PropertyValueFactory<Comment, String>("commentTeacherName"));

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
//
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
//        lessonBtn.setTextFill(Color.BLACK);
        lessonBtn.setId("lessonBtnTest");
        lessonBtn.setMinSize(260, 55);
        lessonBtn.setPrefSize(200, 55);
        lessonBtn.setStyle("-fx-background-color: #D9D9D9");
        lessonBtn.setStyle("-fx-font-size: 20");

        lessonBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onMouseClickGetTblCommentsByLessonName);

        lessonsContainer.getChildren().add(lessonBtn);
    }
    public int loadLessons(int classId)
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
            btn.setStyle("-fx-background-color: #D9D9D9");
            btn.setStyle("-fx-font-size: 20");
        }
    }

    // Xu ly event
    public void onActionBtnUploadFile(ActionEvent event)
    {
        File file = uploadFile();
        List<String> listExcelContent = readExcelInfo(file);

        String titleValue = listExcelContent.get(0);
        String contentValue = listExcelContent.get(1);
        String classValue = listExcelContent.get(2);

        ArrayList<List<String>> listExcelComment = readExcelComment(file);

        Files uploadFile = new Files();
        int classId = uploadFile.getClassId(classValue);

        addLessonBtn(countGlobal);

        if (!checkDuplicate(listExcelContent))
        {
            boolean isUploaded = uploadFile.storeExcelInfo(classId, titleValue, contentValue);
            if (!isUploaded)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Upload that bai!");
                alert.show();
                return;
            }

            for (List<String> item : listExcelComment)
            {
                int studentId = Integer.parseInt(item.get(0));
                String studentComment = item.get(2);

                uploadFile.storeExcelComment(studentId, titleValue, studentComment);
            }

            countGlobal += 1;

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Upload thanh cong!");
            alert.show();

            Button btn = (Button)lessonsContainer.getChildren().getLast();

            resetAllBtn(countGlobal);

//            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #F05454");
            btn.setStyle("-fx-font-size: 20");

            loadTable1Comment(uploadFile.findLessonByTitle(titleValue));
            loadTable2Comment(uploadFile.findLessonByTitle(titleValue));

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("File bi trung!");
            alert.show();
        }
    }
    public void onMouseClickLoadListLessons(MouseEvent event)
    {
        int classId = 3;
        countGlobal = loadLessons(classId) + 1;

        resetAllBtn(countGlobal);
    }
    public void onMouseClickGetTblCommentsByLessonName(MouseEvent event)
    {
        Files file = new Files();

        Button btn = (Button)event.getSource();
        String text = btn.getText();

        int btnLessonId = Integer.parseInt(text.substring(7));

        List<Integer> listLessonId = new ArrayList<>();
        ResultSet rs = file.listLessonsByClassId(3);

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

//        btn.setTextFill(Color.WHITE);
        btn.setStyle("-fx-background-color: #F05454");
        btn.setStyle("-fx-font-size: 20");

//        System.out.println(countGlobal);
    }
}