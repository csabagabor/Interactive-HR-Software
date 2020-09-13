<template>
    <div style="margin-top: 50px" class="card">
        <div class="card-header view view-cascade gradient-card-header blue-gradient  py-2 d-flex justify-content-between align-items-center">
            Metrics
        </div>
        <div class="card-body">

            <h4 class="card-title"><a>{{header}}</a></h4>
            <slot></slot>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SimpleCard",
        props: {
            header: String
        }
    }
</script>

<style scoped>

</style>