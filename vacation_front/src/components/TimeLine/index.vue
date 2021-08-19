<template>
  <el-card shadow="never" class="grid-wrapper">
    <el-steps :active="orderPosition" align-center :finish-status="'process'">
      <el-step
        v-for="(item) in approverList"
        :key="item.userName"
        :title="item.userName"
        :status="item.approverFlag === 'WAIT' ? 'process' : item.approverFlag === 'APPROVE' ? 'success' : 'error'"
        :description="orderPosition > item.order ? $moment(item.modifiedAt ).format('YYYY-MM-DD HH:mm:ss') : '' "
      />
    </el-steps>
  </el-card>
</template>

<script>
export default {
  name: 'Index',
  props: [
    // eslint-disable-next-line vue/require-prop-types
    'orderPosition', 'approverList', 'approverStatus'
  ]
}
</script>

<style scoped>
  .el-step__line-inner {
    border-color: #C0C4CC;
  }
</style>
