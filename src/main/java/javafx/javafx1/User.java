package javafx.javafx1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User
{
    private Connection connect;

    public User(){
        this.connect = Connector.getConnection();
    }

    public ResultSet login(String email, String password) {
        String sql = "SELECT * FROM accounts WHERE email = '" + email + "' AND password = '" + password + "'";
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
