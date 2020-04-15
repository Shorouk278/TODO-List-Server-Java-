/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import helpers.*;
import model.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONObject;

/**
 *
 * @author Fouad
 */
public class Handler extends Thread {

    public DataInputStream dis;
    public PrintStream ps;
    String fullData;
    public String username;
    public static Vector<Handler> clientsVector = new Vector<Handler>();
    Registeration registeration = new Registeration();
    ListHelper listHelper = new ListHelper();
    RequestHelper requestHelper = new RequestHelper();
    FriendsHelper friendHelper = new FriendsHelper();
    ItemHelper itemHelper = new ItemHelper();
    UserHelper userHelper = new UserHelper();

    /**
     * constructor that take socket to open stream .
     *
     * @param socket
     */
    public Handler(Socket socket) {
        try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            start();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Can Not Open Connection ").show();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                fullData = dis.readLine();
                if (fullData != null) {
                    managerInvoker(new JSONObject(fullData));
                } else {
                    if (username != null) {
                        clientClosed();
                           
                    }
                    break;
                }
            } catch (IOException ex) {
                clientClosed();
            }
        }
    }

    /**
     *
     * @param data
     */
    private void managerInvoker(JSONObject object) {

        switch (Integer.parseInt(object.get("functionNumber").toString())) {
            case 1:
                ps.println(registeration.login(object));
                break;
            case 2:
                ps.println(registeration.signup(object));
                break;
            case 3:
                ps.println(registeration.recoverPassword(object));
                break;
            case 4:
                ps.println(listHelper.addList(object));
                break;
            case 5:
                username = object.get("username").toString();
                clientsVector.add(this);
                System.out.println("login " + clientsVector.size());
                DatabaseModel.ChangeUserState(new User(username), "true");
                ps.println(userHelper.getUserData(object));
                break;
            case 6:
                ps.println(userHelper.searchByUserName(object));
                break;
            case 7:
                DatabaseModel.sendFriendRequest(new User(object.get("sender").toString()), new User(object.get("reciever").toString()));
                requestHelper.sendRequestNotification(object.get("reciever").toString(), object.getString("sender"), "7", 0);
                break;
            case 8:
                ps.println(requestHelper.searchRequests(object));
                break;

            case 9:
                if (DatabaseModel.acceptFriendRequest(new User(object.getString("sender")), new User(object.getString("reciever")))) {
                    requestHelper.sendAcceptRequest(object.getString("sender"), object.getString("reciever"));
                }
                break;
            case 10:
                ps.println(requestHelper.rejectFriendRequest(object));
                break;
            case 11:
                if (DatabaseModel.removeFriend(new User(object.getString("username")), new User(object.getString("friend")))) {
                    friendHelper.removeFriend(object.getString("friend"), object.getString("username"));
                }
                break;
            case 12: //add item
                if (DatabaseModel.addItem(new List(object.getInt("listID")), new Item(object.getString("title"), object.getString("desc")))) {
                    itemHelper.sendItemtoUser(object);
                }
                break;
            case 13://remove item
                DatabaseModel.deleteItem(new Item(object.getInt("itemID")));
                break;
            case 14:
                ResultSet rsItem = DatabaseModel.getItemByID(new Item(object.getInt("itemID")));
                ResultSet assignItem = DatabaseModel.getAssignedColl(new Item(object.getInt("itemID")));
                ps.println(itemHelper.sendItemDetails(rsItem, assignItem));
                break;
            case 15:
                username = object.get("username").toString();
                clientsVector.add(this);
                System.out.println("add " + clientsVector.size());
                DatabaseModel.ChangeUserState(new User(username), "true");
                friendHelper.getFriends(DatabaseModel.getUserFriends(new User(username)), username);
                break;
            case 16:
                itemHelper.updateItem(object);
                break;
            case 17:
                DatabaseModel.assignItemToFriend(new Item(object.getInt("itemID")), new User(object.getString("friend")), new User(object.getString("username")));
                requestHelper.sendRequestNotification(object.getString("friend"), object.getString("username"), "17", object.getInt("itemID"));
                break;
            case 18://accept assign
                DatabaseModel.acceptAssignRequest(new Item(object.getInt("id")));
                //send accepted item to user
                ResultSet item = DatabaseModel.getItemByID(new Item(object.getInt("id")));
                ps.println(itemHelper.sendAssignedItem(item));
                break;
            case 19://reject assign
                DatabaseModel.rejectAssignedRequest(new Item(object.getInt("id")));
                break;
            case 21:
                DatabaseModel.updateList(new List(object.getInt("listID"), object.getString("title"), object.getString("status")));
                break;
            case 22:
                DatabaseModel.deleteList(new List(object.getInt("listID")));
                break;

        }
    }

    private void clientClosed() {
        try {
            dis.close();
            ps.close();
            DatabaseModel.ChangeUserState(new User(username), "false");
            ResultSet rs = DatabaseModel.getUserFriends(new User(username));
            friendHelper.updateOffline(rs, username);
            clientsVector.remove(this);
            System.out.println("close " + clientsVector.size());
            stop();
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
