<template>
  <div>
    <navbar />
  </div>
</template>

<script>
import { Navbar } from './components'

/* import { mapState } from 'vuex'*/

export default {
  name: 'LayoutAdmin',
  components: {
    Navbar
  },
  methods: {
    /* handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }*/
  }
}
</script>

<style lang="scss" scoped>
</style>
