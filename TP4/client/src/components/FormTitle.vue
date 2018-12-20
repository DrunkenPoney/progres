<template>
    <legend :class="`display-4 mb-3 border-${theme.text} border-bottom text-center font-italic`">{{text}}</legend>
</template>

<script>
    export default {
        name: "FormTitle",
        props: {
            text: {
                type: String,
                required: true
            },
            theme: {
                type: Object,
                default: () => ({ text: 'dark' })
            }
        }
    };
</script>

<style scoped>

</style>