package common;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity to demonstrate a general user within the system: customer, subscriber, 
 * manager and every other user that required to use the system 
 */

public class User implements Serializable {

    private String username;
    private String  password;
    private String first_name;
    private String last_name;
    private Roles role;
    private String user_id;
    private String phoneNumber;
    private String emailAddress;
    private boolean isLoggedIn=false;

    public User(String username, String password, String first_name, String last_name, Roles role, String user_id,
                String phoneNumber, String emailAddress) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.user_id = user_id;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }
    public User(String username, String password, String first_name, String last_name, Roles role, String user_id,
                String phoneNumber, String emailAddress,Integer is_loggin) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.user_id = user_id;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        if (is_loggin==1)
            isLoggedIn = true;
    }
    public User(User user){
        this.username = user.getUsername();
        this.password =  user.getPassword();
        this.first_name =  user.getFirst_name();
        this.last_name =  user.getLast_name();
        this.role =  user.getRole();
        this.user_id =  user.getUser_id();
        this.phoneNumber =  user.getPhoneNumber();
        this.emailAddress =  user.getEmailAddress();
    }

    public User() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return first_name.equals(user.first_name) && last_name.equals(user.last_name) && user_id.equals(user.user_id) && password.equals(user.password) && username.equals(user.username) && role == user.role && phoneNumber.equals(user.phoneNumber) && emailAddress.equals(user.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first_name, last_name, user_id, password, username, role, phoneNumber, emailAddress);
    }


}
