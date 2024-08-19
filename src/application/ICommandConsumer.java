package application;

import java.util.List;

public interface ICommandConsumer {
    public void updateUserList(List<String> connectedUsers, List<Boolean> usersStatus);
    public void gameEnd(boolean isVictory);
    public void gameStart(String username);
    public void receiveInviteResponse(String replierName, boolean response);
    public void receiveInvite(String username);
    public void opponentIsReady(); //METTI A FALSE IL FLAG PLAYERTURN IN GAMEMODEL
    public void receiveShot(int posX, int posY);
    public void receiveShotResponse(boolean hasHit);
    public void joinResponse(boolean accepted);
    public void setConnectionUp(); 
    public void error(String errorMessage);
}
