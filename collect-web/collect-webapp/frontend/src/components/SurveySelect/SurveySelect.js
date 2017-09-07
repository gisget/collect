import React, { Component } from 'react';
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { FormControl } from 'react-bootstrap';
import { selectPreferredSurvey } from '../../actions'

class SurveySelect extends Component {
    constructor( props ) {
        super( props );
        this.handleChange = this.handleChange.bind( this );
    }
    static propTypes = {
        summaries: PropTypes.array.isRequired,
        selectedSurvey: PropTypes.object,
		isFetchingSummaries: PropTypes.bool.isRequired,
		isFetchingPreferredSurvey: PropTypes.bool.isRequired,
        lastUpdated: PropTypes.number,
        dispatch: PropTypes.func.isRequired
    }
    componentDidMount() {
        //const { dispatch } = this.props
        //dispatch(fetchSurveySummaries())
	}
	
    handleChange(event) {
    	const { summaries } = this.props
    	var survey = summaries.find(summary => summary.id === parseInt(event.target.value, 10));
    	if (survey) {
    		this.props.dispatch(selectPreferredSurvey(survey));
    	}
    }
	
    render() {
    	const { selectedSurvey, isFetchingSummaries, summaries } = this.props
    	const isEmpty = summaries.length === 0
    	
    	var options = [];
    	options.push(<option key='-1' value='-1'>--- Select the preferred survey ---</option>);
    	for (var i=0; i < summaries.length; i++) {
    		var summary = summaries[i];
    		options.push(<option key={i} value={summary.id}>{summary.name}</option>);
    	}
    	return (<div>
			{
			isEmpty ? (isFetchingSummaries ? <span>Loading...</span> : <span>No published surveys found</span>)
            : <div style={{ opacity: isFetchingSummaries ? 0.5 : 1 }}>
	            <FormControl componentClass="select" bsClass="btn btn-info" value={selectedSurvey == null ? '': selectedSurvey.id} 
	        		onChange={this.handleChange}>{options}</FormControl>
              </div>
			}
			</div>
		)
    }
}

const mapStateToProps = state => {
  const { preferredSurvey, surveySummaries } = state
  const {
	  	isFetching: isFetchingPreferredSurvey,
	  	lastUpdated: lastPreferredSurveyUpdate,
	    survey: selectedSurvey
	  } = preferredSurvey || {
		  isFetching: true,
		  survey: null
	  }
  const {
	  	isFetching: isFetchingSummaries,
	    lastUpdated: lastSummariesUpdate,
	    items: summaries
	  } = surveySummaries || {
		  isFetching: true,
		  items: []
	  }
  
  return {
    isFetchingSummaries,
    summaries,
    lastSummariesUpdate,
    isFetchingPreferredSurvey,
    selectedSurvey,
    lastPreferredSurveyUpdate
  }
}
export default connect(mapStateToProps)(SurveySelect)