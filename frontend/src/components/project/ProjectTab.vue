<template>
    <model-tab :model_url="url"
               :model_get_url="url"
               :model_name="model_name" :modelPlural_name="modelPlural_name"
               :fields="fields"
               :can-edit="canEdit"
               :can-delete="canDelete"
               :canAdd="canAdd"
    >
    </model-tab>
</template>

<script>
    import ModelTab from "../crud/ModelTab";
    import auth, * as constants from '../../api'

    export default {
        name: "ProjectTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.PROJECT_URL,
                model_name: 'Project',
                modelPlural_name: 'Projects',
                fields: this.createFields(),
                canEdit: auth.isModerator(),
                canDelete: auth.isModerator(),
                canAdd: auth.isModerator(),
            }
        },
        methods: {
            createFields() {
                let fields = new Map()
                fields.set('title', 'String')
                fields.set('description', 'Text')
                return fields
            }
        }
    }
</script>

<style scoped>

</style>