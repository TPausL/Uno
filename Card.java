import java.util.Arrays;

/**
 * Card
 */
public class Card implements HasId {

    private String value;
    private String color;
    private int points;

    Card(String value, String color) {
        this.color = color;
        this.value = value;
        String[] twenty = { "+2", "r", "s" };
        String[] fifty = { "c", "+4" };
        if (Arrays.asList(fifty).contains(value)) {
            points = 50;
        } else if (Arrays.asList(twenty).contains(value)) {
            points = 20;
        } else {
            points = Integer.parseInt(value);
        }
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

    public int getPoints() {
        return points;
    }

    public String getId() {
        return color + "_" + value;
    }

    public String toString() {
        return color + "_" + value;
    }

    public boolean isEqual(Card c) {
        return this.getId().equals(c.getId());
    }
}