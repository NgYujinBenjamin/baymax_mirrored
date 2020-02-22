import React, { useState, useContext, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Avatar, Button, Typography, TextField, Container, Card } from '@material-ui/core'
import VerifiedUserIcon from '@material-ui/icons/VerifiedUser';
import AlertContext from '../../context/alert/alertContext'
import AuthContext from '../../context/auth/authContext'

const Login = (props) => {
    const classes = useStyles();
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);

    const { setAlert } = alertContext;
    const { login, isAuthenticated, clearErrors, error, updateNavItem } = authContext;

    useEffect(() => {
        if(isAuthenticated){
            props.history.push('/')
        }

        if(error !== null){
            setAlert(error);
            clearErrors();
        }

        updateNavItem(0)
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
        <Container maxWidth='sm'>
            <Card style={{padding: '12px 28px'}}>
                <Avatar className={classes.avatar} style={{ margin: '8px auto' }}>
                    <VerifiedUserIcon style={{ margin: '0 auto' }} />
                </Avatar>
                <Typography variant='h5' align='center' style={{ fontWeight: 'bold' }}>
                    Login
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
            </Card>
        </Container>
    )
}

const useStyles = makeStyles(theme => ({
    form: {
        width: '100%',
        marginTop: theme.spacing(1)
    },
    avatar: {
        backgroundColor: 'blue'
    },
    submit: {
        margin: theme.spacing(3, 0, 2)
    }
}));

export default Login
