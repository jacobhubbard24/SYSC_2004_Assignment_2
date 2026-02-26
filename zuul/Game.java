import java.util.Stack;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.  Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 * <p>
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns.
 *
 * @author Michael Kolling and David J. Barnes
 * @author Lynn Marshall
 * @version October 21, 2012
 *
 * @author Jacob Hubbard 101348462
 * @version February 10, 2026
 *
 */
public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Stack<Room> stackBack;
    private Item currentItem;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        createRooms();
        parser = new Parser();
        stackBack = new Stack<Room>();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;

        // create the rooms
        outside = new Room("outside the main entrance of the university", 1);
        theatre = new Room("in a lecture theatre", 2);
        pub = new Room("in the campus pub", 3);
        lab = new Room("in a computing lab", 4);
        office = new Room("in the computing admin office", 5);

        Item averageBanana = new Item("Banana", "Plenty big enough banana, honestly too big", 0.2);
        Item shrek = new Item("Shrek", "A life-size statue of Shrek in all of his greatness", 100);
        Item juice = new Item("Juice", "A non-suspicious bottle of juice", 700);
        Item stapler = new Item("Stapler", "A regular stapler", 1.5);
        Item paper = new Item("Paper", "A sheet of paper with some writing on it", 0.01);
        Item pen = new Item("Pen", "An expensive-looking pen", 0.3);
        Item mask = new Item("Mask", "A classic comedy mask", 0.5);
        Item microscope = new Item("Microscope", "A ", 1);


        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        // initialize room items
        outside.addItem(averageBanana);

        theatre.addItem(mask);

        pub.addItem(shrek);

        lab.addItem(paper);

        lab.addItem(juice);

        office.addItem(stapler);

        currentRoom = outside;  // start game outside
    }

    /**
     * Main play routine.  Loops until end of play.
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
     *
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command)
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp()
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     *
     * @param command The command to be processed
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
        stackBack.add(currentRoom);

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
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
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
     * Take a look around the room
     * @param command
     */
    private void look(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            System.out.println(currentRoom.getLongDescription());
        }

    }

    /**
     * Eat something
     * @param command
     */

    private void eat(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        else {
            System.out.println("You have eaten and are no longer hungry.");
        }
    }

    /**
     * Go back 1 room. Will loop back and forth between last 2 rooms visited
     * @param command
     */
    private void back(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else if (stackBack.isEmpty()) {
            System.out.println("Cannot go back from the start!");
        }
        else {
            Room temp = currentRoom;
            currentRoom = stackBack.peek();
            stackBack.add(temp);
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Go back 1 room, all the way to the beginning
     * @param command
     */
    private void stackBack(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else if (stackBack.isEmpty()) {
            System.out.println("Cannot go back from the start!");
        }
        else {
            currentRoom = stackBack.pop();
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /**
     * Take an item from the current room
     * @param command
     */
    private void take(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }
        String name = command.getSecondWord();

        // Loop through the items in currentRoom and remove
        for (Item item : currentRoom.getItems())
        {
            if (name.equals(item.getDescription()))
            {
                currentItem = item;
                currentRoom.getItems().remove(item);
                System.out.println("You picked up " + item.getName());
                return;
            }
        }
        System.out.println("There is no " + name + " in the room");

    }

    /**
     * Drop an item into the current room
     * @param command
     */
    private void drop(Command command)
    {
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
        }
        else {
            System.out.println(currentRoom.getLongDescription());
        }

    }

    /**
     * Attempts to charge the beamer.
     * @param command
     */
    private void charge(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Charge what?");
        }
        else if (currentItem instanceof Beamer beamer)
        {
            beamer.charge(currentRoom);
            System.out.println("Charged beamer with current room!");
        }
    }

    /**
     * Attempts to fire the beamer
     * @param command
     */
    private void fire(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Charge what?");
        }
        else if (currentItem instanceof Beamer beamer)
        {
            Room nextRoom = beamer.fire();
            if (nextRoom != null)
            {
                currentRoom = beamer.fire();
                System.out.println("Beamer fired! Now in: " + currentRoom.getLongDescription());
            }
            else {
                System.out.println("You have not charged!");
            }
        }
        else {
            System.out.println("Current item is not a beamer!");
        }
    }
}
