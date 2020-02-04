# Baymax Application
- Final Year Project Application

## Frontend Setup
1. git clone url-at-github
2. npm install
    - to install all the dependencies 
    - make sure you are inside the directory before doing this command
3. npm start
    - start up react

## Backend Setup
For windows users:
1. Open cmd prompt in springboot folder.
2. Run "mvnw spring-boot:run"

For mac users:
1. Open cmd prompt in springboot folder.
2. Run "./mvnw spring-boot:run"

If an error occurs and you are sure there is no error in your code, run "mvnw clean" for windows and "./mvnw clean" for mac.

## Final exports
For final export of backend server into client's computer:
1. Run "mvvnw package" in springboot folder
2. After packaged, go to the target folder in springboot's folder.
3. There will be a compiled .jar file inside which is the backend server in an executable file.
4. Run "java -jar <name_of_file>.jar" command to start up the server once file is in client's computer.