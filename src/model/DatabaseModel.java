/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author Fouad
 */
public class DatabaseModel {

    static Connection conn;

    /**
     * this method starts the connection with database
     */
    public static void connectToDataBase() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/TodoList", "root", "root");
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "can not connect to database !!").show();
        }
    }

    /**
     * this method closes the connection with database
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * this method let you add new user into database .
     *
     * @param user object contains data about new register user .
     */
    public static boolean addUser(User user) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ROOT.USERS (USERNAME, PASSWORD, ONLINE_STATE, ANSWER1, ANSWER2)VALUES (?,?,default,?,?)");
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getAnswer1());
            pst.setString(4, user.getAnswer2());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not Insert to database !!");
        }
        return false;
    }

    /**
     * this method check username and password
     *
     * @param user object contains data about signed in user .
     * @return boolean depend on the existence of user in database .
     */
    public static boolean checkLogin(User user) {
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * from ROOT.USERS  WHERE USERNAME =? AND PASSWORD =?");
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());

            if (pst.executeQuery().next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not connect to database !!");
        }

        return false;
    }

    public static ResultSet getCreatedLists(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT * from ROOT.LIST  WHERE USERNAME =? ");
            pst.setString(1, user.getUsername());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Cannot execute get created lists query");
        }
        return rs;
    }

    public static int ListsNumberByStatus(String status) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT count(*) as col_num FROM ROOT.LIST where status = ?");
            pst.setString(1, status);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("col_num");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute get list collaborators number query !!");
        }
        return 0;
    }

    /**
     * this method recover password for user
     *
     * @param user
     * @return password or error message .
     */
    public static String recoverPassword(User user) {
        ResultSet rs;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT password from ROOT.USERS  WHERE USERNAME =? AND answer1 =?  AND answer2 =?");
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getAnswer1());
            pst.setString(3, user.getAnswer2());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute recovery password query !!");
        }
        return "Invalid Data";
    }

    /**
     * add new list .
     *
     * @param list object which contains the list details .
     * @param user user who creates the list .
     * @return boolean
     */
    public static boolean addList(List list, User user) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ROOT.LIST (TITLE, START_DATE, DEADLINE, COLOR, STATUS, USERNAME) VALUES (?, ?, ?, ?, ?, ?)");
            pst.setString(1, list.getTitle());
            pst.setString(2, list.getStartDate());
            pst.setString(3, list.getDeadline());
            pst.setString(4, list.getColor());
            pst.setString(5, list.getStatus());
            pst.setString(6, user.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute add list query !!");
        }

        return false;

    }

    public static boolean deleteList(List list) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM ROOT.LIST WHERE ID = ?");
            pst.setInt(1, list.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("can not execute delete list query !!");
        }

        return false;
    }

    /**
     * update list content
     *
     * @param list
     * @return boolean
     */
    public static boolean updateList(List list) {
        try {
            PreparedStatement pst = conn.prepareStatement("update list set title = ? , status = ? where id = ?");
            pst.setString(1, list.getTitle());
            pst.setString(2, list.getStatus());
            pst.setInt(3, list.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("can not execute update list query !!");
        }
        return false;
    }

    /**
     * get number of collaborators in list
     *
     * @param list
     * @return integer
     */
    public static int getListColaboratorsNumber(List list) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT count(*) as col_num FROM ROOT.LIST_COLLABORATORS where list_id = ?");
            pst.setInt(1, list.getId());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("col_num");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute get list collaborators number query !!");
        }
        return 0;
    }

    /**
     * get List Collaborator Names
     *
     * @param list
     * @return result set (list of names or null)
     */
    public static ResultSet getListColaboratorsNames(List list) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT COLLABORATOR_NAME as name FROM ROOT.LIST_COLLABORATORS where list_id = ?");
            pst.setInt(1, list.getId());
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            System.out.println("can not execute get list colaborators names query !!");
        }
        return rs;
    }

   /**
     * add new item in list .
     *
     * @param list list that contains this item .
     * @return boolean
     */
    public static boolean addItem(List list,Item item) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ROOT.ITEM (TITLE, STATUS, DESCRIPTION, COMMENT, LIST_ID) VALUES (?,'todo',?,'', ?)");
            pst.setString(1, item.getTitle());
            pst.setString(2, item.getDescription());
            pst.setInt(3, list.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute add item query !!");

        }
        return false;
    }

    /**
     * get number of collaborators for item .
     *
     * @param item
     * @return integer
     */
    public static int getItemCollaboratorsNumber(Item item) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT count(*) as col_num FROM ROOT.USER_ITEM_ASSIGNED where item_id = ?");
            pst.setInt(1, item.getId());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("col_num");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute get item colaborators number query !!");

        }

        return 0;
    }

    /**
     * search for user by user name .
     *
     * @param user
     * @return result set
     */
    public static ResultSet searchByUsername(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from users where username  = ?");
            pst.setString(1, user.getUsername());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            System.out.println("can not execute search by Username query !!");
        }
        return rs;
    }

    /**
     *
     * @param sender user who send friend request
     * @param receiver user who receive friend request
     * @return
     */
    public static boolean sendFriendRequest(User sender, User receiver) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO ROOT.USER_TEAM (CREATOR_NAME, TEAM_MEMBER, ACCEPT_STATE) VALUES (?, ?, false)");
            pst.setString(1, sender.getUsername());
            pst.setString(2, receiver.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute send friend request query !!");
        }
        return false;
    }

    /**
     *
     * @param sender user who send friend request
     * @param receiver user who receive friend request
     * @return
     */
    public static boolean acceptFriendRequest(User sender, User receiver) {
        try {
            PreparedStatement pst = conn.prepareStatement("update user_team set accept_state = true where creator_name = ? and team_member = ?");
            pst.setString(1, sender.getUsername());
            pst.setString(2, receiver.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute accept friend request query !!");
        }
        return false;
    }

    /**
     * get user friends .
     *
     * @param user
     * @return result set
     */
    public static ResultSet getUserFriends(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from ROOT.USER_TEAM where (creator_name = ? or team_member = ?) and (accept_state = true)");
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getUsername());
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            System.out.println("can not execute get user friends query !!");
        }
        return rs;
    }

    /**
     * returns a result set containing pending friend requests
     *
     * @param user
     * @return
     */
    public static ResultSet getUserFriendRequests(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select creator_name from ROOT.USER_TEAM where  team_member = ? and accept_state = false");
            pst.setString(1, user.getUsername());
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            System.out.println("can not execute get user friends query !!");
        }
        return rs;
    }

    /**
     * remove friend from user list .
     *
     * @param user1
     * @param user2
     * @return
     */
    public static boolean removeFriend(User user1, User user2) {
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE from user_team where (creator_name = ? and team_member = ?) or (creator_name = ? and team_member = ?)");
            pst.setString(1, user1.getUsername());
            pst.setString(2, user2.getUsername());
            pst.setString(3, user2.getUsername());
            pst.setString(4, user1.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute remove friend query !!");
        }
        return false;
    }

    /**
     *
     * @param sender who send friend request .
     * @param receiver who receive friend request .
     * @return
     */
    public static boolean searchRequests(User sender, User receiver) {

        try {
            PreparedStatement pst = conn.prepareStatement("select * from ROOT.USER_TEAM where (creator_name =? and team_member = ?) or (creator_name =? and team_member = ?)");
            pst.setString(1, sender.getUsername());
            pst.setString(2, receiver.getUsername());
            pst.setString(3, receiver.getUsername());
            pst.setString(4, sender.getUsername());
            if (pst.executeQuery().next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute search Requests query !!");
        }

        return false;
    }

    /**
     * delete request from user-team table if user pressed reject button
     *
     * @param sender
     * @param receiver
     * @return
     */
    public static boolean rejectFriendRequest(User sender, User receiver) {
        try {
            PreparedStatement pst = conn.prepareStatement("delete from user_team  where creator_name = ? and team_member = ?");
            pst.setString(1, sender.getUsername());
            pst.setString(2, receiver.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute accept friend request query !!");
        }
        return false;
    }

    public static void ChangeUserState(User user, String state) {
        try {
            PreparedStatement pst = conn.prepareStatement("update users set online_state  = ? where username = ?");
            pst.setString(1, state);
            pst.setString(2, user.getUsername());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * get number of users registered on the system
     *
     * @return integer
     */
    public static int getUsersNumber() {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT count(*) as col_num FROM ROOT.USERs");
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("col_num");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute get item colaborators number query !!");

        }

        return 0;
    }

    public static int getUsersNumberBystate(String state) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT count(*) as col_num FROM ROOT.USERs where online_state = ?");
            pst.setString(1, state);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("col_num");
            }
        } catch (SQLException ex) {
            System.out.println("can not execute get item colaborators number query !!");

        }

        return 0;
    }
    
     /**
     * get item by title
     * @param item
     * @return 
     */
    public  static ResultSet getItem(Item item,List list){
        ResultSet rs = null;
        try {
            
            PreparedStatement pst = conn.prepareStatement("SELECT *  FROM ROOT.ITEM where TITLE = ? and LIST_ID = ?");
            pst.setString(1, item.getTitle());
            pst.setInt(2, list.getId());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
         return rs;
    }
    public static boolean deleteItem(Item item){
        try {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM ROOT.ITEM WHERE ID = ?");
            pst.setInt(1, item.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("can not execute delete list query !!");
        }
        return false;
    }
    /**
     * get items by ListID
     * @param list
     * @return 
     */
    public  static ResultSet getItemByListID(List list){
        ResultSet rs = null;
        try {           
            PreparedStatement pst = conn.prepareStatement("SELECT *  FROM ROOT.ITEM where LIST_ID = ?");
            pst.setInt(1, list.getId());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
    /**
     * get items by ID
     * @param item
     * @return 
     */
    public  static ResultSet getItemByID(Item item){
        ResultSet rs = null;
        try {           
            PreparedStatement pst = conn.prepareStatement("SELECT *  FROM ROOT.ITEM where ID = ?");
            pst.setInt(1, item.getId());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    /**
     * update list content
     *
     * @param list
     * @return boolean
     */
    public static boolean updateItem(Item item) {
        try {
            PreparedStatement pst = conn.prepareStatement("update item set title = ? , description = ? ,comment = ? ,status = ? where id = ?");
            pst.setString(1, item.getTitle());
            pst.setString(2, item.getDescription());
            pst.setString(3, item.getComment());
            pst.setString(4, item.getStatus());
            pst.setInt(5, item.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("can not execute update list query !!");
        }
        return false;
    }
    public static boolean assignItemToFriend(Item item,User user,User creator) {
        try {
            PreparedStatement pst = conn.prepareStatement("INSERT INTO USER_ITEM_ASSIGNED (USERNAME, ITEM_ID, ACCEPT_STATE,DONE_STATE,creator) VALUES (?, ?, false,false,?)");
            pst.setString(1, user.getUsername());
            pst.setInt(2, item.getId());
            pst.setString(3, creator.getUsername());
            if (pst.executeUpdate() == 1) {
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("can not execute update list query !!");
        }
        return false;
    }
    
    /**
     * delete request from user-team table if user pressed reject button
     *
     * @param sender
     * @param receiver
     * @return
     */
    public static boolean rejectAssignedRequest(Item item) {
        try {
            PreparedStatement pst = conn.prepareStatement("delete from USER_ITEM_ASSIGNED  where item_id =?");
            pst.setInt(1, item.getId());         
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute reject assign request query !!");
        }
        return false;
    }
    
    /**
     *
     * @param sender user who send friend request
     * @param receiver user who receive friend request
     * @return
     */
    public static boolean acceptAssignRequest(Item item) {
        try {
            PreparedStatement pst = conn.prepareStatement("update USER_ITEM_ASSIGNED set accept_state = true where item_id = ?");
            pst.setInt(1, item.getId());
            if (pst.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("can not execute accept assign request query !!");
        }
        return false;
    }
    
    public static ResultSet getUserAssignedRequests(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from USER_ITEM_ASSIGNED  where username = ? and accept_state = false");
            pst.setString(1, user.getUsername());
            return pst.executeQuery();
            
        } catch (SQLException ex) {
            System.out.println("can not execute accept assign request query !!");
        }
        return rs;
    }
    
    public static ResultSet getUserAssigneditems(User user) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from USER_ITEM_ASSIGNED  where username = ? and accept_state = true");
            pst.setString(1, user.getUsername());
            return pst.executeQuery();
            
        } catch (SQLException ex) {
            System.out.println("can not execute accept assign request query !!");
        }
        return rs;
    }
    
    
    public  static ResultSet getAssignedColl(Item item){
        ResultSet rs = null;
        try {           
            PreparedStatement pst = conn.prepareStatement("SELECT *  FROM ROOT.USER_ITEM_ASSIGNED where Item_ID = ?");
            pst.setInt(1,item.getId());
            rs = pst.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
    
     public static ResultSet getUserAssigneditemById(Item item) {
        ResultSet rs = null;
        try {
            PreparedStatement pst = conn.prepareStatement("select * from USER_ITEM_ASSIGNED  where item_id = ? and ACCEPT_STATE = ?");
            pst.setInt(1,item.getId());
            pst.setBoolean(2, true);
            return pst.executeQuery();
            
        } catch (SQLException ex) {
            System.out.println("can not execute accept assign request query !!");
        }
        return rs;
    }
    
}
