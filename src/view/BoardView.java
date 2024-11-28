package view;

import model.Board;
import model.Piece;
import controller.PieceObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class BoardView extends JPanel implements ModelObserver {
    private List<PieceObserver> observers = new ArrayList<>();
    private List<Point> legalMoves = new ArrayList<>();
    private Board board;
    private PieceView[][] pieceViews;
    private PieceView selectedPieceView;
    private int selectedRow;
    private int selectedCol;
    
    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void setSelectedCol(int selectedCol) {
        this.selectedCol = selectedCol;
    }

    public PieceView getPieceViewAt(int row, int col) {
        return pieceViews[row][col];
    }

    public PieceView getSelectedPieceView() {
        return selectedPieceView;
    }

    public void setSelectedPieceView(PieceView selectedPieceView) {
        this.selectedPieceView = selectedPieceView;
    }

    public void addObserver(PieceObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PieceObserver observer) {
        observers.remove(observer);
    }

    public void notifyObserversPieceClicked(int row, int col) {
        for (PieceObserver observer : observers) {
            observer.onPieceClicked(row, col);
        }
    }

    public void notifyObserversPieceToMove(Point from, Point to) {
        for (PieceObserver observer : observers) {
            observer.onPieceToMove(from, to);
        }
    }

    @Override
    public void updateView() {
        System.out.println("BoardView updated");
        repaint();
    }

    public BoardView(Board board) {
        this.board = board;
        this.pieceViews = new PieceView[board.getRows()][board.getCols()];
        setLayout(null); // Use absolute positioning

        // Initialize PieceView components
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                if (piece != null) {
                    PieceView pieceView = new PieceView(piece);
                    pieceViews[row][col] = pieceView;
                    pieceView.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {                            
                            handlePieceViewSelection(pieceView);                            
                        }
                    });
                    add(pieceView);
                }
            }
        }
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    public void updateLegalMoves(List<Point> legalMoves) {
        this.legalMoves = legalMoves;
        repaint(); // Repaint the board to reflect the new legal moves
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces();
        drawLegalMoves(g);
    }

    private void drawLegalMoves(Graphics g) {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        int pieceSize = (int) (cellWidth * 0.9); // 90% of the cell size
        int offset = (cellWidth - pieceSize) / 2; // Centering offset

        for (Point point : legalMoves) {
            int row = point.x;
            int col = point.y;
            g.setColor(new Color(255, 255, 0, 100)); // Transparent yellow
            g.fillOval(col * cellWidth + offset, row * cellHeight + offset, pieceSize, pieceSize);
        }
    }

    private void drawBoard(Graphics g) {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(196,164,132));
                }
                g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }
    }

    private void drawPieces() {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        int pieceSize = (int) (cellWidth * 0.9); // 90% of the cell size
        int offset = (cellWidth - pieceSize) / 2; // Centering offset

        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Piece piece = board.getPieceAt(row, col);
                PieceView pieceView = pieceViews[row][col];
                if (piece != null) {
                    if (pieceView == null) {
                        pieceView = new PieceView(piece);
                        pieceViews[row][col] = pieceView;
                        add(pieceView);
                    }
                    pieceView.setBounds(col * cellWidth + offset, row * cellHeight + offset, pieceSize, pieceSize);
                    pieceView.setVisible(true);
                } else if (pieceView != null) {
                    pieceView.setVisible(false);
                }
            }
        }
        revalidate();
        repaint();
    }

    private void handlePieceViewSelection(PieceView pieceView) {
        if (selectedPieceView != null) {
            selectedPieceView.setSelected(false); // Deselect the previously selected PieceView
        }
        selectedPieceView = pieceView;
        selectedPieceView.setSelected(true); // Select the new PieceView
        selectedRow = (selectedPieceView.getY() / selectedPieceView.getHeight());
        selectedCol = (selectedPieceView.getX() / selectedPieceView.getWidth());        
        notifyObserversPieceClicked(selectedRow, selectedCol);
        //repaint(); // Repaint the board to reflect the selection change
    }

    private void handleMouseClick(MouseEvent e) {
        int cellHeight = getHeight() / board.getRows();
        int cellWidth = cellHeight;
        int col = e.getX() / cellWidth;
        int row = e.getY() / cellHeight;

        Point clickedPoint = new Point(row, col);
        if (legalMoves.contains(clickedPoint)) {
            // Handle the legal move click
            System.out.println("Legal move clicked at: " + clickedPoint);            
            notifyObserversPieceToMove(new Point(selectedRow, selectedCol), clickedPoint);
        }
    }

    public void updatePieceView(Point from, Point to, Piece piece) {
        pieceViews[to.x][to.y] = pieceViews[from.x][from.y];
        pieceViews[from.x][from.y] = null;
        revalidate();
        repaint();
    }
}