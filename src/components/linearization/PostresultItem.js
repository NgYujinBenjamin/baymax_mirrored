import React, { Fragment } from 'react'
import { TableCell, TableRow } from '@material-ui/core'

const PostresultItem = ({ result }) => {
    const argoID = result['Argo ID'];
    const utID = result['Slot ID/UTID'];
    const productName = result['Build Product'];
    const toolCompletion = result['Internal Ops Ship Readiness Date'];
    const delta = parseInt(result['Delta'])
    let checker = Math.sign(delta)

    return (
        checker !== -1 && (
            <Fragment>
                <TableRow>
                    <TableCell>{argoID}</TableCell>
                    <TableCell>{utID}</TableCell>
                    <TableCell>{productName}</TableCell>
                    <TableCell>{toolCompletion}</TableCell>
                    <TableCell>{delta}</TableCell>
                </TableRow>
            </Fragment>
        )
    )
}

export default PostresultItem
