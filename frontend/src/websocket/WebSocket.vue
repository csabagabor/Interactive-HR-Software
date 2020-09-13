<template>

    <div>
        <div v-if="showModal" class="modal-mask">
            <div class="modal-wrapper">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Attention</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true" @click="reload()">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            {{modalBody}}
                        </div>
                        <div class="modal-footer">
                            <button id="save-btn" type="button" @click="reload()"
                                    class="btn btn-primary">OK
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import auth, * as constants from '../api'
    import SockJS from 'sockjs-client'
    import Stomp from 'webstomp-client'
    import router from '../router'

    export default {
        name: "WebSocket",
        data() {
            return {
                stompClient: null,
                connected: false,
                showModal: false,
                modalBody: ''
            }
        },
        methods: {
            reload(){
                location.reload()
            },
            connect() {
                let socket = new SockJS(constants.WEBSOCKET_URL + 'hr-websocket');
                this.stompClient = Stomp.over(socket);

                let self = this;

                this.stompClient.connect({
                        "token": auth.getToken()
                    }

                    , function (frame) {
                        self.connected = true
                        //alert('Connected: ' + frame);

                        self.stompClient.subscribe('/user/queue/reply', function (greeting) {
                            //alert(1)
                            let msg = JSON.parse(greeting.body).msg
                            let type = 'success'
                            if (msg.indexOf('rejected') >= 0) {
                                type = 'error'
                            }
                            console.log(msg)
                            if (msg.indexOf('command:') >= 0) {
                                msg = msg.replace('command:', '')
                                console.log(msg)
                                if (msg === 'business-trip') {
                                    //Add new Request
                                    self.requestTutorial()
                                } else if (msg === 'timecard-fill') {
                                    self.calendarTutorial()
                                }

                            } else {
                                self.$toasted.show(msg, {
                                    theme: "bubble",
                                    position: "top-right",
                                    duration: 1500,
                                    type: type
                                });
                            }
                        });
                    });
            },
            disconnect() {
                if (this.stompClient != null) {
                    this.stompClient.disconnect();
                }

                this.connected = false
                //alert("Disconnected");
            },
            async requestTutorial() {
                var self = this;
                var time = 1;

                $('.mouse').show()
                await self.moveCursorAndClick('.nav-link.dropdown-toggle:eq(0)', time)
                $('#must-show-li').addClass("show");
                $('#nav-dropdown-user').addClass("show");
                await self.moveCursorAndClick('#request-li', time)
                router.push('my-requests').catch(error => {
                    if (error.name != "NavigationDuplicated") {
                        throw error;
                    }
                });

                $('#must-show-li').removeClass("show");
                $('#nav-dropdown-user').removeClass("show");


                await self.moveCursorAndClick('#add-btn', time)
                await self.moveCursorAndClick('.vdatetime-input:eq(0)', time)
                await self.moveCursorAndClick('.vdatetime-popup__actions__button--confirm', time)
                await self.moveCursorAndClick('.vdatetime-input:eq(1)', time)
                await self.moveCursorAndClick('.vdatetime-popup__actions__button--confirm', time)
                await self.moveCursorAndClick('label[for=checkbox-needOfPhone]', time)
                await self.moveCursorAndClick('#text-details', time)
                await self.writeTextWithDelay('#text-details', "Type some details here", 50);
                $('#text-details').click();
                await self.moveCursorAndClick('#drop-transportationMeanName', time)
                await self.moveCursorAndClick('.interactive-dropdown-transportationMeanName:eq(0)', time)
                // $('#drop-transportationMeanName').click();
                await self.sleep(300);

                await self.moveCursorAndClick('#drop-countryName', time)
                await self.moveCursorAndClick('.interactive-dropdown-countryName:eq(0)', time)
                // $('#drop-countryName').click();
                await self.sleep(300);

                await self.moveCursorAndClick('#drop-cityName', time)
                await self.moveCursorAndClick('.interactive-dropdown-cityName:eq(0)', time)
                // $('#drop-cityName').click();

                $('#hidden-field').click();

                await self.moveCursorAndClick('#send-btn', time)

                $('.mouse').hide()
                self.moveCursorBack();
            },
            async type(elem, string, index, speed) {
                console.log("string:" + string);
                var val = string.substr(0, index + 1);
                var self = this;
                $(elem).val(val);
                if (index < string.length) {
                    await self.sleep(Math.random() * speed);
                    await self.type(elem, string, index + 1, speed);
                }
            },
            async writeTextWithDelay(elem, text, speed) {
                $(elem).focus();
                await this.type(elem, text, 0, speed);
            },
            async moveCursorAndClick(elem, time) {
                const el = document.querySelector('.mouse');
                await auth.waitForElem(elem);

                var self = this;
                var position = $(elem).offset();
                let x = position.left + $(elem).width() / 2;
                let y = position.top + $(elem).height() / 2;

                return new Promise(resolve => {
                    requestAnimationFrame(async () => {
                        el.style.transform = `translate(${x}px, ${y}px)`;
                        $('.mouse').css({
                            'transition-duration': '0.5s'
                        });
                        await self.sleep(time * 1000);

                        $(elem).click();
                        $(elem).trigger('change')
                        resolve()
                    });
                });
            },
            async moveCursor(elem, time) {
                const el = document.querySelector('.mouse');
                await auth.waitForElem(elem);

                var self = this;
                var position = $(elem).offset();
                let x = position.left + $(elem).width() / 2;
                let y = position.top + $(elem).height() / 2;

                return new Promise(resolve => {
                    requestAnimationFrame(async () => {
                        el.style.transform = `translate(${x}px, ${y}px)`;
                        $('.mouse').css({
                            'transition-duration': '0.5s'
                        });
                        await self.sleep(time * 1000);
                        resolve()
                    });
                });
            },
            sleep(ms) {
                return new Promise(resolve => setTimeout(resolve, ms));
            },
            async calendarTutorial() {
                var self = this;
                $('.mouse').show()
                let time = 1

                await self.moveCursorAndClick('.nav-link.dropdown-toggle:eq(0)', time)
                $('#must-show-li').addClass("show");
                $('#nav-dropdown-user').addClass("show");
                await self.moveCursorAndClick('#calendar-li', time)
                router.push('calendar').catch(error => {
                    if (error.name != "NavigationDuplicated") {
                        throw error;
                    }
                });

                $('#must-show-li').removeClass("show");
                $('#nav-dropdown-user').removeClass("show");

                await self.moveCursorAndClick('.fc-mon.fc-day', time)
                var date = $('.fc-mon.fc-day').attr('data-date');
                $('#calendar').fullCalendar('select', date);

                await self.moveCursorAndClick('#drop-project', time)
                await self.moveCursorAndClick('.interactive-dropdown-project:eq(0)', time)
                //$('#drop-project').click();
                await self.sleep(300);

                await self.moveCursorAndClick('#drop-taskIdentifier', time)
                await self.moveCursorAndClick('.interactive-dropdown-taskIdentifier:eq(0)', time)
                //$('#drop-taskIdentifier').click();
                await self.sleep(300);

                $('#hidden-field').click();
                await self.sleep(100);

                await self.moveCursorAndClick('#save-btn', time)

                await self.moveCursor('#send-timecard', time)
                await self.sleep(1000);
                this.showModal = true
                this.modalBody = "Please hit the send button only if you are sure you filled out the timecard correctly. You cannot modify it afterwards!"
                $('.mouse').hide()
                self.moveCursorBack();
            },
            moveCursorBack() {
                function render(a) {
                    const el = document.querySelector('.mouse');
                    el.style.transform = `translate(0px, 0px)`;
                    $('.mouse').css({
                        'transition-duration': '0s'
                    });
                    $('.mouse').hide();
                }

                requestAnimationFrame(render);
            }
        },
        mounted() {
            this.connect()
        },
        beforeDestroy() {
            this.disconnect()
        }
    }
</script>

<style scoped>

</style>