package application;

public class GameModel {
    private String opponentName;
    private int playerTable[][];
    private int opponentTable[][];
    private int remainingShipsPieces;
    private int shipsPlaced;
    private int lastShot[];
    private Boolean isPlayerReady;
    private Boolean isOpponentReady;
    private ModelObserver view;

    public GameModel(String opponentName, ModelObserver view) {
        this.opponentName = opponentName;
        remainingShipsPieces = 10;//1 da 4, 2 da 3, 2 da 2, 4 da 1
        shipsPlaced = 0;
        playerTable = new int[10][10];
        opponentTable = new int[10][10];
        lastShot = new int[2];
        isOpponentReady = false;
        isPlayerReady = false;
        this.view = view;
        view.initializeGamePreparationUI(remainingShipsPieces);
    }

    public boolean placeBoat(int x, int y) {
        if (!isPlayerReady) {
            playerTable[y][x] = 1;
            shipsPlaced++;
            if (shipsPlaced==remainingShipsPieces) isPlayerReady=true;
            view.updatePlayerTable(x, y, 1);
        }
        return !isPlayerReady;
    }

    public boolean placeShot(int x, int y) {
        if (opponentTable[y][x]!=0) return false;
        lastShot[0] = y;
        lastShot[1] = x;
        opponentTable[y][x]=-2;
        view.updateGameUI(false);
        return true;
    }

    public void confirmShot(boolean hasHit) {
        if (hasHit) opponentTable[lastShot[0]][lastShot[1]] = 2;
        else opponentTable[lastShot[0]][lastShot[1]] = -1;
        System.out.println(lastShot[1] + " " + lastShot[0] + " " + opponentTable[lastShot[0]][lastShot[1]]);
        view.updateEnemyTable(lastShot[1], lastShot[0], opponentTable[lastShot[0]][lastShot[1]]);
    }

    public boolean checkEnemyShot(int x, int y) {
        view.updateGameUI(true);
        if (playerTable[y][x]==1) {
            remainingShipsPieces--;
            playerTable[y][x] = 2;
            view.updatePlayerTable(x, y, 2);
            return true;
        } else {
            playerTable[y][x] = -1;
            view.updatePlayerTable(x, y, -1);
            return false;
        }
    }

    public void setIsOpponentReady(boolean isOpponentReady) {
        this.isOpponentReady = isOpponentReady;
    }

    public boolean getIsOpponentReady() {
        return isOpponentReady;
    }
    public String getOpponentName() {
        return opponentName;
    }
    public Boolean getIsPlayerReady() {
        return isPlayerReady;
    }
    public int getRemainingShipsPieces() {
        return remainingShipsPieces;
    }
}
