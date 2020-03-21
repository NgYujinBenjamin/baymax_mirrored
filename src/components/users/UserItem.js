import React, { Fragment, useContext } from 'react'
import { Card, CardContent, CardActions, Typography, Button, Box } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import RefreshIcon from '@material-ui/icons/Refresh';
import WorkIcon from '@material-ui/icons/Work';
import PersonIcon from '@material-ui/icons/Person';
import SupervisorAccountIcon from '@material-ui/icons/SupervisorAccount';
import AdminContext from '../../context/admin/adminContext';

const UserItem = ({ user }) => {
    const adminContext = useContext(AdminContext);

    const { deleteUser, resetPassword, convertAdmin } = adminContext
    const { staff_id, username, firstname, lastname, department, role } = user

    const handleDelete = () => {
        deleteUser(staff_id);
    }

    const handleReset = () => {
        resetPassword(staff_id);
    }

    const handleAdmin = () => {
        convertAdmin(staff_id);
    }

    return (
        <Fragment>
            <Card style={{ marginBottom: '12px' }}>
                <CardContent>
                    <Typography variant='h5' component='h3' gutterBottom>
                        {firstname.charAt(0).toUpperCase() + firstname.slice(1)}{' '}{lastname.charAt(0).toUpperCase() + lastname.slice(1)}
                        <Box component='span' style={{float: 'right'}}>
                            <Button 
                                variant='contained' 
                                size='small'
                                disabled
                                style={{ 
                                    backgroundColor: (role === 'admin' ? 'orange' : 'blue'),
                                    color: 'white',
                                    textTransform: 'none',
                                }}
                            >
                                {role.charAt(0).toUpperCase() + role.slice(1)}
                            </Button>
                        </Box>
                    </Typography>
                    <Typography variant='body2' component='p' style={{ display: 'flex', alignItems: 'center' }}>
                        <PersonIcon style={{ marginRight: '6px', marginBottom: '6px' }} />
                        {username}
                    </Typography>
                    <Typography variant='body2' component='p' style={{ display: 'flex', alignItems: 'center' }}>
                        <WorkIcon style={{ marginRight: '6px' }} />
                        {department}
                    </Typography>
                </CardContent>
                <CardActions>
                    {role !== 'admin' && 
                    <Fragment>
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
                        <Button 
                            startIcon={<SupervisorAccountIcon />} 
                            variant='contained' 
                            style={{ margin: '4px'}}
                            onClick={handleAdmin}
                        >
                            Admin Convert
                        </Button>
                    </Fragment>}
                </CardActions>
            </Card>
        </Fragment>
    )
}

export default UserItem
