# Far F5 Mod

Increase the third-person distance, so you can get a better overview of your surroundings.

## Usage

[Download the latest `FarF5_(version).jar`](https://github.com/Gjum/FarF5/releases/latest)

Assign the keybinds: `Options` -> `Controls` -> `Far F5`

Then open a world and go into third-person perspective (usually the `F5` key).
Turn around a bit to get a good overview of your surroundings,
and use the zoom keys to change how much you see.

You can also access the settings through Forge's mod list:
- from the main menu: `Mods` -> select `Far F5` on the left -> click `Config` at the bottom left
- from the ingame menu: `Mods config` -> select `Far F5` on the left -> click `Config` at the bottom left

## Example

With FarF5: ![With FarF5](https://i.imgur.com/KB5KBXw.jpg)
Without FarF5: (spot yourself in the center) ![Without FarF5](https://i.imgur.com/WMHFVI6.jpg)

## Development

Package the distributable `.jar`: `gradlew reobf`

You can then find it in `build/libs/`.

To update this mod to a newer version of Minecraft,
you need to update `PatchedEntityRenderer` with all the new code from `EntityRenderer`,
basically copy-pasting everything and re-patching `orientCamera`.
