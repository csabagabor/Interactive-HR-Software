<template>
    <div id="model-form">

        <button id="add-btn" v-if="model_name !== 'WorkedTask'" type="button" class="btn btn-success btn-sm"
                @click="showModal = true, clearStatus() ">Add new
            {{model_name}}
        </button>

        <div v-if="showModal">
            <div class="modal-mask">
                <div class="modal-wrapper">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Add new {{model_name}}</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true" @click="showModal = false">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <!--          BODY-->
                                <form class="text-center border border-light p-9">

                                    <div :key="field.id" v-for="[field, type] in fields">
                                        <label v-if="type !=='Hardcoded'"
                                               style="padding-left: 15px; margin-top: 15px;width: 220px">{{field.uppercase()}}:</label>
                                        <div style="margin-left:5px; display: inline-block;width: 200px"
                                             v-if="type==='String'">
                                            <input type="text" class="form-control"
                                                   v-model="model[field]" @keypress="clearStatus">
                                        </div>
                                        <div style="margin-left:5px; display: inline-block;width: 200px"
                                             v-if="type==='Text'">
                                            <textarea :id="'text-' +field" type="text" class="form-control md-textarea"
                                                      length="120" rows="3"
                                                      v-model="model[field]" @click = "textClick(field, 'text-' +field)" @keypress="clearStatus">
                                              </textarea>
                                        </div>

                                        <div style="margin-left:5px; display: inline-block" v-else-if="type==='Array'">

                                            <button :id="'drop-' +field" class="btn btn-primary btn-sm dropdown-toggle"
                                                    type="button"
                                                    data-toggle="dropdown"
                                                    aria-haspopup="true" aria-expanded="false">{{model[field]}}
                                            </button>

                                            <div :id="'drop-div-'+field" class="dropdown-menu">
                                                <input :id="'drop-inp-'+field"
                                                       style="margin-left: 5px;margin-right: 5px" type="text"
                                                       placeholder="Search.."
                                                       @input="filterFunction('drop-div-'+field, 'drop-inp-'+field)">
                                                <li :key="obj.id" v-for="obj in arrays.get(field)"
                                                   :class="'interactive-dropdown-'+field + ' dropdown-item'"
                                                   @click.prevent="dropdownEvent(field, obj)">{{obj}}</li>
                                            </div>
                                        </div>

                                        <div style="margin-left:5px; display: inline-block" v-else-if="type==='Boolean'"
                                             class="custom-control custom-checkbox">
                                            <input type="checkbox" class="custom-control-input"
                                                   :id="'checkbox-' + field"
                                                   v-model="model[field]">
                                            <label class="custom-control-label" :for="'checkbox-' + field"></label>
                                        </div>

                                        <div style="margin-left:5px; display: none;width: 200px"
                                             v-else-if="type==='Hardcoded'">
                                            <input disabled type="text" class="form-control" v-model="model[field]">
                                        </div>

                                        <div style="margin-left:5px; display: inline-block" v-else-if="type==='Date'">
                                            <datetime :format="dateFormat" v-if="field==='startDate'"
                                                      v-model="model[field]"
                                                      :min-datetime="new Date().toISOString()"></datetime>
                                            <datetime :format="dateFormat" v-else-if="field==='endDate'
                                             && model.hasOwnProperty('startDate')"
                                                      v-model="model[field]"
                                                      :min-datetime="model['startDate']"></datetime>
                                            <datetime :format="dateFormat" v-else v-model="model[field]"
                                                      :min-datetime="new Date().toISOString()"></datetime>
                                        </div>


                                        <input style="display: none"
                                               id="hidden-field"
                                               @click="changeHidden()"
                                               >
                                    </div>

                                    <p style="display: none;" :id="errorElement" class="alert alert-danger"></p>
                                    <p style="display: none;" :id="successElement" class="alert alert-success">
                                        {{model_name}} successfully added</p>

                                </form>
                                <!--          BODY-->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" @click="showModal = false">Cancel
                                </button>
                                <button v-if="model_name !== 'Request'" id="save-btn" type="button"
                                        class="btn btn-primary" @click="handleSubmit">Save
                                </button>
                                <button v-else id="send-btn" type="button" class="btn btn-primary"
                                        @click="handleSubmit">SEND
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>

<script>

    import * as constants from '../../api'
    import {Datetime} from "vue-datetime";

    export default {
        name: 'model-form',
        data() {
            return {
                showModal: false,
                success: false,
                successElement: `${this.model_name}-success`.replace(/\s/g, ""),
                errorElement: `${this.model_name}-error`.replace(/\s/g, ""),
                model: {},
                dateFormat: constants.DATE_FORMAT
            }
        },
        components: {
            datetime: Datetime
        },
        props: {
            model_name: String,
            fields: Map,
            arrays: Map
        },
        mounted() {
            this.createEmptyModel()
        },
        methods: {
            textClick(field, id){
                if(this.model_name === 'Request'){
                    this.model[field] = $("#"+id).val();
                }
            },
            changeHidden(){
                this.model['hidden'] = 'hidden';
            },
            filterFunction(el, inp) {//taken from https://www.w3schools.com/howto/howto_js_filter_dropdown.asp
                var input, filter, a, i;
                input = document.getElementById(inp);
                filter = input.value.toUpperCase();
                var div = document.getElementById(el);
                a = div.getElementsByTagName("li");
                for (i = 0; i < a.length; i++) {
                    let txtValue = a[i].textContent || a[i].innerText;
                    console.log(txtValue)
                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                        a[i].style.display = "";
                    } else {
                        a[i].style.display = "none";
                        console.log("hidden")
                    }
                }
            },
            handleSubmit() {
                $(`#${this.errorElement}`).hide()
                this.$emit('add:model', this.model)
                this.createEmptyModel()
            },
            clearStatus() {
                $(`#${this.errorElement}`).hide()
            },
            createEmptyModel() {
                let model = {}
                for (let [field, type] of this.fields) {
                    if (type === 'Hardcoded') {
                        model[field] = this.arrays.get(field)
                    } else {
                        model[field] = ''
                    }
                }
                model.hidden = ''

                this.model = model
            },
            dropdownEvent(field, obj) {
                //hack used to prevent choosing a city from a different country
                if (field === 'countryName' && this.model.hasOwnProperty('cityName')) {
                    this.model['cityName'] = ''
                }

                this.model[field] = obj
                this.$emit('choose:array', {field: field, obj: obj})
            }
        }
    }
</script>

<style scoped>
    .text-center {
        text-align: left !important;
    }
</style>
