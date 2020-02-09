import React, { useEffect, useState, Fragment, useContext } from 'react'
import { Link } from 'react-router-dom'
import { Avatar, AppBar, Typography, Button, Toolbar, List, ListItem, withStyles } from '@material-ui/core'
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import FaceIcon from '@material-ui/icons/Face';
import AuthContext from '../../context/auth/authContext'
import UploadContext from '../../context/upload/uploadContext'
import { deepOrange } from '@material-ui/core/colors';
import { makeStyles } from '@material-ui/core/styles';

const Navbar = () => {
    const classes = useStyles();
    const authContext = useContext(AuthContext);
    const uploadContext = useContext(UploadContext);
    const { isAuthenticated, logout, user, currentNavItem, updateNavItem } = authContext;
    const { clearZero } = uploadContext;

    const [guestSelectedIndex, setGuestSelectedIndex] = useState(0);
    const [authSelectedIndex, setAuthSelectedIndex] = useState(currentNavItem);

    useEffect(() => {
        if(currentNavItem !== authSelectedIndex){
            setAuthSelectedIndex(currentNavItem);
        }
        //eslint-disable-next-line
    }, [currentNavItem])
    
    const handleLogout = () => {
        updateNavItem(-1);
        setGuestSelectedIndex(0);
        setAuthSelectedIndex(currentNavItem);
        clearZero();
        logout();
    }

    const handleGuestListItemClick = (event, index) => {
        setGuestSelectedIndex(index);
    }

    const handleAuthListItemClick = (event, index) => {
        updateNavItem(index);
    }

    // const authLinks = (
    //     <Fragment>
    //         <FaceIcon style={{ marginRight: '8px', color: '#000000'}} />
    //         <Typography variant="h5" component="h3" style={styles.title}>
    //             BAYMAX
    //         </Typography>
    //         <Button style={{color:'white'}}>
    //             <Link to='/' style={{color:'white', textDecoration:'none'}}>
    //                 Schedule
    //             </Link>
    //         </Button>
    //         <Link to='/profile' style={{color:'white', textDecoration:'none'}}>
    //             <Avatar className={classes.orange}>
    //                 { user !== null && user.username.charAt(0).toUpperCase() }
    //             </Avatar>
    //         </Link>
    //         <Button disabled style={{color:'white', paddingLeft:0}}>{ user !== null && user.username }</Button>
    //         <Button onClick={handleLogout} style={{ color: '#000000' }}>
    //             Logout
    //             <ExitToAppRoundedIcon style={{marginLeft:3}}/>
    //         </Button>
    //     </Fragment>
    // )

    // const guestLinks = (
    //     <Fragment>
    //         <FaceIcon style={{ marginRight: '6px', color: '#000000' }} />
    //         <Typography variant="h5" component="h3" style={styles.title}>
    //             BAYMAX
    //         </Typography>
    //         <Button>
    //             <Link to='/login' style={{color:'#000000', textDecoration:'none'}}>Login</Link>
    //         </Button>
    //         <Button>
    //             <Link to='/register' style={{color:'#000000', textDecoration:'none'}}>Register</Link>
    //         </Button>
    //     </Fragment>
    // )

    const authLinks = (
        <Fragment>
            <List style={{ display: 'flex', textAlign: 'center', justifyContent: 'center' }}>
                <Link to='/' style={{ textDecoration:'none' }}>
                    <ListItem
                        style={{ backgroundColor: '#ffffff' }}
                        selected={authSelectedIndex === -1}
                        onClick={event => handleAuthListItemClick(event, -1)}
                    >
                        <FaceIcon style={{ marginRight: '6px', color: '#000000' }} />
                        <Typography variant="h5" component="h3" style={styles.title}>
                            BAYMAX
                        </Typography>
                    </ListItem>
                </Link>
            </List>
            <List style={{ display: 'flex', textAlign: 'center', justifyContent: 'center', marginLeft:'auto' }}>
                <Link to='/baseline' style={{ color:'#000000', textDecoration:'none', marginRight: '2px' }}>
                    <StyledListItem
                        button
                        selected={authSelectedIndex === 0}
                        onClick={event => handleAuthListItemClick(event, 0)}
                    >
                        <TrendingUpIcon style={{ marginRight: '4px' }}/>
                        Schedule
                    </StyledListItem>
                </Link>
                <Link to='/profile' style={{ color:'#000000', textDecoration:'none', marginRight: '2px' }}>
                    <StyledListItem
                        button
                        selected={authSelectedIndex === 1}
                        onClick={event => handleAuthListItemClick(event, 1)}
                    >
                        <AccountCircleIcon style={{ marginRight: '4px' }}/>
                        Profile
                    </StyledListItem>
                </Link>
                <StyledListItem
                    button
                    onClick={handleLogout}
                    style={{ color: '#000000' }}
                >
                    <ExitToAppIcon style={{ marginRight: '4px' }}/>
                    Logout
                </StyledListItem>
            </List>
        </Fragment>
    )

    const guestLinks = (
        <Fragment>
            <FaceIcon style={{ marginRight: '6px', color: '#000000' }} />
            <Typography variant="h5" component="h3" style={styles.title}>
                BAYMAX
            </Typography>
            <List style={{ display: 'flex', textAlign: 'center', justifyContent: 'center' }}>
                <Link to='/login' style={{ color:'#000000', textDecoration:'none', marginRight: '4px' }}>
                    <StyledListItem
                        button
                        selected={guestSelectedIndex === 0}
                        onClick={event => handleGuestListItemClick(event, 0)}
                    >
                        Login
                    </StyledListItem>
                </Link>
                <Link to='/register' style={{ color:'#000000', textDecoration:'none' }}>
                    <StyledListItem
                        button
                        selected={guestSelectedIndex === 1}
                        onClick={event => handleGuestListItemClick(event, 1)}
                    >
                        Register
                    </StyledListItem>
                </Link>
            </List>
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
    menu: { backgroundColor: '#FFFFFF', marginBottom: 24},
    title: { flexGrow: 1, color:'#000000' },
}

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

const StyledListItem = withStyles({
    root: {
        borderRadius: '24px',
        padding: '8px 16px',
        fontWeight: 'bold',
        "&$selected": {
            backgroundColor: '#00cc00',
            color: '#FFFFFF'
        },
        "&$selected:hover": {
            color: '#FFFFFF',
            backgroundColor: '#00b200'
        }
    },
    selected: {}
})(ListItem);

export default Navbar
