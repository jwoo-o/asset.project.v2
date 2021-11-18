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
                        <el-button size="small" @click="dialogVisible=true">
                          발급 등록
                        </el-button>
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
                :data="certList"
                size="small"
                @row-click="handlePrint"
              >
                <el-table-column align="center" label="No" width="70">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.id }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="이름">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.userName }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="부서명">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.orgName }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="종류">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.type }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="용도">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.purpose }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="제출처">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.submit }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="직인여부" width="70">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      <el-icon style="font-size: 1.5rem;" :class="scope.row.seal ? 'el-icon-success' : 'el-icon-error'" />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="상태">
                  <template slot-scope="scope">
                    <div style="display:block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                      {{ scope.row.approvalDesc }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="발급요청일" width="150">
                  <template slot-scope="scope">
                    <span>{{ scope.row.createdAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="승인/반려" width="150" column-key="button">
                  <template slot-scope="{row}">
                    <el-button :disabled="row.approvalFlag !== 'WAIT'" type="primary" size="small" @click="handleApprovalRequest(row.id,'APPROVE')">승인</el-button>
                    <el-button :disabled="row.approvalFlag !== 'WAIT'" type="danger" size="small" @click="handleApprovalRequest(row.id,'REJECT')">반려</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <paging
                :total="total"
                :page.sync="searchForm.page"
                :limit.sync="searchForm.limit"
                @pagination="getCertList"
              />
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <certificate-dialog
      :dialog-visible="dialogVisible"
      :cert-form="certForm"
      @handleClose="dialogVisible=false"
      @handleApprove="handleApprove"
    />
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import orgTree from '@/components/OrgTree'
import Paging from '@/components/Pagination/index'
import { api_certificate_list, api_certificates_request, api_certificate_print, api_certificate_state } from '@/api/certificate'
import { defaultSearchForm } from '@/default/data'
import { mapGetters } from 'vuex'
import CertificateDialog from './components/certificateDialog'
import { success_message } from '../message/constant'
const defaultCertForm = {
  type: '',
  purpose: '',
  submit: '',
  seal: true,
  userId: undefined
}
export default {
  name: 'CertList',
  directives: { elDragDialog },
  components: {
    CertificateDialog,
    Paging,
    orgTree
  },
  data() {
    return {
      certList: [],
      total: 0,
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      loading1: false,
      loading2: false,
      dialogVisible: false,
      certForm: Object.assign({}, defaultCertForm)
    }
  },
  computed: {
    ...mapGetters([
      'level'
    ])
  },
  created() {
    this.getCertList()
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
    async getCertList() {
      this.loading = true
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.mgrOrgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.mgrOrgName
      }
      await api_certificate_list(this.searchForm).then(response => {
        const { total, certificateList } = response.data
        this.certList = certificateList
        this.total = total
        this.loading = false
        this.$store.dispatch('alarm/alarmCheck')
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getCertList()
    },
    handleApprove() {
      this.dialogVisible = false
      this.certForm.adminId = this.$store.getters.adminId
      api_certificates_request(this.certForm).then(() => {
        this.$message({
          message: success_message,
          type: 'success'
        })
        this.handleFilter()
      })
      this.certForm = Object.assign({}, defaultCertForm)
    },
    handlePrint(row, column) {
      if (column.columnKey !== 'button') {
        api_certificate_print(row.id).then(response => {
          const printWin = window.open('about:blank')
          printWin.document.write(response.data.html)
          setTimeout(() => {
            printWin.print()
            printWin.close()
          }, 500)
        })
      }
    },
    handleApprovalRequest(id, state) {
      const data = {
        approvalFlag: state,
        adminId: this.$store.getters.adminId
      }
      api_certificate_state(id, data).then(() => {
        this.$message({
          message: success_message,
          type: 'success'
        })
        this.handleFilter()
        this.$store.dispatch('alarm/alarmCheck')
      })
    }
  }
}
</script>

<style scoped>

</style>
