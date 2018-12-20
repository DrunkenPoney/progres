const {Collection} = require('postman-collection');

module.exports = ({name, path, data, baseURL}) => {
    let iter = 0;
    return new Collection({
        info: {name},
        item: data.map(obj => ({
            name: `add-${++iter}`,
            request: {
                method: 'POST',
                header: [
                    {
                        key: "Content-Type",
                        name: "Content-Type",
                        value: "application/json",
                        type: "text"
                    }
                ],
                body: {
                    mode: 'raw',
                    raw: JSON.stringify(obj)
                },
                url: baseURL + path + "/add"
            },
            event: [
                {
                    listen: 'test',
                    script: {
                        type: 'text/javascript',
                        exec: [
                            `pm.test("Is successful", function () {`,
                            `    pm.response.to.be.success;`,
                            `});`
                        ]
                    },
                    disabled: false
                }
            ]
        }))
    });
};