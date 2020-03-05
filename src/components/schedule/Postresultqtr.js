import React, { Fragment, useContext } from 'react';
import { TableContainer, Paper, Typography, Box, Table, TableHead, TableRow, TableCell } from '@material-ui/core';
import Postresultbody from './Postresultbody';
import PropTypes from 'prop-types';

const Postresultqtr = ({ schedule, baseline, value, num, quarter }) => {
    
    const qtrs = new Array();
    Object.keys(schedule).map(quarterName => 
        qtrs.push(quarterName)
    )

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
                <TableContainer component={Paper} style={{maxHeight:440}}>
                    <Table stickyHeader style={{tableLayout: "auto"}}>
                        <TableHead>
                            <TableRow style={{whiteSpace: "nowrap"}}>
                                <TableCell>Slot/UTID</TableCell>
                                <TableCell>Built Product</TableCell>
                                <TableCell>Configuration</TableCell>
                                <TableCell>Tool Start</TableCell>
                                <TableCell>MRP Date</TableCell>
                                <TableCell>MFG Commit Date</TableCell>
                                <TableCell>Int. Readiness Date</TableCell>
                                <TableCell>End Date</TableCell>
                                <TableCell>Gap</TableCell>
                                <TableCell>Cycle Time Days</TableCell>
                                <TableCell>Lock MRP Date?</TableCell>
                                <TableCell>Move to Storage Date</TableCell>
                                { schedule[quarter][0].map((date, index) =>
                                    <TableCell key={index}>{date}</TableCell>
                                )}
                            </TableRow>
                        </TableHead>
                        {/* TableRow */}
                        { Object.keys(schedule).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultbody result={schedule[quarterName].slice(1)} baseline={qtrs[0]===quarterName ? baseline[quarterName].slice(1) : null } key={index} quarter={quarterName}/> : null
                        )}
                    </Table>
                </TableContainer>
            </TabPanel>
        </Fragment>
    )
}

export default Postresultqtr
