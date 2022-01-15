import java.util.Arrays;

/**
 * Game
 */
public class ConnectionHandler extends Server {

    private List<Player> connected;
    private Game game;
    private boolean gameRunning;

    public ConnectionHandler(Integer port) {
        super(port);
        game = new Game();
        connected = new List<>();
    }

    public void processClosingConnection(String pClientIP, int pClientPort) {
        System.out.println("Disconnected/" + pClientIP + ":" + pClientPort);
        GameClient disconClient = null;
        for (connected.toFirst(); connected.hasAccess(); connected.next()) {
            GameClient c = connected.getContent();
            if (c.ipEquals(pClientIP) && c.portEquals(pClientPort)) {
                disconClient = c;
            }
        }
        Util.removeFromList(connected, (Player) disconClient);
        if (gameRunning) {
            if (!game.isPlaying(disconClient))
                return;
            boolean isCurrent = game.removePlayer(disconClient);
            if (checkWin()) {
                gameRunning = false;
                return;
            }
            if (isCurrent) {
                sendToAll("top:" + game.getTop());
                sendToClient(game.getCurrent(), "turn:start");
            }

        }

    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        Player sender = Util.getPlayerByIp(connected, pClientIP, pClientPort);
        String[] split = pMessage.split(":");
        String command = split[0].toLowerCase();
        String data = "";
        try {
            data = split[1].toLowerCase();
        } catch (Exception e) {
        }
        switch (command) {
            case "name":
                sender.setName(data);
                sendToClient(sender, "ok");
                return;
            case "start":
                if (gameRunning) {
                    sendToClient(sender, "error:Game running!");
                    return;
                }
                gameRunning = true;
                game.start(connected);
                List<Player> p = game.getPlayers();
                for (p.toFirst(); p.hasAccess(); p.next()) {
                    Card[] cards = game.deal(p.getContent().getId());
                    for (Card c : cards) {
                        sendToClient(p.getContent(), "card:" + c);

                    }
                }
                sendToAll("top:" + game.getTop());
                sendToClient(game.getCurrent(), "turn:start");
                return;
            case "card":
                if (!sender.idEquals(game.getCurrent().getId())) {
                    sendToClient(sender, "error:Not Your turn");
                    return;
                }
                Card card = game.parseCard(data);
                if (card == null) {
                    this.sendToClient(sender, "error:Invalid card");
                    return;
                }
                Card[] toNext = game.playCard(card);
                if (toNext == null) {
                    this.sendToClient(sender, "error:You aren't allowed to play this card or don't own it!");
                    return;
                }
                for (Card c : toNext) {
                    this.sendToClient(game.getNext(), "card:" + c);
                    game.getNext().addCard(c);

                }

                this.sendToClient(sender, "turn:end");
                this.sendToAll("top:" + game.getTop());
                if (checkWin())
                    return;
                game.next();
                this.sendToClient(game.getCurrent(), "**" + Util.listToString(game.getCurrent().getCards()) + "**");
                this.sendToClient(game.getCurrent(), "turn:start");
                return;
            case "draw":
                Card c = game.draw();
                if (c == null) {
                    this.sendToClient(sender, "error:You can only draw once per turn!");
                    return;
                }
                this.sendToClient(sender, "card:" + c);
                return;
            case "turn":
                if (data.equals("end")) {
                    if (!game.turnDone()) {
                        this.sendToClient(sender, "error:You must either play or draw a card");
                        return;
                    }
                    this.sendToClient(sender, "turn:end");
                    game.next();
                    if (checkWin())
                        return;
                    this.sendToAll("top:" + game.getTop());
                    this.sendToClient(game.getCurrent(), "**" + Util.listToString(game.getCurrent().getCards()) + "**");
                    this.sendToClient(game.getCurrent(), "turn:start");
                } else {
                    this.sendToClient(sender, "error:Invalid command");
                }
                return;
            default:
                this.sendToClient(sender, "error:Invalid command");
                break;
        }
    }

    public void processNewConnection(String pClientIP, int pClientPort) {
        System.out.println("Connected/" + pClientIP + ":" + pClientPort);
        connected.append(new Player(pClientIP, pClientPort));
    }

    private void sendToClient(GameClient p, String message) {
        this.send(p.getIp(), p.getPort(), message);
    }

    private boolean checkWin() {
        Player winner = game.checkWin();
        if (winner != null) {
            sendToAll("win:" + winner.getName());
            gameRunning = false;
            return true;
        }
        return false;
    }

}