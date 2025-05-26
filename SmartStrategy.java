/**
 * SmartStrategy.
 *
 * @author  Ifesinachi Paschal Obiors
 * @version 3.0
 */
public class SmartStrategy implements MoveStrategy {
    @Override
    public int nextMove(int marblesLeft) {
        return (marblesLeft % 3 == 0) ? 2 : 1;
    }
    
    
    
}
