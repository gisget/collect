import fetch from 'isomorphic-fetch'
import Constants from '../utils/Constants'
import UserService from '../services/UserService'
import UserGroupService from '../services/UserGroupService'

export const LOG_IN_PENDING = 'LOG_IN_PENDING'
export const LOG_IN_SUCCESS = 'LOG_IN_SUCCESS'
export const LOG_IN_FAILED = 'LOG_IN_FAILED'

export const REQUEST_CURRENT_USER = 'REQUEST_CURRENT_USER'
export const RECEIVE_CURRENT_USER = 'RECEIVE_CURRENT_USER'

export const REQUEST_SURVEY_SUMMARIES = 'REQUEST_SURVEY_SUMMARIES'
export const RECEIVE_SURVEY_SUMMARIES = 'RECEIVE_SURVEY_SUMMARIES'
export const INVALIDATE_SURVEY_SUMMARIES = 'INVALIDATE_SURVEY_SUMMARIES'

export const SELECT_PREFERRED_SURVEY = 'SELECT_PREFERRED_SURVEY'
export const REQUEST_FULL_PREFERRED_SURVEY = 'REQUEST_FULL_PREFERRED_SURVEY'
export const RECEIVE_FULL_PREFERRED_SURVEY = 'RECEIVE_FULL_PREFERRED_SURVEY'
export const INVALIDATE_PREFERRED_SURVEY = 'INVALIDATE_PREFERRED_SURVEY'

export const REQUEST_USERS = 'REQUEST_USERS'
export const RECEIVE_USERS = 'RECEIVE_USERS'
export const INVALIDATE_USERS = 'INVALIDATE_USERS'

export const REQUEST_USER_GROUPS = 'REQUEST_USER_GROUPS'
export const RECEIVE_USER_GROUPS = 'RECEIVE_USER_GROUPS'
export const INVALIDATE_USER_GROUPS = 'INVALIDATE_USER_GROUPS'

let BASE_URL = Constants.API_BASE_URL;

let userService = new UserService();
let userGroupService = new UserGroupService();

//LOGIN
function loginPending() {
	return {
		type: LOG_IN_PENDING
	}
}

function loginSuccess() {  
	return {
		type: LOG_IN_SUCCESS
	}
}

function loginFailed() {  
	return {
		type: LOG_IN_FAILED
	}
}

export function logInUser(credentials) {  
	return function(dispatch) {
		dispatch(loginPending());

		function handleErrors(response) {
			if (!response.ok) {
				throw Error(response.statusText);
			}
			return response;
		}
		
		userService.login(credentials)
			.then(handleErrors)
			.then(response => {
				if (!response.ok || response.url.indexOf("login_error") > 0) {
					dispatch(loginFailed());
				} else {
					dispatch(loginSuccess());
					dispatch(fetchCurrentUser());
				}
			})
	};
}

function requestCurrentUser() {
	return {
		type: REQUEST_CURRENT_USER
	}
}

export function fetchCurrentUser() {
	return function (dispatch) {
		dispatch(requestCurrentUser())
		var url = BASE_URL + 'session/user';
		return fetch(url, {
			headers: {
				credentials: 'same-origin'
			}
		})
		.then(response => response.json(),
			error => console.log('An error occured.', error))
		.then(json => {
			dispatch(receiveCurrentUser(json));
			dispatch(fetchSurveySummaries(json));
		})
	}
}

function receiveCurrentUser(json) {
	return {
	    type: RECEIVE_CURRENT_USER,
	    user: json //TODO map into User object
	}
}

//SURVEY SUMMARIES
function requestSurveySummaries() {
	return {
		type: REQUEST_SURVEY_SUMMARIES
	}
}

function receiveSurveySummaries(json) {
	return {
	    type: RECEIVE_SURVEY_SUMMARIES,
	    summaries: json.map(s => s), //TODO map into Survey object
	    receivedAt: Date.now()
	}
}

export function fetchSurveySummaries() {
	return function (dispatch) {
		dispatch(requestSurveySummaries())
		var url = BASE_URL + 'survey/summaries.json';
		return fetch(url)
		    .then(response => response.json(),
	    		error => console.log('An error occured.', error))
		    .then(json => dispatch(receiveSurveySummaries(json)))
	}
}

export function invalidateSurveySummaries(summaries) {
	return {
		type: INVALIDATE_SURVEY_SUMMARIES,
		summaries
	}
}

//PREFERRED SURVEY
export function selectPreferredSurvey(preferredSurveySummary) {
	let surveyId = preferredSurveySummary.id;
	return function (dispatch) {
		dispatch(requestFullPreferredSurvey(surveyId))
		var url = BASE_URL + 'survey/' + surveyId + '.json';
		return fetch(url)
		    .then(response => response.json(),
	    		error => console.log('An error occured.', error))
		    .then(json => dispatch(receiveFullPreferredSurvey(json)))
	}
}


export function receiveFullPreferredSurvey(json) {
	return {
		type: RECEIVE_FULL_PREFERRED_SURVEY,
		survey: json,
		receivedAt: Date.now()
	}
}

export function requestFullPreferredSurvey(surveyId) {
	return {
		type: REQUEST_FULL_PREFERRED_SURVEY,
		surveyId,
		receivedAt: Date.now()
	}
}

export function invalidatePreferredSurvey(preferredSurvey) {
	return {
		type: INVALIDATE_PREFERRED_SURVEY,
		preferredSurvey
	}
}

//USERS
function requestUsers() {
	return {
		type: REQUEST_USERS
	}
}

function receiveUsers(json) {
	return {
	    type: RECEIVE_USERS,
	    users: json.map(user => user), //TODO map into User object
	    receivedAt: Date.now()
	}
}

export function fetchUsers() {
	return function (dispatch) {
		dispatch(requestUsers())
		userService.fetchUsers().then(json => dispatch(receiveUsers(json)))
	}
}

export function invalidateUsers(users) {
	return {
		type: INVALIDATE_USERS,
		users
	}
}

//USER GROUPS
function requestUserGroups() {
	return {
		type: REQUEST_USER_GROUPS
	}
}

function receiveUserGroups(json) {
	return {
	    type: RECEIVE_USER_GROUPS,
	    userGroups: json, //TODO map into UserGroup object
	    receivedAt: Date.now()
	}
}

export function fetchUserGroups() {
	return function (dispatch) {
		dispatch(requestUserGroups())
		userGroupService.fetchAllAvailableGroups().then(json => dispatch(receiveUserGroups(json)))
	}
}

export function invalidateUserGroups(userGroups) {
	return {
		type: INVALIDATE_USER_GROUPS,
		userGroups
	}
}
