package javafx.javafx1;

import javafx.beans.property.SimpleStringProperty;

public class Person {

    private int id;
    private SimpleStringProperty name;
    private int age;
    private SimpleStringProperty gender;
    private SimpleStringProperty email;
    private SimpleStringProperty password;
    private SimpleStringProperty phone;
    private SimpleStringProperty address;
    private SimpleStringProperty pName;
    private SimpleStringProperty pPhone;
    private SimpleStringProperty pEmail;
    private int fee;
    private SimpleStringProperty className;
    private SimpleStringProperty status;
    private SimpleStringProperty certificates;
    private int salary;

    public Person(int id, String name)
    {
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public Person(int id, String name, int age, String gender, String email, String password, String phone, String address, String pName, String pPhone, String pEmail, int fee, String className, String status) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.age = age;
        this.gender = new SimpleStringProperty(gender);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.phone = new SimpleStringProperty(phone);
        this.address = new SimpleStringProperty(address);
        this.pName = new SimpleStringProperty(pName);
        this.pPhone = new SimpleStringProperty(pPhone);
        this.pEmail = new SimpleStringProperty(pEmail);
        this.fee = fee;
        this.className = new SimpleStringProperty(className);
        this.status = new SimpleStringProperty(status);
    }
    public Person(int id, String name, int age, String gender, String email, String password, String phone, String address, String certificates, int salary, String status) {
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
        this.status = new SimpleStringProperty(status);
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
    public String getPName() {
        return pName.get();
    }
    public String getPPhone() {
        return pPhone.get();
    }
    public String getPEmail() {
        return pEmail.get();
    }
    public int getFee() {
        return fee;
    }
    public String getClassName() {
        return className.get();
    }
    public String getStatus() {
        return status.get();
    }
    public String getCertificate() {
        return certificates.get();
    }
    public int getSalary() {
        return salary;
    }

}
