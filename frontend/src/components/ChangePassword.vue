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
                            <h3>Change Password</h3>

                            <form id="profileForm" method="POST" role="form" class="form-horizontal  form-inline">

                                <div class="form-group">
                                    <label style="width: 200px" for="OldPassword"><span>*</span> OldPassword</label>
                                    <input style="margin-left: 15px" v-model="model.oldPassword" type="password" class="form-control"
                                           name="OldPassword" id="OldPassword" @keypress="clearStatus">
                                </div>
                                <div style="margin-top: 15px" class="form-group">
                                    <label  style="width: 200px" for="Password1"><span>*</span> New Password</label>
                                    <input style="margin-left: 15px" v-model="model.newPassword" type="password" class="form-control"
                                           name="Password1" id="Password1" @keypress="clearStatus">
                                </div>
                                <div style="margin-top: 15px" class="form-group">
                                    <label  style="width: 200px" for="Password2"><span>*</span> New Password again</label>
                                    <input style="margin-left: 15px" v-model="model.newPasswordAgain" type="password" class="form-control"
                                           name="Password2" id="Password2" @keypress="clearStatus">
                                </div>

                                <div style="margin-top: 15px" class="form-group">
                                        <input @click.prevent="changePassword" class="btn btn-primary"
                                               value="Save Changes">
                                        <span></span>
                                        <input style="margin-left: 15px" @click.prevent="reset" class="btn btn-default" value="Cancel">
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
        name: "ChangePassword",
        data() {
            return {
                model: {
                    oldPassword: '',
                    newPassword: '',
                    newPasswordAgain: '',
                }
            }
        },
        methods: {
            changePassword() {
                this.clearStatus()

                if (this.model.newPassword !== this.model.newPasswordAgain) {
                    $("#profile-error").show();
                    $("#profile-error").text("Passwords do not match");
                } else {
                    axios.put(constants.CHANGE_PASSWORD_URL, this.model)
                        .then(resp => {
                            $("#profile-success").show();
                            $("#profile-success").text("Password successfully updated");
                        })
                        .catch(err => {
                            auth.showErrorMessage("#profile-error", err.response.data.message)
                        })
                }
            },
            reset() {
                this.model = {
                    oldPassword: '',
                    newPassword: '',
                    newPasswordAgain: '',
                }
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