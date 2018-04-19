# MEETUP project

# To install:
(in root directory) mvn clean install
# To run server:
(in /backend directory) mvn spring-boot:run

Application will run on the port 8000;

# To run angular2 app:
(in /frontend/src/main/frontend directory) npm start

Angular2 app will run on the port 8099;

If you need backend, start it seperately:

(in /backend directory) mvn spring-boot:run

All backend requests now are made to sub path of /api.

