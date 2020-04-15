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
import model.DatabaseModel;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class RequestHelper {

    public JSONArray getFriendRequests(ResultSet rs) {
        JSONArray friendRequests = new JSONArray();
        if (rs != null) {
            try {
                while (rs.next()) {
                    JSONObject friendRequest = new JSONObject();
                    friendRequest.put("sender", rs.getString("creator_name"));
                    friendRequests.put(friendRequest);
                }
            } catch (SQLException ex) {
                System.out.println("user has no lists");
            }
        }
        return friendRequests;

    }

    public JSONArray getAssignedRequests(ResultSet rs) {
        JSONArray friendsJson = new JSONArray();
        try {
            while (rs.next()) {
                JSONObject friend = new JSONObject();
                friend.put("id", rs.getInt("item_id"));
                friend.put("creator", rs.getString("creator"));
                friendsJson.put(friend);
            }
        } catch (SQLException ex) {
            System.out.println("user has no friends");
        }
        return friendsJson;

    }

    public JSONObject searchRequests(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        sendJson.put("functionNumber", "8");
        sendJson.put("checkResult", DatabaseModel.searchRequests(new User(object.getString("sender")), new User(object.getString("receiver"))));
        return sendJson;
    }

    public JSONObject rejectFriendRequest(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        if (DatabaseModel.rejectFriendRequest(new User(object.getString("sender")), new User(object.getString("reciever")))) {
            sendJson.put("functionNumber", "10");
            sendJson.put("rejectCondition", "true");
        }
        return sendJson;
    }

    public void sendRequestNotification(String recieverName, String senderName, String functionNum, int id) {
        for (Handler reciever : clientsVector) {
            if (reciever.username.equalsIgnoreCase(recieverName)) {
                JSONObject friendRequest = new JSONObject();
                friendRequest.put("functionNumber", functionNum);
                friendRequest.put("sender", senderName);
                if (id != 0) {
                    friendRequest.put("id", id);
                }
                reciever.ps.println(friendRequest);
            }
        }
    }

    public void sendAcceptRequest(String senderName, String recieverName) {
        for (Handler sender : clientsVector) {
            if (sender.username.equalsIgnoreCase(senderName)) {
                JSONObject friend = new JSONObject();
                friend.put("functionNumber", "9");
                friend.put("reciever", recieverName);
                sender.ps.println(friend);
            }
        }
    }
}
