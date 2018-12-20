import { orderValidator as validator } from '../Validators';
import { TPS, TVQ }                    from '../Utils';

const sym = Symbol();

export class Order {
    constructor(order = {}) {
        order.pizza = order.pizza || {};
        this[sym] = {
            id: order.id,
            pizza: {
                type: order.type || order.pizza.type,
                size: order.size || order.pizza.size,
                state: order.state || order.pizza.state,
                quantity: order.quantity || order.pizza.quantity
            },
            client: order.client,
            total: order.total,
            dateCreated: order.dateCreated,
            dateUpdated: order.dateUpdated
        };
    }
    
    set id(id) {
        this[sym].id = id;
    }
    
    get id() {
        return this[sym].id;
    }
    
    set quantity(qte) {
        this[sym].pizza.quantity = qte;
    }
    
    get quantity() {
        return this[sym].pizza.quantity;
    }
    
    set client(client) {
        if (typeof client === 'number') client = { id: client };
        this[sym].client = client;
    }
    
    get client() {
        return this[sym].client;
    }
    
    set type(type) {
        if (typeof type === 'string') type = { key: type };
        this[sym].pizza.type = type;
    }
    
    get type() {
        return this[sym].pizza.type;
    }
    
    set size(size) {
        if (typeof size === 'string') size = { key: size };
        this[sym].pizza.size = size;
    }
    
    get size() {
        return this[sym].pizza.size;
    }
    
    set state(state) {
        if (typeof state === 'string') state = { key: state };
        this[sym].pizza.state = state;
    }
    
    get state() {
        return this[sym].pizza.state;
    }
    
    get total() {
        if (this[sym].total != null) return this[sym].total;
        console.log(this.quantity, this.size, this.type);
        let subTotal = this.quantity *
                    (((this.size || {}).basePrice || 0) *
                    ((this.type || {}).priceMod || 0));
        return this.total = (subTotal + (subTotal * (TPS + TVQ))) || 0;
    }
    
    set total(total) {
        this[sym].total = total;
    }
    
    get dateCreated() {
        return this[sym].dateCreated && new Date(this[sym].dateCreated);
    }
    
    get dateUpdated() {
        console.log(this[sym], this[sym].dateUpdated && new Date(this[sym].dateUpdated));
        return this[sym].dateUpdated && new Date(this[sym].dateUpdated);
    }
    
    isValid() {
        return !validator.validate(this[sym], { stripUnknown: true }).error;
    }
    
    get() {
        this.total;
        console.log(this.dateUpdated);
        const { error, value } = validator.validate(this[sym], { stripUnknown: true });
        if (error) throw error;
        return value;
    }
}