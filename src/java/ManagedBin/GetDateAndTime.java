/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Priti
 */
public class GetDateAndTime 
{
    public static java.sql.Date DateTime()
    {        
        try
        {     
            //get current date and time
            Calendar cal = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy hh:mm:ss");
            Date myDate = formatter.parse(formatter.format(cal.getTime()));
            java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());

            return sqlDate;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }    
}
