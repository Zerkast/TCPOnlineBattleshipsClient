package application;
import java.util.ArrayList;

import net.IMessageConsumer;

public class ReceiverProtocolManager implements IMessageConsumer{
    private ICommandConsumer consumer;
    private SenderProtocolManager senderProtocolManager;

    public ReceiverProtocolManager(ICommandConsumer consumer, SenderProtocolManager spm) {
        this.consumer = consumer;
        senderProtocolManager = spm;
    }


    public void consumeMessage(String message) {
            System.out.println(message);
            String[] comando = message.split("#");
            if (comando.length==0)  {
                senderProtocolManager.sendError(0); 
            } else {
                String[] argomenti = new String[0];
                if (comando.length>1) argomenti = comando[1].split("&");
                    switch (comando[0]) {
                        case "users":
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            else {
                                ArrayList<String> users = new ArrayList<>();
                                ArrayList<Boolean> userStatuses = new ArrayList<>();
                                for (int i = 0; i < argomenti.length; i++) {
                                    users.add(argomenti[i].split("@")[0]);
                                    userStatuses.add(parseBool(argomenti[i].split("@")[1]));
                                }
                                consumer.updateUserList(users, userStatuses);
                            } 
                            break;
                        case "error": 
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            consumer.error(argomenti[0]);
                            break;
                        case "gameStart":
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            else consumer.gameStart(argomenti[0]);
                            break;
                        case "gameReady":
                            consumer.opponentIsReady();
                            break;
                        case "invite":
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            else consumer.receiveInvite(argomenti[0]);
                            break;
                        case "inviteResponse":
                            if (argomenti.length<2) senderProtocolManager.sendError(1);
                            else {
                                // try {
                                    consumer.receiveInviteResponse(argomenti[0], parseBool(argomenti[1]));
                                // } catch (ParseException e) {
                                    // senderProtocolManager.sendError(1);
                                // }
                            }
                            break;
                        case "shootResponse":
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            else {
                                // try {
                                    consumer.receiveShotResponse(parseBool(argomenti[0]));
                                // } catch (ParseException e) {
                                    // senderProtocolManager.sendError(1);
                                // }
                            }
                            break;
                        case "shoot":
                            if (argomenti.length<2) senderProtocolManager.sendError(1);
                            else {
                                try {
                                    consumer.receiveShot(Integer.parseInt(argomenti[0]), Integer.parseInt(argomenti[1]));
                                } catch (NumberFormatException e) {
                                    senderProtocolManager.sendError(1); 
                                }
                            }
                            break;
                        case "gameEnd":
                                consumer.gameEnd(parseBool(argomenti[0]));    
                            break;
                        case "join":
                            if (argomenti.length==0) senderProtocolManager.sendError(1);
                            else {
                                    consumer.joinResponse(parseBool(argomenti[0]));
                            }
                            break;
                        case "hi":
                            consumer.setConnectionUp();
                            break;
                        default:
                            senderProtocolManager.sendError(0);
                    }
            }
    }


    private boolean parseBool(String s) { //ho fatto questo metodo perche non mi piace che parseBoolean restituisca false anche quando gli arriva una stringa invalida diversa da "false"
        if (s.equals("t")) return true;
        else return false;
    }
}
