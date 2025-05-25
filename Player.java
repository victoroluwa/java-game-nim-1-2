public class Player {
    private String name;
    private MoveStrategy strategy;

    public Player(String name, MoveStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public int makeMove(int marblesLeft) {
        return strategy.nextMove(marblesLeft);
    }

    public String getName() {
        return name;
    }

    public MoveStrategy getStrategy() {
        return strategy;
    }
}
