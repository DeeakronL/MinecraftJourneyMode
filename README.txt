Welcome to the Minecraft Journey Mode mod!

The mod is inspired by the Journey Mode present in Terraria, where the basic idea is to "research" a certain amount of an item, after which you will unlock the ability to duplicate the item as much as you want. In other words, it functions as a variant of creative mode where you have to actually get the things you want to use.

In order to enter into the mode, type "/journeymode on" in game. (to turn it off, just type "/journeymode off")

To open up the journey mode menu, press the "o" key (this can be changed in the in game settings). The first tab will feature the powers menu, which will allow you to toggle all sorts of settings to help make collecting items less difficult if you choose to do so.

The second menu is the research menu, where you can put items to research them, though of course this consumes the item. Researching items can also be optionally done by tossing items into a research grinder, but isn't necessary.

The third menu is the duplication menu, where you can easily duplicate any item you've unlocked, similar to the creative menu. Any items you haven't unlocked will tell you how much more of the item you need to research, or if the item can't be researched it will say so as well.

There are also some important configuration settings that can be changed in the journeyMode-server.toml file under the config folder. The "use_unobtainable" setting, on by default, incorporates methods to get normally unobtainable blocks like bedrock, command blocks, end portal frames, and others. Turn this off if you want to only work within the bounds of vanilla Minecraft.

There are three other settings that are mostly optional, and are more to adjust the experience to how you want. "removals" allows you to prevent certain items from being able to be researched and duplicated, while "replacements" allows you to transfer research progress from one namespaced ID to another; this last one is important for when items change their namespaced ID to a different one across updates.

The "other_files" setting will have the mod read any lists you have supplied, adding any of the specified items and/or changing how much items are needed to research something away from the default amount.