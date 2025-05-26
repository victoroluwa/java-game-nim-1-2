public interface MoveStrategy {
    int nextMove(int marblesLeft);
}
//The interface is something like a contract which specifies what methods a class should implement. That analogy should suffice for now.

//I believe the block inside those curlys is a method that takes one parameter and returns the int.
//Update: I just found that int means integer. 
//I just gained better understanding of interfaces. I found out that by default the method is public and abstract.


//public interface MoveStrategy {
//    int nextMove; //I tried this. I got an error which essentially that I missed the paramatar.
//}

//Problem Resolved:
 
//public interface MoveStrategy {
//    int nextMove(int marblesLeft); //
//} 