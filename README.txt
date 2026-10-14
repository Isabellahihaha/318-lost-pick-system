might need 3 terminal open

cd lost-service
mvn spring-boot:run # port_8081

cd found-service
mvn spring-boot:run # port_8082

cd frontend
python -m http.server 5500 # paste http://localhost:5500 to prototype