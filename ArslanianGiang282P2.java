/* ArslanianGiang282P2.java
 * Authors: Armen Arslanian
 *          Anderson Giang
 * COMP 282
 * November 8, 2015
 * Professor Mike Barnes
 */
/*
ArslanianGiang282P2.java

Mike Barnes
8/12/2015
*/
import java.awt.*;
import java.util.Comparator;
import java.awt.event.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Stack;
import SimulationFramework.*;
import java.util.HashSet;
// CLASSPATH = ... /282projects/SimulationFrameworkV4
// PATH = ... /282projects/SimulationFrameworkV3/SimulationFramework

/**
ArslanianGiang282P2 is the simulation's main class (simulation app) that is a
subclass of SimFrame.  
<p> 

282 Simulation Framework applications must have a subclass of SimFrame
that also has a main method.  The simulation app can make the
appropriate author and usage "help" dialogs, override
setSimModel() and simulateAlgorithm() abstract methods
inherited from SimFrame.  They should also add any specific model
semantics and actions.

<p>

The simulated algorithm is defined in simulateAlgorithm().

<p>
ArslanianGiang282P2 UML class diagram (More detailed UML can be found ArslanianGiang282P2\doc\UML\detailedUML.pdf)
<p>
<img src="ArslanianGiang282P2.png" alt="UML" height="500" width="900">

@since 8/12/2013
@version 3.0
@author G. M. Barnes
@author Armen Arslanian
@author Anderson Giang
*/
public class ArslanianGiang282P2 extends SimFrame   {
   /**eliminate warning @ serialVersionUID*/
   private static final long serialVersionUID = 42L;
   // GUI components for application's menu
   /**the simulation application*/
   private ArslanianGiang282P2 app;
   // application variables;
   /**the actors "bots" of the simulation*/
   private ArrayList <Bot> bot;
   /**Map of WayPoints*/
   private Map map;
   /**Player who will move along the path.*/
   private Player charlie;
   /**For storing the current WayPoint.*/
   private WayPoint current_waypoint;
   /**For storing the starting WayPoint.*/
   private WayPoint initial_waypoint;
   /**For storing the destination WayPoint.*/
   private WayPoint dest_waypoint;
   /**For moving along the calculated path.*/
   private WayPoint target;
   /**If a WayPoint has treasure.*/
   private WayPoint treasure;
   /* Main Method
    */
   public static void main(String args[]) {
      ArslanianGiang282P2 app = new ArslanianGiang282P2("ArslanianGiang282P2", "terrain282.png");
      app.start();  // start is inherited from SimFrame
   }
   /**
   Make the application:  create the MenuBar, "help" dialogs, 
   */
   public ArslanianGiang282P2(String frameTitle, String imageFile) {
      super(frameTitle, imageFile);
      // create menus
      JMenuBar menuBar = new JMenuBar();
      // set About and Usage menu items and listeners.
      aboutMenu = new JMenu("About");
      aboutMenu.setMnemonic('A');
      aboutMenu.setToolTipText(
         "Display informatiion about this program");
      // create a menu item and the dialog it invoke 
      usageItem = new JMenuItem("usage");
      authorItem = new JMenuItem("author");
      usageItem.addActionListener( // anonymous inner class event handler
            new ActionListener() {        
               public void actionPerformed(ActionEvent event) {
                  JOptionPane.showMessageDialog( ArslanianGiang282P2.this, 
                     "Click on a desired WayPoint \n" +
                     "Then Click on your desired destination WayPoint \n" +
                     "Sit back and watch the Bot find its path to the destination \n" +
                     "You can speed up or slow down the Bot with the horizontal bar located at the bottom of the window" +
                     "The rest of the buttons are self-explanatory - Start, Stop, Clear, and Reset",
                     "Usage",   // dialog window's title
                     JOptionPane.PLAIN_MESSAGE);
               }
            }
         );
      // create a menu item and the dialog it invokes
      authorItem.addActionListener(
            new ActionListener() {          
               public void actionPerformed(ActionEvent event) {
                  JOptionPane.showMessageDialog( ArslanianGiang282P2.this, 
                     "Armen Arslanian \n" +
                     "armen.arslanian.526@my.csun.edu \n" +
                     "Anderson Giang \n" +
                     "anderson.giang.820@my.csun.edu \n" +
                     "COMP 282",
                     "author",  // dialog window's title
                     JOptionPane.INFORMATION_MESSAGE,
                     //  author's picture 
                     new ImageIcon("author.png"));
               }
            }
         );
      // add menu items to menu 
      aboutMenu.add(usageItem);   // add menu item to menu
      aboutMenu.add(authorItem);
      menuBar.add(aboutMenu);
      setJMenuBar(menuBar);
      validate();  // resize layout managers
      // construct the application specific variables
   }
   /** 
   Set up the actors (Bots), wayPoints (Markers), and possible traveral
   paths (Connectors) for the simulation.
   */
   public void setSimModel() {
   	// set any initial visual Markers or Connectors
   	// get any required user mouse clicks for positional information.
   	// initialize any algorithm halting conditions (ie, number of steps/moves).
      map = new Map(this);
      setStatus("Enter first position.");
      //first mouse click.
      waitForMousePosition();
      /*create a point that will store the x and y coordinates of the mouse position.
        Mouse position requires a double, so you need to type cast it to store it into
        Point which requires integers.
       */
      Point firstPoint = new Point((int) mousePosition.getX(), (int) mousePosition.getY());
      /*Call the findClosestWayPoint(point) method which will return the closest WayPoint 
        after computing the closest_waypoint, set the waypoint to green and a size of 6.
        CLOSEST BEGINNING WAYPOINT
       */
      initial_waypoint = map.findClosestWayPoint(firstPoint);
      current_waypoint = initial_waypoint;
      initial_waypoint.setColor(Color.blue);
      initial_waypoint.setSize(4);
      initial_waypoint.setVisited(true);
      makePlayer("Charlie", initial_waypoint.getPoint(), Color.red);
      setStatus("Enter destination position.");
      //second mouse click
      waitForMousePosition();
      Point endPoint = new Point((int) mousePosition.getX(), (int) mousePosition.getY());
      dest_waypoint = map.findClosestWayPoint(endPoint);
      dest_waypoint.setColor(Color.blue);
      dest_waypoint.setSize(4);
   }//end of setSimModel
   /**<p>This is a helper method.
    *    It creates a player.</p>
    * @param name name for player
    * @param p starting point of player
    * @param color color of player waypoint 
    */
   public void makePlayer(String name, Point p, Color color) {
      charlie = new Player(name, p, color);
      animatePanel.addBot(charlie);
   }
   /**<p>This method performs <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm</a>.
    * Calculates the shortest path by adding the neighbor cost. 
    * and the heuristic cost to the destination from the neighbor.
    * Makes use of a minimum priority queue to have the lowest cost always at the head of the queue.
    * It's helper method is movePath.</p>
    * @param start starting WayPoint
    * @param end ending WayPoint
    */
   public void aStar(WayPoint start, WayPoint end) {
      animatePanel.clearTemporaryDrawables();
      /*if (charlie.hasTreasure()) { //1
         statusReport("Start (" + target.getPoint().getX() + ", " + target.getPoint().getY() + "), "
            	+ "Stop (" + treasure.getPoint().getX() + ", " + treasure.getPoint().getY() + "), Player " 
                + charlie.getStrength() + " $ " + charlie.getWealth());
      }*/
      //open list minimum priority queue
      PriorityQueue<WayPoint> open_list = new PriorityQueue<WayPoint>(10, 
            new Comparator<WayPoint>() {
               public int compare(WayPoint a,WayPoint b) {
                  return (int) a.getF() - (int)  b.getF();		   
               }
            });
      //closed list HashSet
      HashSet<Point> closed_list = new HashSet<Point>();
      current_waypoint = start;
      current_waypoint.setF(map.threeDimDistance(current_waypoint, end));
      current_waypoint.setParent(null);
      /*if (charlie.hasTreasure() == false && current_waypoint.getColor().equals(Color.yellow)) { //2
         statusReport("Start (" + current_waypoint.getPoint().getX() + ", " + current_waypoint.getPoint().getY() + "), "
            		+ "Stop (" + dest_waypoint.getPoint().getX() + ", " + dest_waypoint.getPoint().getY() + "), Player " 
                  + charlie.getStrength() + " $ " + charlie.getWealth());
      }*/
      open_list.add(current_waypoint);
      while (!open_list.isEmpty() && !current_waypoint.equals(end)) {	   
         current_waypoint = open_list.poll(); //least cost
         if (current_waypoint.equals(end)) {
            break;
         }
         else {
            closed_list.add(current_waypoint.getPoint());
            Marker gray = new Marker(current_waypoint.getPoint(), Color.gray, 2);
            animatePanel.addTemporaryDrawable(gray);
            checkStateToWait();
            for (int i = 0; i < current_waypoint.getNeighbors().size(); i++) { //add neighbors
               WayPoint mark = map.getWayPoint(current_waypoint.getNeighbors().get(i));
               if ((!open_list.contains(mark)) && (!closed_list.contains(mark.getPoint()))) { //do not add the ones already in the lists
                  mark.setParent(current_waypoint);
                  mark.setG(mark.getParent().getG() + map.threeDimDistance(current_waypoint, mark));
                  mark.setH(map.threeDimDistance(mark, end));
                  mark.setF(mark.getG() + mark.getH());
                  open_list.add(mark);
                  Marker white = new Marker(mark.getPoint(), Color.white, 3);
                  animatePanel.addTemporaryDrawable(white);
                  checkStateToWait();
               }
            }
         }
      }
      if (current_waypoint.equals(end)) {
         movePath(start);
      }
      else {
         setStatus("Path does not exist.");
         statusReport("No path (" + initial_waypoint.getPoint().getX() + ", " + initial_waypoint.getPoint().getY() 
                           + ") to (" + dest_waypoint.getPoint().getX() + ", " + dest_waypoint.getPoint().getY() + ")");
         setSimRunning(false);
         animatePanel.setComponentState(false,false,false,false,true);
      }
   }
   /**<p>This method moves the player in the path that was calculated by aStar method.
    *    If it finds a treasure map along the way, aStar is called back to find a new path.</p>
    * @param begin starting WayPoint
    */   
   public void movePath(WayPoint begin) {
      Stack<WayPoint> path = new Stack<WayPoint>();
      path.push(current_waypoint);
      while (!current_waypoint.equals(begin)) {
         current_waypoint = current_waypoint.getParent();
         path.push(current_waypoint);
      }
      /*
       * Since our begin is in our stack.  This means that it will process the begin waypoint.
       * So, if our begin was a city then it will purchase food.  (lose gold, gain strength)
       * if it was gold then it will take the gold and waypoint's gold value to zero.
       * if it was a magenta waypoint (map) then it would take the map and find the treasure.
      */
      //path.size() is the number of waypoints. The path may be interrupted by a magenta waypoint.
      if (charlie.hasTreasure() == true) {
         statusReport("Path (" + target.getPoint().getX() + ", " + target.getPoint().getY() + ") to (" 
                  + treasure.getPoint().getX() + ", " + treasure.getPoint().getY() + "), " 
                  + "length " + (path.size()));
      }
      else {
         statusReport("Path (" + current_waypoint.getPoint().getX() + ", " + current_waypoint.getPoint().getY() + ") to (" 
                        + dest_waypoint.getPoint().getX() + ", " + dest_waypoint.getPoint().getY() + "), " 
                        + "length " + (path.size()));
      }
      WayPoint prev = begin; //begin is not in the stack
      double distance;
      while (!path.isEmpty()) {
         target = path.pop();
         charlie.move(target.getPoint());
         distance = map.threeDimDistance(prev,target);
         prev = target;
         charlie.setStrength(charlie.getStrength()-(int)distance); //casting distance
         if (charlie.getWealth() > target.getCityCost()) {
            charlie.setWealth(charlie.getWealth()- target.getCityCost());
         }
         /***********************************************************************************************
         //Takes care of buying some of the strength offered in the city
         else if ((charlie.getWealth() < target.getCityCost()) && (charlie.getWealth() != 0)) {
            charlie.setWealth(0);
            charlie.setStrength(charlie.getStrength()+charlie.getWealth());
         }
         ************************************************************************************************/
         checkStateToWait();
         //Waypoint is a city
         if (target.getCityCost() > 0) {
            charlie.setStrength(charlie.getStrength()+ target.getCityCost());
            statusReport("City (" + target.getPoint().getX() + ", " + target.getPoint().getY() + "), "
                           + "$ " + target.getCityCost() + ", " + "Player " + charlie.getStrength() + " $ " + charlie.getWealth());
         }
         //Waypoint has gold, if this waypoint is a treasure, then we will set the gold value to zero
         // AFTER the aStar(target, treasure) is finished running
         if (target.getGold() > 0) {
            charlie.setWealth(charlie.getWealth()+target.getGold());
            statusReport("Gold (" + target.getPoint().getX() + ", " + target.getPoint().getY() + "), "
                           + "$ " + target.getGold() + ", " + "Player " + charlie.getStrength() + " $ " + charlie.getWealth());
            target.setGold(0);
         }
         /*************************************************************************************************************************
         //Not used in project 2. If wanted, can be uncommented and used. Complies and works.
         if (charlie.getStrength() <= 0) {
            setStatus("No more strength to move.");
            statusReport("Failure (" + current_waypoint.getPoint().getX() + ", " + current_waypoint.getPoint().getY() + "), " 
                + "Player " + charlie.getStrength() + " $ " + charlie.getWealth());
            setSimRunning(false);
            animatePanel.setComponentState(false, false, false, false, true);
             
              //Break to to not continue status reports, if the destination was a magenta waypoint, it will call 
              //the status reports for Map and Start again.  Don't want that!
              
            break;
         }
         ***************************************************************************************************************************/
         /*
          * Need to make sure that charlie doesn't have a treasure before it lands on the destination waypoint.
          * Because what happens if the shortest path from the magenta waypoint to the treasure passes through the 
          * destination? The simulation will end! and you wouldn't even get your treasure waypoint.
          */
         if (charlie.getPoint().equals(dest_waypoint.getPoint()) && charlie.hasTreasure() == false) {
            setStatus("Destination has been reached.");
            statusReport("Success, goal (" + dest_waypoint.getPoint().getX() + ", " + dest_waypoint.getPoint().getY() + "), "
                              + "Player " + charlie.getStrength() + " $ " + charlie.getWealth());
            setSimRunning(false);
            animatePanel.setComponentState(false, false, false, false, true);
            /*
             * Break to to not continue status reports, if the destination was a magenta waypoint, it will call 
             * the status reports for Map and Start again.  Don't want that!
             */
            break;
         }
         //Found a treasure map
         if ((target.getMapX() != 0) && (target.getMapY() != 0) && charlie.hasTreasure() == false) {
            statusReport("Map (" + target.getPoint().getX() + ", " + target.getPoint().getY() + ") "
                              + "Treasure (" + target.getMapX() + ", " + target.getMapY() + ") Player " 
                              + charlie.getStrength() + " $ " + charlie.getWealth());
            charlie.setTreasure(true);
            Point pointTreasure = new Point(target.getMapX(), target.getMapY());
            treasure = map.getWayPoint(pointTreasure);
            /*
             * In our implementation of getting the mapX and mapY it would be easier to just set mapX and mapY values to zero
             * Therefore, charlie will know if the waypoint has mapX and mapY values of 0, just continue on his path
             */
            target.setMapX(0);
            target.setMapY(0);
            aStar(target, treasure);
            /*
             * After finding the path to the treasure and getting the treasure, set the gold value of that waypoint to zero
             * and making sure charlie doesn't have a treasure map anymore. So, now if he walks to a magenta waypoint, he will have to 
             * go treasure hunting again.
             */
            charlie.setTreasure(false);
            aStar(treasure, dest_waypoint);
            break;
         }
      }
   }
   /**
   Simulate the algorithm.
   */
   public synchronized void simulateAlgorithm() {
   	// Declare and set any local control variables.
   	// Or set up the initial algorithm state:
   	// declare and set any algorithm specific varibles
      while (runnable()) {
      	// put your algorithm code here.
      	// ...
      	// The following statement must be at end of any
      	// overridden abstact simulateAlgorithm() method
         statusReport(map.mapInfo());
         statusReport("Start (" + initial_waypoint.getPoint().getX() + ", " + initial_waypoint.getPoint().getY() + "), "
            	+ "Stop (" + dest_waypoint.getPoint().getX() + ", " + dest_waypoint.getPoint().getY() + "), Player " 
                + charlie.getStrength() + " $ " + charlie.getWealth());
         aStar(current_waypoint, dest_waypoint);
      }
   }
}