<template>
  <div>
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
                style="margin-bottom: 10px;height: 850px;"
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
                      <el-select v-model="year" style="margin-right: 10px;width: 30%" size="small" @change="handleFilter">
                        <el-option
                          v-for="item in yearsList"
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
                              <el-option value="" label="전체" />
                              <el-option value="userId" label="ID" />
                              <el-option value="name" label="이름" />
                              <el-option value="use" label="퇴사자" />
                            </el-select>
                          </el-col>
                          <el-col v-if="searchForm.searchType !== 'use'" :xs="5" :sm="5" :lg="5">
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
                    height="520"
                    size="small"
                    :row-class-name="tableRowClassName"
                    @row-click="handleSelectionVacation"
                    @sort-change="handleSortChange"
                  >
                    <el-table-column
                      label="부서명"
                      align="center"
                      min-width="100"
                      prop="orgName"
                      sortable
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.orgName }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="직급 / 직책"
                      align="center"
                      min-width="100"
                      prop="rankCd"
                      sortable
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.rankNm }} / {{ row.jobNm }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="성명"
                      align="center"
                      min-width="100"
                      prop="userName"
                      sortable
                    >
                      <template slot-scope="{row}">
                        <span>{{ row.userName }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column align="center" label="입사일" width="150" sortable prop="hireDate">
                      <template slot-scope="scope">
                        {{ scope.row.hireDate | moment('YYYY년 MM월 DD일') }}
                      </template>
                    </el-table-column>
                    <el-table-column v-if="useYnView" align="center" label="퇴사일" width="150" sortable prop="leaveDate">
                      <template slot-scope="scope">
                        {{ scope.row.leaveDate | moment('YYYY년 MM월 DD일') }}
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
                      label="휴가 촉진(사용자)"
                      align="center"
                      min-width="70"
                      column-key="USER"
                    >
                      <template slot-scope="{row}">
                        <el-button
                          size="small"
                          :disabled="row.userDeadline ? false : !row.useYn"
                          :type="row.userDeadline ? 'success' : row.sender ? 'warning' : ''"
                        >{{ row.userDeadline ? 'Y' : row.sender ? 'S' : 'N' }}</el-button>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="휴가 촉진(관리자)"
                      align="center"
                      min-width="70"
                      column-key="ADMIN"
                    >
                      <template slot-scope="{row}">
                        <el-button
                          size="small"
                          :disabled="row.useYn ? !row.sender : !row.adminDeadline"
                          :type="row.adminDeadline ? 'success' : ''"
                        >{{ row.adminDeadline ? 'Y' : 'N' }}</el-button>
                      </template>
                    </el-table-column>
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
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { defaultSearchForm } from '@/default/data'
import OrgTree from '@/components/OrgTree/index'
import { mapGetters } from 'vuex'
import { api_user_vacation_info_list, api_vacation_deadline_sender } from '@/api/vacation/vacation'
import { reSend_message, send_message, sender_message } from '../message/constant'

export default {
  name: 'AdminHome',
  components: {
    OrgTree,
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      radio: 3,
      userList: [],
      years: [],
      total: 0,
      year: '',
      useYnView: false
    }
  },
  computed: {
    ...mapGetters([
      'yearsList'
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
        this.handleFilter()
      }
    },
    async getVacationInfoList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.mgrOrgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.mgrOrgName
      }
      this.searchForm.year = this.year
      await api_user_vacation_info_list(this.searchForm).then(response => {
        this.useYnView = this.searchForm.searchType === 'use'
        this.loading = false
        const { userList, total } = response.data
        this.userList = userList
        this.total = total
      }).catch(() => {
        this.loading = false
      })
    },
    handleSelectionVacation(info, column) {
      if (column.columnKey === 'USER') {
        if (info.userDeadline) {
          this.$router.push({ name: 'admin-annual-deadline', query: { userId: info.userId, year: this.year, writer: column.columnKey }})
        } else {
          if (info.useYn) {
            const message = info.sender ? '전송' : '재전송'
            this.$confirm(info.sender ? send_message : reSend_message, `${message}?`, {
              confirmButtonText: '전송',
              cancelButtonText: '취소',
              dangerouslyUseHTMLString: true,
              type: 'warning'
            }).then(() => {
              this.senderVacationDeadline(info)
            }, function() {})
          }
        }
      } else if (column.columnKey === 'ADMIN') {
        if (info.useYn && info.sender) {
          this.$router.push({
            name: 'admin-annual-deadline',
            query: { userId: info.userId, year: this.year, writer: column.columnKey }
          })
        } else if (!info.useYn && info.adminDeadline) {
          this.$router.push({
            name: 'admin-annual-deadline',
            query: { userId: info.userId, year: this.year, writer: column.columnKey }
          })
        }
      } else {
        this.$router.push({ name: 'admin-vacation-detail', query: { userId: info.userId, year: this.year }})
      }
    },
    async senderVacationDeadline(row) {
      const data = {
        year: this.year,
        userId: row.userId
      }
      await api_vacation_deadline_sender(data).then(() => {
        this.$message({
          message: sender_message,
          type: 'success'
        })
        row.sender = true
      }).catch(() => {})
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getVacationInfoList()
    },
    getYear() {
      const day = new Date()
      this.year = this.$moment(day).format('YYYY')
    },
    handleSortChange(column) {
      const { prop, order } = column
      this.searchForm.sort = prop
      if (order === 'descending') {
        this.searchForm.seq = 'desc'
      } else if (order === 'ascending') {
        this.searchForm.seq = 'asc'
      } else {
        this.searchForm.sort = ''
        this.searchForm.seq = ''
      }
      this.getVacationInfoList()
    },
    tableRowClassName({ row }) {
      const hireDate = new Date(row.hireDate)
      const curDate = new Date()
      const btMs = curDate.getTime() - hireDate.getTime()
      if (!row.useYn) {
        return 'danger-row'
      } else if (btMs / (1000 * 60 * 60 * 24) < 365) {
        return 'warning-row'
      }
      return ''
    }
  }
}
</script>
<style>
  .el-table .warning-row {
    background: oldlace;
  }
  .el-table .danger-row {
    background: #FDE2E2;
  }
</style>
<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 10px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
