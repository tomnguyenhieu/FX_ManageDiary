/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafx.javafx1;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javax.swing.UIManager;

/**
 *
 * @author Admin
 */
public class DBEarningController implements Initializable {
  public Integer totalEarningByYear = 0;
  public Integer avgMonthlyEarning;
  public Integer yearSelected;
  public Integer totalInterset;
  public Integer increase;
  
  @FXML
  private ComboBox<Integer> selectYearCB;
  @FXML
  private BarChart<String, Integer> earningByYearChart;
  @FXML
  private Label totalEarningLb;
  @FXML
  private Label avgEarningLb;
  @FXML
  private Label totalInterestLb;
  @FXML
  private Label increaseLb;
  @FXML
  private CategoryAxis xAxis;
  @FXML
  private NumberAxis yAxis;
  
  public void initYearsCB() {
    Files file = new Files();
    ResultSet rs = file.getYears(4);
    try {
      while (rs.next()) {
        String _year = rs.getString("year");
        int year = Integer.parseInt(_year);
        selectYearCB.getItems().add(year);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void onActionSelectYear() {
    yearSelected = selectYearCB.getValue();
    Files file = new Files();
    ResultSet rs = file.getEarningByYear(yearSelected, 4);
    renderDataByYear(rs, yearSelected);
//    updateTotalEarning(rs);
  }
  
  public void renderDataByYear (ResultSet rs, int yearSelected) {
    yAxis.setLabel("Tổng thu");
    
    XYChart.Series<String, Integer> dataSeries = new XYChart.Series<>();
    
    int countMonth = 0;
    try {
      while (rs.next()) {
        String _year = rs.getString("year");
        int year = Integer.parseInt(_year);
        
        int yearlyEarning = rs.getInt("yearlyEarning");
        String month = rs.getString("month");
        int monthlyEarning = rs.getInt("monthlyEarning");
        
        if (year == yearSelected) {
          countMonth++;
          totalEarningByYear = yearlyEarning;
          avgMonthlyEarning = totalEarningByYear/countMonth;
        }
        dataSeries.getData().add(new XYChart.Data<>(month, monthlyEarning));
        
        System.out.println(month + " " + totalEarningByYear);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    earningByYearChart.getData().clear();
    earningByYearChart.getData().add(dataSeries);
    
    String _totalEarning = totalEarningByYear.toString();
    String _avgMonthlyEarning = avgMonthlyEarning.toString();
    totalEarningLb.setText(_totalEarning);
    avgEarningLb.setText(_avgMonthlyEarning + "/tháng");
    
    setInterestLabel(_totalEarning);
  }
  
  public void setInterestLabel (String _totalEarning) {
    int totalEarning = Integer.parseInt(_totalEarning);
    
    Files file = new Files();
    ResultSet rs = file.getTotalSpending(yearSelected);
    try {
      while (rs.next()) {
        int totalSpending = rs.getInt("total_spending");
        totalInterset = totalEarning - totalSpending;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    String interest = totalInterset.toString();
    totalInterestLb.setText(interest);
    
    setIncreaseLabel(interest);
  }
  
  public void setIncreaseLabel (String interest) {
    int yearSelectedInterest = Integer.parseInt(interest);
    Files file = new Files();
    ResultSet rs1 = file.getEarningByYear(yearSelected - 1, 4);
    ResultSet rs2 = file.getTotalSpending(yearSelected);
    
    int lastYearInterest;
    int lastYearEarning = 0;
    int lastYearSpending = 0;
    
    try {
      while (rs1.next()) {
        lastYearEarning = rs1.getInt("yearlyEarning");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    try {
      while (rs2.next()) {
        lastYearSpending = rs2.getInt("total_spending");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    lastYearInterest = lastYearEarning - lastYearSpending;
    increase = yearSelectedInterest - lastYearInterest;
    String _increase = increase.toString();
    increaseLb.setText(_increase);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initYearsCB();
  }
  
}
