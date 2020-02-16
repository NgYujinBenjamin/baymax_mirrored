import React, { Fragment, useState } from 'react'
import { TableBody } from '@material-ui/core'
import PostresultItem from './PostresultItem'

const Postresultbody = ({ result, id}) => {
    const [ objs, setObjects ] = useState(result);
    // console.log(objs);

    const handleChange = (obj) => {
        return (event) => {
            const value = event.target.value;
            const name = event.target.name;
            // console.log(event.target);
            // To update nested object; does not work since "Cycle Time Days" is an attribute inside element 1
            setObjects(prevObjs => (prevObjs.map((o) => {
                if (o === obj) {
                    return {...obj, [name]: value}
                }
                return o;
            })))
        }
    }

    return (
        <Fragment>
            <TableBody style={{whiteSpace: "nowrap"}}>
                { objs.map((obj, index) =>
                    <PostresultItem result={obj} id={id} onChange={handleChange(obj)} key={index} />
                )}
            </TableBody>
        </Fragment>
    )
}

export default Postresultbody
