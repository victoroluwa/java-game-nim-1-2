import java.util.Stack;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NimGame {
    private Player humanPlayer;
    private Player computerPlayer;
    private boolean isHumanTurn;
    private int marbleSize;
    private Stack<Integer> marbleHistory = new Stack<>();
    private Stack<Boolean> turnHistory = new Stack<>();
    private List<GameObserver> observers = new ArrayList<>();
    /**
     * NimGame.
     *
     * @author  Ifesinachi Paschal Obiors
     * @version 3.0 
     */

    public NimGame(Player humanPlayer, Player computerPlayer) {
        this.marbleSize = 10;
        this.humanPlayer = humanPlayer;
        this.computerPlayer = computerPlayer;
        this.isHumanTurn = true;
    }

    public void assignMove(int removeAmount) {
        // The move has to be validated
        if (removeAmount < 1 || removeAmount > 2 || removeAmount > marbleSize) {
            throw new IllegalArgumentException("Invalid move: Must remove 1 or 2 marbles and cannot exceed remaining marbles");
        }
        
        // Save current state before move
        marbleHistory.push(marbleSize);
        turnHistory.push(isHumanTurn);
        marbleSize -= removeAmount;
        isHumanTurn = !isHumanTurn;
        notifyObservers();
    }
    
    public void makeComputerMoveIfNeeded() {
        if (!isHumanTurn) {
            int move = computerPlayer.makeMove(marbleSize);
            // Validate computer move
            if (move < 1 || move > 2 || move > marbleSize) {
                move = Math.min(marbleSize, 1); // Default to safe move if computer strategy returns invalid move
            }
            assignMove(move);
        }
    }

    public boolean checkWinner() {
        return marbleSize <= 0;
    }

    public void saveGame() {
        try (FileWriter writer = new FileWriter("nim_save.txt")) {
            writer.write(marbleSize + "\n");
            writer.write(isHumanTurn + "\n");

            writer.write(String.join(",", marbleHistory.stream()
                    .map(String::valueOf).toArray(String[]::new)) + "\n");
            writer.write(String.join(",", turnHistory.stream()
                    .map(String::valueOf).toArray(String[]::new)) + "\n");

            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader("nim_save.txt"))) {
            marbleSize = Integer.parseInt(reader.readLine());
            isHumanTurn = Boolean.parseBoolean(reader.readLine());

            String[] marbleHistoryValues = reader.readLine().split(",");
            marbleHistory.clear();
            notifyObservers();
            if (!marbleHistoryValues[0].isEmpty()) {
                Arrays.stream(marbleHistoryValues)
                      .map(Integer::parseInt)
                      .forEach(marbleHistory::push);
            }

            String[] turnHistoryValues = reader.readLine().split(",");
            turnHistory.clear();
            if (!turnHistoryValues[0].isEmpty()) {
                Arrays.stream(turnHistoryValues)
                      .map(Boolean::parseBoolean)
                      .forEach(turnHistory::push);
            }

            System.out.println("Game loaded. Marbles: " + marbleSize + ", Turn: " + (isHumanTurn ? "Human" : "Computer"));
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }

    public void undoLastMove() {
        if (!marbleHistory.isEmpty() && !turnHistory.isEmpty()) {
            marbleSize = marbleHistory.pop();
            isHumanTurn = turnHistory.pop();
            notifyObservers();
            System.out.println("Last move undone. Current marbles: " + marbleSize);
        } else {
            System.out.println("No moves to undo.\n");
        }
    }
    
    public void resetGame() {
        Random random = new Random();
        this.marbleSize = 10; 
        this.isHumanTurn = random.nextBoolean();
        marbleHistory.clear();
        turnHistory.clear();
        notifyObservers();
        System.out.println("Game reset. Starting with " + marbleSize + " marbles. " +
                           (isHumanTurn ? "Human" : "Computer") + " goes first.");
    }
    
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    public Player getComputerPlayer() {
        return computerPlayer;
    }

    public int getMarbleSize() {
        return marbleSize;
    }

    public boolean isHumanTurn() {
        return isHumanTurn;
    }
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update();
        }
    }

    public Stack<Integer> getMarbleHistory() {
        return marbleHistory;
    }
    
    public Stack<Boolean> getTurnHistory() {
        return turnHistory;
    }
    
    public void setMarbleSize(int size) {
        this.marbleSize = size;
    }
    
    public void setHumanTurn(boolean isHuman) {
        this.isHumanTurn = isHuman;
    }
    
    public void forceNotify() {
        notifyObservers();
    }
}