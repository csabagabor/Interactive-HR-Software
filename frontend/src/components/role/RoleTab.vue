<template>
    <model-tab :model_url="url" :model_name="model_name"
               :modelPlural_name="modelPlural_name"
               :fields="fields"
               :model_get_url="url"
               :can-edit="canEdit"
               :can-delete="canDelete"
               :canAdd="canAdd"
    >
    </model-tab>
</template>

<script>
    import ModelTab from "../crud/ModelTab";
    import * as constants from '../../api'


    export default {
        name: "RoleTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.ROLE_URL,
                model_name: 'Role',
                modelPlural_name: 'Roles',
                fields: this.createFields(),
                canEdit: false,
                canDelete: true,
                canAdd: true,
            }
        },
        methods: {
            createFields() {
                let fields = new Map()
                fields.set('name', 'String')
                return fields
            }
        }
    }
</script>

<style scoped>

</style>