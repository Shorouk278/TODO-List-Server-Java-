/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import model.DatabaseModel;
import model.User;
import org.json.JSONObject;

/**
 *
 * @author mostafa
 */
public class Registeration {

    public JSONObject login(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        sendJson.put("functionNumber", "1");
        sendJson.put("loginCondition", DatabaseModel.checkLogin(new User(object.get("username").toString(), object.get("password").toString())));
        return sendJson;
    }

    public JSONObject signup(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        sendJson.put("functionNumber", "2");
        sendJson.put("signUpCondition", DatabaseModel.addUser(new User(object.get("username").toString(), object.get("password").toString(), false, object.get("answer1").toString(), object.get("answer2").toString())));
        return sendJson;
    }

    public JSONObject recoverPassword(JSONObject object) {
        JSONObject sendJson = new JSONObject();
        sendJson.put("functionNumber", "3");
        sendJson.put("password", DatabaseModel.recoverPassword(new User(object.get("username").toString(), object.get("answer1").toString(), object.get("answer2").toString())));
        return sendJson;
    }

}
