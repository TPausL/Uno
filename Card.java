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

    Card(String message) throws Exception {
        String[] split;
        try {
            split = message.split("_");
        } catch (Exception e) {
            throw new Exception("Invalid message");
        }
        this.color = split[0];
        this.value = split[1];
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