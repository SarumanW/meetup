# MEETUP project

## ER-diagram

Visit that [link](https://www.lucidchart.com/documents/view/06e90d35-30b6-4039-9f97-c7325942da5f/0) to view diagram.


#### To install:
(in root directory)
```
mvn clean install
```

### To run server:
(in /backend directory)
```
mvn spring-boot:run
```

Application will run on the `port 8000`;

### To run angular2 app:
(in /frontend/src/main/frontend directory)
```
npm start
```
Angular2 app will run on the `port 8099`;

If you need backend, start it seperately:

(in /backend directory) 
```
mvn spring-boot:run
```
All backend requests now are made to sub path of /api.

### Installing Oracle JDBC Driver

1. Open [main site](http://www.oracle.com/technetwork/apps-tech/jdbc-112010-090769.html) and download *ojdbc6.jar*.
2. In backend module add library to module structure.



