import {pizzaSizeURL as sizeUrl} from '../RequestUrls';
import {get}          from '../Query';

export function getAllSizes() {
    return get(sizeUrl.all());
}

export function getByKey(key) {
    return get(sizeUrl.get(key))
}