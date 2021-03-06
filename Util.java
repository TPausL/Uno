public class Util {
    public static <T> int listLength(List<T> l) {
        int i = 0;
        for (l.toFirst(); l.hasAccess(); l.next()) {
            i++;
        }
        return i;
    }

    public static <T> String listToString(List<T> l) {
        String s = "";
        for (l.toFirst(); l.hasAccess(); l.next()) {
            s += l.getContent() + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    public static <T extends HasId> void removeFromList(List<T> l, T object) {
        for (l.toFirst(); l.hasAccess(); l.next()) {
            if (l.getContent().getId().equals(object.getId())) {
                l.remove();
                return;
            }
        }
    }

    public static <T> T findInList(List<T> l, T object) {
        for (l.toFirst(); l.hasAccess(); l.next()) {
            if (l.getContent().toString().equals(object.toString())) {
                return l.getContent();
            }
        }
        return null;
    }

    public static Player getPlayerByIp(List<Player> l, String ip, Integer port) {
        for (l.toFirst(); l.hasAccess(); l.next()) {
            Player c = l.getContent();
            if (c.getIp().equals(ip) && c.getPort().equals(port)) {
                return l.getContent();
            }
        }
        return null;
    }

    public static String[] removeFromArray(String[] a, String remove) {
        String[] newArray = new String[a.length - 1];
        int j = 0;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(remove)) {
                newArray[j] = a[i];
                j++;
            }
        }
        return newArray;
    }

    public static <T> List<T> copyList(List<T> l) {
        List<T> newList = new List<T>();
        for (l.toFirst(); l.hasAccess(); l.next()) {
            newList.append(l.getContent());
        }
        return newList;
    }

    public static CommandLineTable pointsTable(List<Player> players) {
        CommandLineTable clt = new CommandLineTable();
        clt.setShowVerticalLines(true);
        clt.setHeaders("Player", "Points");
        for (players.toFirst(); players.hasAccess(); players.next()) {
            clt.addRow(players.getContent().toString(), ((Integer) players.getContent().getPoints()).toString());
        }
        return clt;
    }
}
