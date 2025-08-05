\## INTRODUCTION GENERALE



0.1. Aperçu général



Le diabète est une maladie chronique en plein essor dans le monde entier, touchant

des millions de personnes sans distinction d’âge ni de sexe. En République Démocratique du

Congo (RDC), cette pathologie connaît une progression inquiétante, exacerbée par un

manque de sensibilisation, un accès limité aux soins spécialisés et une faible numérisation des

outils de suivi médical\[1]. À Lubumbashi, chef-lieu du Haut-Katanga, la situation n’est pas

épargnée. De nombreux patients diabétiques peinent à gérer efficacement leurs données

médicales telles que la glycémie, les traitements, l'alimentation et les rendez-vous médicaux.



Dans ce contexte, les méthodes traditionnelles utilisées par les patients pour suivre ces

paramètres sont souvent archaïques, voire inexistantes : carnets papier, notes manuelles ou

mémorisation. Ces approches sont non seulement imprécises, mais aussi vulnérables à la

perte, à l’oubli ou à la manipulation, ce qui rend difficile l’établissement d’un historique

fiable nécessaire à un suivi médical optimal.



C’est dans cet environnement complexe que s’inscrit notre travail de fin de cycle : concevoir

et développer une application mobile Android permettant aux patients diabétiques de gérer de

manière autonome, sécurisée et fiable leurs données personnelles liées à leur pathologie.

L’application proposée repose sur une architecture technologique innovante intégrant la

blockchain Hyperledger Fabric, garantissant ainsi la sécurité, la traçabilité et la souveraineté

des données médicales\[2].



L’objectif ultime est de doter les patients d’un outil numérique simple, accessible et

performant, favorisant une meilleure compréhension de leur état de santé et facilitant la

communication avec les professionnels de santé.



0.2. Problématique



Comment permettre aux patients diabétiques de gérer efficacement, de manière sécurisée et

confidentielle, leurs données médicales personnelles via une application mobile, dans un

contexte où les méthodes traditionnelles de suivi sont limitées, dispersées et peu fiables?



En effet, plusieurs défis se posent :



\- Les patients utilisent des méthodes non structurées et vulnérables pour le suivi de

&nbsp;   leurs données.

\- Le risque de perte ou de falsification des données est élevé, surtout lorsqu’il s’agit de

&nbsp;   documents physiques.

\- La confidentialité des informations médicales reste fragile dans les systèmes actuels.





\- Il existe un manque d’outils numériques accessibles et adaptés aux réalités locales,

&nbsp;   notamment dans les zones rurales ou semi-urbaines.

\- Le partage des données entre patients et professionnels de santé est laborieux et peu

&nbsp;   standardisé.



Face à ces enjeux, comment peut-on concevoir une solution technologique capable de

répondre à ces besoins tout en restant accessible à un public varié et potentiellement peu

formé à l’informatique?



0.3. Hypothèse



Nous supposons qu’il est possible de répondre à ces défis en développant une application

mobile Android intégrant un système de stockage basé sur la blockchain privée Hyperledger

Fabric, permettant de :



\- Saisir et organiser les données clés (glycémie, alimentation, traitements, etc.) ;

\- Visualiser les tendances via un tableau de bord intuitif ;

\- Notifier les patients via des rappels personnalisés ;

\- Stocker de manière sécurisée les hachés des données critiques sur la blockchain,

&nbsp;   garantissant ainsi leur immutabilité et leur traçabilité.



Cette solution pourrait offrir aux patients diabétiques un outil moderne, autonome et sûr pour

améliorer leur suivi quotidien et faciliter la communication avec les professionnels de santé.



0.4. Choix et intérêt du sujet



0.4.1. Choix du sujet



Notre choix de ce sujet découle de plusieurs observations effectuées sur le terrain, notamment

lors de notre enquête menée à l’Hôpital Sendwe de Lubumbashi. Cette étude a permis de

comprendre les difficultés rencontrées par les patients diabétiques dans la gestion de leurs

données médicales. En tant que futurs ingénieurs logiciens, il nous semblait pertinent de

proposer une solution technologique adaptée au contexte local.



De plus, la blockchain est une technologie émergente qui offre des opportunités prometteuses

dans le domaine de la santé, notamment pour la protection des données sensibles. Nous

souhaitons donc contribuer à son adoption dans le secteur médical congolais, en proposant

une application utile, pertinente et durable.



0.4.2. Intérêts du sujet



Intérêt personnel



Ce projet représente pour moi une opportunité concrète d’appliquer mes connaissances

acquises durant ma formation en génie logiciel, notamment en développement logiciel, en





modélisation UML et en technologies blockchain. Cela me permet également de participer

activement à la résolution de problèmes réels dans mon pays.



Intérêt scientifique



Il s'agit d’un travail original dans le contexte congolais, qui explore l’intégration de la

blockchain dans un système de suivi médical. Ce travail pourra servir de base pour des

recherches futures dans le domaine de la e-santé et de la gestion des données médicales via

des technologies décentralisées.



Intérêt social



L’objectif final est d’améliorer la qualité de vie des patients diabétiques à Lubumbashi en

leur donnant un outil moderne, accessible et fiable pour mieux comprendre et gérer leur

pathologie. En renforçant la souveraineté du patient sur ses données, ce projet participe à la

transformation digitale du secteur de la santé dans un pays en plein besoin de modernisation.



0.5. Méthodologies



0.5.1. Méthode



Pour mener à bien ce projet, nous adopterons la méthodologie centrée utilisateur, comprenant

une phase d’enquête préalable, suivie d’une conception orientée besoins, puis d’un

développement itératif.

La méthode de prototypage rapide sera utilisée afin de valider rapidement les fonctionnalités

principales avec les utilisateurs finaux.



Nous avons opté pour cette méthodologie, principalement parce qu’elle permet d’avoir une

représentation pas forcément fonctionnelle sur un système final, ce qui peut facilement

aiguiller sur les modifications et améliorations à apporter.



0.5.2. Techniques



Les techniques employées incluent :



\- L’enquête terrain : collecte des besoins auprès des patients et médecins à

&nbsp;   Lubumbashi.

\- La modélisation UML : pour représenter les cas d’utilisation, les diagrammes de

&nbsp;   classes, de séquences, etc.

\- Le développement mobile sous Android (Kotlin) : pour la création de l’application.

\- Firebase : pour la gestion du backend et des notifications.

\- Hyperledger Fabric : pour la sécurisation des données via la blockchain.

\- Tests utilisateurs : validation fonctionnelle et ergonomique de l’application auprès

&nbsp;   d’un groupe pilote.





\*\*0.6. État de l’art\*\*



Afin de situer notre travail dans le contexte scientifique et technologique actuel, nous avons

mené une revue de littérature portant sur des projets similaires ou complémentaires à notre

sujet. Cette analyse nous a permis d’identifier les tendances actuelles dans l’utilisation de la

blockchain pour la gestion des données médicales et administratives, tout en mettant en

évidence les lacunes que notre application vise à combler.



Travaux académiques et applications existantes



1.KAFWALUBI MWANSHA GLOIRE (2019, École supérieure d’informatique Salama)



Dans son travail intitulé « Application de gestion des titres de propriété immobiliers et

prévention des fraudes grâce à la technologie Blockchain »\[3] , Gloire Kafwalubi explore

l'utilisation de la blockchain pour sécuriser la gestion des documents fonciers. L’auteur

propose un système décentralisé permettant d’éviter la falsification des titres de propriété, en

garantissant l’intégrité et la traçabilité des transactions. Ce projet utilise principalement la

blockchain Ethereum pour stocker les métadonnées des propriétés et les rendre accessibles

via une interface web.



Ce travail s'inscrit dans une démarche similaire à la nôtre : utiliser la blockchain comme outil

de véracité, de transparence et de sécurité des données sensibles. Cependant, contrairement à

notre cas d’utilisation qui concerne la santé, ce projet se concentre sur le secteur immobilier.



2\. KAFANDA NDALA Paul (2024, Université Don Bosco de Lubumbashi)



Le TFC de Paul Kafanda, intitulé « Conception et réalisation d'une application de gestion

des certificats de conduite en RDC grâce à la blockchain »\[4] , constitue une référence

directe pour notre étude. Il propose une solution innovante basée sur Hyperledger Fabric pour

résoudre le problème d’absence de permis de conduire officiel en RDC. Son application

permet aux auto-écoles agréées de publier des certificats numériques dont l’authenticité peut

être vérifiée par tout utilisateur via la blockchain.



L’approche méthodologique adoptée dans ce projet est très proche de celle que nous

envisageons : modélisation UML, développement avec Laravel, intégration d’un réseau

blockchain privé (Hyperledger Fabric), et déploiement d’une interface web fonctionnelle.

Toutefois, notre travail se distingue par le domaine applicatif : là où Paul Kafanda s'intéresse

à la sécurité routière via la gestion des certificats, notre objectif est d’améliorer la qualité de

vie des patients diabétiques à travers une gestion numérique, autonome et sécurisée de leurs

données médicales personnelles.



3\. ZABAT Anis – Université Mohamed Seddik Benyahia de Jijel



Le travail de Zabat Anis intitulé « Combinaison de blockchain et biométrie pour la gestion

des identités »\[5] explore l’utilisation combinée de deux technologies émergentes : la

blockchain et la biométrie. L’objectif est de créer un système d’identité numérique unique et

inviolable, utilisable dans plusieurs domaines tels que la santé, la finance et l’administration

publique.



Cette recherche montre comment la blockchain peut servir de registre décentralisé et

immuable pour des données critiques, tandis que la biométrie assure une authentification





robuste. Bien que notre projet n’intègre pas la biométrie, il s’aligne sur cette vision : garantir

la sécurité et l’intégrité des données via la blockchain. Notre système pourrait même, à terme,

s’enrichir de solutions biométriques pour renforcer l’identification des patients.



Synthèse



En résumé, les travaux analysés montrent un intérêt croissant pour l’utilisation de la

blockchain dans divers domaines :



\- Sécurité documentaire (titres fonciers, certificats),

\- Gestion des identités (via biométrie).



Cependant, peu de recherches explorent l’application de la blockchain dans le suivi des

maladies chroniques, notamment dans un contexte africain. Notre projet se positionne donc

comme une contribution originale, en apportant une solution technique répondant à la fois

aux besoins pratiques des patients diabétiques et aux exigences de sécurité des données

médicales.



0.7. Délimitation du travail



Le présent travail est délimité dans la province du Haut-Katanga et plus précisément dans la

ville de Lubumbashi durant l'année académique 2024-2025 plus précisément depuis le mois

de Mai jusqu’au mois de juillet, pendant laquelle seront réalisées :



\- Une étude terrain,

\- La conception de l’application,

\- Son développement,

\- Et sa validation auprès d’un panel d’utilisateurs.



La cible principale est constituée des patients diabétiques adultes et jeunes, ainsi que des

professionnels de santé pouvant utiliser l’application pour le suivi des patients.





0.8. Subdivision du travail



Le présent mémoire est structuré en trois parties principales en dehors de l’introduction

générale et de la conclusion générale :



1\. Chapitre I : Généralités et étude préalable : Ce chapitre a pour objectif de poser les

&nbsp;   bases théoriques et techniques du projet. Elle inclut une présentation du contexte

&nbsp;   général, une analyse de l’existant, une introduction à la technologie blockchain ainsi

&nbsp;   que ses caractéristiques fondamentales. Cette partie sert également à identifier les

&nbsp;   besoins fonctionnels et non fonctionnels, tout en définissant clairement les limites du

&nbsp;   système à concevoir

2\. Chapitre II : Conception et modélisation du système : Ce chapitre traite de la phase de

&nbsp;   conception détaillée de l’application. Elle comprend la méthodologie utilisée,

&nbsp;   l’identification des acteurs et des cas d’utilisation, la modélisation UML (diagrammes

&nbsp;   de cas d'utilisation, diagrammes de séquence, diagrammes de classes, etc.), ainsi que

&nbsp;   la réalisation de prototypes graphiques illustrant l’interface utilisateur principale.

&nbsp;   Cette phase constitue un socle essentiel pour la mise en œuvre technique dans la partie

&nbsp;   suivante

3\. Chapitre III : Implémentation et tests : cette partie présente l’architecture technique

&nbsp;   retenue, les outils et technologies utilisés, le déploiement de la blockchain, le

&nbsp;   développement de l’application mobile, ainsi que les tests fonctionnels réalisés auprès

&nbsp;   d’un groupe pilote. Cette dernière partie vise à valider la solution conçue, à mesurer

&nbsp;   sa pertinence par rapport aux besoins exprimés, et à proposer des améliorations

&nbsp;   futures.





0.9. Outils et technologies utilisés



Voici les principaux outils et technologies retenus pour ce projet :



\- Langage de programmation : Kotlin

\- Base de données : Firebase Realtime Database

\- Interface utilisateur : XML

\- Blockchain : Hyperledger Fabric

\- Outils de modélisation : Draw.io, Visual Paradigm

\- Déploiement \& versioning : Git, GitHub

\- IDE : Android Studio

\- Zotero : pour le référencement et la gestion bibliographique

\- Ms Word \& Google Doc : Pour la Rédaction du TFC





\## CHAPITRE I. GÉNÉRALITÉS ET ÉTUDE PRÉALABLE



1.1. Introduction partielle



Ce chapitre vise à poser les bases conceptuelles, contextuelles et médicales nécessaires à la

compréhension du problème traité dans ce travail de fin de cycle. Il s’agit ici d’analyser la

situation actuelle concernant la gestion des données médicales par les patients diabétiques, en

particulier ceux atteints de diabète de type 2, ainsi que les difficultés rencontrées dans leur

suivi quotidien. Nous présenterons également les fondements de la technologie blockchain,

ses caractéristiques essentielles, et son potentiel d'application dans le domaine médical,

notamment pour garantir l’intégrité, la traçabilité et la sécurité des données sensibles.

Enfin, cette partie introduira les besoins fonctionnels et non fonctionnels identifiés lors de

l’enquête menée à l’hôpital Sendwe de Lubumbashi, qui ont guidé la conception de

l’application mobile développée dans le cadre de ce projet



1.2. Contexte général de la maladie (Diabète)



Le diabète est une maladie chronique caractérisée par un taux anormalement élevé de glucose

dans le sang (hyperglycémie). Selon l’Organisation Mondiale de la Santé (OMS), il existe

principalement deux types de diabète :

✓ Diabète de type 1 : lié à une destruction auto-immune des cellules productrices

d’insuline au niveau du pancréas.



```

✓ Diabète de type 2 : lié à une résistance à l’insuline ou à une production insuffisante,

souvent associée à un mode de vie inadapté (sédentarité, mauvaise alimentation).

```

Dans le cadre de ce projet, notre application cible spécifiquement les patients atteints de

diabète de type 2, majoritaires dans la population congolaise et dont la prise en charge repose

largement sur l’auto-gestion au quotidien : alimentation équilibrée, activité physique

régulière, prise correcte des traitements oraux ou injectables, et suivi régulier de la glycémie.

La progression du diabète en République Démocratique du Congo suit une tendance

mondiale : selon des estimations basées sur des données OMS et des études nationales, la

prévalence du diabète chez les adultes tourne autour de 4 à 6 % dans le pays, avec une

augmentation progressive liée à l’urbanisation, à la sédentarité et aux changements de mode

de vie.

À Lubumbashi, siège de ce projet, l’Hôpital Sendwe reçoit régulièrement des patients

diabétiques pour des consultations, suivis ou urgences. Les professionnels de santé soulignent

souvent l’absence de suivi structuré entre les visites médicales, ce qui complique la gestion

efficace de la maladie.





1.3. Problématique actuelle dans la gestion des données diabétiques



Dans la plupart des cas, les patients diabétiques utilisent encore des méthodes archaïques

pour suivre leurs données médicales :



\- Carnets manuels

\- Notes sur téléphone sans structure

\- Mémorisation subjective



Ces approches sont sujettes à plusieurs limitations :



\- Perte fréquente des documents physiques

\- Oubli ou incohérence des mesures notées

\- Difficulté à identifier des tendances sans outil de visualisation

\- Absence de centralisation des données entre différents paramètres (glycémie, repas,

&nbsp;   exercice, traitements)

\- Risque de falsification ou modification involontaire des données



Lors des enquêtes menées à l’Hôpital Sendwe, plusieurs médecins ont confirmé que :



```

Figure 1. 1 : statistiques diabètes, source FID consulté le 15/06/

```



\- Les patients arrivent souvent sans dossier complet ou fiable

\- Les carnets sont incomplets ou perdus

\- Il n’existe pas de système numérique accessible et sécurisé pour le suivi

\- La communication médecin-patient est rendue difficile par le manque de données

&nbsp;   historiques cohérentes



Ainsi, la gestion des données médicales quotidiennes reste un défi majeur, surtout dans un

contexte où :



\- L’accès aux technologies numériques reste limité

\- Le suivi régulier est coûteux

\- La souveraineté des données médicales est fragilisée



Face à ce constat, une solution mobile pourrait offrir une réponse simple, accessible et

durable, en permettant :



\- Une saisie rapide et structurée des mesures importantes

\- Une visualisation claire des tendances glycémiques

\- Un accès facilité aux données par le médecin, via un partage sécurisé

\- Une sauvegarde immuable des informations critiques via la blockchain



1.4. Analyse du système existant



L’analyse du système actuel est une étape essentielle dans la conception d’une solution

adaptée aux besoins réels des utilisateurs. Elle consiste à étudier les méthodes et outils

actuellement utilisés par les patients diabétiques pour gérer leurs données médicales, afin de

mieux identifier leurs limites et de proposer une solution répondant efficacement à ces

lacunes.



1.4.1. Méthodes actuelles de suivi médical



Dans la ville de Lubumbashi, comme dans plusieurs autres villes de RDC, les patients

diabétiques recourent principalement à des méthodes manuelles ou basiques pour suivre leurs

paramètres médicaux au quotidien. Selon l’enquête menée à l’Hôpital Sendwe, la plupart des

patients utilisent :



\- Des carnets papier pour noter leur glycémie, leurs repas et leurs traitements.

\- Des notes sur téléphone via des applications comme WhatsApp, bloc-notes ou encore

&nbsp;   des photos de leurs résultats de mesure.

\- La mémoire personnelle, surtout chez les personnes âgées ou peu scolarisées.



Ces pratiques sont très répandues, mais elles présentent de nombreuses limites :



\- Le risque de perte ou de détérioration des documents physiques est élevé.





\- Les informations peuvent être incomplètes, incohérentes ou difficiles à interpréter lors

&nbsp;   des consultations médicales.

\- Il n’existe pas de moyen structuré pour visualiser les tendances ou évaluer l’évolution

&nbsp;   de la maladie sur le long terme.



De plus, les médecins rencontrés ont souligné que :



\- Beaucoup de patients viennent sans dossier ou avec des données fragmentaires.

\- Certains oublient les mesures prises la veille ou même les doses de médicaments

&nbsp;   absorbés.

\- L'absence d’un outil fiable rend difficile l’évaluation objective de l’état du patient

&nbsp;   entre deux visites.



Ces constats montrent clairement la nécessité de doter les patients d’un outil numérique

simple, accessible et sécurisé pour faciliter leur auto-gestion et améliorer la communication

avec les professionnels de santé.



1.4.2. Outils numériques disponibles



Bien qu’il existe quelques applications internationales dédiées au suivi du diabète, telles que

DiabetCare, conçue pour les patients sous pompe à insuline, ou certaines solutions intégrant

capteurs et appareils connectés, ces outils ne sont pas accessibles ni adaptés à la réalité

congolaise. En effet :



```

Figure 1. 2 Comparaison entre la méthode actuelle et Une application mobile

```



\- DiabetCare est gratuite mais destinée à un usage médical spécifique (pompe à

&nbsp;   insuline), ce qui limite sa portée générale.

\- D’autres applications comme MySugr, OneTouch, ou Glucose Buddy sont

&nbsp;   populaires dans les pays développés, mais elles exigent souvent une connexion

&nbsp;   Internet stable, des appareils connectés ou des smartphones haut de gamme, ce qui

&nbsp;   reste rare ou onéreux dans notre contexte.



Au Congo, il n’existe aucune application mobile locale ou nationale spécifiquement conçue

pour le suivi des patients diabétiques. Même si certains professionnels de santé utilisent Excel

ou Word pour enregistrer les données des patients, ces outils restent limités en termes de

mobilité, de sécurité et de partage des données.

En somme, bien que des outils numériques soient disponibles à l’international, ils ne sont ni

adaptés ni accessibles à la majorité des patients diabétiques vivant à Lubumbashi. Ce constat

justifie pleinement la mise en place d’une application mobile locale, conçue pour fonctionner

avec des appareils simples, voire en mode hors ligne, tout en garantissant la sécurité des

données via la blockchain.



1.4.3. Forces et faiblesses du système actuel



Forces

Malgré ses limites, le système actuel présente quelques avantages :



\- Accessibilité : Les carnets papier et les blocs-notes sur téléphone sont facilement

&nbsp;   accessibles à tous les patients, quel que soit leur niveau technologique.

\- Coût faible : Aucun investissement particulier n’est nécessaire pour utiliser ces

&nbsp;   méthodes.

\- Familiarité : Ces outils sont connus et utilisés depuis longtemps, ce qui rassure

&nbsp;   certains patients.



Faiblesses



Cependant, les faiblesses dominent largement :



\- Perte fréquente des données : Les carnets sont régulièrement perdus ou abîmés.

\- Manque de fiabilité : Les notes prises à la main sont sujettes à l’erreur, à

&nbsp;   l’incohérence ou à l’oubli.

\- Absence de centralisation : Les informations sont dispersées entre différents supports

&nbsp;   (carnet, téléphone, mémoire), ce qui rend difficile la gestion globale de la maladie.

\- Aucune analyse graphique : Impossible de visualiser l’évolution de la glycémie ou

&nbsp;   d’autres indicateurs critiques sans outil numérique.

\- Risque de falsification ou modification involontaire : Une donnée peut être modifiée

&nbsp;   sans trace ou preuve de modification.

\- Difficulté de partage : Les données ne sont pas facilement accessibles aux médecins,

&nbsp;   surtout en dehors des consultations.





\- Dépendance au médecin : Sans outil personnel, le patient dépend entièrement du

&nbsp;   professionnel de santé pour comprendre son état.



Ces faiblesses soulignent la nécessité de développer une solution mobile qui soit :



\- Simple d’utilisation (adaptée à tous les niveaux technologiques),

\- Autonome (fonctionnant aussi en mode hors ligne),

\- Sécurisée (avec stockage des hachés sur blockchain),

\- Accessible (sur Android, système le plus répandu localement).





1.4. La technologie Blockchain : concepts et avantages



La technologie blockchain est au cœur de notre solution. Elle offre un moyen innovant et

sécurisé de gérer les données médicales sensibles, en garantissant leur intégrité, leur

traçabilité et leur souveraineté par le patient. Pour bien comprendre son rôle dans notre projet,

il est essentiel de clarifier ses fondamentaux.



1.5.1. Définition et fonctionnement de la blockchain



La blockchain est une technologie de registre distribué qui permet de stocker et de

transmettre des informations de manière transparente, sans organe central de contrôle. En

d’autres termes, c’est une base de données partagée et synchronisée entre plusieurs nœuds

(ordinateurs ou serveurs), où chaque modification est validée par un consensus entre les

participants.

Concrètement, la blockchain est constituée de blocs contenant des informations (transactions,

données, événements). Chaque bloc est relié au précédent par un hachage cryptographique, ce

qui rend toute tentative de modification facilement détectable. Une fois insérée dans la

chaîne, une donnée devient immuable, c’est-à-dire qu’elle ne peut être modifiée ou supprimée

sans altérer l’ensemble de la chaîne.

Ce système repose sur trois piliers principaux :



\- Le réseau pair-à-pair (P2P) : chaque participant est égal aux yeux du réseau.

\- La cryptographie asymétrique : chaque utilisateur possède une paire de clés (publique

&nbsp;   et privée) pour signer et vérifier les transactions.

\- Le mécanisme de consensus : les participants s’accordent sur l’état valide de la chaîne

&nbsp;   (ex. Proof of Work, Proof of Stake, ou encore Hyperledger Fabric avec son protocole

&nbsp;   de consensus déterministe).





1.5.2. Types de blockchain



Il existe plusieurs types de blockchains, chacune ayant des caractéristiques distinctes adaptées

à différents cas d'utilisation. Les principales catégories sont :

a) Blockchain publique



\- Ouverte à tous : tout individu peut y participer, lire ou écrire des données.

\- Exemples : Bitcoin, Ethereum.

\- Avantages : transparence maximale, aucune autorité centrale.

\- Inconvénients : lenteur, consommation énergétique élevée, faible confidentialité.



b) Blockchain privée



\- Gérée par une entité centrale qui contrôle l’accès.

\- Seuls certains acteurs peuvent lire ou écrire des données.

\- Exemple : Hyperledger Fabric.

\- Avantages : haute performance, contrôle strict sur l’accès, confidentialité renforcée.

\- Inconvénients : moins décentralisée, donc moins "ouverte".



c) Blockchain hybride



\- Mélange de public et privé : certaines parties sont publiques, d'autres réservées.

\- Permet un bon équilibre entre transparence et contrôle.

\- Utile pour les applications nécessitant à la fois sécurité et partage sélectif des données.



d) Blockchain de consortium (ou fédérée)



\- Gérée par un groupe d’organisations plutôt qu’une seule ou qu’un grand public.

\- Idéale pour les partenariats multi-institutions, comme les systèmes de santé partagés.

\- Très utilisée dans le secteur bancaire et médical.



Choix technique pertinent pour notre projet :

Pour notre application mobile de suivi des données diabétiques, nous avons opté pour

Hyperledger Fabric, une blockchain privée et permissionnée, développée par la fondation

Linux Foundation. Ce choix s’explique par :



\- Le besoin de contrôler l’accès aux données critiques,

\- La nécessité de garantir la confidentialité des données médicales,

\- Et l’exigence de performances élevées, compatible avec les contraintes locales (réseau

&nbsp;   limité, appareils modestes).





1.5.3. Caractéristiques clés : immutabilité, sécurité, traçabilité, transparence

a) Immutabilité

Une fois enregistrée, une donnée ne peut être modifiée ni effacée. Cela garantit que les

mesures de glycémie, les traitements ou les rappels saisis par le patient restent intacts et

authentiques. Si une erreur est détectée, elle doit être corrigée par une nouvelle entrée, visible

dans l’historique.

b) Sécurité

Grâce à la cryptographie asymétrique, chaque utilisateur dispose d’une paire de clés

(publique et privée). Seule la clé privée peut modifier ou accéder aux données associées, ce

qui empêche les accès non autorisés.

c) Traçabilité

Chaque action effectuée dans l’application (ajout de mesure, modification, partage) est

horodatée et enregistrée dans la blockchain. Cela permet de retracer l’historique complet des

actions liées à un profil médical.

d) Transparence contrôlée

Bien que la blockchain soit souvent associée à la transparence totale (comme dans le cas des

blockchains publiques), les blockchains privées comme Hyperledger Fabric offrent une

transparence contrôlée : seules les parties autorisées peuvent consulter les données, mais

toutes les modifications sont visibles par les administrateurs ou les médecins concernés.



\*\*1.5.4. Cas d’utilisation dans le secteur de la santé\*\*

La blockchain trouve de plus en plus d’applications dans le domaine de la santé, notamment

pour la gestion des données médicales. Voici quelques exemples concrets :

a) Dossiers médicaux personnels (EHR - Electronic Health Records)



\- Stockage décentralisé des dossiers médicaux

\- Accès rapide et sécurisé par les patients et professionnels de santé

\- Possibilité de partage sous conditions (consentement du patient)



b) Gestion des identités numériques et accès aux soins



\- Identification unique du patient, même lors de changements de médecin ou d’hôpital

\- Réduction des erreurs liées à la confusion entre patients



c) Suivi des traitements et prescriptions



\- Garantie que les ordonnances ne sont pas falsifiées

\- Suivi des prises de médicaments via des dispositifs connectés



d) Stockage de métadonnées médicales



\- Hachage des données critiques (glycémie, poids, tension) sur la blockchain

\- Données elles-mêmes stockées localement ou dans une base chiffrée, mais leur

&nbsp;   empreinte numérique est conservée de manière immuable





1.5. Pourquoi utiliser la blockchain pour la gestion des données diabétiques?



La gestion des données médicales est un domaine sensible où la sécurité, l’intégrité et la

confiance sont primordiales. Dans le cas du suivi du diabète, les patients ont besoin d’un outil

fiable pour enregistrer leurs mesures quotidiennes (glycémie, alimentation, traitements,

exercice physique) tout en conservant le contrôle sur leur information personnelle.

L’utilisation de la blockchain privée Hyperledger Fabric dans notre projet apporte plusieurs

avantages significatifs par rapport aux méthodes actuelles de suivi médical :



a) Souveraineté des données

Avec la blockchain, chaque patient devient propriétaire de ses données. Il peut décider qui

peut y accéder, quand et comment. Cela renforce sa capacité à gérer lui-même sa santé, sans

dépendre entièrement d’un système centralisé ou d’un tiers.

Concrètement, grâce à la cryptographie asymétrique, chaque utilisateur possède une paire de

clés :



\- Une clé publique, utilisée pour identifier l'utilisateur,

\- Une clé privée, permettant de signer les actions effectuées dans l’application (ajout,

&nbsp;   modification, partage).



Cela garantit que seul le propriétaire des données peut modifier ou partager ses informations.



b) Intégrité et immutabilité des données

Une fois qu’un patient saisit une donnée (par exemple, une mesure de glycémie), celle-ci est

sauvegardée localement dans une base chiffrée (Firebase), puis son haché cryptographique est

stocké sur la blockchain.

Cet haché agit comme une empreinte numérique unique de la donnée. En cas de modification

non autorisée, cette empreinte change, ce qui rend toute altération détectable.

Ainsi, même si les données elles-mêmes ne sont pas stockées directement sur la blockchain

(pour des raisons de confidentialité), leur intégrité est garantie.



c) Confidentialité et sécurité renforcée

Contrairement aux blockchains publiques comme Ethereum, Hyperledger Fabric est une

blockchain privée et permissionnée, ce qui signifie que :



\- L’accès au réseau est contrôlé,

\- Seuls les participants autorisés peuvent lire ou écrire des données,

\- Les transactions sont visibles uniquement par les parties concernées.



Cette caractéristique est cruciale dans le domaine médical, où la protection des données

sensibles est encadrée par des normes légales strictes (même si cela n’est pas encore

formellement réglementé en RDC). En adoptant cette architecture, nous anticipons les futurs

cadres légaux et renforçons la confiance des utilisateurs dans l’application.



d) Partage sécurisé des données avec les professionnels de santé





Un des objectifs centraux de l’application est de faciliter la communication entre le patient et

le médecin. Grâce à la blockchain, il est possible de générer un code temporaire ou un QR

code permettant à un professionnel de santé d’accéder aux données validées du patient.

Ce partage reste sécurisé et traçable :



\- Le médecin peut consulter les données sans pouvoir les modifier,

\- Toute tentative d’accès est enregistrée dans l’historique du système.



Ce mécanisme encourage une meilleure collaboration médecin-patient, tout en évitant les

erreurs liées à la perte ou à la falsification des données.



e) Traçabilité et historique fiable

Grâce à la nature immuable de la blockchain, chaque action réalisée dans l’application

(saisie, modification, consultation, partage) est horodatée et stockée de manière indélébile.

Cela permet :



\- De retracer l’évolution de la maladie sur le long terme,

\- D’avoir un historique complet des mesures, utile lors des consultations ou en cas de

&nbsp;   doute médical,

\- De vérifier l’authenticité des données, ce qui est particulièrement important en

&nbsp;   contexte local où les fraudes ou incohérences sont fréquentes.



f) Transparence contrôlée

Bien que la blockchain soit souvent associée à la transparence totale (comme dans les

blockchains publiques), Hyperledger Fabric permet une transparence contrôlée. Cela signifie

que :



\- Les médecins ou administrateurs du système peuvent avoir un accès global aux

&nbsp;   tendances anonymisées,

\- Mais les données personnelles restent accessibles uniquement au patient ou aux

&nbsp;   personnes qu’il autorise explicitement.



g) Réduction de la dépendance aux systèmes centralisés

Dans de nombreux pays africains, les systèmes de santé reposent sur des bases de données

centralisées vulnérables aux pannes, piratages ou manipulations. La blockchain offre une

alternative décentralisée, plus résiliente, où les données ne dépendent pas d’un seul serveur

ou administrateur.

Cela garantit la continuité du suivi, même en cas de panne technique ou de manque

d’infrastructure informatique.



Conclusion de la section

En somme, l’intégration de la blockchain dans l’application mobile de suivi des données

diabétiques apporte une réponse robuste aux défis identifiés :



\- Perte ou falsification des données





\- Absence de traçabilité

\- Manque de contrôle des patients sur leurs propres données

\- Difficultés de partage sécurisé avec les médecins



Elle constitue donc une solution technologique pertinente et innovante, parfaitement adaptée

au contexte local, tout en s’alignant sur les tendances mondiales en matière de santé

connectée et de données médicales sécurisées.





1.8.1. Besoins fonctionnels

Les besoins fonctionnels décrivent les fonctionnalités principales que l’application mobile

devra offrir aux patients diabétiques pour leur permettre de gérer efficacement leurs données

médicales. Ils sont déterminés à partir de l’enquête menée à l’Hôpital Sendwe, ainsi que des

retours des médecins et patients concernés.

Les fonctionnalités principales attendues sont les suivantes :

a) Saisie manuelle des données médicales



\- Permettre aux patients de saisir quotidiennement :

&nbsp;   - Le taux de glycémie

&nbsp;   - Les médicaments pris (nom, dose, heure)

&nbsp;   - Les injections d’insuline (si applicable)

&nbsp;   - Les repas (catégorie : petit-déjeuner, déjeuner, dîner ; ou notes personnalisées)

&nbsp;   - Les exercices physiques réalisés (type, durée)

&nbsp;   - Des notes de santé ou symptômes observés

&nbsp;   - Les rendez-vous médicaux programmés



b) Visualisation des tendances



\- Afficher sous forme de graphiques ou tableaux les mesures importantes (ex. évolution

&nbsp;   de la glycémie sur la semaine).

\- Permettre au patient de visualiser ses tendances alimentaires et sportives.



c) Stockage sécurisé des hachés sur la blockchain



\- Enregistrer les hachés des données critiques sur la blockchain Hyperledger Fabric.

\- Garantir l’intégrité des données via un mécanisme de vérification immuable.



d) Rappels personnalisés



\- Proposer un système de rappels configurables par le patient :

&nbsp;   - Prise de médicaments

&nbsp;   - Mesure de glycémie

&nbsp;   - Rendez-vous médicaux



e) Partage sécurisé des données avec les professionnels de santé



\- Permettre au patient de générer un code temporaire ou QR code pour partager

&nbsp;   certaines données avec son médecin.

\- Donner au professionnel de santé un accès rapide à l’historique des mesures validées

&nbsp;   sans altération possible.



f) Gestion du profil utilisateur



\- Création d’un compte utilisateur avec authentification sécurisée.





\- Possibilité de modifier ses informations personnelles (poids, taille, type de diabète,

&nbsp;   etc.)



1.8.2. Besoins non fonctionnels



En plus des fonctionnalités, il est crucial de définir les contraintes techniques et

opérationnelles qui influenceront l’utilisabilité, la sécurité et la performance de l’application.



a) Sécurité des données



\- Utilisation de la blockchain privée Hyperledger Fabric pour garantir l’intégrité et la

&nbsp;   traçabilité des données.

\- Chiffrement local des données sensibles sur le smartphone.

\- Authentification renforcée via un système de connexion sécurisé (mot de passe +

&nbsp;   option biométrique si disponible).



b) Performance et réactivité



\- Application légère, optimisée pour fonctionner sur des appareils Android modestes.





\- Temps de réponse rapide, même en mode hors ligne.

\- Interface fluide et sans latence lors de la navigation entre les différentes sections.



c) Ergonomie et accessibilité



\- Interface simple, intuitive, conçue pour être utilisée par tous les patients, y compris

&nbsp;   ceux peu familiers avec la technologie.

\- Icônes visuels et menus hiérarchisés pour faciliter l’accès aux fonctionnalités

&nbsp;   essentielles.



d) Compatibilité et déploiement



\- Application développée en Kotlin pour Android, compatible avec Android 6.0 et

&nbsp;   versions ultérieures.

\- Fonctionnement en mode hors ligne, avec synchronisation automatique lorsque la

&nbsp;   connexion Internet est rétablie.



e) Maintenabilité et évolutivité



\- Code modulaire et bien documenté pour faciliter les mises à jour futures.

\- Architecture ouverte permettant l’intégration progressive de nouvelles fonctionnalités

&nbsp;   (ex. capteurs connectés, synchronisation avec Google Fit/Health Connect).



f) Confidentialité et conformité



\- Respect de la vie privée des utilisateurs : aucune donnée personnelle n’est stockée sur

&nbsp;   un serveur distant sans consentement explicite.

\- Aucun partage automatique des données vers des tiers.

\- Conception anticipant une future conformité avec les réglementations locales et

&nbsp;   internationales (même si celles-ci ne sont pas encore formalisées en RDC).



1.9. Délimitation du système





La délimitation du système est une étape essentielle dans tout projet informatique. Elle

permet de définir clairement les frontières du système à concevoir, en précisant ce qui relève

ou non de son périmètre. Cette délimitation se fait généralement selon plusieurs axes :

fonctionnel, technologique, spatial et temporel.



a) Périmètre fonctionnel



L’application mobile que nous développons vise à répondre à un besoin spécifique :

permettre aux patients diabétiques de gérer efficacement leurs données médicales

quotidiennes, avec un accent particulier sur l’utilisation de la blockchain pour garantir la

sécurité, la traçabilité et la souveraineté des données.

Ainsi, le système comprendra les fonctionnalités suivantes :



\- Saisie manuelle des mesures de glycémie, repas, traitements, exercices physiques.

\- Visualisation graphique des tendances médicales.

\- Stockage sécurisé des hachés des données critiques sur une blockchain privée

&nbsp;   (Hyperledger Fabric ).

\- Génération de rappels personnalisables (médicaments, glycémie, rendez-vous).

\- Partage sécurisé des données avec les professionnels de santé via un QR code ou une

&nbsp;   clé temporaire.

\- Gestion d’un profil utilisateur avec authentification sécurisée.



Cependant, le système ne prévoit pas :



\- L’intégration de capteurs médicaux connectés en temps réel (comme les lecteurs de

&nbsp;   glycémie continus).

\- Le suivi automatique des habitudes alimentaires ou sportives via l’intelligence

&nbsp;   artificielle.

\- La gestion des ordonnances ou prescriptions électroniques directement intégrées au

&nbsp;   dossier médical.

\- Une version web destinée aux médecins (bien que cela soit envisageable à long

&nbsp;   terme).



b) Périmètre technologique

Le système est conçu pour être développé sous Android (Kotlin), avec une architecture

backend basée sur Firebase pour le stockage local chiffré, et Hyperledger Fabric pour la

sécurisation des données critiques via la blockchain.

Le développement s’appuie sur une approche mobile-first, c’est-à-dire prioritairement pensée

pour smartphone, avec possibilité future d’une version web légère pour les professionnels de

santé.

Les technologies choisies ont été retenues pour leur accessibilité, leur performance et leur

compatibilité avec les contraintes locales :





\- Android : le système le plus répandu à Lubumbashi.

\- Mode hors ligne : l’application doit pouvoir fonctionner sans connexion Internet

&nbsp;   permanente.

\- Synchronisation automatique : les données sont synchronisées avec la blockchain dès

&nbsp;   qu’une connexion est disponible.



c) Périmètre spatial

Le projet est principalement orienté vers la ville de Lubumbashi, en République

Démocratique du Congo. Il s’agit du lieu de déploiement initial de l’application, et c’est là

que l’enquête terrain a été menée à l’Hôpital Sendwe.

Bien que l’application puisse être utilisée dans d’autres villes ou pays, sa conception est

centrée sur les réalités locales :



\- Faible accès à Internet stable,

\- Utilisation de smartphones modestes,

\- Besoin d’un outil simple, intuitif et accessible à tous les niveaux technologiques.



d) Périmètre temporel

Le projet est inscrit dans une période allant de mai à juillet 2025 , incluant les phases

suivantes :



\- Enquête terrain et collecte des besoins,

\- Conception du système (modélisation UML),

\- Développement de l’application mobile et déploiement de la blockchain,

\- Tests pilotes auprès d’un groupe de patients à Lubumbashi.



À l’issue de cette période, une version pilote fonctionnelle sera livrée, prête à être testée et

validée par les utilisateurs finaux.





1.10. Conclusion partielle

Ce premier chapitre a permis de poser les bases théoriques, médicales et technologiques

nécessaires à la compréhension du problème traité et à la conception de la solution proposée.

Nous avons identifié un besoin criant dans la gestion quotidienne des données des patients

diabétiques à Lubumbashi, où les méthodes actuelles sont souvent archaïques, vulnérables à

la perte ou à la falsification, et peu fiables pour une analyse précise de l’évolution de la

maladie.

L’enquête menée à l’Hôpital Sendwe a confirmé que les patients utilisent principalement des

carnets papier, des notes sur smartphone ou même des photos stockées localement pour

suivre leurs mesures glycémiques, traitements et repas. Cependant, ces outils manquent de

centralisation, de sécurité, d’analyse graphique et surtout de traçabilité. Les médecins

interrogés ont également souligné la difficulté de suivre l’évolution de la maladie sans un

historique fiable des mesures prises entre les consultations.

Dans ce contexte, la blockchain apparaît comme une technologie prometteuse pour garantir

l’intégrité, la souveraineté et la traçabilité des données médicales. Grâce à son caractère

immuable et sécurisé, elle offre une réponse robuste aux défauts constatés dans les systèmes

existants. L’utilisation d’une blockchain privée (Hyperledger Fabric) permet de conserver un

contrôle strict sur les accès, tout en assurant la confiance dans les données saisies par le

patient.

Enfin, nous avons spécifié les besoins fonctionnels et non fonctionnels attendus de

l’application mobile, en tenant compte des réalités locales : interface simple, fonctionnalité

hors ligne, sécurité renforcée, mais aussi facilité de partage avec les professionnels de santé.

Ainsi, ce chapitre a permis de clarifier les motivations du projet, de justifier le choix de la

blockchain comme outil de garantie des données, et de définir les attentes concrètes de

l’application. La suite de ce travail s’articulera autour de la conception du système, avec une

modélisation UML détaillée et une approche centrée utilisateur.





CHAPITRE 2 : CONCEPTION DU SYSTÈME



2.1. Introduction partielle

Ce chapitre a pour objectif de présenter les étapes de conception du système informatique

développé dans le cadre de ce travail de fin de cycle. Il s’agit d’une phase essentielle qui

permet de traduire les besoins identifiés lors de l’étude préalable en une architecture

logicielle claire, structurée et modélisée.

La conception est guidée par une approche centrée utilisateur, utilisant la méthode de

prototypage rapide afin de valider rapidement les idées, les interfaces et les fonctionnalités

principales avec des utilisateurs réels (patients diabétiques et médecins). Cette méthode

itérative facilite l’ajustement des fonctionnalités selon les retours obtenus, garantissant ainsi

que l’application finale répondra aux attentes des utilisateurs.

Pour modéliser notre système, nous avons utilisé le langage UML (Unified Modeling

Language), largement adopté dans le domaine du génie logiciel. Les diagrammes tels que les

cas d’utilisation, séquence, classes, d'états-transitions et d'activité ont été réalisés à l’aide de

l’outil Figma et complétés par des prototypes graphiques illustrant l’interface principale de

l’application mobile.

Ainsi, cette partie permettra au lecteur de comprendre comment le système a été conçu avant

son développement effectif, et comment les exigences fonctionnelles et non fonctionnelles

ont été intégrées dans la structure globale de l’application.



2.2. Méthodologie de conception

2.2.1. Prototypage rapide

La méthode de prototypage rapide a été choisie comme approche principale pour la

conception de l’application mobile. Elle consiste à développer rapidement des versions

simplifiées de l’application (appelées prototypes) pour tester l’ergonomie, les fonctionnalités

et l’expérience utilisateur avant de passer à l’implémentation complète.

Cette méthodologie présente plusieurs avantages :



\- Permet de valider l’interface utilisateur dès les premières phases du projet.

\- Facilite l’implication des utilisateurs finaux (patients et médecins).

\- Réduit les risques liés à une mauvaise compréhension des besoins.

\- Accélère le processus de développement grâce à des itérations rapides.



Lors de la phase de conception, deux séries de prototypes ont été testées :



\- La première série portait sur les écrans de base : connexion, tableau de bord, saisie

&nbsp;   manuelle.

\- La seconde série, améliorée suite aux retours reçus, incluait davantage de détails

&nbsp;   fonctionnels : visualisation graphique, rappels personnalisés et partage sécurisé des

&nbsp;   données via un QR code.



Les tests ont été menés avec deux médecins et deux patients diabétiques volontaires,

permettant de recueillir des retours pertinents sur la clarté, la navigation, la lisibilité et la

pertinence des fonctionnalités.





Ci-dessous le cycle de cette méthodologie.



2.3. Identification des besoins



Comme mentionné dans le chapitre précédent, l’objectif de ce projet est de développer une



Application mobile Android permettant aux patients diabétiques de gérer efficacement leurs

données médicales, grâce à une architecture sécurisée basée sur la blockchain privée

Hyperledger Fabric.



Pour concevoir un système répondant aux attentes des utilisateurs, il est essentiel d’identifier

clairement les besoins fonctionnels et non fonctionnels. Ces exigences guideront la

modélisation et le développement du système.



2.3.1. Les exigences fonctionnelles



Les exigences fonctionnelles décrivent les fonctionnalités principales que l’application doit

offrir pour répondre aux besoins des utilisateurs. Elles définissent ce que le système doit

faire.



Les fonctionnalités attendues sont les suivantes :



\- Créer un compte utilisateur : permettre au patient de s’inscrire avec un email, un mot

&nbsp;   de passe et des informations personnelles (nom, âge, type de diabète, etc.).

\- S’authentifier : permettre au patient de se connecter à son compte de manière

&nbsp;   sécurisée.

\- Saisir une mesure de glycémie : permettre au patient de renseigner son taux de

&nbsp;   glycémie, accompagné d’informations contextuelles (heure, repas, activité physique).

\- Enregistrer les traitements : permettre la saisie des médicaments pris (nom, dose,

&nbsp;   heure) ou des injections d’insuline.





\- Ajouter des notes de santé : permettre au patient de noter des symptômes, des repas ou

&nbsp;   des observations.

\- Visualiser les tendances médicales : afficher sous forme de graphiques les évolutions

&nbsp;   de la glycémie, des repas, des traitements, etc.

\- Configurer des rappels personnalisés : permettre la création de notifications pour la

&nbsp;   prise de médicaments, la mesure de glycémie ou les rendez-vous médicaux.

\- Générer un QR code de partage : permettre au patient de partager temporairement

&nbsp;   certaines données avec un médecin.

\- Stocker un haché sur la blockchain : enregistrer l’empreinte cryptographique des

&nbsp;   données critiques pour garantir leur intégrité.

\- Consulter l’historique des données : permettre au patient de retrouver toutes ses

&nbsp;   saisies passées.



2.3.2. Les exigences non fonctionnelles



Les exigences non fonctionnelles concernent les contraintes de performance, de sécurité,

d’ergonomie et de fiabilité du système. Elles définissent comment le système doit

fonctionner.



Les principales exigences non fonctionnelles sont :



\- Sécurité des données : utilisation de la blockchain Hyperledger Fabric pour garantir

&nbsp;   l’intégrité et la traçabilité des données. Chiffrement local des informations sensibles.

\- Confidentialité : aucun accès non autorisé aux données du patient. Partage

&nbsp;   uniquement via un QR code temporaire.

\- Fonctionnement hors ligne : l’application doit permettre la saisie et la consultation des

&nbsp;   données sans connexion Internet. La synchronisation avec la blockchain se fera dès

&nbsp;   que la connexion est rétablie.

\- Ergonomie : interface simple, intuitive, accessible même aux utilisateurs peu familiers

&nbsp;   avec la technologie.

\- Performance : temps de réponse rapide, même sur des smartphones modestes.

\- Compatibilité : application développée pour Android 6.0 et versions ultérieures.

\- Fiabilité : pas de perte de données, sauvegarde automatique, vérification d’intégrité

&nbsp;   via la blockchain.



2.3.3. Analyse des exigences fonctionnelles et non fonctionnelles



L’analyse des exigences permet de mieux comprendre les interactions entre les utilisateurs et

le système. Elle sert de base à la modélisation UML, notamment à la création du diagramme





de cas d’utilisation, qui illustrera visuellement les fonctionnalités du système et les acteurs

concernés.



2.3.4. Identification des acteurs



Les acteurs sont les entités externes qui interagissent avec le système. Ils peuvent être des

personnes, des systèmes ou des services.



Les acteurs identifiés dans notre application sont :



\- Patient diabétique : principal utilisateur du système. Il saisit ses données, configure

&nbsp;   ses rappels, visualise ses tendances et partage ses informations.

\- Médecin / Professionnel de santé : utilisateur secondaire. Il peut consulter les données

&nbsp;   partagées par le patient via un QR code.

\- Système : composé de l’application mobile, de la base de données locale (Firebase) et

&nbsp;   du réseau blockchain (Hyperledger Fabric). Il gère le stockage, la synchronisation et

&nbsp;   la vérification des données.



2.4. Modélisation

Après avoir identifié les besoins et les acteurs du système, nous passons à la phase de

modélisation. Cette étape consiste à représenter visuellement les différentes composantes du

système à l’aide du langage UML (Unified Modeling Language). Cette modélisation permet

de clarifier les interactions entre les acteurs et le système, de définir la structure des données

et de décrire les processus métiers.

Les diagrammes UML utilisés dans ce projet sont les suivants :



\- Diagramme de cas d’utilisation : pour représenter les fonctionnalités du système et les

&nbsp;   interactions avec les acteurs.

\- Diagrammes de séquence : pour illustrer l’ordre des interactions entre les objets lors

&nbsp;   de l’exécution d’un cas d’utilisation.

\- Diagramme de classes du modèle du domaine : pour décrire la structure des données

&nbsp;   manipulées par le système.

\- Diagramme d’états-transitions : pour modéliser les états possibles d’un objet (ex. :

&nbsp;   une mesure médicale).

\- Diagramme d’activité : pour représenter les flux de processus au sein de l’application.



\*\*2.4.1. Diagramme de cas d’utilisation\*\*

Le diagramme de cas d’utilisation est un outil fondamental de la modélisation UML. Il

permet de visualiser les fonctionnalités du système du point de vue des utilisateurs. Chaque

cas d’utilisation représente une action que l’acteur peut effectuer pour atteindre un objectif

spécifique.

Dans notre application, ce diagramme illustre les interactions entre les trois acteurs

principaux (Patient, Médecin, Système) et les fonctionnalités offertes par l’application

mobile.









