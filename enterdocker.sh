# run only after `docker compose up -d`
docker exec -it mongodb mongosh -u rootuser -p rootpass --authenticationDatabase admin

## to view records
# show dbs
# use letsplay
# show collections
# db.<collection>.find().pretty()