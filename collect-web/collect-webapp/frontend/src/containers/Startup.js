import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'
import * as Actions from 'actions';

class Startup extends Component {
    
    static propTypes = {
        loggedUser: PropTypes.object
    }

    componentDidMount() {
        this.props.actions.fetchApplicationInfo()
        this.props.actions.fetchCurrentUser()
        this.props.actions.fetchUsers()
        this.props.actions.fetchUserGroups()
    }

    render() {
        const p = this.props
        if (!p.isLoggedUserReady || p.isFetchingLoggedUser 
            || !p.isUsersReady || p.isFetchingUsers
            || !p.isUserGroupsReady || p.isFetchingUserGroups ) {
            return <p>Loading...</p>
        } else {
            return this.props.children
        }
    }
}

function mapStateToProps(state) {
    const {
        loggedUser,
        isFetching: isFetchingLoggedUser,
        initialized: isLoggedUserReady
    } = state.session || {
        isLoggedUserReady: false,
        isFetchingLoggedUser: true
    }

    const {
        users,
        isFetching: isFetchingUsers,
        initialized: isUsersReady
    } = state.users || {
        isUsersReady: false,
        isFetchingUsers: true
    }

    const {
        userGroups,
        isFetching: isFetchingUserGroups,
        initialized: isUserGroupsReady
    } = state.userGroups || {
        isUserGroupsReady: true,
        isFetchingUserGroups: true
    }

    return {
        isLoggedUserReady,
        isFetchingLoggedUser,
        loggedUser,
        isUsersReady,
        isFetchingUsers,
        users,
        isUserGroupsReady,
        isFetchingUserGroups,
        userGroups
    };
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators(Actions, dispatch)
    };
}

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Startup);