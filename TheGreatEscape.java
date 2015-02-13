import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int w = in.nextInt(); // width of the board
        int h = in.nextInt(); // height of the board
        int playerCount = in.nextInt(); // number of players (2 or 3)
        int myId = in.nextInt(); // id of my player (0 = 1st player, 1 = 2nd player, ...)
        ArrayList<Map> players = new ArrayList<Map>();  // Array with all the player ingame and their info
        ArrayList<Map> walls = new ArrayList<Map>();  // Array with all the walls ingame and their info
        ArrayList<State> movesDone = new ArrayList<State>(); 
        
        
        int costSoFar = 0;
        
        // game loop
        while (true) {
            
            State currentState = new State(myId, 0, 0, costSoFar, walls, players, movesDone, "NULL");
            //Parsing Players
            for (int i = 0; i < playerCount; i++) {
                int x = in.nextInt(); // x-coordinate of the player
                int y = in.nextInt(); // y-coordinate of the player
                int wallsLeft = in.nextInt(); // number of walls available for the player
                
                Map pos = new HashMap(); // Map with the positions for each player, at each turn
               
                pos.put("x", x);
                pos.put("y", y);
                
                players.add(pos);
            }
            System.err.println("Players: " + players);
            
            System.err.println("My X pos: " + players.get(myId).get("x"));
            System.err.println("My Y pos: " + players.get(myId).get("y"));
            
            
            //Parsing Walls
            int wallCount = in.nextInt(); // number of walls on the board
            
            for (int i = 0; i < wallCount; i++) {
                int wallX = in.nextInt(); // x-coordinate of the wall
                int wallY = in.nextInt(); // y-coordinate of the wall
                String wallOrientation = in.next(); // wall orientation ('H' or 'V')
            
                Map wallInfo = new HashMap(); // Map with the positions for each player, at each turn
                wallInfo.put("x", wallX);
                wallInfo.put("y", wallY);
                wallInfo.put("ori", wallOrientation);
                if(!walls.contains(wallInfo))
                walls.add(wallInfo);
            }
            
            //Testing Code
            System.err.println("Number of walls analyzed: " + walls.size());
            int myX = (int) players.get(myId).get("x");
            int myY = (int) players.get(myId).get("y");
         
            currentState.myX = myX;
            currentState.myY = myY;
         
            //Test insert wall after enemy if enemyPos <= 7
            for(int i = 0; i < playerCount; i++)
            {
                if(i != myId)
                {
                    int auxX = (int) players.get(i).get("x");
                    int auxY = (int) players.get(i).get("y");
                    
                    int eneX = auxX-1;
                    
                    if(auxX <= 7)
                    {
                        //System.out.println("" + myX + " " + myY + " V Stop!");
                    }
                }
            }
            
            System.err.println("Walls :" + walls); 
            
            //String move = getGreedyMove(myId, myX, myY, walls);
            
           State nextState = AStar(currentState);
           
           if((myX - (int)players.get(1).get("x")) >1)
           {
               System.out.println((int)players.get(1).get("x")-1 + " " + (int)players.get(1).get("y") + " V");
           }
           else
            System.out.println(nextState.move);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            
           // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
           
           currentState = (State) nextState.clone();
           costSoFar = currentState.costSoFar-1;
           movesDone.add(currentState);
           
           players.clear(); 
        }
        
    }
    
    public String getBestAction(State currentState)
    {
       return null; 
    }
    
    public static ArrayList<State> generatePossibleValidStates(State currentState)
        {
            ArrayList<State> states = new ArrayList<State>();
            int costSoFar = currentState.getG();
            int myId = currentState.myId;
            int myX = currentState.myX;
            int myY = currentState.myY;
            ArrayList<State> movesDone = currentState.movesDone;
            ArrayList<Map> walls = currentState.walls;
            ArrayList<Map> players = currentState.players;
            
            State state1 = new State(myId, myX+1, myY, costSoFar, walls, players,  movesDone, "RIGHT");
            State state2 = new State(myId, myX-1, myY, costSoFar, walls, players,  movesDone, "LEFT");
            State state3 = new State(myId, myX, myY-1, costSoFar, walls, players,  movesDone, "UP");
            State state4 = new State(myId, myX, myY+1, costSoFar, walls, players,  movesDone, "DOWN");
            
            
            if(state1.isValidState() && !currentState.move.equals("LEFT"))
            {   
                states.add(state1);
            }
            else
            {
                System.err.println("State1 not valid");
            }
            
            if(state2.isValidState() && !currentState.move.equals("RIGHT"))
            states.add(state2);
            else
            {
                System.err.println("State2 not valid");
            }
            
            if(state3.isValidState() && !currentState.move.equals("DOWN"))
            states.add(state3);
            else
            {
                System.err.println("State3 not valid");
            }
            
            if(state4.isValidState() && !currentState.move.equals("UP"))
            states.add(state4);
            else
            {
                System.err.println("State4 not valid");
            }
            
            return states;
            
        }
    
        
    public static State AStar(State currentState)
    {
        PriorityQueue<State> open = new PriorityQueue<State>(10, new Comparator<State>() {
            public int compare(State state1, State state2) {
                   return state1.getF() < state2.getF() ? -1 : state1.getF() == state2.getF() ? 0 : 1;
            }
        });
        
        ArrayList<State> path1 = new ArrayList<State>();
        Stack path = new Stack();
        ArrayList<State> closed = new ArrayList<State>();
        State chosen = currentState.clone();
       
       closed.clear();
       open.clear();
       
        open.add(currentState);
        int i = 0;
        while(!open.isEmpty())
        {   
            State current = open.poll();
            System.err.println("Chosen state at " + i + " iteration: " + current.move + " to " + current.myX + ", " + current.myY + " With f value = " + current.getF() + ", G = " + current.getG() + " and H = " + current.getHeuristics());
            System.err.println(i + " iteration: " + open.size());
            i++;
            
            if(current.calculateStraightDistance() == 0)
            {
              path = getOptimalPath(current);
              System.err.println("Size of path: " + path.size());
              ArrayList<String> pathx = new ArrayList<String>();
              System.err.println(pathx);
              
              chosen = (State) path.pop();
              break;
            }
            
            closed.add(current);
            
            ArrayList<State> generatedStates = new ArrayList<State>();
            generatedStates = generatePossibleValidStates(current);
            
            
            System.err.println("Generated States Size: " + generatedStates.size());
            
            for(int u = 0; u < generatedStates.size(); u++)
            {
                
                State next = generatedStates.get(u);
               
                if(closed.contains(next))
                {
                   continue;
                }
                
                int newCost = current.costSoFar + 1; 
            
                if(!(open.contains(next)) && !next.visited)
                {
                    next.parent = current;
                    next.costSoFar = newCost;
                    open.add(next);
                    
                }
                
                next.visited = true;
            }
            
        }
        return chosen;
    }
    
    public static Stack getOptimalPath(State Goal)
    {
        Stack st = new Stack();
        st.push(Goal);
        State Parent = (State) Goal.parent.clone();
        
       while(Parent.parent!=null)
       {
           st.push(Parent);
           Parent = (State) Parent.parent.clone();
       }
       
       return st;
    }
    
    public static class State extends CloneableObject 
    {
        public ArrayList<Map> players;  // Array with all the player ingame and their info
        public ArrayList<Map> walls;
        public ArrayList<State> movesDone;
        public State parent;
        
        public int costSoFar;
        
        
        public int myId, myX, myY;
        
        public String move;
        public boolean visited;
        
        
        public State(int myId, int myX, int myY, int costSoFar, ArrayList<Map> walls, ArrayList<Map> players, ArrayList<State> movesDone, String move)
        {
            this.myId = myId;
            this.myX = myX;
            this.myY = myY;
            this.costSoFar = costSoFar;
            this.walls = walls;
            this.visited = false;
            this.parent = null;
            this.players = players;
            this.movesDone = movesDone;
            this.move = move;
        }
        
        public boolean isValidState()
        {
            boolean valid = true;
            
            for(int i = 0; i < walls.size(); i++)
            {
                int wallX = (int) walls.get(i).get("x");
                int wallY = (int) walls.get(i).get("y");
                
                
                if(isInsideBorders())
                {
                    if(move.equals("RIGHT") || move.equals("LEFT"))
                    {
                        if(wallX == myX && wallY == myY)
                        {
                           return false;
                        }
                        else if(wallX == myX && wallY+1 == myY)
                        {
                            return false;
                        }
                    }
                    else if(move.equals("DOWN") || move.equals("UP"))
                    {
                        if(wallX == myX && wallY == myY)
                        {
                           return false;
                        }
                        else if(wallX + 1  == myX && wallY == myY)
                        {
                            return false;
                        }
                    }
                    
                }
                else
                {
                    return false;
                }
            }
            
            return true;
        }
        
        public boolean isInsideBorders()
        {
            
            
            if(myId == 0)
            {
                if(myX < 0)
                {
                    return false;
                }
                else if(myY > 8)
                {
                    return false;
                }
                else if(myY < 0)
                {
                    return false;
                }
            }
            
            else if(myId == 1)
            {
                if(myX > 8)
                {
                    return false;
                }
                else if(myY > 8)
                {
                    return false;
                }
                else if(myY < 0)
                {
                    return false;
                }
            }
            
            else if(myId == 2)
            {
                if(myX > 8)
                {
                    return false;
                }
                else if(myX < 0)
                {
                    return false;
                }
                else if(myY < 0)
                {
                    return false;
                }
            }
            
            return true;    
        }
        
        public int getG()
        {
           return costSoFar; 
        } 
        
        public void setG(int g)
        {
            this.costSoFar = g;
        }
        
        public int getHeuristics()  //ERROR IS HERE
        {
            int H = calculateStraightDistance();
            
            if(myId == 0)
            {
                if(move.equals("RIGHT"))
                {
                    H += 1;
                }
                else if(move.equals("LEFT"))
                {
                    H += 4;
                }
                else if(move.equals("UP"))
                {
                    H += 2;
                }
                else if(move.equals("DOWN"))
                {
                    H += 3;
                }
            }
            
            else if(myId == 1)
            {
                if(move.equals("RIGHT"))
                {
                    H += 4;
                }
                else if(move.equals("LEFT"))
                {
                    H += 1;
                }
                else if(move.equals("UP"))
                {
                    H += 2;
                }
                else if(move.equals("DOWN"))
                {
                    H += 3;
                }
            }
            
            else if(myId == 2)
            {
                if(move.equals("RIGHT"))
                {
                    H += 3;
                }
                else if(move.equals("LEFT"))
                {
                    H += 2;
                }
                else if(move.equals("UP"))
                {
                    H += 4;
                }
                else if(move.equals("DOWN"))
                {
                    H += 1;
                }
            }
            
            return H;
        }
        
        public int calculateStraightDistance()
        {
            int dist = 0;
            
            if(myId == 0)
            {
                dist = 8 - myX;
            }
            else if(myId == 1)
            {
                dist = myX;
            }
            else if(myId == 2)
            {
                dist = 8 - myX;
            }
            
            return dist;
        }
        
        public int getF()
        {
            return getG() + getHeuristics();
        }
        
        public State clone() {
            return (State) super.clone();
        }
        
    }
    
    public static class CloneableObject implements Cloneable {
        public CloneableObject clone() {
            try {
                return (CloneableObject)super.clone();
            } catch (CloneNotSupportedException err) {
                
                return null;
            }
        }
    }
}
