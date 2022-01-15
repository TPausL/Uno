import java.util.Arrays;

public class Game {

    private Pile pile;
    private List<Player> connected;
    private String[] playing;
    private int current, direction;
    private boolean drawn, turnDone, skip;

    public void start(List<Player> clients) {
        this.connected = clients;
        pile = new Pile();
        playing = new String[Util.listLength(connected)];
        direction = 1;
        int i = 0;
        for (clients.toFirst(); clients.hasAccess(); clients.next()) {
            playing[i] = clients.getContent().getId();
            i++;
        }
        current = (int) Math.floor(Math.random() * playing.length);
    }

    public boolean isPlaying(GameClient client) {
        return Util.findInList(connected, (Player) client) != null;
    }

    public List<Player> getPlayers() {
        return this.connected;
    }

    // 0,1,2 1
    // 0,2
    // TODO wenn remove > current wird einer übersprungen
    public boolean removePlayer(GameClient c) {
        Player player = (Player) c;
        Util.removeFromList(connected, (Player) c);
        String currentId = playing[current];
        playing = Util.removeFromArray(playing, c.getId());
        pile.reinsert(player.getCards());
        if (player.idEquals(currentId)) {
            next();
            return true;
        } else {
            int i = 0;
            for (String id : playing) {
                if (id != currentId) {
                    i++;
                } else {
                    current = i;
                    return false;
                }
            }
        }
        return false;
    }

    public Card getTop() {
        return pile.getTop();
    }

    public Player getCurrent() {
        if (playing == null)
            return null;
        if (playing.length <= 0) {
            return null;
        }
        return getById(playing[current]);
    }

    public Player getNext() {
        return getById(playing[(((current + direction) % playing.length) + playing.length) % playing.length]);
    }

    public void next() {
        drawn = false;
        turnDone = false;
        current = ((((current + (skip ? 2 : 1) * direction)) % playing.length) + playing.length) % playing.length;
        skip = false;
    }

    private void reverse() {
        direction *= -1;
    }

    private Player getById(String id) {
        for (connected.toFirst(); connected.hasAccess(); connected.next()) {
            Player p = connected.getContent();
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Card[] deal(String id) {
        Player p = getById(id);
        Card[] cards = new Card[7];
        List<Card> c = new List<>();
        for (int i = 0; i < 7; i++) {
            Card t = pile.take();
            cards[i] = t;
            c.append(t);
        }
        p.addCards(c);
        return cards;
    }

    public Card draw() {
        if (drawn)
            return null;
        drawn = true;
        Card c = pile.take();
        getById(playing[current]).addCard(c);
        turnDone = true;
        return c;
    }

    public Card[] playCard(Card c) {
        if (checkActionValidity(c)) {
            Player current = getCurrent();
            if (current.removeCard(c)) {
                turnDone = true;
                pile.putDown(c);
                return executeCardActions(c);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Card[] executeCardActions(Card c) {
        switch (c.getValue()) {
            case "+2":
                skip = true;
                return pile.take(2);
            case "+4":
                skip = true;
                return pile.take(4);
            case "s":
                skip = true;
                return new Card[0];
            case "r":
                reverse();
                return new Card[0];
            default:
                return new Card[0];
        }
    }

    private boolean checkActionValidity(Card c) {
        if (c.getValue().equals("+4") || c.getValue().equals("s"))
            return true;
        Card top = pile.getTop();
        String cVal = c.getValue(), cCol = c.getColor();
        return cVal.equals(top.getValue()) || cCol.equals(top.getColor()) || cVal.equals("+4") || cVal.equals("c");
    }

    public Card parseCard(String data) {
        String color, value;
        try {
            String[] split_data = data.split("_");
            color = split_data[0];
            value = split_data[1];
            String[] colors = { "r", "g", "b", "y" };
            String[] values = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+2", "+4", "s", "r", "c" };
            if (!Arrays.asList(colors).contains(color) || !Arrays.asList(values).contains(value))
                throw new Exception();
        } catch (Exception e) {
            return null;
        }
        return new Card(value, color);
    }

    public Player checkWin() {
        if (playing.length == 1)
            return getById(playing[0]);
        for (String id : playing) {
            if (getById(id).getCardCount() == 0) {
                return getById(id);
            }
        }
        return null;
    }

    public boolean turnDone() {
        return this.turnDone;
    }
}
