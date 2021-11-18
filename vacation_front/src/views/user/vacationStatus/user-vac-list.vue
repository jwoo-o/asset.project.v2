<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :xs="24" :sm="24" :lg="6">
        <org-tree :org-code="searchForm.orgCode" @orgTree="getOrgInfo" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="18">
        <el-row>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-card
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
                  <el-col :xs="12" :sm="14" :lg="16">
                    <span style="margin: 10px">휴가현황</span>
                  </el-col>
                  <el-col :xs="12" :sm="10" :lg="8" align="right">
                    <el-select v-model="year" style="margin-right: 10px;width: 30%" size="small" @change="handleFilter">
                      <el-option
                        v-for="item in yearsListByOrgCode"
                        :key="item"
                        :label="item+'년도'"
                        :value="item"
                      />
                    </el-select>
                  </el-col>
                </el-row>
              </div>
              <el-card shadow="never" class="grid-wrapper">
                <el-form :model="searchForm" label-position="left">
                  <el-row>
                    <el-col :xs="24" :sm="12" :lg="10">
                      <el-form-item label="검색 부서" label-width="80px">
                        <el-input v-model="searchForm.orgName" disabled size="small" />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :xs="24" :sm="24" :lg="24">
                      <el-row :gutter="20">
                        <el-col :xs="5" :sm="4" :lg="4">
                          <el-select v-model="searchForm.searchType" size="small" style="width: 100%;">
                            <el-option value="" label="전체">전체</el-option>
                            <el-option value="userId" label="ID">ID</el-option>
                            <el-option value="name" label="이름">이름</el-option>
                          </el-select>
                        </el-col>
                        <el-col :xs="5" :sm="5" :lg="5">
                          <el-input
                            v-model="searchForm.searchWord"
                            size="small"
                            :disabled="searchForm.searchType === ''"
                            @keyup.enter.native="handleFilter"
                          />
                        </el-col>
                        <el-button
                          icon="el-icon-search"
                          size="small"
                          @click.native.prevent="handleFilter"
                        >검색</el-button>
                      </el-row>
                    </el-col>
                  </el-row>
                </el-form>
              </el-card>
              <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
                <el-table
                  v-loading="loading"
                  :data="userList"
                  :header-cell-style="{background:'#F5F7FA'}"
                  size="small"
                  @row-click="handleSelectionVacation"
                >
                  <el-table-column
                    label="부서명"
                    align="center"
                    min-width="100"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.orgName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="직급 / 직책"
                    align="center"
                    min-width="100"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.rankNm }} / {{ row.jobNm }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="성명"
                    align="center"
                    min-width="100"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.userName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="휴가 일수" align="center">
                    <el-table-column
                      label="전체"
                      align="center"
                      min-width="50"
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.vacationTotalCnt }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="사용"
                      align="center"
                      min-width="50"
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.vacationUseCnt }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="잔여"
                      align="center"
                      min-width="50"
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.vacationTotalCnt - row.vacationUseCnt }}</span>
                      </template>
                    </el-table-column>
                  </el-table-column>
                  <el-table-column
                    prop="rmk"
                    label="비고"
                    align="center"
                    min-width="70"
                  />
                </el-table>
                <paging
                  :total="total"
                  :page.sync="searchForm.page"
                  :limit.sync="searchForm.limit"
                  @pagination="getVacationInfoList"
                />

              </el-card>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { defaultSearchForm } from '@/default/data'
import OrgTree from '@/components/OrgTree/index'
import { mapGetters } from 'vuex'
import { api_user_vacation_info_list } from '@/api/vacation/vacation'

export default {
  name: 'UserVacList',
  components: {
    OrgTree,
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      userList: [],
      years: [],
      total: 0,
      year: '',
      dialogVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'yearsListByOrgCode'
    ])
  },
  created() {
    this.getYear()
    this.getVacationInfoList()
  },
  methods: {
    getOrgInfo(data) {
      if (this.searchForm.orgCode !== data.orgCode) {
        this.searchForm = Object.assign({}, defaultSearchForm)
        this.searchForm.orgCode = data.orgCode
        this.searchForm.orgName = data.orgName
        this.searchForm.order = data.order
      }
    },
    async getVacationInfoList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.orgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.orgName
      }
      this.searchForm.year = this.year
      await api_user_vacation_info_list(this.searchForm).then(response => {
        this.loading = false
        const { userList, total } = response.data
        this.userList = userList
        this.total = total
      }).catch(() => {
        this.loading = false
      })
    },
    handleSelectionVacation(info) {
      this.$router.push({ name: 'user-vac-detail', query: { userId: info.userId, year: this.year }})
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getVacationInfoList()
    },
    getYear() {
      const day = new Date()
      this.year = this.$moment(day).format('YYYY')
    },
    handleVactionInfoUpdate() {
      this.dialogVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
