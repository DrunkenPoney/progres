import { del, get, post, put }         from '../Query';
import { commandeURL as cmdUrl }       from '../RequestUrls';
import { orderValidator as validator } from '../Validators';
import { Order }                       from '../models/Order';

export async function createCmd(cmd) {
    cmd = new Order(cmd).get();
    console.log(`%ccarrote ${JSON.stringify(cmd)}`, 'color: #F90');
    const { error, value: body } = validator.validate(cmd, { stripUnknown: true });
    if (error) throw error;
    cmd.id = body.id = await post(cmdUrl.add(), body);
    return body;
}

export async function editCmd(cmd) {
    cmd = new Order(cmd).get();
    const { error, value: body } = validator.validate(cmd, { stripUnknown: true });
    if (error) throw error;
    const order = await put(cmdUrl.edit(), body);
    return order && new Order(order);
}

export function delCmd(idCmd) {
    if (idCmd && typeof idCmd === 'object') idCmd = idCmd.id;
    if (typeof idCmd !== 'number') throw new TypeError();
    return del(cmdUrl.delete(idCmd));
}

export async function getCmd(idCmd) {
    if (idCmd && typeof idCmd === 'object') idCmd = idCmd.id;
    if (typeof idCmd !== 'number') throw new TypeError();
    const order = await get(cmdUrl.get(idCmd));
    return order && new Order(order);
}