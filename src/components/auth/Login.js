import React, { Fragment } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Avatar, Button, Typography, TextField, Container } from '@material-ui/core'
import LockOutlinedIcon from '@material-ui/icons/LockOutlined'

const Login = () => {
    const classes = useStyles();

    return (
        <Container maxWidth='xs'>
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component='h1' variant='h5'>
                    Sign In
                </Typography>
                <form className={classes.form}>
                    <TextField 
                        variant='outlined' 
                        margin='normal' 
                        type='text'
                        fullWidth 
                        required 
                        id='username' 
                        name='username' 
                        label='Username'
                        autoFocus
                    />
                    <TextField 
                        variant='outlined' 
                        margin='normal'
                        type='password'
                        label='Password'
                        fullWidth
                        required
                        id='password'
                        name='password'
                    />
                    <Button
                        type='submit'
                        fullWidth
                        variant='contained'
                        color='primary'
                        className={classes.submit}
                    >
                        Login
                    </Button>
                </form>
            </div>
        </Container>
    )
}

const useStyles = makeStyles(theme => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
    },
    form: {
        width: '100%',
        marginTop: theme.spacing(1)
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main
    },
    submit: {
        margin: theme.spacing(3, 0, 2)
    }
}));

export default Login
