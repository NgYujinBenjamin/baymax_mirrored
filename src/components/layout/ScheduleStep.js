import React, { Fragment, useContext, useState } from 'react'
import { Stepper, StepLabel, Step } from '@material-ui/core'
import UploadContext from '../../context/upload/uploadContext'

const ScheduleStep = () => {

    const uploadContext = useContext(UploadContext);

    const { steps, stepcount } = uploadContext;

    return (
        <Fragment>
            <Stepper activeStep={stepcount} alternativeLabel style={{paddingBottom: 40, paddingTop: 45}}>
                {steps.map(label => (
                    <Step key={label}>
                    <StepLabel>{label}</StepLabel>
                    </Step>
                ))}
            </Stepper>
        </Fragment>
    )
}

export default ScheduleStep
