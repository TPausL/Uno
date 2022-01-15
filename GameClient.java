import java.nio.charset.Charset;
import java.util.Random;

/**
 * GameClient
 */
public class GameClient {

    protected String ip, id;
    protected Integer port;

    GameClient(String ip, int port) {
        this.id = genId();
        this.ip = ip;
        this.port = port;
    }

    GameClient(String ip, int port, String id) {
        this.id = id;
        this.ip = ip;
        this.port = port;
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

    public boolean idEquals(String id) {
        return this.id.equals(id);
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    public boolean ipEquals(String ip) {
        return this.ip.equals(ip);
    }

    /**
     * @return the port
     */
    public Integer getPort() {
        return port;
    }

    public boolean portEquals(Integer port) {
        return this.port.equals(port);
    }

    public String toString() {
        return this.id;
    }

}