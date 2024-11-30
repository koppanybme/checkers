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
import java.util.Iterator;

public class GameController implements java.io.Serializable, MenuObserver, PieceObserver {
    private List<ControllerObserver> observers = new ArrayList<>();
    private GameView view;
    private GameState model;
    private boolean previouslyJumped = false;
    private boolean isFirstMove = true;
    private PieceView boundPieceView;
    private int boundRow;
    private int boundCol;

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
        BoardView bw = view.getBoardView();
        boolean shouldPlayerJump = canPlayerJump();
        if(
            //Check if user's own piece
            p.getColor().equals(Color.WHITE) && model.getTurn().equals("white") ||
            p.getColor().equals(Color.BLACK) && model.getTurn().equals("black")
        ) {
            List<Point> legalMoves = new ArrayList<>();
            legalMoves = getLegalMoves(b, p, row, col);
            if(isFirstMove){                
                if(shouldPlayerJump){                
                    legalMoves.removeIf(move -> Math.abs(move.x - row) != 2 || Math.abs(move.y - col) != 2);
                }      
            } else {
                //If not first move, can only jump with bound piece
                legalMoves.removeIf(move -> Math.abs(move.x - boundRow) != 2 || Math.abs(move.y - boundCol) != 2);
            }
            bw.updateLegalMoves(legalMoves);            
            notifyObservers();
        } else {
            System.out.println("Selected " + p.getColor() + " but was " + model.getTurn() + "'s turn");
            bw.getSelectedPieceView().setSelected(false);
            notifyObservers();
            return;
        }
    }

    public boolean canPlayerJump(){
        Board b = model.getBoard();
        //Iterate over every piece
        for(int i = 0; i < b.getRows(); i++){
            for(int j = 0; j < b.getCols(); j++){
                Piece p = b.getPieceAt(i, j);
                if(p == null){
                    continue;
                }
                if(
                    //Check if user's own piece
                    p.getColor().equals(Color.WHITE) && model.getTurn().equals("white") ||
                    p.getColor().equals(Color.BLACK) && model.getTurn().equals("black")
                ){
                    List<Point> jumpMoves = getLegalMoves(b, p, i, j);
                    Iterator<Point> iterator = jumpMoves.iterator();
                    while (iterator.hasNext()) {
                        Point point = iterator.next();
                        if (Math.abs(point.x - i) != 2 || Math.abs(point.y - j) != 2) {
                            iterator.remove();
                        }
                    }                
                    if(!jumpMoves.isEmpty()){
                        return true;
                    }
                } else {
                    //If not own piece, skip
                    continue;
                }                
            }
        }
        return false;
    }

    public List<Point> getLegalMoves(Board b, Piece p, int row, int col){
        List<Point> legalMoves = new ArrayList<>();
        if (p != null) {
            if(p.isQueen()){
                System.out.println("Queen piece clicked");
            } else{
                System.out.println("Regular piece clicked");
                //check if a single step is possible
                if(true){
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
            }
        }
        return legalMoves;
    }

    @Override
    public void onPieceToMove(Point from, Point to) {
        Board b = model.getBoard();
        Piece p = b.getPieceAt(from.x, from.y);
        BoardView bw = view.getBoardView();
        /*
        if(p == null){
            p = bw.getSelectedPieceView().getPiece();;
            bw.getSelectedPieceView().setSelected(true);
            bw.setSelectedRow(to.x);
            bw.setSelectedCol(to.y);
            bw.updateView();
        }
        */
        if(!(
            p.getColor().equals(Color.WHITE) && model.getTurn().equals("white") ||
            p.getColor().equals(Color.BLACK) && model.getTurn().equals("black")
        )){
            System.out.println("Invalid move");
            return;
        }
        //Remove piece from previous position
        b.setPieceAt(from.x, from.y, null);
        //Put piece at new position
        b.setPieceAt(to.x, to.y, p);
        bw.updatePieceViewAt(from, to, p);
        //If it's a jump, remove the opponent's piece
        if(Math.abs(to.x - from.x) == 2 ||Math.abs(to.y - from.y) == 2){
            b.setPieceAt((to.x + from.x)/2, (to.y + from.y)/2, null);
            boundPieceView = bw.getSelectedPieceView();    
            previouslyJumped = true;        
        }
        boundPieceView = bw.getPieceViewAt(to.x, to.y);
        bw.setSelectedPieceView(boundPieceView);
        boundRow = to.x;
        boundCol = to.y;
        // bw.getPieceViewAt(to.x, to.y).setSelected(true);
        // bw.setSelectedRow(3to.x);
        // bw.setSelectedCol(to.y);
        notifyObservers();
        System.out.println("Piece moved from " + from + " to " + to);
        List<Point> legalMoves = getLegalMoves(b, p, boundRow, boundCol);
        legalMoves.removeIf(move -> Math.abs(move.x - boundRow) != 2 || Math.abs(move.y - boundCol) != 2);
        bw.updateLegalMoves(legalMoves);
        if(legalMoves.isEmpty() || !previouslyJumped){
            bw.updateLegalMoves(new ArrayList<>());
            model.setTurn(model.getTurn().equals("white") ? "black" : "white");
            previouslyJumped = false;
            isFirstMove = true;
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