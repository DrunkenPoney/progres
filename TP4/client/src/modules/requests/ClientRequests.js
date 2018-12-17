'use strict';

import { clientValidator as validator } from '../Validators';
import { clientURL }                    from '../RequestUrls';
import { get, post }                    from '../Query';
import { Order }                        from '../models/Order';

export async function createClient(client) {
    const { error, value: body } = validator.validate(client, { stripUnknown: true });
    if (error) throw error;
    client.id = body.id = await post(clientURL.add(), body);
    return body;
}

export function byUsername(username) {
    return get(clientURL.getByUsername(username));
}

export async function listCommandes(clientId) {
    const orders = await get(clientURL.commandes(clientId));
    return orders.map(order => new Order(order));
}