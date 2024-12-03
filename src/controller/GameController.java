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

/**
 * A GameController osztály felelős a játék logikájának és az interakcióknak a kezeléséért
 * a modell (GameState), a nézet (GameView) és a felhasználói bemenetek között. 
 * Megvalósítja a java.io.Serializable, MenuObserver és PieceObserver interfészeket, 
 * hogy kezelje a játékállapot sorosítását, a menü műveleteket és a bábu interakciókat.
 * 
 * <p>Ez az osztály módszereket biztosít új játék indítására, játékállapotok mentésére és betöltésére,
 * bábu kattintások és mozgások kezelésére, valamint az állapotváltozásokról való értesítésre.</p>
 * 
 * <p>Főbb funkciók:</p>
 * <ul>
 *   <li>Új játék indítása a {@link #newGame()} metódussal</li>
 *   <li>Az aktuális játékállapot mentése a {@link #saveGame()} metódussal</li>
 *   <li>Korábban mentett játékállapot betöltése a {@link #loadGame()} metódussal</li>
 *   <li>Bábu kattintások kezelése és legális lépések meghatározása a {@link #onPieceClicked(int, int)} metódussal</li>
 *   <li>Bábu mozgások kezelése a {@link #onPieceToMove(Point, Point)} metódussal</li>
 *   <li>Annak ellenőrzése, hogy a játékos tud-e lépni vagy ugrani a {@link #canPlayerMove()} és {@link #canPlayerJump()} metódusokkal</li>
 *   <li>Legális lépések megszerzése egy bábu számára a {@link #getLegalMoves(Board, Piece, int, int)} metódussal</li>
 * </ul>
 * 
 * <p>A GameController osztály kezeli a körökre osztott logikát, frissíti a nézetet az aktuális 
 * játékállapottal, és kezeli a játék végi feltételeket.</p>
 * 
 * @szerző Debreczeni Koppány Balázs
 * @verzió 1.0
 */

public class GameController implements java.io.Serializable, MenuObserver, PieceObserver {
    private List<ControllerObserver> observers = new ArrayList<>();
    public GameView view;
    public GameState model;
    private boolean previouslyJumped = false;
    private PieceView boundPieceView;
    private int boundRow;
    private int boundCol;

    /**
     * A fő metódus, amely elindítja a játékot.
     * Ez a metódus létrehoz egy új GameController példányt és elindít egy új játékot.
     *
     * @param args Parancssori argumentumok (nem használt).
     */
    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.newGame();
    }

    /**
     * Hozzáad egy megfigyelőt az observer listához.
     *
     * @param observer A hozzáadandó megfigyelő.
     */
    public void addObserver(ControllerObserver observer) {
        observers.add(observer);
    }

    /**
     * Eltávolít egy megfigyelőt az observer listából.
     *
     * @param observer A hozzáadandó megfigyelő.
     */
    public void removeObserver(ControllerObserver observer) {
        observers.remove(observer);
    }

    /**
     * Hozzáad egy megfigyelőt az observer listához.
     *
     * @param observer A hozzáadandó megfigyelő.
     */
    public void notifyObservers() {
        for (ControllerObserver observer : observers) {
            observer.update();
        }
    }
    
    /**
     * Kezeli a menüelemek kattintásait.
     *
     * @param menuItem A kattintott menüelem neve.
     */
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

    /**
     * Kezeli a bábuk kattintásait.
     * Ellenőrzi, hogy a játékos tud-e lépni vagy ugrani, és frissíti a nézetet a lehetséges lépésekkel.
     *
     * @param row A kattintott sor.
     * @param col A kattintott oszlop.
     */
    @Override
    public void onPieceClicked(int row, int col) {
        if(!previouslyJumped && !canPlayerMove()){
            model.setWinner(model.getTurn().equals("white") ? "black" : "white");
            view.turnLabel.setText(model.getWinner() + " won");
        }
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
            if(p.isQueen()){
                List<Point> legalMoves = new ArrayList<>();
                legalMoves = getLegalMoves(b, p, row, col);                              
                if(shouldPlayerJump){                
                    int[] directions = {-1, 1};
                    for (int dx : directions) {
                        for (int dy : directions) {
                            for (int distance = 1; distance < Math.min(b.getRows(), b.getCols()); distance++) {
                                int newRow = row + distance * dx;
                                int newCol = col + distance * dy;
                                try {
                                    if (b.getPieceAt(newRow, newCol) == null) {
                                        //Remove moves that aren't jumps
                                        legalMoves.remove(new Point(newRow, newCol));                                                        
                                    } else {
                                        break;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    break; // Stop if out of bounds
                                }
                            }
                        }
                    }
                }
                if(previouslyJumped){
                    if(row != boundRow || col!= boundCol){
                        legalMoves = new ArrayList<>();
                    }
                }
                bw.updateLegalMoves(legalMoves);            
                notifyObservers();
            } else {
                List<Point> legalMoves = new ArrayList<>();
                legalMoves = getLegalMoves(b, p, row, col);
                if(shouldPlayerJump){                
                    legalMoves.removeIf(move -> Math.abs(move.x - row) != 2 || Math.abs(move.y - col) != 2);
                }
                bw.updateLegalMoves(legalMoves);            
                notifyObservers();
            }
        } else {
            System.out.println("Selected " + p.getColor() + " but was " + model.getTurn() + "'s turn");
            bw.getSelectedPieceView().setSelected(false);
            notifyObservers();
            return;
        }
    }

    /**
     * Ellenőrzi, hogy a játékos tud-e lépni.
     *
     * @return Igaz, ha a játékos tud lépni, egyébként hamis.
     */
    public boolean canPlayerMove(){
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
                    List<Point> legalMoves = getLegalMoves(b, p, i, j);
                    if(!legalMoves.isEmpty()){
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

    /**
     * Ellenőrzi, hogy a játékos tud-e ugrani.
     *
     * @return Igaz, ha a játékos tud ugrani, egyébként hamis.
     */
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
                    if(p.isQueen()){
                        List<Point> jumpMoves = getLegalMoves(b, p, i, j);
                        Iterator<Point> iterator = jumpMoves.iterator();
                        //Iterate over every legal move
                        while (iterator.hasNext()) {
                                //Get the next legal move to check if jump
                                Point legalMove = iterator.next();
                                //Calculate direction between p and legalMove                    
                                int dirX = (legalMove.x - i) / Math.abs(legalMove.x - i);
                                int dirY = (legalMove.y - j) / Math.abs(legalMove.y - j);
                                //Current piece's coordinates
                                int x = i;
                                int y = j; 
                                //Iterate over pieces starting from the current piece, ending at the legal move
                                while (x != legalMove.x && y != legalMove.y) {
                                    //Check the piece at (x, y)
                                    Piece iterPiece = b.getPieceAt(x, y);
                                    if (iterPiece != null && !iterPiece.getColor().equals(p.getColor())) {
                                        //If current piece is opponent's, then it's a jump
                                        return true;                                   
                                    }
                                    x += dirX;
                                    y += dirY;
                                }
                            }
                    } else {
                        List<Point> jumpMoves = getLegalMoves(b, p, i, j);
                        Iterator<Point> iterator = jumpMoves.iterator();
                        while (iterator.hasNext()) {
                                Point legalMove = iterator.next();                        
                                if (Math.abs(legalMove.x - i) != 2 || Math.abs(legalMove.y - j) != 2) {
                                    iterator.remove();
                                }
                            }                                                         
                        if(!jumpMoves.isEmpty()){
                            return true;
                        }
                    }
                } else {
                    //If not own piece, skip
                    continue;
                }                
            }
        }
        return false;
    }

    /**
     * Meghatározza a bábu számára a legális lépéseket.
     * 
     * @param b A játéktábla.
     * @param p A bábu.
     * @param row A bábu sor koordinátája.
     * @param col A bábu oszlop koordinátája.
     * @return
     */
    public List<Point> getLegalMoves(Board b, Piece p, int row, int col){
        List<Point> legalMoves = new ArrayList<>();
        if (p != null) {
            if(p.isQueen()) {
                System.out.println("Queen piece clicked");
                //Check all four directions
                int[] directions = {-1, 1};
                for (int dx : directions) {
                    for (int dy : directions) {
                        for (int distance = 1; distance < Math.max(b.getRows(), b.getCols()); distance++) {
                            int newRow = row + distance * dx;
                            int newCol = col + distance * dy;
                            try {
                                if (b.getPieceAt(newRow, newCol) == null) {
                                    legalMoves.add(new Point(newRow, newCol));                                
                                } else if(!b.getPieceAt(newRow, newCol).getColor().equals(p.getColor())) {
                                    //If piece at new position is opponent's, check if jump is possible
                                    //Coordinate to jump to
                                    int jumpRow = newRow + dx;
                                    int jumpCol = newCol + dy;
                                    if(b.getPieceAt(jumpRow, jumpCol) == null){
                                        legalMoves.add(new Point(jumpRow, jumpCol));                                                                                    
                                    }
                                    break;                                  
                                } else {
                                    break;
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                break; // Stop if out of bounds
                            }
                        }
                    }
                }
            } else {
                System.out.println("Regular piece clicked");
                //check if a single step is possible
                int[] directions = {-1,1};
                for(int dx : directions){
                    for(int dy : directions){
                        try{
                            if(b.getPieceAt(row + dx, col + dy) == null){
                                legalMoves.add(new Point(row + dx, col + dy));
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }            
                //check if a jump is possible
                for(int dx : directions){
                    for(int dy : directions){
                        try {
                            Piece neighbor = b.getPieceAt(row + dx, col + dy);
                            Piece jump = b.getPieceAt(row + 2 * dx, col + 2 * dy);
                            //check if the neighbor is an opponent and the jump is empty
                            if (neighbor != null && jump == null && !neighbor.getColor().equals(p.getColor())) {
                                legalMoves.add(new Point(row + 2 * dx, col + 2 * dy));
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }                                                
            }
        }
        System.out.println("Legal moves: " + legalMoves);
        return legalMoves;
    }

    /**
     * Kezeli a bábu mozgásokat.
     * Frissíti a nézetet a bábu új pozíciójával, és eltávolítja az ellenfél bábuit, ha szükséges.
     * Ellenőrzi, hogy a játékosnak van-e még ugrási lehetősége, és ha nincs, akkor a következő játékosra vált.
     * Ellenőrzi, hogy a játékos nyert-e, és frissíti a nézetet a győztes játékossal.
     * 
     * @param from Az eredeti pozíció.
     * @param to Az új pozíció.
     */
    @Override
    public void onPieceToMove(Point from, Point to) {        
        Board b = model.getBoard();
        Piece p = b.getPieceAt(from.x, from.y);
        BoardView bw = view.getBoardView();
        if(!(
            p.getColor().equals(Color.WHITE) && model.getTurn().equals("white") ||
            p.getColor().equals(Color.BLACK) && model.getTurn().equals("black")
        )){
            System.out.println("Invalid move");
            return;
        }
        if(p.isQueen()){
            //Remove piece from previous position
            b.setPieceAt(from.x, from.y, null);
            //Put piece at new position
            b.setPieceAt(to.x, to.y, p);
            bw.updatePieceViewAt(from, to, p);
            //If it's a jump, remove the opponent's piece
            int dirX = (to.x - from.x) / Math.abs(to.x - from.x);
            int dirY = (to.y - from.y) / Math.abs(to.y - from.y);
            int x = from.x;
            int y = from.y; 
            //Iterate over pieces starting from the selected piece, ending at destination
            while (x != to.x && y != to.y) {
                //Check the piece at (x, y)
                Piece currentPiece = b.getPieceAt(x, y);
                if (currentPiece != null && !currentPiece.getColor().equals(p.getColor())) {
                    //If current piece is opponent's remove it
                    b.setPieceAt(x, y, null);
                    if(model.getTurn().equals("white")){
                        model.blackPieces--;
                    } else {
                        model.whitePieces--;
                    }
                    boundPieceView = bw.getSelectedPieceView();
                    previouslyJumped = true;
                    notifyObservers();
                    break;
                }
                boundPieceView = bw.getPieceViewAt(to.x, to.y);
                bw.setSelectedPieceView(boundPieceView);
                boundRow = to.x;
                boundCol = to.y;
                x += dirX;
                y += dirY;
            }
            bw.updateLegalMoves(new ArrayList<>());
            notifyObservers();
            System.out.println("Piece moved from " + from + " to " + to);
            List<Point> legalMoves = getLegalMoves(b, p, boundRow, boundCol);
            int[] directions = {-1,1};
            for(int dx : directions){
                for(int dy : directions){
                    for (int distance = 1; distance < Math.max(b.getRows(), b.getCols()); distance++) {
                        int newRow = boundRow + distance * dx;
                        int newCol = boundCol + distance * dy;
                        try {
                            if (b.getPieceAt(newRow, newCol) == null) {
                                //Remove moves that aren't jumps
                                legalMoves.remove(new Point(newRow, newCol));                                 
                            } else {
                                break;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            break; // Stop if out of bounds
                        }
                    }
                }
            }
            bw.updateLegalMoves(legalMoves);
            if(legalMoves.isEmpty() || !previouslyJumped){
                bw.updateLegalMoves(new ArrayList<>());
                model.setTurn(model.getTurn().equals("white") ? "black" : "white");
                view.turnLabel.setText(model.getTurn() + "'s turn");
                System.out.println("White pieces: " + model.whitePieces + " Black pieces: " + model.blackPieces);
                if(model.whitePieces <= 0){
                    model.setWinner("black");
                    view.turnLabel.setText(model.getWinner() + " won");
                } else if (model.blackPieces <= 0){
                    model.setWinner("white");
                    view.turnLabel.setText(model.getWinner() + " won");
                }                
                previouslyJumped = false;
            }            
        } else {
            //Remove piece from previous position
            b.setPieceAt(from.x, from.y, null);
            //Put piece at new position
            b.setPieceAt(to.x, to.y, p);            
            bw.updatePieceViewAt(from, to, p);
            //If it's a jump, remove the opponent's piece
            if(Math.abs(to.x - from.x) == 2 || Math.abs(to.y - from.y) == 2){
                b.setPieceAt((to.x + from.x)/2, (to.y + from.y)/2, null);
                if(model.getTurn().equals("white")){
                    model.blackPieces--;
                } else {
                    model.whitePieces--;
                }
                boundPieceView = bw.getSelectedPieceView();    
                previouslyJumped = true;        
            }
            boundPieceView = bw.getPieceViewAt(to.x, to.y);
            bw.setSelectedPieceView(boundPieceView);
            boundRow = to.x;
            boundCol = to.y;
            notifyObservers();
            System.out.println("Piece moved from " + from + " to " + to);
            List<Point> legalMoves = getLegalMoves(b, p, boundRow, boundCol);
            legalMoves.removeIf(move -> Math.abs(move.x - boundRow) != 2 || Math.abs(move.y - boundCol) != 2);
            bw.updateLegalMoves(legalMoves);
            if(
                p.getColor().equals(Color.WHITE) && to.x == 7 ||
                p.getColor().equals(Color.BLACK) && to.x == 0
                ){
                    p.setQueen(true);
                    notifyObservers();
            }
            if(legalMoves.isEmpty() || !previouslyJumped){
                bw.updateLegalMoves(new ArrayList<>());
                model.setTurn(model.getTurn().equals("white") ? "black" : "white");
                view.turnLabel.setText(model.getTurn() + "'s turn");
                System.out.println("White pieces: " + model.whitePieces + " Black pieces: " + model.blackPieces);
                if(model.whitePieces <= 0){
                    model.setWinner("black");
                    view.turnLabel.setText(model.getWinner() + " won");
                } else if (model.blackPieces <= 0){
                    model.setWinner("white");
                    view.turnLabel.setText(model.getWinner() + " won");
                }            
                previouslyJumped = false;
            }
        }
    }

    /**
     * Elmenti a játékállapotot a gameState.ser fájlba.
     */
    public void saveGame() {
        try (FileOutputStream fileOut = new FileOutputStream("gameState.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(model);
            System.out.println("Game state saved to gameState.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Betölti a játékállapotot a gameState.ser fájlból.
     */
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
            view.turnLabel.setText(model.getTurn() + "'s turn");
            notifyObservers();
            System.out.println("Game state loaded from gameState.ser");            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Visszaállítja a játékot a kiinduló állapotba.
     */
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