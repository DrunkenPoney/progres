'use strict';

import { merge } from './Utils';

const baseOpts = {
    mode: 'cors',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
};

const NotOkError = (response) => `REQUEST ERROR > Status: ${response.status} `
                                 + (response.statusText ? `(${response.statusText})` : '');

async function get(url, options = {}) {
    let resp = await fetch(url,
        merge({
            method: 'GET',
            mode: 'cors'
        }, options));
    if (!resp.ok) throw NotOkError(resp);
    resp = await resp.text();
    return resp ? JSON.parse(resp) : resp;
}

async function post(url, body = {}, options = {}) {
    let resp = await fetch(url,
        merge(baseOpts, {
            method: 'POST',
            body: JSON.stringify(body)
        }, options));
    if (!resp.ok) throw NotOkError(resp);
    resp = await resp.text();
    return resp ? JSON.parse(resp) : resp;
}

async function del(url, body = {}, options = {}) {
    let resp = await fetch(url,
        merge(baseOpts, {
            method: 'DELETE',
            body: JSON.stringify(body)
        }, options));
    if (!resp.ok) throw NotOkError(resp);
    resp = await resp.text();
    return resp ? JSON.parse(resp) : resp;
}

async function put(url, body = {}, options = {}) {
    let resp = await fetch(url,
        merge(baseOpts, {
            method: 'PUT',
            body: JSON.stringify(body)
        }, options));
    if (!resp.ok) throw NotOkError(resp);
    resp = await resp.text();
    return resp ? JSON.parse(resp) : resp;
}

export {
    get, post, put, del
};

