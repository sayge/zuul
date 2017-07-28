
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private String description;
    private int weight; // in grams

    /**
     * Constructor for objects of class Item
     */
    public Item(String n, String d, int w)
    {
        // initialise instance variables
        name = n;
        description = d;
        weight = w;
    }

    /**
     * getter for description
     *
     * @return    item description
     */
    public String getDescription()
    {
        return description;
    }
    /**
     * setter for description
     *
     * @param  d  new description of object
     */
    public void setDescription(String d)
    {
        description = d;
    }

    /**
     * getter for name
     *
     * @return    item name
     */
    public String getName()
    {
        return name;
    }
    /**
     * setter for name
     *
     * @param  d  new name of object
     */
    public void setName(String d)
    {
        description = d;
    }

    /**
     * getter for weight
     *
     * @return    item weight
     */
    public int getWeight()
    {
        return weight;
    }
}
