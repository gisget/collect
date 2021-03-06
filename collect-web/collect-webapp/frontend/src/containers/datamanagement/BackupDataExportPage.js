import React, { Component } from 'react';
import { Button, Container, Form, FormGroup, Label, Input, Row, Col } from 'reactstrap';
import { connect } from 'react-redux';

import ServiceFactory from 'services/ServiceFactory'
import * as JobActions from 'actions/job';

class BackupDataExportPage extends Component {

    constructor(props) {
        super(props)

        this.state = {
            exportOnlyOwnedRecords: false,
            includeRecordFiles: true
        }

        this.handleExportButtonClick = this.handleExportButtonClick.bind(this)
        this.handleBackupDataExportModalOkButtonClick = this.handleBackupDataExportModalOkButtonClick.bind(this)
    }

    handleExportButtonClick() {
        const survey = this.props.survey
        const surveyId = survey.id
        
        const backupExportParams = {
            includeRecordFiles: this.state.includeRecordFiles,
            onlyOwnedRecords: this.state.exportOnlyOwnedRecords
        }
        ServiceFactory.recordService.startBackupDataExport(surveyId, backupExportParams).then(job => {
            this.props.dispatch(JobActions.startJobMonitor({
                jobId: job.id, 
                title: 'Exporting data',
                okButtonLabel: 'Done',                        
                handleOkButtonClick: this.handleBackupDataExportModalOkButtonClick
            }))
        })
    }

    handleCsvDataExportModalOkButtonClick(job) {
        if (job.completed) {
            const survey = this.props.survey
            const surveyId = survey.id
            ServiceFactory.recordService.downloadCSVDataExportResult(surveyId)
        }
        this.props.dispatch(JobActions.closeJobMonitor())
    }

    handleBackupDataExportModalOkButtonClick(job) {
        if (job.completed) {
            const survey = this.props.survey
            const surveyId = survey.id
            ServiceFactory.recordService.downloadBackupDataExportResult(surveyId)
        }
        this.props.dispatch(JobActions.closeJobMonitor())
    }

    handleEntityCheck(event) {
        this.setState({...this.state, selectedEntityDefinition: event.selectedNodeDefinitions.length == 1 ? event.selectedNodeDefinitions[0]: null})
    }

    render() {
        if (!this.props.survey) {
            return <div>Select survey first</div>
        }
        return (
            <Container>
                <Form>
                    <FormGroup tag="fieldset">
                        <legend>Parameters</legend>
                        <FormGroup row>
                            <Col sm={{size: 12}}>
                                <Label check>
                                    <Input type="checkbox" onChange={event => this.setState({exportOnlyOwnedRecords: event.target.checked})} 
                                        checked={this.state.exportOnlyOwnedRecords} />{' '}
                                    Export only owned records
                                </Label>
                            </Col>
                        </FormGroup>
                        <FormGroup row>
                            <Col sm={{size: 12}}>
                                <Label check>
                                    <Input type="checkbox" onChange={event => this.setState({includeRecordFiles: event.target.checked})} 
                                        checked={this.state.includeRecordFiles} />{' '}
                                    Include uploaded files (images, documents, etc.)
                                </Label>
                            </Col>
                        </FormGroup>
                    </FormGroup>
                    <Row>
                        <Col sm={{ size: 'auto', offset: 5 }}>
                            <Button onClick={this.handleExportButtonClick} className="btn btn-success">Export</Button>
                        </Col>
                    </Row>
                </Form>
            </Container>
        )
    }
}



const mapStateToProps = state => {
    const { survey } = state.preferredSurvey

    return { survey: survey }
}

export default connect(mapStateToProps)(BackupDataExportPage);