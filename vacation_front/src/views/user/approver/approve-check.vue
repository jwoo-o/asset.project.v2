<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-form>
          <el-card
            v-loading="loading"
            shadow="never"
            class="grid-wrapper"
            :body-style="{padding:'10px'}"
            style="margin-bottom: 10px;height: 850px; overflow: auto"
          >
            <div
              class="grid-header"
              style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
            >
              <el-row>
                <el-col>
                  <span style="margin: 10px"> 휴가결재</span>
                </el-col>
              </el-row>
            </div>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">소속: {{ userList.orgName }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">성명: {{ userList.userName }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">직위: {{ userList.rankNm }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">작성일자: {{ approverVacationList.createdAt | moment('YYYY-MM-DD') }}</el-col>
              </el-card>
              <time-line style="height: 120px" :approver-status="approverVacationList.approveState" :order-position="approverVacationList.orderPosition" :approver-list="approverList" />
              <el-card shadow="never" class="grid-wrapper scroll" style="overflow: auto;" :body-style="{padding:'5px'}">
                <table class="tg">
                  <thead>
                    <tr>
                      <th class="tg-qxe1" rowspan="2">일시</th>
                      <th class="tg-43cr">시작일</th>
                      <th class="tg-43cr">종료일</th>
                      <th class="tg-43cr">신청일수</th>
                      <th class="tg-43cr">휴가구분</th>
                      <th class="tg-43cr">승인상태</th>
                    </tr>
                    <tr>
                      <td class="tg-i3ry" style="text-align: center">{{ approverVacationList.startDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ approverVacationList.endDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ approverVacationList.countDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ approverVacationList.vacationKindDesc }}</span>
                      </td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ approverVacationList.approveStateDesc }}</span>
                      </td>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td class="tg-qxe1">사유</td>
                      <td class="tg-i3ry" colspan="5">
                        <el-input
                          v-model="approverVacationList.vacationReason"
                          type="textarea"
                          :rows="5"
                          :autosize="false"
                          class="inputTag"
                          resize="none"
                          readonly
                          placeholder="사유를 입력해 주세요"
                        />
                      </td>
                    </tr>
                    <tr>
                      <td class="tg-qxe1">인수인계</td>
                      <td class="tg-i3ry" colspan="5">{{ approverVacationList.takeOver }}</td>
                    </tr>
                    <tr>
                      <td class="tg-qxe1">비상연락망</td>
                      <td class="tg-i3ry" colspan="5">{{ approverVacationList.vacationTel }}</td>
                    </tr>
                    <tr>
                      <td class="tg-qxe1">첨부파일</td>
                      <td class="tg-i3ry" colspan="5">
                        <el-upload
                          ref="upload"
                          :file-list="approverVacationList.fileList"
                          :auto-upload="false"
                          :disabled="true"
                          :on-preview="handlePreview"
                          action=""
                        />
                      </td>
                    </tr>
                    <tr v-if="approverVacationList.approveState === 'REJECT'">
                      <td class="tg-qxe1">반려사유</td>
                      <td class="tg-i3ry" colspan="5">{{ approverVacationList.rejectReason }}</td>
                    </tr>
                  </tbody>
                </table>
              </el-card>
              <div v-if="isApproverState">
                <el-button
                  style="margin: 10px; float: right"
                  size="small"
                  :disabled="!isApproverOrder"
                  @click="handleRejectVacation"
                >반려
                </el-button>
                <el-button
                  style="margin: 10px; float: right"
                  size="small"
                  :disabled="!isApproverOrder"
                  @click="vacationApproval"
                >승인
                </el-button>
              </div>
            </el-col>
          </el-card>
        </el-form>
      </el-col>
    </el-row>
    <vac-reason-dialog :dialog-visible.sync="dialogVisible" :approver-vacation-list="approverVacationList" :vacation-id="vacationId" :reason-flag="reasonFlag" @handleClose="dialogVisible=false" />
  </div>
</template>

<script>
import timeLine from '@/components/TimeLine'
import { api_vacation_approval } from '@/api/approver/approver'
import { defaultSearchForm } from '@/default/data'
import { api_user_list_userId } from '@/api/user/user'
import { approval_success_message } from '../message/constant'
import { api_vacation_detail } from '@/api/vacation/vacation'
import { api_approver_order_check } from '@/api/approver/approver'
import VacReasonDialog from '@/components/VacationReasonDialog/vac-reason-dialog'

export default {
  name: 'VacApproval',
  components: {
    timeLine, VacReasonDialog
  },
  data() {
    return {
      loading: false,
      radio: '3',
      loginType: 'user',
      approverVacationList: [],
      searchForm: Object.assign({}, defaultSearchForm),
      userList: {},
      vacationId: '',
      isApproverOrder: false,
      isApproverState: false,
      orderPosition: '',
      approverList: [],
      dialogVisible: false,
      reasonFlag: 'reject'
    }
  },
  created() {
    this.vacationId = this.$route.query.vacationId
    this.searchForm.userId = this.$route.query.userId
    this.orderPosition = this.$route.query.orderPosition
    this.getUserByUserId()
    this.getVacationDetail()
    this.getApproverCheckAndOrderCheck()
  },
  methods: {
    getUserByUserId() {
      api_user_list_userId(this.searchForm).then(response => {
        const { userList } = response.data
        this.userList = userList
      }).catch(() => {})
    },
    async getVacationDetail() {
      this.loading = true
      this.vacationId = this.$route.query.vacationId
      await api_vacation_detail(this.vacationId).then(response => {
        const { vacationDetail, approverList } = response.data
        this.approverVacationList = vacationDetail
        this.approverList = approverList
        if (vacationDetail.approveState !== 'APPROVE' && vacationDetail.approveState !== 'REJECT') {
          this.isApproverState = true
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    async vacationApproval() {
      if (confirm('승인 하시겠습니까?') === true) { // 확인
        this.approverVacationList.approveState = 'ING'
        await api_vacation_approval(this.approverVacationList).then(() => {
          this.$message({
            type: 'success',
            message: approval_success_message
          })
        }).catch(() => {})
        this.$router.push({ name: 'approve-list' })
      } else { // 취소
        return false
      }
    },
    handlePreview(file) {
      const link = document.createElement('a')
      link.href = file.url
      link.download = file.name
      document.body.appendChild(link)
      link.click()
    },
    async getApproverCheckAndOrderCheck() {
      await api_approver_order_check(this.$store.getters.userId, this.orderPosition, this.vacationId).then(response => {
        this.isApproverOrder = response.data
      }).catch(() => {})
    },
    handleRejectVacation() {
      this.dialogVisible = true
    }
  }
}
</script>

<style scoped>
  .tg {
    border-collapse: collapse;
    border-spacing: 0;
    width: 100%;
    display: table-row;
  }

  .tg td {
    border-color: black;
    border-style: solid;
    border-width: 1px;
    font-family: Arial, sans-serif;
    font-size: 14px;
    padding: 10px 20px;
    word-break: normal;
  }

  .tg th {
    border-color: black;
    border-style: solid;
    border-width: 1px;
    font-family: Arial, sans-serif;
    font-size: 14px;
    font-weight: normal;
    padding: 10px 20px;
    word-break: normal;
  }

  .tg .tg-qxe1 {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    min-width: 150px
  }

  .tg .tg-43cr {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    vertical-align: top;
    min-width: 120px
  }

  .tg .tg-i3ry {
    background-color: #ffffff;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: left;
    vertical-align: top;
    width: 400px
  }

  .inputTag {
    border: none;
    width: 100%;
    outline: none;
  }
</style>
