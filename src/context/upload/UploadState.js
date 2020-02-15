import React, { useReducer } from 'react';
import UploadContext from './uploadContext';
import UploadReducer from './uploadReducer';
import XLSX from 'xlsx';
import axios from 'axios';
import { SET_BASELINE, SET_SCHEDULE, SET_BAYS, CLEAR_PRERESULT, SET_LOADING, UPDATE_SCHEDULE, CREATE_RESULT, EXPORT_RESULT, EXPORT_SCHEDULE, CLEAR_ALL, SAVE_RESULT, UPLOAD_ERROR, UPLOAD_CLEAR_ERROR, CLEAR_ZERO } from '../types';

const UploadState = (props) => {
    const initialState = {
        baseline: null,
        schedule: null,
        bays: '',
        loading: false,
        postResult: null,
        scheduleDone: false,
        error: null,
        scheduletest: {
          "baseline":
            {
              "CY19Q1" : [
                [
                  "May 1, 2019, 12:00:00 AM",
                  "May 2, 2019, 12:00:00 AM",
                  "May 3, 2019, 12:00:00 AM",
                  "May 4, 2019, 12:00:00 AM",
                  "May 5, 2019, 12:00:00 AM",
                  "May 6, 2019, 12:00:00 AM",
                  "May 7, 2019, 12:00:00 AM",
                  "May 8, 2019, 12:00:00 AM",
                  "May 9, 2019, 12:00:00 AM",
                  "May 10, 2019, 12:00:00 AM",
                  "May 11, 2019, 12:00:00 AM",
                  "May 12, 2019, 12:00:00 AM",
                  "May 13, 2019, 12:00:00 AM",
                  "May 14, 2019, 12:00:00 AM",
                  "May 15, 2019, 12:00:00 AM",
                  "May 16, 2019, 12:00:00 AM"
                ],
                [
                  [
                    {
                      "argoID": 16661,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "forecastID": "4080671",
                      "slotID_UTID": "CY20-55",
                      "fabName": "SUMO NARITA FAB1",
                      "buildProduct": "AUDI TT",
                      "productPN": "0729127-000",
                      "MRPDate": "Sep 2, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Sep 9, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Sep 15, 2020, 12:00:00 AM",
                      "shipRecogDate": "Sep 15, 2020, 12:00:00 AM",
                      "cycleTimeDays": 120,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "605440",
                      "toolStartDate": "May 5, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 16642,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "forecastID": "4080469",
                      "slotID_UTID": "CY20-38",
                      "fabName": "NISSIN CUP NOODLE",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Jul 30, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 6, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Aug 6, 2020, 12:00:00 AM",
                      "shipRecogDate": "Aug 6, 2020, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "605218",
                      "toolStartDate": "May 6, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 15986,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "salesOrder": 102414,
                      "forecastID": "4082960",
                      "slotID_UTID": "20Q2TOOL 9",
                      "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                      "buildProduct": "GLA",
                      "productPN": "0707776-000",
                      "MRPDate": "Jun 3, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Jun 3, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Jun 3, 2020, 12:00:00 AM",
                      "shipRecogDate": "Jun 15, 2020, 12:00:00 AM",
                      "cycleTimeDays": 25,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "615115",
                      "toolStartDate": "May 9, 2020, 12:00:00 AM"
                    },
                    "E",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 15991,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "salesOrder": 102416,
                      "forecastID": "4084923",
                      "slotID_UTID": "CY20-21",
                      "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                      "buildProduct": "HONDA CIVIC",
                      "productPN": "0260000-000",
                      "MRPDate": "Jun 9, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Jun 16, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Jun 16, 2020, 12:00:00 AM",
                      "shipRecogDate": "Jun 16, 2020, 12:00:00 AM",
                      "cycleTimeDays": 30,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "615115",
                      "toolStartDate": "May 10, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 17067,
                      "plant": 3008,
                      "buildComplete": 0,
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
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 17, 2020, 12:00:00 AM"
                    },
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 17066,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Pre-Build",
                      "shipRevenueType": "Revenue",
                      "slotID_UTID": "CY20-83",
                      "fabName": "OPEN",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Aug 18, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 25, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                      "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 25, 2020, 12:00:00 AM"
                    },
                    "O",
                    "E",
                    "E",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ]
                ]
              ], 
              "CY19Q2" : [
                [
                  "Jan 1, 2019, 12:00:00 AM",
                  "Jan 2, 2019, 12:00:00 AM",
                  "Jan 3, 2019, 12:00:00 AM",
                  "Jan 4, 2019, 12:00:00 AM",
                  "Jan 5, 2019, 12:00:00 AM",
                  "Jan 6, 2019, 12:00:00 AM",
                  "Jan 7, 2019, 12:00:00 AM",
                  "Jan 8, 2019, 12:00:00 AM",
                  "Jan 9, 2019, 12:00:00 AM",
                  "Jan 10, 2019, 12:00:00 AM",
                  "Jan 11, 2019, 12:00:00 AM",
                  "Jan 12, 2019, 12:00:00 AM",
                  "Jan 13, 2019, 12:00:00 AM",
                  "Jan 14, 2019, 12:00:00 AM",
                  "Jan 15, 2019, 12:00:00 AM",
                  "Jan 16, 2019, 12:00:00 AM"
                ],
                [
                  [
                    {
                      "argoID": 16661,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "forecastID": "4080671",
                      "slotID_UTID": "CY20-55",
                      "fabName": "SUMO NARITA FAB1",
                      "buildProduct": "AUDI TT",
                      "productPN": "0729127-000",
                      "MRPDate": "Sep 2, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Sep 9, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Sep 15, 2020, 12:00:00 AM",
                      "shipRecogDate": "Sep 15, 2020, 12:00:00 AM",
                      "cycleTimeDays": 120,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "605440",
                      "toolStartDate": "May 5, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 16642,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "forecastID": "4080469",
                      "slotID_UTID": "CY20-38",
                      "fabName": "NISSIN CUP NOODLE",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Jul 30, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 6, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Aug 6, 2020, 12:00:00 AM",
                      "shipRecogDate": "Aug 6, 2020, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "605218",
                      "toolStartDate": "May 6, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 15986,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "salesOrder": 102414,
                      "forecastID": "4082960",
                      "slotID_UTID": "20Q2TOOL 9",
                      "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                      "buildProduct": "GLA",
                      "productPN": "0707776-000",
                      "MRPDate": "Jun 3, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Jun 3, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Jun 3, 2020, 12:00:00 AM",
                      "shipRecogDate": "Jun 15, 2020, 12:00:00 AM",
                      "cycleTimeDays": 25,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "615115",
                      "toolStartDate": "May 9, 2020, 12:00:00 AM"
                    },
                    "E",
                    "O",
                    "E",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 15991,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "salesOrder": 102416,
                      "forecastID": "4084923",
                      "slotID_UTID": "CY20-21",
                      "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                      "buildProduct": "HONDA CIVIC",
                      "productPN": "0260000-000",
                      "MRPDate": "Jun 9, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Jun 16, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Jun 16, 2020, 12:00:00 AM",
                      "shipRecogDate": "Jun 16, 2020, 12:00:00 AM",
                      "cycleTimeDays": 30,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "615115",
                      "toolStartDate": "May 10, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 17067,
                      "plant": 3008,
                      "buildComplete": 0,
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
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 17, 2020, 12:00:00 AM"
                    },
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 17066,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Pre-Build",
                      "shipRevenueType": "Revenue",
                      "slotID_UTID": "CY20-83",
                      "fabName": "OPEN",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Aug 18, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 25, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                      "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 25, 2020, 12:00:00 AM"
                    },
                    "O",
                    "E",
                    "E",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ]
                ]
              ]
            },
            "predicted":
            {
              "CY19Q1" : [
                [
                  "May 1, 2019, 12:00:00 AM",
                  "May 2, 2019, 12:00:00 AM",
                  "May 3, 2019, 12:00:00 AM",
                  "May 4, 2019, 12:00:00 AM",
                  "May 5, 2019, 12:00:00 AM",
                  "May 6, 2019, 12:00:00 AM",
                  "May 7, 2019, 12:00:00 AM",
                  "May 8, 2019, 12:00:00 AM",
                  "May 9, 2019, 12:00:00 AM",
                  "May 10, 2019, 12:00:00 AM",
                  "May 11, 2019, 12:00:00 AM",
                  "May 12, 2019, 12:00:00 AM",
                  "May 13, 2019, 12:00:00 AM",
                  "May 14, 2019, 12:00:00 AM",
                  "May 15, 2019, 12:00:00 AM",
                  "May 16, 2019, 12:00:00 AM"
                ],
                [
                  [
                    {
                      "argoID": 17066,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Pre-Build",
                      "shipRevenueType": "Revenue",
                      "slotID_UTID": "CY20-83",
                      "fabName": "OPEN",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Aug 18, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 25, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                      "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 25, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 16642,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "forecastID": "4080469",
                      "slotID_UTID": "CY20-38",
                      "fabName": "NISSIN CUP NOODLE",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Jul 30, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 6, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Aug 6, 2020, 12:00:00 AM",
                      "shipRecogDate": "Aug 6, 2020, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "605218",
                      "toolStartDate": "May 6, 2020, 12:00:00 AM"
                    },
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ],
                  [
                    {
                      "argoID": 15986,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Build Plan",
                      "shipRevenueType": "Revenue",
                      "salesOrder": 102414,
                      "forecastID": "4082960",
                      "slotID_UTID": "20Q2TOOL 9",
                      "fabName": "JIA JIA LIANG CHA ELECTRONIC",
                      "buildProduct": "GLA",
                      "productPN": "0707776-000",
                      "MRPDate": "Jun 3, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Jun 3, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Jun 3, 2020, 12:00:00 AM",
                      "shipRecogDate": "Jun 15, 2020, 12:00:00 AM",
                      "cycleTimeDays": 25,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "615115",
                      "toolStartDate": "May 9, 2020, 12:00:00 AM"
                    },
                    "E",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ]
                ]
              ], 
              "CY19Q2" : [
                [
                  "Jan 1, 2019, 12:00:00 AM",
                  "Jan 2, 2019, 12:00:00 AM",
                  "Jan 3, 2019, 12:00:00 AM",
                  "Jan 4, 2019, 12:00:00 AM",
                  "Jan 5, 2019, 12:00:00 AM",
                  "Jan 6, 2019, 12:00:00 AM",
                  "Jan 7, 2019, 12:00:00 AM",
                  "Jan 8, 2019, 12:00:00 AM",
                  "Jan 9, 2019, 12:00:00 AM",
                  "Jan 10, 2019, 12:00:00 AM",
                  "Jan 11, 2019, 12:00:00 AM",
                  "Jan 12, 2019, 12:00:00 AM",
                  "Jan 13, 2019, 12:00:00 AM",
                  "Jan 14, 2019, 12:00:00 AM",
                  "Jan 15, 2019, 12:00:00 AM",
                  "Jan 16, 2019, 12:00:00 AM"
                ],
                [
                  [
                    {
                      "argoID": 17066,
                      "plant": 3008,
                      "buildComplete": 0,
                      "slotStatus": "Approved - Loaded",
                      "planProductType": "Tool",
                      "buildCategory": "Pre-Build",
                      "shipRevenueType": "Revenue",
                      "slotID_UTID": "CY20-83",
                      "fabName": "OPEN",
                      "buildProduct": "AUDI A4",
                      "productPN": "0566983-000",
                      "MRPDate": "Aug 18, 2020, 12:00:00 AM",
                      "intOpsShipReadinessDate": "Aug 25, 2020, 12:00:00 AM",
                      "MFGCommitDate": "Mar 30, 2021, 12:00:00 AM",
                      "shipRecogDate": "Mar 30, 2021, 12:00:00 AM",
                      "cycleTimeDays": 85,
                      "quantity": 1,
                      "RMATool": 0,
                      "new_Used": "New",
                      "fabID": "OPEN",
                      "toolStartDate": "May 25, 2020, 12:00:00 AM"
                    },
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E",
                    "O",
                    "O",
                    "O",
                    "O",
                    "E",
                    "E",
                    "E",
                    "E"
                  ]
                ]
              ], 
            }
        }
    }

    const [state, dispatch] = useReducer(UploadReducer, initialState);

    //methods all over here
    //export schedule
    const createExportSchedule = async (res) => {
        setLoading();
        // const config = {
        //   headers: {
        //     'Content-Type': 'application/json'
        //   }
        // }
        // const res = await axios.post('http://localhost:8080/exportschedule', file, config);

        //Format - 23 May 2019 - day month year
        const dateoptions = { year: 'numeric', month: 'long', day: 'numeric' };
        const datesFormat = res.weekOf.map(val => val = new Date(val).toLocaleDateString('en-GB', dateoptions));

        //headers for the columns
        const header = [
            ['Shipment Plan'],
            ['SO', 'UTID', 'Customer', 'Configuration', 'Model', 'Tool Start', 'MFG Commit Ship Date', 'Bay', 'Week of (Friday)', ...datesFormat]
        ];

        //week of : mfg commit ship date - tool start
        //formed the data for exporting
        const resdata = []
        res.bayUsage.forEach(arr => {
            const temp = []
            temp.push(...arr)
            temp.shift();
            temp.unshift(
              arr[0].argoID, 
              arr[0].slotID_UTID, 
              arr[0].fabName, 
              '', 
              arr[0].buildProduct, 
              new Date(arr[0].toolStartDate).toLocaleDateString('en-GB'), 
              new Date(arr[0].MFGCommitDate).toLocaleDateString('en-GB'), 
              '', 
              Math.round((new Date(arr[0].MFGCommitDate) - new Date(arr[0].toolStartDate)) / (1000 * 60 * 60 * 24))
            );
            resdata.push(temp);
        });
        
        //getting total number of 'O'
        let allCount = {};
        resdata.forEach(val => {
          let counter = 0;
          for(let i = 9; i < val.length; i++){
            if(val[i] === 'O'){
              if( !(datesFormat[counter] in allCount) ){
                allCount[datesFormat[counter]] = 1;
              } else {
                allCount[datesFormat[counter]] += 1;
              }
            }
            counter++;
          }
        });
        const arrAllCount = [];
        arrAllCount.push(allCount);

        //putting it into excel
        const wb = XLSX.utils.book_new();
        const ws = XLSX.utils.aoa_to_sheet(header);
        XLSX.utils.sheet_add_aoa(ws, resdata, {
            origin: -1
        });
        XLSX.utils.sheet_add_aoa(ws, [['No. of bays required']], {
          origin: { r: resdata.length+3, c: 8 }
        });
        XLSX.utils.sheet_add_json(ws, arrAllCount, {
          skipHeader: true,
          origin: { r: resdata.length+3, c: 9 }
        });

        XLSX.utils.book_append_sheet(wb, ws, 'Shipment Plan');

        XLSX.writeFile(wb, `Bay_Requirement_${new Date().toLocaleDateString('en-GB')}.xlsx`);

        dispatch({
          type: EXPORT_SCHEDULE
        });
    }

    //export file
    const createExport = (file) => {
        setLoading();

        const allProductResult = file.allProduct;

        //create new workbook
        const massWB = XLSX.utils.book_new(); 

        //create new worksheet
        const massWsOne = XLSX.utils.json_to_sheet(allProductResult);

        //parse in a worksheet into the workbook
        //1st arg: workbook, 2nd arg: worksheet, 3rd: name of worksheet
        XLSX.utils.book_append_sheet(massWB, massWsOne, 'SAP Document Export');

        //write workbook to file
        //1st arg: workbook, 2nd arg: name of file
        XLSX.writeFile(massWB, 'Mass_Slot_Upload.xlsx');

        dispatch({
            type: EXPORT_RESULT
        })
    }

    //save file
    const saveFile = async (file) => {
        setLoading();

        const output = file.allProduct;

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            await axios.post('http://localhost:8080/save', output, config)
            dispatch({
                type: SAVE_RESULT
            })
        } catch (err) {
            //prompt error

        }
    }

    //clear all - back to default state
    const clearAll = () => dispatch({ type: CLEAR_ALL })

    //create result - mass slot upload
    const createResult = async (objs, bay, baseline) => {
        setLoading();

        objs.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })

        let preResult = null;
        // baseline !== null ? preResult = { bay: bay, data: [...objs], baseline: baseline } : preResult = { bay: bay, data: [...objs] }
        preResult =  [...objs];
        // console.log(preResult)

        const config = {
            headers: {
                'Content-Type': 'application/json'
            }
        }

        try {
            const res = await axios.post('http://localhost:8080/algo', preResult, config);
            console.log(res.data)

            //change schedule formatting
            const output = [];
            res.data.schedule.forEach(arr => {
                arr.schedule.forEach(val => {
                    output.push(val);
                })
            })
            res.data.schedule = output;

            // change date formatting - allProduct
            res.data.allProduct.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')                 
            })

            // change date formatting - schedule
            res.data.schedule.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')                  
            })

            // change date formatting - unfulfilled
            res.data.unfulfilled.forEach(val => {
                val['MRPDate'] = val['MRPDate'] === undefined ? '' : new Date(val['MRPDate']).toLocaleDateString('en-GB')
                val['intOpsShipReadinessDate'] = val['intOpsShipReadinessDate'] === undefined ? '' : new Date(val['intOpsShipReadinessDate']).toLocaleDateString('en-GB')
                val['MFGCommitDate'] = val['MFGCommitDate'] === undefined ? '' : new Date(val['MFGCommitDate']).toLocaleDateString('en-GB')
                val['shipRecogDate'] = val['shipRecogDate'] === undefined ? '' : new Date(val['shipRecogDate']).toLocaleDateString('en-GB')
                val['toolStartDate'] = val['toolStartDate'] === undefined ? '' : new Date(val['toolStartDate']).toLocaleDateString('en-GB')  
            })

            dispatch({
                type: CREATE_RESULT,
                payload: res.data
            })
        } catch (err) {
            //prompt error

        }
    }

    //import masterops
    const setSchedule = async (file) => {
        setLoading();

        let data = await convertExcelToJSON(file);
        let scheduleCounter = false;

        //if excelfile is not masterops data/excel file
        const firstTenData = data.slice(0,10);
        firstTenData.forEach(val => {
            if(!('Argo ID' in val) && !('Slot ID/UTID' in val) && !('Build Product' in val) && !('Cycle Time Days' in val) && !('MRP Date' in val)){
                scheduleCounter = true
            }
        })

        if(scheduleCounter){
            dispatch({
                type: UPLOAD_ERROR,
                payload: 'Please upload a proper Masterops excel file'
            })
        } else {
            //else (masterops data/excel file)
            let filtered = data.filter(obj => obj['Plan Product Type'] === 'Tool');
            filtered.forEach(obj => {
                obj['MRP Date'] = obj['MRP Date'] === undefined ? '' : obj['MRP Date'].toLocaleDateString('en-GB');
                obj['Created On'] = obj['Created On'] === undefined ? '' : obj['Created On'].toLocaleDateString('en-GB');
                obj['Created Time'] = obj['Created Time'] === undefined ? '' : obj['Created Time'].toLocaleDateString('en-GB');
                obj['SAP Customer Req Date'] = obj['SAP Customer Req Date'] === undefined ? '' : obj['SAP Customer Req Date'].toLocaleDateString('en-GB');
                obj['Ship Recog Date'] = obj['Ship Recog Date'] === undefined ? '' : obj['Ship Recog Date'].toLocaleDateString('en-GB');
                obj['Slot Request Date'] = obj['Slot Request Date'] === undefined ? '' : obj['Slot Request Date'].toLocaleDateString('en-GB');
                obj['Int. Ops Ship Readiness Date'] = obj['Int. Ops Ship Readiness Date'] === undefined ? '' : obj['Int. Ops Ship Readiness Date'].toLocaleDateString('en-GB');
                obj['MFG Commit Date'] = obj['MFG Commit Date'] === undefined ? '' : obj['MFG Commit Date'].toLocaleDateString('en-GB');
                obj['Div Commit Date'] = obj['Div Commit Date'] === undefined ? '' : obj['Div Commit Date'].toLocaleDateString('en-GB');
                obj['Changed On'] = obj['Changed On'] === undefined ? '' : obj['Changed On'].toLocaleDateString('en-GB');
                obj['Last Changed Time'] = obj['Last Changed Time'] === undefined ? '' : obj['Last Changed Time'].toLocaleDateString('en-GB');

            });

            filtered[filtered.length - 1]['Argo ID'] === undefined && filtered.pop();

            // console.log(JSON.stringify(filtered))

            dispatch({
                type: SET_SCHEDULE,
                payload: filtered
            });
        }
    }

    //update masterops
    const updateSchedule = (objs) => {
        //convert cycle time days to integer
        objs.forEach(obj => {
            obj['Cycle Time Days'] = parseInt(obj['Cycle Time Days'])
        })
        dispatch({ type: UPDATE_SCHEDULE, payload: objs })
    }

    //set bays
    const setBays = (num) => dispatch({ type: SET_BAYS, payload: num })

    //clear preresult
    const clearPreresult = () => dispatch({ type: CLEAR_PRERESULT })

    //clear upload errors
    const uploadClearError = () => dispatch({ type: UPLOAD_CLEAR_ERROR })

    //set baseline
    const setBaseline = async (file) => {
        const data = await convertExcelToJSON(file);
        console.log(data);
        dispatch({
            type: SET_BASELINE,
            payload: data
        })
    }

    //convert excel to json
    const convertExcelToJSON = async (file) => {
        return new Promise((resolve, reject) => {
            let reader = new FileReader();
            reader.onload = (e) => {
                let data = new Uint8Array(e.target.result);
                let workbook = XLSX.read(data, { type: 'array', cellDates: true });
                let firstSheetName = workbook.SheetNames[0];
                let worksheet = workbook.Sheets[firstSheetName];
                let excelData = XLSX.utils.sheet_to_json(worksheet);
                resolve(excelData);
            };
            reader.readAsArrayBuffer(file);
        });
    }

    //set loading
    const setLoading = () => dispatch({ type: SET_LOADING })

    //back to default - for logout purpose
    const clearZero = () => dispatch({ type: CLEAR_ZERO })

    return <UploadContext.Provider
        value={{
            baseline: state.baseline,
            schedule: state.schedule,
            bays: state.bays,
            loading: state.loading,
            scheduleDone: state.scheduleDone,
            postResult: state.postResult,
            error: state.error,
            scheduletest: state.scheduletest,
            setBaseline,
            setSchedule,
            setBays,
            updateSchedule,
            createResult,
            createExportSchedule,
            clearPreresult,
            setLoading,
            createExport,
            saveFile,
            clearAll,
            uploadClearError,
            clearZero
        }}>
        {props.children}
    </UploadContext.Provider>
}

export default UploadState
