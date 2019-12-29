import React, { Fragment } from 'react'
import Baseline from '../linearization/Baseline';

const Home = (props) => {
    return (
        <Fragment>
            <Baseline history={props.history} />
        </Fragment>
    )
}

export default Home
