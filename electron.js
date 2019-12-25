const electron = require('electron');
require('electron-reload');
const path = require('path');

const { app, BrowserWindow, ipcMain } = electron;

let mainWindow;

app.on('ready', () => {
    mainWindow = new BrowserWindow({
        webPreferences: { nodeIntegration: true }
    });
    mainWindow.maximize();
    mainWindow.loadURL(process.env.NODE_ENV !== 'production' ? 
    'http://localhost:3000' : `file://${path.join(__dirname,'./public/index.html')}`);
    mainWindow.on('closed', () => mainWindow = null);
});

ipcMain.on('getResult:send', (event, preResult) => {
    //[{bay:noofbay},{},{},{},....]
    let res = JSON.parse(preResult);
});