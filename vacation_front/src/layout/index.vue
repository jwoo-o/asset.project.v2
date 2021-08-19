<template>
  <div>
    <navbar />
  </div>
</template>

<script>
import { Navbar } from './components'

/* import { mapState } from 'vuex'*/

export default {
  name: 'Layout',
  components: {
    Navbar
  },
  // eslint-disable-next-line vue/require-prop-types
  methods: {}
}
</script>

<style lang="scss" scoped>
</style>
