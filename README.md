# World1-6Essentials
This is an essentials plugin that I've made for a Private Minecraft Server called World1-6.

This plugin was made due to the fact that EssentialsX overrides some Minecraft Commands.
Even though I could've easily forked EssentialsX and fixed that. I didn't though so I just made this.

[List of servers using this plugin](https://bstats.org/plugin/bukkit/World1-6Essentials)
## Data Converting:
This plugin has a simple data converting system that converts data from other plugins to this plugin.
`/debug1-6 convert from <platform>`

And can also convert to other plugins if you want to.
`/debug1-6 convert to <platform>`

List of plugins that can be converted:
- EssentialsX *(homes, warps, kits)*
- CMI *(homes, warps, kits, and saved inventories)*

### ⚠️ Read before converting data ⚠️
1. Before converting data "from" or "to" you must have that plugin installed at the same time as this plugin.
2. Make sure all of your worlds are loaded before converting data.

## To Install:
*You must have World1-6Utils plugin installed you can get that [here](https://github.com/World1-6/World1-6Utils/releases)*

[Latest Build of World1-6Essentials](https://github.com/World1-6/World1-6Essentials/releases)

## Commands & Permissions:
https://github.com/World1-6/World1-6Essentials/blob/master/src/main/resources/plugin.yml

## Building:
`./gradlew build`

The built jar will be in `build/libs/`