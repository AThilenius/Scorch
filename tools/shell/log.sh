RED='\033[0;31m'
YELLOW='\033[0;33m'
GREEN='\033[0;32m'
NC='\033[0m'

function INFO {
printf "${GREEN}[INFO]${NC} $(date): $1\n"
} 

function WARN {
printf "${YELLOW}[WARN]${NC} $(date): $1\n"
} 

function EROR {
printf "${RED}[EROR] $(date): $1${NC}\n"
exit 1
}
