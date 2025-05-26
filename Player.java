// The rationale as to why the player class is made public not private so that any external class can access it. It is similar to the files accessible on public domain as opposed to the top secret private files 
public class Player {
    private String name; //These are the instance variables otherwise known as Fields.I remember that the name stores the character as a string because of the String definition
    private MoveStrategy strategy; //strategy which is the instance of MoveStrategy (Interface) 
                                    //Note to self,research online as to why all class cant be public like the exportable components in react? It will save a lot of time.
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
    public int nextMove(int marbleSize) {
        return strategy.nextMove(marbleSize);
    }
    public MoveStrategy getStrategy() {
        return strategy;
    }
}
