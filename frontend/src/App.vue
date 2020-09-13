<template>
    <div id="app">
        <svg display="none" viewBox="11.8 9 16 22" class="mouse"><path d="M20,21l4.5,8l-3.4,2l-4.6-8.1L12,29V9l16,12H20z"></path></svg>

        <nav v-if="auth.isAuthenticated()"
             class="navbar fixed-top navbar-expand-lg navbar-light white scrolling-navbar">
            <div class="container navbar-container">
                <ul class="nav navbar-nav">

                    <li class="nav-item">
                        <router-link class="nav-link" to="/home">Home</router-link>
                    </li>

                    <ul class="navbar-nav ml-auto">
                        <li id="must-show-li" class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-1" data-toggle="dropdown"
                               aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-tasks"></i> Action </a>
                            <div v-if="auth.isModerator()" class="dropdown-menu dropdown-menu-right dropdown-info"
                                 aria-labelledby="navbarDropdownMenuLink-1">
                                <router-link class="dropdown-item" to="/request">Accept/Deny Requests</router-link>
                                <router-link class="dropdown-item" to="/accepted-request">Accepted Requests
                                </router-link>
                                <router-link class="dropdown-item" to="/rejected-request">Rejected Requests
                                </router-link>
                                <router-link class="dropdown-item" to="/countries">Modify Countries</router-link>
                                <router-link class="dropdown-item" to="/cities">Modify Cities</router-link>
                                <router-link class="dropdown-item" to="/transportation-means">Modify Transportation</router-link>
                                <router-link class="dropdown-item" to="/projects">Modify Projects</router-link>

                            </div>
                            <div v-else-if="auth.isAdmin()" class="dropdown-menu dropdown-menu-right dropdown-info"
                                 aria-labelledby="navbarDropdownMenuLink-1">
                                <router-link class="dropdown-item" to="/users">Users</router-link>
                            </div>
                            <div v-else-if="auth.isUser()" id="nav-dropdown-user" class="dropdown-menu dropdown-menu-right dropdown-info"
                                 aria-labelledby="navbarDropdownMenuLink-1">
                                <li id="request-li"><router-link class="dropdown-item" to="/my-requests">My Business Trip Requests</router-link></li>
                                <li id="calendar-li"><router-link class="dropdown-item" to="/calendar">Calendar</router-link></li>
                                <router-link class="dropdown-item" to="/projects">Projects and Tasks</router-link>
                            </div>
                            <div v-else-if="auth.isPM()" class="dropdown-menu dropdown-menu-right dropdown-info"
                                 aria-labelledby="navbarDropdownMenuLink-1">
                                <router-link class="dropdown-item" to="/projects">Projects</router-link>
                                <router-link class="dropdown-item" to="/timecards">Timecards</router-link>
                            </div>
                            <div v-else-if="auth.isPayroll()" class="dropdown-menu dropdown-menu-right dropdown-info"
                                 aria-labelledby="navbarDropdownMenuLink-1">
                                <router-link class="dropdown-item" to="/timecards-payroll">Accepted Timecards</router-link>
                            </div>
                        </li>
                    </ul>
                </ul>

                <ul class="navbar-nav ml-auto">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-4" data-toggle="dropdown"
                           aria-haspopup="true" aria-expanded="false">
                            <i class="fas fa-user"></i> Profile </a>
                        <div class="dropdown-menu dropdown-menu-right dropdown-info"
                             aria-labelledby="navbarDropdownMenuLink-4">
                            <router-link class="dropdown-item" to="/profile">My profile</router-link>
                            <router-link class="dropdown-item" to="/change-password">Change password</router-link>
                            <div class="dropdown-divider"></div>
                            <router-link class="dropdown-item" @click.native="logout()" to="/login">Logout
                            </router-link>
                        </div>
                    </li>
                    <li class="nav-item">

                    </li>
                </ul>
            </div>
        </nav>

        <div v-if="auth.isAuthenticated()">
            <web-socket></web-socket>
        </div>

        <div class="router-container">
            <router-view/>
        </div>

    </div>
</template>

<script>
    import auth from './api'
    import webSocket from "./websocket/WebSocket";


    export default {
        name: 'app',
        data() {
            return {
                auth: auth
            }
        },
        methods: {
            logout() {
                auth.logout()
            },
        }, components: {
            webSocket
        },
    }
</script>
<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
        background-color: #EDEDEE;
    }

    #app {
        display: flex;
        height: 100%;
        font-family: Arial, Helvetica, sans-serif;
        background-color: #EDEDEE;
    }

    .my-container {
        margin-top: 90px
    }

    /*global modal style*/
    /*modal  */
    .modal-mask {
        position: absolute;
        z-index: 9998;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, .5);
        display: table;
        transition: opacity .3s ease;
        overflow: scroll;
    }

    .modal-wrapper {
        display: table-cell;
        vertical-align: middle;
    }

    .space-input {
        margin-top: 20px
    }

    /*table*/


    .table-wrapper {
        max-height: 600px;
        max-width: 1800px;
        overflow: auto;
    }

    .router-container{
        margin: 0 auto;
        width: 80%;
    }

    .navbar-container{
        width: 1800px !important;
    }

    .container{
        max-width: 1800px !important;
    }

    td.fc-sat, td.fc-sun {
        background-color: grey !important;
    }

    svg {
        z-index: 99999;
        width: 30px;
        top: 0px;
        left: 0px;
        position: fixed;
    }
</style>
