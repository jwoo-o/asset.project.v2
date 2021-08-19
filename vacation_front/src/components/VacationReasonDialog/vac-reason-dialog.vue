<template>
  <el-dialog
    :width="width < 768 ? '95%' : width < 1200 ? '70%' : '30%'"
    :visible.sync="dialogVisible"
    center
    :before-close="handleClose"
  >
    <el-card
      shadow="never"
      class="grid-wrapper scroll"
      :body-style="{padding:0}"
      style="overflow: auto; height: 300px;"
    >
      <div v-if="reasonFlag === 'cancel'">
        <div class="header">
          <span>취소사유</span>
        </div>
        <div class="content">
          <el-input
            v-model="reason"
            type="textarea"
            :rows="5"
            placeholder="취소 사유를 입력해 주세요."
          />
        </div>
        <el-button style="margin: 10px; float:right;" size="small" @click="cancelVaction">취소요청</el-button>
      </div>
      <div v-if="reasonFlag === 'reject'">
        <div class="header">
          <span>반려사유</span>
        </div>
        <div class="content">
          <el-input
            v-model="reason"
            type="textarea"
            :rows="5"
            placeholder="반려 사유를 입력해 주세요."
          />
        </div>
        <el-button style="margin: 10px; float:right;" size="small" @click="vacationReject">반려</el-button>
      </div>
    </el-card>
  </el-dialog>
</template>

<script>
import { mapGetters } from 'vuex'
import { api_vacation_cancel } from '@/api/vacation/vacation'
import { success_cancel_message,
  necessary_reject_reason_message,
  necessary_cancel_reason_message } from './message/constant'
import { api_vacation_approval } from '@/api/approver/approver'

export default {
  name: 'VacCancel',
  // eslint-disable-next-line vue/require-prop-types
  props: ['dialogVisible', 'vacationId', 'reasonFlag', 'approverVacationList'],
  data() {
    return {
      reason: ''
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  methods: {
    async cancelVaction() {
      const data = {
        reason: this.reason,
        vacationId: this.vacationId
      }
      if (this.reason === '') {
        this.$message({
          type: 'warning',
          message: necessary_cancel_reason_message
        })
      } else {
        await api_vacation_cancel(data).then(() => {
          this.$message({
            type: 'success',
            message: success_cancel_message
          })
          this.$router.push({ name: 'user-home' })
        }).catch(() => {})
      }
    },
    async vacationReject() {
      if (confirm('반려 하시겠습니까?') === true) { // 확인
        if (this.reason === '') {
          this.$message({
            type: 'warning',
            message: necessary_reject_reason_message
          })
        } else {
          this.approverVacationList.approveState = 'REJECT'
          this.approverVacationList.rejectReason = this.reason
          await api_vacation_approval(this.approverVacationList).then(() => {
            this.$message({
              type: 'success',
              message: '반려가 완료되었습니다.'
            })
          }).catch(() => {})
          this.$router.push({ name: 'approve-list' })
        }
      } else { // 취소
        return
      }
    },
    handleClose() {
      this.$emit('handleClose')
    }
  }
}
</script>

<style scoped>

</style>
