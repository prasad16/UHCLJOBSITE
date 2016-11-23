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
public class JobDetails 
{
    private int jobid;
    private String jobTitle;
    private String jobDescription;
    private int noOfOpen;
    private int noOfApplicants;
    
    public JobDetails(int jId, String jTitle, String jobDesc, int noOpen, int noAppl)
    {
        jobid = jId;
        jobTitle = jTitle;
        jobDescription = jobDesc;
        noOfOpen = noOpen;
        noOfApplicants = noAppl;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public int getNoOfOpen() {
        return noOfOpen;
    }

    public void setNoOfOpen(int noOfOpen) {
        this.noOfOpen = noOfOpen;
    }

    public int getNoOfApplicants() {
        return noOfApplicants;
    }

    public void setNoOfApplicants(int noOfApplicants) {
        this.noOfApplicants = noOfApplicants;
    }
    
    
}
