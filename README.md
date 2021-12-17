# Rodmguerra's ISS Studio

ISS Studio is a Graphical User Interface to create your own game based on the game **International Super Star Soccer** for Super Nintendo Entertainment System (SNES).


### Latest version (1.5 beta) ###
- [Windows executable (.exe)](https://github.com/rodmguerra/issparser/releases/download/v1.5-beta/rodmguerra-iss-studio-1.5-beta-windows.zip)
- [Java archive (.jar)](https://github.com/rodmguerra/issparser/releases/download/v1.5-beta/rodmguerra-iss-studio-1.5-beta-jarfile.zip)

### Features ###
You can edit:
- Team names (positional text and tile images under the flag)
- Player names
- Flag (design and colors)
- Uniform (kit) colors (for outfield players and goalkeepers)
- Hair and skin colors (for outfield players and goalkeepers)

### Notes ###
- Works well with original ROM **International Super Star Soccer (Europe)**, and ROMs based on it
- When you save the design of a flag or a team name tile based image, team name tiled images are moved to the address 0x17680, so that you will have additional space to create more flags than the original game (from 0x48000 to 0x48A7F)
- Windows is required for the features Flag Design and Team Name, that is because it makes use of konami compressor/decompressor tool which works only in Windows. 

**Positions used by editor** (you will recieve an error in order to protect your rom if data overtake this addresses)
- 0x48000 to 0x48A7F - Flag design tile images
- 0x17680 to 0x17FFF - Team name tile images (automatically moved to this address)
- 0x43ED5 to 0x44486 - Team name positional text data

### Images ###
#### Team names ####
![uruguai](https://user-images.githubusercontent.com/1441876/146484653-892e5aba-cbfb-4580-9594-74d98fa5897f.png)

#### Flag Design ####
![vasco-fla](https://user-images.githubusercontent.com/1441876/145671018-c48e3605-cda3-45b8-b940-dd28a9a542b6.png)

---

### Author ###
* Rodrigo Mallmann Guerra

### Special thanks to
* Equipe Puma (particularly, Daniel Cardoso), for support, including rom hacking information, lots of offset maps and feature suggestions.
* Marcos Fernandes, for dedicated support, and crucial information to make possible flag design
* Equipe Falcon Brasil, for video tutorials, information on goalkeeper colors, player hair and skin and feature suggestions.
