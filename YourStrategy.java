public class YourStrategy implements MoveStrategy {

    @Override
    public int nextMove(int marblesLeft) {
        if (marblesLeft <= 0) return 0;
        int move = marblesLeft % 3;
        return move == 0 ? 1 : move;
    }
}
