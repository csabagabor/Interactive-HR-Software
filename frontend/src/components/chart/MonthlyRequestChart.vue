<template>
    <div>
        <chart v-if="ready"
               :width="500"
               :height="300"
               :labels="['Jan', 'Feb', 'Mar', 'Apr', 'May','June', 'July','August', 'September','October','November']"
               :datasets="datasets"
               type="line"
        ></chart>
    </div>
</template>

<script>
    import Chart from './Chart';
    import axios from 'axios';
    import * as constants from '../../api'

    export default {
        name: 'MonthlyRequestChart',

        components: {
            Chart
        },
        data() {
            return {
                ready: false,
                datasets: [],
                colors: [
                    'rgba(255, 50, 100, 0.5)',
                    'rgba(120, 50, 100, 0.5)',
                    'rgba(120, 150, 100, 0.5)',
                    'rgba(30,74,120,0.5)',
                    'rgba(25,120,11,0.5)',
                    'rgba(120,10,31,0.5)',
                    'rgba(120,115,4,0.5)',
                    'rgba(120,1,8,0.5)',
                    'rgba(11,9,120,0.5)',
                ],
                colorIndex: 0
            };
        },
        mounted() {
            this.getData()
        },
        methods: {
            async getData() {
                axios.get(constants.REQUESTS_PER_MONTH_METRICS_URL)
                    .then(resp => {
                        let map = resp.data

                        for (let year in map) {
                            let yearMetric = map[year]

                            let data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
                            for (let month in yearMetric) {
                                //backend sends months from 1..12, array contains elements from 0..11
                                data[month - 1] = yearMetric[month]
                            }
                            this.datasets.push({
                                label: year,
                                data: data,
                                borderColor: this.colors[this.colorIndex],
                                backgroundColor: this.colors[this.colorIndex].replace(/[^,]+(?=\))/, '0.2'),//replaces alpha value from the color
                            })

                            this.colorIndex++
                            if (this.colorIndex >= this.colors.length) {
                                this.colorIndex = 0
                            }
                        }

                        this.ready = true
                    })
                    .catch(err => console.log(err))
            },
        }
    }
</script>
