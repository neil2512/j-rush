# JRush

Circulez en toute logique !

---

## Présentation

JRush est une implémentation logicielle complète du célèbre casse-tête "Rush
Hour". Développé en Java, ce projet propose non seulement de jouer aux niveaux
classiques, mais intègre également un éditeur de niveaux en temps réel et un 
résolveur efficace de niveau.

## Fonctionnalités Principales

* **Mode Classique** : Résolution des niveaux du jeu original avec système 
  de sauvegarde et de score.
* **Editeur de Niveau** : Création de niveau personnalisés avec validation 
  de soluilité en temps réel.
* **Assistant de résolution** : Calcul du chemin optimal et résolution 
  automatique grâce à l'implémentation interne d'une algorithmique adaptée.
* **Historique temporel** : Système d'annulation et rétablissement des coups 
  effectués.

---

## Architecture Technique

Le projet a été pensé selon des standards stricts d'ingénierie logicielle pour 
garantir sa robustesse et son évolutivité :

* **Patron MVC (Modèle-Vue-Contrôleur)** : Séparation absolue entre la 
  logique métier (Modèle) et l'interface graphique (Vue). Les interactions 
  sont orchestrées par des moteurs principaux (`GameEngine` et `BuildEngine`).
* **Interface Graphique (JavaFX)** : Rendu dynamique de la grille, gestion  
  événementielle fluide et séparation de la structure et du style via CSS.
* **Programmation par Contrat** : Utilisation intensive d'assertions 
  (`Contract.checkCondition`) pour interdire formellement les états illégaux 
  (collisions de véhicules, sorties de plateau).
* **Responsabilité Unique (SRP)** : Extraction des caractéristiques 
  physiques dans des entités dédiées (ex: `VehicleType`) et isolation de la 
  persistance des données.

---

## Structure du Projet

* `jrush.app.model` : Contient les entités physiques (`Board`, `Vehicle`, 
  `Position`, `Move`).
* `jrush.app.model.logic` : Contient les chefs d'orchestre (`GameEngine`, 
  `BuildEngine`, `LevelLoader`).
* `jrush.app.model.logic.solver` : Contient les algorithmes d'intelligence 
  artificielle (`AStarSolver`, `BreadthFirstSolver`).
* `jrush.app.view` : Contient l'interface graphique JavaFX et le routeur de 
  scènes (`ViewNavigator`).

---

## Prérequis et Lancement

* **Environnement d'exécution :** Java 21 ou supérieur.
  *(Note : La bibliothèque JavaFX est directement embarquée dans 
  l'exécutable, aucune installation ou configuration supplémentaire n'est 
  requise).*

### Option 1 : Lancement rapide
C'est la méthode la plus simple pour jouer immédiatement.
1. Téléchargez la dernière version exécutable (`.jar`) depuis l'onglet 
   **Releases** de ce dépôt GitHub.
2. Lancez le jeu via votre terminal avec la commande suivante :
   ```bash
   java -jar jrush-1.0.jar
   ```
   *(Ou double-cliquez simplement sur le fichier .jar si votre système 
   d'exploitation est configuré pour).*

### Option 2 : Compilation manuelle
Le projet utilise le Maven Wrapper, ce qui vous permet de compiler le code 
source sans avoir besoin d'installer Maven au préalable sur votre machine.
1. Clonez ce dépôt localement :
   ```bash
   git clone [https://github.com/neil2512/jrush.git](https://github.
   com/neil2512/jrush.git)
   cd jrush
   ```
2. Compilez le projet et générez l'exécutable :
   * Sous Linux / macOS : `./mvnw clean package`
   * Sous Windows : `mvnw.cmd clean package`

3. Lancez l'application fraîchement compilée (située dans le dossier target/) :
   ```bash
    java -jar target/jrush-1.0.jar
   ```
