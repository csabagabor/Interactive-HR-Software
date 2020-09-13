<template>
    <model-tab :model_url="url"
               :model_get_url="url"
               :model_name="model_name"
               :modelPlural_name="modelPlural_name" :fields="fields"
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
        name: "TransportationMeanTab",
        components: {
            ModelTab,
        },
        data() {
            return {
                url: constants.TRANSPORTATION_MEAN_URL,
                model_name: 'Transportation-Mean',
                modelPlural_name: 'Transportation-Means',
                fields: this.createFields(),
                canEdit: true,
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