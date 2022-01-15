
public class Pile {
    private List<Card> draw, discard;
    private Card top;

    Pile() {
        draw = genCards();
        discard = new List<>();
        top = take();
    }

    public void drawTop() {
        putDown(top);
        top = take();
    }

    public Card take() {
        int length = Util.listLength(draw);
        if (length == 0)
            restock();
        draw.toFirst();
        int random = (int) Math.floor(Math.random() * length);
        for (int i = 0; i < random; i++) {
            draw.next();
        }
        Card c = draw.getContent();
        draw.remove();
        return c;
    }

    public Card getTop() {
        return top;
    }

    public void reinsert(List<Card> l) {
        draw.concat(l);
    }

    public void putDown(Card c) {
        discard.append(top);
        top = c;
    }

    private List<Card> genCards() {
        List<Card> l = new List<Card>();
        String[] colors = "r,g,y,b".split(",");
        String[] valuesSingle = "0,+4,c".split(",");
        String[] valuesDouble = "1,2,3,4,5,6,7,8,9,+2,s,r".split(",");
        for (String c : colors) {
            for (String v : valuesSingle) {
                l.append(new Card(v, c));
            }
            for (String v : valuesDouble) {

                l.append(new Card(v, c));
                l.append(new Card(v, c));
            }
        }
        return l;
    }

    private void restock() {
        draw.concat(discard);
        discard = new List<>();
    }
}
