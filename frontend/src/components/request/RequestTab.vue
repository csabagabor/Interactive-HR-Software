<template>
    <div id="RequestTab" class="">
        <div class="row h-100 justify-content-center align-items-center">
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
                                    <th :key="field.id" v-for="[field, type] in fields" class="th-lg">
                                        {{field}}
                                        <i class="fa fa-fw fa-sort"></i></th>
                                    <th></th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr :key="model.id" v-for="model in models">
                                    <td v-for="[field, type] in fields">{{model[field]}}</td>
                                    <td>
                                        <button style="display: inline;" type="button" class="btn btn-success btn-sm"
                                                @click="acceptRequest(model.id)"><i
                                                class="fas fa-check"></i>
                                        </button>
                                    </td>
                                    <td>
                                        <button style="display: inline; " type="button" class="btn btn-danger btn-sm"
                                                @click="rejectRequest(model.id)"><i
                                                class="fas fa-trash-alt"></i>
                                        </button>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import * as constants from '../../api'
    import axios from 'axios';

    export default {
        name: "RequestTab",
        data() {
            return {
                url: constants.REQUEST_OPEN_URL,
                model_name: 'Request',
                modelPlural: 'Open Requests',
                fields: this.createFields(),
                models: []
            }
        },
        mounted() {
            function comparer(index) {
                return function(a, b) {
                    var valA = getCellValue(a, index),
                        valB = getCellValue(b, index);

                    console.log(valA +" " + valB);

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

                    //console.log(table.find('tr:gt(0)').toArray());

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

                    return false;
            });

            this.getModels()
        },
        methods: {
            async getModels() {
                axios.get(this.url)
                    .then(resp => this.models = resp.data)
                    .catch(err => console.log(err))
            },
            createFields() {
                let fields = new Map()
                fields.set('startDate', 'Date')
                fields.set('endDate', 'Date')
                fields.set('needOfPhone', 'Boolean')
                fields.set('needOfVpn', 'Boolean')
                fields.set('needOfLaptop', 'Boolean')
                fields.set('details', 'String')
                //fields.set('userEmail', 'Hardcoded')
                fields.set('transportationMeanName', 'Array')
                fields.set('countryName', 'Array')
                fields.set('cityName', 'Array')
                return fields
            },
            acceptRequest(id) {
                axios.put(constants.REQUEST_ACCEPT_URL + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => console.log(err))
            },
            rejectRequest(id) {
                axios.put(constants.REQUEST_REJECT_URL + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => console.log(err))
            },
        }
    }
</script>

<style scoped>
    html {
        overflow-y: scroll;
    }
</style>