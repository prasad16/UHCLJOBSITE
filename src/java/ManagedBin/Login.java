/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.*;
import java.util.Scanner;
import java.io.Serializable;
/**
 *
 * @author Priti
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable
{
    private String id;
    private String password;
    private UserAccount theUserAccount;
    private UserAccount theHrUserAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserAccount getTheUserAccount() {
        return theUserAccount;
    }

    public UserAccount getTheHrUserAccount() {
        return theHrUserAccount;
    }
   
    
    
    public String login()
    {
        //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            return ("Internal Error! Please try again later.");
        }
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
        
        Connection connection = null;
        Statement statement = null;  
        ResultSet resultSet = null;
        
        try
        {
            connection = DriverManager.getConnection(DATABASE_URL, "shahp1511", "1357780");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from useraccount "
                    + " where UserID = '" + id + "'");
            
            if(resultSet.next())
            {
                if(password.equals(resultSet.getString(3)))
                {
                    //check type of user
                    if((resultSet.getString(4)).equals("User"))
                    {
                        theUserAccount = new UserAccount(id, password, resultSet.getString(2),resultSet.getString(4));
                        return "home";
                        
                    }
                    else
                    {
                        theHrUserAccount = new UserAccount(id, password, resultSet.getString(2),resultSet.getString(4));
                        return "homeHR";
                    }
                    
                }
                else
                {
                    id = "";
                    password = "";
                    return "loginNotOk";
                }
            }
            else
            {
                id = "";
                password = "";
                return "loginNotOk";
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "Internal Error....";
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
}
