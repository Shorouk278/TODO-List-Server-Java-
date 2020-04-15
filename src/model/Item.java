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
public class Item {
    private int id;
    private String title;
    private String description;
    private String status;
    private String comment;


 
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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
     *
     * @return comment
     */
    public String getComment() {
        return comment;
    }

   /**
    * @param comment 
    */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * for creation
     * @param title
     * @param description
     * @param status
     * @param comment
     */
    public Item( String title, String description, String status, String comment) {
      
        this.title = title;
        this.description = description;
        this.status = status;
        this.comment = comment;
    }
    
    /**
     * for delete
     * @param id 
     */
    public Item(int id)
    {
       this.id = id;
    }
   /**
    * for creation
    * @param title
    * @param description 
    */
   public Item( String title, String description) {
        this.title = title;
        this.description = description;
    }
}
