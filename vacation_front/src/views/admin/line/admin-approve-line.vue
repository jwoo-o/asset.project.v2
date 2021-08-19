<template>
  <div>
    <div class="app-container">
      <el-row :gutter="15">
        <el-col :xs="24" :sm="24" :lg="6">
          <org-tree :org-code="searchForm.orgCode" @orgTree="getOrgInfo" />
        </el-col>
        <el-col :xs="24" :sm="24" :lg="18">
          <el-card
            v-loading="loading"
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
                <el-col :xs="8" :sm="14" :lg="16">
                  <span style="margin: 10px">{{ searchForm.orgName }} 결재라인</span>
                </el-col>
              </el-row>
            </div>
            <el-card shadow="never" class="grid-wrapper">
              <el-form>
                <el-row>
                  <el-col :xs="24" :sm="24" :lg="24">
                    <el-row>
                      <el-col :xs="24" :sm="24" :lg="24" align="right">
                        <router-link :to="{name: 'admin-line-create',params:{orgCode:searchForm.orgCode,orgName:searchForm.orgName}}">
                          <el-button size="small" style="margin-right: 15px">추가</el-button>
                        </router-link>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-form>
            </el-card>
            <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
              <el-table
                size="small"
                :data="approverGroupList"
                height="580"
                style="width: 100%;"
                :header-cell-style="{background:'#F5F7FA'}"
              >
                <el-table-column
                  label="팀명"
                  align="center"
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.orgName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="직책"
                  align="center"
                  min-width="50"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.jobName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="결재자"
                  align="center"
                  min-width="70"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.approver }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="참조자"
                  align="center"
                  min-width="120"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.cc }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="편집"
                  align="center"
                  width="70"
                >
                  <template slot-scope="{row}">
                    <router-link
                      :to="{path: '/admin-line-create', query:{approverGroupCode : row.approverGroupCode}}"
                      class="link-type"
                    >
                      <el-button
                        type="primary"
                        size="small"
                        icon="el-icon-edit-outline"
                      />
                    </router-link>
                  </template>
                </el-table-column>
                <el-table-column
                  label="사용안함"
                  align="center"
                  width="70"
                >
                  <template slot-scope="{row}">
                    <el-button
                      type="danger"
                      size="small"
                      icon="el-icon-edit-outline"
                      @click="handleDelete(row)"
                    />
                  </template>
                </el-table-column>
              </el-table>
              <paging
                :total="total"
                :page.sync="searchForm.page"
                :limit.sync="searchForm.limit"
                @pagination="getApproverGroupList"
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
import { api_approver_group_list } from '@/api/approverGroup/approverGroup'
import { defaultSearchForm } from '@/default/data'
import OrgTree from '@/components/OrgTree/index'
import { api_approvel_group_delete } from '@/api/approverGroup/approverGroup'
import { delete_message } from '../message/constant'

export default {
  name: 'AdminApproveLine',
  components: {
    OrgTree,
    Paging
  },
  data() {
    return {
      loading: false,
      searchForm: Object.assign({}, defaultSearchForm),
      approverGroupList: [],
      total: 0
    }
  },
  created() {
    this.getApproverGroupList()
  },
  methods: {
    async getApproverGroupList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.mgrOrgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.mgrOrgName
      }
      await api_approver_group_list(this.searchForm).then(response => {
        const { approverGroupList, total } = response.data
        this.approverGroupList = approverGroupList
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    async handleDelete(value) {
      await api_approvel_group_delete(value.approverGroupCode).then(() => {
        this.$message({
          type: 'success',
          message: delete_message
        })
        this.handleFilter()
      }).catch(() => {})
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getApproverGroupList()
    },
    getOrgInfo(data) {
      if (this.searchForm.orgCode !== data.orgCode) {
        this.searchForm = Object.assign({}, defaultSearchForm)
        this.searchForm.orgCode = data.orgCode
        this.searchForm.orgName = data.orgName
        this.searchForm.order = data.order
        this.searchForm.searchType = ''
        this.searchForm.searchWord = ''
        this.handleFilter()
      }
    }
  }
}
</script>

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
