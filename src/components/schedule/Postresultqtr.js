import React, { Fragment, useState } from 'react'
import { TableContainer, Paper, Typography, Box, Table, Button } from '@material-ui/core'
import Postresultheader from './Postresultheader';
import Postresultbody from './Postresultbody';
import PropTypes from 'prop-types';

const Postresultqtr = ({ schedule, value, num, quarter }) => {
    // console.log(schedule);
    
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

    // return (
    //     <Fragment>
    //         <TabPanel value={value} index={num}>
    //             { Object.keys(schedule).map((quarterName, index) => (quarterName === quarter) ?
    //                 <Postresultbody sendData={sendData} result={schedule[quarterName].slice(1)} id='predicted' key={index} quarter={quarterName}/> : null
    //             )}
    //         </TabPanel>
    //     </Fragment>
    // )

    // const handletest = () => {
        
    // }

    // const [arr, setarr] = useState([])
    // const givemedata = (res) => {
    //     setarr(res)
    // }

    return (
        <Fragment>
            <TabPanel value={value} index={num}>
                <TableContainer component={Paper}>
                    <Table style={{tableLayout: "auto"}}>
                        {/* TableHeader */}
                        { Object.keys(schedule).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultheader result={schedule[quarterName][0]} key={index} /> : null
                        )}
                        {/* TableRow */}
                        {/* { Object.keys(schedule.baseline).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultbody result={schedule.baseline[quarterName]} id='baseline' key={index}/> : null
                        )} */}
                        { Object.keys(schedule).map((quarterName, index) => (quarterName === quarter) ?
                            <Postresultbody result={schedule[quarterName].slice(1)} id='predicted' key={index} quarter={quarterName}/> : null
                        )}
                    </Table>
                </TableContainer>
                {/* <Button variant='contained' onClick={handletest} color='primary'>test</Button> */}
            </TabPanel>
        </Fragment>
    )
}

export default Postresultqtr
