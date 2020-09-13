<template>
    <div id="RequestTab" class="">

        <div v-if="showModal">
            <div class="modal-mask">
                <div class="modal-wrapper">
                    <div style="max-width: 800px" class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Calendar</h5>
                            </div>
                            <div class="modal-body">
                                <!--          BODY-->
                                <div  id="calendar"></div>
                                <!--          BODY-->
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" @click="showModal = false">OK</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>



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
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr :key="model.id" v-for="model in models">
                                    <td v-for="[field, type] in fields">{{model[field]}}</td>
                                    <td>
                                        <button style="display: inline;" type="button" class="btn btn-info btn-sm"
                                                @click="viewRequest(model.id)"><i
                                                class="fas fa-eye"></i>
                                        </button>
                                    </td>
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
                url: constants.TIMECARD+"/open",
                model_name: 'Request',
                modelPlural: 'Open Timecards',
                fields: this.createFields(),
                models: [],
                showModal : false,
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
                fields.set('userEmail', 'Date')
                fields.set('year', 'String')
                fields.set('month', 'String')
                return fields
            },
            viewRequest(id){
                this.showModal = true
                this.loadCalendaer(id)
            },
            acceptRequest(id) {
                axios.put(constants.TIMECARD+"/accept" + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => console.log(err))
            },
            rejectRequest(id) {
                axios.put(constants.TIMECARD+"/reject" + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => console.log(err))
            },
            async loadCalendaer(id) {
                var comp = this;

                var events = [];
                // console.log(this.models);
                // alert(this.models.length);

                var getUrl = constants.TIMECARD + "/tasks/" + id


                let calendarEvents = []
                await axios.get(getUrl)
                    .then(resp => calendarEvents = resp.data)
                    .catch(err => console.log(err))

                for (let i = 0; i < calendarEvents.length; i++) {
                    var eventData = {
                        title: calendarEvents[i].taskIdentifier + ":" + calendarEvents[i].duration,
                        start: calendarEvents[i].date,
                        id: calendarEvents[i].id,
                    };
                    //console.log(eventData)
                    events.push(eventData)

                }

                $("#calendar").fullCalendar({
                    header: {
                        left: "",
                        center: "title",
                        right: ""
                    },
                    eventOverlap: true,
                    selectOverlap: true,
                    defaultView: "month",
                    events: events,
                    firstDay: '1',
                    navLinks: true, // can click day/week names to navigate views
                    selectable: true,
                    selectHelper: false,
                    editable: false,
                    eventLimit: true, // allow "more" link when too many events
                    showNonCurrentDates: false,
                    fixedWeekCount: false,
                });
            },
        }
    }
</script>

<style scoped>
    html {
        overflow-y: scroll;
    }
</style>