<template>
    <button type="button" :class="`list-group-item list-group-item-action ` + (active ? 'active' : '')" @click="$emit('click', )">{{text}}</button>
</template>

<script>
    export default {
        name: "NavItem",
        props: ['text', 'active']
    };
</script>