<template>
    <div>
        <chart v-if="ready"
               :width="500"
               :height="300"
               :labels="labels"
               :datasets="datasets"
               type="line"
        ></chart>
    </div>
</template>

<script>
    import Chart from './Chart';
    import * as constants from '../../api'
    import helper from './helper'

    export default {
        name: 'DurationRequestChart',

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

                let chart = await helper.getChartData(constants.REQUESTS_PER_DURATION_METRICS_URL);

                this.labels = chart.labels.map(label => {
                    if (label === "31")
                        return "31+"
                    else return label
                })

                this.datasets.push({
                    label: 'nr requests',
                    data: chart.data,
                    borderColor: 'rgba(255, 50, 100, 0.5)',
                    backgroundColor: 'rgba(255, 50, 100, 0.2)'
                })

                this.ready = true
            },
        }
    }
</script>
