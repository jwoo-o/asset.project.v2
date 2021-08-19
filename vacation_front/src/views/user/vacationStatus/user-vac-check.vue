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
            style="margin-bottom: 10px;height: 860px; overflow: auto"
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
                <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">소속: {{ userList.orgName }}</el-col>
                <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">성명: {{ userList.userName }}</el-col>
                <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">직위: {{ userList.rankNm }}</el-col>
                <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">작성일자: {{ vacationDetail.createdAt | moment('YYYY-MM-DD') }}</el-col>
              </el-card>
              <time-line :order-position="vacationDetail.orderPosition" :approver-list="approverList" />
              <el-card shadow="never" class="grid-wrapper" style="overflow: auto" :body-style="{padding:'5px'}">
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
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.startDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.endDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">{{ vacationDetail.countDay }}</td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ vacationDetail.vacationKind }}</span>
                      </td>
                      <td class="tg-i3ry" style="text-align: center">
                        <span>{{ vacationDetail.approveState }}</span>
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
                    <tr v-if="vacationDetail.approveState === '반려'">
                      <td class="tg-qxe1">반려사유</td>
                      <td class="tg-i3ry" colspan="5">{{ vacationDetail.rejectReason }}</td>
                    </tr>
                  </tbody>
                </table>
              </el-card>
            </el-col>
          </el-card>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { defaultSearchForm } from '@/default/data'
import { api_user_list_userId } from '@/api/user/user'
import { api_vacation_detail } from '@/api/vacation/vacation'
import timeLine from '@/components/TimeLine'

export default {
  name: 'UserVacCheck',
  components: {
    timeLine
  },
  data() {
    return {
      loading: false,
      radio: '3',
      loginType: 'user',
      vacationDetail: {},
      searchForm: Object.assign({}, defaultSearchForm),
      userList: {},
      approverList: []
    }
  },
  created() {
    this.getUserByUserId()
    this.getVacationDetail()
  },
  methods: {
    async getUserByUserId() {
      this.searchForm.userId = this.$route.query.userId
      await api_user_list_userId(this.searchForm).then(response => {
        const { userList } = response.data
        this.userList = userList
      }).catch(() => {})
    },
    async getVacationDetail() {
      this.loading = true
      const vacationId = this.$route.query.vacationId
      await api_vacation_detail(vacationId).then(response => {
        const { vacationDetail, approverList } = response.data
        this.vacationDetail = vacationDetail
        this.approverList = approverList
        this.loading = false
        this.loading = false
      }).catch(() => {})
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
  }

  .tg td {
    border-color: black;
    border-style: solid;
    border-width: 1px;
    font-family: Arial, sans-serif;
    font-size: 14px;
    overflow: hidden;
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
    overflow: hidden;
    padding: 10px 20px;
    word-break: normal;
  }

  .tg .tg-qxe1 {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    width: 150px
  }

  .tg .tg-43cr {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    vertical-align: top;
    width: 400px
  }

  .tg .tg-i3ry {
    background-color: #ffffff;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: left;
    vertical-align: top;
    width: 400px
  }

</style>
