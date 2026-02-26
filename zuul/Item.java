/**
 * Class Item - an item in an adventure game
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * An "Item" represents an item with a description and a weight.
 * Ex: description: "A non-suspicious bottle of juice", weight = 700 kg
 *
 * @author Jacob Hubbard
 * @version February 10, 2026
 */

public class Item {
    private String description;
    private double weight;
    private String name;

    /** Constructor to initialize the item
     *
     * @param name
     * @param description
     * @param weight
     */
    public Item(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Getter for description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }
}
