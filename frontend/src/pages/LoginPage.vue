<!--=========================TEMPLATE=========================-->
<template>
  <section class="hero is-success">
    <div class="hero-body">
      <div class="container has-text-centered">
        <div class="column is-4 is-offset-4">

          <h3 class="title has-text-grey">Log in to your account</h3>

          <div class="box">

            <!--USERNAME-->
            <b-field>
              <b-input size="is-large" type="text" v-model="credentials.username"
                       placeholder="Username" icon="account" autofocus="">
              </b-input>
            </b-field>

            <!--PASSWORD-->
            <b-field>
              <b-input size="is-large" type="password" v-model="credentials.password"
                       placeholder="Password" icon="key-variant">
              </b-input>
            </b-field>

            <!--SUBMIT BUTTON-->
            <button @click="login" class="button is-block is-info is-large is-fullwidth">
              Log in
            </button>

          </div>

        </div>
      </div>
    </div>
    <b-loading :active.sync="isLoading"></b-loading>
  </section>
</template>
<!--=======================TEMPLATE END=======================-->

<!--==========================SCRIPT==========================-->
<script>
  import {notificationUtils} from '../mixins/notificationUtils'

  export default {
    name: "loginPage",
    data() {
      return {
        credentials: {
          username: '',
          password: ''
        },
        isLoading: false
      }
    },
    mixins: [notificationUtils],
    methods: {
      login() {
        this.isLoading = true;
        this.$store.dispatch('login', this.credentials).then(() => {
          this.isLoading = false;
          if (!this.$store.getters.authenticated) {
            this.dangerSnackbar('Invalid username or password');
            this.credentials.password = '';
          }
        });
      }
    }
  }
</script>
<!--=========================SCRIPT END========================-->

<!--===========================STYLE===========================-->
<style scoped>
</style>
<!--=========================STYLE END=========================-->
