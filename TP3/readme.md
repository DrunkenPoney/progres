# TP3 - Gestion de réservations

## Directives
- En équipe de 2 ou 3 obligatoirement.
- Pénalité pour les erreurs de compilation et d'exécution, ainsi que les warnings.
- Respecter les normes de programmation.
- Utilisation de JavaFX et SceneBuilder.
- Remise :
  - Remettre vos projets sous ces noms :
    - TP3_CLIENT_XXYYZZ (où XX, YY et ZZ sont vos initiales).
    - TP3_SERVEUR_XXYYZZ (où XX, YY et ZZ sont vos initiales).
  - La JavaDoc doit être générée pour les méthodes publiques.
  - **Une démo obligatoire doit être faite durant le cours du 7 novembre 2018.**
  - **Vous devez aussi ajouter une fonctionalité créative et avec un niveau de complexité de 5<sup>e</sup> session.**

## Mise en situation
Vous êtes en charge de la conception et du développement d'un système de gestion de réservations de chambres à des clients particuliers (riches, stars, ...) pour un grand hôtel de Québec.

<div>
Les employés de l'hôtel ont accès à une application Java qui permet :

- de gérer les clients (ajout, consultation, suppression, modification, ...)
- d'effectuer les réservations suite à l'appel du client, de consulter les réservations, de les modifier et les annuler.
</div>

## Consignes

### Partie 1 - Serveur
- Votre serveur doit gérer plusieurs connexions à la fois.
- Il possède un **base de données** (MySQL ou autre) pour garder à jour les informations clients et les réservations.
- Pas d'interface graphique.
- Les communications avec les clients doivent se faire avec des **connexions sécurisés** (SSL).
- Vous devez monter votre propre stratégie de [journalisation](#Journalisation).

### Partie 2 - Client
- Le client doit avoir une interface graphique.
- Doit contenir un menu pour :
  - Se connecter au serveur
  - Se déconnecter du serveur
  - La gestion des clients (créer, consulter, modifier, supprimer une fiche)
  - La gestion des réservations (créer, consulter, modifier, supprimer une réservation)
- La connexion au serveur est nécessaire pour la gestion des clients et des réservations.
- Vous devez monter votre propre stratégie de [journalisation](#Journalisation).

### Journalisation
<div>
  Chaque entrée de journalisation doit contenir:
  <ul>
    <li>La date et l'heure</li>
    <li>Le nom de la classe</li>
    <li>Le numéro de l'erreur pour les erreurs</li>
    <li>Le message</li>
  </ul>
</div>
<div>
  On doit voir ces traces dans la journalisation :
  <ul>
    <li>Connexion d'un client/serveur avec l'adresse IP et le port.</li>
    <li>Déconnexion d'un client/serveur avec l'adresse IP et le port.</li>
    <li>Tous les messages d'erreur.</li>
  </ul>
</div>

### Autres
Ajouter une fonctionnalité créative avec un niveau de complexité de 5<sup>e</sup> session.

## Grille de correction
|<div style="font-size: 24px">Vérifié?</div>|<div style="font-size: 24px">Critère</div>|<div style="font-size: 24px">Résultat</div>|<div style="font-size: 24px">Total</div>|
|:--------:|--------------------------------------------------------------------------------------------------|:--------:|:-----:|
|----------|<div align="center">                        **Directives**                                  </div>|----------|-------|
|![Non][0] | Aucune erreur de compilation ni d'exécution ET pas de warning                                    |    ??    |   3   |
|![Non][0] | Respect du modèle MVC                                                                            |    ??    |   3   |
|![Non][0] | Respect des normes de programmation                                                              |    ??    |   1   |
|![Non][0] | Utilisation de JavaFX et SceneBuilder                                                            |    ??    |   2   |
|----------|<div align="center">                         **Serveur**                                    </div>|----------|-------|
|![Non][0] | Base de données                                                                                  |    ??    |   5   |
|![Non][0] | Autre code (pas d'interface graphique)                                                           |    ??    |   5   |
|![Non][0] | Connexion sécurisée (clé privé, ...)                                                             |    ??    |   6   |
|----------|<div align="center">                        Journalisation                                  </div>|----------|-------|
|![Non][0] | Connexion d'un client machine avec l'adresse IP et le port                                       |    ??    |   3   |
|![Non][0] | Déconnexion d'un client machine avec l'adresse IP et le port                                     |    ??    |   2   |
|![Non][0] | Tous les messages d'erreur                                                                       |    ??    |   3   |
|----------|<div align="center">                            Test                                        </div>|----------|-------|
|![Non][0] | 1 serveur + 2 clients                                                                            |    ??    |   3   |
|----------|<div align="center">                         **Client**                                     </div>|----------|-------|
|----------|<div align="center">                   Menu et Fonctionnalité                               </div>|----------|-------|
|![Non][0] | Connexion au serveur                                                                             |    ??    |   2   |
|![Non][0] | Déconnexion du serveur                                                                           |    ??    |   2   |
|![Non][0] | Gestion des clients (ouvrir, modifier, fermer, créer ou supprimer une fiche)                     |    ??    |   2   |
|![Non][0] | Gestion des réservations (ouvrir, modifier, fermer, créer ou annuler une réservation)            |    ??    |   2   |
|----------|<div align="center">                        Journalisation                                  </div>|----------|-------|
|![Non][0] | Connexion au serveur avec l'adresse IP et le port                                                |    ??    |   3   |
|![Non][0] | Déconnexion au serveur avec l'adresse IP et le port                                              |    ??    |   2   |
|![Non][0] | Tous les messages d'erreur                                                                       |    ??    |   2   |
|----------|<div align="center">                            Test                                        </div>|----------|-------|
|![Non][0] | Créer une fiche client                                                                           |    ??    |   5   |
|![Non][0] | Modifier une fiche client                                                                        |    ??    |   5   |
|![Non][0] | Consulter une fiche client                                                                       |    ??    |   5   |
|![Non][0] | Supprimer une fiche client                                                                       |    ??    |   5   |
|![Non][0] | Créer une réservation                                                                            |    ??    |   5   |
|![Non][0] | Modifier une réservation                                                                         |    ??    |   5   |
|![Non][0] | Consulter une réservation                                                                        |    ??    |   5   |
|![Non][0] | Supprimer une réservation                                                                        |    ??    |   5   |
|----------|<div align="center">                         **Autres**                                     </div>|----------|-------|
|![Non][0] | Fonctionnalité générale                                                                          |    ??    |   6   |
|![Non][0] | JavaDoc                                                                                          |    ??    |   3   |
|----------|                                                                                                  |----------|-------|
|----------| **Total**                                                                                        |  **??**  |**100**|

[1]:https://raw.githubusercontent.com/DrunkenPoney/progres/master/svg/check.svg?sanitize=true
[0]:https://raw.githubusercontent.com/DrunkenPoney/progres/master/svg/times.svg?sanitize=true