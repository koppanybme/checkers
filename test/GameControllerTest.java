import controller.*;
import model.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Color;
import java.util.*;

public class GameControllerTest {

    @Test
    public void testNewGame() {
        GameController controller = new GameController();
        controller.newGame();
        assertTrue(controller.canPlayerMove());
        assertFalse(controller.canPlayerJump());
    }

    @Test
    public void testMove() {
        GameController controller = new GameController();
        controller.newGame();
        List<Point> legalMoves = controller.getLegalMoves(controller.model.getBoard(), controller.model.getBoard().getPieceAt(2, 1), 2, 1);
        List<Point> expected = new ArrayList<>(Arrays.asList(new Point(3, 0), new Point(3, 2)));
        assertEquals(legalMoves, expected);
    }

    @Test
    public void testJump() {
        GameController controller = new GameController();
        controller.newGame();
        controller.model.getBoard().setPieceAt(3, 2, new Piece(Color.BLACK, false));
        List<Point> legalMoves = controller.getLegalMoves(controller.model.getBoard(), controller.model.getBoard().getPieceAt(2, 1), 2, 1);
        List<Point> expected = new ArrayList<>(Arrays.asList(new Point(3, 0), new Point(4, 3)));
        assertEquals(legalMoves, expected);
    }

    @Test
    public void testMoved() {
        GameController controller = new GameController();
        controller.newGame();
        controller.onPieceToMove(new Point(2, 7), new Point(3, 6));
        assertEquals(controller.model.getBoard().getPieceAt(3, 6).getColor(), Color.WHITE);
        assertEquals(controller.model.getBoard().getPieceAt(2, 7), null);
    }

    @Test
    public void testSaveGame() {
        GameController controller = new GameController();
        controller.newGame();       
        controller.onPieceToMove(new Point(2, 7), new Point(3, 6));
        controller.saveGame();
        GameController controller2 = new GameController();
        controller2.loadGame();
        assertEquals(controller.model.getBoard().getPieceAt(3, 6).getColor(), controller2.model.getBoard().getPieceAt(3, 6).getColor());
    }
}
