import React, { useState, useContext, useEffect } from 'react'
import { makeStyles } from '@material-ui/core/styles'
import { Avatar, Button, Typography, TextField, Container, Card } from '@material-ui/core'
import PersonAddIcon from '@material-ui/icons/PersonAdd';
import AuthContext from '../../context/auth/authContext'
import AlertContext from '../../context/alert/alertContext'

const Register = (props) => {
    const classes = useStyles();
    const alertContext = useContext(AlertContext);
    const authContext = useContext(AuthContext);

    const { setAlert } = alertContext;
    const { register ,isAuthenticated, clearErrors, error } = authContext;

    useEffect(() => {
        if(isAuthenticated){
            props.history.push('/');
        }

        if(error !== null){
            setAlert(error);
            clearErrors();
        }
        //eslint-disable-next-line
    }, [error, isAuthenticated, props.history])

    const [form, setForm] = useState({
        username: '',
        password: '',
        password2: '',
        firstname: '',
        lastname: '',
        department: '',
        role: 'user'
    })

    const { username, password, password2, firstname, lastname, department, role } = form;
 
    const handleChange = (event) => {
        setForm({
            ...form,
            [event.target.name]: event.target.value
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if(password !== password2){
            setAlert('Passwords do not match');
        } else {
            register({
                username,
                password,
                firstname,
                lastname,
                department,
                role
            })
        }
    }

    return (
        <Container maxWidth='sm'>
            <Card style={{padding: '12px 28px'}}>
                <Avatar className={classes.avatar} style={{ margin: '8px auto' }}>
                    <PersonAddIcon style={{ margin: '0 auto' }} />
                </Avatar>
                <Typography variant='h5' align='center' style={{ fontWeight: 'bold' }}>
                    Register
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
                    <TextField 
                        variant='outlined' 
                        margin='normal'
                        type='password'
                        label='Confirm Password'
                        fullWidth
                        required
                        id='password2'
                        name='password2'
                        onChange={handleChange}
                    />
                    <TextField 
                        variant='outlined' 
                        margin='normal' 
                        type='text'
                        fullWidth 
                        required 
                        id='firstname' 
                        name='firstname' 
                        label='First Name'
                        onChange={handleChange}
                    />
                    <TextField 
                        variant='outlined' 
                        margin='normal' 
                        type='text'
                        fullWidth 
                        required 
                        id='lastname' 
                        name='lastname' 
                        label='Last Name'
                        onChange={handleChange}
                    />
                    <TextField 
                        variant='outlined' 
                        margin='normal' 
                        type='text'
                        fullWidth 
                        required 
                        id='department' 
                        name='department' 
                        label='Department'
                        onChange={handleChange}
                    />
                    <Button
                        type='submit'
                        fullWidth
                        variant='contained'
                        color='primary'
                        className={classes.submit}
                    >
                        Register
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

export default Register
