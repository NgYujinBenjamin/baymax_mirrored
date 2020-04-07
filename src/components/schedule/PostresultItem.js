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
    const fabName = result[0].fabName==='OPEN';
    const MFGCommit = result[0].MFGCommitDate;
    const intReadDate = result[0].intOpsShipReadinessDate;
    const endDate = result[0].endDate;
    const gapDays = result[0].gapDays;

    const cycleName = 'cycleTimeDays';
    let cycleTime = result[0].cycleTimeDays;

    const MRPName = "MRPDate";
    let MRPDate = result[0].MRPDate;

    const storageName = "sendToStorageDate";
    let storageDate = result[0].sendToStorageDate;

    const lockMRPName = "lockMRPDate";
    let lockMRPCheck = result[0].lockMRPDate;
    
    const MRPUnique = argoID + "_" + MRPName;
    const storageUnique = argoID + "_" + storageName;
    const cycleTimeUnique = argoID + "_" + cycleName;

    return (
        <Fragment>
            <TableRow style={{whiteSpace: "nowrap"}}>
                <TableCell> {slotID_UTID} </TableCell>
                <TableCell> {buildProd} </TableCell>
                <TableCell> {config == null ? "NA" : config} </TableCell>
                <TableCell style={id==='baseline' ? {backgroundColor: '#1cc61c'} : {backgroundColor: '#00b8ff'}}> {toolStart} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input error={MRPUnique in postResultErrors} style={{width: '140px'}} id={slotID_UTID + "_mrpDate"} type='text' name={MRPName} value={MRPDate} onChange={onChange} required /> : MRPDate } 
                    { MRPUnique in postResultErrors && <FormHelperText className={classes.errorText}>{postResultErrors[MRPUnique]}</FormHelperText>}
                </TableCell>
                <TableCell style={fabName ? {backgroundColor: 'yellow'} : null}> {MFGCommit} </TableCell>
                <TableCell> {intReadDate} </TableCell>
                <TableCell> {endDate} </TableCell>
                <TableCell style={gapDays >= 0 ? null : {backgroundColor: 'red'}}> {gapDays} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input error={cycleTimeUnique in postResultErrors} id={slotID_UTID + "_cycleTime"} type='text' name={cycleName} value={cycleTime} onChange={onChange} required /> : cycleTime }
                    { cycleTimeUnique in postResultErrors && <FormHelperText className={classes.errorText}>{postResultErrors[cycleTimeUnique]}</FormHelperText>}
                </TableCell>
                <TableCell align='center'> {id === 'predicted' ? <Checkbox name={lockMRPName} id={slotID_UTID + "_lockMRPDate"} checked={lockMRPCheck} onChange={onChange} color="primary" /> : null} </TableCell>
                <TableCell> 
                    { id === 'predicted' ? <Input error={storageUnique in postResultErrors} id={slotID_UTID + "_storageDate"} type='text' name={storageName} value={storageDate} onChange={onChange} /> : null }
                    { storageUnique in postResultErrors && <FormHelperText className={classes.errorText}>{postResultErrors[storageUnique]}</FormHelperText>}                     
                </TableCell>
                { result.map((obj, index) => (typeof(obj) === "object") ?
                    null : <TableCell key={index}> {obj} </TableCell>
                )}
            </TableRow>
        </Fragment>
    )
}, (prevProps, nextProps) => prevProps.result[0].cycleTimeDays === nextProps.result[0].cycleTimeDays && prevProps.result[0].MRPDate === nextProps.result[0].MRPDate && prevProps.result[0].sendToStorageDate === nextProps.result[0].sendToStorageDate && prevProps.result[0].lockMRPDate === nextProps.result[0].lockMRPDate)

export default PostresultItem
