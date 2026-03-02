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
 * @version February 27, 2026 (updated for Assignment 2)
 *
 */
public class Game
{
    private static Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> stackBack;
    private Item currentItem;
    private int numItemsTaken;
    private static final int numItemsAllowedTakenBeforeEat = 5;

    /**
     * Create the game and initialise its internal map.
     */
    public Game()
    {
        numItemsTaken = 0;
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
        TransporterRoom transporter;

        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporter = new TransporterRoom("in a transporter room");

        Item paperclip = new Item("paperclip", "A perfectly average paperclip", 2);
        Item shrek = new Item("shrek", "A life-size statue of Shrek in all of his greatness", 100);
        Item jar = new Item("jar", "A non-suspicious jar", 12000);
        Item stapler = new Item("stapler", "A regular stapler", 1.5);
        Item paper = new Item("paper", "A sheet of paper with some writing on it", 0.01);
        Item pen = new Item("pen", "An expensive-looking pen", 0.3);
        Item mask = new Item("mask", "A classic comedy mask", 0.5);
        Item microscope = new Item("microscope", "A high-power microscope", 1);
        Item cookie = new Item("cookie", "A scrumptious chocolate chip cookie", 0.1);
        Item beamer1 = new Beamer("beamer", "A beamer that lets you teleport back to this room after being charged and fired", 1.3);
        Item beamer2 = new Beamer("beamer", "A beamer that lets you teleport back to this room after being charged and fired", 1.3);

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);
        theatre.setExit("east", transporter);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        office.setExit("north", transporter);

        transporter.setExit("west", theatre);
        transporter.setExit("south", office);

        // initialize room items
        outside.addItem(paperclip);
        outside.addItem(beamer1);

        theatre.addItem(mask);
        theatre.addItem(cookie);

        pub.addItem(shrek);
        pub.addItem(cookie);

        lab.addItem(paper);
        lab.addItem(jar);
        lab.addItem(cookie);
        lab.addItem(microscope);

        office.addItem(stapler);
        office.addItem(pen);
        office.addItem(cookie);

        transporter.addItem(beamer2);

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
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
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

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom;
            stackBack.add(currentRoom);
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            if (currentItem != null) {
                System.out.println("Holding: " + currentItem.getName());
            }
            else{
                System.out.println("Holding nothing");
            }
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
            if (currentItem != null) {
                System.out.println("Holding: " + currentItem.getName());
            }
            else{
                System.out.println("Holding nothing");
            }
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
        else if (currentItem.getName().equals("Cookie")){
            System.out.println("You have eaten your cookie!");
            currentItem = null;
            numItemsTaken = 0;
        }
        else {
            System.out.println("You are not carrying food.");
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
        else if (previousRoom == null) {
            System.out.println("Cannot go back from the start!");
        }
        else {
            Room nextRoom = previousRoom;
            stackBack.add(currentRoom);
            previousRoom = currentRoom;
            currentRoom = nextRoom;
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
            previousRoom = currentRoom;
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

        // Check to see if you can pick up another item
        if (numItemsTaken >= numItemsAllowedTakenBeforeEat && !name.equals("Cookie")) {
            if (currentItem != null && currentItem.getName().equals("Cookie")){
                System.out.println("You need to eat your cookie before picking up any other items");
            }
            else{
                System.out.println("You need to eat a cookie before picking up any other items");
            }
            return;
        }

        // Loop through the items in currentRoom and remove
        for (Item item : currentRoom.getItems())
        {
            if (name.equals(item.getName())) {
                currentItem = item;
                currentRoom.removeItem(item);
                System.out.println("You picked up " + item.getName());
                numItemsTaken += 1;
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
        if (command.hasSecondWord()) {
            System.out.println("Drop what?");
        }
        else {
            if (currentItem == null) {
                System.out.println("You need to pick up an item before you can drop");
            }
            else {
                currentRoom.addItem(currentItem);
                currentItem = null;
                System.out.println("You dropped your item!");
            }
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
            if (!beamer.getCharged())
            {
                beamer.charge(currentRoom);
            }
            else{
                System.out.println("You have already charged your beamer!");
            }
        }
        else {
            System.out.println("Current Item is not a beamer!");
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
            if (beamer.getCharged())
            {
                Room nextRoom = beamer.fire();
                if (nextRoom != null)
                {
                    previousRoom = currentRoom;
                    currentRoom = nextRoom;
                    System.out.println("Beamer fired!\n" + currentRoom.getLongDescription());
                }
            }
            else {
                System.out.println("You need to charged your beamer first!");
            }
        }
        else {
            System.out.println("Current item is not a beamer!");
        }
    }
}