import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios/index";
import router from '../router/index'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex);

const state = {
  authenticated: false,
  activePage: '',
  logList: [],
  currentProgress: 0,
  maxProgress: 1,
  updating: 0,
};

const getters = {
  authenticated: state => state.authenticated,
  activePage: state => state.activePage,
  logList: state => state.logList,
  currentProgress: state => state.currentProgress,
  maxProgress: state => state.maxProgress,
  updating: state => state.updating,
};

const mutations = {
  SET_AUTHENTICATED(state, newValue) {
    state.authenticated = newValue;
  },
  SET_ACTIVE_PAGE(state, newValue) {
    state.activePage = newValue;
  },
  SET_LOG_LIST(state, newValue) {
    state.logList = newValue;
  },
  SET_CURRENT_PROGRESS(state, newValue) {
    state.currentProgress = newValue;
  },
  SET_MAX_PROGRESS(state, newValue) {
    state.maxProgress = newValue;
  },
  SET_UPDATING(state, newValue) {
    state.updating = newValue;
  }
};

const actions = {
  login: async (context, credentials) => {
    let params = 'password=' + credentials.password + '&username=' + credentials.username;
    await axios('/login', {
      method: "post",
      data: params,
      withCredentials: true
    }).then(() => {
      context.commit('SET_AUTHENTICATED', true);
      router.push({path: '/'});
    }).catch(() => {
    })
  },
  logout: async (context) => {
    await axios.post('/logout').then(() => {
        context.commit('SET_AUTHENTICATED', false);
        router.push({path: '/login'});
      }
    )
  },
  registerPageChange: (context, pageName) => {
    context.commit('SET_ACTIVE_PAGE', pageName);
  },
  getLogs: async (context) => {
    await axios.get('/logs')
      .then(response => {
        context.commit('SET_LOG_LIST', response.data);
      });
  },
  getProgress: (context) => {
    axios.get('/progress')
      .then(response => {
        context.commit('SET_CURRENT_PROGRESS', response.data.currentProgress);
        context.commit('SET_MAX_PROGRESS', response.data.maxProgress);
        context.commit('SET_UPDATING', response.data.isUpdating);
      });
  },
};

const store = new Vuex.Store({
  state,
  getters,
  actions,
  mutations,
  plugins: [createPersistedState()]
});

export default store;
