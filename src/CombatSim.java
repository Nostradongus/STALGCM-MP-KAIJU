import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
This class models the intelligence of a kaiju.
*/
class Kaiju {
    // TODO: Place your code here
      // set variables 
    String name;
    String[] states;
    Move[] moves; 
    String[][] transitions; 
    String[][] moveFunc; 
    final int maxHp;

      // changing variables 
    String curState; 
    String curMove; 
    int curHp; 
    String curStatus;

    int prevIndex = 0;

    /**
    This basic constructor initializes the values for the kaiju intelligence 
    model.

    Parameters:
    name          : String
        - name of kaiju
    states        : array-like of size (|Q|, ) 
        - list of Strings representing the states
    moves         : array-like of size (|M|,3) 
        - list of moves. Each move is in the format (name,hpCost,dmg)
    transitions   : array-like of size (|f|,4)
        - list of transitions, each in the format 
          (sourceState,moveName,status,destinationState) which denotes
          f(sourceState,moveName,status) = destinationState
    moveRules    : array-like of size (|f|,4)
        - list of transitions, each in the format 
          (sourceState,moveName,status,responseMoveName) which denotes
          g(sourceState,moveName,status) = responseMoveName
    initialState : String
        - name of the initial state of the kaiju
    maxHp        : int
        - maximum hit points of the kaiju
    initialMove  : String
        - initial move of the kaiju
    */
    public Kaiju(String name, String[] states, Move[] moves, 
                 String[][] transitions, String[][] moveRules, 
                 String initialState, int maxHp, String initialMove){
        // TODO: Place your code here
        this.name = name; 
        this.states = states; 
        this.moves = moves; 
        this.transitions = transitions; 
        this.moveFunc = moveRules; 
        this.maxHp = maxHp; 
        
        this.curState = initialState;
        this.curHp = maxHp; 
        this.curMove = initialMove; 
        this.curStatus = "ok";
    }

    /**
    Applies a transition based on a move used on this kaiju and the status of 
    the opponent kaiju. This changes the state of this kaiju and returns the
    move used.

    Parameters:
    move   : String - name of move used against this kaiju
    status : String - status of opponent kaiju. Could be either "ok" or "hurt"

    Returns name of move to use in response.
    */
    String applyTransition(String move, String status){
        // TODO: Place your code here
        // get indexes mathematically based on input specifications
        int stateIndex = 0; // state index 
        int moveIndex = 0;  // move index 
        
        // get index of state on states[] 
        for (int j = 0; j < states.length; j++)
            if (states[j].equals(curState))
                stateIndex = j; 
        
        // as there are 2*|M| move transitions per state 
        // "ok" and "hurt" statuses per move transitions 
        stateIndex *= (2 * moves.length);
        
        // get index of move on moves[] 
        for (int j = 0; j < moves.length; j++)
            if (moves[j].name.equals(move))
                moveIndex = j; 
        
        // as there are 2 transitions per move on each state 
        // "ok and hurt" statuses 
        moveIndex *= 2; 
        
        // if the given status of opponent kaiju is "hurt"
        // move to the second pair of state and move's transition 
        if (status.equals("hurt"))
            moveIndex++; 
        
        // final index value 
        int index = stateIndex + moveIndex; 
        
        // get the next state that the kaiju will transition into as well as its
        // next move 
        curState = transitions[index][3]; 
        return moveFunc[index][3];
    }

    /**
    This method returns the status of this kaiju.

    Returns a string indicating whether this kaiju is "ok" or "hurt".
    */
    public String getStatus(){
        // TODO: Place your code here
        return curStatus;
    }

    /**
    Returns the current hit points of this kaiju.

    Returns an integer indicating the current hit points of the kaiju.
    */
    public int getHP(){
        // TODO: Place your code here
        return curHp;
    }

    /**
    Returns the name of this kaiju.

    Returns a string indicating the name of the kaiju.
    */
    public String getName(){
        // TODO: Place your code here
        return name;
    }

    /**
    Uses a move on a target kaiju, updating the hit points of both kaiju.

    Parameters:
    moveName    : string - name of move to use on target kaiju
    targetKaiju : Kaiju  - kaiju to use the move on
    */
    public void useMove(String moveName, Kaiju targetKaiju){
        // TODO: Place your code here
        // You may print in this function.
        
        int cost = 0, damage = 0;
        
        // Finds cost and damage move used
        for (Move m : moves) 
          if (m.name.equals(moveName)) {
            cost = m.cost; 
            damage = m.dmg; 
                  break;
          }
        
        // Applies HP changes to self
        this.curHp -= cost;
        if (this.curHp > this.maxHp)
          this.curHp = this.maxHp;
        else if (this.curHp < 0)
          this.curHp = 0;
        
        // Applies HP changes to target kaiju
        targetKaiju.curHp -= damage;
        if (targetKaiju.getHP() < 0)
          targetKaiju.curHp = 0;
        
        // Update status of both kaijus 
        this.curStatus = 2 * this.curHp > maxHp ? "ok" : "hurt";
        targetKaiju.curStatus = 2 * targetKaiju.getHP() > maxHp ? "ok" : "hurt";

        // Print results 
        CombatSim.sb.append(this.name + " used " + moveName + "\n" + this.name + 
        " HP: " + this.curHp + "; " + targetKaiju.getName() + " HP: " + targetKaiju.getHP() + "\n");
    }
}

/**
This class represents a kaiju combat simulator.
*/
public class CombatSim {
    public static StringBuilder sb;
    public static BufferedReader br;
    // TODO: Place your code here
    Kaiju kaiju1; 
    Kaiju kaiju2;

    /**
    This constructor initializes the values of the kaiju combat simulator

    Parameters:
    kaiju1 : Kaiju - first kaiju in battle
    kaiju2 : Kaiju - second kaiju in battle
    */
    public CombatSim(Kaiju kaiju1, Kaiju kaiju2) {
        // TODO: Place your code here
        this.kaiju1 = kaiju1; 
        this.kaiju2 = kaiju2; 
    }

    /**
    This method steps one round in the combat. 

    Returns:
    winner : string - name of the winner of the round. If this round results in
                      a draw, return "DRAW". If there is no winner yet, 
                      value is "NONE"
    */
    public String stepRound(){
        // TODO: Place your code here
        String result = "NONE";

        // kaiju 1's turn 
        result = stepTurn(1);
        
        // analyze result after kaiju 1's turn 
        if (result.equals("WIN"))
          return kaiju1.getName(); 
        else if (result.equals("DRAW"))
          return result; 
        else if (result.equals("LOSS"))
          return kaiju2.getName();
        
        // kaiju 2's turn 
        result = stepTurn(2);
        
        // analyze result after kaiju 2's turn 
        if (result.equals("WIN"))
          return kaiju2.getName(); 
        else if (result.equals("DRAW"))
          return result; 
        else if (result.equals("LOSS"))
          return kaiju1.getName();
        
        return result; // NONE
    }

    /**
    This method steps one turn in the combat.

    Parameters:
    kaijuId : int - this value is 1 if it is kaiju1's turn and 2 otherwise

    Returns "WIN" if the kaiju eliminated the opponent this turn, "DRAW" if 
    both kaiju got knocked out, "LOSS" if only the kaiju taking their turn was 
    knocked out, and "NONE" otherwise
    */
    public String stepTurn(int kaijuId){
        // TODO: Place your code here
        // turn result 
        // kaiju in turn and opponent kaiju 
        Kaiju turnKaiju = null; 
        Kaiju oppKaiju = null; 
    
        // if kaiju in turn is kaiju 1
        if (kaijuId == 1) {
          turnKaiju = kaiju1; 
          oppKaiju = kaiju2; 
        }
        // else kaiju 2
        else {
          turnKaiju = kaiju2; 
          oppKaiju = kaiju1; 
        }

        // use kaiju in turn's current move 
        turnKaiju.useMove(turnKaiju.curMove, oppKaiju);

        // apply transition to opponent kaiju 
        oppKaiju.curMove = oppKaiju.applyTransition (turnKaiju.curMove, turnKaiju.getStatus());

        // If Kaiju defeated enemy Kaiju this turn
        if (turnKaiju.getHP() > 0 && oppKaiju.getHP() <= 0)
          return "WIN";
        // If both Kaijus got knocked out
        else if (turnKaiju.getHP() <= 0 && oppKaiju.getHP() <= 0)
          return "DRAW";
        // If Kaiju in current turn was knocked out
        else if (turnKaiju.getHP() <= 0 && oppKaiju.getHP() > 0)
          return "LOSS";
        // Otherwise..
        else
          return "NONE";
    }

    public static void main(String[] args) throws Exception{
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        int moveCtr;
        String initState, initMove, dump, name;
        String[] parts;
        Move[] moves;

        // read moves
        moveCtr = Integer.parseInt(br.readLine().trim());
        moves = new Move[moveCtr];

        for(int cc = 0; cc < moveCtr; cc++){
            name = br.readLine().trim();
            int cost, dmg;
            parts = br.readLine().trim().split(" ");
            cost = Integer.parseInt(parts[0]);
            dmg = Integer.parseInt(parts[1]);
            moves[cc] = new Move(name,cost,dmg);
        }

        // read kaiju
        Kaiju kaiju1 = readKaiju(moves);
        Kaiju kaiju2 = readKaiju(moves);

        // initialize combat simulator
        CombatSim combatSim = new CombatSim(kaiju1,kaiju2);

        String winner = "NONE";

        while(winner.equals("NONE")){
            winner = combatSim.stepRound();
            if(winner.equals("DRAW")){
                sb.append("It's a draw!\n");
                continue;
            }
            if(!winner.equals("NONE")) {
                sb.append(String.format("%s wins!\n",winner));
            }
        }

        System.out.print(sb);
    }

    public static Kaiju readKaiju(Move[] moves) throws Exception {
        // declare variables
        String[] statuses = {"ok", "hurt"}; // kaiju status
        
        // read name
        String name = br.readLine().trim();

        // read states
        int stateCtr = Integer.parseInt(br.readLine().trim());
        String[] states = br.readLine().trim().split(" ");

        String[][] transitions = new String[stateCtr * moves.length * 2][4];
        String[][] moveFunc = new String[stateCtr * moves.length * 2][4];
        int ind = 0;

        // read transition and move functions
        for(String state : states) {
            for(Move move : moves) {
                for(String status : statuses) {
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

        return new Kaiju(name,states,moves,transitions,moveFunc,initState, 
                         maxHp,initMove);
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