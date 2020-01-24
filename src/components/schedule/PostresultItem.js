import React, { Fragment } from 'react'
import { TableCell, TableRow } from '@material-ui/core'

const PostresultItem = ({ result }) => {
    const argoID = result.argoID;
    const utID = result.slotID_UTID;
    const productName = result.buildProduct;
    const toolCompletion = result.toolStartDate;

    return (
        <Fragment>
            <TableRow>
                <TableCell>{argoID}</TableCell>
                <TableCell>{utID}</TableCell>
                <TableCell>{productName}</TableCell>
                <TableCell>{toolCompletion}</TableCell>
            </TableRow>
        </Fragment>
    )
}

export default PostresultItem
