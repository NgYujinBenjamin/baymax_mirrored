import React, { Fragment, memo } from 'react';
import { TableRow, TableCell, Input } from '@material-ui/core';

const HistoryDetailsItem =  memo (({record, onChange}) => {

    const cycleName = 'cycleTime';
    const mrpName = 'mrpDate';

    return (
        <Fragment>
            <TableRow>
                <TableCell>{record.argoID}</TableCell>
                <TableCell>{record.utID}</TableCell>
                <TableCell>{record.productName}</TableCell>
                <TableCell>
                    <Input type='text' name={cycleName} value={record.cycleTime} onChange={onChange} required />
                </TableCell>
                <TableCell>
                    <Input type='text' name={mrpName} value={record.mrpDate} onChange={onChange} required />
                </TableCell>
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.record['cycleTime'] === nextProps.record['cycleTime'] && prevProps.record['mrpDate'] === nextProps.record['mrpDate']);

export default HistoryDetailsItem