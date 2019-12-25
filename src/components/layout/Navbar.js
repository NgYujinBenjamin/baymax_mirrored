import React, { Fragment } from 'react'
import { AppBar, Typography, Button, Toolbar } from '@material-ui/core'

const Navbar = () => {
    return (
        <Fragment>
            <AppBar style={styles.menu} position="sticky">
                <Toolbar>
                    <Typography variant="h6" color="inherit" style={styles.title}>Baymax</Typography>
                    <Button color='inherit'>Login</Button>
                </Toolbar>
            </AppBar>
        </Fragment>
    )
}

const styles = {
    menu: { backgroundColor: '#E8C73F', marginBottom: 24},
    title: { flexGrow: 1 }
}

export default Navbar
