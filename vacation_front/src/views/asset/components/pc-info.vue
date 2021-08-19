<template>
  <div>
    <el-form :model="info" label-position="left" size="small">
      <el-form-item label="프로세스 정보">
        <el-input v-model="info.cpuName" />
      </el-form-item>
      <el-form-item label="메모리 정보">
        <el-input v-model="info.memory" />
      </el-form-item>
      <el-form-item label="OS 정보">
        <el-input v-model="info.osInfo" />
      </el-form-item>
      <el-form-item label="네트워크 정보">
        <el-input v-model="info.address" />
      </el-form-item>
      <el-form-item label="HDD 용량">
        <el-input v-model="info.hdd" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'PcInfo',
  // eslint-disable-next-line vue/require-prop-types
  props: ['info']
}
</script>

<style scoped>

</style>
