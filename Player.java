import java.nio.charset.Charset;
import java.util.Random;

/**
 * ClientModel
 */
public class Player extends GameClient {

    private String name = "unknown";
    private List<Card> cards;

    Player(String ip, Integer port) {
        super(ip, port);
        cards = new List<Card>();
    }

    Player(GameClient client) {
        super(client.ip, client.port, client.id);
        cards = new List<Card>();
    }

    public List<Card> getCards() {
        return cards;
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
            if (c.toString().equals(cur.toString())
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

}