const joi = require('joi');

const { regex, defaults: { settings, iterationData } } = require('./constants');

module.exports = {
    iterationData: joi.array()
        .items(joi.object()
            .keys({
                order: joi.number()
                    .integer()
                    .required(),
                
                name: joi.string()
                    .min(1)
                    .required(),
                
                path: joi.string()
                    .uri({ allowRelative: true })
                    .required()
                    .replace(/^\/?/, '/'),
                
                data: joi.array()
                    .items(joi.object())
                    .required(),
                
                disabled: joi.boolean()
                    .default(iterationData.disabled)
            })),
    
    settings: joi.object()
        .keys({
            baseURL: joi.string()
                .regex(regex.url, 'URL')
                .default(settings.baseURL)
                .replace(/\/?$/, ''),
            
            minSuccessRate: joi.number()
                .min(0)
                .max(1)
                .default(settings.minSuccessRate),
            
            alwaysConfirm: joi.boolean()
                .default(settings.alwaysConfirm),
            
            verbose: joi.boolean()
                .default(settings.verbose)
        })
};