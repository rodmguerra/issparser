# Rodmguerra's ISS Studio

ISS Studio is a Graphical User Interface to create your own game based on **International Superstar Soccer** for Super Nintendo Entertainment System (SNES).


### Download version 1.6 beta ###
- [Windows executable (.exe)](https://github.com/rodmguerra/issparser/releases/download/v1.6-beta/rodmguerra-iss-studio-1.6-beta-windows.zip)
- [Java archive (.jar)](https://github.com/rodmguerra/issparser/releases/download/v1.6-beta/rodmguerra-iss-studio-1.6-beta-jarfile.zip)

### Features ###
You can edit:
- Players (name, shirt number, hair and skin color, normal or special, hairstyle)
- Hair and skin colors (normal players, special players and goalkeepers)
- Team names (positional text and tile images under the flag)
- Flag (design and colors)
- Uniform (kit) colors (for outfield players, first and second kits, and goalkeepers)

### Notes ###
- Works well with original ROM **International Superstar Soccer (Europe)**, and ROMs based on it
- When you save the design of a flag or a team name tile based image, team name tiled images are moved to the address 0x17680, so that you will have additional space to create more flags than the original game (from 0x48000 to 0x48A7F)
- Windows is required for the features Flag Design and Team Name, that is because it makes use of konami compressor/decompressor tool which works only in Windows.
- Fully localized to English, Portuguese and Spanish, according to your operating system's language.

**Addresses used by editor** (you will recieve an error in order to protect your ROM if data overtake these addresses)
- 0x48000 to 0x48A7F - Flag design tile images
- 0x17680 to 0x17FFF - Team name tile images (automatically moved to this address)
- 0x43ED5 to 0x44486 - Team name positional text data

### Author ###
* Rodrigo Mallmann Guerra

### Special thanks to
* Equipe Puma (particularly, Daniel Cardoso), for support, including rom hacking information, for identifying and solving an important bug, for lots of offset maps and feature suggestions.
* Marcos Fernandes, for dedicated support, and crucial information to make possible flag design
* Equipe Falcon Brasil, for video tutorials, information on goalkeeper colors, player hair and skin and feature suggestions.

---

# Rodmguerra's ISS Studio
???????? Information in Portuguese / Informa????es em portugu??s

ISS Studio ?? uma interface gr??fica para criar seu pr??prio jogo baseado no **International Superstar Soccer** do Super Nintendo Entertainment System (SNES).


### Baixe a vers??o 1.6 beta ###
- [Execut??vel Windows (.exe)](https://github.com/rodmguerra/issparser/releases/download/v1.6-beta/rodmguerra-iss-studio-1.6-beta-windows.zip)
- [Arquivo Java (.jar)](https://github.com/rodmguerra/issparser/releases/download/v1.6-beta/rodmguerra-iss-studio-1.6-beta-jarfile.zip)


### Funcionalidades ###
Voc?? pode editar:
- Jogadores (nome, n??mero da camisa, cor da pele e do cabelo, normal ou especial, corte de cabelo)
- Nomes das equipes (texto posicional e imagem quadriculada embaixo da bandeira)
- Bandeira (desenho e cores)
- Cores do uniforme (uniformes titular e reserva dos jogadores de linha e uniforme dos goleiros)
- Cores de pele e cabelo (dos jogadores de linha e dos goleiros)

### Notas ###
- Funciona bem com a ROM original **International Superstar Soccer (Europe)**, e ROMs baseadas nela.
- Quando voc?? salva o desenho de uma bandeira ou do nome de uma equipe, as imagens quadriculadas dos nomes das equipes s??o movidas para o endere??o 0x17680, assim voc?? ter?? um espa??o adicional para criar mais bandeiras que as do jogo original (de 0x48000 a 0x48A7F)
- ?? necess??rio Windows para as funcionalidades Desenho da Bandeira e Nome da Equipe, isso porque elas fazem uso do compressor/descompressor da konami compressor/decompressor que funciona apenas no Windows. 
- Totalmente traduzido para portugu??s, ingl??s e espanhol, de acordo com a configura????o de idioma do sistema operacional

**Endere??os usados pelo editor** (voc?? receber?? um erro para que sua ROM fique protegida se os dados ultrapassarem esses endere??os)
- 0x48000 to 0x48A7F - Imagens com os desenhos das bandeiras
- 0x17680 to 0x17FFF - Imagens quadriculadas dos nomes das equipes (automaticamente movidas para esse endere??o)
- 0x43ED5 to 0x44486 - Dados de texto posicional com os nomes das equipes

### Autor ###
* Rodrigo Mallmann Guerra

### Agradecimentos ###
* Equipe Puma (em especial, Daniel Cardoso), pelo apoio com informa????es de rom hacking, mapas de endere??os, aux??lio na corre????o de erro e sugest??es de funcionalidades.
* Marcos Fernandes, pelo apoio dedicado, que foi crucial para que a funcionalidade de desenho das bandeiras fosse poss??vel
* Equipe Falcon Brasil, pelos tutoriais em v??deo, informa????es sobre as cores dos goleiros, pele e cabelo dos jogadores e por sugest??es de funcionalidades.

---

### Images ###
#### Players ####
![players](https://user-images.githubusercontent.com/1441876/147856394-54d090dc-cf29-4f61-a10f-1e6c32f33893.png)

#### Hair and skin colors ####
![hair-skin](https://user-images.githubusercontent.com/1441876/147856395-78cb2560-8f05-40e6-b398-cfc76e3e63c7.png)

#### Team names ####
![Team names](https://user-images.githubusercontent.com/1441876/146484653-892e5aba-cbfb-4580-9594-74d98fa5897f.png)

#### Flag design ####
![Flag design](https://user-images.githubusercontent.com/1441876/145671018-c48e3605-cda3-45b8-b940-dd28a9a542b6.png)

---
