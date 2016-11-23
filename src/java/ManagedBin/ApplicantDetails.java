/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBin;

import java.sql.*;
import java.util.ArrayList;
import java.text.*;
/**
 *
 * @author Priti
 */
public class ApplicantDetails 
{
    private int applicationID;
    private String applicantName;
    private String statusHR;
    
    private String applicantAge;
    private String applicantGender;
    private String applicantEmailID;
    private String applicantContactNumber;
    private String applicantEducation;
    private String applicantWorkExp;;
    
    private String isApplicationFinalzed;
            
    public ApplicantDetails(int aId, String aName, String sHr,
             String aAge, String aGender, String aEID, String aCNum, 
             String aEdu, String aWorkExp, String isFinal)
    {
        applicationID = aId;
        applicantName = aName;
        statusHR = sHr;
        applicantAge = aAge;
        applicantGender = aGender;
        applicantEmailID = aEID;
        applicantContactNumber = aCNum;
        applicantEducation = aEdu;
        applicantWorkExp = aWorkExp;
        isApplicationFinalzed = isFinal;
    
    }

    public int getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(int applicationID) {
        this.applicationID = applicationID;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getStatusHR() {
        return statusHR;
    }

    public void setStatusHR(String statusHR) {
        this.statusHR = statusHR;
    }

    public String getApplicantAge() {
        return applicantAge;
    }

    public void setApplicantAge(String applicantAge) {
        this.applicantAge = applicantAge;
    }

    public String getApplicantGender() {
        return applicantGender;
    }

    public void setApplicantGender(String applicantGender) {
        this.applicantGender = applicantGender;
    }

    public String getApplicantEmailID() {
        return applicantEmailID;
    }

    public void setApplicantEmailID(String applicantEmailID) {
        this.applicantEmailID = applicantEmailID;
    }

    public String getApplicantContactNumber() {
        return applicantContactNumber;
    }

    public void setApplicantContactNumber(String applicantContactNumber) {
        this.applicantContactNumber = applicantContactNumber;
    }

    public String getApplicantEducation() {
        return applicantEducation;
    }

    public void setApplicantEducation(String applicantEducation) {
        this.applicantEducation = applicantEducation;
    }

    public String getApplicantWorkExp() {
        return applicantWorkExp;
    }

    public void setApplicantWorkExp(String applicantWorkExp) {
        this.applicantWorkExp = applicantWorkExp;
    }   

    public String getIsApplicationFinalzed() {
        return isApplicationFinalzed;
    }

    public void setIsApplicationFinalzed(String isApplicationFinalzed) {
        this.isApplicationFinalzed = isApplicationFinalzed;
    }
        
}
