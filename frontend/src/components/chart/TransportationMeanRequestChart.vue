<template>
    <div>
        <chart v-if="ready"
               :width="500"
               :height="300"
               :labels="labels"
               :datasets="datasets"
               type="pie"
        ></chart>
    </div>
</template>

<script>
    import Chart from './Chart';
    import * as constants from '../../api'
    import helper from './helper'

    export default {
        name: 'TransportationMeanRequestChart',

        components: {
            Chart
        },
        data() {
            return {
                ready: false,
                datasets: [],
                labels: []
            };
        },
        mounted() {
            this.getData()
        },
        methods: {
            async getData() {

                let chart = await helper.getChartData(constants.REQUESTS_PER_TRANSPORTATION_MEAN_METRICS_URL);

                this.labels = chart.labels

                this.datasets.push({
                    label: 'TransportationMean',
                    data: chart.data,
                    borderColor: chart.borderColors,
                    backgroundColor: chart.backgroundColors
                })

                this.ready = true
            },
        }
    }
</script>
