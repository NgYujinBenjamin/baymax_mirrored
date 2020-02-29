import React, { Fragment, useContext } from 'react'
import { Link } from 'react-router-dom'
import { AppBar, Typography, Toolbar, List, ListItem, withStyles } from '@material-ui/core'
import TrendingUpIcon from '@material-ui/icons/TrendingUp';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import FaceIcon from '@material-ui/icons/Face';
import AuthContext from '../../context/auth/authContext'
import UploadContext from '../../context/upload/uploadContext'

const Navbar = () => {
    const authContext = useContext(AuthContext);
    const uploadContext = useContext(UploadContext);

    const { isAuthenticated, logout, currentNavItem } = authContext;
    const { clearZero } = uploadContext;
    
    const handleLogout = () => {
        clearZero();
        logout();
    }

    const authLinks = (
        <Fragment>
            <List style={{ display: 'flex', textAlign: 'center', justifyContent: 'center' }}>
                <Link to='/' style={{ textDecoration:'none' }}>
                    <ListItem
                        style={{ backgroundColor: '#ffffff' }}
                        selected={currentNavItem === -1}
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
                        selected={currentNavItem === 0}
                        onClick={() => clearZero()}
                    >
                        <TrendingUpIcon style={{ marginRight: '4px' }}/>
                        Schedule
                    </StyledListItem>
                </Link>
                <Link to='/profile' style={{ color:'#000000', textDecoration:'none', marginRight: '2px' }}>
                    <StyledListItem
                        button
                        selected={currentNavItem === 1}
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
                        selected={currentNavItem === 0}
                    >
                        Login
                    </StyledListItem>
                </Link>
                <Link to='/register' style={{ color:'#000000', textDecoration:'none' }}>
                    <StyledListItem
                        button
                        selected={currentNavItem === 1}
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
