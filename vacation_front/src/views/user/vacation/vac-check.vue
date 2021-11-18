<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-form>
          <el-card
            shadow="never"
            class="grid-wrapper"
            :body-style="{padding:'10px'}"
            style="margin-bottom: 10px;height: 100%; overflow: auto"
          >
            <div
              class="grid-header"
              style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
            >
              <el-row>
                <el-col>
                  <span style="margin: 10px"> 휴가상세정보</span>
                </el-col>
              </el-row>
            </div>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px;">
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px">
                  <span>소속: {{ userList.orgName }}</span>
                </el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px">성명: {{ userList.userName }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px">직위: {{ userList.rankNm }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px">작성일자: {{ this.$moment(vacationDetail.createdAt).format('YYYY-MM-DD HH:mm:ss') }}
                </el-col>
              </el-card>
              <time-line style="height: 120px" :approver-status="vacationDetail.approveState" :order-position="vacationDetail.orderPosition" :approver-list="approverList" />
              <el-card shadow="never" class="grid-wrapper scroll" style="overflow: auto;" :body-style="{padding:'5px'}">
                <table v-loading="loading" class="tg">
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
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.startDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.endDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.countDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ vacationDetail.vacationKindDesc }}</span>
                      </td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ vacationDetail.approveStateDesc }}</span>
                      </td>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td class="tg-qxe1">사유</td>
                      <td class="tg-i3ry" colspan="5">
                        <el-input
                          v-model="vacationDetail.vacationReason"
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
                      <td class="tg-i3ry" colspan="5">{{ vacationDetail.takeOver }}</td>
                    </tr>
                    <tr>
                      <td class="tg-qxe1">비상연락망</td>
                      <td class="tg-i3ry" colspan="5">{{ vacationDetail.vacationTel }}</td>
                    </tr>
                    <tr>
                      <td class="tg-qxe1">첨부파일</td>
                      <td class="tg-i3ry" colspan="5">
                        <el-upload
                          ref="upload"
                          :file-list="vacationDetail.fileList"
                          :auto-upload="false"
                          :disabled="true"
                          :on-preview="handlePreview"
                          action=""
                        />
                      </td>
                    </tr>
                    <tr v-if="vacationDetail.approveState === 'REJECT'">
                      <td class="tg-qxe1">반려사유</td>
                      <td class="tg-i3ry" colspan="5">{{ vacationDetail.rejectReason }}</td>
                    </tr>
                  </tbody>
                </table>
              </el-card>
              <div>
                <el-button
                  v-if="vacationDetail.approveState === 'WAIT'"
                  style="margin: 10px; float:right;"
                  size="small"
                  @click="deleteVacation"
                >삭제
                </el-button>
                <el-button
                  v-if="vacationDetail.approveState === 'WAIT'"
                  style="margin: 10px; float:right;"
                  size="small"
                  @click="editVacation"
                >수정
                </el-button>
                <el-button
                  v-if="vacationDetail.approveState === 'APPROVE'"
                  style="margin: 10px; float:right;"
                  size="small"
                  @click="handleCancelVacation"
                >취소요청
                </el-button>
              </div>
            </el-col>
          </el-card>
        </el-form>
      </el-col>
    </el-row>
    <vac-reason-dialog :dialog-visible.sync="dialogVisible" :vacation-id="vacationId" :reason-flag="reasonFlag" @handleClose="dialogVisible=false" />
  </div>
</template>

<script>
import timeLine from '@/components/TimeLine'
import { api_vacation_delete, api_vacation_detail } from '@/api/vacation/vacation'
import { api_user_list_userId } from '@/api/user/user'
import { defaultSearchForm } from '@/default/data'
import { delete_message } from '../message/constant'
import VacReasonDialog from '@/components/VacationReasonDialog/vac-reason-dialog'

export default {
  name: 'VacApproval',
  components: {
    VacReasonDialog,
    timeLine
  },
  data() {
    return {
      loading: false,
      radio: '3',
      loginType: 'user',
      vacationId: '',
      vacationDetail: {},
      userList: {},
      searchForm: Object.assign({}, defaultSearchForm),
      approver: '',
      cc: '',
      approverList: [],
      dialogVisible: false,
      reasonFlag: 'cancel'
    }
  },
  created() {
    this.getUserByUserId()
    this.getVacationDetail()
  },
  methods: {
    addRow: function() {
      this.rows.push({
        name: '',
        job: ''
      })
    },
    removeRow: function(row) {
      this.rows.$remove(row)
    },
    getUserByUserId() {
      this.searchForm.userId = this.$route.query.userId
      api_user_list_userId(this.searchForm).then(response => {
        const { userList } = response.data
        this.userList = userList
      }).catch(() => {
      })
    },
    async getVacationDetail() {
      this.loading = true
      this.vacationId = this.$route.query.vacationId
      api_vacation_detail(this.vacationId).then(response => {
        const { vacationDetail, approverList } = response.data
        this.vacationDetail = vacationDetail
        this.approverList = approverList
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    async deleteVacation() {
      if (confirm('삭제 하시겠습니까?') === true) { // 확인
        await api_vacation_delete(this.vacationId).then(() => {
          this.$message({
            type: 'success',
            message: delete_message
          })
        })
        this.$router.push({ name: 'home' })
      } else { // 취소
        return
      }
    },
    editVacation() {
      this.$router.push({ name: 'vac-edit', query: { vacationId: this.vacationId }})
    },
    handleCancelVacation() {
      this.dialogVisible = true
    },
    handlePreview(file) {
      const link = document.createElement('a')
      link.href = file.url
      link.download = file.name
      document.body.appendChild(link)
      link.click()
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
