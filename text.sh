DB_IP=$(node -pe "JSON.parse(JSON.parse(process.argv[1]).node.value).ip"\
"$(curl -s -L http://172.17.42.1:4001/v2/keys/databases/mongodb)");\
DB_PORT=$(node -pe "JSON.parse(JSON.parse(process.argv[1]).node.value).port"\
"$(curl -s -L http://172.17.42.1:4001/v2/keys/databases/mongodb)");\
echo "$DB_IP"
echo "$DB_PORT"
