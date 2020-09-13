import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/Home.vue'
import Request from '../components/request/RequestTab'
import Login from '../components/Login.vue'

import auth from '../api'
import Profile from "../components/Profile";
import UserTab from "../components/user/UserTab";
import NotFound from "../components/NotFound";
import TransportationMeanTab from "../components/transportation/TransportationMeanTab";
import RoleTab from "../components/role/RoleTab";
import CityTab from "../components/city/CityTab";
import CountryTab from "../components/country/CountryTab";
import MyRequest from "../components/my_request/MyRequestTab";
import AcceptedRequest from "../components/request/AcceptedRequestTab";
import RejectedRequest from "../components/request/RejectedRequestTab";
import ChangePassword from "../components/ChangePassword";
import ProjectTab from "../components/project/ProjectTab";
import TaskTab from "../components/task/TaskTab";
import MyCalendarTab from "../components/calendar/MyCalendarTab";
import PayrollCalendarsTab from "../components/calendar/PayrollCalendarsTab";
import CalendarsTab from "../components/calendar/CalendarsTab";

Vue.use(Router)

const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: 'home_simple',
            component: Home
        },
        {
            path: '/home',
            name: 'home',
            component: Home
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            props: true
        },
        {
            path: '/profile',
            name: 'profile',
            component: Profile
        },
        {
            path: '/change-password',
            name: 'change-password',
            component: ChangePassword
        },
        {
            path: '/not-found',
            name: 'not-found',
            component: NotFound
        },
        {
            path: '/my-requests',
            name: 'my-requests',
            component: MyRequest,
            beforeEnter: (to, from, next) => {
                isUser(next)
            }
        },
        {
            path: '/calendar',
            name: 'calendar',
            component: MyCalendarTab,
            beforeEnter: (to, from, next) => {
                isUser(next)
            }
        },
        //for admins
        {
            path: '/users',
            name: 'users',
            component: UserTab,
            beforeEnter: (to, from, next) => {
                isAdmin(next)
            }
        },
        {
            path: '/roles',
            name: 'roles',
            component: RoleTab,
            beforeEnter: (to, from, next) => {
                isAdmin(next)
            }
        },//for PMs
        {
            path: '/timecards',
            name: 'timecards',
            component: CalendarsTab,
            beforeEnter: (to, from, next) => {
                isPM(next)
            }
        },
        //for Payrolls
        {
            path: '/timecards-payroll',
            name: 'timecards-payroll',
            component: PayrollCalendarsTab,
            beforeEnter: (to, from, next) => {
                isPayroll(next)
            }
        },
        //for moderators
        {
            path: '/transportation-means',
            name: 'transportation-means',
            component: TransportationMeanTab,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }

        },
        {
            path: '/countries',
            name: 'countries',
            component: CountryTab,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }
        },
        {
            path: '/cities',
            name: 'cities',
            component: CityTab,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }
        },
        {
            path: '/request',
            name: 'request',
            component: Request,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }
        },
        {
            path: '/projects',
            name: 'projects',
            component: ProjectTab
        },
        {
            path: '/accepted-request',
            name: 'accepted-request',
            component: AcceptedRequest,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }
        },
        {
            path: '/rejected-request',
            name: 'rejected-request',
            component: RejectedRequest,
            beforeEnter: (to, from, next) => {
                isModerator(next)
            }
        },
        //project manager
        {
            path: '/tasks/:id',
            name: 'tasks',
            component: TaskTab
        },
        //not found, when none of the above matches:
        {
            path: "*",
            name: 'NotFound',
            component: NotFound
        }
    ]
})


function isUser(next) {
    if (auth.isUser()) {
        next()
    } else {
        next('not-found')
    }
}

function isPM(next) {
    if (auth.isPM()) {
        next()
    } else {
        next('not-found')
    }
}

function isPayroll(next) {
    if (auth.isPayroll()) {
        next()
    } else {
        next('not-found')
    }
}

function isAdmin(next) {
    if (auth.isAdmin()) {
        next()
    } else {
        next('not-found')
    }
}

function isModerator(next) {
    if (auth.isModerator()) {
        next()
    } else {
        next('not-found')
    }
}

router.beforeEach((to, from, next) => {

    let go = false

    //if logged in
    if (auth.isAuthenticated()) {
        if (to.fullPath === '/login') {
            go = true
            next('/home');
        }
    } else {
        if (to.fullPath === '/forgot-password') {
            go = true
            next();
        } else if (to.fullPath !== '/login') {
            go = true
            next('/login');
        }
    }

    if (!go) {
        next();
    }

});

export default router;
