import java.nio.charset.Charset;
import java.util.Random;

/**
 * ClientModel
 */
public class Player extends GameClient {

    private String name = "unknown";
    private List<Card> cards;
    private int points;

    Player(String ip, Integer port) {
        super(ip, port);
        cards = new List<Card>();
        points = 0;
    }

    Player(GameClient client) {
        super(client.ip, client.port, client.id);
        cards = new List<Card>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void clearCards() {
        this.cards = new List<>();
    }

    public void addCards(List<Card> cards) {
        this.cards.concat(cards);
    }

    public void addCard(Card card) {
        cards.append(card);
    }

    public boolean removeCard(Card c) {
        for (cards.toFirst(); cards.hasAccess(); cards.next()) {
            Card cur = cards.getContent();
            if (c.equals(cur)
                    || (c.getValue().equals("+4") && c.getValue().equals(cur.getValue()))
                    || (c.getValue().equals("c") && c.getValue().equals(cur.getValue()))) {
                cards.remove();
                return true;
            }
        }
        return false;
    }

    public int getCardCount() {
        int i = 0;
        for (cards.toFirst(); cards.hasAccess(); cards.next()) {
            i++;
        }
        return i;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void resetPoints() {
        this.points = 0;
    }

    public int getCardPoints() {
        int p = 0;
        for (cards.toFirst(); cards.hasAccess(); cards.next()) {
            p += cards.getContent().getPoints();
        }
        return p;
    }

    public int getPoints() {
        return points;
    }

    public String toString() {
        return this.name;
    }

}