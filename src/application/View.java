package application;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class View extends JFrame implements ModelObserver{
    private Controller controller;
    private JButton playerGrid[][];
    private JButton opponentGrid[][];
    JPanel mainPanel;
    private JLabel text1;
    private JLabel text2;
    private final int rows = 10;
    private final int cols = 10;
    private int shipsNumber;
    private JFrame tempFrame;
    // private final int buttWidth = 30;
    // private final int buttHeight= 30;
    public View(Controller controller) {
        this.controller = controller;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    controller.quit();
                    dispose();
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void initializeUI() {
        setVisible(false);
        getContentPane().removeAll();
        revalidate();
        repaint();
        setTitle("Battleships - Lobby");
        mainPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        this.add(scrollPane);
        this.pack();
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    @Override
    public void initializeGamePreparationUI(Integer shipsNumber) {
        setVisible(false);
        getContentPane().removeAll();
        revalidate();
        repaint();
        this.shipsNumber = shipsNumber;
        setTitle("Battleships - Preparation");
        playerGrid = new JButton[rows][cols];
        JPanel generalCenter = new JPanel();
        JPanel generalSouth = new JPanel();
        JPanel center = new JPanel();
        JLabel nums[] = new JLabel[rows];
        JLabel letters[] = new JLabel[cols];
        JPanel left = new JPanel();
        JPanel north = new JPanel();
        generalSouth.setLayout(new BorderLayout());
        generalCenter.setLayout(new GridLayout(2, 1));
        left.setLayout(new GridLayout(rows, 1));
        north.setLayout(new GridLayout(1, cols));
        center.setLayout(new GridLayout(rows, cols));
        text1 = new JLabel("Place your ships");
        text1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        text2 = new JLabel(shipsNumber + " ship remaining");
        text2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        generalCenter.add(text1);
        generalCenter.add(text2);
        for (int i = 0; i < cols; i++) {
            letters[i] = new JLabel(String.valueOf((char)(i + (int)'A')), SwingConstants.CENTER);
            north.add(letters[i]);
        }
        for (int i = 0; i < rows; i++) {
            nums[i] = new JLabel(String.valueOf(i+1));
            left.add(nums[i]);
            for (int j = 0; j < cols; j++) {
                playerGrid[i][j] = new JButton(); //creiamo il rettangolino
                playerGrid[i][j].setPreferredSize(new Dimension(40, 40));
                playerGrid[i][j].setBackground(Color.WHITE);
                playerGrid[i][j].setOpaque(true);
                playerGrid[i][j].setEnabled(true);
                int currentI = i;
                int currentJ = j; //copie da usare nell'action event funzionale
                playerGrid[i][j].addActionListener((ActionEvent e) -> {
                    playerGrid[currentI][currentJ].setEnabled(false);
                    controller.placeBoat(currentJ, currentI);
                    decreaseShipsNumber();
                    text2.setText(getShipsNumber() + " ships remaining");
                });
                center.add(playerGrid[i][j]); //mettiamo il rettangolino nel pannello
            }
        }
        generalSouth.add(center);
        generalSouth.add(left, BorderLayout.WEST);
        generalSouth.add(north, BorderLayout.NORTH);
        this.add(generalCenter, BorderLayout.CENTER);
        this.add(generalSouth, BorderLayout.SOUTH);
        this.pack();
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void updateGamePreparationUI() {
        setTitle("Battleships - waiting for opponent");
        text1.setText("You're ready, wait for your opponent");
        text1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        text2.setText("good luck");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                playerGrid[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void initializeGameUI() {
        setTitle("Battleships - In Game");
        JOptionPane.showMessageDialog(this, "The game has started");
        opponentGrid = new JButton[rows][cols];
        JLabel text3 = new JLabel();
        text1.setText("");
        text1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        text2.setText("<html>BLUE - Missed shot<br>WHITE - Water<br>GREEN - Your ship<br>RED - Destroyed ship</html>");
        text2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        JPanel generalNorth = new JPanel();
        JPanel generalRight = new JPanel();
        JPanel center = new JPanel();
        JLabel nums[] = new JLabel[rows];
        JLabel letters[] = new JLabel[cols];
        JPanel left = new JPanel();
        JPanel north = new JPanel();
        generalNorth.setLayout(new BorderLayout());
        generalRight.setLayout(new BorderLayout());
        left.setLayout(new GridLayout(rows, 1));
        north.setLayout(new GridLayout(1, cols));
        center.setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                playerGrid[i][j].setEnabled(false);
            }
        }
        for (int i = 0; i < cols; i++) {
            letters[i] = new JLabel(String.valueOf((char)(i + (int)'A')), SwingConstants.CENTER);
            north.add(letters[i]);
        }
        for (int i = 0; i < rows; i++) {
            nums[i] = new JLabel(String.valueOf(i+1));
            left.add(nums[i]);
            for (int j = 0; j < cols; j++) {
                opponentGrid[i][j] = new JButton(); //creiamo il rettangolino
                opponentGrid[i][j].setPreferredSize(new Dimension(40, 40));
                opponentGrid[i][j].setBackground(Color.WHITE);
                opponentGrid[i][j].setOpaque(true);
                opponentGrid[i][j].setEnabled(true);
                int currentI = i;
                int currentJ = j; //copie da usare nell'action event funzionale
                opponentGrid[i][j].addActionListener((ActionEvent e) -> {
                    opponentGrid[currentI][currentJ].setEnabled(false);
                    if (controller.shoot(currentJ, currentI)) {
                        text1.setText("it's not your turn");
                        freezeOpponentGrid();
                    } else {
                        JOptionPane.showMessageDialog(this, "You already shot there");
                    }
                });
                center.add(opponentGrid[i][j]); //mettiamo il rettangolino nel pannello
            }
        }
        generalRight.add(text3);
        generalNorth.add(center);
        generalNorth.add(left, BorderLayout.WEST);
        generalNorth.add(north, BorderLayout.NORTH);
        this.add(generalNorth, BorderLayout.NORTH);
        this.add(generalRight, BorderLayout.EAST);
        this.pack();
    }

    @Override
    public void printError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        if (errorMessage.equals("Connection lost")) {
            this.dispose();
            tempFrame.dispose();
        }
    }

    @Override
    public void printMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public void printInvite(String senderUsername) {
        this.setVisible(false);
        int reply = JOptionPane.showConfirmDialog(this, senderUsername + " invited you to play", "invite" , JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            controller.sendInviteResponse(senderUsername, true);
        } else {
            controller.sendInviteResponse(senderUsername, false);
        }
        this.setVisible(true); //forse va nei due rami dell'if
    }

    @Override
    public void updatePlayerTable(int posX, int posY, int state) {
        switch (state) {
            case -1:
                playerGrid[posY][posX].setBackground(Color.BLUE);
                break;
            case 1:
                playerGrid[posY][posX].setBackground(Color.GREEN);
                break;
            case 2:
                playerGrid[posY][posX].setBackground(Color.RED);
                break;
            default: //0 e -2
                playerGrid[posY][posX].setBackground(Color.WHITE);
        }
    }

    @Override
    public void updateEnemyTable(int posX, int posY, int state) {
        switch (state) {
            case -1:
                opponentGrid[posY][posX].setBackground(Color.BLUE);
                break;
            case 1:
                opponentGrid[posY][posX].setBackground(Color.GREEN);
                break;
            case 2:
                opponentGrid[posY][posX].setBackground(Color.RED);
                break;
            default: //0 e -2
                opponentGrid[posY][posX].setBackground(Color.WHITE);
        }
    }

    @Override
    public void updateUI(List<String> usernames, List<Boolean> isInGame) {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        if (usernames.isEmpty()) {
            JLabel text = new JLabel("The room is empty", JLabel.CENTER); 
            mainPanel.add(text); 
            mainPanel.repaint();
        }
        for (int i = 0;i < usernames.size();i++) {
            JPanel entry = new JPanel(new GridLayout(1, 2));
            JLabel username = new JLabel();
            entry.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
            username.setText(usernames.get(i));
            entry.add(username);
            JButton invite = new JButton("Invite");
            invite.addActionListener((ActionEvent e) -> controller.sendInvite(username.getText()));
            if (isInGame.get(i)) {
                invite.setEnabled(false);
                invite.setText("not available");
            }
            entry.add(invite);
            entry.setPreferredSize(new Dimension(300, 100));
            mainPanel.add(entry);
        }
        this.setFocusable(true);
        this.setVisible(true);
    }

    private void decreaseShipsNumber() {
        shipsNumber--;
    }

    private int getShipsNumber() {
        return shipsNumber;
    }

    private void freezeOpponentGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                opponentGrid[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void updateGameUI(boolean isTurn) {
        if (isTurn) text1.setText("it's your turn");
        else text1.setText("It's not your turn");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                opponentGrid[i][j].setEnabled(isTurn);
            }
        }
    }

    @Override
    public void showLoginScreen() {
        tempFrame = new JFrame("Login");
        tempFrame.setSize(300, 120);
        JTextField usernameField = new JTextField();
        usernameField.setSize(200, 20);
        usernameField.setLocation(50, 20);
        JButton loginButton = new JButton("Join");
        loginButton.setSize(100, 20);
        loginButton.setLocation(100, 60);
        loginButton.addActionListener((ActionEvent e) -> {
            String usernameWritten = usernameField.getText();
            if (!usernameWritten.replaceAll("[a-zA-Z0-9]", "").isBlank() || usernameWritten.isBlank()) {
                JOptionPane.showMessageDialog(this, "Special characters and blank spaces in username are not allowed");
            } else {
                controller.join(usernameWritten);
                tempFrame.dispose();
            }
        } );
        tempFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.quit();
                dispose();
            }
        });
        tempFrame.add(usernameField);
        tempFrame.add(loginButton);
        tempFrame.setLayout(null);
        tempFrame.setLocationRelativeTo(null);
        tempFrame.setVisible(true);
        tempFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        tempFrame.setFocusable(true);
    }

    
}
