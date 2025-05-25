import java.util.Random;

public class RandomStrategy implements MoveStrategy {
    private Random random = new Random();

    @Override
    public int nextMove(int marblesLeft) {
        return marblesLeft == 1 ? 1 : random.nextInt(2) + 1;
    }
   
}
