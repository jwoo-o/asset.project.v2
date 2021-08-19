<template>
  <div class="app-wrapper">
    <layout_admin />
    <section class="app-main" style="background-color: #F8F9FA">
      <transition name="fade-transform" mode="out-in">
        <router-view :key="key" />
      </transition>
    </section>
  </div>

</template>

<script>
import layout_admin from '@/layout-admin'

export default {
  name: 'Index',
  components: {
    layout_admin
  },
  computed: {
    key() {
      return this.$route.name
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "../../styles/mixin.scss";
  @import "../../styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }
  .app-main {
    /* 50= navbar  50  */
    min-height: calc(100vh - 60px);
    width: 100%;
    position: relative;
    overflow: hidden;
  }

  .fixed-header + .app-main {
    padding-top: 50px;
  }

  .hasTagsView {
    .app-main {
      /* 84 = navbar + tags-view = 50 + 34 */
      min-height: calc(100vh - 84px);
    }

    .fixed-header + .app-main {
      padding-top: 84px;
    }
  }
</style>
