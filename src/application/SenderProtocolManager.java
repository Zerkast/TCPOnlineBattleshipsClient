package application;
import net.Sender;

import java.util.HashMap;
public class SenderProtocolManager {
    private HashMap<Integer, String> errors;
    private Sender sender;

    public SenderProtocolManager(Sender sender) {
        this.sender = sender;
        errors =  new HashMap<>();
        errors.put(0, "unknown command");
        errors.put(1, "illegal arguments");
        // errors.put(2, "username already taken");
        // errors.put(3, "game already started"); 
        // errors.put(4, "not allowed");
    }

    public void sendOutcome(String command, boolean outcome) {
        sender.send(command+"#"+translateBoolean(outcome));
    }

    public void sendError(int errorCode) {
        sender.send(errors.get(errorCode));
    }

    public void sendJoinRequest(String username) {
        sender.send("join#"+username);
    }

    public void close() {
        sender.close();
    }
    public void sendShot(int posX, int posY, String username) {
        sender.send("shoot#"+posX+"&"+posY+"&"+username);
    }

    public void sendShotOutcome(boolean outcome, String username) {
        sender.send("shootResponse#"+translateBoolean(outcome)+"&"+username);
    }

    public void sendInvite(String username) {
        sender.send("invite#"+username);
    }

    public void sendInviteResponse(String username, boolean response) {
        sender.send("inviteResponse#" + username + "&"+translateBoolean(response));
    }

    public void sendGameReadyNotification(String username) {
        sender.send("gameReady#"+username);
    }

    public void sendKeepAlive() {
        sender.send("hi");
    }

    public void sendGameStart(String username) {
        sender.send("gameStart#"+username);
    }

    public void requestRoomStatus(String username) {
        sender.send("checkUsers#"+username);
    }

    public void sendQuit() {
        sender.send("quit#");
        sender.close();
    }

    public void sendGameEnd(String username, boolean isVictory) {
        sender.send("gameEnd#"+username+"&"+translateBoolean(isVictory));
    }

    private String translateBoolean(boolean bool) {
        String msg = "f";
        if (bool) msg = "t";
        return msg;
    }

}
