/**
 * Card
 */
public class Card {

    private String value;
    private String color;

    Card(String value, String color) {
        this.color = color;
        this.value = value;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return color + "_" + value;
    }
}