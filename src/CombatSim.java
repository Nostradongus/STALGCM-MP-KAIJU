import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class models the intelligence of a kaiju.
 */
class Kaiju {
    // TODO: Place your code here
    // fixed variables
    String name;
    String[] states;
    Move[] moves;
    String[][] transitions;
    String[][] moveFunc;
    final int maxHp;

    // progress variables
    String curState;
    String curMove;
    int curHp;
    String curStatus;

    /**
     * This basic constructor initializes the values for the kaiju intelligence
     * model.
     * 
     * Parameters: name : String - name of kaiju states : array-like of size (|Q|, )
     * - list of Strings representing the states moves : array-like of size (|M|,3)
     * - list of moves. Each move is in the format (name,hpCost,dmg) transitions :
     * array-like of size (|f|,4) - list of transitions, each in the format
     * (sourceState,moveName,status,destinationState) which denotes
     * f(sourceState,moveName,status) = destinationState moveRules : array-like of
     * size (|f|,4) - list of transitions, each in the format
     * (sourceState,moveName,status,responseMoveName) which denotes
     * g(sourceState,moveName,status) = responseMoveName initialState : String -
     * name of the initial state of the kaiju maxHp : int - maximum hit points of
     * the kaiju initialMove : String - initial move of the kaiju
     */
    public Kaiju(String name, String[] states, Move[] moves, String[][] transitions, String[][] moveRules,
            String initialState, int maxHp, String initialMove) {
        // TODO: Place your code here
        // fixed variables
        this.name = name;
        this.states = states;
        this.moves = moves;
        this.transitions = transitions;
        this.moveFunc = moveRules;
        this.maxHp = maxHp;

        // progress variables
        this.curState = initialState;
        this.curMove = initialMove;
        this.curHp = maxHp;
        this.curStatus = "ok";
    }

    /**
     * Applies a transition based on a move used on this kaiju and the status of the
     * opponent kaiju. This changes the state of this kaiju and returns the move
     * used.
     * 
     * Parameters: move : String - name of move used against this kaiju status :
     * String - status of opponent kaiju. Could be either "ok" or "hurt"
     * 
     * Returns name of move to use in response.
     */
    String applyTransition(String move, String status) {
        // TODO: Place your code here
        for (int i = 0; i < transitions.length; i++)
            if (transitions[i][0].equals(this.curState) && transitions[i][1].equals(move)
                    && transitions[i][2].equals(status)) {
                this.curState = transitions[i][3];
                return moveFunc[i][3];
            }

        return null;
    }

    /**
     * This method returns the status of this kaiju.
     * 
     * Returns a string indicating whether this kaiju is "ok" or "hurt".
     */
    public String getStatus() {
        // TODO: Place your code here
        return this.curStatus;
    }

    /**
     * Returns the current hit points of this kaiju.
     * 
     * Returns an integer indicating the current hit points of the kaiju.
     */
    public int getHP() {
        // TODO: Place your code here
        return this.curHp;
    }

    /**
     * Returns the name of this kaiju.
     * 
     * Returns a string indicating the name of the kaiju.
     */
    public String getName() {
        // TODO: Place your code here
        return this.name;
    }

    /**
     * Uses a move on a target kaiju, updating the hit points of both kaiju.
     * 
     * Parameters: moveName : string - name of move to use on target kaiju
     * targetKaiju : Kaiju - kaiju to use the move on
     */
    public void useMove(String moveName, Kaiju targetKaiju) {
        // TODO: Place your code here
        // You may print in this function.
        for (Move move : this.moves)
            if (move.name.equals(moveName)) {
                // apply move's damage value to opponent kaiju's health value
                targetKaiju.curHp -= move.dmg;
                this.curHp -= move.cost;

                // update health values of both kaijus
                if (this.curHp < 0)
                    this.curHp = 0;
                if (targetKaiju.getHP() < 0)
                    targetKaiju.curHp = 0;

                // if healing move was used and current health passed the max health cap,
                // update current health value to max health value
                if (this.curHp > maxHp)
                    this.curHp = maxHp;
            }

        // update status of both kaijus
        this.curStatus = 2 * this.curHp > maxHp ? "ok" : "hurt";
        targetKaiju.curStatus = 2 * targetKaiju.getHP() > maxHp ? "ok" : "hurt";

        // apply transition to opponent kaiju
        targetKaiju.curMove = targetKaiju.applyTransition(moveName, this.curStatus);

        // TEMPORARY: output results (System class will be replaced with StringBuilder)
        System.out.println(this.name + " used " + moveName);
        System.out.print(this.name + " HP: " + this.curHp + "; ");
        System.out.println(targetKaiju.getName() + " HP: " + targetKaiju.getHP());
    }
}

/**
 * This class represents a kaiju combat simulator.
 */
public class CombatSim {
    public static StringBuilder sb;
    public static BufferedReader br;
    // TODO: Place your code here
    // 2 Kaijus
    private Kaiju kaiju1;
    private Kaiju kaiju2;

    /**
     * This constructor initializes the values of the kaiju combat simulator
     * 
     * Parameters: kaiju1 : Kaiju - first kaiju in battle kaiju2 : Kaiju - second
     * kaiju in battle
     */
    public CombatSim(Kaiju kaiju1, Kaiju kaiju2) {
        // TODO: Place your code here
        this.kaiju1 = kaiju1;
        this.kaiju2 = kaiju2;
    }

    /**
     * This method steps one round in the combat.
     * 
     * Returns: winner : string - name of the winner of the round. If this round
     * results in a draw, return "DRAW". If there is no winner yet, value is "NONE"
     */
    public String stepRound() {
        // TODO: Place your code here
        // round result
        String result = "NONE";

        // Kaiju 1's turn
        result = stepTurn(1);

        // analyze result after kaiju 1's turn
        if (result.equals("WIN"))
            return kaiju1.getName();
        else if (result.equals("DRAW"))
            return result;
        else if (result.equals("LOSS"))
            return kaiju2.getName();

        // Kaiju 2's turn
        result = stepTurn(2);

        // analyze result after kaiju 2's turn
        if (result.equals("WIN"))
            return kaiju2.getName();
        else if (result.equals("DRAW"))
            return result;
        else if (result.equals("LOSS"))
            return kaiju1.getName();

        return result;
    }

    /**
     * This method steps one turn in the combat.
     * 
     * Parameters: kaijuId : int - this value is 1 if it is kaiju1's turn and 2
     * otherwise
     * 
     * Returns "WIN" if the kaiju eliminated the opponent this turn, "DRAW" if both
     * kaiju got knocked out, "LOSS" if only the kaiju taking their turn was knocked
     * out, and "NONE" otherwise
     */
    public String stepTurn(int kaijuId) {
        // TODO: Place your code here
        // turn result
        String result = "NONE";

        // kaiju in turn and opponent kaiju
        Kaiju turnKaiju = null;
        Kaiju oppKaiju = null;

        if (kaijuId == 1) {
            turnKaiju = kaiju1;
            oppKaiju = kaiju2;
        } else {
            turnKaiju = kaiju2;
            oppKaiju = kaiju1;
        }

        // use kaiju in turn's current move
        turnKaiju.useMove(turnKaiju.curMove, oppKaiju);

        // determine result
        if (turnKaiju.getHP() <= 0 && oppKaiju.getHP() <= 0)
            result = "DRAW";
        else if (oppKaiju.getHP() <= 0)
            result = "WIN";
        else if (turnKaiju.getHP() <= 0)
            result = "LOSS";

        return result;
    }

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        int moveCtr;
        String initState, initMove, dump, name;
        String[] parts;
        Move[] moves;

        // read moves
        moveCtr = Integer.parseInt(br.readLine().trim());
        moves = new Move[moveCtr];

        for (int cc = 0; cc < moveCtr; cc++) {
            name = br.readLine().trim();
            int cost, dmg;
            parts = br.readLine().trim().split(" ");
            cost = Integer.parseInt(parts[0]);
            dmg = Integer.parseInt(parts[1]);
            moves[cc] = new Move(name, cost, dmg);
        }

        // read kaiju
        Kaiju kaiju1 = readKaiju(moves);
        Kaiju kaiju2 = readKaiju(moves);

        // initialize combat simulator
        CombatSim combatSim = new CombatSim(kaiju1, kaiju2);

        String winner = "NONE";

        while (winner.equals("NONE")) {
            winner = combatSim.stepRound();
            if (winner.equals("DRAW")) {
                sb.append("It's a draw!\n");
                continue;
            }
            if (!winner.equals("NONE")) {
                sb.append(String.format("%s wins!\n", winner));
            }
        }

        System.out.print(sb);
    }

    public static Kaiju readKaiju(Move[] moves) throws Exception {
        // declare variables
        String[] statuses = { "ok", "hurt" }; // kaiju status

        // read name
        String name = br.readLine().trim();

        // read states
        int stateCtr = Integer.parseInt(br.readLine().trim());
        String[] states = br.readLine().trim().split(" ");

        String[][] transitions = new String[stateCtr * moves.length * 2][4];
        String[][] moveFunc = new String[stateCtr * moves.length * 2][4];
        int ind = 0;

        // read transition and move functions
        for (String state : states) {
            for (Move move : moves) {
                for (String status : statuses) {
                    String targState = br.readLine().trim();
                    String targMove = br.readLine().trim();
                    transitions[ind][0] = moveFunc[ind][0] = state;
                    transitions[ind][1] = moveFunc[ind][1] = move.name;
                    transitions[ind][2] = moveFunc[ind][2] = status;
                    transitions[ind][3] = targState;
                    moveFunc[ind][3] = targMove;
                    ind++;
                }
            }
        }

        // read initial values
        String[] parts = br.readLine().trim().split(" ");
        String initState = parts[0];
        int maxHp = Integer.parseInt(parts[1]);
        String initMove = br.readLine().trim();

        return new Kaiju(name, states, moves, transitions, moveFunc, initState, maxHp, initMove);
    }
}

class Move {
    public String name;
    public int cost;
    public int dmg;

    Move(String name, int cost, int dmg) {
        this.name = name;
        this.cost = cost;
        this.dmg = dmg;
    }
}