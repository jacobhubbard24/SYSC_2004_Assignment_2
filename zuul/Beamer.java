/**
 * Class Beamer - an item in an adventure game
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * A "Beamer" represents an item that can teleport the player
 * back to the room it was charged in
 *
 * @author Jacob Hubbard
 * @version February 26, 2026
 */

public class Beamer extends Item{
    private Boolean charged;
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
     */
    public void charge(Room _room){
        if (!charged){
            charged = true;
            room = _room;
        }
        else {
            System.out.println("You have already charged");
        }
    }

    /**
     * Fire the beamer. If successful, returns the room to beam to,
     * otherwise returns null
     * @return the room to beam to
     */
    public Room fire(){
        if (!charged){
            System.out.println("Canot fire as this beamer is not charged");
            return null;
        }
        else{
            charged = false;
            return room;
        }
    }
}
