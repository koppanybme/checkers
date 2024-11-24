package controller;

import model.*;
import view.*;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameController implements MenuObserver {
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
        view.addObserver(this);
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

    public void onMenuItemClicked(String menuItem) {
        switch (menuItem) {
            case "Save Game":
                saveGame();
                break;
            case "Load Game":
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
}