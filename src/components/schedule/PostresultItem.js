import React, { Fragment, memo } from 'react'
import { TableCell, TableRow, Input } from '@material-ui/core'

const PostresultItem = memo(({ result, id, onChange }) => {
    // console.log(result)
    const slotID_UTID = result[0].slotID_UTID;
    const buildProd = result[0].buildProduct;
    const config = result[0].configuration;
    const toolStart = result[0].toolStartDate;
    const fabID = result[0].fabID==='OPEN';
    const MFGCommit = result[0].MFGCommitDate;
    const intReadDate = result[0].intOpsShipReadinessDate;
    const endDate = result[0].endDate;
    const gapDays = result[0].gapDays;

    let cycleTime = result[0].cycleTimeDays;
    const cycleName = 'cycleTimeDays';

    const MRPName = "MRPDate";
    let MRPDate = result[0].MRPDate;

    return (
        <Fragment>
            <TableRow style={{whiteSpace: "nowrap"}}>
                <TableCell> {slotID_UTID} </TableCell>
                <TableCell> {buildProd} </TableCell>
                <TableCell> {config == null ? "NA" : config} </TableCell>
                <TableCell style={id==='baseline' ? {backgroundColor: '#1cc61c'} : {backgroundColor: '#00b8ff'}}> {toolStart} </TableCell>
                <TableCell> {id === 'predicted' ? <Input type='text' name={MRPName} value={MRPDate} onChange={onChange} required /> : MRPDate} </TableCell>
                <TableCell style={fabID ? {backgroundColor: 'yellow'} : null}> {MFGCommit} </TableCell>
                <TableCell> {intReadDate} </TableCell>
                <TableCell> {endDate} </TableCell>
                <TableCell style={gapDays >= 0 ? null : {backgroundColor: 'red'}}> {gapDays} </TableCell>
                <TableCell> 
                    { id === 'predicted' ?
                        <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} required /> : cycleTime
                    }
                </TableCell>
                { result.map((obj, index) => (typeof(obj) === "object") ?
                    null : <TableCell key={index}> {obj} </TableCell>
                )}
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.result[0].cycleTimeDays === nextProps.result[0].cycleTimeDays && prevProps.result[0].MRPDate === nextProps.result[0].MRPDate)

export default PostresultItem
