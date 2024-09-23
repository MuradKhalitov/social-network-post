@echo off
mvn clean package -DskipTests
docker build --no-cache -t muradkhalitov/social-network-post .