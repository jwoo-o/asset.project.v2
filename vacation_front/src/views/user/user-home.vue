<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col v-if="width>=1200" :xs="24" :sm="12" :lg="12">
        <el-card
          shadow="never"
          class="grid-wrapper"
          :body-style="{padding:'10px'}"
          style="margin-bottom: 10px;height: 850px;"
        >
          <div
            class="grid-header"
            style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
          >
            <el-row>
              <el-col :xs="12" :sm="14" :lg="16">
                <span style="margin: 10px"> {{ $store.getters.orgName }} 휴가 현황</span>
              </el-col>
            </el-row>
          </div>
          <CustomCalendar />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card
          v-loading="loading"
          shadow="never"
          class="grid-wrapper"
          :body-style="{padding:'10px'}"
          :style="{
            marginBottom: '10px',
            height: mobile ? height-120+'px' : '850px'
          }"
        >
          <div
            class="grid-header"
            style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
          >
            <el-row>
              <el-col :xs="12" :sm="14" :lg="16">
                <span style="margin: 10px"> 휴가현황</span>
              </el-col>
              <el-col :xs="12" :sm="10" :lg="8" align="right">
                <el-select v-model="year" style="margin-right: 10px;width: 80%" size="small" @change="handleFilter">
                  <el-option
                    v-for="item in years"
                    :key="item"
                    :label="item+'년도'"
                    :value="item"
                  />
                </el-select>
              </el-col>
            </el-row>
          </div>
          <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
            <el-col v-if="!mobile" :xs="12" :sm="12" :lg="12">
              <el-radio-group v-model="approvalState" @change="getVacationListByApproveState">
                <el-radio :label="''">전체</el-radio>
                <el-radio :label="'APPROVE'">승인</el-radio>
                <el-radio :label="'REJECT'">반려</el-radio>
              </el-radio-group>
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12">
              <div style="float: right; font-size: 14px">
                <span>휴가일수  </span>
                <span>총: {{ userInfo.vacationTotalCnt }}개 </span>
                <span>사용: {{ userInfo.vacationUseCnt }}개 </span>
                <span>잔여: {{ userInfo.vacationTotalCnt - userInfo.vacationUseCnt }}개 </span>
              </div>
            </el-col>
          </el-card>
          <el-card
            shadow="never"
            class="grid-wrapper"
            :style="{
              height: mobile ? height-300+'px' : '640px'
            }"
            :body-style="{padding:'5px'}"
          >
            <el-table
              :data="vacationList"
              style="width: 100%;"
              :height="mobile ? height-420 : 515"
              size="small"
              @row-click="handleSelectionVacation"
            >
              <el-table-column
                v-if="!mobile"
                label="No"
                align="center"
                min-width="50"
              >
                <template slot-scope="{row}">
                  <span>{{ row.vacationId }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-if="!mobile"
                prop="file"
                label="첨부"
                align="center"
                min-width="50"
              >
                <template slot-scope="{ row}">
                  <span v-if="row.isAttach" class="el-icon-s-claim" style="font-size: 20px" />
                </template>
              </el-table-column>
              <el-table-column
                v-if="!mobile"
                label="기안자"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.userName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-if="!mobile"
                label="작성일자"
                align="center"
                min-width="140"
              >
                <template slot-scope="{row}">
                  <span>{{ row.createdAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="휴가 구분" align="center">
                <el-table-column
                  label="구분"
                  align="center"
                  min-width="60"
                >
                  <template slot-scope="{row}">
                    <span> {{ row.vacationKind }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="시작일"
                  align="center"
                  min-width="90"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.startDay }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="종료일"
                  align="center"
                  min-width="90"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.endDay }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="일수"
                  align="center"
                  min-width="50"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.countDay }}</span>
                  </template>
                </el-table-column>
              </el-table-column>
              <el-table-column
                label="상태"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span> {{ row.approveState }}</span>
                </template>
              </el-table-column>
            </el-table>
            <paging
              :total="total"
              :page.sync="searchForm.page"
              :limit.sync="searchForm.limit"
              @pagination="getVacationList"
            />
            <div>
              <router-link :to="{name: 'vac-approval'}">
                <el-button size="small" style="float: right; margin-bottom: 10px">휴가상신</el-button>
              </router-link>
            </div>
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { defaultSearchForm } from '@/default/data'
import { api_user_vacation_list, api_years_list_search_userId } from '@/api/vacation/vacation'
import { mapGetters } from 'vuex'
import CustomCalendar from '@/components/Calendar/index'

export default {
  name: 'UserHome',
  components: {
    CustomCalendar,
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      approvalState: '',
      vacationList: [],
      userInfo: {},
      total: 0,
      year: '',
      years: []
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'width',
      'height',
      'mobile'
    ])
  },
  created() {
    this.getYears(this.userId)
    this.getYear()
    this.getVacationList()
  },
  methods: {
    handleSelectionVacation(data) {
      this.$router.push({
        name: 'vac-check',
        query: { vacationId: data.vacationId, userId: data.userId, approverGroupCode: data.approverGroupCode }
      })
    },
    async getYears(userId) {
      await api_years_list_search_userId(userId).then(response => {
        const { data } = response
        this.years = data
      }).catch(() => {})
    },
    async getVacationList() {
      this.loading = true
      this.searchForm.year = this.year
      await api_user_vacation_list(this.userId, this.searchForm).then(response => {
        this.loading = false
        const { vacationList, total, userInfo } = response.data
        this.vacationList = vacationList
        this.total = total
        this.userInfo = userInfo
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    async getVacationListByApproveState(data) {
      this.searchForm.approveState = data
      this.handleFilter()
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getVacationList()
    },
    getYear() {
      const day = new Date()
      this.year = this.$moment(day).format('YYYY')
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
