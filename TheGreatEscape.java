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
        
        // game loop
        while (true) {
            
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
            
            String action = "RIGHT";
            for(int i = 0; i < wallCount; i++)
            {
                int auxX = (int) walls.get(i).get("x");
                int auxY = (int) walls.get(i).get("y");
                String auxOri = (String) walls.get(i).get("ori");
                
                if(auxX == myX +1 && (auxY == myY || auxY == myY+1 ))
                {
                    if(myY  > 0)
                    {   
    
                        action = "UP";
                    }
                    else
                    action = "DOWN";
                }
                /*else if((int) walls.get(i).get("x") == myX +1 && ((int) walls.get(i).get("y") == myX ||(int) walls.get(i).get("y") == myX+1))
                {
                    if(myX  > -1)
                    {
                        action = "RIGHT";
                    }
                    else
                    action = "LEFT";
                }*/
                else
                {
                    action = "RIGHT";
                }
                
                
            }
            
            System.err.println("Walls :" + walls);
            
            String move = getGreedyMove(myId, myX, myY, walls);
            
            System.out.println(move);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

           // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
            
           players.clear(); 
        }
        
    }
    
    
    public static String getGreedyMove(int myId, int myX, int myY, ArrayList<Map> walls)
    {
        String toDo = null;
        
        int currentDist = calculateStraightDistance(myId, myX, myY);
        
        
        ArrayList<Integer> Values = new ArrayList<Integer>();
        
        int Right = costOfMoving("RIGHT", myId,  myX, myY, walls);
        int Left = 2*costOfMoving("LEFT", myId,  myX, myY, walls);
        int Up = costOfMoving("UP", myId,  myX, myY, walls);
        int Down = costOfMoving("DOWN", myId,  myX, myY, walls);
        
        Values.add(Right); // i = 0
        Values.add(Left);
        Values.add(Up);
        Values.add(Down); // i = 3
        
        int leastValue = 1000;
        int lesserI = 0;
        
        if(myId == 0)
        {
            if(Right == -1)
            {
                toDo = "RIGHT";
            }
            else
            {
                for(int i = 0; i < Values.size(); i++)
                {
                    if(Values.get(i) < leastValue)
                    {
                        leastValue = Values.get(i);
                        lesserI = i;
                    }
                }
                
                if(lesserI == 0)
                {
                    toDo = "RIGHT";
                }
                else if(lesserI == 1)
                {
                    toDo = "LEFT";
                }
                else if(lesserI == 2)
                {
                    toDo = "UP";
                }
                else if(lesserI == 3)
                {
                    toDo = "DOWN";
                }
                
            }
            
        }
        
        return toDo;
    }
    
    public static int calculateStraightDistance(int myId, int myX, int myY)
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
    
    public static int wallsInPathPenalty(int myId, int myX,int myY, ArrayList<Map> walls) //- -1 if no walls in the path, 0-n being distance from wall to current pos
    {
        int penalty = 0;
        
        if(myX < 0)
        {
            penalty += 1000;
        }
        else if(myX > 9)
        {
            penalty += 1000;
        }
        else if(myY < 0)
        {
            penalty += 1000;
        }
        else if(myY > 9)
        {
            penalty += 1000;
        }
        
        if(!walls.isEmpty())
        {
            for(int i = 0; i < walls.size(); i++)
            {   
                
                int wallX = (int) walls.get(i).get("x");
                int wallY = (int) walls.get(i).get("y");
                String ori = (String) walls.get(i).get("ori");
                if(myId == 0)
                {
                    if(ori.equals("V"))
                    {
                        if(myY == wallY || myY == wallY+1)  //Same row
                        {   
                            int dist = 10 - (wallX - myX);
                            System.err.println("Dist: " + dist);
                            if(dist > 0)  //On the right side, worse for player 0
                            {
                                penalty += dist;
                            }
                            else if(dist < 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                    else if(ori == "H")         // CHECK AGAIN
                    {
                        if(myX == wallX || myX == wallX+1)
                        {
                            int dist = 10 - (wallX - myX);
                            
                            if(dist > 0)  //On the right side, worse for player 0
                            {
                                penalty += dist*0.5;
                            }
                            else if(dist < 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                }
                
                else if(myId == 1)
                {
                    if(ori == "V")
                    {
                        if(myY == wallY || myY == wallY+1)  //Same row
                        {
                            int dist = 10 - (wallX - myX);
                            
                            if(dist < 0)  //On the right side, worse for player 0
                            {
                                penalty += dist;
                            }
                            else if(dist > 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                    else if(ori == "H")
                    {
                        if(myX == wallX || myX == wallX+1)
                        {
                            int dist = 10 - (wallX - myX);
                            
                            if(dist > 0)  //On the right side, worse for player 0
                            {
                                penalty += dist*0.5;
                            }
                            else if(dist < 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                }
                
                else if(myId == 2)
                {
                    if(ori == "V")
                    {
                        if(myY == wallY || myY == wallY+1)  //Same row
                        {
                            int dist = 10 - (wallX - myX);
                            
                            if(dist < 0)  //On the right side, worse for player 0
                            {
                                penalty += dist*0.5;
                            }
                            else if(dist > 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                    else if(ori == "H")
                    {
                        if(myX == wallX || myX == wallX+1)
                        {
                            int dist = 10 - (wallX - myX);
                            
                            if(dist > 0)  //On the right side, worse for player 0
                            {
                                penalty += dist;
                            }
                            else if(dist < 0) // on the left side, not to bad for player 0
                            {
                                
                            }
                        }
                    }
                }
                
                
            }
        }
        else
        {
            penalty = -1;
        }
        
        return penalty;
    }
    
    public static int costOfMoving(String where, int myId, int myX,int myY, ArrayList<Map> walls)
        {
            int penalty = 0;
            if(where.equals("RIGHT"))
            {
                penalty = wallsInPathPenalty(myId, myX+1, myY,  walls);
                
            }
            else if(where.equals("LEFT"))
            {
                penalty = wallsInPathPenalty(myId, myX-1, myY,  walls);
                
            }
            else if(where.equals("UP"))
            {
                penalty = wallsInPathPenalty(myId, myX, myY-1,  walls);
                
            }
            else if(where.equals("DOWN"))
            {
                penalty = wallsInPathPenalty(myId, myX, myY-1,  walls);
               
            }
            
            return penalty;
        }
    
    public class State
    {
        public ArrayList<Map> players = new ArrayList<Map>();  // Array with all the player ingame and their info
        public ArrayList<Map> walls = new ArrayList<Map>();
        
        int myId, myX, myY;
        
        public State(int myId, int myX, int myY, ArrayList<Map> walls, ArrayList<Map> players)
        {
            this.myId = myId;
            this.myX = myX;
            this.myY = myY;
            this.walls = walls;
            this.players = players;
        }
        
        
    }
    
    
}
