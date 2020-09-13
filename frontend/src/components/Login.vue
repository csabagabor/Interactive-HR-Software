<template>
    <div class="container body-content">

        <div class="row">
            <div style="margin-left: 250px" class="col-md-8">
                <br><br><br><br><br><br>
                <h1 class="text-center mb-4">Login</h1>
                
                <div class="row">
                    <div class="col-md-6 mx-auto">
                        <div class="card rounded shadow-lg p-3 mb-5 bg-white rounded">
                            <div class="card-body">

                                <div v-if="msg && msg.length > 0 " class="alert alert-danger">
                                    <p>{{ msg }}</p>
                                </div>

                                <form class=" border border-light p-5">

                                    <p class="h4 mb-4 text-center">Sign in</p>

                                    <div class="md-form">
                                        <i class="fas fa-envelope prefix"></i>
                                        <input type="email" id="inputValidationEx" class="form-control"
                                               v-model="loginUser.email">
                                        <label for="inputValidationEx" data-error="wrong" data-success="right">Type
                                            your email</label>
                                    </div>

                                    <div class="md-form">
                                        <i class="fas fa-lock prefix"></i>
                                        <input type="password" id="inputValidationEx2" class="form-control"
                                               v-model="loginUser.password">
                                        <label for="inputValidationEx2" data-error="wrong" data-success="right">Type
                                            your password</label>
                                    </div>

                                    <div class="d-flex justify-content-around">
                                        <div>
                                            <div class="custom-control custom-checkbox">
                                                <input type="checkbox" class="custom-control-input"
                                                       id="defaultLoginFormRemember"
                                                       v-model="loginUser.rememberMe">
                                                <label class="custom-control-label" for="defaultLoginFormRemember">Remember
                                                    me</label>
                                            </div>
                                        </div>
                                    </div>

                                    <button class="btn btn-info btn-block my-4" @click.prevent="submit()">Sign
                                        in
                                    </button>
                                    <div class="alert alert-danger" v-if="error">
                                        <p>{{ error }}</p>
                                    </div>
                                </form>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
    import auth from '../api'


    export default {
        name: 'Login',
        data() {
            return {
                loginUser: {
                    email: '',
                    password: '',
                    rememberMe: false
                },
                error: ''
            }
        },
        props: {
            msg: String,
        },
        methods: {
            submit() {
                auth.login(this, this.loginUser)
            }
        }

    }
</script>