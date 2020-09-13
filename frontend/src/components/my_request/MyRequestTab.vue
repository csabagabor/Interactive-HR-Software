<template>
    <model-tab :model_url="url"
               :model_get_url="urlGetAllUrl"
               :model_name="model_name"
               :modelPlural_name="modelPlural_name" :fields="fields"
               :arrays="arrays"
               @choose:array="updateCities"
               :canEdit="canEdit"
               :canDelete="canDelete"
               :canAdd="canAdd"
    >
    </model-tab>
</template>

<script>
    import ModelTab from "../crud/ModelTab";
    import * as constants from '../../api'
    import auth from '../../api'
    import axios from 'axios';

    export default {
        name: "MyRequestTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.REQUEST_URL,
                urlGetAllUrl: constants.REQUEST_OWN_URL,
                model_name: 'Request',
                modelPlural_name: 'My Requests',
                fields: this.createFields(),
                arrays: new Map([['userEmail', auth.getEmail()]]),
                canEdit: false,
                canDelete: true,
                canAdd: true,
            }
        },
        methods: {
            createFields() {
                let fields = new Map()
                fields.set('startDate', 'Date')
                fields.set('endDate', 'Date')
                fields.set('needOfPhone', 'Boolean')
                fields.set('needOfVpn', 'Boolean')
                fields.set('needOfLaptop', 'Boolean')
                fields.set('details', 'Text')
                //fields.set('userEmail', 'Hardcoded')
                fields.set('transportationMeanName', 'Array')
                fields.set('countryName', 'Array')
                fields.set('cityName', 'Array')
                fields.set('status', 'String')
                return fields
            },
            async createArrays() {
                this.arrays = new Map()
                //get countries
                let countries = []
                await axios.get(constants.COUNTRY_URL)
                    .then(resp => countries = resp.data.map(obj => obj.name))
                    .catch(err => console.log(err))

                //get cities
                let cities = []
                await axios.get(constants.CITY_URL)
                    .then(resp => cities = resp.data.map(obj => obj.name))
                    .catch(err => console.log(err))

                //get cities
                let transportationMeans = []
                await axios.get(constants.TRANSPORTATION_MEAN_URL)
                    .then(resp => transportationMeans = resp.data.map(obj => obj.name))
                    .catch(err => console.log(err))

                this.arrays.set('countryName', countries)
                this.arrays.set('cityName', cities)
                this.arrays.set('transportationMeanName', transportationMeans)
                this.arrays.set('userEmail', auth.getEmail())
            },
            async updateCities(passObj) {
                let field = passObj.field
                let country = passObj.obj

                if (field === 'countryName') {
                    let cities = []
                    await axios.get(constants.CITY_BY_COUNTRY_URL + country)
                        .then(resp => cities = resp.data.map(obj => obj.name))
                        .catch(err => console.log(err))

                    //Arrays and Maps are not reactive
                    this.arrays = new Map(this.arrays.set('cityName', cities))
                }
            }
        },
        mounted() {
            this.createArrays()
        }
    }
</script>

<style scoped>

</style>