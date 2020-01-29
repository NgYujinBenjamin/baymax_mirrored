import React, { Fragment, useContext } from 'react'
import { Link } from 'react-router-dom'
import { Avatar, AppBar, Typography, Button, Toolbar } from '@material-ui/core'
import ExitToAppRoundedIcon from '@material-ui/icons/ExitToAppRounded';
import AuthContext from '../../context/auth/authContext'
import { deepOrange } from '@material-ui/core/colors';
import { makeStyles } from '@material-ui/core/styles';

const Navbar = () => {

    const useStyles = makeStyles(theme => ({
        root: {
          display: 'flex',
          '& > *': {
            margin: theme.spacing(1),
          },
        },
        orange: {
          color: theme.palette.getContrastText(deepOrange[500]),
          backgroundColor: deepOrange[500],
        },
    }));

    const classes = useStyles();

    const authContext = useContext(AuthContext);

    const { isAuthenticated, logout, user } = authContext;

    const handleLogout = () => {
        logout();
    }

    const authLinks = (
        <Fragment>
            <Typography variant="h6" color="inherit" style={styles.title}>Baymax</Typography>
            <Link to='/profile' style={{color:'white', textDecoration:'none'}}>
                {/* <Avatar className={classes.orange}>{ user && user.username.substring(0, 1).toUpperCase() }</Avatar> */}
            </Link>
            <Button disabled style={{color:'white'}}>{ user && user.username }</Button>
            {/* <Button style={{color:'white'}}>
                <Link to='/admin' style={{color:'white', textDecoration:'none'}}>Users</Link>
            </Button> */}
            <Button style={{color:'white'}} onClick={handleLogout}>
                Logout
                <ExitToAppRoundedIcon style={{marginLeft:3}}/>
            </Button>
        </Fragment>
    )

    const guestLinks = (
        <Fragment>
            <Typography variant="h6" color="inherit" style={styles.title}>Baymax</Typography>
            <Button style={{color:'white'}}>
                <Link to='/login' style={{color:'white', textDecoration:'none'}}>Login</Link>
            </Button>
        </Fragment>
    )

    return (
        <Fragment>
            <AppBar style={styles.menu} position="sticky">
                <Toolbar>
                    { isAuthenticated ? authLinks : guestLinks }
                </Toolbar>
            </AppBar>
        </Fragment>
    )
}

const styles = {
    menu: { backgroundColor: '#E8C73F', marginBottom: 24},
    title: { flexGrow: 1 }
}

export default Navbar
