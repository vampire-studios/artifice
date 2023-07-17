# Artifice
[![Discord](https://img.shields.io/discord/901129108275216392?color=red&label=Discord)](https://discord.gg/63hmSTxyDA)
[![Latest Commit](https://img.shields.io/github/last-commit/vampire-studios/artifice/babric-b1.7.3?color=blue)](https://github.com/vampire-studios/artifice/commits/babric-b1.7.3)

A library mod for Minecraft 1.15+ and Beta 1.7.3, for programmatically generated resource files.

- [API Javadoc](https://htmlpreview.github.io/?https://github.com/vampire-studios/artifice/blob/babric-b1.7.3/doc/index.html)
- [Project Wiki](https://github.com/vampire-studios/artifice/blob/babric-b1.7.3/src/testmod/java/com/swordglowsblue/artifice/test/ArtificeTestMod.java)

Installation: 

```gradle
repositories {
    maven { url = "https://maven.siphalor.de" }
}

dependencies {
  modImplementation "io.github.vampirestudios:artifice-$mc_version:$total_version"
  include "io.github.vampirestudios:artifice-$mc_version:$total_version"
}
```