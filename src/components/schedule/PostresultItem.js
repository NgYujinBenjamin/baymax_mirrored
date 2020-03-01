import React, { Fragment, memo, useContext } from 'react';
import { TableCell, TableRow, Input, Checkbox, FormHelperText } from '@material-ui/core';
import UploadContext from '../../context/upload/uploadContext';
import { makeStyles } from '@material-ui/core/styles';

const PostresultItem = memo(({ result, id, onChange }) => {
    
    const uploadContext = useContext(UploadContext);
    const { postResultErrors } = uploadContext;
    
    const useStyles = makeStyles(theme => ({
        errorText: {
            color: 'red',
            fontSize: '12px'
        },
    }));

    const classes = useStyles();

    const argoID = result[0].argoID;
    const slotID_UTID = result[0].slotID_UTID;
    const buildProd = result[0].buildProduct;
    const config = result[0].configuration;
    const toolStart = result[0].toolStartDate;
    const fabID = result[0].fabID==='OPEN';
    const MFGCommit = result[0].MFGCommitDate;
    const intReadDate = result[0].intOpsShipReadinessDate;
    const endDate = result[0].endDate;
    const gapDays = result[0].gapDays;

    const cycleName = 'cycleTimeDays';
    let cycleTime = result[0].cycleTimeDays;

    const MRPName = "MRPDate";
    let MRPDate = result[0].MRPDate;

    const storageName = "moveToStorage";
    let storageDate = result[0].moveToStorage;

    const lockMRPName = "lockMRPDate";
    let lockMRPCheck = result[0].lockMRPDate;
    
    return (
        <Fragment>
            <TableRow style={{whiteSpace: "nowrap"}}>
                <TableCell> {slotID_UTID} </TableCell>
                <TableCell> {buildProd} </TableCell>
                <TableCell> {config == null ? "NA" : config} </TableCell>
                <TableCell style={id==='baseline' ? {backgroundColor: '#1cc61c'} : {backgroundColor: '#00b8ff'}}> {toolStart} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input className={postResultErrors.includes(argoID + "_MRPDate") ? classes.errorField : ''} type='text' name={MRPName} value={MRPDate} onChange={onChange} required /> : MRPDate } 
                    { postResultErrors.includes(argoID + "_MRPDate") && <FormHelperText className={classes.errorText}>Invalid Date</FormHelperText>}
                </TableCell>
                <TableCell style={fabID ? {backgroundColor: 'yellow'} : null}> {MFGCommit} </TableCell>
                <TableCell> {intReadDate} </TableCell>
                <TableCell> {endDate} </TableCell>
                <TableCell style={gapDays >= 0 ? null : {backgroundColor: 'red'}}> {gapDays} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input type='text' name={cycleName} value={cycleTime} onChange={onChange} required /> : cycleTime }
                    { postResultErrors.includes(argoID + "_cycleTimeDays") && <FormHelperText className={classes.errorText}>Invalid Number</FormHelperText>}
                </TableCell>
                <TableCell align='center'> {id === 'predicted' ? <Checkbox name={lockMRPName} checked={lockMRPCheck} onChange={onChange} color="primary" /> : null} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input type='text' name={storageName} value={storageDate} onChange={onChange} /> : null }
                    { postResultErrors.includes(argoID + "_moveToStorage") && <FormHelperText className={classes.errorText}>Invalid Date</FormHelperText>}                     
                </TableCell>
                { result.map((obj, index) => (typeof(obj) === "object") ?
                    null : <TableCell key={index}> {obj} </TableCell>
                )}
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.result[0].cycleTimeDays === nextProps.result[0].cycleTimeDays && prevProps.result[0].MRPDate === nextProps.result[0].MRPDate && prevProps.result[0].moveToStorage === nextProps.result[0].moveToStorage && prevProps.result[0].lockMRPDate === nextProps.result[0].lockMRPDate)

export default PostresultItem
