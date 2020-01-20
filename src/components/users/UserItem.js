import React, { Fragment, useContext } from 'react'
import { Card, CardContent, CardActions, Typography, Button, Box } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import RefreshIcon from '@material-ui/icons/Refresh';
import WorkIcon from '@material-ui/icons/Work';
import PersonIcon from '@material-ui/icons/Person';
import AdminContext from '../../context/admin/adminContext';

const UserItem = ({ user }) => {
    const adminContext = useContext(AdminContext);

    const { deleteUser, resetPassword } = adminContext
    const { id, username, firstname, lastname, department, role } = user

    const handleDelete = () => {
        deleteUser(id);
    }

    const handleReset = () => {
        resetPassword(id);
    }

    return (
        <Fragment>
            <Card style={{ marginBottom: '12px' }}>
                <CardContent>
                    <Typography variant='h5' component='h3' gutterBottom>
                        {firstname}{' '}{lastname}
                        <Box component='span' style={{float: 'right'}}>
                            <Button 
                                variant='contained' 
                                size='small'
                                disabled
                                style={{ 
                                    backgroundColor: (role === 'admin' ? 'orange' : 'blue'),
                                    color: 'white',
                                    textTransform: 'none',
                                    fontWeight: 'bold'
                                }}
                            >
                                {role.charAt(0).toUpperCase() + role.slice(1)}
                            </Button>
                        </Box>
                    </Typography>
                    <Typography variant='body2' component='p' style={{ display: 'flex', alignItems: 'center'}}>
                        <PersonIcon style={{ marginRight: '6px', marginBottom: '6px'}} />
                        {username}
                    </Typography>
                    <Typography variant='body2' component='p' style={{ display: 'flex', alignItems: 'center'}}>
                        <WorkIcon style={{ marginRight: '6px'}} />
                        {department}
                    </Typography>
                </CardContent>
                <CardActions>
                    <Button 
                        startIcon={<DeleteIcon />} 
                        variant='contained' 
                        color='secondary' 
                        style={{ margin: '4px' }}
                        onClick={handleDelete}
                    >
                        Delete
                    </Button>
                    <Button 
                        startIcon={<RefreshIcon />} 
                        variant='contained' 
                        style={{ margin: '4px'}}
                        onClick={handleReset}
                    >
                        Reset Password
                    </Button>
                </CardActions>
            </Card>
        </Fragment>
    )
}

export default UserItem
