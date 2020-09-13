<template>
    <div id="model-tab" class="my-container container">
        <div v-if="model_name === 'WorkedTask'" style="height:40%; width:60%;" id="calendar"></div>

            <div v-if="canAdd" class="row h-100 justify-content-center align-items-center">
                <model-form @add:model="addModels" :model_name="model_name"
                            :fields="fields2" :arrays="arrays"
                            @choose:array="dropdownEvent"
                >
                </model-form>
            </div>
            <div v-if="model_name!=='WorkedTask'" class="row h-100 justify-content-center align-items-center">
                <model-table :modelPlural="modelPlural_name"
                             :models="models" @delete:model="deleteModel"
                             @edit:model="editModel" :fields="fields"
                             :canEdit="canEdit"
                             :canDelete="canDelete"
                             :arrays="arrays"
                             @choose:array="dropdownEvent"
                >

                </model-table>
            </div>
        </div>

</template>

<script>
    import modelTable from './ModelTable.vue'
    import modelForm from './ModelForm.vue'
    import axios from 'axios';
    import auth, * as constants from '../../api'

    export default {
        name: "model-tab",
        components: {
            modelTable,
            modelForm,
        },
        props: {
            model_url: String,
            model_get_url: String,
            model_name: String,
            modelPlural_name: String,
            fields: Map,
            arrays: Map,
            canEdit: Boolean,
            canDelete: Boolean,
            canAdd: Boolean
        },
        data() {
            return {
                models: [],
                successElement: `#${this.model_name}-success`.replace(/\s/g, ""),
                errorElement: `#${this.model_name}-error`.replace(/\s/g, ""),

                successEditElement: `#${this.modelPlural_name}-edit-success`.replace(/\s/g, ""),
                errorEditElement: `#${this.modelPlural_name}-edit-error`.replace(/\s/g, ""),
                fields2: this.createFieldsMap()
            }
        },

        mounted() {
            this.getModels()
        },
        methods: {
            createFieldsMap() {
                let map = new Map(this.fields)
                map.delete('status') //status shouldn't appear in the input form
                return map
            },
            dropdownEvent(passObj) {
                this.$emit('choose:array', passObj)
            },
            async getModels() {

                var getUrl = this.model_get_url

                if (this.model_name === 'Task') {
                    getUrl = constants.API_URL + "projects/tasks/" + this.$route.params.id
                }

                axios.get(getUrl)
                    .then(resp => this.models = resp.data)
                    .catch(err => console.log(err))
            },

            async addModels(model) {

                if (this.model_name === 'Task') {
                    model.projectId = this.$route.params.id
                }

                axios.post(this.model_url, model)
                    .then(resp => {
                        this.models = [...this.models, resp.data]
                        $(this.successElement).show()
                    })
                    .catch(err => {
                        auth.showErrorMessage(this.errorElement, err.response.data.message)
                    })
            },

            async editModel(id, updatedModel, cachedModel) {
                $(this.errorEditElement).hide()
                $(this.successEditElement).hide()

                axios.put(this.model_url + '/' + id, updatedModel)
                    .then(resp => {
                        this.models = this.models.map(model => model.id === id ? resp.data : model)
                        $(this.successEditElement).show()
                    })
                    .catch(err => {
                        //revert back if error
                        this.models = this.models.map(model => model.id === id ? cachedModel : model)
                        auth.showErrorMessage(this.errorEditElement, err.response.data.message)
                    })
            },

            async deleteModel(id) {
                axios.delete(this.model_url + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => console.log(err))
            },
        },
    }
</script>

<style>

</style>
