import java.nio.charset.Charset;
import java.util.Random;

/**
 * ClientModel
 */
public class ClientModel {

    private String name = "unknown";
    private String ip, id;
    private Integer port;
    private List<Card> cards;

    ClientModel(String ip, Integer port) {
        this.id = genId();
        this.ip = ip;
        this.port = port;
        cards = new List<Card>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCards(List<Card> cards) {
        cards.concat(cards);
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

    private String genId() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    public String toString() {
        return this.id;
    }
}