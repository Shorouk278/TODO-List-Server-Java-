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
public class User {

    private String username;
    private String password;
    private boolean onlineState;
    private String answer1;
    private String answer2;

    /**
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return onlineState
     */
    public boolean isOnlineState() {
        return onlineState;
    }

    /**
     *
     * @param onlineState
     */
    public void setOnlineState(boolean onlineState) {
        this.onlineState = onlineState;
    }

    /**
     *
     * @return answer1
     */
    public String getAnswer1() {
        return answer1;
    }

    /**
     *
     * @param answer1
     */
    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    /**
     *
     * @return answer2
     */
    public String getAnswer2() {
        return answer2;
    }

    /**
     *
     * @param answer2
     */
    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    /**
     *
     * @param username
     * @param password
     * @param onlineState
     * @param answer1
     * @param answer2
     */
    public User(String username, String password, boolean onlineState, String answer1, String answer2) {
        this.username = username;
        this.password = password;
        this.onlineState = onlineState;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }

    /**
     *
     * @param username
     * @param password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @param username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * 
     * @param username
     * @param answer1
     * @param answer2 
     */
    public User(String username, String answer1, String answer2) {
        this.username = username;
        this.answer1 = answer1;
        this.answer2 = answer2;
    }
}
