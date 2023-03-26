package com.github.allinkdev.betacraftserverlistparser;

final class Entrypoint {
    public static void main(final String[] args) {
        final BetacraftServerList serverList = BetacraftServerList.getFuture()
                .join();

        final int onlineModeCount = serverList.getOnlineModeServers().size();
        final int offlineModeCount = serverList.getOfflineModeServers().size();
        final int totalServerCount = serverList.getServers().size();

        System.out.printf("There are currently %d classic server(s), %d indev server(s), %d infdev server(s), %d alpha server(s), %d beta server(s) and %d release server(s) on the BetaCraft serverlist. %d are/is of unknown type. Of those servers, %d are/is online mode and %d are/is offline mode. In all, there are/is %d server(s).%n",
                serverList.getClassicServers().size(),
                serverList.getIndevServers().size(),
                serverList.getInfdevServers().size(),
                serverList.getAlphaServers().size(),
                serverList.getBetaServers().size(),
                serverList.getReleaseServers().size(),
                serverList.getUnknownServers().size(),
                onlineModeCount,
                offlineModeCount,
                totalServerCount
        );
    }
}
