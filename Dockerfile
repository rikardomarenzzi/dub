FROM openjdk:15-oracle

MAINTAINER Rikardo Marenzzi <rikardo@marenzzi.de>

ADD backend/target/dub.jar app.jar

EXPOSE 5000

CMD ["sh" , "-c", "java -jar -Dserver.port=5000 -Djwt.secretkey=$JWT_SECRETKEY -Dspring.data.mongodb.uri=$MONGO_DB_URI -Dtmdb.api.key=$TMDB_API_KEY -Docp.apim.subscription.key=$AZURE_KEY -Daws.access.key=$AWS_ACCESS_KEY -Daws.secret.key=$AWS_SECRET_KEY -Daws.region=$AWS_REGION app.jar"]
