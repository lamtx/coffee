package erika.app.coffee.model;

import erika.app.coffee.application.Define;

public class ClientInfo {
    public String host;
    public int port;
    public String userName;
    public String password;

    public ClientInfo(String userName, String password, String host) throws IllegalArgumentException {
        this.userName = userName;
        this.password = password;
        HostPort hostPort = parseHost(host);
        this.host = hostPort.host;
        this.port = hostPort.port;
    }

    public ClientInfo(String userName, String password, String host, int port) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    private static HostPort parseHost(String hostAndPort) throws IllegalArgumentException {
        HostPort result = new HostPort();
        int index = hostAndPort.indexOf(':');
        if (index == -1) {
            result.host = hostAndPort;
            result.port = Define.PORT;
        } else {
            result.host = hostAndPort.substring(0, index);
            String portAsString = hostAndPort.substring(index + 1);
            result.port = Integer.parseInt(portAsString);
        }
        return result;
    }

    private static class HostPort {
        String host;
        int port;
    }
}
