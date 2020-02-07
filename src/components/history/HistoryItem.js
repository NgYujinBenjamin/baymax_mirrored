import React, { Fragment } from 'react';
import { Typography, Card, CardContent, CardActions, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';

const HistoryItem = ({ record }) => {
    const { msuID, dateGenerated } = record;

    // const handleClick = () => {
    //     console.log(msuID);
    // }

    return (
        <Fragment>
            <Card style={{minWidth: 275, marginBottom: 15}} variant="outlined">
            
                <CardContent>
                    <Typography style={{fontSize: 14}} color="textSecondary" gutterBottom>
                        Schedule created on
                    </Typography>
                    <Typography variant="h5" component="h2">
                        { dateGenerated }
                    </Typography>
                </CardContent>

                <CardActions>
                    <Button size="small" color="primary">
                        <Link to={'/history/' + msuID} style={{ textDecoration:'none' }}> View Full Data </Link>
                    </Button>
                </CardActions>

            </Card>
        </Fragment>
    )
}

export default HistoryItem