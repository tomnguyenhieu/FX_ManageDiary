package javafx.javafx1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DBChiController extends App implements Initializable
{
    private LocalDate today = LocalDate.now();
    private Integer currentMonth = today.getMonthValue();
    private Integer currentYear = today.getYear();
    private String time = currentMonth.toString() + "/" + currentYear.toString();
    private int year = 0;

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private Label totalTeachersSalary;
    @FXML
    private Label totalStaffsSalary;
    @FXML
    private Label totalCSVC;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis2;
    @FXML
    private NumberAxis yAxis2;

    // Xu ly logic
    public void renderLineChart()
    {
        Files files = new Files();
        yAxis.setLabel("Tổng lương");

        // Giáo viên
        XYChart.Series<String, Number> dataSeriesTeachers = new XYChart.Series<>();
        ResultSet rs = files.getTotalSalary(1);
        try {
            while (rs.next())
            {
                dataSeriesTeachers.getData().add(new XYChart.Data<>(rs.getString("month"), rs.getInt("total_salary")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        // Nhân viên
        XYChart.Series<String, Number> dataSeriesStaffs = new XYChart.Series<>();
        ResultSet rs1 = files.getTotalSalary(2);
        try {
            while (rs1.next())
            {
                dataSeriesStaffs.getData().add(new XYChart.Data<>(rs1.getString("month"), rs1.getInt("total_salary")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dataSeriesTeachers.setName("Giáo viên");
        dataSeriesStaffs.setName("Nhân viên");

        lineChart.getData().add(dataSeriesTeachers);
        lineChart.getData().add(dataSeriesStaffs);

    }
    public void updateTeachersSalary()
    {
        Files files = new Files();
        ResultSet rs = files.getTotalSalary(1);
        try {
            while (rs.next())
            {
                String month = rs.getString("month");
                if (month.equals(time))
                {
                    Integer _totalTeachersSalary = rs.getInt("total_salary");
                    String strTotalTeachersSalary = _totalTeachersSalary.toString();
                    totalTeachersSalary.setText(strTotalTeachersSalary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateStaffsSalary()
    {
        Files files = new Files();
        ResultSet rs = files.getTotalSalary(2);
        try {
            while (rs.next())
            {
                String month = rs.getString("month");
                if (month.equals(time))
                {
                    Integer _totalStaffsSalary = rs.getInt("total_salary");
                    String strTotalStaffsSalary = _totalStaffsSalary.toString();
                    totalStaffsSalary.setText(strTotalStaffsSalary);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void updateCSVC()
    {
        Files files = new Files();
        ResultSet rs = files.getTotalCSVC();
        try {
            while (rs.next())
            {
                String month = rs.getString("month");
                if (month.equals(time))
                {
                    Integer _totalCSVC = rs.getInt("total_quantity");
                    String strTotalCSVC = _totalCSVC.toString();

                    System.out.println(strTotalCSVC);
                    totalCSVC.setText(strTotalCSVC);
                }
            }
        } catch (SQLException e) {
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
    public void renderBarChart()
    {
        Files files = new Files();
        yAxis2.setLabel("Số lượng");

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        ResultSet rs = files.getTotalCSVC();
        try {
            while (rs.next())
            {
                dataSeries.getData().add(new XYChart.Data<>(rs.getString("month"), rs.getInt("total_quantity")));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dataSeries.setName("Số lượng");
        barChart.getData().clear();
        barChart.getData().add(dataSeries);
    }
    public ArrayList<List<String>> readExcel(File file)
    {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            FileInputStream fs = new FileInputStream(file.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fs);

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getLastRowNum();
            for (int i = 1; i <= rows; i++)
            {
                List<String> list = new ArrayList<>();

                XSSFRow csvcRow = sheet.getRow(i);
                XSSFCell nameCell = csvcRow.getCell(1);
                XSSFCell quantityCell = csvcRow.getCell(2);
                XSSFCell priceCell = csvcRow.getCell(3);
                XSSFCell totalPriceCell = csvcRow.getCell(4);
                XSSFCell timeCell = csvcRow.getCell(5);

                String nameCellValue = nameCell.getStringCellValue();
                double quantityCellValue = quantityCell.getNumericCellValue();
                Integer _quantityCellValue = (int) quantityCellValue;
                String __quantityCellValue = _quantityCellValue.toString();

                double priceCellValue = priceCell.getNumericCellValue();
                Integer _priceCellValue = (int) priceCellValue;
                String __priceCellValue = _priceCellValue.toString();

                double totalPriceCellValue = totalPriceCell.getNumericCellValue();
                Integer _totalPriceCellValue = (int) totalPriceCellValue;
                String __totalPriceCellValue = _totalPriceCellValue.toString();

                String timeCellValue = timeCell.getStringCellValue();

                list.add(nameCellValue);
                list.add(__quantityCellValue);
                list.add(__priceCellValue);
                list.add(__totalPriceCellValue);
                list.add(timeCellValue);

                arrayList.add(list);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return arrayList;
    }

    // Xu ly event
    public void onActionUploadBtn()
    {
        boolean isSuccess = false;
        File excel = uploadFile();
        Files files = new Files();
        ArrayList<List<String>> arrayList = readExcel(excel);
        for (List<String> item : arrayList)
        {
            String name = item.getFirst();
            String quantity = item.get(1);
            int _quantity = Integer.parseInt(quantity);
            String price = item.get(2);
            int _price = Integer.parseInt(price);
            String totalPrice = item.get(3);
            int _totalPrice = Integer.parseInt(totalPrice);
            String month = item.getLast().substring(3);

            if (files.storeBill(name, _quantity, _price, _totalPrice, month))
            {
                isSuccess = true;
                updateCSVC();
                renderBarChart();
            }
        }
        if (isSuccess)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Upload thanh cong!");
            alert.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderLineChart();
        updateTeachersSalary();
        updateStaffsSalary();
        updateCSVC();
        renderBarChart();
    }
}
