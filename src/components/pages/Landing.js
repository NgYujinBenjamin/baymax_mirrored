import React, { useContext, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Grid, Typography, Card, CardActions, CardActionArea, CardContent, Container, Divider } from '@material-ui/core';
import AuthContext from '../../context/auth/authContext';
import Spinner from '../layout/Spinner';
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import HistoryIcon from '@material-ui/icons/History';
import GroupIcon from '@material-ui/icons/Group';

const Landing = () => {
    const authContext = useContext(AuthContext);

    const { loadUser, user, loading, updateNavItem } = authContext;

    useEffect(() => {
        loadUser();
        //eslint-disable-next-line
    }, [])

    const handleNavItem = (event, index) => {
        updateNavItem(index);
    }

    return (
        user !== null && !loading ? (
            <Container maxWidth='lg'>
                <Typography variant='h2' style={{ fontSize: '3rem', fontWeight: 'bold' }}>
                    Hello, {user.firstname.charAt(0).toUpperCase() + user.firstname.slice(1)}{' '}{user.lastname.charAt(0).toUpperCase() + user.lastname.slice(1)}! 
                </Typography>
                <Typography variant='h2' style={{ fontSize: '2rem', marginBottom: '16px' }}>
                    Select Option
                </Typography>
                <Grid container spacing={2}>
                    <Grid item xs>
                        <Link to='/baseline' style={{ textDecoration:'none' }}>
                        <Card style={{ borderRadius: '8px' }}>
                            <CardActionArea onClick={event => handleNavItem(event, 0)}>
                                <CardActions disableSpacing style={{ padding: '70px' }}>
                                    <TrendingUpIcon style={{ margin: '0 auto', fontSize: '200px', color: '#4d4dff' }}/>
                                </CardActions>
                                <Divider />
                                <CardContent style={{ backgroundColor: '#4d4dff', color: '#ffffff', padding: '20px' }}>
                                    <Typography gutterBottom variant='h5' component='h2' align='center' style={{ fontWeight: 'bold' }}>
                                        Schedule Creation
                                    </Typography>
                                    <Typography variant='body2' component='p' align='center'>
                                        Begin schedule creation here!
                                    </Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                        </Link>
                    </Grid>
                    <Grid item xs>
                        <Link to='/history' style={{ textDecoration:'none' }}>
                        <Card style={{ borderRadius: '8px' }}>
                            <CardActionArea onClick={event => handleNavItem(event, 1)}>
                                <CardActions disableSpacing style={{ padding: '70px' }}>
                                    <HistoryIcon style={{ margin: '0 auto', fontSize: '200px', color: '#00b273' }}/>
                                </CardActions>
                                <Divider />
                                <CardContent style={{ backgroundColor: '#00b273', color: '#ffffff', padding: '20px' }}>
                                    <Typography gutterBottom variant='h5' component='h2' align='center' style={{ fontWeight: 'bold' }}>
                                        History
                                    </Typography>
                                    <Typography variant='body2' component='p' align='center'>
                                        View your recent histories!
                                    </Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                        </Link>
                    </Grid>
                    {user.role === 'admin' && (
                        <Grid item xs>
                            <Link to='/admin' style={{ textDecoration:'none' }}>
                            <Card style={{ borderRadius: '8px' }}>
                                <CardActionArea onClick={event => handleNavItem(event, 1)}>
                                    <CardActions disableSpacing style={{ padding: '70px' }}>
                                        <GroupIcon style={{ margin: '0 auto', fontSize: '200px', color: '#ffa500' }}/>
                                    </CardActions>
                                    <Divider />
                                    <CardContent style={{ backgroundColor: '#ffa500', color: '#ffffff', padding: '20px' }}>
                                        <Typography gutterBottom variant='h5' component='h2' align='center' style={{ fontWeight: 'bold' }}>
                                            Users
                                        </Typography>
                                        <Typography variant='body2' component='p' align='center'>
                                            Manage your users in the application!
                                        </Typography>
                                    </CardContent>
                                </CardActionArea>
                            </Card>
                            </Link>
                        </Grid>
                    )}
                </Grid>
            </Container>
        ) : (
            <Spinner />
        )
    )
}

export default Landing
