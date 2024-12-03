import controller.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Color;

public class BoardViewTest {
    
    @Test
    public void testMoved() {
        GameController controller = new GameController();
        controller.newGame();
        controller.onPieceToMove(new Point(2, 1), new Point(3, 2));
        assertTrue(controller.view.getBoardView().getPieceViewAt(2, 1) == null);
        assertTrue(controller.view.getBoardView().getPieceViewAt(3, 2) != null);
    }

    @Test
    public void testSize() {
        GameController controller = new GameController();
        controller.newGame();
        assertEquals(
            controller.view.getBoardView().getPieceViewAt(1, 2).getHeight(),
            controller.view.getBoardView().getPieceViewAt(1, 2).getWidth()
            );
    }

    @Test
    public void testColor() {
        GameController controller = new GameController();
        controller.newGame();
        assertEquals(
            controller.view.getBoardView().getPieceViewAt(2, 1).getBackground(),
            new Color(238, 238, 238)
            );
    }

}
