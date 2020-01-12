import React, { Fragment } from 'react'
import { Card, CardContent, CardActions, Typography, Badge, Button, Box } from '@material-ui/core';
import DeleteIcon from '@material-ui/icons/Delete';
import RefreshIcon from '@material-ui/icons/Refresh';
import WorkIcon from '@material-ui/icons/Work';
import PersonIcon from '@material-ui/icons/Person';

const UserItem = ({ user }) => {
    const { username, firstname, lastname, department, role } = user

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
                    <Button startIcon={<DeleteIcon />} variant='contained' color='secondary' style={{ margin: '4px' }}>
                        Delete
                    </Button>
                    <Button startIcon={<RefreshIcon />} variant='contained' style={{ margin: '4px'}}>
                        Reset Password
                    </Button>
                </CardActions>
            </Card>
        </Fragment>
    )
}

export default UserItem
