module.exports = [
    {
        order: 1,
        name: 'ingredients',
        path: '/ingredient',
        data: require('./ingredients'),
        disabled: false
    }, {
        order: 3,
        name: 'pizza-ingredients',
        path: '/pizza/ingredient',
        data: require('./pizza-ingredients'),
        disabled: false
    }, {
        order: 0,
        name: 'pizza-states',
        path: '/pizza/state',
        data: require('./pizza-states'),
        disabled: false
    }, {
        order: 2,
        name: 'pizza-types',
        path: '/pizza/type',
        data: require('./pizza-types'),
        disabled: false
    }, {
        order: 0,
        name: 'volume-units',
        path: '/volume-unit',
        data: require('./volume-units'),
        disabled: false
    }, {
        order: 0,
        name: 'pizza-sizes',
        path: '/pizza/size',
        data: require('./pizza-sizes'),
        disabled: false
    }
];