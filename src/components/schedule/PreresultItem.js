import React, { Fragment, memo } from 'react'
import { TableCell, TableRow, Checkbox, TextField } from '@material-ui/core'

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

    const regxDate = /^\d\d\/\d\d\/\d\d\d\d$/;
    const regxNum = /^[0-9]+$/;

    return (
        <Fragment>
            <TableRow>
                <TableCell>{argoID}</TableCell>
                <TableCell>{utID}</TableCell>
                <TableCell>{productName}</TableCell>
                <TableCell>
                    <TextField 
                        error={!regxDate.test(mrpDate)} 
                        helperText={!regxDate.test(mrpDate) && 'Invalid Date (dd/mm/yyyy)'} 
                        label={!regxDate.test(mrpDate) && 'Error'} 
                        type='text' 
                        name={mrpName} 
                        value={mrpDate} 
                        onChange={onChange} 
                        required
                    /> 
                </TableCell>
                <TableCell>{mfgCommit}</TableCell>
                <TableCell>{internalRDate}</TableCell>
                <TableCell>{endDate}</TableCell>
                <TableCell>
                    <TextField 
                        error={!regxNum.test(cycleTime)}
                        helperText={!regxNum.test(cycleTime) && 'Invalid Number'}
                        label={!regxNum.test(cycleTime) && 'Error'}
                        type='text' 
                        name={cycleName} 
                        value={cycleTime} 
                        onChange={onChange} 
                        required
                    />
                </TableCell>
                <TableCell align='center'>
                    <Checkbox name={lockMRPDate} onChange={onChange} checked={lockChecked} />
                </TableCell>
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.obj['Cycle Time Days'] === nextProps.obj['Cycle Time Days'] && prevProps.obj['MRP Date'] === nextProps.obj['MRP Date'] && prevProps.obj['Lock MRP Date'] === nextProps.obj['Lock MRP Date']);

export default PreresultItem
