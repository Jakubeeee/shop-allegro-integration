<!--=========================TEMPLATE=========================-->
<template>
  <div>
    <aside class="menu">
      <p class="menu-label">
        Menu
      </p>
      <ul class="menu-list">
        <li>
          <a href="#/dashboard" :class="{ 'is-active': activePage === 'dashboardPage' }">Dashboard</a>
        </li>
        <li>
          <a href="#/logs" :class="{ 'is-active': activePage === 'logsPage' }">Logs</a>
        </li>
      </ul>
    </aside>
  </div>
</template>
<!--=======================TEMPLATE END=======================-->

<!--==========================SCRIPT==========================-->
<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "sideMenu",
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        activePage: 'activePage'
      })
    }
  }
</script>
<!--=========================SCRIPT END========================-->

<!--===========================STYLE===========================-->
<style scoped>
  .menu-label {
    color: #8F99A3;
    letter-spacing: normal;
    font-weight: 700;
  }

  .menu-list a {
    color: #0F1D38;
    font-size: 14px;
    font-weight: 700;
  }

  .menu-list a:hover {
    background-color: transparent;
    color: #276cda;
  }

  .menu-list a.is-active {
    background-color: transparent;
    color: #276cda;
    font-weight: 700;
  }
</style>
<!--=========================STYLE END=========================-->
