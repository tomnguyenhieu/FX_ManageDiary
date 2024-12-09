package javafx.javafx1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.ColorConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.cell.PropertyValueFactory;

public class ManageDiaryController extends App
{
    private String title = "";
    private String content = "";
    private String teacherName = "";
    private String className = "";
    private String commentStudentName = "";
    private String commentStudentComment = "";

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
    public void addLessonBtn()
    {
        Label lessonLabel = new Label("Lesson 1");
        lessonLabel.setMinSize(250, 55);
        lessonLabel.setPrefSize(250, 55);
//                lessonLabel.setStyle("-fx-background-color: #000000;");
        lessonLabel.setAlignment(Pos.CENTER);
        lessonLabel.setTextFill(Color.BLACK);
        lessonLabel.setFont(new Font(22));

        HBox lessonBody = new HBox(lessonLabel);
        lessonBody.setMinSize(200, 55);
        lessonBody.setPrefSize(200, 55);
        lessonBody.setStyle("-fx-background-color: #D9D9D9;");

        lessonsContainer.getChildren().add(lessonBody);
    }
    public void loadLessons(int classId)
    {
        Files file = new Files();
        ResultSet rs = file.listLessonsByClassId(classId);

//        List<Integer> listLessonsId = new ArrayList<>();

        try {
            while (rs.next())
            {
//                int lessonId = rs.getInt("id");
//                listLessonsId.add(lessonId);

                addLessonBtn();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

//        for (List<String> list : listExcelComment)
//        {
//            System.out.println(list);
//        }

        Files uploadFile = new Files();
        int classId = uploadFile.getClassId(classValue);

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
//                System.out.println(studentId + " " + studentComment);
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Upload thanh cong!");
            alert.show();

            addLessonBtn();

            loadTable1Comment(uploadFile.findLessonByTitle(titleValue));
            loadTable2Comment(uploadFile.findLessonByTitle(titleValue));

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("File bi trung!");
            alert.show();
        }
    }
    public void onMouseClickLoadTblComments(MouseEvent event)
    {
        int lessonId = 67;
        loadTable1Comment(lessonId);
        loadTable2Comment(lessonId);
    }
    public void onMouseClickLoadListLessons(MouseEvent event)
    {
        int classId = 3;
        loadLessons(classId);
    }
}