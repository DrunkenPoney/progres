module.exports = {
    regex: {
        url: /^((?:https?|ftp):\/\/?)?([^:/\s.]+\.[^:/\s]|localhost)(:\d+)?((?:\/\w+)*\/)?([\w\-.]+[^#?\s]+)?([^#]+)?(#[\w-]+)?$/
    },
    defaults: {
        settings: {
            baseURL: 'http://localhost:8080',
            minSuccessRate: 1,
            alwaysConfirm: true,
            verbose: false
        },
        iterationData: {
            disabled: false
        }
    }
};