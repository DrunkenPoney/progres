'use strict';

import joi from 'joi';

// noinspection JSValidateTypes
const clientValidator = joi.object().keys({
    id: joi.number()
        .integer()
        .optional(),
    username: joi.string()
        .lowercase()
        .min(1)
        .max(255)
        .regex(/^\w{3,255}$/, "username")
        .required(),
    firstName: joi.string()
        .replace(/\s+/g, ' ')
        .trim()
        .min(1)
        .regex(/^[a-z ]+$/i, "first name")
        .required(),
    lastName: joi.string()
        .replace(/\s+/g, ' ')
        .trim()
        .min(1)
        .regex(/^[a-z ]+$/i, "last name")
        .required(),
    postalCode: joi.string()
        .replace(/\s+/g, '')
        .uppercase()
        .length(6)
        .regex(/^([A-Z][0-9]){3}$/, "zip code")
        .required(),
    city: joi.string()
        .replace(/\s+/g, ' ')
        .trim()
        .min(1)
        .required(),
    address: joi.string()
        .replace(/\s+/g, ' ')
        .trim()
        .min(1)
        .required(),
    phone: joi.string()
        .replace(/\D+/g, '')
        .min(10)
        .max(15)
        .required()
});

const orderValidator = joi.object().keys({
    id: joi.number()
        .integer()
        .optional(),
    total: joi.number().min(0).required(),
    pizza: joi.object().keys({
        type: joi.object().keys({
            key: joi.string().uppercase().min(1).required()
        }).required(),
        size: joi.object().keys({
            key: joi.string().uppercase().min(1).required()
        }).required(),
        state: joi.object().keys({
            key: joi.string().uppercase().min(1).required()
        }).required(),
        quantity: joi.number().integer().min(1).required()
    }).required(),
    client: joi.object().keys({
        id: joi.number().integer().required()
    }).required(),
    dateCreated: joi.date().optional()
});

export {
    clientValidator,
    orderValidator
};