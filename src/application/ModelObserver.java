package application;

import java.util.List;

public interface ModelObserver {
    void printError(String errorMessage);
    void printMessage(String message);
    void printInvite(String senderUsername);
    void updatePlayerTable(int posX, int posY, int state); //questi due update aggiornano solo i colori nelle griglie
    void updateEnemyTable(int posX, int posY, int state);
    void initializeUI();
    void showLoginScreen();
    void updateUI(List<String> usernames, List<Boolean> isInGame);
    void initializeGamePreparationUI(Integer shipsNumber);
    void updateGamePreparationUI(); //update dopo il ready
    void updateGameUI(boolean isTurn);
    void initializeGameUI();
    //TODO:EVENTUALE INITIALIZE PER QUANDO LA PARTITA EFFETTIVAMENTE PARTE
}
