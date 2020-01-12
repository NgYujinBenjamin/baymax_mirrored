import React, { Fragment, useState, useContext } from 'react'
import { Card, CardContent, TextField, Button, Typography, Box } from '@material-ui/core'
import AdminContext from '../../context/admin/adminContext'

const UserForm = () => {
    const adminContext = useContext(AdminContext);

    const [form, setForm] = useState({
        username: '',
        firstname: '',
        lastname: '',
        department: '',
        role: 'user'
    })

    const { username, firstname, lastname, department, role } = form;
    const { addUser } = adminContext;
 
    const handleChange = (event) => {
        setForm({
            ...form,
            [event.target.name]: event.target.value
        })
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        addUser(form);
    }

    return (
        <Fragment>
            <Card>
                <CardContent>
                    <Typography component='h1' variant='h5'>Add User</Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField 
                            variant='outlined' 
                            margin='normal' 
                            type='text'
                            fullWidth 
                            required 
                            id='username' 
                            name='username' 
                            label='Username'
                            value={username}
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
                            value={firstname}
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
                            value={lastname}
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
                            value={department}
                            onChange={handleChange}
                        />
                        <Box style={{ marginTop: '8px' }}>
                            <Typography component='span' variant='body1'>
                                Role: {'  '}
                            </Typography>
                            <input type='radio' name='role' value='user' checked={ role === 'user' } onChange={handleChange} /> {' '}
                            <Typography component='span' variant='body1' style={{ marginRight: '6px' }}>User</Typography>
                            <input type='radio' name='role' value='admin' checked={ role === 'admin' } onChange={handleChange} /> {' '}
                            <Typography component='span' variant='body1' style={{ marginRight: '6px' }}>Admin</Typography>
                        </Box>
                        <Button type='submit' style={{ marginTop: '12px' }} fullWidth variant='contained' color='primary'>Submit</Button>
                    </form>
                </CardContent>
            </Card>
        </Fragment>
    )
}

export default UserForm
