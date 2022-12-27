# Artifice
[![Discord](https://img.shields.io/discord/901129108275216392?color=red&label=Discord)](https://discord.gg/63hmSTxyDA)
[![Latest Commit](https://img.shields.io/github/last-commit/vampire-studios/artifice/1.19.3?color=blue)](https://github.com/vampire-studios/artifice/commits/1.19.3)

A library mod for Minecraft 1.15+, for programmatically generated resource files.

- [API Javadoc](https://htmlpreview.github.io/?https://github.com/vampire-studios/artifice/blob/1.19.3/doc/index.html)
- [Project Wiki](https://github.com/vampire-studios/artifice/blob/1.19.3/src/testmod/java/com/swordglowsblue/artifice/test/ArtificeTestMod.java)

Installation: 

```gradle
repositories {
    maven { url = "https://maven.siphalor.de" }
}

dependencies {
  modImplementation "io.github.vampirestudios:artifice:$total_version"
  include "io.github.vampirestudios:artifice:$total_version"
}
```