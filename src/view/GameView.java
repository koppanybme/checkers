package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import controller.MenuObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * A GameView osztály a játék megjelenítéséért felelős.
 * A játék megjelenítéséhez szükséges elemeket tartalmazza.
 * Az elemek frissítéséért felelős.
 */
public class GameView extends JFrame implements ModelObserver {
    private List<MenuObserver> observers = new ArrayList<>();
    private JMenu menu;
    private BoardView boardView;
    public JLabel turnLabel;

    /**
     * A játék nézetének frissítéséért felelős metódus.
     */
    @Override
    public void updateView() {
        System.out.println("View updated");
        repaint();
        boardView.repaint();
    }

    /**
     * A játék nézetének konstruktora.
     * Beállítja a játék nézetének elemeit.
     * 
     * @param boardView A tábla nézete.
     */
    public GameView(BoardView boardView) {
        this.boardView = boardView;
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 800));
        add(boardView);
        
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Create a menu
        menu = new JMenu("Options");
        
        // Create menu items
        JMenuItem option1 = new JMenuItem("Save Game");
        JMenuItem option2 = new JMenuItem("Load Game");
        JMenuItem option3 = new JMenuItem("New Game");

        option1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(option1.getText());
            }
        });
        
        option2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(option2.getText());
            }
        });
        
        option3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(option3.getText());
            }
        });
        
        // Add menu items to the menu
        menu.add(option1);
        menu.add(option2);
        menu.add(option3);
        
        // Push menu to the right
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menu);
        
        // Set the menu bar to the frame
        setJMenuBar(menuBar);

        // Create a JLabel and add it to the right (east) of the window
        turnLabel = new JLabel("white's turn");
        turnLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Add some padding
        add(turnLabel, BorderLayout.EAST);
        
        setVisible(true);
    }

    /**
     * Hozzáad egy új megfigyelőt az állapotváltozások értesítéséhez.
     * 
     * @param observer Az új megfigyelő.
     */
    public void addObserver(MenuObserver observer) {
        observers.add(observer);
    }

    /**
     * Eltávolít egy megfigyelőt az állapotváltozások értesítéséből.
     * 
     * @param observer Az eltávolítandó megfigyelő.
     */
    public void removeObserver(MenuObserver observer) {
        observers.remove(observer);
    }

    /**
     * Értesíti a megfigyelőket a menü állapotváltozásairól.
     * 
     * @param menuItem A menüpont, amelyre kattintottak.
     */
    public void notifyObservers(String menuItem) {
        for (MenuObserver observer : observers) {
            observer.onMenuItemClicked(menuItem);
        }
    }

    /**
     * Visszaadja a tábla nézetét.
     * @return
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Frissíti a tábla nézetét.
     * @param newBoardView Az új tábla nézete.
     */
    public void updateBoardView(BoardView newBoardView) {
        remove(this.boardView);
        this.boardView = newBoardView;
        add(this.boardView);
        revalidate();
        repaint();
    }
}
