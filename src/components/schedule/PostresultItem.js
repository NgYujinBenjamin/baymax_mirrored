import React, { Fragment, memo } from 'react'
import { TableCell, TableRow, Input } from '@material-ui/core'

const PostresultItem = memo(({ result, id, onChange }) => {
    // console.log(result)
    let cycleTime = result[0].cycleTimeDays;
    const cycleName = 'Cycle Time Days';

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
                    {id==='baseline' ?
                        cycleTime : <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} required />
                    }
                </TableCell>
                { result.map((obj, index) => (typeof(obj) === "object") ?
                    null : <TableCell key={index}> {obj} </TableCell>
                )}
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps[0].cycleTimeDays === nextProps[0].cycleTimeDays)

export default PostresultItem
