
Feature: Connexion utilisateur

  @loginValid
  Scenario: Connexion avec identifiants valides
    Given je suis sur la page de connection
    When je saisis le username "ahmed"
    And je saisis le mot de passe "Ahmed1997"
    And je clique sur le bouton Se connecter
    Then la connexion est réussie

  @loginInvalid
  Scenario: Connexion avec identifiants invalides
    Given je suis sur la page de connection    When je saisis le username non valide "ahmeds"
    And je saisis le mot de passe non valide "wrongpassword"
    And je clique sur le bouton Se connecter
    Then un message d'erreur est affiché
  
  @loginEmpty
  Scenario: Connexion avec un username vide et un mot de passe vide
    Given je suis sur la page de connection    When je saisis le username ""    And je saisis le mot de passe ""    And je clique sur le bouton Se connecter    Then un message d'erreur champ obligatoire est affiché