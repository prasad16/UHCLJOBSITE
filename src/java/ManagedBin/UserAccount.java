/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBin;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.sql.*;
import java.util.ArrayList;
import java.text.*;
import java.util.List;

/**
 *
 * @author Priti
 */

public class UserAccount 
{
    private String id;
    private String password;
    private String name;
    private String type;
    
    private ArrayList<JobDetails> allJobDetails;
    private ArrayList<String> appliedJobsID;
    private ArrayList<JobDetails> jobDetailsForHr;
    private ArrayList<ApplicantDetails> jobApplicantDetails;
    private ArrayList<String> allStatus;
    
    private int selectedJobID;
    private String selectedJobTitle;
    private String selectedJobDescription;
    private int selectedJobNoOfOpenings; 
    private String selectedJobStatus;
    
    private String userName;
    private String userAge;    
    private String userGender;
    private String userEmailId;
    private String userContactNumber;
    private String userEducation;
    private String userWorkExp;
    
    private int applicationID;
    private String statusHR;
    private String previousStatusHR;
    
    private String isApplicationFinal;
    
    private String newJobTitle;
    private String newJobDescription;
    private String newJobNoOfOpenings;
    
    public UserAccount(String myID, String myPassword, String myName, String userType) 
    {
        id = myID;
        password = myPassword;
        name = myName;
        type = userType;
        
        selectedJobID = 0;
        
        if(type.equals("User"))
        {
            displayAllJobs();
        }
        else
        {
            displayJobsForHr();
        }
        
    }

    public ArrayList<JobDetails> getAllJobDetails() {
        return allJobDetails;
    }

    public ArrayList<JobDetails> getJobDetailsForHr() {
        return jobDetailsForHr;
    }

    public ArrayList<ApplicantDetails> getJobApplicantDetails() {
        return jobApplicantDetails;
    }

    public ArrayList<String> getAppliedJobsID() {
        return appliedJobsID;
    }

    public ArrayList<String> getAllStatus() {
        return allStatus;
    }
            
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }   

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public int getSelectedJobID() {
        return selectedJobID;
    }

    public void setSelectedJobID(int selectedJobID) {
        this.selectedJobID = selectedJobID;
    }
    
    public String setSelectedJobData(int jId,String jTitle, String jDesc, int jNoOpen)
    {
        setSelectedJobID(jId);
        /* Also can do sql query here to get data from jId and 
           and then set SelectedJobTitle, SelectedJobDescription, 
           SelectedJobNoOfOpenings using set method or 
           using this.attribute = value */      
        setSelectedJobTitle(jTitle);
        setSelectedJobDescription(jDesc);
        setSelectedJobNoOfOpenings(jNoOpen);
        
        if(type.equals("User"))
        {
            return "viewJob";
        }
        else
        {
            getJobApplicants(selectedJobID);
            return "viewApplicants.xhtml";
        }
    }
    
    public String setSelectedApplicantData(int aID)
    {
        allStatus = new ArrayList<String> ();
        allStatus.add("Being Reviewed");
        allStatus.add("Selected");
        allStatus.add("Not Selected");
        allStatus.add("Withdrawn");
        
        for(int i=0; i<jobApplicantDetails.size(); i++)
        {
            if(jobApplicantDetails.get(i).getApplicationID() == aID )
            {
                setApplicationID(jobApplicantDetails.get(i).getApplicationID());
                setUserName(jobApplicantDetails.get(i).getApplicantName());
                setUserAge(jobApplicantDetails.get(i).getApplicantAge());
                setUserGender(jobApplicantDetails.get(i).getApplicantGender());
                setUserEmailId(jobApplicantDetails.get(i).getApplicantEmailID());
                setUserContactNumber(jobApplicantDetails.get(i).getApplicantContactNumber());
                setUserEducation(jobApplicantDetails.get(i).getApplicantEducation());
                setUserWorkExp(jobApplicantDetails.get(i).getApplicantWorkExp());
                setStatusHR(jobApplicantDetails.get(i).getStatusHR());
                setPreviousStatusHR(jobApplicantDetails.get(i).getStatusHR());
                setIsApplicationFinal(jobApplicantDetails.get(i).getIsApplicationFinalzed());
            }
        }  
        
        return "selectApplicant.xhtml";
    }
    
    public String changeHRStatus()
    {
        if(statusHR.equals(previousStatusHR))
        {
            return "The status of application is : '" + statusHR + "'";
        }
        else if(!statusHR.equals(previousStatusHR) && previousStatusHR.equals("Withdrawn"))
        {
            return "You can not change status of application that is withdrawn.";
        }
        else if(!statusHR.equals(previousStatusHR) && statusHR.equals("Withdrawn"))
        {
            return "You can not withdraw the application.";
        }
        else if(isApplicationFinal.equals("True"))
        {
            return "You have already finalized this application.";
        }
        else
        {
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;

            try
            {
                final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";

                //connect to the database with user name and password
                connection = DriverManager.getConnection(DATABASE_URL, 
                        "shahp1511", "1357780");   
                statement = connection.createStatement();

                int u = statement.executeUpdate("update jobapplications "
                        + " set StatusHR = '" + statusHR + "' where "
                        + " ApplicationID = '" + applicationID + "'" );
                
                return "Status is changed to '" + statusHR + "'";
            }
            catch (SQLException e)
            {           
                e.printStackTrace();
                return "Internal Error! Please try again later.";
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
    
    public String finalizeApplicant()
    {
        boolean underReview = false;
        boolean isFinalizedAll = true;
        int noOfSelectedApplicant = 0;
        
        for(int i=0; i<jobApplicantDetails.size(); i++)
        {            
            if(jobApplicantDetails.get(i).getStatusHR().equals("Selected"))
            {
                noOfSelectedApplicant++;
            }
            if(jobApplicantDetails.get(i).getStatusHR().equals("Being Reviewed"))
            {
                underReview = true;
            }
            if(jobApplicantDetails.get(i).getIsApplicationFinalzed().equals("False"))
            {
                isFinalizedAll = false;
            }
        } 
        
        if(noOfSelectedApplicant == 0)
        {
            return "You have not selected any applicant for this job";
        }
        else if(jobApplicantDetails.size() < selectedJobNoOfOpenings)
        {
            return "You can not finalize applicants since no. of applicants "
                    + " are less than the number of openings for this job";
        }
        else if(noOfSelectedApplicant > selectedJobNoOfOpenings)
        {
            return "You can not select the applicants more than the "
            + "no. of available positions for this job";
        }
        else if(underReview)
        {
            return "Since there are some applications which are Being Reviewed, you can not finalize applicantions.";
        }
        else if(isFinalizedAll)
        {
            return "You have already finalized all applicants";
        }
        else
        {
            for(int i=0; i<jobApplicantDetails.size(); i++)
            {            
                if(jobApplicantDetails.get(i).getStatusHR().equals("Selected") ||   
                   jobApplicantDetails.get(i).getStatusHR().equals("Not Selected") )
                {
                    Connection connection = null;
                    Statement statement = null;
                    ResultSet resultSet = null;

                    try
                    {
                        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";

                        //connect to the database with user name and password
                        connection = DriverManager.getConnection(DATABASE_URL, 
                                "shahp1511", "1357780");   
                        statement = connection.createStatement();

                        int u = statement.executeUpdate("update jobapplications "
                                + " set Status = '" + jobApplicantDetails.get(i).getStatusHR() + "' "
                                + ", IsFinalized = 'True' where "
                                + " ApplicationID = '" + jobApplicantDetails.get(i).getApplicationID() + "'" );                        
                    }
                    catch (SQLException e)
                    {           
                        e.printStackTrace();
                        return "Internal Error! Please try again later.";
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
            return "You have finalized " + noOfSelectedApplicant + 
                    " applicants for this job";
        }
    }
    
    public void displayAllJobs()
    {
        allJobDetails = new ArrayList<JobDetails>();
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL, 
                    "shahp1511", "1357780");   
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("Select * from "
                    + "jobdetails where NumberOfOpening > 0" );
            
            int noOfApp = 0;
            
            while(resultSet.next())
            {
                allJobDetails.add(new JobDetails(resultSet.getInt(1), 
                        resultSet.getString(2), resultSet.getString(3), 
                        resultSet.getInt(4), noOfApp));
            }
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
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
    
    public void displayJobsForHr()
    {      
        jobDetailsForHr = new ArrayList<JobDetails>();
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;        
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL, 
                    "shahp1511", "1357780");   
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("Select jd.JobID, jd.JobTitle, "
                    + " jd.JobDescription, jd.NumberOfOpening, count(ApplicantID) from "
                    + " jobdetails jd left outer join jobapplications ja "
                    + " on jd.JobID = ja.JobID and ja.Status != 'Withdrawn'  group by jd.JobID " );
            
            while(resultSet.next())
            {
                jobDetailsForHr.add(new JobDetails(resultSet.getInt(1), 
                        resultSet.getString(2), resultSet.getString(3), 
                        resultSet.getInt(4), resultSet.getInt(5)));
            }
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
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
    
    public void getJobApplicants(int jId)
    {
        jobApplicantDetails = new ArrayList<ApplicantDetails> ();
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;        
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL, 
                    "shahp1511", "1357780");   
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("select * from jobapplications "
                    + " where JobID = '" + jId + "' and Status != 'Withdrawn'" );
            
            while(resultSet.next())
            {
                jobApplicantDetails.add(new ApplicantDetails(resultSet.getInt(1), resultSet.getString(4),
                 resultSet.getString(12), resultSet.getString(5), resultSet.getString(6),
                 resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                 resultSet.getString(10), resultSet.getString(13)));
            }
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
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

    public String getSelectedJobTitle() {
        return selectedJobTitle;
    }

    public void setSelectedJobTitle(String selectedJobTitle) {
        this.selectedJobTitle = selectedJobTitle;
    }

    public String getSelectedJobDescription() {
        return selectedJobDescription;
    }

    public void setSelectedJobDescription(String selectedJobDescription) {
        this.selectedJobDescription = selectedJobDescription;
    }

    public int getSelectedJobNoOfOpenings() {
        return selectedJobNoOfOpenings;
    }

    public void setSelectedJobNoOfOpenings(int selecteJobNoOfOpenings) {
        this.selectedJobNoOfOpenings = selecteJobNoOfOpenings;
    }

    public String getSelectedJobStatus() {
        return selectedJobStatus;
    }

    public void setSelectedJobStatus(String selectedJobStatus) {
        this.selectedJobStatus = selectedJobStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
        this.userContactNumber = userContactNumber;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public String getUserWorkExp() {
        return userWorkExp;
    }

    public void setUserWorkExp(String userWorkExp) {
        this.userWorkExp = userWorkExp;
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public String getStatusHR() {
        return statusHR;
    }

    public void setStatusHR(String statusHR) {
        this.statusHR = statusHR;
    }

    public String getPreviousStatusHR() {
        return previousStatusHR;
    }

    public void setPreviousStatusHR(String previousStatusHR) {
        this.previousStatusHR = previousStatusHR;
    }

    public String getIsApplicationFinal() {
        return isApplicationFinal;
    }

    public void setIsApplicationFinal(String isApplicationFinal) {
        this.isApplicationFinal = isApplicationFinal;
    }

    public String getNewJobTitle() {
        return newJobTitle;
    }

    public void setNewJobTitle(String newJobTitle) {
        this.newJobTitle = newJobTitle;
    }

    public String getNewJobDescription() {
        return newJobDescription;
    }

    public void setNewJobDescription(String newJobDescription) {
        this.newJobDescription = newJobDescription;
    }

    public String getNewJobNoOfOpenings() {
        return newJobNoOfOpenings;
    }

    public void setNewJobNoOfOpenings(String newJobNoOfOpenings) {
        this.newJobNoOfOpenings = newJobNoOfOpenings;
    }

 

              
    
    //log out, kill the session and return to the main page
    public String signOut()
    {
        FacesContext.getCurrentInstance().
                getExternalContext().invalidateSession();
        return "index.xhtml";       
    }   
    
    public void resetUserData()
    {
        userName = "";
        userAge = "";
        userGender = "";
        userEmailId = "";
        userContactNumber = "";
        userEducation = "";
        userWorkExp = "";
        selectedJobTitle = "";
    }
    
    public String applyForJob()
    {
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
            resultSet = statement.executeQuery("select * from jobapplications where "
                    + " JobID = '" + selectedJobID + "' and ApplicantID = '" + id + "'");
                        
            //check if already applied for job
            if(resultSet.next())
            {
                return "You have already applied for this job";
            }
            else
            {
                int i = statement.executeUpdate("insert into jobapplications values (null,'"
                + selectedJobID + "','" + id + "','" + userName + "','" + userAge +
                "','" + userGender + "','" + userEmailId + "','" + userContactNumber
                + "','" + userEducation + "','" + userWorkExp + "','Being Reviewed',"
                        + " 'Being Reviewed', 'False' ,' "
                        + GetDateAndTime.DateTime() + "')");                                                                               
                
                return "Congratulations..!! You have succussfully applied"
                        + " for this job..!!!";
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
    
    public void selectJobToCheckStatus()
    {
        //load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
        }
        catch (Exception e)
        {
            //error message
            e.printStackTrace();
        }
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            connection = DriverManager.getConnection(DATABASE_URL, "shahp1511", "1357780");  
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select jd.JobTitle "
                    + " from jobdetails jd, jobapplications ja where "
                    + " jd.JobID = ja.JobID and ja.ApplicantID = '" + id + "'" );
            
            appliedJobsID = new ArrayList<String> ();
            
            while(resultSet.next())
            {
                appliedJobsID.add(resultSet.getString(1));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();                       
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
       
    public void displayJobStatus()
    {
        //load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");            
        }
        catch (Exception e)
        {
            //error message
            e.printStackTrace();
        }
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahp1511";
            
            connection = DriverManager.getConnection(DATABASE_URL, "shahp1511", "1357780");  
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select jd.JobID, jd.JobTitle, jd.JobDescription, ja.Status "
                    + " from jobdetails jd, jobapplications ja where "
                    + " jd.JobID = ja.JobID and jd.JobTitle = '" + selectedJobTitle + "' "
                    + " and ApplicantID = '" + id +"'" );
                                    
            if(resultSet.next())
            {
                setSelectedJobID(resultSet.getInt(1));
                setSelectedJobDescription(resultSet.getString(3));
                setSelectedJobStatus(resultSet.getString(4));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();                       
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
    
    public String withdrawApplication()
    {
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
            
            if(selectedJobStatus.equals("Being Reviewed"))
            {
                connection = DriverManager.getConnection(DATABASE_URL, "shahp1511", "1357780");  
                statement = connection.createStatement();
                int u = statement.executeUpdate("update jobapplications set Status = 'Withdrawn', "
                        + " StatusHR = 'Withdrawn' "
                        + " where JobID = '" + selectedJobID + "' and "
                        + " ApplicantID = '" + id + "'" );  
                
                return "Your application has been successfully withdrawn..!!";
            }
            else
            {
                return "You can not withdraw this application now..!!";
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
    
    public String createNewJob()
    {
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
            
            resultSet = statement.executeQuery("select * from jobdetails "
                    + " where JobTitle ='" + newJobTitle + "'");
            
            if(resultSet.next())
            {
                return "Job : " + newJobTitle + " already exists.";
            }
            else
            {
                int i = statement.executeUpdate("insert into jobdetails values(null, '"
                        + newJobTitle + "','" + newJobDescription + "','" +
                         newJobNoOfOpenings + "')");  
                
                newJobTitle = "";
                newJobDescription = "";
                newJobNoOfOpenings = "";

                return "New job created successfully..!!";                
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
    
}
