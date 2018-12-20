import {pizzaStateURL as stateUrl} from '../RequestUrls';
import {get}           from '../Query';

export function getAllStates() {
    return get(stateUrl.all());
}