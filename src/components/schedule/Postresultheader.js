import React, { Fragment } from 'react'
import { TableHead, TableCell, TableRow } from '@material-ui/core'

const Postresultheader = ({ result }) => {
    // console.log(result[0]);
    
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
                    <TableCell>Gap</TableCell>
                    <TableCell>Cycle Time Days</TableCell>
                    { result.map(date =>
                        <TableCell>{date.split(",")[0].split(" ")[1] + " " + date.split(",")[0].split(" ")[0] + " " + date.split(",")[1]}</TableCell>
                    )}
                </TableRow>
            </TableHead>
        </Fragment>
    )
}

export default Postresultheader
