import {pizzaTypeURL as typeUrl} from '../RequestUrls';
import {get}          from '../Query';

export function getAllTypes() {
    return get(typeUrl.all());
}