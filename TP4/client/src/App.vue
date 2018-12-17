<template>
    <div id="app" :class="`bg-${theme.bg} text-${theme.text}`" >
        <ErrorModal v-if="error != null" :error="error" :theme="theme" @hide="error = null" />
        <Login v-if="!client && login" @submit="loginSubmit" @go-register="login = false"
               :theme="theme" />
        <Register v-if="!client && !login" :worker="worker" @submit="registerSubmit"
                  @go-login="login = true" :theme="theme" />
        <template v-if="client">
            <b-col cols="8">
                <order-editor :order="currentOrder || null" :theme="theme" :types="types"
                              :sizes="sizes"
                              :initial-state="'WAITING'" @submit="createOrder"
                              @cancel="cancelOrder" />
            </b-col>
            <b-col cols="4" class="position-fixed m-0 p-0 order-list">
                <OrdersList :theme="theme" :orders="orders" :selected="currentOrder"
                            @click="showEditOrder"
                            @create-order="currentOrder = null" />
            </b-col>
        </template>
        <div class="bg-transparent theme-selector">
            <template v-for="(t, idx) in themes">
                <b-button :key="idx" v-if="theme.bg !== t.bg"
                          :class="['rounded-circle',  `bg-${t.bg || 'light'}`]"
                          @click="theme = t" />
            </template>
        </div>
    </div>
</template>

<script>
    import 'bootstrap/dist/css/bootstrap.css';
    import 'bootstrap-vue/dist/bootstrap-vue.css';
    import { byUsername, createClient, listCommandes } from './modules/requests/ClientRequests';
    import { createCmd, editCmd }                      from './modules/requests/OrderRequests';
    import { getAllTypes }                             from './modules/requests/PizzaTypeRequests';
    import { getAllSizes }                             from './modules/requests/PizzaSizeRequests';
    import { getAllStates }                            from './modules/requests/PizzaStateRequests';

    import Register    from './components/Register';
    import Login       from './components/Login';
    import OrdersList  from './components/OrdersList';
    import OrderEditor from './components/OrderEditor';
    import ErrorModal  from './components/ErrorModal';
    import { Order }   from './modules/models/Order';
    // import '../public/lib/bootstrap/themes/darkly/bootstrap.min.css';

    const worker = new Worker('js/worker.js');

    export default {
        name: 'app',
        components: {
            ErrorModal,
            OrderEditor,
            OrdersList,
            Login,
            Register
        },
        data() {
            return {
                theme: { bg: 'light', text: 'dark', isDark: false },
                themes: [
                    { bg: 'light', text: 'dark', isDark: false },
                    { bg: 'dark', text: 'light', isDark: true }
                ],
                types: [{ value: null, text: 'Sélectionnez une option', disabled: true }],
                sizes: [{ value: null, text: 'Sélectionnez une option', disabled: true }],
                states: [],
                orders: [],
                currentOrder: null,
                login: true,
                showError: false,
                error: null,
                client: sessionStorage.client ? JSON.parse(sessionStorage.client) : null,
                worker
            };
        },
        methods: {
            async registerSubmit(client) {
                try {
                    if (!(await byUsername(client.username))) {
                        this.client = client = await createClient(client);
                        sessionStorage.setItem('client', JSON.stringify(client));
                        // TODO
                    } else {
                        this.printError({
                            title: `Nom d'utilisateur non disponible!`,
                            message: `Le nom d'utilisateur «${client.username}» n'est pas disponible.`
                                  + `Veuillez choisir un nom d'utilisateur différent.`
                        });
                    }
                } catch (err) {
                    this.printError(err);
                }
            },
            async loginSubmit(username) {
                try {
                    if (!(this.client = await byUsername(username))) {
                        this.printError({
                            title: 'Utilisateur introuvable!',
                            message: `L'utilisateur «${username}» n'existe pas.`
                        });
                    } else {
                        this.orders = (await listCommandes(this.client.id));
                    }
                } catch (err) {
                    this.printError(err);
                }
            },
            showEditOrder(order) {
                this.currentOrder = order;
            },
            async createOrder({ type, size, quantity }) {
                try {
                    if (this.currentOrder != null) {
                        this.currentOrder.type     = type;
                        this.currentOrder.size     = size;
                        this.currentOrder.quantity = quantity;
                        this.currentOrder          = await editCmd(this.currentOrder);
                    } else {
                        this.currentOrder        = new Order({ type, size, quantity });
                        this.currentOrder.client = this.client;
                        this.currentOrder.state  = this.states.find(state => state.key === 'WAITING');
                        this.currentOrder        = await createCmd(this.currentOrder);
                        this.orders.push(this.currentOrder);
                    }
                } catch (err) {
                    this.printError(err);
                }
            },
            async cancelOrder() {
                try {
                    this.currentOrder.state = this.states.find(state => state.key === 'CANCELED');
                    await editCmd(this.currentOrder);
                } catch (err) {
                    this.printError(err);
                }
            },
            printError(err) {
                console.error(err);
                this.showError = true;
                this.error     = {
                    title: err.title || err.name || err.constructor.name,
                    message: err.text || err.message,
                    stack: err.stack
                };
            }
        },
        created() {
            const self = this;
            getAllTypes().then(types =>
                self.types.push(...types.map(type => ({
                    value: type,
                    text: type.title
                }))));
            getAllSizes()
                .then(sizes =>
                    self.sizes.push(...sizes
                        .sort((a, b) => a.inches - b.inches)
                        .map(size => ({
                            value: size,
                            text: size.title + ` (${size.inches} pouces)`
                        }))));
            getAllStates()
                .then(states =>
                    self.states.push(...states));
        },
        mounted() {
            window.types  = this.types;
            window.states = this.states;
            window.orders = this.orders;
            window.sizes  = this.sizes;
        }
    };
</script>

<style lang="scss">
    .order-list {
        top: 0;
        right: 0;
    }

    .theme-selector {
        position: fixed;
        left: 0;
        top: 0;

        > * {
            display: inline-block;
            cursor: pointer;
            padding: .75em;
            margin: 0.25em;
            width: 0;
            height: 0;
        }
    }

    #app {
        min-height: 100vh;
    }
</style>
