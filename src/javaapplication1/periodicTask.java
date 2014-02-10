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

public class periodicTask implements Comparable <periodicTask>{
    private int taskPeriod;//perioda
    private int phi;//faza
    private int cTaskExecutionTime;//vrijeme izvrsavanja
    
    //constructor
    public periodicTask(int taskPeriod, int phi, int cTaskExecutionTime){
        this.cTaskExecutionTime = cTaskExecutionTime;
        this.phi = phi;
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
     * @return the cTaskExecutionTime
     */
    public int getcTaskExecutionTime() {
        return cTaskExecutionTime;
    }

    /**
     * @param cTaskExecutionTime the cTaskExecutionTime to set
     */
    public void setcTaskExecutionTime(int cTaskExecutionTime) {
        this.cTaskExecutionTime = cTaskExecutionTime;
    }
    
    //@override
    public int compareTo(periodicTask other){
        return this.taskPeriod - other.getTaskPeriod();
    }
}
      