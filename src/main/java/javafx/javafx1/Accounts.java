package javafx.javafx1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Accounts
{
    private Connection connect;

    public Accounts(){
        this.connect = Connector.getConnection();
    }

    public ResultSet login(String email, String password) {
        String sql = "SELECT * FROM accounts WHERE email = '"+email+"' AND password = '"+password+"'";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public ResultSet studentsInfo() {
        String sql = "SELECT accounts.id, accounts.name, accounts.age, "
                    + "accounts.gender, accounts.email, accounts.password, "
                    + "accounts.phone, accounts.address, accounts.parents_name, "
                    + "accounts.parents_phone, accounts.parents_email, accounts.fee, "
                    + "classes.name FROM accounts JOIN classes "
                    + "ON classes.id = accounts.class_id WHERE accounts.role = 4";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    public int getClassId(String className)
    {
        String sql = "SELECT * FROM classes WHERE name = '" +className+"'";
        PreparedStatement ps;
        try {
            ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int classId = 0;
            while (rs.next())
            {
                classId = rs.getInt("id");
            }
            return classId;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ResultSet getAllClass(){
        String sql = "SELECT * FROM classes";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    public void deleteStudentById(int studentId){
        String sql1 = "DELETE FROM comments WHERE student_id = '"+studentId+"'";
        try {
            PreparedStatement ps = connect.prepareStatement(sql1);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
        String sql2 = "DELETE FROM accounts WHERE accounts.id = '"+studentId+"'";
        try {
            PreparedStatement ps = connect.prepareStatement(sql2);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    public void addStudent(String name, int age, String gender, String email, String pass, String phone, String address, String pName, String pPhone, String pEmail, int fee, String className){
        String sql = "INSERT INTO accounts (name, age, gender, email, password, role, phone, address, class_id, parents_name, parents_phone, parents_email, fee)" +
                     " VALUE (?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, email);
            ps.setString(5, pass);
            ps.setInt(6, 4);
            ps.setString(7, phone);
            ps.setString(8, address);
            ps.setInt(9, getClassId(className));
            ps.setString(10, pName);
            ps.setString(11, pPhone);
            ps.setString(12, pEmail);
            ps.setInt(13, fee);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void editStudent(String name, int age, String gender, String email, String pass, String phone, String address, String pName, String pPhone, String pEmail, int fee, String className, int id){
        String sql = "UPDATE accounts " +
                "SET NAME = ?, age = ?, gender = ?, email = ?, PASSWORD = ?, phone = ?, address = ?, parents_name = ?, parents_phone = ?, parents_email = ?, fee = ?, class_id = ? WHERE id = ?";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, email);
            ps.setString(5, pass);
            ps.setString(6, phone);
            ps.setString(7, address);
            ps.setString(8, pName);
            ps.setString(9, pPhone);
            ps.setString(10, pEmail);
            ps.setInt(11, fee);
            ps.setInt(12, getClassId(className));
            ps.setInt(13, id);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Get Teachers info
    public ResultSet getTeachersInfo() {
        String sql = "SELECT * FROM accounts WHERE role = 2";
        try {
            PreparedStatement ps = connect.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
