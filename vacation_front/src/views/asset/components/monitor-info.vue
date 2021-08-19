<template>
  <div>
    <el-form :model="info" label-position="left" size="small">
      <el-form-item label="인치">
        <el-input v-model="info.size" />
      </el-form-item>
      <el-form-item label="해상도">
        <el-input v-model="info.memory" />
      </el-form-item>
      <el-form-item label="최대 주사율">
        <el-input v-model="info.osInfo" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'MonitorInfo',
  // eslint-disable-next-line vue/require-prop-types
  props: ['info']
}
</script>

<style scoped>

</style>
