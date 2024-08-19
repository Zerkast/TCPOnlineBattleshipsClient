package application;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Model implements ICommandConsumer{
    private String username;
    private GameModel currentGameStatus;
    private SenderProtocolManager spm;
    private ModelObserver view;
    private boolean isConnectionUp;
    private boolean isInGame;
    private AtomicBoolean isProgramClosing;
    
    public Model(SenderProtocolManager spm) {
        this.spm = spm;
        isProgramClosing = new AtomicBoolean(false);
        new InnerKeepAliveClient().start(); //attento a ownership exception coi thread
        new InnerKeepAliveServer().start();
    }

    public void sendInvite(String username) {
        spm.sendInvite(username);
    }

    public void sendInviteResponse(String username, boolean response) {
        spm.sendInviteResponse(username, response);
    }

    public void notifyGameReady() {
        spm.sendGameReadyNotification(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public void placeBoat(int posX, int posY) {
        if(!currentGameStatus.placeBoat(posX, posY)) {
            spm.sendGameReadyNotification(currentGameStatus.getOpponentName());
            if (currentGameStatus.getIsOpponentReady()) {
                view.initializeGameUI();
                view.updateGameUI(false);
            } 
            else {
                view.printMessage("You're ready, wait for your opponent");
                view.updateGamePreparationUI();
            }
        }
    }

    public boolean shoot(int posX, int posY) {
        if (currentGameStatus.placeShot(posX, posY)) {
            spm.sendShot(posX, posY, currentGameStatus.getOpponentName());
            return true;
        }
        return false;
    }

    public void setObserver(ModelObserver obs) {
        view = obs;
        view.initializeUI();
        view.showLoginScreen();
    }
    @Override
    public void updateUserList(List<String> connectedUsers, List<Boolean> usersStatus) {
        if (isInGame && !connectedUsers.contains(currentGameStatus.getOpponentName())) gameEnd(false);
        for (int i = 0; i < connectedUsers.size(); i++) {
            if (connectedUsers.get(i).equals(username)) {
                connectedUsers.remove(i);
                usersStatus.remove(i);
            }
        }
        view.updateUI(connectedUsers, usersStatus);
        setConnectionUp();
    }

    @Override
    public void gameEnd(boolean isVictory) {
        isInGame = false;
        currentGameStatus = null;
        view.initializeUI();
        String message = "The game has finished.";
        if (isVictory) message += "You have won";
        else message += "Your opponent has disconnected";
        spm.requestRoomStatus(username);
        view.printMessage(message);
    }

    @Override
    public void receiveInviteResponse(String replierName, boolean response) {
        if (response && !isInGame) {
            spm.sendGameStart(replierName);
            gameStart(replierName);
        }
        setConnectionUp();
    }

    @Override
    public void opponentIsReady() {
        currentGameStatus.setIsOpponentReady(true);
        if (currentGameStatus.getIsPlayerReady()) {
            view.initializeGameUI();
            view.updateGameUI(true);
        }
        setConnectionUp();
    }

    @Override
    public void receiveShot(int posX, int posY) {
        spm.sendShotOutcome(currentGameStatus.checkEnemyShot(posX, posY), currentGameStatus.getOpponentName());
        if (currentGameStatus.getRemainingShipsPieces()==0) {
            spm.sendGameEnd(currentGameStatus.getOpponentName(), true);
            view.initializeUI();
            view.printMessage("You lost to " + currentGameStatus.getOpponentName());
            spm.requestRoomStatus(username);
        }
        setConnectionUp();
    }

    @Override
    public void receiveShotResponse(boolean hasHit) {
        currentGameStatus.confirmShot(hasHit);
        setConnectionUp();
    }

    @Override
    public void error(String errorMessage) {
        view.printError(errorMessage);
    }
    
    @Override
    public void gameStart(String username) {
        if (!isInGame) {
            isInGame = true;
            currentGameStatus = new GameModel(username, view);
        } else {
            spm.sendError(0);
        }
    }

    public void quit() {
        if (isInGame) spm.sendGameEnd(currentGameStatus.getOpponentName(), false);
        isProgramClosing.set(true);
        spm.sendQuit();
    }

    @Override
    public void receiveInvite(String username) {
        if (!isInGame) {
            view.printInvite(username);
        }
    }
   
    @Override
    public void joinResponse(boolean accepted) {
        if (!accepted) {
            view.showLoginScreen();
            view.printError(username);
        } 
    }


    public void join(String usernameChosen) {
        username = usernameChosen;
        spm.sendJoinRequest(usernameChosen);
    }

    @Override
    public void setConnectionUp() {
        isConnectionUp = true;
    }
    public class InnerKeepAliveClient extends Thread{
        @Override
        public void run() {
                while (!isProgramClosing.get()) {
                try {
                    sleep(13000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spm.sendKeepAlive();
                }
        }
    }
     public class InnerKeepAliveServer extends Thread{
        @Override
        public void run() {
            // synchronized(this) {
            while (!isProgramClosing.get()) {
                try {
                    sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isConnectionUp) {
                    if (!isProgramClosing.get()) {
                        view.printError("Connection lost");
                        quit();
                    }
                }
                isConnectionUp=false;
            }
        }
    }
}