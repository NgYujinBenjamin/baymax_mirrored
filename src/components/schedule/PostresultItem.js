import React, { Fragment, memo } from 'react'
import { TableCell, TableRow, Input } from '@material-ui/core'

const PostresultItem = memo(({ result, id, onChange }) => {
    // console.log(result[0])
    let cycleTime = result[0].cycleTimeDays;
    const cycleName = 'cycleTimeDays';

    return (
        <Fragment>
            <TableRow>
                {/* Date format changed here */}
                <TableCell> {result[0].slotID_UTID} </TableCell>
                <TableCell> {result[0].buildProduct} </TableCell>
                <TableCell>  </TableCell>
                <TableCell style={id==='baseline' ? {backgroundColor: '#1cc61c'} : {backgroundColor: '#00b8ff'}}> { new Date(result[0].toolStartDate).toLocaleDateString('en-GB') } </TableCell>
                <TableCell style={id==='baseline' ? {backgroundColor: '#1cc61c'} : {backgroundColor: '#00b8ff'}}> { new Date(result[0].MRPDate).toLocaleDateString('en-GB') } </TableCell>
                <TableCell style={result[0].fabID==='OPEN' ? {backgroundColor: 'yellow'} : null}> { new Date(result[0].MFGCommitDate).toLocaleDateString('en-GB') } </TableCell>
                <TableCell>  </TableCell>
                <TableCell> 
                    { id === 'predicted' ?
                        <Input type='text' name={cycleName} value={result[0]['cycleTimeDays']} onChange={onChange} required /> : result[0]['cycleTimeDays']
                    }
                </TableCell>
                { result.map((obj, index) => (typeof(obj) === "object") ?
                    null : <TableCell key={index}> {obj} </TableCell>
                )}
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.result[0].cycleTimeDays === nextProps.result[0].cycleTimeDays)

// prevProps, nextProps) => prevProps.obj['Cycle Time Days'] === nextProps.obj['Cycle Time Days'] && prevProps.obj['End Date'] === nextProps.obj['End Date'])

export default PostresultItem
