# MinecraftJourneyMode
A mod inspired by Journey Mode from Terraria


How to do configuration:

Go to the JourneyMode-server.toml in your config file, where you will see four options:

use_unobtainable - this determines whether or not features to allow normally unobtainable items like bedrock and command blocks will be present. True by default.

removals - allows you to choose which items can't be researched and duplicated.

replacements - allows research progress to be moved from one namespaced ID to another (especially useful if an update changes namespaced IDs)

other_files - allows additional json files to determine changes to duplication, whether it be adding new items or changing how much is needed to unlock duplication.

For the last three, you need an additional json file in the config folder to specify the changes.

For removals, have a file called "removals.json" with the following structure, with \<item\> being the namespaced ID and \<category\> being an identifier (does not need to be specific):

```
{
  "items": [
    {
      "item" : "<item>",
      "count" : 0,
      "category" : "<category>"
    }
  ]
}
```

As an example that removes bedrock and prismarine:
  
```
{
  "items": [
    {
      "item" : "minecraft:bedrock",
      "count" : 0,
      "category" : "Minecraft"
    },
    {
      "item" : "minecraft:prismarine",
      "count" : 0,
      "category" : "Minecraft"
    }
  ]
}
```

For replacements, have a file called "replacements.json" with the following structure, with \<original\> being the old namespaced ID and \<new\> being the new namespaced ID:
  
```
{
  "items": [
    {
      "original": "<original>",
      "new": "<new>"
    }
  ]
}
```
  
As an example:

```
{
  "items": [
    {
      "original": "my_mod:lame_ore",
      "new": "my_mod:cool_ore"
    }
  ]
}
```
  
For changes, have a file called "changes.json" with the following structure, with \<item\> being the namespaced ID, \<count\> being how much of the item is needed to fully research the item, and \<category\> being the identifier, though the last part doesn't need to be specific:
  
```
{
  "category": "Changes",
  "items": [
    {
      "item": "<item>",
      "count": <count>,
      "category": "<category>"
    }
  ]
}
```
  
As an example, to add some ores from Thermal Foundation and change the amount of stone needed to be researched:
  
```
{
  "category": "Changes",
  "items": [
    {
      "item": "thermal:niter_ore",
      "count": 64,
      "category": "Thermal Foundation"
    },
    {
      "item": "thermal:sulfur_ore",
      "count": 64,
      "category": "Thermal Foundation"
    },
    {
      "item": "minecraft:stone",
      "count": 10000,
      "category": "Minecraft"
    }
  ]
}
```
