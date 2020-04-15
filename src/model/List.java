/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;



/**
 *
 * @author Fouad
 */
public class List {
    private int id;
    private String title;
    private String startDate;
    private String deadline;
    private String color;
    private String status ;

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     *
     * @param deadline
     */
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    /**
     *
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * for create new list
     * @param title
     * @param startDate
     * @param deadline
     * @param color
     * @param status
     */
    public List(String title, String startDate, String deadline, String color, String status) {
       
        this.title = title;
        this.startDate = startDate;
        this.deadline = deadline;
        this.color = color;
        this.status = status;
    }
    
    /**
     * for update
     * @param id
     * @param title
     * @param startDate
     * @param deadline
     * @param color
     * @param status 
     */
     public List(int id,String title, String startDate, String deadline, String color, String status) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.deadline = deadline;
        this.color = color;
        this.status = status;
    }
    
    /**
     * for delete
     * @param id 
     */
    public List(int id)
    {   
         this.id = id;
    }

    public List(int id, String title, String status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }
    
    
    
}
