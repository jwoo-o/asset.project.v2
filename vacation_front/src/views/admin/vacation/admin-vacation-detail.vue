<template>
  <div>
    <div class="app-container">
      <el-row type="flex" justify="center" :gutter="10">
        <el-col :xs="24" :sm="24" :lg="16">
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
                <el-col :xs="24" :sm="14" :lg="16">
                  <span style="margin: 10px">{{ year }}년도 휴가현황조회</span>
                </el-col>
              </el-row>
            </div>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px;margin-top: 7px">소속: {{ userInfo.orgName }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px;margin-top: 7px">성명: {{ userInfo.userName }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px;margin-top: 7px">직위: {{ userInfo.rankNm }}</el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">사용/전체: {{ userInfo.vacationUseCnt }} / {{ userInfo.vacationTotalCnt }}
                  <el-button v-if="userInfo.useYn ? currentYear === year : false" icon="el-icon-plus" size="mini" style="margin-left: 10px" circle @click="handleModified('+')" />
                  <el-button v-if="userInfo.useYn ? currentYear === year : false" icon="el-icon-minus" size="mini" circle @click="handleModified('-')" />
                  <el-button v-if="isEdit" icon="el-icon-check" size="mini" circle @click="handleModified('run')" />
                </el-col>
              </el-card>
            </el-col>
            <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
              <el-table
                :data="vacationList"
                style="width: 100%"
                size="small"
                :header-cell-style="{background:'#F5F7FA'}"
                @row-click="handleSelectionVacation"
              >
                <el-table-column
                  label="No"
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
                      <span> {{ row.vacationKindDesc }}</span>
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
                    <span>{{ row.approveStateDesc }}</span>
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
        <el-col v-if="width > 1200" :xs="24" :sm="24" :lg="8">
          <el-card
            v-loading="loading1"
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
                <el-col :xs="24" :sm="14" :lg="16">
                  <span style="margin: 10px">변경 로그</span>
                </el-col>
              </el-row>
            </div>
            <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
              <el-table
                :data="vacationChangeList"
                style="width: 100%"
                size="small"
                :header-cell-style="{background:'#F5F7FA'}"
              >
                <el-table-column
                  label="권한/관리자"
                  align="center"
                  min-width="120"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.writeType }} {{ row.adminName !== '' ? ' ('+row.adminName+')' : '' }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="변경내역"
                  align="center"
                  min-width="120"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.changeCnt }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="변경사유"
                  align="center"
                  min-width="120"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.changeReason }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="변경일"
                  align="center"
                  min-width="120"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.modifiedAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <paging
                :total="total1"
                :page.sync="searchForm1.page"
                :limit.sync="searchForm1.limit"
                @pagination="getVacationList"
              />
            </el-card>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<script>
import Paging from '@/components/Pagination/index'
import { api_user_vacation_list, api_user_total_update, api_vacation_change_list } from '@/api/vacation/vacation'
import { defaultSearchForm } from '@/default/data'
import { edit_message } from '../message/constant'
import { mapGetters } from 'vuex'
import { necessary_message } from '../../admin/message/constant'
export default {
  name: 'AdminVacationDetail',
  components: {
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      searchForm1: Object.assign({}, defaultSearchForm),
      loading: false,
      loading1: false,
      radio: 3,
      year: this.$route.query.year,
      vacationList: [],
      vacationChangeList: [],
      userInfo: {},
      total: 0,
      total1: 0,
      isEdit: false,
      totalCnt: 0,
      currentYear: new Date()
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  created() {
    this.getVacationList()
    this.getVacationChangeLog()
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
    async getVacationChangeLog() {
      this.loading1 = true
      this.searchForm.year = this.year
      await api_vacation_change_list(this.$route.query.userId, this.searchForm).then(response => {
        this.loading1 = false
        const { vacationChangeList, total } = response.data
        this.vacationChangeList = vacationChangeList
        this.total1 = total
      }).catch(() => {
        this.loading = false
      })
    },
    handleSelectionVacation(info) {
      this.$router.push({ name: 'admin-vacation-check', query: { vacationId: info.vacationId, userId: info.userId, year: this.year }})
    },
    handleModified(type) {
      if (type === '+') {
        ++this.userInfo.vacationTotalCnt
      } else if (type === '-') {
        if (--this.userInfo.vacationTotalCnt < 0) {
          this.userInfo.vacationTotalCnt = 0
        }
      } else {
        this.$prompt('변경사유', '사유', {
          confirmButtonText: '확인',
          cancelButtonText: '취소'
        }).then(({ value }) => {
          if (value === null) {
            this.$message({
              type: 'warning',
              message: necessary_message
            })
            return false
          }
          const data = {
            year: this.year,
            totalCnt: this.userInfo.vacationTotalCnt,
            changeReason: value
          }
          api_user_total_update(this.userInfo.userId, data).then(() => {
            this.isEdit = false
            this.totalCnt = this.userInfo.vacationTotalCnt
            this.$message({
              type: 'success',
              message: edit_message
            })
            this.getVacationChangeLog()
          }).catch(() => {})
        }, () => {})
      }
      if (Number(this.userInfo.vacationTotalCnt) !== Number(this.totalCnt)) {
        this.isEdit = true
      } else {
        this.isEdit = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
