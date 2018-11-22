# TP04 - Domino's Pizza

**Objectif:** Création d'un service SOAP ou REST en Java avec Tomcat

## Description

**Domino’s Pizza**, une mégachaine de la pizza, dispose de plusieurs succursales dans la région de Québec. Une des caractéristiques de cette entreprise est qu’elle permet à n’importe qui de placer une commande. Toute commande doit être dirigée vers une succursale et sera livrée chez le client.

## Tâches

- [ ] Développer un service web SOAP/REST capable de lire des données codées sous forme XML et de retourner de l'information en utilisant une collection de simple méthode ou requête http. (**40 pts**)
- [ ] Développer une application qui sera utilisée par les clients pour passer leurs commandes. (**15 pts**)
- [ ] Développer une application qui sera utilisée par le Pizzaman (cuisine). (**10 pts**)
- [ ] Faire un rapport qui retrace le processus du développement. (**5 pts**)
- [ ] Démonstration (**10 pts**)
- [ ] Deux formatifs. (**10 pts**)

## Application

Le serveur stockera les commandes sous forme de liste ou dans une BD et offrira des web services aux programmes qui interagiront avec lui. 

### Structure <code>Commande</code>

```tcl
Commande
	+-- Pizza
		+-- Sorte
			+-- Fromage
			+-- Peppéroni
			+-- Bacon
			+-- Garnie
			+-- Végétarienne
		+-- Prix
		+-- État
			+-- En attente
			+-- En cuisson
			+-- Prête
			+-- En livraison
			+-- Livrée
			+-- Annulée
	+-- Numéro de commande
	+-- Coordonnées du client
		+-- Nom
		+-- Prénom
		+-- Adresses
		+-- Code Postal
		+-- Ville
		+-- Numéro de téléphone
```

### Application Client

Le client pourra transmettre une commande en invoquant les ressources du serveur. Le client pourra suivre l’état de sa commande. Tant que la commande ne sera pas en cuisson, il sera possible pour le client de modifier sa commande. Il pourra même l’annuler.
Dans cette application, le client peut :

- Créer une commande
  - Le numéro de commande est incrémenté automatiquement
  - Le prix est modifié par l'application
  - L'état est mis « *En attente* »
- Modifier la commande
  - Peut modifier la sorte selon le numéro de commande envoyé
  - Seulement si la commande est « *En attente* »
  - Le prix est modifié par l'application
- Annuler la commande
  - Peut annuler la commande selon le numéro de la commande envoyé
  - Seulement si la commande est « *En attente* »
- Trouver l'état
  - Affiche l'état selon le numéro de commande

### Application Cuisine

Le Pizzaman d’une succursale pourra consulter les commandes en invoquant les ressources du serveur. Dans cette application, le Pizzaman peut :

- Afficher 	les commandes qui sont seulement « *en attente* » et « *en cuisson* »
- Modifier l’état
  - Modifier	l’état d’une commande selon le numéro de commande
  - Peut seulement changer à « *en cuisson* » et « *prête* » pour livraison

### Application Livreur

Dans cette application, le livreur peut :

- Afficher les commandes qui sont « *prête* » et « *en livraison* »
- Modifier l'état
  - Modifie l'état d'une commande selon le numéro de commande
  - Peut seulement changer à « *en lirvraison* » et « *livré* »

## Critères d'évaluation

- Respect des consignes
- Qualité du code
- Les commentaires seront très important

