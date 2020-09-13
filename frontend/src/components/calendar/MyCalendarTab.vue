<template>
    <div>
        <div id="model-tab" class="my-container container">
            <div v-if="status === 'ACCEPTED'" class="alert alert-success" role="alert">
                APPROVED
            </div>
            <div v-else-if="status === 'REJECTED'" class="alert alert-danger" role="alert">
                REJECTED. PLEASE MODIFY IT AND SEND AGAIN.
            </div>
            <div  v-else-if="status === 'OPEN'" class="alert alert-info" role="alert">
                WAITING FOR APPROVAL
            </div>
            <div  v-else class="alert alert-info" role="alert">
                NOT SENT YET
            </div>
            <div class="card" >

                <div class="card-header view view-cascade gradient-card-header blue-gradient  py-2 d-flex justify-content-between align-items-center">
                    <button id="send-timecard"v-if="status !== 'ACCEPTED' && status !== 'OPEN'" type="button" class="btn btn-success btn-sm" @click="sendTimecard() ">
                        Send TimeCard
                    </button>
                </div>
                <div class="card-body">
                    <div class="row h-100 justify-content-center align-items-center">

                        <div style="height:40%; width:60%;" id="calendar"></div>

                        <div  v-if="showModal">
                            <div class="modal-mask">
                                <div class="modal-wrapper">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title">Log Time</h5>
                                                <button type="button" class="close" data-dismiss="modal"
                                                        aria-label="Close">
                                                    <span aria-hidden="true" @click="showModal = false">&times;</span>
                                                </button>
                                            </div>
                                            <div class="modal-body">
                                                <!--          BODY-->
                                                <form class="text-center border border-light p-9">

                                                    <div :key="field.id" v-for="[field, type] in fields">
                                                        <label v-if="field !== 'taskIdentifier' && type !=='Hardcoded'  && ((model['project'] !== 'VACATION' && model['project'] !== '') || field !== 'duration')"
                                                               style="padding-left: 15px; margin-top: 15px;width: 220px">{{field.uppercase()}}:</label>

                                                        <label v-if="field === 'taskIdentifier' && type !=='Hardcoded'  && ((model['project'] !== 'VACATION' && model['project'] !== '') || field !== 'duration')"
                                                               style="padding-left: 15px; margin-top: 15px;width: 220px">Task Identifier:</label>

                                                        <div style="margin-left:5px; display: inline-block;width: 200px"
                                                             v-if="type==='String' && ((model['project'] !== 'VACATION' && model['project'] !== '') || field !== 'duration')">
                                                            <input type="text" class="form-control"
                                                                   v-model="model[field]" @keypress="clearStatus">
                                                        </div>
                                                        <div style="margin-left:5px; display: inline-block;width: 200px"
                                                             v-if="type==='Text'">
                                            <textarea type="text" class="form-control md-textarea"
                                                      length="120" rows="3"
                                                      v-model="model[field]" @keypress="clearStatus">
                                              </textarea>
                                                        </div>

                                                        <div style="margin-left:5px; display: inline-block"
                                                             v-else-if="type==='Array'">
                                                            <button :id="'drop-' +field" class="btn btn-primary btn-sm dropdown-toggle"
                                                                    type="button"
                                                                    data-toggle="dropdown"
                                                                    aria-haspopup="true" aria-expanded="false">
                                                              {{model[field]}}
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

                                                        <div style="margin-left:5px; display: inline-block"
                                                             v-else-if="type==='Boolean'"
                                                             class="custom-control custom-checkbox">
                                                            <input type="checkbox" class="custom-control-input"
                                                                   :id="'checkbox-' + field"
                                                                   v-model="model[field]">
                                                            <label class="custom-control-label"
                                                                   :for="'checkbox-' + field"></label>
                                                        </div>

                                                        <div style="margin-left:5px; display: none;width: 200px"
                                                             v-else-if="type==='Hardcoded'">
                                                            <input disabled type="text" class="form-control"
                                                                   v-model="model[field]">
                                                        </div>

                                                        <div style="margin-left:5px; display: inline-block"
                                                             v-else-if="type==='Date'">
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

                                                    </div>
                                                    <input style="display: none"
                                                           id="hidden-field"
                                                           @click="changeHidden()"
                                                    >

                                                    <p style="display: none;" :id="errorElement"
                                                       class="alert alert-danger"></p>
                                                    <p style="display: none;" :id="successElement"
                                                       class="alert alert-success">
                                                        {{model_name}} successfully added</p>

                                                </form>
                                                <!--          BODY-->
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                        @click="cancel()">Cancel
                                                </button>
                                                <button id="save-btn" type="button" class="btn btn-primary" @click="handleSubmit">
                                                    Save
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>


    </div>

</template>

<script>
    import ModelTab from "../crud/ModelTab";
    import auth, * as constants from '../../api'
    import axios from 'axios';
    import {Datetime} from "vue-datetime";

    export default {
        name: "MyCalendarTab",
        components: {
            ModelTab,
            datetime: Datetime
        },
        data() {
            return {
                url: constants.WORKED_TASK,
                model_url: constants.WORKED_TASK,
                model_name: 'WorkedTask',
                modelPlural_name: 'WorkedTasks',
                fields: this.createFields(),
                arrays: new Map(),
                canEdit: true,
                canDelete: true,
                canAdd: true,
                showModal: false,
                success: false,
                successElement: `WorkedTask-success`.replace(/\s/g, ""),
                errorElement: `WorkedTask-error`.replace(/\s/g, ""),
                model: {},
                dateFormat: constants.DATE_FORMAT,
                startDate : '',
                endDate : '',
                allprojs : [],
                models: [],
                calEvent : {},
                editing: false,
                status : '',
            }
        },
        methods: {
            changeHidden(){
                this.model.hidden = 'hidden';
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
            sendTimecard(){
                axios.post(constants.TIMECARD + "/send", {})
                    .then(resp => {
                        this.getTimecardStatus()
                    })
                    .catch(err => {
                        console.log(err);
                        let msg = err.response.data.message
                    })
            },
            getTimecardStatus(){
                axios.get(constants.TIMECARD + '/status', {})
                    .then(resp => {
                        this.status = resp.data
                    })
                    .catch(err => {
                        console.log(err);
                        let msg = err.response.data.message
                    })
            },
            //modelForm
            cancel(){
                this.createEmptyModel()
                $(`#${this.errorElement}`).hide()
                this.showModal = false
            },
            handleSubmit() {
                $(`#${this.errorElement}`).hide()
                if(this.editing){
                    this.editModel(this.calEvent.id, this.model, this.model)
                } else {
                    this.addModels(this.model)
                }
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
                console.log(this.model);
                this.model[field] = obj
                this.updateTasks({field: field, obj: obj})
            },
            async updateTasks(passObj) {
                let field = passObj.field
                let project = passObj.obj

                if (field === 'project'){
                    let tasks = []
                    this.model['taskIdentifier'] = ''
                    this.arrays.get('project')

                    //let projObj = this.allprojs.filter(m => m.title === project)

                    await axios.get(constants.API_URL + "projects/tasks?title=" + project)
                        .then(resp => tasks = resp.data.map(obj => obj.identifier))
                        .catch(err => console.log(err))
                    this.arrays = new Map(this.arrays.set('taskIdentifier', tasks))
                }

                //Arrays and Maps are not reactive

            },
            //ModelTab
            createFieldsMap() {
                let map = new Map(this.fields)
                map.delete('status') //status shouldn't appear in the input form
                return map
            },
            async getModels() {
                var getUrl = constants.WORKED_TASK+"/own"

                await axios.get(getUrl)
                    .then(resp => this.models = resp.data)
                    .catch(err => console.log(err))
            },

            async addModels(model) {
                let task = model['taskIdentifier'];

                console.log(task);
                if(task === undefined){
                    auth.showErrorMessage(`#${this.errorElement}`, 'Task must not be empty')
                }

                for (var m = moment(this.startDate); m.isBefore(this.endDate); m.add(1, 'days')) {
                    var date = m.format('YYYY-MM-DD');

                    var postModel;
                    // alert(model.project)
                    var duration = 8

                    if (model.project !== 'VACATION') {
                        duration = model.duration
                    }

                    postModel = {
                        taskIdentifier: task,
                        date: date,
                        duration: duration,
                        userEmail : auth.getEmail(),
                        hidden : model.hidden
                    };

                    console.log(postModel);
                    await axios.post(constants.WORKED_TASK, postModel)
                        .then(resp => {
                            let rModel = resp.data

                            this.models = [...this.models,rModel]
                            this.showModal = false

                            var eventData = {
                                id : rModel.id,
                                title: task + ":" + duration,
                                start: m
                            };

                            $("#calendar").fullCalendar("renderEvent", eventData, true); // stick? = true
                        })
                        .catch(err => {
                            auth.showErrorMessage(`#${this.errorElement}`, err.response.data.message)
                        })
                }


                this.refreshEvents();
            },

            async editModel(id, updatedModel, cachedModel) {
                $(this.errorEditElement).hide()
                $(this.successEditElement).hide()

                let postModel = {
                    taskIdentifier: updatedModel.taskIdentifier,
                    date: updatedModel.date,
                    duration: updatedModel.duration,
                    userEmail : auth.getEmail(),
                };

                await axios.put(constants.WORKED_TASK + '/' + id, postModel)
                    .then(async resp => {
                        this.models = this.models.map(model => model.id === id ? resp.data : model)
                        $(this.successEditElement).show()
                        // await this.getModels()
                        // this.loadCalendaer()

                        let rModel = resp.data

                        this.calEvent.title = updatedModel.taskIdentifier + ":" + updatedModel.duration
                        this.calEvent.start = updatedModel.start

                        $("#calendar").fullCalendar("updateEvent", this.calEvent);
                        this.showModal = false
                    })
                    .catch(err => {
                        //revert back if error
                        this.models = this.models.map(model => model.id === id ? cachedModel : model)
                        auth.showErrorMessage(`#${this.errorElement}`, err.response.data.message)
                    })
            },

            async deleteModel(id) {
                axios.delete(constants.WORKED_TASK + '/' + id)
                    .then(resp => this.models = this.models.filter(model => model.id !== id))
                    .catch(err => {alert("Cannot modify timecard");location.reload();})
            },

            //end
            refreshEvents() {
            },
            loadCalendaer() {
                var comp = this;
                function showPopUpAdd(start, end){
                    comp.startDate = start
                    comp.endDate = end

                    comp.editing = false
                    comp.showModal = true;
                    comp.clearStatus();
                }

                function showPopUpAddEdit(calEvent){
                    comp.calEvent = calEvent;

                    comp.model = comp.models.filter(m => m.id === calEvent.id)[0]

                    var taskIdentifier = comp.model['taskIdentifier'] //save because updateTasks removes it
                    comp.updateTasks({field: 'project', obj: comp.model['taskProjectTitle']})

                    comp.model['taskIdentifier'] = taskIdentifier
                    comp.model['project'] = comp.model['taskProjectTitle']

                    comp.editing = true
                    comp.showModal = true;
                    comp.clearStatus();
                }

                var events = [];
                // console.log(this.models);
                // alert(this.models.length);
                for (let i = 0; i < this.models.length; i++) {
                    var eventData = {
                        title: this.models[i].taskIdentifier + ":" +this.models[i].duration,
                        start: this.models[i].date,
                        id: this.models[i].id,
                    };
                    //console.log(eventData)
                    events.push(eventData)

                }


                
                var date = new Date();
                var day = date.getDate();
                var month = date.getMonth() + 1;
                var year = date.getYear();
                var day = date.getDay();

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
                    select: function (start, end) {
                        showPopUpAdd(start, end);
                        $("#calendar").fullCalendar("unselect");
                    },
                    eventRender: function (event, element) {
                        element
                            .find(".fc-content")
                            .prepend("<span class='closeBtn material-icons' style='color:red'>&#xe5cd;</span>");
                        element.find(".closeBtn").on("click", function () {
                            $("#calendar").fullCalendar("removeEvents", event._id);
                            comp.deleteModel(event.id)
                        });
                    },

                    eventClick: function (calEvent) {
                        showPopUpAddEdit(calEvent);
                    },
                });
            },
            createFields() {
                let fields = new Map()
                fields.set('project', 'Array')
                fields.set('taskIdentifier', 'Array')
                fields.set('duration', 'String')
                return fields
            },
            async createArrays() {
                let countries = []
                //get countries

                await axios.get(constants.PROJECT_URL)
                    .then(resp => {countries = resp.data.map(country => country.title); this.allprojs = resp.data})
                    .catch(err => console.log(err))


                this.arrays.set('project', countries)
            },
        },
        async mounted() {
            await this.getModels()
            this.createEmptyModel()
            this.createArrays()
            this.loadCalendaer()
            this.getTimecardStatus()
        }
    }
</script>

<style scoped>
    @import '~fullcalendar/dist/fullcalendar.css';
    .text-center{
        text-align: left !important;
    }

    .modal-mask {
        height: 800px !important;
    }

    card {
         margin-left: 0px!important;
    }
</style>