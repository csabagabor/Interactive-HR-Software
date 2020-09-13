<template>
    <div id="home" class="my-container">

        <div class="card">
            <div class="card-header view view-cascade gradient-card-header blue-gradient  py-2 d-flex justify-content-between align-items-center">
                About
            </div>
            <div class="card-body">

                <h4 class="card-title"><a>HR management system</a></h4>
                <p v-if="auth.isAdmin()" class="card-text">Hi admin, <br> your role is to modify users/roles</p>
                <p v-if="auth.isUser()" class="card-text">Hi user, <br> you can send Business Trip requests and fill in your timecard using
                    this app</p>
                <p v-if="auth.isModerator()" class="card-text">Hi moderator, <br> your role is to accept/reject
                    requests,
                    add cities, countries, transportation means<br></p>
                <p v-if="auth.isPM()" class="card-text">Hi project manager, <br> your role is to accept/reject
                    timecards and edit tasks<br></p>
                <p v-if="auth.isPayroll()" class="card-text">Hi Payroll, <br> your role is to view
                    timecards and print them<br></p>
            </div>
        </div>

        <div v-if="auth.isModerator()">
            <simple-card header="Number of Requests based on year/months">
                <monthly-request-chart></monthly-request-chart>
            </simple-card>
            <simple-card header="Number of Requests based on countries">
                <country-request-chart></country-request-chart>
            </simple-card>
            <simple-card header="Number of Requests based on transportation means">
                <TransportationMeanRequestChart></TransportationMeanRequestChart>
            </simple-card>
            <simple-card header="Laptop requests">
                <laptop-request-chart></laptop-request-chart>
            </simple-card>
            <simple-card header="Number of Requests based on cities">
                <city-chart></city-chart>
            </simple-card>
            <simple-card header="Number of Requests based on status">
                <status-chart></status-chart>
            </simple-card>
            <simple-card header="Number of Requests based on duration">
                <duration-request-chart></duration-request-chart>
            </simple-card>

        </div>


    </div>

</template>

<script>
    import auth from '../api'
    import MonthlyRequestChart from "./chart/MonthlyRequestChart";
    import CountryRequestChart from "./chart/CountryRequestChart";
    import LaptopRequestChart from "./chart/LaptopRequestChart";
    import TransportationMeanRequestChart from "./chart/TransportationMeanRequestChart";
    import SimpleCard from "./SimpleCard";
    import CityChart from "./chart/CityRequestChart";
    import StatusChart from "./chart/StatusRequestChart";
    import DurationRequestChart from "./chart/DurationRequestChart";

    export default {
        name: 'Home',
        data() {
            return {
                quote: '',
                auth: auth
            }
        },
        components: {
            StatusChart,
            CityChart,
            MonthlyRequestChart,
            CountryRequestChart,
            LaptopRequestChart,
            TransportationMeanRequestChart,
            SimpleCard,
            DurationRequestChart
        },
        methods: {
            logout() {
                auth.logout()
            }
        }

    }
</script>
<style>

</style>