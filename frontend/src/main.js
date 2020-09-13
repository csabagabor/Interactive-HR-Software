import Vue from 'vue'
import App from './App.vue'

import router from './router'
// import 'bootstrap'
// import 'bootstrap/dist/css/bootstrap.min.css'
import axios from "axios";
import * as constants from './api'
import auth from './api'
import 'vue-datetime/dist/vue-datetime.css'
import Datetime from 'vue-datetime'

import FullCalendar from 'vue-full-calendar';
Vue.use(FullCalendar);
Vue.use(Datetime)
window.$ = require('jquery')
window.JQuery = require('jquery')

import Toasted from 'vue-toasted';
Vue.use(Toasted, {
    iconPack : 'material'
});

//uppercase first letter
String.prototype.uppercase = function (str) {
    let s = str ? this : this.replace(/(?:(?:^|\n)\s+|\s+(?:$|\n))/g, '').replace(/\s+/g, ' ');
    return s.length > 0 ? s.charAt(0).toUpperCase() + s.slice(1) : s;
}

axios.interceptors.request.use(function (config) {

    if (localStorage.getItem(constants.storageTokenName)) {
        config.headers.Authorization = 'Bearer ' + localStorage.getItem(constants.storageTokenName);
    }

    return config;
});

axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    //if response is unauthorized, log out user
    let status = error.response.status

    if (status === 401) {
        let msg = error.response.data.message

        if (msg.startsWith(constants.UNAUTHORIZED_MSG)) {
            //token is expired or invalid, log out
            //token can be invalid when user data was changed for example
            auth.logout()
        } else {//for example access denied
            //else go back
            router.go(-1)
        }
    } else if (status === 403) {//Spring security returns this Status code(Forbidden)
        router.go(-1)
    }

    return Promise.reject(error);
});


Vue.config.productionTip = false

new Vue({el: '#app', router, render: h => h(App)})
