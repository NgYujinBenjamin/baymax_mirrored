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
    // console.log(typeof res.data[0]['Delta Days']);
    // console.log(typeof res.data[0]['Cycle Time Days']);
    // console.log(res.data[1]);
    // console.log(res.data[2]);
    let data = {
        "allProduct": [
          {
            "argoID": 12185,
            "plant": 3008,
            "buildComplete": "No",
            "slotStatus": "Approved - Loaded",
            "planProductType": "Tool",
            "buildCategory": "Build Plan",
            "shipRevenueType": "Revenue",
            "salesOrder": 95754,
            "forecastID": "4073364",
            "slotID_UTID": "CY19Q4TOOL30",
            "fabName": "SUMO NARITA FAB1",
            "buildProduct": "AUDI TT",
            "productPN": "0729127-000",
            "MRPDate": "Oct 1, 2019, 12:00:00 AM",
            "intOpsShipReadinessDate": "Nov 10, 2019, 12:00:00 AM",
            "MFGCommitDate": "Nov 10, 2019, 12:00:00 AM",
            "shipRecogDate": "Nov 10, 2019, 12:00:00 AM",
            "cycleTimeDays": 120,
            "quantity": 1,
            "RMATool": "No",
            "new_Used": "New",
            "fabID": "605440",
            "toolStartDate": "Jun 3, 2019, 12:00:00 AM"
          },
          {
            "argoID": 12136,
            "plant": 3008,
            "buildComplete": "No",
            "slotStatus": "Approved - Loaded",
            "planProductType": "Tool",
            "buildCategory": "NPL",
            "shipRevenueType": "Revenue",
            "salesOrder": 104243,
            "forecastID": "4079225",
            "slotID_UTID": "CY19TOOL22",
            "fabName": "BOTTEGA VENETA",
            "buildProduct": "HONDA VEZEL",
            "productPN": "0756781-000",
            "MRPDate": "Nov 8, 2019, 12:00:00 AM",
            "intOpsShipReadinessDate": "Dec 19, 2019, 12:00:00 AM",
            "MFGCommitDate": "Dec 19, 2019, 12:00:00 AM",
            "shipRecogDate": "Jan 2, 2020, 12:00:00 AM",
            "cycleTimeDays": 150,
            "quantity": 1,
            "RMATool": "No",
            "new_Used": "New",
            "fabID": "613750",
            "toolStartDate": "Jun 11, 2019, 12:00:00 AM"
          }
        ],
        "schedule": [
            {
              "schedule": [
                {
                  "argoID": 12185,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 95754,
                  "forecastID": "4073364",
                  "slotID_UTID": "CY19Q4TOOL30",
                  "fabName": "SUMO NARITA FAB1",
                  "buildProduct": "AUDI TT",
                  "productPN": "0729127-000",
                  "MRPDate": "Oct 1, 2019, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Nov 10, 2019, 12:00:00 AM",
                  "MFGCommitDate": "Nov 10, 2019, 12:00:00 AM",
                  "shipRecogDate": "Nov 10, 2019, 12:00:00 AM",
                  "cycleTimeDays": 120,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "605440",
                  "toolStartDate": "Jun 3, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 13469,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 95505,
                  "forecastID": "4075139",
                  "slotID_UTID": "CY19TOOL45",
                  "fabName": "SUMO NARITA FAB1",
                  "buildProduct": "GLA",
                  "productPN": "0707776-000",
                  "MRPDate": "Oct 27, 2019, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Nov 22, 2019, 12:00:00 AM",
                  "MFGCommitDate": "Nov 25, 2019, 12:00:00 AM",
                  "shipRecogDate": "Nov 25, 2019, 12:00:00 AM",
                  "cycleTimeDays": 25,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "605440",
                  "toolStartDate": "Oct 2, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 13463,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 100686,
                  "forecastID": "4076736",
                  "slotID_UTID": "CY19TOOL46",
                  "fabName": "SHINSHIN HANEBO",
                  "buildProduct": "GLA",
                  "productPN": "0707776-000",
                  "MRPDate": "Nov 22, 2019, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Dec 18, 2019, 12:00:00 AM",
                  "MFGCommitDate": "Dec 18, 2019, 12:00:00 AM",
                  "shipRecogDate": "Dec 18, 2019, 12:00:00 AM",
                  "cycleTimeDays": 25,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "600835",
                  "toolStartDate": "Oct 28, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 14590,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Revenue",
                  "forecastID": "4088273",
                  "slotID_UTID": "CY20Q1TOOL3",
                  "fabName": "TEDDY FAB 1B",
                  "buildProduct": "AUDI A4",
                  "productPN": "0566983-000",
                  "MRPDate": "Feb 17, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Mar 12, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Mar 12, 2020, 12:00:00 AM",
                  "shipRecogDate": "Mar 12, 2020, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "612396",
                  "toolStartDate": "Nov 24, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 17063,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "slotID_UTID": "CY20-80",
                  "fabName": "OPEN",
                  "buildProduct": "AUDI A4",
                  "productPN": "0566983-000",
                  "MRPDate": "May 15, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Jun 26, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                  "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "OPEN",
                  "toolStartDate": "Feb 20, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 17067,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "slotID_UTID": "CY20-84",
                  "fabName": "OPEN",
                  "buildProduct": "AUDI A4",
                  "productPN": "0566983-000",
                  "MRPDate": "Aug 10, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Aug 17, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                  "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "OPEN",
                  "toolStartDate": "May 17, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 16658,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 92453,
                  "forecastID": "4068437",
                  "slotID_UTID": "CY20-52",
                  "fabName": "TOMMY HILFIGER TOKYO CORPORATION",
                  "buildProduct": "TOYOTA RAV4",
                  "productPN": "0507333-000",
                  "MRPDate": "Sep 25, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Oct 2, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Oct 2, 2020, 12:00:00 AM",
                  "shipRecogDate": "Oct 2, 2020, 12:00:00 AM",
                  "cycleTimeDays": 43,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "614207",
                  "toolStartDate": "Aug 13, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 17039,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Proposed Add",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 99319,
                  "forecastID": "4079171",
                  "slotID_UTID": "20Q4TOOL 2",
                  "fabName": "BAILEY'S ICE-CREAM SEMICONDUCTOR",
                  "buildProduct": "GLA",
                  "productPN": "0707776-000",
                  "MRPDate": "Oct 22, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Oct 29, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Dec 15, 2020, 12:00:00 AM",
                  "shipRecogDate": "Dec 15, 2020, 12:00:00 AM",
                  "cycleTimeDays": 25,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "614343",
                  "toolStartDate": "Sep 27, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 17042,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Proposed Add",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 99322,
                  "forecastID": "4079174",
                  "slotID_UTID": "20Q4TOOL 5",
                  "fabName": "BAILEY'S ICE-CREAM SEMICONDUCTOR",
                  "buildProduct": "GLA",
                  "productPN": "0707776-000",
                  "MRPDate": "Nov 19, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Nov 26, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Dec 15, 2020, 12:00:00 AM",
                  "shipRecogDate": "Dec 15, 2020, 12:00:00 AM",
                  "cycleTimeDays": 25,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "614343",
                  "toolStartDate": "Oct 25, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 17045,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Proposed Add",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "forecastID": "4082961",
                  "slotID_UTID": "20Q4TOOL 8",
                  "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                  "buildProduct": "GLA",
                  "productPN": "0707776-000",
                  "MRPDate": "Dec 17, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Dec 24, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Dec 15, 2020, 12:00:00 AM",
                  "shipRecogDate": "Dec 15, 2020, 12:00:00 AM",
                  "cycleTimeDays": 25,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "615115",
                  "toolStartDate": "Nov 22, 2020, 12:00:00 AM"
                }
              ],
              "availableDate": "Dec 17, 2020, 12:00:00 AM"
            },
            {
              "schedule": [
                {
                  "argoID": 12136,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "NPL",
                  "shipRevenueType": "Revenue",
                  "salesOrder": 104243,
                  "forecastID": "4079225",
                  "slotID_UTID": "CY19TOOL22",
                  "fabName": "BOTTEGA VENETA",
                  "buildProduct": "HONDA VEZEL",
                  "productPN": "0756781-000",
                  "MRPDate": "Nov 8, 2019, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Dec 19, 2019, 12:00:00 AM",
                  "MFGCommitDate": "Dec 19, 2019, 12:00:00 AM",
                  "shipRecogDate": "Jan 2, 2020, 12:00:00 AM",
                  "cycleTimeDays": 150,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "613750",
                  "toolStartDate": "Jun 11, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 14587,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Consignment",
                  "forecastID": "4083808",
                  "slotID_UTID": "CY20Q1TOOL5",
                  "fabName": "SARSI 7",
                  "buildProduct": "AUDI A4",
                  "productPN": "0755956-000",
                  "MRPDate": "Feb 5, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Feb 19, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Feb 19, 2020, 12:00:00 AM",
                  "shipRecogDate": "Feb 19, 2020, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "608402",
                  "toolStartDate": "Nov 12, 2019, 12:00:00 AM"
                },
                {
                  "argoID": 17363,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Proposed Add",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "slotID_UTID": "CY20-88",
                  "fabName": "OPEN",
                  "buildProduct": "TOYOTA HARRIER",
                  "productPN": "0556104-000",
                  "MRPDate": "Mar 23, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Mar 30, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                  "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                  "cycleTimeDays": 43,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "OPEN",
                  "toolStartDate": "Feb 9, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 16958,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "slotID_UTID": "CY20-58",
                  "fabName": "OPEN",
                  "buildProduct": "AUDI A4",
                  "productPN": "0566983-000",
                  "MRPDate": "Jun 19, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Jun 26, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                  "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "OPEN",
                  "toolStartDate": "Mar 26, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 16647,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Approved - Loaded",
                  "planProductType": "Tool",
                  "buildCategory": "Build Plan",
                  "shipRevenueType": "Revenue",
                  "forecastID": "4087342",
                  "slotID_UTID": "CY20-41",
                  "fabName": "INFINITY TECHNOLOGY GERMANY",
                  "buildProduct": "HONDA VEZEL",
                  "productPN": "0756781-000",
                  "MRPDate": "Aug 20, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Aug 27, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Sep 15, 2020, 12:00:00 AM",
                  "shipRecogDate": "Sep 15, 2020, 12:00:00 AM",
                  "cycleTimeDays": 60,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "602023",
                  "toolStartDate": "Jun 21, 2020, 12:00:00 AM"
                },
                {
                  "argoID": 17036,
                  "plant": 3008,
                  "buildComplete": "No",
                  "slotStatus": "Proposed Add",
                  "planProductType": "Tool",
                  "buildCategory": "Pre-Build",
                  "shipRevenueType": "Revenue",
                  "forecastID": "4088213",
                  "slotID_UTID": "CY20-76",
                  "fabName": "TEDDY FAB C",
                  "buildProduct": "AUDI A4",
                  "productPN": "0566983-000",
                  "MRPDate": "Nov 19, 2020, 12:00:00 AM",
                  "intOpsShipReadinessDate": "Nov 26, 2020, 12:00:00 AM",
                  "MFGCommitDate": "Dec 15, 2020, 12:00:00 AM",
                  "shipRecogDate": "Dec 15, 2020, 12:00:00 AM",
                  "cycleTimeDays": 85,
                  "quantity": 1,
                  "RMATool": "No",
                  "new_Used": "New",
                  "fabID": "614439",
                  "toolStartDate": "Aug 26, 2020, 12:00:00 AM"
                }
              ],
              "availableDate": "Nov 19, 2020, 12:00:00 AM"
            }
        ],
        unfulfilled: [
            {
                "argoID": 13485,
                "plant": 3008,
                "buildComplete": "No",
                "slotStatus": "Approved - Loaded",
                "planProductType": "Tool",
                "buildCategory": "Build Plan",
                "shipRevenueType": "Revenue",
                "salesOrder": 100664,
                "forecastID": "4069938",
                "slotID_UTID": "CY19TOOL39",
                "fabName": "SHINSHIN HANEBO",
                "buildProduct": "GLA",
                "productPN": "0707776-000",
                "MRPDate": "Oct 15, 2019, 12:00:00 AM",
                "intOpsShipReadinessDate": "Nov 1, 2019, 12:00:00 AM",
                "MFGCommitDate": "Nov 1, 2019, 12:00:00 AM",
                "shipRecogDate": "Nov 1, 2019, 12:00:00 AM",
                "cycleTimeDays": 25,
                "quantity": 1,
                "RMATool": "No",
                "new_Used": "New",
                "fabID": "600835",
                "toolStartDate": "Sep 20, 2019, 12:00:00 AM"
            }
        ]
    }
    mainWindow.webContents.send('getResult:received', JSON.stringify({
        type: 'SUCCESS',
        data: data
    }))
});