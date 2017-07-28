
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private Item carrying;
    private int strength;
    private Room currentRoom;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        // initialise instance variables
        carrying = null;
        strength = 10000; // 10 kg
    }
    public void setLocation(Room room)
    {
        currentRoom = room;
    }
    public Room getLocation()
    {
        return currentRoom;
    }
    
    public boolean hasItem(String thing)
    {
        return( carrying != null && thing.equals(carrying.getName()) );
    }
    public Item onMe()
    {
        return carrying;
    }

    /** 
     * Try to pick an object up. Fails if (1) the object is not in the present room,
     * (2) if the player is already carrying something or (3) if the object is too heavy
     * 
     * @return boolean success
     */
    public boolean pickUpItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Grab what?");
            return false;
        }
        
        String itemName = command.getSecondWord();
        if( carrying != null && itemName.equals( carrying.getName() ) ) {
            System.out.println("Uh, you're carrying it already.");
            return false;
        }
        
        Item thing = currentRoom.searchFor(itemName);
        if( thing == null ) {
            System.out.println("This item isn't in this room!");
            return false;
        }
        if( thing.getWeight() > strength ) {
            System.out.println("This item is too heavy!");
            return false;
        }
        if( carrying != null ) {
            System.out.println("First drop what you're carrying!");
            return false;
        }
        carrying = thing;
        currentRoom.removeItem(thing);
        
        return true;
    }

    /** 
     * Try to drop an object. Fails if (1) the player isn't carrying anything, or
     * (2) if the player is not carrying this object
     * 
     * @return boolean success
     */
    public boolean dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return false;
        }
        
        String itemName = command.getSecondWord();
        if( carrying == null) {
            System.out.println("Uh, you're not carrying anything.");
            return false;
        }
        if( ! itemName.equals(carrying.getName()) ) {
            System.out.println("You're not carrying this object!");
            return false;
        }
        currentRoom.addItem(carrying);
        carrying = null;
        
        return true;
    }
}
