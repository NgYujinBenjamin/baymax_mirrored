import React, { Fragment, memo } from 'react'
import { Input, TableCell, TableRow, Checkbox } from '@material-ui/core'

const PreresultItem = memo (({ obj, onChange }) => {
    let argoID = obj['Argo ID'];
    let utID = obj['Slot ID/UTID'];
    let productName = obj['Build Product'];
    let mfgCommit = obj['MFG Commit Date'];
    let internalRDate = obj['Int. Ops Ship Readiness Date'];
    let endDate = obj['End Date'];
    
    // editable
    let cycleTime = obj['Cycle Time Days'];
    let mrpDate = obj['MRP Date'];
    let lockChecked = obj['Lock MRP Date'];
    
    const cycleName = 'Cycle Time Days';
    const mrpName = 'MRP Date';
    const lockMRPDate = 'Lock MRP Date';

    return (
        <Fragment>
            <TableRow>
                <TableCell>{argoID}</TableCell>
                <TableCell>{utID}</TableCell>
                <TableCell>{productName}</TableCell>
                <TableCell>
                    <Input type='text' name={mrpName} value={mrpDate} onChange={onChange} required /> 
                </TableCell>
                <TableCell>{mfgCommit}</TableCell>
                <TableCell>{internalRDate}</TableCell>
                <TableCell>{endDate}</TableCell>
                <TableCell>
                    <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} required />
                </TableCell>
                <TableCell>
                    <Checkbox name={lockMRPDate} onChange={onChange} checked={lockChecked} />
                </TableCell>
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.obj['Cycle Time Days'] === nextProps.obj['Cycle Time Days'] && prevProps.obj['MRP Date'] === nextProps.obj['MRP Date'] && prevProps.obj['Lock MRP Date'] === nextProps.obj['Lock MRP Date']);

export default PreresultItem
