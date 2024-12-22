package javafx.javafx1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.etsi.uri.x01903.v13.IntegerListType;

import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExportExcelController extends ManageClassController implements Initializable
{
    @FXML
    private ComboBox<String> cbClasses;

    // Xu ly logic
    public void initComboBox()
    {
        Files files = new Files();
        ResultSet rs = files.getClasses();
        try {
            while (rs.next())
            {
                cbClasses.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean exportExcel(ArrayList<List<String>> arrayList, String className)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel file", "*.xlsx")
        );
        File exportFile = fileChooser.showSaveDialog(stage);
        String filePath = exportFile.getPath();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sheet1");

            XSSFRow titleRow = sheet.createRow(0);
            XSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Thời gian");

            XSSFRow contentRow = sheet.createRow(1);
            XSSFCell contentCell = contentRow.createCell(0);
            contentCell.setCellValue("Nội dung bài học");

            XSSFRow classRow = sheet.createRow(2);
            XSSFCell classCell = classRow.createCell(0);
            classCell.setCellValue("Lớp");
            XSSFCell classCellValue = classRow.createCell(1);
            classCellValue.setCellValue(className);

            XSSFRow labelRow = sheet.createRow(3);
            XSSFCell labelID = labelRow.createCell(0);
            labelID.setCellValue("ID");
            XSSFCell labelName = labelRow.createCell(1);
            labelName.setCellValue("Tên");
            XSSFCell labelComment = labelRow.createCell(2);
            labelComment.setCellValue("Nhận xét");
            XSSFCell labelScore = labelRow.createCell(3);
            labelScore.setCellValue("Điểm số");

            for (int i = 0; i < arrayList.size(); i++)
            {
                XSSFRow row = sheet.createRow(i + 4);

                List<String> rowData = arrayList.get(i);
                for (int j = 0; j < rowData.size(); j++)
                {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellValue(rowData.get(j));
                }
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Xu ly event
    public void close(ActionEvent event){
        // phat event dong window
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    public void onActionConfirm(ActionEvent event)
    {
        String className = cbClasses.getValue();

        Files files = new Files();
        int classId = files.getClassId(className);
        ResultSet rs = files.getStudentsByClassName(classId);

        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            while (rs.next())
            {
                List<String> list = new ArrayList<>();
                Integer studentId = rs.getInt("id");
                String _studentId = studentId.toString();
                String studentName = rs.getString("name");

                list.add(_studentId);
                list.add(studentName);

                arrayList.add(list);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean isExported = false;
        isExported = exportExcel(arrayList, className);

        if (isExported)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Export thanh cong!");
            alert.show();
        }

        close(event);
    }
    public void onActionCancel(ActionEvent event)
    {
        close(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBox();
    }
}
