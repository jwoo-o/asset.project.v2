<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
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
              <el-col :xs="8" :sm="14" :lg="16">
                <span style="margin: 10px">{{ year }}년도 휴가현황조회</span>
              </el-col>
            </el-row>
          </div>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
              <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">소속: {{ userInfo.orgName }}</el-col>
              <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">성명: {{ userInfo.userName }}</el-col>
              <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">직위: {{ userInfo.rankNm }}</el-col>
              <el-col :xs="6" :sm="6" :lg="6" style="font-size: 14px">사용/전체: {{ userInfo.vacationUseCnt }} / {{ userInfo.vacationTotalCnt }}
              </el-col>
            </el-card>
          </el-col>
          <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
            <el-table
              :data="vacationList"
              style="width: 100%"
              :header-cell-style="{background:'#F5F7FA'}"
              @row-click="handleSelectionVacation"
            >
              <el-table-column
                label="등록번호"
                align="center"
                min-width="50"
              >
                <template slot-scope="{row}">
                  <span>{{ row.vacationId }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="file"
                label="첨부"
                align="center"
                min-width="50"
              >
                <template slot-scope="{ row}">
                  <span v-if="row.attach" class="el-icon-s-claim" style="font-size: 20px" />
                  <span v-else class="el-icon-s-release" style="font-size: 20px" />
                </template>
              </el-table-column>
              <el-table-column
                label="작성일자"
                align="center"
                min-width="120"
              >
                <template slot-scope="{row}">
                  <span>{{ row.createdAt }}</span>
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
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.startDay }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="종료일"
                  align="center"
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.endDay }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="일수"
                  align="center"
                  min-width="70"
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
                  <span>{{ row.approveState }}</span>
                </template>
              </el-table-column>
            </el-table>
            <paging
              :total="total"
              :page.sync="searchForm.page"
              :limit.sync="searchForm.limit"
              @pagination="getVacationList"
            />
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import Paging from '@/components/Pagination/index'
import { api_user_vacation_list } from '@/api/vacation/vacation'
import { defaultSearchForm } from '@/default/data'

export default {
  name: 'UserVacDetail',
  components: {
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      radio: 3,
      year: this.$route.query.year,
      vacationList: [],
      userInfo: {},
      total: 0,
      isEdit: false,
      totalCnt: 0,
      currentYear: new Date()
    }
  },
  created() {
    this.getVacationList()
  },
  methods: {
    async getVacationList() {
      this.loading = true
      this.searchForm.year = this.year
      this.currentYear = this.$moment(this.currentYear).format('YYYY')
      await api_user_vacation_list(this.$route.query.userId, this.searchForm).then(response => {
        this.loading = false
        const { vacationList, total, userInfo } = response.data
        this.vacationList = vacationList
        this.total = total
        this.userInfo = userInfo
        this.totalCnt = userInfo.vacationTotalCnt
      }).catch(() => {
        this.loading = false
      })
    },
    handleSelectionVacation(info) {
      this.$router.push({ name: 'user-vac-check', query: { vacationId: info.vacationId, userId: info.userId, year: this.year }})
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
