import React, { Fragment, useContext, useEffect } from 'react';
import { Typography, Card, CardContent, CardActions, Button } from '@material-ui/core';
import { Link } from 'react-router-dom';
import AuthContext from '../../context/auth/authContext';

const HistoryItem = ({ record }) => {
    const authContext = useContext(AuthContext);

    const { loadUser, updateNavItem, user } = authContext;
    
    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [])

    const { histID, dateGenerated } = record;

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
                    <Link to={'/historyDetails/' + user.staff_id + '/' + histID} style={{ textDecoration:'none' }}>
                        <Button size="small" color="primary"> View Full Data </Button>
                    </Link>
                </CardActions>

            </Card>
        </Fragment>
    )
}

export default HistoryItem