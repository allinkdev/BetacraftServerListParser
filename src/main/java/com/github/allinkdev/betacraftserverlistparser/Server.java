package com.github.allinkdev.betacraftserverlistparser;

public final class Server {
    private final String name;
    private final int playerCount;
    private final int playerLimit;
    private final String host;
    private final int port;
    private final Version version;
    private final boolean onlineMode;
    private final String joinUrl;

    Server(final String name, final int playerCount, final int playerLimit, final String host, final int port, final Version version, final boolean onlineMode, final String joinUrl) {
        this.name = name.trim();
        this.playerCount = playerCount;
        this.playerLimit = playerLimit;
        this.host = host;
        this.port = port;
        this.version = version;
        this.onlineMode = onlineMode;
        this.joinUrl = joinUrl;
    }

    public String getJoinUrl() {
        return this.joinUrl;
    }

    public boolean isOnlineMode() {
        return this.onlineMode;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getPlayerLimit() {
        return this.playerLimit;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getName() {
        return this.name;
    }

    public Version getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", playerCount=" + playerCount +
                ", playerLimit=" + playerLimit +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", version=" + version +
                ", onlineMode=" + onlineMode +
                ", joinUrl='" + joinUrl + '\'' +
                '}';
    }
}
