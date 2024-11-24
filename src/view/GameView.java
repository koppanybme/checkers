package view;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MenuObserver;

import java.util.ArrayList;
import java.util.List;

public class GameView extends JFrame {
    private List<MenuObserver> observers = new ArrayList<MenuObserver>();
    public JMenu menu;

    public GameView(BoardView boardView) {
        // Create a new JFrame to hold the BoardView
        JFrame frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(boardView);
        
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
        frame.setJMenuBar(menuBar);
        
        frame.setVisible(true);
    }

    public void addObserver(MenuObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(MenuObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String menuItem) {
        for (MenuObserver observer : observers) {
            observer.onMenuItemClicked(menuItem);
        }
    }
}
