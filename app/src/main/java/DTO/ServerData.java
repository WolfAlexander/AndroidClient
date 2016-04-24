package DTO;

/**
 * This DTO is used to transfer server data
 * For now it is server IP address and port number
 */
public final class ServerData {
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
