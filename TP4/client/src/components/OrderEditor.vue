<template>
    <b-form class="container-fluid" @submit.prevent.stop="submit" novalidate>
        <fieldset>
            <FormTitle :text="(order == null ? 'Créez une' : 'Modifiez votre') + ' commande'"
                       :theme="theme" />
            <b-form-group label="Type de pizza :"
                          label-for="pizzaType">
                <b-select v-model="selection.type" :options="types" id="pizzaType" required
                          :disabled="!isWaiting()"
                          :readonly="!isWaiting()" />
            </b-form-group>
            <b-form-group label="Format :"
                          label-for="pizzaSize">
                <b-select v-model="selection.size" :options="sizes" id="pizzaSize" required
                          :disabled="!isWaiting()"
                          :readonly="!isWaiting()" />
            </b-form-group>
            <b-form-group horizontal label="Quantité :"
                          label-for="quantity">
                <b-form-input v-model="selection.quantity"
                              type="number" id="quantity" :formatter="formatQte"
                              :disabled="!isWaiting()"
                              required :readonly="!isWaiting()" />
            </b-form-group>


            <b-row align-h="end" class="px-3">
                <b-col cols="4">
                    <b-row class="text-right">
                        <b-col cols="6" class="pl-0">Sous-Total:</b-col>
                        <b-col cols="6">{{subTotal().toFixed(2)}} $</b-col>
                    </b-row>
                </b-col>
            </b-row>
            <b-row align-h="end" class="px-3">
                <b-col cols="4">
                    <b-row class="text-right">
                        <b-col cols="6" class="pl-0">TVQ:</b-col>
                        <b-col cols="6">{{(subTotal() * tvq).toFixed(2)}} $</b-col>
                    </b-row>
                </b-col>
            </b-row>
            <b-row align-h="end" class="px-3">
                <b-col cols="4">
                    <b-row class="text-right">
                        <b-col cols="6" class="pl-0">TPS:</b-col>
                        <b-col cols="6">{{(subTotal() * tps).toFixed(2)}} $</b-col>
                    </b-row>
                </b-col>
            </b-row>
            <b-row align-h="end" class="px-3">
                <b-col cols="4">
                    <b-row class="text-right font-weight-bold border-top">
                        <b-col cols="6" class="pl-0">Total:</b-col>
                        <b-col cols="6">{{((subTotal() * (tps + tvq)) + subTotal()).toFixed(2)}} $
                        </b-col>
                    </b-row>
                </b-col>
            </b-row>
            <b-row align-h="end" class="px-3 mt-3">
                <b-button v-if="isWaiting()" class="col-4"
                          variant="danger" @click="$emit('cancel')">Annuler la commande
                </b-button>
            </b-row>
            <b-row align-h="end" class="px-3 mt-3">
                <b-button v-if="order == null || isWaiting()" class="col-4"
                          type="submit" variant="primary">{{order == null ? 'Commander' :
                                                          'Appliquer'}}
                </b-button>
            </b-row>
        </fieldset>
    </b-form>
</template>

<script>
    import FormTitle    from './FormTitle';
    import { TPS, TVQ } from '../modules/Utils';

    export default {
        name: "OrderEditor",
        props: ["order", "theme", "types", "sizes"],
        components: {
            FormTitle
        },
        data() {
            console.log(this.order);
            return {
                tvq: TVQ,
                tps: TPS,
                selection: {
                    type: (this.order || {}).type || null,
                    size: (this.order || {}).size || null,
                    quantity: (this.order || {}).quantity || 1
                }
            };
        },
        watch: {
            order(order) {
                order = order || {};
                this.selection.type = order.type || null;
                this.selection.size = order.size || null;
                this.selection.quantity = order.quantity || 1;
            }
        },
        methods: {
            submit(ev) {
                const form = ev.target;
                if (form.checkValidity() && this.selection.type && this.selection.size) {
                    this.$emit('submit', this.selection);
                } else {
                    form.classList.add('was-validated');
                }
            },
            formatQte(val) {
                return Math.max(Math.round(val), 1);
            },
            subTotal() {
                const { size, type, quantity: qte } = this.selection;
                return qte * (((size || {}).basePrice || 0) * ((type || {}).priceMod || 0));
            },
            isWaiting() {
                return this.order != null && (this.order.state||{}).key === 'WAITING';
            }
        }
    };
</script>

<style scoped>

</style>