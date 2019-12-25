import React, { Fragment, useContext, useState } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button } from '@material-ui/core'
import PreresultItem from './PreresultItem'
import UploadContext from '../../context/upload/uploadContext'

const Preresult = () => {
    const uploadContext = useContext(UploadContext);
    const classes = useStyles();
    const { loading, linearize } = uploadContext;

    const [objs, setObjects] = useState(linearize);

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            console.log(event.target);
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    if (name === 'Cycle Time Days'){
                        return {...obj, 'Cycle Time Days': value }
                    }
                    if (name === 'MRP Date'){
                        return {...obj, 'MRP Date': value}
                    }
                }
                return o;
            })))
        }
    }

    const handleLinearize = (event) => {
        console.log(linearize);
        event.preventDefault();
        uploadContext.updateLinearize(objs);
    }

    const checkme = () => {
        console.log(linearize);
    }
    
    return (
        <Fragment>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Argo ID</TableCell>
                            <TableCell>UTID</TableCell>
                            <TableCell>Product Name</TableCell>
                            <TableCell>Cycle Time Days</TableCell>
                            <TableCell>MRP Date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {objs.map((obj, index) => (
                            <PreresultItem obj={obj} onChange={handleChange(obj)} key={index} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Button className={classes.marginTop} fullWidth color='primary' variant='contained' onClick={handleLinearize}>Begin Linearization</Button>
            <Button onClick={checkme}>Check</Button>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    marginTop: { marginTop: 8 }
}));

export default Preresult
