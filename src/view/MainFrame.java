package view;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    public JMenu menu;

    public MainFrame(BoardView boardView) {
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
        JMenuItem option1 = new JMenuItem("Option 1");
        JMenuItem option2 = new JMenuItem("Option 2");
        JMenuItem option3 = new JMenuItem("Option 3");
        JMenuItem option4 = new JMenuItem("Option 4");

        option1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Option 1 selected!");
            }
        });
        
        option2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Option 2 selected!");
            }
        });
        
        option3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Option 3 selected!");
            }
        });
        
        option4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Option 4 selected!");
            }
        });
        
        // Add menu items to the menu
        menu.add(option1);
        menu.add(option2);
        menu.add(option3);
        menu.add(option4);
        
        // Push menu to the right
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menu);
        
        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);
        
        frame.setVisible(true);
    }
}
