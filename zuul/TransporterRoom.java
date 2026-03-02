import java.util.Random;
/**
 * Class TransportRoom - a transporter room in an adventure game.
 *<p>
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *<p>
 * A "TransportRoom" represents one location in the scenery of the game.
 * Whenever you leave the TransportRoom you will be teleported to a random other room.
 * @author Jacob Hubbard 101348462
 * @version Feb 26 2026
 */

public class TransporterRoom extends Room{

    private final Random rand = new Random();

    /**
     * Constructor for TransporterRoom
     * @param description
     */
    public TransporterRoom(String description)
    {
        super(description);
    }

    /**
     * Returns a random room, independent of the direction parameter.
     *
     * @param direction Ignored.
     * @return A randomly selected room.
     */
    public Room getExit(String direction)
    {
        return allRooms.get(rand.nextInt(allRooms.size()));
    }
}
