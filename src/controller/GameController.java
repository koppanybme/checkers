package controller;

import model.*;
import view.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameController implements java.io.Serializable, MenuObserver, PieceObserver {
    private List<ControllerObserver> observers = new ArrayList<>();
    private GameView view;
    private GameState model;

    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.newGame();
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

    @Override
    public void onPieceClicked(int row, int col) {
        System.out.println("Piece clicked at row " + row + ", col " + col);
        Board b = model.getBoard();
        Piece p = b.getPieceAt(row, col);
        List<Point> legalMoves = new ArrayList<>();
        if(
            //Check if user's own piece
            p.getColor().equals(Color.WHITE) && model.getTurn().equals("white") ||
            p.getColor().equals(Color.BLACK) && model.getTurn().equals("black")
        ) {
        } else {
            System.out.println("Selected " + p.getColor() + " but was " + model.getTurn() + "'s turn");
            return;
        }
        if (p != null) {
            if(p.isQueen()){
                System.out.println("Queen piece clicked");
            } else{
                System.out.println("Regular piece clicked");
                //check if a single step is possible
                try {
                    if (b.getPieceAt(row + 1, col + 1) == null) {
                        legalMoves.add(new Point(row + 1, col + 1));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't step off the board");
                }
                try {
                    if (b.getPieceAt(row + 1, col - 1) == null) {
                        legalMoves.add(new Point(row + 1, col - 1));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't step off the board");
                }
                try {
                    if (b.getPieceAt(row - 1, col - 1) == null) {
                        legalMoves.add(new Point(row - 1, col - 1));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't step off the board");
                }
                try {
                    if (b.getPieceAt(row - 1, col + 1) == null) {
                        legalMoves.add(new Point(row - 1, col + 1));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't step off the board");
                }
                //check if a jump is possible                
                try {
                    Piece neighbor = b.getPieceAt(row + 1, col + 1);
                    Piece jump = b.getPieceAt(row + 2, col + 2);
                    //check if the neighbor is an opponent and the jump is empty
                    if (neighbor != null && jump == null && !neighbor.getColor().equals(p.getColor())) {
                        legalMoves.add(new Point(row + 2, col + 2));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't jump off the board");
                }
                try {
                    Piece neighbor = b.getPieceAt(row + 1, col - 1);
                    Piece jump = b.getPieceAt(row + 2, col - 2);
                    //check if the neighbor is an opponent and the jump is empty
                    if (neighbor != null && jump == null && !neighbor.getColor().equals(p.getColor())) {
                        legalMoves.add(new Point(row + 2, col - 2));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't jump off the board");
                }
                try {
                    Piece neighbor = b.getPieceAt(row - 1, col - 1);
                    Piece jump = b.getPieceAt(row - 2, col - 2);
                    //check if the neighbor is an opponent and the jump is empty
                    if (neighbor != null && jump == null && !neighbor.getColor().equals(p.getColor())) {
                        legalMoves.add(new Point(row - 2, col - 2));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't jump off the board");
                }
                try {
                    Piece neighbor = b.getPieceAt(row - 1, col + 1);
                    Piece jump = b.getPieceAt(row - 2, col + 2);
                    //check if the neighbor is an opponent and the jump is empty
                    if (neighbor != null && jump == null && !neighbor.getColor().equals(p.getColor())) {
                        legalMoves.add(new Point(row - 2, col + 2));
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Can't jump off the board");
                }                
                System.out.println("Legal moves: " + legalMoves);
                view.getBoardView().updateLegalMoves(legalMoves);
            }
        }
    }

    @Override
    public void onPieceMoved(Point from, Point to) {
        System.out.println("Piece moved from " + from + " to " + to);
        Board b = model.getBoard();
        Piece p = b.getPieceAt(from.x, from.y);
        b.setPieceAt(from.x, from.y, null);
        b.setPieceAt(to.x, to.y, p);
        view.getBoardView().updatePieceView(from, to, p);
        view.getBoardView().updateLegalMoves(new ArrayList<>());
        notifyObservers();
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
            if (view == null) {
                view = new GameView(new BoardView(model.getBoard()));
                view.addObserver(this);
                model.addObserver(view);
                addObserver(model);                
            } else {
                view.updateBoardView(new BoardView(model.getBoard()));
            }
            view.getBoardView().addObserver(this);
            model.addObserver(view.getBoardView());
            notifyObservers();
            System.out.println("Game state loaded from gameState.ser");            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newGame() {
        model = new GameState();
        model.initializeBoard();
        if (view == null) {
            view = new GameView(new BoardView(model.getBoard()));
            view.addObserver(this);
            model.addObserver(view);
            addObserver(model);
        } else {            
            view.updateBoardView(new BoardView(model.getBoard()));
        }
        view.getBoardView().addObserver(this);
        model.addObserver(view.getBoardView());
        notifyObservers();
        model.notifyObservers();
        System.out.println("New game started.");
    }
}