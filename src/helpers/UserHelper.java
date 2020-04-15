/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseModel;
import model.User;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class UserHelper {

    ListHelper listHelper = new ListHelper();
    RequestHelper requestHelper = new RequestHelper();
    FriendsHelper friendHelper = new FriendsHelper();
    ItemHelper itemHelper = new ItemHelper();

    public JSONObject getUserData(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        // created list
        ResultSet rs = DatabaseModel.getCreatedLists(new User(object.get("username").toString()));
        sendJson.put("functionNumber", "5");
        sendJson.put("listArray", listHelper.getCreatedLists(rs));
        // friend requests
        rs = DatabaseModel.getUserFriendRequests(new User(object.get("username").toString()));
        sendJson.put("friendRequest", requestHelper.getFriendRequests(rs));
        // friends
        rs = DatabaseModel.getUserFriends(new User(object.get("username").toString()));
        sendJson.put("friends", friendHelper.getFriends(rs, object.get("username").toString()));
        //assign requests
        rs = DatabaseModel.getUserAssignedRequests(new User(object.get("username").toString()));
        sendJson.put("itemsReq", requestHelper.getAssignedRequests(rs));
        rs = DatabaseModel.getUserAssigneditems(new User(object.get("username").toString()));
        sendJson.put("assignedItems", itemHelper.getAssignedItems(rs));
        return sendJson;
    }

    public JSONObject searchByUserName(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        ResultSet rsSearch = DatabaseModel.searchByUsername(new User(object.get("username").toString()));
        try {
            if (rsSearch.next()) {
                sendJson.put("functionNumber", "6");
                sendJson.put("username", rsSearch.getString("username"));

            }
        } catch (SQLException ex) {
            System.out.println("no user registered for that name");
        }
        return sendJson;
    }
}
