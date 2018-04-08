<!--=========================TEMPLATE=========================-->
<template>
  <nav class="navbar is-white">
    <div class="container">
      <div class="navbar-brand">
        <a class="navbar-item brand-text" href="#/dashboard">
          ClickShop-Allegro integrator
        </a>
      </div>
      <div id="navMenu" class="navbar-menu">
        <div class="navbar-start"></div>
        <div class="navbar-item" v-if="authenticated">
          <a class="button is-danger is-outlined navbar-item" @click="logout">
            <span>Log out</span>
            <b-icon icon="logout"></b-icon>
          </a>
        </div>
      </div>
    </div>
  </nav>
</template>
<!--=======================TEMPLATE END=======================-->

<!--==========================SCRIPT==========================-->
<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "navbar",
    data() {
      return {}
    },
    methods: {
      logout() {
        this.$store.dispatch('logout');
      }
    },
    computed: {
      ...mapGetters({
        authenticated: 'authenticated'
      }),
    }
  }
</script>
<!--=========================SCRIPT END========================-->

<!--===========================STYLE===========================-->
<style scoped>

  nav.navbar {
    border-top: 4px solid #276cda;
    margin-bottom: 1rem;
  }

  .navbar-item.brand-text {
    font-weight: 300;
    color: #8F99A3;
  }

  .navbar-item, .navbar-link {
    font-size: 14px;
    font-weight: 700;
    padding: 10px;
  }

</style>
<!--=========================STYLE END=========================-->
