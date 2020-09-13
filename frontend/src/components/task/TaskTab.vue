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
        name: "TaskTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.TASK_URL,
                model_name: 'Task',
                modelPlural_name: 'Tasks',
                fields: this.createFields(),
                canEdit: !auth.isUser(),
                canDelete: !auth.isUser(),
                canAdd: !auth.isUser(),
            }
        },
        methods: {
            createFields() {
                let fields = new Map()
                fields.set('identifier', 'String')
                fields.set('description', 'Text')
                return fields
            }
        }
    }
</script>

<style scoped>

</style>