/* Map.java
 * Authors: Armen Arslanian
 *          Anderson Giang
 * COMP 282
 * November 8, 2015
 * Professor Mike Barnes
 */
import java.util.Scanner;
import java.io.*;
import java.util.HashMap;
import java.awt.*;
import java.util.Iterator;
import java.util.ArrayList;
import SimulationFramework.*;
import java.util.Collection;
/**<p>Map of all WayPoints.</p>
 * @author Armen Arslanian
 * @author Anderson Giang
 * @version 1.0
 * @since 11/8/2015
 */
public class Map {
   /**Hold the number of gold WayPoints*/
   private int numOfGold = 0;
	/**Hold the number of treasure maps*/
   private int numOfMaps = 0;
	/**Hold the number of cities*/
   private int numOfCities = 0;
   /**HashMap of all WayPoints from text file*/
   private HashMap<Point, WayPoint> myMap = new HashMap<Point,WayPoint>();
   /**ArrayList of neighbors from text file*/
   private ArrayList<Point> neighbors = new ArrayList<Point>();
   /**For GUI components
    * @see SimFrame from SimulationFramework
    */
   private AnimatePanel animatePanel;
   /**For GUI components
    * @see SimFrame from SimulationFramework
    */
   private SimFrame simFrame;
   /**<p>Constructor that creates a Map with the Waypoints.</p> 
    * @param simFrame simulation frame
    */
   public Map(SimFrame simFrame) {
      this.simFrame = simFrame;
      animatePanel = simFrame.getAnimatePanel();
      readWayPoints();
   }
   /**<p>This method reads WayPoints from waypoint.txt file.
    * It parses information needed from WayPoints.
    * Then, it creates the WayPoints and puts them in a HashMap.
    * 16 values for 1 WayPoint. Check if there is a neighbor Point, 
    * once there is, it will always be the default value of [8],
    * which will always be the neighbor point x and the index + 1 will be
    * the neighbor point y and we will be incrementing our i by two to get
    * index 10 for the next x neighbor point and so on....</p>
    */
    /*      	          x    y     H    C    G   MAPX&Y  N
    * example: WayPoint  40   300   68   67   0   0   0   4   (60   300)   (20   300)   (60   320)   (40   320)
    *       	  Index   0    1     2    3    4   5   6   7    8     9      10    11     12   13      14    15
    */
   public void readWayPoints() {
      Scanner input = null;
      try { 
         input = new Scanner(new File("waypoint.txt"));        
      }
      catch(FileNotFoundException e) {
         System.out.println("Could not find waypoint.txt. \n Please make sure it is in the correct directory.");
         return;
      }
      while (input.hasNextLine()) {
         //regular expression for java in spaces.
         String[] token = input.nextLine().trim().split("\\s+");
         //creating an ArrayList of neighbor points. Initialize it here because every time we go to a new line         
         ArrayList<Point> listOfNPoints = new ArrayList<Point>();
         int x = Integer.parseInt(token[0]);
         int y = Integer.parseInt(token[1]);
         int height = Integer.parseInt(token[2]);
         int cityCost = Integer.parseInt(token[3]);
         int gold = Integer.parseInt(token[4]);
         int mapX = Integer.parseInt(token[5]);
         int mapY = Integer.parseInt(token[6]);
         int countOfNeighbors = Integer.parseInt(token[7]);
         if (token.length > 7) {
            for (int i = 8; i < token.length - 1; i+=2) {
               int neighborX = Integer.parseInt(token[i]);
               int neighborY = Integer.parseInt(token[i+1]);
               //Create point object to add it into the ArrayList of neighbor points
               Point myPoint = new Point(neighborX, neighborY);
               listOfNPoints.add(myPoint);
            }
         }
         //count how many gold WayPoints
         if (gold > 0) {
            numOfGold++;
         }
         //count how many treasure maps
         if (mapX != 0 && mapY != 0) {
            numOfMaps++;
         }
         //count how many cities
         if (cityCost > 0) {
            numOfCities++;
         }
         /* create WayPoint object with all of the variable parameters,
          * how come we didn't include the count of neighbors in the WayPoint constructor?
          */
         WayPoint wp = new WayPoint(x, y, height, cityCost, gold, mapX, mapY, countOfNeighbors, listOfNPoints);
         Point myPoint = new Point(x, y);
         myMap.put(myPoint, wp);
         animatePanel.addPermanentDrawable(wp);
         //connectors to see potential paths
         for (int i = 0; i < wp.getNeighbors().size(); i++) {
            Connector c = new Connector(wp.getPoint(),wp.getNeighbors().get(i), Color.black);
            animatePanel.addPermanentDrawable(c);
         }
      }//end of while loop
      input.close(); //close file
   }  
	/**<p>This method finds the closest WayPoint to the given point.
	 *    Calculates the two dimensional distance between the given point
	 *    and ALL of the WayPoints in the HashMap.
	 *    Therefore, chooses closest WayPoint by shortest distance.</p>
	 * @param p given point (will find closest WayPoint to p)
	 * @return WayPoint (closest one).
	 */
   public WayPoint findClosestWayPoint(Point p) {
      Iterator<WayPoint> iterator = myMap.values().iterator();
   	//This is the WayPoint in the iterator.
      WayPoint closest_waypoint = iterator.next();	   
      Point point = closest_waypoint.getPoint();
   	//compute distance formulas and updating variables if necessary
      double closest_distance = twoDimDistance(point, p);
      while (iterator.hasNext()) {		
         //This is the second WayPoint in the iterator.
         WayPoint wp = iterator.next();
         Point nextPoint = wp.getPoint();
         double distance = twoDimDistance(nextPoint, p);
         if (distance < closest_distance) {
            closest_distance = distance;
            closest_waypoint = wp;
         }
      }
      return closest_waypoint;
   }//end of findClosestWayPoint
	/**<p>Helper Method.
	 *    Calculates the two dimensional distance between two points.</p>
	 * @param a given point a
	 * @param b given point b
	 * @return 2D distance between point a and b.
	 */
   public double twoDimDistance(Point a,Point b) {
      double x = Math.pow(a.getX() - b.getX(), 2);
      double y = Math.pow(a.getY() - b.getY(), 2);
      return Math.sqrt(x + y);
   }
	/**<p>Helper Method.
	 * Calculates the three dimensional distance between two WayPoints.</p>
	 * @param a given point a
	 * @param b given point b
	 * @return 3D distance between point a and b
	 */
   public double threeDimDistance(WayPoint a, WayPoint b) {
      double x = Math.pow(a.getPoint().getX() - b.getPoint().getX(), 2);
      double y = Math.pow(a.getPoint().getY() - b.getPoint().getY(), 2);
      double z = Math.pow(a.getHeight() - b.getHeight(), 2);
      return Math.sqrt(x + y + z);
   } 
	/**<p>This method returns a WayPoint from the HashMap with a unique point value.</p>
	 * @param p unique point value
	 * @return WayPoint
	 */
   public WayPoint getWayPoint(Point p) {
      WayPoint wp = myMap.get(p);
      return wp;
   }
   /**<p>This method is useful for debugging.</p>
    * @return String of map size, number of cities, number of gold WayPoints, and number of treasure maps.
    */
   public String mapInfo() {
      return "Create HashMap " + myMap.size() + " City " + numOfCities + " Gold " + numOfGold + " Map " + numOfMaps;
   }
}//end Map class