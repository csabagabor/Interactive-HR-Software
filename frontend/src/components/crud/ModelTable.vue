<template>
    <div id="model-table" class="my-container">
        <div class="card card-cascade narrower">

            <div class="view view-cascade gradient-card-header blue-gradient narrower py-2 d-flex justify-content-between align-items-center">
                <a @click.prevent class="white-text mx-3">{{modelPlural}}</a>
            </div>

            <div class="px-4">
                <div class="table-wrapper">
                    <table class="table table-hover mb-0 text-center">
                        <thead>
                        <tr>
                            <th :key="field.id" v-if="type !=='Hardcoded' && field !== 'userEmail'" v-for="[field, type] in fields" class="th-lg">
                                {{field}}
                                <i class="fa fa-fw fa-sort"></i></th>
                            <th></th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr :key="model.id" v-for="model in models">

                            <td v-for="[field, type] in fields" v-if="editing === model.id && type !== 'Hardcoded'">
                                <div v-if="type==='String'">
                                    <input size="4" type="text" class="form-control"
                                           v-model="model[field]" @keypress="clearStatus">
                                </div>

                                <div v-if="type==='Text'">
                                    <textarea type="text" class="form-control md-textarea"
                                              length="120" rows="3"
                                              v-model="model[field]" @keypress="clearStatus">
                                              </textarea>
                                </div>

                                <div v-else-if="type==='Array'">
                                    <button class="btn btn-primary dropdown-toggle btn-sm" type="button"
                                            data-toggle="dropdown"
                                            aria-haspopup="true" aria-expanded="false">{{model[field]}}
                                    </button>

                                    <div class="dropdown-menu">
                                        <a :key="obj.id" v-for="obj in arrays.get(field)" class="dropdown-item"
                                           @click.prevent="dropdownEvent(model, field, obj)">{{obj}}</a>
                                    </div>
                                </div>

                                <div v-else-if="type==='Boolean'" class="custom-control custom-checkbox">
                                    <input type="checkbox" class="custom-control-input"
                                           :id="'checkbox-' + field"
                                           v-model="model[field]">
                                    <label class="custom-control-label" :for="'checkbox-' + field"></label>
                                </div>

                                <div v-else-if="type==='Hardcoded'">
                                    <input style="display: none" type="text" class="form-control" v-model="model[field]">
                                </div>

                                <!--                                no minimum date as in the form-->
                                <div v-else-if="type==='Date'">
                                    <datetime value-zone="local" type="date" :format="dateFormat" v-if="field==='endDate'
                                             && model.hasOwnProperty('startDate')"
                                              v-model="model[field]"
                                              :min-datetime="new Date((new Date(model['startDate'])).
                                              getTime() + ( 3600 * 1000 * 24)).toISOString()">

                                    </datetime>
                                    <datetime value-zone="local" type="date" :format="dateFormat" v-else
                                              v-model="model[field]"></datetime>
                                </div>

                            </td>


                            <td v-else-if="type==='Date'">{{parseDate(model[field])}}</td>
                            <td v-else-if="type !== 'Hardcoded'">{{model[field]}}</td>

                            <td v-if="editing === model.id">
                                <button type="button" class="btn btn-info btn-sm" @click="editModel(model)">Save
                                </button>
                                <button type="button" class="btn btn-warning btn-sm" @click="cancelEdit(model)">Cancel
                                </button>
                            </td>
                            <td v-else>
                                <button v-if="canEdit" type="button" class="btn btn-success btn-sm"
                                        @click="editMode(model)">Edit
                                </button>
                                <button v-if="canDelete" type="button" class="btn btn-danger btn-sm"
                                        @click="$emit('delete:model', model.id)">Delete <i
                                        class="fas fa-trash-alt"></i>
                                </button>
                                <button v-if="isProject" @click="goToTasks(model.id)" type="button" class="btn btn-info btn-sm"
                                        >Tasks
                                </button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <p style="display: none;" :id="errorEditElement" class="alert alert-danger"></p>
        <p style="display: none;" :id="successEditElement" class="alert alert-success">model successfully updated</p>

    </div>
</template>

<script>
    import moment from 'moment'
    import auth, * as constants from '../../api'
    import router from '../../router'

    export default {
        data: function () {
            return {
                successEditElement: `${this.modelPlural}-edit-success`.replace(/\s/g, ""),
                errorEditElement: `${this.modelPlural}-edit-error`.replace(/\s/g, ""),
                editing: null,
                dateFormat: constants.DATE_FORMAT,
                isProject : this.modelPlural === 'Projects' && (auth.isPM() || auth.isUser()),
            }
        },
        name: 'model-table',
        props: {
            models: Array,
            modelPlural: String,
            fields: Map,
            canEdit: Boolean,
            canDelete: Boolean,
            arrays: Map
        },
        mounted(){
            function comparer(index) {
                return function(a, b) {
                    var valA = getCellValue(a, index),
                        valB = getCellValue(b, index);
                    return $.isNumeric(valA) && $.isNumeric(valB) ?
                        valA - valB : valA.localeCompare(valB);
                };
            }

            function getCellValue(row, index) {
                return $(row).children('td').eq(index).text();
            }

            $('th').unbind('click').click(function() {
                var table = $(this).parents('table').eq(0);

                var rows = table.find('tr:gt(0)').toArray().sort(comparer($(this).index()));

                var elemI = $(this).find("i");

                if (elemI.length) {
                    let attr = $(elemI).attr('class');
                    if (attr.indexOf("asc") >= 0) {
                        this.asc = false;
                    } else if (attr.indexOf("desc") >= 0) {
                        this.asc = true;
                    }
                }

                if(this.asc === undefined){
                    this.asc = false;
                }
                //this.asc = !this.asc;

                //remove all previous sorted icons
                $('.fa-sort-asc').addClass('fa-sort').removeClass('fa-sort-asc');
                $('.fa-sort-desc').addClass('fa-sort').removeClass('fa-sort-desc');

                if (!this.asc) {
                    $(this).find("i").remove();
                    $(this).append("<i class=\"fa fa-fw fa-sort-desc\"></i>");
                    rows = rows.reverse();
                }else {
                    $(this).find("i").remove();
                    $(this).append("<i class=\"fa fa-sort-asc\"></i>");
                }
                table.children('tbody').empty().html(rows);
            });
        },
        methods: {
            goToTasks(id){
                router.push({name: 'tasks', params: {id: id}})
            },
            parseDate(date) {
                return moment(date).format('YYYY-MM-DD')
            },
            editMode(model) {
                this.cachedmodel = Object.assign({}, model)
                this.editing = model.id
            },

            cancelEdit(model) {
                Object.assign(model, this.cachedmodel)
                this.editing = null;
            },

            editModel(model) {
                this.$emit('edit:model', model.id, model, this.cachedmodel)
                this.editing = null
            },
            clearStatus() {
                $(`#${this.errorEditElement}`).hide()
            },
            dropdownEvent(model, field, obj) {
                //hack used to prevent choosing a city from a different country
                if (field === 'countryName' && model.hasOwnProperty('cityName')) {
                    model['cityName'] = ''
                }

                model[field] = obj
                this.$emit('choose:array', {field: field, obj: obj})
            }
        }
    }
</script>

<style scoped>

</style>
