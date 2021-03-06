import React, { Component } from 'react';
import { connect } from 'react-redux';

import JobMonitorDialog from 'components/JobMonitorDialog'
import * as JobActions from 'actions/job'

class CurrentJobMonitorDialog extends Component {

    timer = null

    constructor(props) {
        super(props)

        this.handleTimeout = this.handleTimeout.bind(this)
        this.handleCancelButtonClick = this.handleCancelButtonClick.bind(this)
        this.handleOkButtonClick = this.handleOkButtonClick.bind(this)
        this.handleCloseButtonClick = this.handleCloseButtonClick.bind(this)

        if (props.open && props.jobMonitorConfiguration.jobId) {
            this.startTimer()
         } 
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.open && nextProps.jobMonitorConfiguration.jobId && 
                (this.props.jobMonitorConfiguration === null || 
                    nextProps.jobMonitorConfiguration.jobId !== this.props.jobMonitorConfiguration.jobId)) {
            this.startTimer()
        } else if (nextProps.job && nextProps.job.ended) {
            this.stopTimer()
        }
    }

    startTimer() {
        this.timer = setInterval(this.handleTimeout, 2000);
    }

    stopTimer() {
        clearInterval(this.timer)
    }

    handleTimeout() {
        this.loadJob()
    }

    handleOkButtonClick() {
        if (this.props.jobMonitorConfiguration.handleOkButtonClick) {
            this.props.jobMonitorConfiguration.handleOkButtonClick(this.props.job)
        }
        this.dispatchCloseJobMonitorAction()
    }
    
    handleCancelButtonClick() {
        if (this.props.jobMonitorConfiguration.handleCancelButtonClick) {
            this.props.jobMonitorConfiguration.handleCancelButtonClick(this.props.job)
        } else {
            this.props.dispatch(JobActions.cancelJob(this.props.jobMonitorConfiguration.jobId))
        }
    }

    handleCloseButtonClick() {
        this.dispatchCloseJobMonitorAction()
    }

    dispatchCloseJobMonitorAction() {
        this.props.dispatch(JobActions.closeJobMonitor())
    }

    loadJob() {
        if (this.props.jobMonitorConfiguration) {
            const jobId = this.props.jobMonitorConfiguration.jobId
            this.props.dispatch(JobActions.fetchJob(jobId))
        } else {
            this.stopTimer()
        }
    }

    render() {
        if (!this.props.open){
            return <div></div>
        }
        return (
            <JobMonitorDialog
                open={this.props.open}
                title={this.props.jobMonitorConfiguration.title}
                jobId={this.props.jobMonitorConfiguration.jobId}
                job={this.props.job}
                okButtonLabel={this.props.jobMonitorConfiguration.okButtonLabel}
                handleOkButtonClick={this.handleOkButtonClick}
                handleCancelButtonClick={this.handleCancelButtonClick}
                handleCloseButtonClick={this.handleCloseButtonClick}
                handleJobCompleted={this.props.jobMonitorConfiguration.handleJobCompleted}
            />
        )
    }
}

const mapStateToProps = state => {
    const {
        open,
        job,
        jobMonitorConfiguration,
    } = state.currentJob || {
        open: false,
        jobMonitorConfiguration: null,
        job: null
    }
    return {
        open,
        jobMonitorConfiguration,
        job
    }
}

export default connect(mapStateToProps)(CurrentJobMonitorDialog)