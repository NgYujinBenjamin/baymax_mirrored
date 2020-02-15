import React, { Fragment } from 'react'
import { TableContainer, Paper, Typography, Box, Table } from '@material-ui/core'
import Postresultheader from './Postresultheader';
import Postresultbody from './Postresultbody';
import PropTypes from 'prop-types';

const Postresultqtr = ({ schedule, value, num, quarter}) => {
    // console.log(result[0]);
    
    const TabPanel = (props) => {
        const { children, value, index, ...other } = props;
      
        return (
            <Typography
                component="div"
                role="tabpanel"
                hidden={value !== index}
                id={`scrollable-force-tabpanel-${index}`}
                aria-labelledby={`scrollable-force-tab-${index}`}
                {...other}
            >
                {value === index && <Box p={3}>{children}</Box>}
            </Typography>
        );
    }

    TabPanel.propTypes = {
        children: PropTypes.node,
        index: PropTypes.any.isRequired,
        value: PropTypes.any.isRequired,
    };

    return (
        <Fragment>
            <TabPanel value={value} index={num}>
                <TableContainer component={Paper}>
                    <Table style={{tableLayout: "auto"}}>
                        {/* TableHeader; Dates from baseline and predicted are the same hence no diff if we use baseline / predicted */}
                        { Object.keys(schedule.baseline).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultheader result={schedule.baseline[quarterName][0]} key={index} /> : null
                        )}
                        {/* TableRow */}
                        { Object.keys(schedule.baseline).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultbody result={schedule.baseline[quarterName]} id='baseline' key={index}/> : null
                        )}
                        { Object.keys(schedule.predicted).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultbody result={schedule.predicted[quarterName]} id='predicted' key={index}/> : null
                        )}
                    </Table>
                </TableContainer>
            </TabPanel>
        </Fragment>
    )
}

export default Postresultqtr
