# Baymax Application
- Final Year Project Application

## Frontend Setup
1. git clone url-at-github
2. npm install
    - to install all the dependencies 
    - make sure you are inside the directory before doing this command
3. npm start
    - start up react
3. Start Up React

## Backend Setup
For windows users:
1. Open cmd prompt in springboot folder.
2. Start the server
```
mvnw spring-boot:run
```

For mac users:
1. Ensure mvnw is executable
```
chmod +x mvnw executable
```
2. Open cmd prompt in springboot folder. 
3. Run "./mvnw spring-boot:run"

If an error occurs and you are sure there is no error in your code, run "mvnw clean" for windows and "./mvnw clean" for mac.

## Backend Testing
Always do the following before running test cases
1. Change the databasename from baymaxdb to testbaymaxdb in mysqlcon.java
    > file found in springboot.src.java.authentication.connection
2. Reset test database
    ```
    # for windows users
    resetBaymaxDB.bat
    
    # for mac users
    ./resetBaymaxDB.sh
    ```
3. Head to springboot folder and do the following
   ```
   # for windows users
   mvnw clean install
   mvnw test
   
   # for mac users
   ./mvnw clean install
   ./mvnw test
   ```
4. verify the results from step 3 and change the databasename back to baymaxdb

## Final exports
For final export of backend server into client's computer:
1. Run "mvvnw package" in springboot folder
2. After packaged, go to the target folder in springboot's folder.
3. There will be a compiled .jar file inside which is the backend server in an executable file.
4. Run "java -jar <name_of_file>.jar" command to start up the server once file is in client's computer.