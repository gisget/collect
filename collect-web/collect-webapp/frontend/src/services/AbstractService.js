import Constants from '../utils/Constants';

export default class AbstractService {

    BASE_URL = Constants.API_BASE_URL;
    
    get(url, data) {
        let queryData = this._toQueryData(data);
        return fetch(this.BASE_URL + url + '?' + queryData, {
            credentials: 'include'
        })
        .then(response => response.json(),
            error => console.log('An error occured.', error))
        .catch(error => {
            throw(error);
        })
    }

    delete(url) {
        return fetch(this.BASE_URL + url, {
            credentials: 'include',
            method: 'DELETE'
        })
        .then(response => response.json(),
            error => console.log('An error occured.', error))
        .catch(error => {
            throw(error);
        })
    }

    post(url, data) {
        let body = this._toQueryData(data);

        return fetch(this.BASE_URL + url, {
            credentials: 'include',
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
            },
            body: body
        }).then(response => response.json(),
            error => console.log('An error occured.', error))
        .catch(error => {
            throw(error);
        })
    }

    postJson(url, data) {
        return this._sendJson(url, data, 'POST')
    }

    patchJson(url, data) {
        return this._sendJson(url, data, 'PATCH')
    }

    _sendJson(url, data, method) {
        return fetch(this.BASE_URL + url, {
            credentials: 'include',
            method: method,
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }).then(response => response.json(),
            error => console.log('An error occured.', error))
        .catch(error => {
            throw(error);
        })
    }

    downloadFile(url, windowName) {
        window.open(url, windowName ? windowName : '_blank')
    }

    _toQueryData(data, propPrefix) {
        if (! data) {
            return ''
        }
        return Object.keys(data).map((key) => {
            let val = data[key];
            if (val === null) {
                return '';
            } else {
                if (val instanceof Array) {
                    let arrQueryDataParts = []
                    for(let i=0; i<val.length; i++) {
                        let nestedPropPrefix = encodeURIComponent(key) + '[' + i +'].';
                        arrQueryDataParts.push(this._toQueryData(val[i], nestedPropPrefix))
                    }
                    return arrQueryDataParts.join('&');
                } else {
                    return (propPrefix ? propPrefix : '') + encodeURIComponent(key) + '=' + encodeURIComponent(val)
                }
            }
          }).join('&');
    }
    
}
    