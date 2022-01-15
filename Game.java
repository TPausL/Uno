/**
 * Game
 */
public class Game extends Server {

    private Pile pile;
    private List<ClientModel> clients;
    private String[] playerIds;
    private int curPlayer, turnDirection;
    private boolean drawn = false, started = false;

    public Game(Integer port) {
        super(port);
        pile = new Pile();
        clients = new List<ClientModel>();
        turnDirection = 1;
    }

    public void processClosingConnection(String pClientIP, int pClientPort) {
        System.out.println("disconnect/" + pClientIP + ":" + pClientPort);
        ClientModel client = getByIp(pClientIP, pClientPort);
        Util.removeFromList(clients, client);
        if (playerIds.length - 1 == 1) {
            sendToAll("win");
        }
        pile.reinsert(client.getCards());
        String[] newPlayers = new String[playerIds.length - 1];
        int i = 0;
        for (String id : playerIds) {
            if (!id.equals(client.getId())) {
                newPlayers[i] = id;
            }
            i++;
        }
        playerIds = newPlayers;
    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        String[] split = pMessage.split(":");
        String command = split[0].toLowerCase();
        String data = "";
        try {
            data = split[1].toLowerCase();
        } catch (Exception e) {
        }
        ClientModel sender = getByIp(pClientIP, pClientPort);
        try {
            switch (command) {
                case "name":
                    sender.setName(data);
                    sendToClient(sender, "ok");
                    return;
                case "start":
                    if (started) {
                        sendToClient(sender, "error:Game running");
                        return;
                    }
                    started = true;
                    playerIds = new String[Util.listLength(clients)];
                    int i = 0;
                    for (clients.toFirst(); clients.hasAccess(); clients.next()) {
                        playerIds[i] = clients.getContent().getId();
                        i++;
                        giveCardsTo(clients.getContent(), 7);
                    }
                    curPlayer = (int) Math.floor(Math.random() * playerIds.length);
                    this.sendToAll("game started");
                    sendToAll("top:" + pile.getTop());
                    sendToClient(getById(playerIds[curPlayer]), "turn:start");
                    return;
                case "card":
                    if (!pClientIP.equals(getById(playerIds[curPlayer]).getIp())
                            || pClientPort != getById(playerIds[curPlayer]).getPort()) {
                        sendToClient(sender, "error:Not your turn!");
                        return;
                    }
                    Card card = new Card(data);
                    if (checkActionValidity(card)) {
                        if (sender.removeCard(card)) {
                            pile.putDown(card);
                            executeCardActions(sender, card);
                            return;
                        } else {
                            sendToClient(sender, "error:Not in your possession");
                        }
                    } else {
                        sendToClient(sender, "error:Card not allowed");
                    }
                    return;
                case "draw":
                    if (!pClientIP.equals(getById(playerIds[curPlayer]).getIp())
                            || pClientPort != getById(playerIds[curPlayer]).getPort()) {
                        sendToClient(sender, "error:" + "Not your turn!");
                        return;
                    }
                    if (drawn) {
                        sendToClient(sender, "error:Can't draw twice!");
                    } else {
                        drawn = true;
                        giveCardsTo(sender, 1);
                    }
                    return;
                case "turn":
                    if (data.equals("end")) {
                        if (drawn) {
                            nextTurn(sender, false);
                        } else {
                            sendToClient(sender, "error:Can't end turn without playing or drawing a card!");
                        }
                    } else {
                        sendToClient(sender, "error:Invalid command");
                    }
                    return;
                default:
                    sendToClient(sender, "error:Invalid command");
                    break;
            }
        } catch (Exception e) {
            sendToClient(sender, "error:" + e.getMessage());
        }
    }

    public void processNewConnection(String pClientIP, int pClientPort) {
        System.out.println("connect/" + pClientIP + ":" + pClientPort);
        clients.append(new ClientModel(pClientIP, pClientPort));
    }

    private List<Card> giveCardsTo(ClientModel recipient, int number) {
        List<Card> returnList = new List<Card>();
        for (int i = 0; i < number; i++) {
            Card c = pile.take();
            recipient.addCard(c);
            returnList.append(c);
            sendToClient(recipient, "card:" + c);
        }
        return returnList;
    }

    private void sendToClient(ClientModel c, String message) {
        this.send(c.getIp(), c.getPort(), message);
    }

    private ClientModel getByIp(String ip, Integer port) {
        for (clients.toFirst(); clients.hasAccess(); clients.next()) {
            ClientModel c = clients.getContent();
            if (c.getIp().equals(ip) && c.getPort().equals(port)) {
                return clients.getContent();
            }
        }
        return null;
    }

    private ClientModel checkWin() {
        for (String id : playerIds) {
            if (getById(id).getCardCount() == 0) {
                return getById(id);
            }
        }
        return null;
    }

    private ClientModel getById(String id) {
        for (clients.toFirst(); clients.hasAccess(); clients.next()) {
            ClientModel c = clients.getContent();
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    private boolean checkActionValidity(Card c) {
        if (c.getValue().equals("+4") || c.getValue().equals("s"))
            return true;
        Card top = pile.getTop();
        String cVal = c.getValue(), cCol = c.getColor();
        return cVal.equals(top.getValue()) || cCol.equals(top.getColor()) || cVal.equals("+4") || cVal.equals("c");
    }

    private void executeCardActions(ClientModel sender, Card c) {
        int next = (((curPlayer + turnDirection) % playerIds.length) + playerIds.length) % playerIds.length;
        switch (c.getValue()) {
            case "+2":
                giveCardsTo(getById(playerIds[next]), 2);
                nextTurn(sender, true);
                break;
            case "+4":
                giveCardsTo(getById(playerIds[next]), 4);
                nextTurn(sender, true);
                break;
            case "s":
                nextTurn(sender, true);
                break;
            case "r":
                turnDirection *= -1;
                nextTurn(sender, false);
                break;
            default:
                nextTurn(sender, false);
                break;
        }
    }

    private void nextTurn(ClientModel sender, boolean skip) {
        drawn = false;
        sendToClient(sender, "turn:end");
        if (checkWin() != null) {
            sendToAll("win:" + checkWin().getName() + " won!");
            pile = new Pile();
            turnDirection = 1;
            started = false;
            return;
        }
        sendToAll("top:" + pile.getTop());
        curPlayer = (((curPlayer + (skip ? 2 : 1) * turnDirection) % playerIds.length) + playerIds.length)
                % playerIds.length;
        sendToClient(getById(playerIds[curPlayer]),
                "**" + Util.listToString(getById(playerIds[curPlayer]).getCards()) + "**");
        sendToClient(getById(playerIds[curPlayer]), "turn:start");
    }
}