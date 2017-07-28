 
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Item carrying;
    private int strength;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        
        carrying = null;
        strength = 10000; // 10 kg
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room items
        outside.addItem(new Item("bench","a place to sit",20000));
        theater.addItem(new Item("ticket","a paper slip that allows you to attend a show",10));
        pub.addItem(new Item("mug","something that holds beer", 400));
        lab.addItem(new Item("flask","container for chemicals",150));
        lab.addItem(new Item("burner","a heater for mixtures",300));
        office.addItem(new Item("pen","a device to write with",20));
        office.addItem(new Item("stapler","a tool to join pieces of paper",100));
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch(commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "quit":
                wantToQuit = quit(command);
                break;
            case "describe":
                describeItem(command);
                break;
            case "grab":
                pickUpItem(command);
                break;
            case "drop":
                dropItem(command);
                break;
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * Prints an object's description
     */
    private void describeItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Describe what?");
            return;
        }
        
        String itemName = command.getSecondWord();
        Item thing = currentRoom.searchFor(itemName);
        if( thing == null ) {
            System.out.println("This item isn't in this room.");
            return;
        }
        
        System.out.println( thing.getDescription() );
    }
    
    /** 
     * Try to pick an object up. Fails if (1) the object is not in the present room,
     * (2) if the player is already carrying something or (3) if the object is too heavy
     * 
     * @return boolean success
     */
    private boolean pickUpItem(Command command) 
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
     * Try to pick an object up. Fails if (1) the object is not in the present room,
     * (2) if the player is already carrying something or (3) if the object is too heavy
     * 
     * @return boolean success
     */
    private boolean dropItem(Command command) 
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
