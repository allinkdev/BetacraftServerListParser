package com.github.allinkdev.betacraftserverlistparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class BetacraftServerList {
    private static final String USER_AGENT = "Java/" + Runtime.version();
    private static final String SERVERLIST_URL = "https://betacraft.uk/serverlist";
    private final List<Server> servers;

    BetacraftServerList(final List<Server> servers) {
        this.servers = servers;
    }

    /**
     * @return a fully parsed betacraft server list completable future
     */
    public static CompletableFuture<BetacraftServerList> getFuture() {
        return CompletableFuture.supplyAsync(() -> {
            final Document document;

            try {
                document = Jsoup.connect(SERVERLIST_URL)
                        .userAgent(USER_AGENT)
                        .header("Accept", "text/html, image/gif, image/jpeg, ; q=.2,/*; q=.2")
                        .post()
                        .quirksMode(Document.QuirksMode.quirks);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get Jsoup document from server list url", e);
            }

            final List<Server> servers = new LinkedList<>();
            final Elements serverElements = document.getElementsByClass("online");

            for (final Element serverElement : serverElements) {
                final String joinUrl = serverElement.attr("href");

                if (joinUrl.length() < 7) {
                    continue;
                }

                final String substringedUrl = joinUrl.substring(7);

                final String[] urlParts = substringedUrl.split("/");

                if (urlParts.length != 4) {
                    continue;
                }

                final String hostAndPort = urlParts[0];
                final int portColonIndex = hostAndPort.lastIndexOf(":");

                if (portColonIndex == -1) {
                    continue;
                }

                // We're using substring here in-case someone uses IPv6 for their server.
                final String portStr = hostAndPort.substring(Math.min(portColonIndex + 1, hostAndPort.length() - 1));
                final int port;

                try {
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException ignored) {
                    continue;
                }

                // We're doing this .replace operation because some server entries somehow manage to duplicate their port.
                final String host = hostAndPort.substring(0, portColonIndex).replace(":" + port, "");
                final String versionIdentifier = urlParts[3];
                final Version version = Version.fromString(versionIdentifier);
                final String rawNameStr = serverElement.text();
                final int firstIndexOfClosingSquareBracket = rawNameStr.indexOf("]");

                if (firstIndexOfClosingSquareBracket == -1) {
                    continue;
                }

                final String halfParsedNameStr = rawNameStr.substring(Math.min(firstIndexOfClosingSquareBracket + 2, rawNameStr.length() - 1));
                final boolean onlineMode = halfParsedNameStr.endsWith("[Online Mode]");
                final String parsedNameStr = onlineMode ? halfParsedNameStr.replace("[Online Mode]", "") : halfParsedNameStr;
                final Element playerCountElement = serverElement.nextElementSibling();

                if (playerCountElement == null) {
                    continue;
                }

                final String playerCountContent = playerCountElement.text();
                final int indexOfOpeningBracket = playerCountContent.indexOf("(");
                final int indexOfClosingBracket = playerCountContent.indexOf(")");

                if (indexOfOpeningBracket == -1 || indexOfClosingBracket == -1) {
                    continue;
                }

                final String playerCountActualContent = playerCountContent.substring(indexOfOpeningBracket + 1, indexOfClosingBracket)
                        .replace(" ", "");
                final String[] splitPlayerCount = playerCountActualContent.split("/");

                if (splitPlayerCount.length != 2) {
                    continue;
                }

                final int playerCount;
                final int playerLimit;

                try {
                    playerCount = Integer.parseInt(splitPlayerCount[0]);
                    playerLimit = Integer.parseInt(splitPlayerCount[1]);
                } catch (NumberFormatException ignored) {
                    continue;
                }

                final Server server = new Server(parsedNameStr,
                        playerCount,
                        playerLimit,
                        host,
                        port,
                        version,
                        onlineMode,
                        joinUrl,
                        versionIdentifier);

                servers.add(server);
            }

            return new BetacraftServerList(servers);
        });
    }

    /**
     * @return a copy of the internal server list as an unmodifiable list
     */
    public List<Server> getServers() {
        return Collections.unmodifiableList(this.servers);
    }

    /**
     * @param version the version you want to query for
     * @return the server(s) of the provided version
     */
    public List<Server> getServersOfVersion(final Version version) {
        final List<Server> serverListCopy = this.getServers();

        return Collections.unmodifiableList(serverListCopy.stream()
                .filter(s -> s.getVersion().equals(version))
                .collect(Collectors.toList()));
    }

    /**
     * @return the classic server(s)
     */
    public List<Server> getClassicServers() {
        return this.getServersOfVersion(Version.CLASSIC);
    }

    /**
     * @return the indev server(s)
     */
    public List<Server> getIndevServers() {
        return this.getServersOfVersion(Version.INDEV);
    }

    /**
     * @return the infdev server(s)
     */
    public List<Server> getInfdevServers() {
        return this.getServersOfVersion(Version.INFDEV);
    }

    /**
     * @return the alpha server(s)
     */
    public List<Server> getAlphaServers() {
        return this.getServersOfVersion(Version.ALPHA);
    }

    /**
     * @return the beta server(s)
     */
    public List<Server> getBetaServers() {
        return this.getServersOfVersion(Version.BETA);
    }

    /**
     * @return the release server(s)
     */
    public List<Server> getReleaseServers() {
        return this.getServersOfVersion(Version.RELEASE);
    }

    /**
     * @return the server(s) whose version failed to be parsed
     */
    public List<Server> getUnknownServers() {
        return this.getServersOfVersion(Version.UNKNOWN);
    }

    /**
     * @param on the filtered online mode value of the servers
     * @return the server(s) which match
     */
    public List<Server> getServersWithOnlineMode(final boolean on) {
        return Collections.unmodifiableList(this.getServers()
                .stream()
                .filter(s -> s.isOnlineMode() == on)
                .collect(Collectors.toList()));
    }

    /**
     * @return the online mode server(s)
     */
    public List<Server> getOnlineModeServers() {
        return this.getServersWithOnlineMode(true);
    }

    /**
     * @return the offline mode server(s)
     */
    public List<Server> getOfflineModeServers() {
        return this.getServersWithOnlineMode(false);
    }

    /**
     * @return servers with the provided game version string
     */
    public List<Server> getWithGameVersion(final String gameVersion) {
        return Collections.unmodifiableList(this.getServers()
                .stream()
                .filter(s -> s.getGameVersion().equals(gameVersion))
                .collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "BetacraftServerList{" +
                "servers=" + servers +
                '}';
    }
}
