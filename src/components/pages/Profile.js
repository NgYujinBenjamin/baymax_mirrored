import React, { Fragment, useState, useContext, useEffect } from 'react';
import { Grid, TextField, DialogContent, DialogContentText, DialogActions, DialogTitle, Dialog , Avatar, Typography, Button, FormHelperText } from '@material-ui/core';
import AuthContext from '../../context/auth/authContext';
import { Link } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';

const Profile = () => {
    const classes = useStyles();
    const authContext = useContext(AuthContext);

    const { user, updatePwd, loadUser, updateNavItem } = authContext;
    const [error, setError] = useState(false);

    useEffect(() => {
        loadUser();
        updateNavItem(1);
        //eslint-disable-next-line
    }, [error])

    // form
    const [form, setForm] = useState({
        oldpwd: '',
        newpwd: '',
        reenterpwd: ''
    })

    const { oldpwd, newpwd, reenterpwd} = form;

    const handleChange = (event) => {
        setForm({
            ...form,
            [event.target.name]: event.target.value
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log(newpwd, reenterpwd);
        if(newpwd !== reenterpwd){
            //alert prompt
            setError(true);
        } else {
            // updatePwd({ newpwd });
            handleClose();
        }
    }

    // This is for dialogue box to popup
    const [open, setOpen] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    }; 

    const handleClose = () => {
        setForm({
            oldpwd: '',
            newpwd: '',
            reenterpwd: ''
        })
        setError(false);
        setOpen(false);
    };

    return (
        <Fragment>
            <Grid container spacing={3} style={{ textAlign: 'center' }}>
                <Grid item xs > 
                    <Typography variant='h5' component='h3' gutterBottom>
                        <Avatar src=" " className={classes.large} style={{ margin: '0 auto', marginBottom: '10px' }}/>
                        {/* Got to change to first name and last name once backend has been set up */}
                        { user && user.username.toUpperCase() + ' ' + user.lastname.toUpperCase() } 
                    </Typography>

                    <Typography gutterBottom>
                        <Button variant="outlined" color="primary" onClick={handleClickOpen} style={{ width: '25%' }}>
                            Update Password
                        </Button>
                    </Typography>
                    
                    {/* Check if user is admin; if so, show the button */}
                    {user !== null && user.role === "admin" && <Typography gutterBottom> 
                        <Link to='/admin' style={{ color: 'white', textDecoration:'none' }}>
                            <Button variant="contained" color="primary" style={{ width: '25%' }}> View / Add Users </Button>
                        </Link>
                    </Typography>}
                    
                    <Typography gutterBottom>
                        <Link to='/history' style={{ color: 'white', textDecoration:'none' }}>
                            <Button variant="contained" color="primary" style={{ width: '25%' }}> View History </Button>
                        </Link>
                    </Typography>
                    
                    <Dialog
                        open={open}
                        onClose={handleClose}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
                        fullWidth={true}
                    >
                        <DialogTitle id="alert-dialog-title">{"Update Password"}</DialogTitle>
                        <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            <form onSubmit={handleSubmit}>
                                <TextField 
                                    variant='outlined' 
                                    margin='normal' 
                                    type='password'
                                    fullWidth 
                                    required 
                                    id='oldpwd' 
                                    name='oldpwd' 
                                    label='Old Password'
                                    value={oldpwd}
                                    onChange={handleChange}
                                />

                                <TextField 
                                    error = {error}
                                    variant='outlined' 
                                    margin='normal' 
                                    type='password'
                                    fullWidth 
                                    required 
                                    id='newpwd' 
                                    name='newpwd' 
                                    label='New Password'
                                    value={newpwd}
                                    onChange={handleChange}
                                />
                                { error && <FormHelperText className={classes.errorText}>New password do not match! Please re-enter</FormHelperText>}

                                <TextField 
                                    error = {error}
                                    variant='outlined' 
                                    margin='normal' 
                                    type='password'
                                    fullWidth 
                                    required 
                                    id='reenterpwd' 
                                    name='reenterpwd' 
                                    label='Re-enter New Password'
                                    value={reenterpwd}
                                    onChange={handleChange}
                                />
                                { error && <FormHelperText className={classes.errorText}>New password do not match! Please re-enter</FormHelperText>}

                                <DialogActions>
                                    <Button type='submit' color="primary">
                                        Submit
                                    </Button>
                                    <Button onClick={handleClose} color="primary">
                                        Cancel
                                    </Button>
                                </DialogActions>
                            </form>
                        </DialogContentText>
                        </DialogContent>
                        
                    </Dialog>
                </Grid>
            </Grid>

        </Fragment>
    )
}
const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
        '& > *': {
            margin: theme.spacing(1),
        },
    },
    large: {
        width: theme.spacing(20),
        height: theme.spacing(20),
    },
    errorText: {
        color: 'red',
        fontSize: '12px'
    },
}));

export default Profile
