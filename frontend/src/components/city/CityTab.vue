<template>
    <model-tab :model_url="url"
               :model_get_url="url"
               :model_name="model_name"
               :modelPlural_name="modelPlural_name" :fields="fields"
               :arrays="arrays"
               :can-edit="canEdit"
               :can-delete="canDelete"
               :canAdd="canAdd"
    >
    </model-tab>
</template>

<script>
    import ModelTab from "../crud/ModelTab";
    import * as constants from '../../api'
    import axios from 'axios';

    export default {
        name: "CityTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.CITY_URL,
                model_name: 'City',
                modelPlural_name: 'Cities',
                fields: this.createFields(),
                arrays: new Map(),
                canEdit: true,
                canDelete: true,
                canAdd: true
            }
        },
        methods: {
            createFields() {
                let fields = new Map()
                fields.set('name', 'String')
                fields.set('countryName', 'Array')
                return fields
            },
            async createArrays() {
                let countries = []
                //get countries
                await axios.get(constants.COUNTRY_URL)
                    .then(resp => countries = resp.data.map(country => country.name))
                    .catch(err => console.log(err))

                this.arrays.set('countryName', countries)
            }
        },
        mounted() {
            this.createArrays()
        }
    }
</script>

<style scoped>

</style>