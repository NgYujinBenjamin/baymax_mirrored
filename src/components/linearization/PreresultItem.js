import React, { Fragment, memo } from 'react'
import { Input, TableCell, TableRow } from '@material-ui/core'

const PreresultItem = memo (({ obj, onChange }) => {
    const argoID = obj['Argo ID'];
    const utID = obj['Slot ID/UTID'];
    const productName = obj['Build Product'];
    const cycleTime = obj['Cycle Time Days'];
    const mrpDate = obj['MRP Date'];

    const cycleName = 'Cycle Time Days';
    const mrpName = 'MRP Date';

    return (
        <Fragment>
            <TableRow>
                <TableCell>{argoID}</TableCell>
                <TableCell>{utID}</TableCell>
                <TableCell>{productName}</TableCell>
                <TableCell>
                    <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} />
                </TableCell>
                <TableCell>
                    <Input type='text' name={mrpName} value={mrpDate} onChange={onChange} />
                </TableCell>
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.obj['Cycle Time Days'] === nextProps.obj['Cycle Time Days'] && prevProps.obj['MRP Date'] === nextProps.obj['MRP Date']);

export default PreresultItem
