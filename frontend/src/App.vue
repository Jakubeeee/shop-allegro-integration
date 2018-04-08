<template>
  <div id="app">
    <navbar/>
    <router-view/>
  </div>
</template>

<script>
  import Navbar from './components/Navbar.vue';

  export default {
    name: 'App',
    components: {
      'navbar': Navbar
    },
    created() {
      setInterval(() => {
        this.$store.dispatch('getProgress');
        this.$store.dispatch('getLogs');
      }, 2000)
    }
  }
</script>

<style>
  html, body {
    font-family: 'Open Sans', serif;
    font-size: 16px;
    line-height: 1.5;
    height: 100%;
    background: #ECF0F3;
  }

  .hero.is-success {
    background: #F2F6FA;
  }

  .hero, .hero.is-success {
    -webkit-box-shadow: none;
    box-shadow: none;
  }

  .box {
    margin-top: 2rem;
    text-align: left;
  }

  label {
    color: rgb(122, 122, 122);
  }

  input {
    font-weight: 300;
  }

  p {
    font-weight: 700;
  }

  p.subtitle {
    padding-top: 1rem;
  }

  .snackbar.is-bottom-left {
    align-self: start;
  }
</style>
