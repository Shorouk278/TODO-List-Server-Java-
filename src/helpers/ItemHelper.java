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
import model.DatabaseModel;
import model.Item;
import model.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class ItemHelper {

    public JSONArray sendItemDetails(ResultSet itemDetails, ResultSet itemUserAssigned) {
        JSONArray itemAllDetail = new JSONArray();
        itemAllDetail.put(0, itemDetails(itemDetails));
        itemAllDetail.put(1, itemCollaborate(itemUserAssigned));
        return itemAllDetail;
    }

    private JSONObject itemDetails(ResultSet itemDetails) {
        JSONObject item = new JSONObject();
        try {

            itemDetails.next();
            item.put("title", itemDetails.getString("title"));
            item.put("desc", itemDetails.getString("description"));
            item.put("comments", itemDetails.getString("comment"));
            item.put("status", itemDetails.getString("status"));
        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    private JSONObject itemCollaborate(ResultSet itemCol) {
        JSONObject item = new JSONObject();
        try {
            if (itemCol.next()) {
                item.put("USERNAME", itemCol.getString("USERNAME"));
                item.put("ITEM_ID", itemCol.getInt("ITEM_ID"));
                item.put("ACCEPT_STATE", itemCol.getBoolean("ACCEPT_STATE"));
                item.put("DONE_STATE", itemCol.getBoolean("DONE_STATE"));
                item.put("CREATOR", itemCol.getString("CREATOR"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

    public JSONArray getAssignedItems(ResultSet rs) {
        JSONArray assignedItems = new JSONArray();
        try {
            while (rs.next()) {
                ResultSet itemdetails = DatabaseModel.getItemByID(new Item(rs.getInt("item_id")));
                while (itemdetails.next()) {
                    JSONObject item = new JSONObject();
                    item.put("id", itemdetails.getInt("id"));
                    item.put("title", itemdetails.getString("title"));
                    assignedItems.put(item);
                }

            }
        } catch (SQLException ex) {
            System.out.println("user has no assigned items");
        }
        return assignedItems;

    }

    public void sendItemtoUser(JSONObject object) {
        ResultSet rs = DatabaseModel.getItem(new Item(object.getString("title"), object.getString("desc")), new List(object.getInt("listID")));
        try {
            if (rs.next()) {

                for (Handler reciever : clientsVector) {
                    if (reciever.username.equalsIgnoreCase(object.getString("username"))) {
                        JSONObject item = new JSONObject();
                        item.put("functionNumber", "13");
                        item.put("title", rs.getString("title"));
                        item.put("desc", rs.getString("description"));
                        item.put("itemID", rs.getInt("ID"));
                        reciever.ps.println(item);
                        break;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateItem(JSONObject object) {
        Item updateItem = new Item(object.getInt("itemID"));
        updateItem.setTitle(object.getString("title"));
        updateItem.setStatus(object.getString("status"));
        updateItem.setDescription(object.getString("desc"));
        updateItem.setComment(object.getString("comment"));
        DatabaseModel.updateItem(updateItem);
        Notify(object.getInt("itemID"), object.getString("title"), object.getString("updater"));
    }

    private void Notify(int itemID, String title, String updater) {
        ResultSet rs = DatabaseModel.getUserAssigneditemById(new Item(itemID));
        try {
            if (rs.next()) {
                if (rs.getString("username").equals(updater)) {
                    sendNotification(updater, rs.getString("creator"), title);
                } else {
                    sendNotification(updater, rs.getString("username"), title);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void sendNotification(String updater, String reciever, String title) {
        JSONObject notification = new JSONObject();
        notification.put("functionNumber", "20");
        notification.put("updater", updater);
        notification.put("itemTitle", title);
        for (Handler ch : clientsVector) {
            if (ch.username.equals(reciever)) {
                ch.ps.println(notification);
                break;
            }
        }
    }

    public JSONObject sendAssignedItem(ResultSet rs) {
        JSONObject item = new JSONObject();
        try {
            if (rs.next()) {
                item.put("functionNumber", "18");
                item.put("title", rs.getString("title"));
                item.put("id", rs.getInt("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return item;
    }

}
