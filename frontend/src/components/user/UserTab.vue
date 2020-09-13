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
        name: "UserTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.USER_URL,
                model_name: 'User',
                modelPlural_name: 'Users',
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
                fields.set('firstName', 'String')
                fields.set('lastName', 'String')
                fields.set('email', 'String')
                fields.set('roleName', 'Array')
                return fields
            },
            async createArrays() {
                let roles = []

                //get roles
                await axios.get(constants.ROLE_URL)
                    .then(resp => roles = resp.data.map(role => role.name))
                    .catch(err => console.log(err))

                this.arrays.set('roleName', roles)
            }
        },
        mounted() {
            this.createArrays()
        }
    }
</script>

<style scoped>

</style>