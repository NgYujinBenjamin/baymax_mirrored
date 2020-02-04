import React, { Fragment, useState, useContext } from 'react'
import { Grid, TextField, DialogContent, DialogContentText, DialogActions, DialogTitle, Dialog , Avatar, Typography, Button } from '@material-ui/core';
import AuthContext from '../../context/auth/authContext'
import { Link } from 'react-router-dom'
import { makeStyles } from '@material-ui/core/styles';

const Profile = () => {

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
    }));

    const classes = useStyles();

    // initialize
    const authContext = useContext(AuthContext);
    // method name inside auth state
    const { user, updatePwd } = authContext;

    // form
    const [form, setForm] = useState({
        newpwd: '',
        reenterpwd: ''
    })

    const { newpwd, reenterpwd} = form;

    const handleChange = (event) => {
        setForm({
            ...form,
            [event.target.name]: event.target.value
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        // console.log(form);
        if(newpwd !== reenterpwd){
            //alert prompt

        } else {
            updatePwd({
                newpwd
            });
            handleClose(); // auto close the dialog once password has been updated
        }
    }

    // This is for dialogue box to popup
    const [open, setOpen] = useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <Fragment>
            <Grid container spacing={3} style={{ textAlign: 'center' }}>
                <Grid item xs > 
                    <Typography variant='h5' component='h3' gutterBottom>
                        <Avatar src=" " className={classes.large} style={{ margin: '0 auto', marginBottom: '10px' }}/>
                        {/* Got to change to first name and last name once backend has been set up */}
                        { user && user.username.toUpperCase() } 
                    </Typography>

                    <Typography gutterBottom>
                        <Button variant="outlined" color="primary" onClick={handleClickOpen} style={{ width: '25%' }}>
                            Update Password
                        </Button>
                    </Typography>
                    
                    {/* Check if user is admin; if so, show the button */}
                    {user !== null && user.role === "admin" && <Typography gutterBottom> 
                        <Button variant="contained" color="primary" style={{ width: '25%' }}>
                            <Link to='/admin' style={{ color: 'white', textDecoration:'none' }}> View / Add Users </Link>
                        </Button>
                    </Typography>}
                    
                    <Typography gutterBottom>
                        <Button variant="contained" color="primary" style={{ width: '25%' }}>
                            View History
                        </Button>
                    </Typography>
                    
                    <Dialog
                        open={open}
                        onClose={handleClose}
                        aria-labelledby="alert-dialog-title"
                        aria-describedby="alert-dialog-description"
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
                                    id='newpwd' 
                                    name='newpwd' 
                                    label='New Password'
                                    value={newpwd}
                                    onChange={handleChange}
                                />

                                <TextField 
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

export default Profile
