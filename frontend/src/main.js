import Vue from 'vue'
import App from './App'
import store from './vuex/store'
import router from './router/index'
import buefy from 'buefy'

Vue.use(buefy);

Vue.config.devTools = true;
Vue.config.productionTip = false;
Vue.config.debug = true;

new Vue({
  el: '#app',
  store,
  router,
  components: {App},
  template: '<App/>'
});
