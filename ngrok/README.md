# NGROK
- Used for Remote Access to Conduct User Testing of Developed Application

## Step 1 - start backend server
- mvnw spring-boot:run
- backend server is running at port 8080

## Step 2 - use ngrok to port forward backend server
For mac users:
- ./macngrok http 8080

For windows users:
- windowsngrok http 8080

Copy the ngrokip, e.g. df2bec12.ngrok.io into memory

## Step 3 - Configure Frontend Application
All Users: update every occurrence of localhost:8080 with the ngrokip copied above for the following files
1.	Admin/Adminstate.js
2.	Alert/alertstate.js
3.	Auth/authstate.js
4.	Upload/uploadstate.js

e.g. of change

original: axios.post("http://localhost:8080/login");

change to: axios.post("http://df2bec12.ngrok.io/login");

## Step 4 - Start the Frontend Application
For all users:
- cd ..
- npm install
- npm start
- frontend app is running at port 3000

## Step 5 - use ngrok to port forward frontend app server
For mac users:
- ./macngrok http 3000

For windows users:
- windowsngrok http 3000

## Step 6 - pass ip to anyone else and conduct user testing