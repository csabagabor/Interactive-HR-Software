<template>
    <div id="profile" style="width: 1000px;" class="my-container">

        <div class="card" style="margin-left: 350px">

            <div class="card-header view view-cascade gradient-card-header blue-gradient  py-2 d-flex justify-content-between align-items-center">

            </div>
            <div class="card-body">

                <div class="container">
                    <h1></h1>
                    <hr>
                    <div class="row">

                        <!-- edit form column -->
                        <div class="col-md-9 personal-info">
                            <div id="serverMessage"></div>
                            <br/>
                            <h3>Personal info</h3>

                            <form id="profileForm" method="POST" role="form" class="form-horizontal form-inline">

                                <div class="form-group">
                                    <label style="width: 200px" for="firstName"><span></span> First Name</label>
                                    <input style="margin-left: 15px;width: 200px" v-model="model.firstName" type="text" class="form-control"
                                           name="firstName" id="firstName" @keypress="clearStatus">
                                </div>
                                <div style="margin-top: 15px" class="form-group">
                                    <label style="width: 200px" for="lastName"><span></span> Last Name</label>
                                    <input style="margin-left: 15px;width: 200px" v-model="model.lastName" type="text" class="form-control"
                                           name="lastName" id="lastName" @keypress="clearStatus">
                                </div>
                                <div style="margin-top: 15px" class="form-group">
                                    <label style="width: 200px" for="email"><span></span> Email</label>
                                    <input style="margin-left: 15px;width: 200px" v-model="model.email" type="text" class="form-control"
                                           name="email" id="email" @keypress="clearStatus">
                                </div>

                                <div style="margin-top: 15px" class="form-group">

                                        <input style="display: inline-block" @click.prevent="changePassword" class="btn btn-primary btn"
                                               value="Save Changes">
                                        <span></span>
                                        <input style="display: inline-block" @click.prevent="reset" class="btn btn-default" value="Cancel">

                                </div>

                            </form>

                            <p style="display: none;" id="profile-error" class="alert alert-danger"></p>
                            <p style="display: none;" id="profile-success" class="alert alert-success"></p>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import auth, * as constants from '../api'
    import axios from 'axios';

    export default {
        name: "Profile",
        data() {
            return {
                model: {
                    firstName: '',
                    lastName: '',
                    email: '',
                }
            }
        },
        mounted() {
            this.getUserInfo()
        },
        methods: {
            async getUserInfo() {
                let userInfo = await auth.getUserInfo()
                this.model.firstName = userInfo.firstName
                this.model.lastName = userInfo.lastName
                this.model.email = userInfo.email
                this.model.roleName = userInfo.roleName
            },
            changePassword() {
                this.clearStatus()
                axios.put(constants.CHANGE_USER_URL, this.model)
                    .then(resp => {
                        $("#profile-success").show();
                        $("#profile-success").text("Information successfully updated");
                    })
                    .catch(err => {
                        auth.showErrorMessage("#profile-error", err.response.data.message)
                    })
            },
            reset() {
                this.model.firstName = ''
                this.model.lastName = ''
                this.model.email = ''
            },
            clearStatus() {
                $("#profile-error").hide();
                $("#profile-success").hide();
            }
        }
    }
</script>

<style scoped>
    .text-center{
        text-align: left !important;
    }
</style>