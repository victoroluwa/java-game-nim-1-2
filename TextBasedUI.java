import java.util.Scanner;

public class TextBasedUI {
    
    private NimGame game;
    private Scanner reader;

    public TextBasedUI() {
        reader = new Scanner(System.in);
        
        System.out.println("The Game of 1-2 Nim Assessment!");
        System.out.println("------------------------------");
        System.out.println("Choose a computer strategy:");
        System.out.println("[R] Random\n" +
                           "[Y] Your Strategy");

        String gameMode = reader.nextLine().toUpperCase();
        MoveStrategy computerStrategy;

        if (gameMode.equals("R")) {
            computerStrategy = new RandomStrategy();
            System.out.println("You selected Random Computer strategy.");
        } else if (gameMode.equals("Y")) {
            computerStrategy = new YourStrategy();
            System.out.println("You selected Your Computer strategy.");
        } else {
            System.out.println("Invalid option. Exiting.");
            return;
        }

        Player player1 = new Player("Human", new HumanUserStrategy());
        Player player2 = new Player("Computer", computerStrategy);

        this.game = new NimGame(player1, player2);
        startGame();
    }

    private void startGame() {
        System.out.println("\nInitial number of marbles: " + game.getMarbleSize());
        displayMarbles();

        while (!game.checkWinner()) {
            displayMenu();
        }

        announceWinner();
    }

        private void displayMenu() {  
        System.out.println("\nChoose an option: \n"
            + "[M] Make a move\n"
            + "[S] Save game\n"
            + "[L] Load saved game\n"
            + "[U] Undo move\n"
            + "[C] Clear game\n"
            + "[Q] Quit game\n");

        String choice = reader.nextLine().toUpperCase();
        switch (choice) {
            case "M":
                makeMove();
                break;
            case "S":
                game.saveGame();
                break;
            case "L":
                game.loadGame();
                displayMarbles();
                break;
            case "U":
                game.undoLastMove();
                displayMarbles();
                break;
            case "C":
                game.resetGame();
                displayMarbles();
                break;
            case "Q":
                System.out.println("Thank you for playing! Exiting game...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please select again.");
        }
    }

    /**
     * Handle making a move for the human or computer player
     */ 
    private void makeMove() {
        
        if (game.isHumanTurn()){
            assignMoveFrom(game.getHumanPlayer()); //human move
            if(!game.checkWinner())
                assignMoveFrom(game.getComputerPlayer()); // computer move
        } else {
            assignMoveFrom(game.getComputerPlayer());
            if(!game.checkWinner())
                assignMoveFrom(game.getHumanPlayer());
        }
    }
        

    private void assignMoveFrom(Player player) {
        System.out.println("\nIt is " + player.getName() + "'s turn to play.");
        int move = player.getMove(game.getMarbleSize());
        game.assignMove(move);
        System.out.println(player.getName() + " takes " + move + " marbles.");
        displayMarbles();
    }
    
    private void displayMarbles() {
        System.out.println("Current number of marbles: " + game.getMarbleSize());
        for (int i = 0; i < game.getMarbleSize(); i++) {
            System.out.print("@ ");
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }
    
    private void announceWinner() {
        String winnerName;
        if (game.isHumanTurn())
            winnerName = game.getComputerPlayer().getName(); //Computer Wins
        else
            winnerName = game.getHumanPlayer().getName(); //Human Wins
        System.out.println("*** " + winnerName + " is the winner! ***");
    }


    public static void main(String[] args) {
        TextBasedUI textUi = new TextBasedUI();
    }
}
