const extend = require('extend');

module.exports = {
    merge: (...objs) => extend(true, ...objs)
};