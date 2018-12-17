<template>
    <b-card no-body header="<b>Commandes</b>"
            :class="`list-commandes bg-${theme.bg} text-${theme.text}`">
        <b-list-group flush>
            <b-list-group-item v-for="order in sorted()" :key="order.id"
                               :active="(selected||{}).id === (order||{}).id"
                               @click="$emit('click', order)"
                               :variant="theme.bg"
                               :class="order.state.key.toLowerCase()"
                               href="#" align-h="between" align-v="top">
                <div class="row">
                    <div class="col-sm-12 col-md-6 text-left p-0 pl-2">#{{order.id}}</div>
                    <div class="col-sm-12 col-md-6 text-right p-0 pr-2">
                        <small :class="`text-muted ${theme.isDark ? 'dark' : 'light'}` ">{{format(order.dateCreated)}}</small>
                    </div>
                    <div class="col-12 text-right p-0 pr-2 order-state">
                        <small :class="`text-muted ${theme.isDark ? 'dark' : 'light'}` ">{{order.state.title}}</small>
                    </div>
                </div>
            </b-list-group-item>
            <b-list-group-item :variant="selected == null ? 'primary' : theme.bg"
                               class="special-item"
                               @click="$emit('click', null)"
                               :active="selected == null" href="#">
                <fai :icon="['far', 'plus']" />
                Créer une commande
            </b-list-group-item>
        </b-list-group>
    </b-card>
</template>

<script>
    import fecha from 'fecha';

    export default {
        name: "OrdersList",
        props: {
            orders: {
                type: Array,
                required: true
            },
            selected: {
                type: Object,
                default: null
            },
            theme: {
                type: Object,
                default: () => ({ bg: 'light', text: 'dark' })
            }
        },
        // data() {
        //     return {
        //         selected: this.selected
        //     }
        // },
        methods: {
            format(date) {
                return fecha.format(date, 'YYYY-MM-DD')
            },
            sorted() {
                return this.orders.sort((a,b) => {
                    return (a.state.order - b.state.order) || a.dateCreated - b.dateCreated;
                })
            },
            select(order) {
                this.$emit('select', order);
            }
        }
    };
</script>

<style lang="scss">
    .list-commandes {
        border-top: none;
        border-right: none;
        border-bottom: none;
        border-radius: 0;
        height: 100vh;

        .active .light {
            color: #343a40 !important;
        }

        .special-item {
            &:not(.active) {
                color: lighten(#004085, 10%) !important;
                &:hover {
                    box-shadow: inset 0 0 50px rgba(0,123,255,0.1);
                }
            }

            &.active {

            }
        }

        .waiting {

        }

        .canceled {
            /*box-shadow: inset 0 0 50px rgba(220, 53, 69, 0.2);*/
            .order-state *,
            .order-state .light {
                color: #dc3545 !important;
            }
        }

        .cooking, .delivered, .delivering {
            /*box-shadow: inset 0 0 50px rgba(253, 126, 20, 0.2);*/
            .order-state *,
            .order-state .light {
                color: #fd7e14 !important;
            }
        }

        .ready {
            /*box-shadow: inset 0 0 50px rgba(40, 167, 69, 0.2);*/
            .order-state *,
            .order-state .light {
                color: #20c997 !important;
            }
        }
    }
</style>