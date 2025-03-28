# AgarIoGame

## Introduction
Ce projet à été réalisé lors d'une SAE en 2ème année de BUT informatique.
AgarIoGame est une implémentation inspirée du célèbre jeu Agar.io. Ce projet utilise Java et JavaFX.

## Installation
### Prérequis
- Java 22+
- Git

### Étapes d'installation
1. Cloner le dépôt :
   ```sh
   git clone <URL_DU_REPO>
   cd AgarIoGame
   ```

## Utilisation
### Branches utiles
Sur la branche Rendu-1 il y a le jeu jouable en multijoueur ou en solo (mais moins de fonctionnalités).
  ```sh
    git checkout Rendu-1
  ```
Sur la branche choixSpecialPellet, on peut voir le bon fonctionnement des IAs en solo.
```sh
    git checkout choixSpecialPellet
  ```

### Lancement du jeu
   Pour lancer le jeu il vous suffit de vous mettre dans le fichier src/main/java/sae/launch/agario/models/App.java, et de lancer le projet.
   Si vous souhaiter héberger un serveur et jouer sur la meme machine il suffit de lancer deux configurations qui lancent le même projet, et vous connecter
   au serveur avec 127.0.0.1 (car c'est vous l'hôte) et 8081 en port.
   Pour rejoindre un serveur, il faut entrer l'ip de la machine du serveur et entrer 8081 comme port.


## Fonctionnalités implémentés
- Jouer en local contre 3 IA différentes. -> (Solo, branche choixSpecialPellet)
- Jouer en réseau contre d'autres joueurs en créant un serveur et en renseignant les information de connexion. -> (Multi, branche Rendu-1)
- Choisir si l'on souhaite jouer ou non avec des IA quand on est en local, et choisir combien et lesquelles. -> (Solo, branche choixSpecialPellet)
- Choisir si on souhaite jouer avec les pastilles spéciales. -> (Solo, branche choixSpecialPellet)
- Tableau des scores fonctionnel, il affiches les 10 premières personnes en tête du classement. -> (Solo, choixSpecialPellet)
- Une minimap est disponnible afin de mieux se repérer dans l'espace. -> (Solo, branche choixSpecialPellet)
- Il es possible de se diviser, puis après un certain temps, de se regrouper. -> (Multi, branche Rendu-1)
- Spawn et respawn aléatoire sur la map, pour les joueurs comme pour les IAs, et respawn automatique du joueur ou des IA après la mort.

### Explication des IAs
Il existe 3 type d'IA :
- IA aléatoire
  Cette IA ce déplace aléatoirement dans la map, sans avoir de réel but.
- IA récolteuse
  Cette IA cherche à manger le plus de pastilles possible, ainsi elle ne vous mangera pas, sauf si vous vous mettez devant elle bien sûr.
- IA chasseuse
  Cette IA cherche à tout prix à vous manger. Si malheuresement elle est encore trop petite pour vous manger, elle ne va pas venir vers vous pour mourir mais
  elle va aller chercher des pastilles pour ensuite venir vous manger dès qu'elle sera assez grosse. Elle se transforme en quelque sorte en une IA récolteuse.

### Explications des pastilles spéciales
Il existe 4 type de pastilles spéciales :
- Boost de vitesse (x2)
   Augmentation de la vitesse pendant quelques secondes.
- Ralentissement (x0.5)
   Diminution de la vitesse pendant quelques secondes.
- Invisibilité
  Cela rend le joueur invisible à la vue des IA, donc surtout pour les IA chasseuse. Vous ne serais donc pas poursuivis pendant quelques secondes.
- Division (non implémentée mais pastilles existantes donc elles donnent juste de la nouriture)

## Contributeurs
Grp 1 : THEAULT Hugo, CHIRAC Nathan, JEAN Maxime, RABUTE Antoine, MASSET Gaël.

