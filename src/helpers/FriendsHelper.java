/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import handler.Handler;
import static handler.Handler.clientsVector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class FriendsHelper {

    public JSONArray getFriends(ResultSet rs, String username) {
        JSONArray friendsJson = new JSONArray();
        try {
            while (rs.next()) {
                JSONObject friend = new JSONObject();
                if (rs.getString("creator_name").equals(username)) {
                    friend.put("friend", rs.getString("team_member"));
                } else {
                    friend.put("friend", rs.getString("creator_name"));
                }
                if (updateFriends(friend.getString("friend"), "online", username)) {
                    friend.put("state", "online");
                  
                } else {
                    friend.put("state", "offline");

                }
                friendsJson.put(friend);
            }
        } catch (SQLException ex) {
            System.out.println("user has no friends");
        }
        return friendsJson;

    }

    private boolean updateFriends(String name, String state, String username) {
        boolean flag = false;
        for (Handler user : Handler.clientsVector) {
            if (user.username.equals(name)) {
                flag = true;
                JSONObject changeState = new JSONObject();
                changeState.put("friend", username);
                changeState.put("functionNumber", "12");
                changeState.put("state", state);
                user.ps.println(changeState);
                break;
            }
        }
        return flag;
    }

    public void removeFriend(String deleted, String recieverName) {
        for (Handler deletedfri : clientsVector) {
            if (deletedfri.username.equalsIgnoreCase(deleted)) {
                JSONObject friend = new JSONObject();
                friend.put("functionNumber", "11");
                friend.put("remover", recieverName);
                deletedfri.ps.println(friend);
            }
        }
    }

    public void updateOffline(ResultSet rs, String username) {
        try {
            while (rs.next()) {
                String friendName;
                if (rs.getString("creator_name").equalsIgnoreCase(username)) {
                    friendName = rs.getString("team_member");
                } else {
                    friendName = rs.getString("creator_name");
                }
                updateFriends(friendName, "offline", username);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
