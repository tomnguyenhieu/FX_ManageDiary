package javafx.javafx1;

import java.sql.Connection;
import javafx.beans.property.SimpleStringProperty;

public class Teacher {

    private Connection connect;

    private int id;
    private SimpleStringProperty name;
    private int age;
    private SimpleStringProperty gender;
    private SimpleStringProperty email;
    private SimpleStringProperty password;
    private SimpleStringProperty phone;
    private SimpleStringProperty address;
    private SimpleStringProperty certificates;
    private int salary;
    private int status;

    public Teacher(int id, String name, int age, String gender, String email, String password, String phone, String address, String certificates, int salary, int status) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.age = age;
        this.gender = new SimpleStringProperty(gender);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.certificates = new SimpleStringProperty(certificates);
        this.salary = salary;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name.get();
    }
    public int getAge() {
        return age;
    }
    public String getGender() {
        return gender.get();
    }
    public String getEmail() {
        return email.get();
    }
    public String getPassword() {
        return password.get();
    }
    public String getPhone() {
        return phone.get();
    }
    public String getAddress() {
        return address.get();
    }
    public String getCertificate() {
        return certificates.get();
    }
    public int getSalary() {
        return salary;
    }
    public int getStatus() {
        return status;
    }
}
