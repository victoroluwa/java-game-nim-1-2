import org.junit.Test;
import static org.junit.Assert.*;

public class NimGameTest {

    @Test
    public void testInitialMarbleCount() {
        Player p1 = new Player("Human", new HumanUserStrategy());
        Player p2 = new Player("Computer", new YourStrategy());
        NimGame game = new NimGame(p1, p2);
        
        assertEquals(12, game.getMarbleSize()); 
    }

    @Test
    public void testAssignMove() {
        Player p1 = new Player("Human", new HumanUserStrategy());
        Player p2 = new Player("Computer", new YourStrategy());
        NimGame game = new NimGame(p1, p2);

        game.assignMove(2);
        assertEquals(10, game.getMarbleSize());
    }

    @Test
    public void testUndoMove() {
        Player p1 = new Player("Human", new HumanUserStrategy());
        Player p2 = new Player("Computer", new YourStrategy());
        NimGame game = new NimGame(p1, p2);

        game.assignMove(2);
        game.undoLastMove();
        assertEquals(12, game.getMarbleSize());
    }

    @Test
    public void testCheckWinner() {
        Player p1 = new Player("Human", new HumanUserStrategy());
        Player p2 = new Player("Computer", new YourStrategy());
        NimGame game = new NimGame(p1, p2);

        game.setMarbleSize(0);
        assertTrue(game.checkWinner());
    }
}
