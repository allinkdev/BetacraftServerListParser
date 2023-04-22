package com.github.allinkdev.betacraftserverlistparser;

import java.util.Arrays;

public enum Version {
    RELEASE(new String[]{"r", "1" /* Hacky I know, but it works */}),
    CLASSIC(new String[]{"c"}),
    INDEV(new String[]{"mp-in-", "in-", "indev"}),
    INFDEV(new String[]{"infdev"}),
    // 12-year-old server owners try not to give their server a non-compliant "custom" version challenge (Quite impossible)
    ALPHA(new String[]{"a", "nsss"}),
    BETA(new String[]{"b"}),
    UNKNOWN(new String[0]);

    final String[] versionPrefixes;

    Version(final String[] versionPrefixes) {
        this.versionPrefixes = versionPrefixes;
    }

    public String[] getVersionPrefixes() {
        final String[] copy = new String[versionPrefixes.length];
        System.arraycopy(versionPrefixes, 0, copy, 0, versionPrefixes.length);

        return copy;
    }

    public static Version fromString(final String versionString) {
        final String lowercaseVersionString = versionString.toLowerCase();

        return Arrays.stream(Version.values())
                .filter(v -> Arrays.stream(v.versionPrefixes).anyMatch(lowercaseVersionString::startsWith))
                .findFirst()
                .orElse(Version.UNKNOWN);
    }

    @Override
    public String toString() {
        return "Version{" +
                "versionPrefixes=" + Arrays.toString(versionPrefixes) +
                '}';
    }
}
