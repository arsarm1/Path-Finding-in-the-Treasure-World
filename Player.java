/* Player.java
 * Authors: Armen Arslanian
 *          Anderson Giang
 * COMP 282
 * November 8, 2015
 * Professor Mike Barnes
 */
import java.awt.*;
import SimulationFramework.*;
/**<p>Player inherits Bot so it moves along the path.</p>
 * @author Armen Arslanian
 * @author Anderson Giang
 * @version 1.0
 * @since 11/8/2015
 */
public class Player extends Bot {
   /**Player's strength. Player starts with a strength of 2000*/
   private int strength = 2000;
   /**Player's wealth. Player starts with a wealth of 1000*/
   private int wealth = 1000;   
   /**If there is treasure or not.*/
   private boolean treasure = false;
   /**<p>Constructor that creates a Player</p>
    * @param label name of player
    * @param p starting point of player
    * @param colorValue color of player
    */
   public Player(String label, Point p, Color colorValue) {
      super(label, p, colorValue); //use suitable constructor from Bot class
   }	
   /**<p>Get Method to return strength of a player.</p>
    * @return Strength of player.
    */
   public int getStrength() {
      return strength;
   }
   /**<p>Get Method to return strength value of a player.</p>
    * @return Wealth of player.
    */
   public int getWealth() {
      return wealth;
   }
   /**<p>Set Method to set player's strength.
    * Distance that the player moves is subtracted from player's strength.
    * Player can buy strength (increase strength) up to the city's cost.</p>
    * @param strength int variable.
    */
   public void setStrength(int strength) {
      this.strength = strength;
   }
   /**<p>Set Method to set wealth of player.
    * Player can increase wealth when landing on a WayPoint that has gold.</p>
    * @param wealth int variable 
    */
   public void setWealth(int wealth) {
      this.wealth = wealth;
   }
   /**<p>Get Method to return if there is treasure on the WayPoint.</p>
    * @return If there is treasure or not.
    */
   public boolean hasTreasure() {
      return treasure;
   }
   /**<p>Set Method to set if the player has treasure or not.
    * True means there is treasure.</p>
    * @param treasure boolean variable
    */
   public void setTreasure(boolean treasure) {
      this.treasure = treasure;
   }
   /**<p>Calls moveTo from Bot class.
    * This is how the player will move to the next WayPoint.</p>
    * @param pt point variable
    */
   public void move(Point pt) { 
      moveTo(pt);
   }
}//end Player class

