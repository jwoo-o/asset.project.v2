<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :xs="24" :sm="24" :lg="6">
        <org-tree :org-code="searchForm.orgCode" @orgTree="getOrgInfo" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="18">
        <el-row>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-card shadow="never" class="grid-wrapper" style="margin-bottom: 10px">
              <el-form :model="searchForm" label-position="left">
                <el-row>
                  <el-col :xs="24" :sm="12" :lg="10">
                    <el-form-item label="검색 부서" label-width="80px">
                      <el-input v-model="searchForm.orgName" disabled size="small" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :xs="24" :sm="18" :lg="14">
                    <el-row :gutter="20">
                      <el-col :xs="5" :sm="5" :lg="5">
                        <el-select v-model="searchForm.searchType" size="small" @change="searchForm.searchWord=''">
                          <el-option label="전체" value="" />
                          <el-option label="ID" value="userId" />
                          <el-option label="이름" value="name" />
                          <el-option label="퇴사자" value="use" />
                        </el-select>
                      </el-col>
                      <el-col v-if="searchForm.searchType !== 'use'" :xs="8" :sm="10" :lg="10">
                        <el-input
                          v-model="searchForm.searchWord"
                          :disabled="searchForm.searchType === ''"
                          size="small"
                          @keyup.enter.native="handleFilter"
                        />
                      </el-col>
                      <el-col :xs="11" :sm="9" :lg="9">
                        <el-button
                          size="small"
                          icon="el-icon-search"
                          @click.native.prevent="handleFilter"
                        >검색
                        </el-button>
                        <router-link :to="{name:'user-edit'}" style="margin-left: 10px;">
                          <el-button size="small" icon="el-icon-s-check">
                            등록
                          </el-button>
                        </router-link>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-form>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-card shadow="never" :body-style="{padding:'10px'}" style="height: 707px;">
              <el-table
                v-loading="loading"
                style="width: 100%"
                height="600px"
                :header-cell-style="{background:'#F5F7FA'}"
                :data="userList"
                size="small"
                @sort-change="handleSortChange"
              >
                <el-table-column align="center" label="아이디" sortable prop="userId">
                  <template slot-scope="scope">
                    {{ scope.row.userId }}
                  </template>
                </el-table-column>
                <el-table-column align="center" label="이름" sortable prop="userName">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.userName }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="부서명" sortable prop="orgName">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.orgName }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="직위/직책" sortable prop="rankCd">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.rankNm }}{{ scope.row.jobNm !== undefined ? '/'+scope.row.jobNm : '' }}
                    </div>
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
                <el-table-column align="center" label="등록일/수정일" width="160">
                  <template slot-scope="scope">
                    <span><i class="el-icon-plus" style="margin-right: 5px" />{{ scope.row.createdAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                    <br>
                    <span><i class="el-icon-edit" style="margin-right: 5px" />{{ scope.row.modifiedAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="수정" width="100">
                  <template slot-scope="{row}">
                    <el-button :disabled="!row.useYn" type="primary" size="small" icon="el-icon-edit-outline" @click="handleEdit(row.userId)" />
                  </template>
                </el-table-column>
                <el-table-column align="center" label="비밀번호 초기화" width="130">
                  <template slot-scope="{row}">
                    <el-button :disabled="!row.useYn" size="small" icon="el-icon-refresh-right" @click="handlePwdExpire(row.userId)" />
                  </template>
                </el-table-column>
                <el-table-column align="center" label="퇴사처리" width="100">
                  <template slot-scope="{row}">
                    <el-button icon="el-icon-minus" size="small" type="danger" @click="handleDelete(row)" />
                  </template>
                </el-table-column>
              </el-table>
              <paging
                :total="total"
                :page.sync="searchForm.page"
                :limit.sync="searchForm.limit"
                @pagination="getUserList"
              />
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import orgTree from '@/components/OrgTree'
import Paging from '@/components/Pagination/index'
import { api_user_delete, api_user_list, api_user_pwd_edit } from '@/api/user/user'
import { edit_message, pwd_expire_message, user_leave_message, user_pwd_change_message } from '../message/constant'
import { defaultSearchForm } from '@/default/data'
import { mapGetters } from 'vuex'

export default {
  name: 'User',
  directives: { elDragDialog },
  components: {
    Paging,
    orgTree
  },
  data() {
    return {
      userList: [],
      total: 0,
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      loading1: false,
      loading2: false,
      useYnView: false
    }
  },
  computed: {
    ...mapGetters([
      'level'
    ])
  },
  created() {
    this.getUserList()
  },
  methods: {
    getOrgInfo(data) {
      if (this.searchForm.orgCode !== data.orgCode) {
        // this.searchForm = Object.assign({}, defaultSearchForm)
        this.searchForm.orgCode = data.orgCode
        this.searchForm.orgName = data.orgName
        this.searchForm.order = data.order
        this.handleFilter()
      }
    },
    async getUserList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.mgrOrgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.mgrOrgName
      }
      await api_user_list(this.searchForm).then(response => {
        this.useYnView = this.searchForm.searchType === 'use'
        const { userList, total } = response.data
        this.userList = userList
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getUserList()
    },
    handleEdit(userId) {
      this.$router.push({ path: 'user-edit', query: { userId: userId }})
    },
    handlePwdExpire(userId) {
      this.$confirm(user_pwd_change_message.replace(/(?:\r\n|\r|\n)/g, '<br />'), '초기화', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        dangerouslyUseHTMLString: true,
        type: 'warning'
      }).then(async() => {
        this.loading1 = true
        api_user_pwd_edit({ userId: userId }).then(() => {
          this.loading1 = false
          this.$message({
            type: 'success',
            message: pwd_expire_message
          })
        }).catch(() => {
          this.loading1 = false
        })
      }, function() {
      })
    },
    handleDelete(row) {
      this.$prompt(row.useYn ? '퇴사처리하시겠습니까?' : '퇴사일 변경하시겠습니까?', row.useYn ? '퇴사?' : '변경?', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        inputPattern: /^(19|20)\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$/,
        inputErrorMessage: '올바른 날짜를 입력하세요(YYYY-MM-dd)'
      }).then(async(value) => {
        if (row.hireDate <= value.value) {
          await api_user_delete(row.userId, value.value).then(() => {
            this.$message({
              type: 'success',
              message: row.useYn ? user_leave_message : edit_message
            })
            this.getUserList()
          })
        } else {
          this.$message({
            type: 'warning',
            message: '입사일보다 퇴사일이 이전입니다'
          })
        }
      }, function() {
      })
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
      this.getUserList()
    }
  }
}
</script>

<style scoped>

</style>
