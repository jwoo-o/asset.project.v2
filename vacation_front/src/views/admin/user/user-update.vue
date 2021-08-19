<template>
  <div class="app-container">
    <layout />
    <el-row :gutter="15">
      <el-col :xs="24" :sm="24" :lg="24">
        <el-row>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-form>
              <el-card
                v-loading="loading"
                shadow="never"
                class="grid-wrapper"
                :body-style="{padding:'10px'}"
                style="margin-bottom: 10px;height: 860px;"
              >
                <div
                  class="grid-header"
                  style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
                >
                  <el-row>
                    <el-col>
                      <span style="margin: 10px"> 사용자 정보</span>
                    </el-col>
                  </el-row>
                </div>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
                    <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">소속: {{ this.$store.getters.orgName }}</el-col>
                    <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">성명: {{ this.$store.getters.userName }}</el-col>
                    <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">직위: {{ this.$store.getters.rankNm }}</el-col>
                  </el-card>
                  <el-card shadow="never" class="grid-wrapper">
                    <div style="font-size: 14px">
                      <el-row>
                        이전 비밀번호 <input>
                      </el-row>
                      <el-row>
                        신규 비밀번호 <input>
                      </el-row>
                      <el-row>
                        신규 비밀번호 확인 <input>
                      </el-row>
                    </div>
                    <div>
                      <el-button style="float: right" size="small" @click="messageAlert">수정</el-button>
                    </div>

                  </el-card>
                </el-col>
              </el-card>
            </el-form>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import layout from '@/layout/index'

export default {
  name: 'UserUpdate',
  components: {
    layout
  },
  methods: {
    messageAlert() {
      if (confirm('수정하시겠습니까?') === true) { // 확인
        this.$router.push({ name: 'home' })
      } else { // 취소
        return
      }
    }
  }
}
</script>

<style scoped>

</style>
