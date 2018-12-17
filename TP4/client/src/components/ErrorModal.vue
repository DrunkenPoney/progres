<template>
    <b-modal ok-only header-bg-variant="danger" header-text-variant="light"
             :header-border-variant="theme.bg" :visible="true" :body-bg-variant="theme.bg"
             :body-text-variant="theme.text" :footer-bg-variant="theme.bg"
             :footer-border-variant="theme.bg" centered v-bind:title="error.title" title-tag="h4"
             @ok.prevent="$emit('hide')" :aria-hidden="false" :aria-visible="true"
             @hide.prevent="$emit('hide')">
        {{error.message}}
        <b-card v-if="error.stack" :class="`bg-${theme.bg} mt-3`" no-body>
            <b-card-header header-tag="header" class="p-0" role="tab">
                <b-btn block href="#" v-b-toggle.stacktrace variant="info">
                    Afficher les détails
                </b-btn>
            </b-card-header>
            <b-collapse id="stacktrace" role="tabpanel" class="p-0">
                <b-card-body class="p-0 m-0">
                    <pre :class="`text-${theme.text} p-1 m-0 stack-trace`"
                         v-html="printStack(error.stack)" />
                </b-card-body>
            </b-collapse>
        </b-card>
    </b-modal>
</template>
<script>
    export default {
        name: 'ErrorModal',
        props: ["error", "theme"],
        methods: {
            printStack(stack) {
                return stack.replace(/webpack-internal:\/{3}/g, '').split(/\r?\n/).map(line => {
                    return line.replace(/^(\s+at) ((?:[^\s(]+|\(anonymous function\))+)([^(]+)\(([^)]+)\)$/, (m, g1, g2, g3, g4) => {
                        g4 = g4.split(':');
                        return g1 + ` <span class="object">${g2}</span>${g3}(<span class="file-path">${g4[0]}</span>`
                               + `:<span class="line-number">${g4[1]}</span>:<span class="line-column">${g4[2]}</span>)`;
                    });
                }).join('\n');
            },
            ok(ev) {
                ev.stopImmediatePropagation();
                this.$emit('hide');
            }
        }
    };
</script>

<style lang="scss">
    #stacktrace {
        * {
            ::-webkit-scrollbar-track {
                -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
                background-color: rgba(0,0,0, 0.3);
            }

            ::-webkit-scrollbar {
                width: 6px;
                background-color: rgba(255,255,255, 0.3);
            }

            ::-webkit-scrollbar-thumb {
                background-color: darken(#17a2b8, 10%);
            }
        }

        .stack-trace {
            color: lighten(#6c757d, 10%) !important;
            font-size: x-small;

            &:first-line {
                font-weight: bold;
            }

            .object {
                color: darken(desaturate(#ffc107, 10%), 10%);
                font-style: italic;
            }

            .file-path {
                color: darken(desaturate(#fd7e14, 10%), 10%);
            }

            .line-number {
                color: #1f76b0;
            }

            .line-column {
                color: #1f76b0;
            }

        }
    }
</style>