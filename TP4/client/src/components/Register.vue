<template>
    <b-form class="container" @submit.prevent.stop="submit" novalidate>
        <fieldset>
            <legend class="display-4 mb-3 border-dark border-bottom text-center font-italic">
                Création d'un compte
            </legend>
            <b-form-group label="Nom d'utilisateur :"
                          label-for="username"
                          :invalid-feedback="username.length === 0 ? 'Information requise!'
                                : username.length < 3 ? 'Longueur minimale: 3'
                                : 'Longueur maximale: 255'"
                          valid-feedback="Ok!">
                <b-form-input id="username"
                              v-model="username"
                              placeholder="Entrez un nom d'utilisateur"
                              pattern="^\w{3,255}$"
                              required />
            </b-form-group>
            <b-form-group label="Prénom :"
                          label-for="firstName"
                          invalid-feedback="Prénom invalide!"
                          valid-feedback="Ok!">
                <b-form-input v-model="firstName"
                              id="firstName"
                              placeholder="Entrez votre prénom"
                              pattern="^[a-zA-Z .]+"
                              required />
            </b-form-group>
            <b-form-group label="Nom de famille :"
                          label-for="lastName"
                          invalid-feedback="Nom de famille invalide!"
                          valid-feedback="Ok!">
                <b-form-input v-model="lastName"
                              id="lastName"
                              placeholder="Enter last name"
                              pattern="^[a-zA-Z .]+"
                              required />
            </b-form-group>
            <b-form-group label="Ville :"
                          label-for="city"
                          invalid-feedback="Information requise!"
                          valid-feedback="Ok!">
                <b-form-input v-model="city"
                              id="city"
                              placeholder="Entrez le nom de votre ville"
                              required />
            </b-form-group>
            <b-form-group label="Code postal :"
                          label-for="postalCode"
                          invalid-feedback="Code postal invalide!"
                          valid-feedback="Ok!">
                <b-form-input v-model="postalCode"
                              id="postalCode"
                              placeholder="Entrez votre code postal"
                              pattern="^([A-Z]\s?[0-9]){3}$"
                              required />
            </b-form-group>
            <b-form-group label="Adresse :"
                          label-for="address"
                          invalid-feedback="Adresse invalide!"
                          valid-feedback="Ok!">
                <b-form-input v-model="address"
                              id="address"
                              placeholder="Entrez votre adresse"
                              required />
            </b-form-group>
            <b-form-group label="Téléphone :"
                          label-for="phone"
                          invalid-feedback="Numéro de téléphone invalide!"
                          valid-feedback="Ok!">
                <b-form-input v-model="phone"
                              id="phone"
                              placeholder="Entrez votre numéro de téléphone"
                              pattern="^\+?\d{1,4}?[-.\x20]?\(?\d{1,3}?\)?[-.\x20]?\d{1,4}[-.\x20]?\d{1,4}[-.\x20]?\d{1,9}$"
                              required />
            </b-form-group>
            <div class="text-right">
                <b-button type="submit" size="md" class="px-5 col-lg-4 col-sm-12" variant="primary">
                    S'enregistrer
                </b-button>
                <div class="w-100"></div>
                <small class="text-muted">Vous avez déjà un compte?
                    <button type="button" class="btn btn-sm btn-link" @click="$emit('go-login')">
                        Se connecter
                    </button>
                </small>
            </div>
        </fieldset>
    </b-form>
</template>

<script>
    export default {
        name: "Register",
        props: ["worker"],
        data: () => ({
            username: '',
            address: '',
            firstName: '',
            lastName: '',
            postalCode: '',
            city: '',
            phone: ''
        }),
        methods: {
            submit(ev) {
                const form = ev.target;
                if (!form.checkValidity()) {
                    form.classList.add('was-validated');
                } else {
                    this.$emit('submit', {
                        username: this.username,
                        address: this.address,
                        firstName: this.firstName,
                        lastName: this.lastName,
                        postalCode: this.postalCode,
                        city: this.city,
                        phone: this.phone
                    });
                }
                return false;
            }
        }
    };
</script>