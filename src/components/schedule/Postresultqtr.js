import React, { Fragment, useContext } from 'react'
import { TableContainer, Paper, Typography, Box, Table, Button } from '@material-ui/core'
import Postresultheader from './Postresultheader';
import Postresultbody from './Postresultbody';
import PropTypes from 'prop-types';
import UploadContext from '../../context/upload/uploadContext'

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
                    <Table style={{tableLayout: "auto"}}>
                        {/* TableHeader */}
                        <Postresultheader result={schedule[quarter][0]}/>
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
