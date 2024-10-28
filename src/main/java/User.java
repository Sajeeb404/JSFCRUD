
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@ManagedBean
@RequestScoped
public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String address;

    private ArrayList<User> userList;
   	private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;


    public Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jsf", "root","root");

        } catch (Exception e) {
            e.printStackTrace();

        }
    return connection;

    }


    //Used to save user record.
    public String save(){
        int result = 0;

        try {
            connection = getConnection();
            String insertSQL = "insert into user (name, email, password, gender, address) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, address);
            result = preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        if (result != 0) {
            return "index.xhtml?faces-redirect=true";
        }else {
            return "create.xhtml?faces-redirect=true";
        }


    }



//Used to fetch all records

    public ArrayList userList(){

        try {
            userList = new ArrayList<>();
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from user");

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                user.setGender(resultSet.getString(5));
                user.setAddress(resultSet.getString(6));
                userList.add(user);

            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

return userList;
    }


    // used to delete user record

    public void delete(int id){

        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("delete from user where id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();

        }


    }



   //used to fetch record by id to update

   public String edit(int id){
        User user = null;

       System.out.println(id);

       try {
           connection =getConnection();
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery("select * from user where id ="+id);
           if (resultSet.next()) {
               user = new User();
               user.setId(resultSet.getInt(1));
               user.setName(resultSet.getString(2));
               user.setEmail(resultSet.getString(3));
               user.setPassword(resultSet.getString(4));
               System.out.println(resultSet.getString("password"));
               user.setGender(resultSet.getString(5));
               user.setAddress(resultSet.getString(6));

               sessionMap.put("editUser", user);

           }


       } catch (Exception e) {
           e.printStackTrace();

       }
    return "/edit.xhtml?faces-redirect=true";

   }











    // Constructor to initialize userList
    public User() {
        userList = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    // Method to add a user to the list
    public String addUser() {
        userList.add(new User(id, name, email, password, gender, address));
        // Clear fields after adding (optional)
        clearFields();
        return "index"; // Navigation outcome, change as necessary
    }

    private void clearFields() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.gender = "";
        this.address = "";
    }

    // Constructor for adding to list
    public User(int id, String name, String email, String password, String gender, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.address = address;
    }




}
