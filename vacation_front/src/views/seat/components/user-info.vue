<template>
  <el-card
    class="grid-wrapper"
    shadow="never"
    :body-style="{padding:0}"
    style="overflow: visible;"
  >
    <div class="header">
      <span>사용자 정보</span>
    </div>
    <el-form ref="userForm" size="small" :model="userInfo">
      <div class="content">
        <el-row>
          <el-col v-if="userInfo.imgSrc !== null" :xs="24" :sm="24" :lg="24" align="center" style="margin-bottom: 10px">
            <el-image
              style="width: 150px; height: 170px"
              :src="userInfo.imgSrc"
            />
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="이름" label-width="120px" prop="name">
              <el-input v-model="userInfo.userName" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="소속부서" label-width="120px" prop="orgCode">
              <el-input v-model="userInfo.orgName" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="직급" label-width="120px" prop="rankIndex">
              <el-input v-model="userInfo.rankNm" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="직책" label-width="120px" prop="rankIndex">
              <el-input v-model="userInfo.jobNm" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="이메일" label-width="120px" prop="email">
              <el-input v-model="userInfo.email" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="연락처" label-width="120px" prop="tel">
              <el-input v-model="userInfo.tel" :readonly="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form-item label="내선번호" label-width="120px" prop="tel">
              <el-input v-model="userInfo.ex" :readonly="isEdit" />
            </el-form-item>
          </el-col>
        </el-row>
      </div>
    </el-form>
  </el-card>
</template>

<script>
import { job_cd, rank_cd } from '@/default/data'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  name: 'UserInfo',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    userInfo: {
      type: Object,
      require: true
    }
  },
  data() {
    return {
      isEdit: true,
      rankIndex: null,
      jobIndex: null,
      jobList: [],
      rankList: []
    }
  },
  created() {
    this.getCommonList()
  },
  mounted() {
    this.getUserInfo()
  },
  methods: {
    async getCommonList() {
      this.rankList = this.$store.getters.commonCode.rank || rank_cd
      this.jobList = this.$store.getters.commonCode.job || job_cd
    },
    async getUserInfo() {
      for (let i = 0; i < this.rankList.length; i++) {
        if (this.rankList[i].detailCode === this.userInfo.rankCd) {
          this.rankIndex = i
        }
      }
      for (let i = 0; i < this.jobList.length; i++) {
        if (this.jobList[i].detailCode === this.userInfo.jobCd) {
          this.jobIndex = i
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
