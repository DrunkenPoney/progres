import Vue from 'vue';
import App from './App.vue';
import Bootstrap from 'bootstrap-vue';
import { library } from '@fortawesome/fontawesome-svg-core'
// import { fas } from '@fortawesome/free-solid-svg-icons'
import { fal }  from '@fortawesome/pro-light-svg-icons';
import { far } from '@fortawesome/pro-regular-svg-icons';
import { fas } from '@fortawesome/pro-solid-svg-icons';
import { fab } from '@fortawesome/free-brands-svg-icons';

// noinspection ES6CheckImport
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

library.add(fal, far, fas, fab);
Vue.component('fai', FontAwesomeIcon);

Vue.config.productionTip = false;
Vue.use(Bootstrap);


new Vue({
  render: h => h(App),
}).$mount('#app');
