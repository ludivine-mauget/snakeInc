#!/bin/bash

# Script de lancement complet du jeu Snake Inc
# Ce script démarre la base de données, l'API et le jeu

echo "========================================="
echo "   Lancement de Snake Inc"
echo "========================================="
echo ""

# Couleurs pour les messages
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Fonction pour vérifier si un port est utilisé
check_port() {
    lsof -ti:$1 > /dev/null 2>&1
    return $?
}

# Fonction pour attendre qu'un port soit disponible
wait_for_port() {
    local port=$1
    local service=$2
    local max_attempts=30
    local attempt=0

    echo -e "${YELLOW}⏳ Attente du démarrage de $service sur le port $port...${NC}"

    while [ $attempt -lt $max_attempts ]; do
        if check_port $port; then
            echo -e "${GREEN}✓ $service est prêt !${NC}"
            return 0
        fi
        attempt=$((attempt + 1))
        sleep 1
    done

    echo -e "${RED}✗ Timeout: $service n'a pas démarré dans le délai imparti${NC}"
    return 1
}

# Étape 1: Démarrer la base de données PostgreSQL
echo -e "${YELLOW}📦 Étape 1/3: Démarrage de la base de données PostgreSQL...${NC}"
if ! command -v docker-compose &> /dev/null && ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ Docker n'est pas installé. Veuillez installer Docker Desktop.${NC}"
    exit 1
fi

# Vérifier si docker-compose existe, sinon utiliser docker compose
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker-compose"
else
    DOCKER_COMPOSE_CMD="docker compose"
fi

$DOCKER_COMPOSE_CMD up -d

if [ $? -ne 0 ]; then
    echo -e "${RED}✗ Échec du démarrage de la base de données${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Base de données démarrée${NC}"
echo ""
sleep 3

# Étape 2: Démarrer l'API Spring Boot
echo -e "${YELLOW}🚀 Étape 2/3: Démarrage de l'API Spring Boot...${NC}"

# Vérifier si le port 8080 est déjà utilisé
if check_port 8080; then
    echo -e "${YELLOW}⚠ Le port 8080 est déjà utilisé. L'API est peut-être déjà démarrée.${NC}"
    read -p "Voulez-vous continuer quand même ? (o/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Oo]$ ]]; then
        echo -e "${RED}Arrêt du script${NC}"
        exit 1
    fi
else
    # Démarrer l'API en arrière-plan
    echo -e "${YELLOW}Compilation et démarrage de l'API...${NC}"
    ./gradlew :api:bootRun > api.log 2>&1 &
    API_PID=$!

    # Sauvegarder le PID pour pouvoir l'arrêter plus tard
    echo $API_PID > .api.pid

    # Attendre que l'API soit prête
    if wait_for_port 8080 "API"; then
        echo -e "${GREEN}✓ API démarrée avec succès (PID: $API_PID)${NC}"
        echo -e "${GREEN}  Logs disponibles dans: api.log${NC}"
    else
        echo -e "${RED}✗ L'API n'a pas pu démarrer. Consultez api.log pour plus de détails${NC}"
        kill $API_PID 2>/dev/null
        exit 1
    fi
fi

echo ""
sleep 2

# Étape 3: Lancer le jeu Snake
echo -e "${YELLOW}🎮 Étape 3/3: Lancement du jeu Snake...${NC}"
echo ""
echo -e "${GREEN}=========================================${NC}"
echo -e "${GREEN}   Tout est prêt ! Bon jeu ! 🐍${NC}"
echo -e "${GREEN}=========================================${NC}"
echo ""
echo -e "${YELLOW}Instructions:${NC}"
echo -e "  • Utilisez les flèches du clavier pour diriger le serpent"
echo -e "  • Sélectionnez ou créez un joueur au démarrage"
echo -e "  • Votre score sera enregistré automatiquement"
echo ""
echo -e "${YELLOW}Pour arrêter l'application:${NC}"
echo -e "  • Fermez la fenêtre du jeu"
echo -e "  • Puis exécutez: ./stop-snake-game.sh"
echo ""
sleep 2

# Lancer le jeu
./gradlew :snake:run --console=plain

# Quand le jeu se termine, proposer d'arrêter les services
echo ""
echo -e "${YELLOW}Le jeu est terminé.${NC}"
read -p "Voulez-vous arrêter l'API et la base de données ? (o/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Oo]$ ]]; then
    ./stop-snake-game.sh
fi

