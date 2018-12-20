const _get = (path, crash = true) => {
    try {
        return String(path)
            .split(/]?\.|\[/)
            .reduce((o, p) => o[p], self);
    } catch(e) {
        if (crash) throw e;
    }
};

const _set = (path, val) => {
    const last = (path = String(path).split(/]?\.|\[/)).pop();
    return path.reduce((o, p) => o[p], self)[last] = val;
};

function register({username, firstName, lastName, address, postalCode, city, phone }) {

}

// action: call | set | get
// prop: path to property
// data: array of arguments or value to set
onmessage = function ({ action, prop, data }) {
    switch(action) {
        case 'call':
            self.postMessage(_get(prop)(...(Array.isArray(data) ? data : [data])), null);
            break;
        case 'get':
            self.postMessage(_get(prop), null);
            break;
        case 'set':
            self.postMessage(_set(prop, data), null);
            break;
        default:
            throw 'Invalid action';
    }
};