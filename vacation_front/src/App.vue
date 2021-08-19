<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  mounted() {
    window.addEventListener('resize', this.setWindow)
  },
  methods: {
    setWindow() {
      this.$store.dispatch('app/setWidth', window.innerWidth)
      this.$store.dispatch('app/setHeight', window.innerHeight)
    }
  }

}
</script>

