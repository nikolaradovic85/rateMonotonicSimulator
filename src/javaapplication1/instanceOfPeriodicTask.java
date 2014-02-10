/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

/**
 *
 * @author nikola
 */
public class instanceOfPeriodicTask implements Comparable<instanceOfPeriodicTask>{

    private int taskPeriod;// perioda 
    private int phi;//faza 
    private int rActivationTime;//vrijeme aktivacije
    private int dAbsolutDeadline;//apsolutni deadline
    private int cExecutionTime;//vrijeme izvrsavanja 

    /**
    *constructor 
    */
    public instanceOfPeriodicTask(int taskPeriod, int phi,
            int rActivationTime, int dAbsolutDeadline, int cExecutionTime) {
        this.cExecutionTime = cExecutionTime;
        this.dAbsolutDeadline = dAbsolutDeadline;
        this.phi = phi;
        this.rActivationTime = rActivationTime;
        this.taskPeriod = taskPeriod;
    }

    /**
     * @return the taskPeriod
     */
    public int getTaskPeriod() {
        return taskPeriod;
    }

    /**
     * @param taskPeriod the taskPeriod to set
     */
    public void setTaskPeriod(int taskPeriod) {
        this.taskPeriod = taskPeriod;
    }

    /**
     * @return the phi
     */
    public int getPhi() {
        return phi;
    }

    /**
     * @param phi the phi to set
     */
    public void setPhi(int phi) {
        this.phi = phi;
    }

    /**
     * @return the rActivationTime
     */
    public int getrActivationTime() {
        return rActivationTime;
    }

    /**
     * @param rActivationTime the rActivationTime to set
     */
    public void setrActivationTime(int rActivationTime) {
        this.rActivationTime = rActivationTime;
    }

    /**
     * @return the dAbsolutDeadline
     */
    public int getdAbsolutDeadline() {
        return dAbsolutDeadline;
    }

    /**
     * @param dAbsolutDeadline the dAbsolutDeadline to set
     */
    public void setdAbsolutDeadline(int dAbsolutDeadline) {
        this.dAbsolutDeadline = dAbsolutDeadline;
    }

    /**
     * @return the cExecutionTime
     */
    public int getcExecutionTime() {
        return cExecutionTime;
    }

    /**
     * @param cExecutionTime the cExecutionTime to set
     */
    public void setcExecutionTime(int cExecutionTime) {
        this.cExecutionTime = cExecutionTime;
    }
    
    //@override
    public int compareTo(instanceOfPeriodicTask other){
        return this.taskPeriod - other.getTaskPeriod();
    }
    
}