import React, { Fragment } from 'react';
import { TableHead, TableCell, TableRow } from '@material-ui/core';

const Postresultheader = ({ result }) => {
    // console.log(result);

    return (
        <Fragment>
            <TableHead>
                <TableRow style={{whiteSpace: "nowrap"}}>
                    <TableCell>Slot/UTID</TableCell>
                    <TableCell>Built Product</TableCell>
                    <TableCell>Configuration</TableCell>
                    <TableCell>Tool Start</TableCell>
                    <TableCell>MRP Date</TableCell>
                    <TableCell>MFG Commit Date</TableCell>
                    <TableCell>Int. Readiness Date</TableCell>
                    <TableCell>End Date</TableCell>
                    <TableCell>Gap</TableCell>
                    <TableCell>Cycle Time Days</TableCell>
                    <TableCell>Lock MRP Date?</TableCell>
                    <TableCell>Move to Storage Date</TableCell>
                    { result.map((date, index) =>
                        <TableCell key={index}>{date}</TableCell>
                    )}
                </TableRow>
            </TableHead>
        </Fragment>
    )
}

export default Postresultheader
