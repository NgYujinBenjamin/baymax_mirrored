import React, { Fragment, useContext } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Box, Grid } from '@material-ui/core'
import PostresultItem from './PostresultItem'
import UploadContext from '../../context/upload/uploadContext'

const Postresult = () => {
    const classes = useStyles();
    const uploadContext = useContext(UploadContext);

    const { postResult } = uploadContext;

    return (
        <Fragment>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Argo ID</TableCell>
                            <TableCell>UTID</TableCell>
                            <TableCell>Product Name</TableCell>
                            <TableCell>Derived Tool Completion</TableCell>
                            <TableCell>Delta</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {postResult.map((result, index) => (
                            <PostresultItem result={result} key={index} />
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <Box className={classes.marginTop}>
                <Grid container spacing={1}>
                    <Grid item xs>
                        <Button fullWidth variant='contained'>Clear All</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained'>Save</Button>
                    </Grid>
                    <Grid item xs>
                        <Button fullWidth variant='contained'>Export</Button>
                    </Grid>
                </Grid>
            </Box>
        </Fragment>
    )
}

const useStyles = makeStyles(theme => ({
    marginTop: { marginTop: 12 }
}));

export default Postresult
