const electron = require('electron');
require('electron-reload');
const path = require('path');

const { app, BrowserWindow, ipcMain } = electron;

let mainWindow;

//dummy account
const user1 = {
    id: 1,
    username: 'admin',
    password: 'password',
    type: 'administrator'
}

app.on('ready', () => {
    mainWindow = new BrowserWindow({
        webPreferences: { nodeIntegration: true }
    });
    mainWindow.maximize();
    mainWindow.loadURL(process.env.NODE_ENV !== 'production' ? 
    'http://localhost:3000' : `file://${path.join(__dirname,'./public/index.html')}`);
    mainWindow.on('closed', () => mainWindow = null);
});

ipcMain.on('loadUser:send', (event, data) => {
    const { id } = JSON.parse(data);
    if(id === user1.id){
        mainWindow.webContents.send('loadUser:received', JSON.stringify(user1));
    }
})

ipcMain.on('login:send', (event, formData) => {
    const { username, password } = JSON.parse(formData);
    console.log(username, password)
    
    if((username !== user1.username && password !== user1.password) || username !== user1.username || password !== user1.password) {
        mainWindow.webContents.send('login:received', JSON.stringify({
            type: 'ERROR',
            data: {
                msg: 'Invalid credentials',
            }
        }))
    } else {
        mainWindow.webContents.send('login:received', JSON.stringify({
            type: 'SUCCESS',
            data: {
                id: user1.id,
                token: '123456789'
            }
        }))
    }
})

ipcMain.on('getResult:send', (event, preResult) => {
    //[{bay:noofbay},{},{},{},....]
    let res = JSON.parse(preResult);
    console.log(res[0]);
    console.log(res[1]);
    console.log(res[2]);
    console.log(res[3]);
});