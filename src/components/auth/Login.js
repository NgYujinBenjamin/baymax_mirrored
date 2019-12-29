import React, { useState, useContext, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Avatar, Button, Typography, TextField, Container } from '@material-ui/core'
import LockOutlinedIcon from '@material-ui/icons/LockOutlined'
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'

const Login = (props) => {
    const classes = useStyles();
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);

    const { setAlert } = alertContext;
    const { login, isAuthenticated, clearErrors, error } = authContext;

    useEffect(() => {
        if(isAuthenticated){
            props.history.push('/')
        }

        if(error === 'Invalid credentials'){
            setAlert(error);
            clearErrors();
        }
        //eslint-disable-next-line
    }, [isAuthenticated, props.history, error])

    const [user, setUser] = useState({
       username: '',
       password: '' 
    });

    const { username, password } = user;

    const handleChange = (event) => {
        setUser({
            ...user,
            [event.target.name]: event.target.value
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if(username === '' || password === ''){
            setAlert('Please fill in all fields');
        } else {
            login({
                username: username,
                password: password
            })
        }
    }

    return (
        <Container maxWidth='xs'>
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component='h1' variant='h5'>
                    Sign In
                </Typography>
                <form className={classes.form} onSubmit={handleSubmit}>
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
                        onChange={handleChange}
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
                        onChange={handleChange}
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
