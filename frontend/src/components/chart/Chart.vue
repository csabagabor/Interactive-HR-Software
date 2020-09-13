<template>
    <canvas ref="myChart" :width="width" :height="height"></canvas>
</template>

<script>
    import Chart from 'chart.js';

    export default {
        name: 'chart',
        props: {
            width: Number,
            height: Number,
            labels: Array,
            datasets: Array,
            type: String
        },
        data() {
            return {
                chart: null
            };
        },
        mounted() {
            this.chart = new Chart(this.$refs.myChart, {
                type: this.type,
                data: {
                    labels: this.labels,
                    datasets: this.datasets
                }
            });
        }
    }
</script>
