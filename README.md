# ImageManager — Traitement d'Images & Sécurité (JavaFX)

Application de bureau développée en **Java / JavaFX** dans le cadre de ma Licence 2 Mathématiques-Informatique à l'Université de Limoges.  
Le projet met en œuvre le paradigme **Modèle-Vue-Contrôleur (MVC)**, la programmation orientée objet avancée (interfaces, polymorphisme) ainsi que le traitement matriciel de pixels et la cryptographie appliquée[cite: 1, 2].

---

## ⚙️ Fonctionnalités Principales

### 1. Filtres & Traitement Matriciel (Pixel-by-Pixel)
* **Détection de contours (Filtre de Prewitt) :** Calcul de gradients spatiaux horizontal ($G_x$) et vertical ($G_y$) via deux masques de convolution $3\times3$ et extraction de la magnitude $\sqrt{G_x^2 + G_y^2}$[cite: 1, 2].
* **Nuances Sépia :** Pondération chromatique des canaux RGB selon les coefficients Microsoft avec écrêtage dynamique sur $[0.0, 1.0]$[cite: 1, 2].
* **Noir et Blanc :** Désaturation par luminance moyenne arithmétique $\frac{R + G + B}{3}$[cite: 1, 2].
* **Permutation de composantes :** Décalage circulaire des canaux couleur $(R, G, B) \to (G, B, R)$[cite: 1, 2].

### 2. Transformations Géométriques
* **Rotations cardinales :** Pivots horaires à $90^\circ$, $180^\circ$ et $270^\circ$ avec transposition des dimensions ($m \times n \to n \times m$)[cite: 1, 2].
* **Symétries axiales :** Miroir horizontal (inversion sur l'axe $X$) et vertical (inversion sur l'axe $Y$)[cite: 1, 2].

### 3. Module de Sécurité & Chiffrement Visuel
* **Permutation pseudo-aléatoire reproductible :** Décomposition de l'image en tableau 1D ARGB et mélange des pixels par l'algorithme de **Fisher-Yates**.
* **Génération déterministe (`SecureRandom`) :** Dérivation du mot de passe utilisateur via l'empreinte cryptographique **SHA-256** (32 octets) utilisée comme graine (*seed*) du PRNG[cite: 1, 2].
* **Déchiffrement sans conservation de clé :** Inversion mathématique de la table de permutation ($P^{-1}$); le mot de passe n'est jamais persisté en mémoire ni sur disque[cite: 1, 2].

### 4. Indexation par Tags & Persistance
* **Système de métadonnées :** Ajout, suppression dynamique et recherche d'images par tags[cite: 1, 2].
* **Historique des opérations :** Traçabilité sérialisée des transformations appliquées à chaque ressource dans un fichier texte structuré (`~/image_manager_metadata.txt`)[cite: 1, 2].

---

## 🏗️ Architecture Logicielle (MVC)

Le code sépare strictement l'interface utilisateur de la logique de calcul[cite: 1, 2] :

* **Modèle (`fr.univ.imagemanager.model`) :**
  * Interface `FiltreImage` : Contrat unifié imposant la méthode `WritableImage appliquer(WritableImage img)`.
  * Les filtres, transformations géométriques et le chiffrement implémentent cette interface (7 classes polymorphiques).
  * Gestion d'I/O via `GestionMetaDonnees` (`BufferedReader` / `BufferedWriter`).
* **Vue (`fr.univ.imagemanager.view`) :**
  * Description de l'interface en XML déclaratif (`main_view.fxml`)[cite: 1, 2].
  * Habillage et cohérence visuelle via feuille de style (`style.css`).
* **Contrôleur (`fr.univ.imagemanager.controller`) :**
  * `MainController` orchestre les interactions IHM, délègue les calculs aux instances de `FiltreImage` et rafraîchit l'affichage du `PixelReader` / `PixelWriter` sans coupler la vue aux algorithmes[cite: 1, 2].

---

## 🛠️ Stack Technique

* **Langage :** Java 17+ (testé sous Java 21)[cite: 1]
* **IHM :** JavaFX (FXML, CSS)[cite: 1, 2]
* **Build System :** Apache Maven 3.8+
* **Sécurité :** `java.security.MessageDigest` (SHA-256), `java.security.SecureRandom`[cite: 1, 2]

---

## 🚀 Installation & Exécution

### Prérequis
* **JDK 17** ou supérieur.
* **Maven 3.8+**.

### Option 1 — Lancement via Terminal (Maven)
1. Cloner le dépôt :
   ```bash
   git clone https://github.com/keliandadou910-creator/Outil_gestion_images.git
2. Compiler et démarrer l'application JavaFX :
   ```bash
   mvn clean javafx:run

### Option 2 — Lancement via IntelliJ IDEA
1. Ouvrir IntelliJ IDEA et importer le dossier contenant le fichier pom.xml.
2. Laisser Maven télécharger et synchroniser les dépendances JavaFX.
3. Naviguer vers src/main/java/fr/univ/imagemanager/App.java
4. Cliquer sur le bouton d'exécution vert (Run / Play)

### 🎮 Raccourcis & Contrôles
1. Ouvrir une image : Ctrl + O ou Menu Fichier → Ouvrir
2. Sauvegarder l'image : Ctrl + S ou Menu Fichier → Sauvegarde
3. Réinitialiser l'image d'origine : Ctrl + Z ou bouton ↩ Réinitialiser
4. Appliquer un filtre / transformation : Panneau latéral gauche
5. Chiffrement / Déchiffrement : Section Sécurité (saisie interactive du mot de passe)
6. Gestion des tags : Saisie en bas d'écran (Entrée pour valider, clic sur × pour supprimer
