# TP2 - Live Chat

### <u>Introduction</u>

#### Directives

 - En équipe de 2 ou 3 obligatoirement.
 - Pénalité pour les erreurs de compilation et d’exécution, ainsi que les warnings. 
 - Respecter les normes de programmation.
 - Vous devez respecter le modèle MVC.
 - Utilisation de JavaFX et SceneBuilder.
 - Remise (3 octobre 2018) :
    - Remettre votre projet sous ce nom : TP2_XXYY (où XX et YY vos initiales).
    - Javadoc doit être généré pour les méthodes publiques.
    - Une démo doit être faite durant le cours du 3 octobre 2018.

#### Présentation

 - Vous devez programmer un logiciel de chat qui sera utilisé entre 2 machines d’un réseau local.
 - De plus, vous devez pouvoir envoyer un fichier à l’ordinateur distant.
 - Vous devez utiliser le protocole TCP pour envoyer des messages de chat et aussi un fichier. 
 - L’application est à la fois client et serveur. Il y a un socket d’ouvert entre le client de l’application 1 et le serveur de l’application 2 et vice versa. Il y a donc 2 sockets d’ouverts au total.
 - Le fichier reçu ira directement dans le dossier `C:\temp\destination\` et portera le même nom de fichier que la source.
 - Vous devez pouvoir envoyer plusieurs fichiers un à la fois bien sûr !
 - L’interface ne doit pas geler en aucun cas. Pour cela, utiliser les threads comme dans le TP1.
 - Vous devez aussi ajouter une fonctionnalité créative et avec un niveau de complexité de 5e session.

![Preview1](https://raw.githubusercontent.com/DrunkenPoney/progres/master/TP2/preview1.png)
![Preview2](https://raw.githubusercontent.com/DrunkenPoney/progres/master/TP2/preview2.png)

<hr>

### <u>Planification</u>

#### Tâches
 - [ ] **Interface utilisateur (vue) :**
    - [ ] Bob
 - [ ] **Modèle (Serveur) :**
    - [ ] Bob
 - [ ] **Modèle (Client) :**
    - [ ] Bob
 - [ ] **Contrôleur :**
    - [ ] Bob

<hr>

### <u>Grille de correction</u>

|<div style="font-size: 24px">Vérifié?</div>|<div style="font-size: 24px">Critère</div>|<div style="font-size: 24px">Résultat</div>|<div style="font-size: 24px">Total</div>|
|:--------:|--------------------------------------------------------------------------------------------------|:--------:|:-----:|
|----------|<div align="center">                       **Directives**                                   </div>|----------|-------|
|![Non][0] | Aucune erreur de compilation ni d'exécution ET pas de warning                                    |    ??    |   5   |
|![Non][0] | Respect du modèle MVC                                                                            |    ??    |   5   |
|![Non][0] | Respect des normes de programmation                                                              |    ??    |   5   |
|![Non][0] | Utilisation de JavaFX et SceneBuilder                                                            |    ??    |   2   |
|![Non][0] | Projet avec initiale                                                                             |    ??    |   3   |
|![Non][0] | Javadoc généré pour les méthodes publiques                                                       |    ??    |   5   |
|----------|<div align="center">                      **Fonctionnement**                                </div>|----------|-------|
|![Non][0] | Connection                                                                                       |    ??    |   5   |
|----------|<div align="center">                   ListView (Droite / Info)                             </div>|----------|-------|
|![Non][0] | Serveur en attente sur le port 4444                                                              |    ??    |  2.5  |
|![Non][0] | Client connecté 172.18.10.41                                                                     |    ??    |  2.5  |
|![Non][0] | Fichier C:\temp\destination\text.txt envoyé                                                      |    ??    |  2.5  |
|![Non][0] | Fichier test.txt reçu                                                                            |    ??    |  2.5  |
|----------|<div align="center">                            Chat                                        </div>|----------|-------|
|![Non][0] | Fonctionnelle des 2 côtés                                                                        |    ??    |  10   |
|![Non][0] | Message débute par le nom                                                                        |    ??    |  2.5  |
|----------|<div align="center">                      Envoie de fichier                                 </div>|----------|-------|
|![Non][0] | Utiliser le chemin choisi par l'utilisateur                                                      |    ??    |  2.5  |
|![Non][0] | Envoie du nom de fichier                                                                         |    ??    |  2.5  |
|![Non][0] | Envoie du fichier                                                                                |    ??    |  10   |
|----------|<div align="center">                     Réception du fichier                               </div>|----------|-------|
|![Non][0] | Reçu et placé dans C:\temp\destination\                                                          |    ??    |   5   |
|![Non][0] | Porte le même nom que l'envoie                                                                   |    ??    |   5   |
|![Non][0] | Envoie de plusieurs fichiers                                                                     |    ??    |  2.5  |
|![Non][0] | Après envoie de fichier, réenvoyer un msg                                                        |    ??    |  2.5  |
|----------|<div align="center">                          **Code**                                      </div>|----------|-------|
|![Non][0] | Utilisation de 2 sockets maximum, un pour l'envoie et un pour la réception                       |    ??    |  2.5  |
|![Non][0] | Exercices Dockets clients et serveurs                                                            |    ??    |   5   |
|![Non][0] | Ajout fonctionnalité                                                                             |    ??    |  10   |
|----------|                                                                                                  |----------|-------|
|----------| **Total**                                                                                        |  **??**  |**100**|


[1]:https://raw.githubusercontent.com/DrunkenPoney/progres/master/svg/check.svg?sanitize=true
[0]:https://raw.githubusercontent.com/DrunkenPoney/progres/master/svg/times.svg?sanitize=true