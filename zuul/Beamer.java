/**
 * Class Beamer - an item in an adventure game
 *<p></p>
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *<p></p>
 * A "Beamer" represents an item that can teleport the player
 * back to the room it was charged in
 *
 * @author Jacob Hubbard 101348462
 * @version February 27, 2026
 */

public class Beamer extends Item{
    private boolean charged;
    private Room room;

    /** Constructor to initialize the beamer.
     *
     * @param name
     * @param description
     * @param weight
     */
    public Beamer(String name, String description, double weight) {
        super(name, description, weight);
        charged = false;
        room = null;
    }

    /**
     * Charges the beamer
     * @param _room the room to charge the beamer with
     */
    public void charge(Room room){
        charged = true;
        this.room = room;
        System.out.println("Charging beamer with current room!");
    }

    /**
     * Getter for charged
     * @return charged
     */
    public boolean getCharged(){
        return charged;
    }

    /**
     * Fire the beamer, returning the room to beam to
     * @return the room to beam to
     */
    public Room fire(){
        charged = false;
        return room;
    }
}
