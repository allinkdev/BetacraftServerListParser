[BetaCraft Server List]: https://betacraft.uk/serverlist

[Unlicense]: https://unlicense.org

[Jsoup]: https://jsoup.org/

# BetaCraftServerListParser

A parser for the [BetaCraft Server List], written in Java.

The only reason why this library exists is that the Server List is returned in a silly format, and does not expose any
endpoints for third-party applications to use. It would be nice if they did, alas, here we are.

This library will most likely break any time the format in which the server list is displayed is updated, *hopefully*
without raising an exception.

This utilises the wonderful [Jsoup] library to parse the site.

# Installation

Add `jitpack.io`, and central Maven, to your repositories, and add `com.github.allinkdev:BetacraftServerListParser:1.1.0` to your
dependencies.

# Usage

Simply call `BetaCraftServerList.getFuture()`, and wait for the future to complete. You can then get all servers of a
specific version, specific online mode status, every server and so forth.

# Modifying

Feel free to adapt the code here as you please, as this project is licensed under the [Unlicense].