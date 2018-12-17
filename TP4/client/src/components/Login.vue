<template>
    <b-form class="container" align-v="center"
            @submit.stop.prevent="username ? $emit('submit', username) : validated = true"
            :validated="validated" novalidate>
        <FormTitle text="Connexion" :theme="theme" />
        <b-form-group label="Nom d'utilisateur :"
                      label-for="username"
                      invalid-feedback="Required!">
            <b-form-input id="username"
                          type="text"
                          v-model="username"
                          required
                          placeholder="Entrez votre nom d'utilisateur">
            </b-form-input>
        </b-form-group>
        <div class="text-right">
            <b-button type="submit" size="md" class="px-5 col-lg-4 col-sm-12" variant="primary">
                Se connecter
            </b-button>
            <div class="w-100"></div>
            <small class="text-muted">
                Vous ne possédez aucun compte?
                <button type="button" class="btn btn-sm btn-link px-0"
                        @click="$emit('go-register')">Créer un compte
                </button>
            </small>
        </div>
    </b-form>
</template>

<script>
    import FormTitle from './FormTitle';

    export default {
        name: "Login",
        props: {
            theme: {
                type: Object,
                default: () => ({ text: 'dark', bg: 'light' })
            }
        },
        components: {
            FormTitle
        },
        data() {
            return {
                username: '',
                validated: false
            };
        },
        methods: {

        }
    };
</script>