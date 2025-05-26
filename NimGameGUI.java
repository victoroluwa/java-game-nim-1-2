import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

/**
 * GUI class NimGameGUI.
 *
 * @author  Ifesinachi Paschal Obiors
 * @version 3.0
 */
public class NimGameGUI extends JFrame implements GameObserver {
    private NimGame game;
    private JLabel marbleLabel;
    private JLabel turnLabel;
    private JTextArea log;
    private MarblePanel marblePanel;
    private JButton take1;
    private JButton take2;
    
    public static void main(String[] args) {
        showGameModeDialog();
    }
    
    private static void showGameModeDialog() {
        String[] options = {"Random Strategy", "Smart Strategy"};
        int choice = JOptionPane.showOptionDialog(null,
            "Choose Computer Strategy:",
            "Game Mode Selection",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
            
        Player human = new Player("Human Player", new HumanUserStrategy());
        Player computer;
        
        if (choice == 0) {
            computer = new Player("Computer Player", new RandomStrategy());
        } else {
            computer = new Player("Computer Player", new SmartStrategy());
        }
        
        NimGame game = new NimGame(human, computer);
        NimGameGUI gui = new NimGameGUI(game);
        gui.setVisible(true);
    }

    public NimGameGUI(NimGame game) {
        this.game = game;
        this.game.addObserver(this);

        setTitle("1-2 Nim Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        marbleLabel = new JLabel();
        marblePanel = new MarblePanel();
        marblePanel.setPreferredSize(new Dimension(300, 150));
    
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        
        centerPanel.add(marblePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        turnLabel = new JLabel();
        log = new JTextArea(5, 20);
        log.setEditable(false);
        centerPanel.add(new JScrollPane(log), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        statusPanel.add(marbleLabel);
        statusPanel.add(turnLabel);

        JPanel buttonPanel = new JPanel();
        take1 = new JButton("Take 1");
        take2 = new JButton("Take 2");
        JButton undo = new JButton("Undo");
        JButton reset = new JButton("Reset");
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");

        take1.addActionListener(e -> {
            try {
                game.assignMove(1);
                log.append("Human takes 1 marble\n");
            } catch (IllegalArgumentException ex) {
                log.append("Invalid move: " + ex.getMessage() + "\n");
            }
        });

        take2.addActionListener(e -> {
            try {
                game.assignMove(2);
                log.append("Human takes 2 marbles\n");
            } catch (IllegalArgumentException ex) {
                log.append("Invalid move: " + ex.getMessage() + "\n");
            }
        });
        
        undo.addActionListener(e -> game.undoLastMove());
        reset.addActionListener(e -> game.resetGame());
        
        save.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(game.getMarbleSize() + "\n");
                    writer.write(game.isHumanTurn() + "\n");

                    writer.write(String.join(",", game.getMarbleHistory().stream()
                            .map(String::valueOf).toArray(String[]::new)) + "\n");
                    writer.write(String.join(",", game.getTurnHistory().stream()
                            .map(String::valueOf).toArray(String[]::new)) + "\n");

                    log.append("Game saved to: " + file.getName() + "\n");
                } catch (IOException ex) {
                    log.append("Error saving game: " + ex.getMessage() + "\n");
                }
            }
        });

        load.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    game.setMarbleSize(Integer.parseInt(reader.readLine()));
                    game.setHumanTurn(Boolean.parseBoolean(reader.readLine()));

                    String[] marbleHistoryValues = reader.readLine().split(",");
                    game.getMarbleHistory().clear();
                    if (!marbleHistoryValues[0].isEmpty()) {
                        Arrays.stream(marbleHistoryValues)
                                .map(Integer::parseInt)
                                .forEach(game.getMarbleHistory()::push);
                    }

                    String[] turnHistoryValues = reader.readLine().split(",");
                    game.getTurnHistory().clear();
                    if (!turnHistoryValues[0].isEmpty()) {
                        Arrays.stream(turnHistoryValues)
                                .map(Boolean::parseBoolean)
                                .forEach(game.getTurnHistory()::push);
                    }

                    log.append("Game loaded from: " + file.getName() + "\n");
                    game.forceNotify(); 
                } catch (IOException | NumberFormatException ex) {
                    log.append("Error loading game: " + ex.getMessage() + "\n");
                }
            }
        });

        buttonPanel.add(take1);
        buttonPanel.add(take2);
        buttonPanel.add(undo);
        buttonPanel.add(reset);
        buttonPanel.add(save);
        buttonPanel.add(load);

        add(statusPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        update();
    }

    private void playerMove(int marbles) {
        if (game.isHumanTurn()) {
            try {
                game.assignMove(marbles);
                log.append("Human takes " + marbles + "\n");

                if (!game.checkWinner()) {
                    int compMove = game.getComputerPlayer().makeMove(game.getMarbleSize());
                    game.assignMove(compMove);
                    log.append("Computer takes " + compMove + "\n");
                }
            } catch (IllegalArgumentException ex) {
                log.append("Invalid move: " + ex.getMessage() + "\n");
            }
        }
        if (game.checkWinner()) {
            log.append((game.isHumanTurn() ? "Computer" : "Human") + " wins!\n");
        }
    }

    @Override
    public void update() {
        int count = game.getMarbleSize();
        marblePanel.setMarbleCount(count);
        if (!game.isHumanTurn()) {
            Timer timer = new Timer(1000, e -> game.makeComputerMoveIfNeeded());
            timer.setRepeats(false);
            timer.start();
        }

        if (game.checkWinner()) {
            String winner = game.isHumanTurn() ? game.getComputerPlayer().getName() : game.getHumanPlayer().getName();
            log.append("Game Over! Winner: " + winner + "\n");
            take1.setEnabled(false);
            take2.setEnabled(false);
        } else {
            take1.setEnabled(game.isHumanTurn());
            take2.setEnabled(game.isHumanTurn() && game.getMarbleSize() >= 2);
        }
        marbleLabel.setText("Marbles left: " + game.getMarbleSize());
        turnLabel.setText("Turn: " + (game.isHumanTurn() ? game.getHumanPlayer().getName() : game.getComputerPlayer().getName()));
    }
}