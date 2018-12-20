import extend from 'extend';

const TPS = 0.05;
const TVQ = 0.0997;
const noop = () => void 0;

function merge(...objs) {
    return extend(true, ...objs);
}

export {
    merge,
    noop,
    TPS,
    TVQ
}