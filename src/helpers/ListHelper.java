/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseModel;
import model.List;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class ListHelper {

    public JSONObject addList(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        sendJson.put("functionNumber", "4");
        sendJson.put("addListCondition", DatabaseModel.addList(new List(object.get("listtitle").toString(), object.get("startdate").toString(), object.get("deadline").toString(), object.get("color").toString(), "todo"), new User(object.get("username").toString())));
        return sendJson;
    }

    public JSONArray getCreatedLists(ResultSet rs) {
        JSONArray listsJson = new JSONArray();
        if (rs != null) {
            try {
                while (rs.next()) {
                    JSONObject listJson = new JSONObject();
                    listJson.put("id", rs.getInt("ID"));
                    listJson.put("title", rs.getString("TITLE"));
                    listJson.put("startdate", rs.getString("START_DATE"));
                    listJson.put("deadline", rs.getString("DEADLINE"));
                    listJson.put("color", rs.getString("COLOR"));
                    listJson.put("status", rs.getString("STATUS"));
                    ResultSet rsItems = DatabaseModel.getItemByListID(new List(rs.getInt("ID")));
                    JSONArray itemsArray = new JSONArray();
                    while (rsItems.next()) {
                        JSONObject itemObject = new JSONObject();
                        itemObject.put("title", rsItems.getString("title"));
                        itemObject.put("id", rsItems.getInt("ID"));
                        itemObject.put("desc", rsItems.getString("description"));
                        itemObject.put("comment", rsItems.getString("comment"));
                        itemObject.put("status", rsItems.getString("status"));
                        itemsArray.put(itemObject);
                    }
                    listJson.put("items", itemsArray);
                    listsJson.put(listJson);
                }
            } catch (SQLException ex) {
                System.out.println("user has no lists");
            }
        }
        return listsJson;

    }

}
