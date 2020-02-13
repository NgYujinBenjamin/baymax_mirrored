import React, { Fragment, memo } from 'react'
import { Input, TableCell, TableRow } from '@material-ui/core'

const PreresultItem = memo (({ obj, onChange }) => {
    const argoID = obj['Argo ID'];
    const utID = obj['Slot ID/UTID'];
    const productName = obj['Build Product'];
    const mrpDate = obj['MRP Date'];

    let cycleTime = obj['Cycle Time Days'];
    let endDate = obj['End Date'];
    
    const cycleName = 'Cycle Time Days';
    const endDateName = 'End Date';

    return (
        <Fragment>
            <TableRow>
                <TableCell>{argoID}</TableCell>
                <TableCell>{utID}</TableCell>
                <TableCell>{productName}</TableCell>
                <TableCell>{mrpDate}</TableCell>
                <TableCell>
                    <Input type='text' name={endDateName} value={endDate} onChange={onChange} required /> 
                </TableCell>
                <TableCell>
                    <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} required />
                </TableCell>
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.obj['Cycle Time Days'] === nextProps.obj['Cycle Time Days'] && prevProps.obj['End Date'] === nextProps.obj['End Date']);

export default PreresultItem
