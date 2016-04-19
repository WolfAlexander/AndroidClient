package DTO;

/**
 * Created by Alexander on 2016-04-19.
 */
public class ServerData {
    private String serverIP;
    private int portNr;

    public ServerData(String serverIP, int portNr) {
        this.serverIP = serverIP;
        this.portNr = portNr;
    }

    public int getPortNr() {
        return portNr;
    }

    public void setPortNr(int portNr) {
        this.portNr = portNr;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }
}
