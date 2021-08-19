<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
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
            <span style="margin: 10px"> 휴가승인현황</span>
          </div>
          <el-form :model="searchForm" label-position="left">
            <el-col :xs="24" :sm="24" :lg="24">
              <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-radio-group v-model="radio" @change="getVacationListByApproveState">
                    <el-radio :style="{width: mobile ? '100px' : '', marginBottom: mobile ? '10px' : ''}" :label="''">전체</el-radio>
                    <el-radio :style="{width: mobile ? '100px' : '', marginBottom: mobile ? '10px' : ''}" :label="'WAIT'">상신</el-radio>
                    <el-radio :style="{width: mobile ? '100px' : '', marginBottom: mobile ? '10px' : ''}" :label="'ING'">진행중</el-radio>
                    <el-radio :style="{width: mobile ? '100px' : '', marginBottom: mobile ? '10px' : ''}" :label="'APPROVE'">승인완료</el-radio>
                    <el-radio :style="{width: mobile ? '100px' : '', marginBottom: mobile ? '10px' : ''}" :label="'REJECT'">반려</el-radio>
                  </el-radio-group>
                </el-col>
              </el-card>
            </el-col>
            <el-card v-if="!mobile" shadow="never" class="grid-wrapper">
              <el-row>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-row :gutter="20">
                    <el-col :xs="9" :sm="4" :lg="4">
                      <el-form-item size="small">
                        <el-select v-model="searchForm.searchType" style="width: 100%;">
                          <el-option value="" label="전체">전체</el-option>
                          <el-option value="userId" label="ID">ID</el-option>
                          <el-option value="name" label="이름">이름</el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="9" :sm="5" :lg="5">
                      <el-form-item size="small">
                        <el-input
                          v-model="searchForm.searchWord"
                          :disabled="searchForm.searchType === ''"
                          @keyup.enter.native="handleFilter"
                        />
                      </el-form-item>
                    </el-col>
                    <el-button
                      icon="el-icon-search"
                      size="small"
                      @click.native.prevent="handleFilter"
                    >검색
                    </el-button>
                  </el-row>
                </el-col>
              </el-row>
            </el-card>
          </el-form>
          <el-card
            shadow="never"
            class="grid-wrapper scroll"
            :style="{
              height: mobile ? height-350+'px' : '550px'
            }"
            :body-style="{padding:'5px'}"
          >
            <el-table
              :data="approverVacationList"
              :height="mobile ? height-420 : 450"
              width="100%"
              size="small"
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
                v-if="width > 850"
                :key="1"
                prop="file"
                label="첨부"
                align="center"
                min-width="50"
              >
                <template slot-scope="{ row}">
                  <span v-if="row.attach" class="el-icon-s-claim" style="font-size: 20px" />
                </template>
              </el-table-column>
              <el-table-column
                label="기안자"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.userName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                v-if="width > 850"
                :key="2"
                label="작성일자"
                align="center"
                min-width="140"
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
                    <span>{{ row.vacationKind }}</span>
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
                  prop="dayCnt"
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
                prop="state"
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
              @pagination="getApproverList"
            />
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { defaultSearchForm } from '@/default/data'
import { api_approver_vacation_list } from '@/api/approver/approver'
import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      radio: '',
      approverVacationList: [],
      total: 0,
      id: ''
    }
  },
  computed: {
    ...mapGetters([
      'width',
      'mobile',
      'height'
    ])
  },
  created() {
    this.getApproverList()
  },
  methods: {
    async logout() {
      await this.$store.dispatch('admin/logout')
      location.reload()
    },
    handleSelectionVacation(info) {
      this.$router.push({ name: 'approve-check', query: { vacationId: info.vacationId, userId: info.userId, orderPosition: info.orderPosition }})
    },
    async getApproverList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.orgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.orgName
      }
      this.searchForm.userId = this.$store.getters.userId
      this.searchForm.userName = this.$store.getters.userName
      await api_approver_vacation_list(this.searchForm).then(response => {
        const { approverVacationList, total } = response.data
        this.approverVacationList = approverVacationList
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getApproverList()
    },
    async getVacationListByApproveState(data) {
      this.loading = true
      this.searchForm.approveState = data
      this.searchForm.page = 1
      this.searchForm.limit = 10
      this.searchForm.order = 1
      this.searchForm.offset = 1
      await this.getApproverList()
    }

  }
}
</script>

<style lang="scss" scoped>
</style>
