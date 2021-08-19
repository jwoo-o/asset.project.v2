<script>

import { reject_empty_message, approval_message } from '../message/constant'
import { api_vacation_approval_mail } from '@/api/approver/approver'

export default {
  name: 'ApproveMail',
  created() {
    this.vacationApproval()
  },
  methods: {
    vacationApproval() {
      const token = this.$route.query.token
      const data = {
        orderPosition: this.$route.query.orderPosition,
        approveState: this.$route.query.approveState,
        vacationId: this.$route.query.vacationId,
        vacationKind: this.$route.query.vacationKind
      }
      if (data.approveState === 'REJECT') {
        const prompt = this.$prompt('반려 사유', '반려', {
          confirmButtonText: '확인',
          cancelButtonText: '취소'
        }).then(({ value }) => {
          if (value === null) {
            this.$message({
              type: 'warning',
              message: reject_empty_message
            })
            this.vacationApproval()
            return false
          }
          data.rejectReason = value
          api_vacation_approval_mail(token, data).then(() => {
            this.$message({
              type: 'success',
              message: approval_message
            })
            this.exit()
          }).catch(() => {
            this.exit()
          })
        }, () => { this.exit() })
        if (!prompt) {
          this.exit()
        }
      } else {
        api_vacation_approval_mail(token, data).then(() => {
          this.$message({
            type: 'success',
            message: approval_message
          })
          this.exit()
        }).catch(() => {
          this.exit()
        })
      }
    },
    exit() {
      setTimeout(() => {
        window.close() // 일반적인 현재 창 닫기
        window.open('about:blank', '_self').self.close() // IE에서 묻지 않고 창 닫기
      }, 1000)
    }
  }
}
</script>

<style scoped>

</style>
