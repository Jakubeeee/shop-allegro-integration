import Vue from 'vue'
import Router from 'vue-router'
import store from '../vuex/store'
import Content from '@/pages/Content.vue'
import LoginPage from '@/pages/LoginPage.vue'
import DashboardPage from '@/pages/DashboardPage.vue'
import LogsPage from '@/pages/LogsPage.vue'

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/login',
      name: 'loginPage',
      component: LoginPage,
      meta: {requiresAuth: false}
    },
    {
      path: '/',
      name: 'content',
      component: Content,
      meta: {requiresAuth: false},
      children: [
        {
          path: '/dashboard',
          name: 'dashboardPage',
          component: DashboardPage,
          meta: {requiresAuth: true}
        },
        {
          path: '/logs',
          name: 'logsPage',
          component: LogsPage,
          meta: {requiresAuth: true}
        }
      ]
    }
  ]
});

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    if (store.getters.authenticated)
      next();
    else
      next('/login');
  }
  else {
    if (store.getters.authenticated)
      next('/dashboard');
    else
      next();
  }
});

router.afterEach((to) => {
  store.dispatch('registerPageChange', to.name);
});

export default router;
