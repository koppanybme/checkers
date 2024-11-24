package controller;

import model.*;
import view.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameController implements java.io.Serializable, MenuObserver {
    private List<ControllerObserver> observers = new ArrayList<>();
    private PieceView selectedPieceView;
    private GameView view;
    private GameState model;

    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.newGame();
    }

    public void newGame() {
        model = new GameState();
        model.initializeBoard();
        view = new GameView(new BoardView(model.getBoard()));
        // Set the observers    
        view.addObserver(this);
        model.addObserver(view);
        addObserver(model);
        notifyObservers();
        model.notifyObservers();
        System.out.println("New game started.");
    }

    public void pieceSelected(PieceView pieceView) {
        if (selectedPieceView != null) {
            selectedPieceView.setSelected(false);
            selectedPieceView.repaint();
        }
        selectedPieceView = pieceView;
        selectedPieceView.setSelected(true);
        selectedPieceView.repaint();
    }

    public void addObserver(ControllerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ControllerObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (ControllerObserver observer : observers) {
            observer.update();
        }
    }
    public void onMenuItemClicked(String menuItem) {
        switch (menuItem) {
            case "Save Game":
                saveGame();
                break;
            case "Load Game":
                loadGame();
                break;
            case "New Game":
                newGame();
                break;
            default:
                break;
        }
    }

    public void saveGame() {
        try (FileOutputStream fileOut = new FileOutputStream("gameState.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(model);
            System.out.println("Game state saved to gameState.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (FileInputStream fileIn = new FileInputStream("gameState.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            model = (GameState) in.readObject();
            view = new GameView(new BoardView(model.getBoard()));
            view.addObserver(this);
            model.addObserver(view);
            addObserver(model);
            notifyObservers();
            System.out.println("Game state loaded from gameState.ser");            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}