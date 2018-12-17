// noinspection JSUnusedGlobalSymbols

const baseURL = 'http://localhost:8080/rest';

function join(...paths) {
    return paths.reduce((p1, p2) => String(p1).replace(/\/?$/, '')
                                    + String(p2).replace(/^\/?/, '/'))
}

class RequestURL {
    constructor(path) {
        this.path = path;
    }
    
    get url() {
        return join(baseURL, this.path);
    }
    
    add() {
        return join(this.url, '/add');
    }
    
    edit() {
        return join(this.url, '/edit');
    }
    
    all() {
        return join(this.url, '/all');
    }
}

class BasicURL extends RequestURL {
    constructor(path) {
        super(path);
    }
    
    get(key) {
        return join(this.url, `/${key}/get`);
    }
    
    delete(key) {
        return join(this.url, `/${key}/delete`);
    }
}



const clientURL = new (class extends BasicURL {
    constructor() {
        super('/client');
    }
    
    getByUsername(username) {
        return join(this.url, `/${username}/getByUsername`);
    }
    
    commandes(idClient) {
        return join(this.url, `/${idClient}/get/commandes`);
    }
})();

const commandeURL = new BasicURL('/commande');
const ingredientURL = new BasicURL('/ingredient');
const pizzaSizeURL = new BasicURL('/pizza/size');
const pizzaStateURL = new BasicURL('pizza/state');
const pizzaTypeURL = new BasicURL('/pizza/type');
const volumeUnitURL = new BasicURL('/volume-unit');
// const pizzaIngredientURL = new BasicURL('/pizza/ingredient');


export {
    baseURL,
    clientURL,
    commandeURL,
    ingredientURL,
    pizzaSizeURL,
    pizzaStateURL,
    pizzaTypeURL,
    volumeUnitURL
}