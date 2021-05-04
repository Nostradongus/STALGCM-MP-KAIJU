import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
This class models the intelligence of a kaiju.
*/
public class Kaiju {

	// TODO: fill this with your own code

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
		// TODO: fill this with your own code
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
		// TODO: fill this with your own code
		return null;
	}

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(
								new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		// declare variables
		String[] statuses = {"ok", "hurt"}; // kaiju status
		int maxHp, moveCtr, numInputs, stateCtr;
		String initState, initMove, dump, name;
		String[] states, parts;
		String[][] transitions, moveFunc;
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

		// read name
		name = br.readLine().trim();

		// read states
		stateCtr = Integer.parseInt(br.readLine().trim());
		states = br.readLine().trim().split(" ");

		transitions = new String[stateCtr * moveCtr * 2][4];
		moveFunc = new String[stateCtr * moveCtr * 2][4];
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
		parts = br.readLine().trim().split(" ");
		initState = parts[0];
		maxHp = Integer.parseInt(parts[1]);
		initMove = br.readLine().trim();

		Kaiju k = new Kaiju(name,states,moves,transitions,moveFunc,initState,
							 maxHp,initMove);

		// read inputs
		numInputs = Integer.parseInt(br.readLine().trim());

		for(int cc = 0; cc < numInputs; cc++){
			String move = br.readLine().trim();
			String status = br.readLine().trim();
			sb.append(
				String.format(
					"%s used %s\n",k.name,k.applyTransition(move,status)
				)
			);
		}
		System.out.print(sb);
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