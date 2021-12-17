# Rodmguerra's ISS Studio

ISS Studio is a Graphical User Interface to create your own game based on the game **International Superstar Soccer** for Super Nintendo Entertainment System (SNES).


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
- Works well with original ROM **International Superstar Soccer (Europe)**, and ROMs based on it
- When you save the design of a flag or a team name tile based image, team name tiled images are moved to the address 0x17680, so that you will have additional space to create more flags than the original game (from 0x48000 to 0x48A7F)
- Windows is required for the features Flag Design and Team Name, that is because it makes use of konami compressor/decompressor tool which works only in Windows.

**Addresses used by editor** (you will recieve an error in order to protect your ROM if data overtake these addresses)
- 0x48000 to 0x48A7F - Flag design tile images
- 0x17680 to 0x17FFF - Team name tile images (automatically moved to this address)
- 0x43ED5 to 0x44486 - Team name positional text data
- 
### Images ###
#### Team names ####
![uruguai](https://user-images.githubusercontent.com/1441876/146484653-892e5aba-cbfb-4580-9594-74d98fa5897f.png)

#### Flag Design ####
![vasco-fla](https://user-images.githubusercontent.com/1441876/145671018-c48e3605-cda3-45b8-b940-dd28a9a542b6.png)

### Author ###
* Rodrigo Mallmann Guerra

### Special thanks to
* Equipe Puma (particularly, Daniel Cardoso), for support, including rom hacking information, for identifying and solving an important bug, for lots of offset maps and feature suggestions.
* Marcos Fernandes, for dedicated support, and crucial information to make possible flag design
* Equipe Falcon Brasil, for video tutorials, information on goalkeeper colors, player hair and skin and feature suggestions.

---


# Rodmguerra's ISS Studio
🇧🇷 Information in Portuguese / Informações em português

ISS Studio é uma interface gráfica para criar seu próprio jogo baseado no jogo **International Superstar Soccer** do Super Nintendo Entertainment System (SNES).


### Versão mais recente (1.5 beta) ###
- [Executável Windows (.exe)](https://github.com/rodmguerra/issparser/releases/download/v1.5-beta/rodmguerra-iss-studio-1.5-beta-windows.zip)
- [Arquivo Java (.jar)](https://github.com/rodmguerra/issparser/releases/download/v1.5-beta/rodmguerra-iss-studio-1.5-beta-jarfile.zip)


### Funcionalidades ###
Você pode editar:
- Nomes das equipes (texto posicional e imagem quadriculada embaixo da bandeira)
- Nomes dos jogadores
- Bandeira (desenho e cores)
- Cores do uniforme (dos jogadores de linha e dos goleiros)
- Cores de pele e cabelo (dos jogadores de linha e dos goleiros)

### Notas ###
- Funciona bem com a ROM original **International Superstar Soccer (Europe)**, e ROMs baseadas nela.
- Quando você salva o desenho de uma bandeira ou do nome de uma equipe, as imagens quadriculadas dos nomes das equipes são movidas para o endereço 0x17680, assim você terá um espaço adicional para criar mais bandeiras que as do jogo original (de 0x48000 a 0x48A7F)
- É necessário Windows para as funcionalidades Desenho da Bandeira e Nome da Equipe, isso porque elas fazem uso do compressor/descompressor da konami compressor/decompressor que funciona apenas no Windows. 

**Endereços usados pelo editor** (você receberá um erro para que sua ROM fique protegida se os dados ultrapassarem esses endereços)
- 0x48000 to 0x48A7F - Imagens com os desenhos das bandeiras
- 0x17680 to 0x17FFF - Imagens quadriculadas dos nomes das equipes (automaticamente movidas para esse endereço)
- 0x43ED5 to 0x44486 - Dados de texto posicional com os nomes das equipes

### Imagens ###
#### Nomes das equipes ####
![uruguai](https://user-images.githubusercontent.com/1441876/146484653-892e5aba-cbfb-4580-9594-74d98fa5897f.png)

#### Desenho das bandeiras ####
![vasco-fla](https://user-images.githubusercontent.com/1441876/145671018-c48e3605-cda3-45b8-b940-dd28a9a542b6.png)

### Autor ###
* Rodrigo Mallmann Guerra

### Agradecimentos ###
* Equipe Puma (em especial, Daniel Cardoso), pelo apoio com informações de rom hacking, mapas de endereços, auxílio na correção de erro e sugestões de funcionalidades.
* Marcos Fernandes, pelo apoio dedicado, que foi crucial para que a funcionalidade de desenho das bandeiras fosse possível
* Equipe Falcon Brasil, pelos tutoriais em vídeo, informações sobre as cores dos goleiros, pele e cabelo dos jogadores e por sugestões de funcionalidades.

