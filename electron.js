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
    type: 'administrator',
    secret: '123456789'
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
    const { token } = JSON.parse(data);
    if(token === user1.secret){
        mainWindow.webContents.send('loadUser:received', JSON.stringify({ type: 'SUCCESS', user: user1 }));
    } else if(token === null){
        mainWindow.webContents.send('loadUser:received', JSON.stringify({ type: 'ERROR' }));
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
    //{ bay: 0, data: [{...},{...},{...}] }
    let res = JSON.parse(preResult);
    // console.log(res.bay);
    console.log(typeof res.data[0]['Delta Days']);
    console.log(typeof res.data[0]['Cycle Time Days']);
    // console.log(res.data[1]);
    // console.log(res.data[2]);
    let data = [
        {
            "Argo ID":"16073",
            "Slot ID/UTID":"CY19Q4TOOL19",
            "Slot Status":"open",
            "Ship Rev Type":"hello",
            "Build Category":"Build Plan",
            "Build Product":"AUDI A4",
            "Slot Plan Notes":"nil",
            "Plan Product Type":"Tool",
            "Ship Risk/Upside":"hi",
            "Ship Risk Reason":"hi",
            "Comment for $ Change":"hi",
            "Committed Ship $":"hi",
            "Secondary Customer Name":"hi",
            "Fab ID":'1',
            "Sales Order #":"hi",
            "Forecast ID":"hi",
            "Mfg Commit Date":"hi",
            "Ship Recognition Date":"hi",
            "MRP Date":"2019-11-26T15:59:35.000Z",
            "Build Complete":"hi",
            "Internal Ops Ship Readiness Date":"11/02/2020",
            "Plant":"hi",
            "New/Used":"hi",
            "Core Need Date":"hi",
            "Core Arrival Date":"hi",
            "Refurb Start Date":"hi",
            "Refurb Complete Date":"2020-02-11T15:59:35.000Z",
            "Donor Status":"hi",
            "Core UTID":"hi",
            "Core Notes":"hi",
            "Mfg Status":"hi",
            "Qty":"hi",
            "Configuration Note":"hi",
            "Drop Ship":"hi",
            "RMA":"hi",
            "Product PN":"hi",
            "Hand off Date to DE":"hi",
            "Hand off Date back to Mfg":"hi",
            "Install Start Date":"hi",
            "Cycle Time Days":'1',
            "Delta":'10',
            "Flex01":"hi",
            "Flex02":"hi",
            "Flex03":"hi",
            "Flex04":"hi"
        },
        {
            "Argo ID":"16074",
            "Slot ID/UTID":"CY19Q4TOOL20",
            "Slot Status":"open",
            "Ship Rev Type":"hello",
            "Build Category":"Build Plan",
            "Build Product":"AUDI TT",
            "Slot Plan Notes":"nil",
            "Plan Product Type":"Tool",
            "Ship Risk/Upside":"hi",
            "Ship Risk Reason":"hi",
            "Comment for $ Change":"hi",
            "Committed Ship $":"hi",
            "Secondary Customer Name":"hi",
            "Fab ID":'1',
            "Sales Order #":"hi",
            "Forecast ID":"hi",
            "Mfg Commit Date":"hi",
            "Ship Recognition Date":"hi",
            "MRP Date":"2019-11-26T15:59:35.000Z",
            "Build Complete":"hi",
            "Internal Ops Ship Readiness Date":"10/02/2020",
            "Plant":"hi",
            "New/Used":"hi",
            "Core Need Date":"hi",
            "Core Arrival Date":"hi",
            "Refurb Start Date":"hi",
            "Refurb Complete Date":"2020-02-11T15:59:35.000Z",
            "Donor Status":"hi",
            "Core UTID":"hi",
            "Core Notes":"hi",
            "Mfg Status":"hi",
            "Qty":"hi",
            "Configuration Note":"hi",
            "Drop Ship":"hi",
            "RMA":"hi",
            "Product PN":"hi",
            "Hand off Date to DE":"hi",
            "Hand off Date back to Mfg":"hi",
            "Install Start Date":"hi",
            "Cycle Time Days":'1',
            "Delta":'20',
            "Flex01":"hi",
            "Flex02":"hi",
            "Flex03":"hi",
            "Flex04":"hi"
        }
    ]
    console.log(data[0]['Delta']);
    console.log(typeof data[0]['Delta']);
    mainWindow.webContents.send('getResult:received', JSON.stringify({
        type: 'SUCCESS',
        data: data
    }))
});