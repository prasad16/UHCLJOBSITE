/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.*;
/**
 *
 * @author Priti
 */
@ManagedBean
@RequestScoped
public class Register 
{
    private String id;
    private String name;
    private String password;
    private String type;

    boolean hasSpecialCharacter;
    boolean hasOneLetter;
    boolean hasOneDigit;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String registerUser()
    {
        hasSpecialCharacter = false;
        hasOneDigit = false;
        hasOneLetter = false;
        
        //load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            //error message
            return ("Internal Error! Please try again later.");
        }
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            connection = DriverManager.getConnection(DATABASE_URL, "shahp1511", "1357780");  
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from useraccount where "
                    + " UserID = '" + id + "'");
            
            //check if id already exist
            if(resultSet.next())
            {
                return "The User ID has been taken. Please try another one!";
            }
            else
            {
                isValidAccountID(id);
                
                if(hasSpecialCharacter == true)
                {
                    return "Your ID can not contain special characters";
                }
                else if(hasOneLetter == false)
                {
                    return "Your ID needs to contain at least one letter and at least one digit";
                }
                else if(hasOneDigit == false)
                {
                    return "Your ID needs to contain at least one letter and at least one digit";
                }
                else
                {
                    //insert new user into useraccount table
                    int i = statement.executeUpdate("insert into useraccount "
                            + "values ('" + id + "','" + name + "','" + password +
                            "','" + type + "')");

                    return ("Registration Successful! Please return to login your"
                            + " account.");                    
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return ("Internal Error! Please try again later.");            
        }
        finally
        {
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
                
            }
            catch (Exception e)
            {
                 
                e.printStackTrace();
            }
        }        
    }
    
    private void isValidAccountID(String userID)
    {
        //boolean isLetterOrDigit = true;            

        for(int i=0; i< userID.length(); i++)
        {
            if(!Character.isLetter(userID.charAt(i)) && !Character.isDigit(userID.charAt(i)))
            {
                hasSpecialCharacter = true;
                break;
            }
            if(Character.isLetter(userID.charAt(i)))
            {
                hasOneLetter = true;
            }
            if(Character.isDigit(userID.charAt(i)))
            {
                hasOneDigit = true;
            }
        }        
    }    

    
}
