public class Util {
    static <T> int listLength(List<T> l) {
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
        return s;
    }

    public static <T> void removeFromList(List<T> l, T object) {
        for (l.toFirst(); l.hasAccess(); l.next()) {
            if (l.getContent().toString().equals(object.toString())) {
                l.remove();
                return;
            }
        }
    }
}
