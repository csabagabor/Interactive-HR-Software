import router from '../router'
import axios from 'axios';



const jwt_decode = require('jwt-decode');

export const API_URL = 'http://localhost:8081/api/'
export const METRICS_URL = 'http://localhost:8082/api/'
export const WEBSOCKET_URL = 'http://localhost:8083/'
export const LOGIN_URL = API_URL + 'auth/login'
export const INFO_URL = API_URL + 'account/userinfo'
export const CHANGE_PASSWORD_URL = API_URL + 'account/changePassword'
export const CHANGE_USER_URL = API_URL + 'account/changeUser'

export const USER_URL = API_URL + 'users'
export const ROLE_URL = API_URL + 'roles'
export const COUNTRY_URL = API_URL + 'countries'
export const CITY_URL = API_URL + 'cities'
export const CITY_BY_COUNTRY_URL = API_URL + 'cities/country/'
export const REQUEST_URL = API_URL + 'requests'
export const REQUEST_OPEN_URL = API_URL + 'requests/open'
export const REQUEST_REJECTED_URL = API_URL + 'requests/rejected'
export const REQUEST_ACCEPTED_URL = API_URL + 'requests/accepted'
export const REQUEST_ACCEPT_URL = API_URL + 'requests/accept'
export const REQUEST_REJECT_URL = API_URL + 'requests/reject'
export const TASK_URL = API_URL + 'tasks'
export const PROJECT_URL = API_URL + 'projects'
export const WORKED_TASK = API_URL + 'worked-tasks'
export const TIMECARD = API_URL + 'timecards'

export const REQUEST_OWN_URL = API_URL + 'requests/own'
export const TRANSPORTATION_MEAN_URL = API_URL + 'transportation-means'

export const REQUESTS_PER_MONTH_METRICS_URL = METRICS_URL + 'metrics/requests-per-month'
export const REQUESTS_PER_COUNTRY_METRICS_URL = METRICS_URL + 'metrics/requests-per-country'
export const REQUESTS_PER_TRANSPORTATION_MEAN_METRICS_URL = METRICS_URL + 'metrics/requests-per-transportation-mean'
export const REQUESTS_PER_STATUS_METRICS_URL = METRICS_URL + 'metrics/requests-per-status'
export const REQUESTS_PER_LAPTOP_METRICS_URL = METRICS_URL + 'metrics/requests-per-laptop'
export const REQUESTS_PER_CITY_METRICS_URL = METRICS_URL + 'metrics/requests-per-city'
export const REQUESTS_PER_DURATION_METRICS_URL = METRICS_URL + 'metrics/requests-per-duration'


export const ROLE_ADMIN = 'ROLE_ADMIN'
export const ROLE_USER = 'ROLE_USER'
export const ROLE_MODERATOR = 'ROLE_MODERATOR'
export const ROLE_PM = 'ROLE_PM'
export const ROLE_PAYROLL = 'ROLE_PAYROLL'
export const storageTokenName = 'token'
export const UNAUTHORIZED_MSG = 'Unauthorized'
export const DATE_FORMAT = 'yyyy-MM-dd'

export default {
    async waitForElem(elem){
        while(!$(elem).length) {
            await new Promise(r => setTimeout(r, 100));
        }
    },
    getToken() {
        return localStorage.getItem(storageTokenName)
    },
    getEmail() {
        let token = localStorage.getItem(storageTokenName)
        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.sub
        }
    },

    isAuthenticated() {
        if (!localStorage.getItem(storageTokenName)) {
            return false
        }

        return true
    },

    isAdmin(role) {
        if (role) {
            return role === ROLE_ADMIN
        }

        let token = localStorage.getItem(storageTokenName)

        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.role === ROLE_ADMIN
        }
    },

    isModerator(role) {
        if (role) {
            return role === ROLE_MODERATOR
        }

        let token = localStorage.getItem(storageTokenName)

        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.role === ROLE_MODERATOR
        }
    },
    isPM(role) {
        if (role) {
            return role === ROLE_PM
        }

        let token = localStorage.getItem(storageTokenName)

        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.role === ROLE_PM
        }
    },
    isPayroll(role) {
        if (role) {
            return role === ROLE_PAYROLL
        }

        let token = localStorage.getItem(storageTokenName)

        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.role === ROLE_PAYROLL
        }
    },
    isUser(role) {
        if (role) {
            return role === ROLE_USER
        }

        let token = localStorage.getItem(storageTokenName)

        if (!token) {
            this.logout()
            return null
        } else {
            let jwtDecoded = jwt_decode(token);
            return jwtDecoded.role === ROLE_USER
        }
    },
    async getUserInfo() {
        let token = localStorage.getItem(storageTokenName)

        if (token) {
            let resp = await axios.put(INFO_URL)
            return resp.data
        } else {
            this.logout()
            return null
        }
    },
    async login(context, loginUser) {
        await axios.post(LOGIN_URL, loginUser)
            .then(async resp => {
                localStorage.setItem(storageTokenName, resp.data.token)
                router.push('home')
            })
            .catch(err => {
                if (context) {
                    context.error = err.response.data.message
                }
            });
    },

    logout() {
        localStorage.clear()
        //router.push('login')
        router.push({name: 'login', params: {msg: 'You have been logged out'}})
    },

    showErrorMessage(selectorName, msg) {
        let errorMsg = "Something went wrong"
        if (msg) {
            errorMsg = msg
        }

        if (errorMsg) {
            if (errorMsg instanceof Array) {
                $(selectorName).text('')
                errorMsg.map(t => $(selectorName).append(t + '<br />'));
            } else {
                $(selectorName).text(errorMsg)
            }
            $(selectorName).show()
        }
    }
}
