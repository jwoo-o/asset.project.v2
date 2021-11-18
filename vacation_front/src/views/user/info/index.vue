<template>
  <div class="app-container">
    <el-row :type="width < 760 ? '' : 'flex'" justify="center" :gutter="20">
      <el-col :xs="24" :sm="10" :lg="8">
        <el-card
          v-loading="loading"
          class="grid-wrapper"
          shadow="never"
          :body-style="{padding:0}"
          style="overflow: visible;"
        >
          <div class="header">
            <span>사용자 정보</span>
          </div>
          <el-form ref="userForm" size="small" :model="userInfo">
            <div class="content">
              <el-row>
                <el-col v-if="userInfo.imgSrc !== null" :xs="24" :sm="24" :lg="24" align="center" style="margin-bottom: 10px">
                  <el-image
                    style="width: 150px; height: 170px"
                    :src="userInfo.imgSrc"
                  />
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="이름" label-width="120px" prop="name">
                    <el-input v-model="userInfo.name" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="소속부서" label-width="120px" prop="orgCode">
                    <el-input v-model="userInfo.orgName" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="직급" label-width="120px" prop="rankIndex">
                    <el-input v-model="userInfo.rankNm" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="직책" label-width="120px" prop="rankIndex">
                    <el-input v-model="userInfo.jobNm" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="이메일" label-width="120px" prop="email">
                    <el-input v-model="userInfo.email" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="연락처" label-width="120px" prop="tel">
                    <el-input v-model="userInfo.tel" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="내선번호" label-width="120px" prop="tel">
                    <el-input v-model="userInfo.ex" :readonly="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24" align="right">
                  <el-form-item label="발급요청" label-width="120px" prop="tel">
                    <el-button @click="dialogVisible=true">증명서 발급</el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
      </el-col>
      <certificate-dialog
        :dialog-visible="dialogVisible"
        :cert-form="certForm"
        @handleClose="dialogVisible=false"
        @handleApprove="handleApprove"
      />
      <el-col :xs="24" :sm="14" :lg="10">
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
                <span style="margin: 10px">발급 내역</span>
              </el-col>
            </el-row>
          </div>
          <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
            <el-table
              :data="certificateList"
              style="width: 100%"
              size="small"
              :header-cell-style="{background:'#F5F7FA'}"
            >
              <el-table-column
                label="발급요청일"
                align="center"
                min-width="120"
              >
                <template slot-scope="{row}">
                  <span>{{ row.createdAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="종류"
                align="center"
                min-width="120"
              >
                <template slot-scope="{row}">
                  <span>{{ row.type }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="발급상태"
                align="center"
                min-width="120"
              >
                <template slot-scope="{ row }">
                  <el-button size="small" :disabled="row.approvalFlag !== 'APPROVE'" @click="handlePrint(row.id)">{{ row.approvalDesc }}</el-button>
                </template>
              </el-table-column>
            </el-table>
            <paging
              :total="total"
              :page.sync="searchForm.page"
              :limit.sync="searchForm.limit"
              @pagination="getCertificateList"
            />
          </el-card>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
const defaultCertForm = {
  type: '',
  purpose: '',
  submit: '',
  seal: true
}
import { job_cd, rank_cd } from '@/default/data'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { defaultSearchForm } from '@/default/data'
import { api_user_info } from '@/api/user/user'
import { api_certificate_request, api_certificate_userId, api_certificate_print, api_certificate_print_success } from '@/api/certificate'
import { mapGetters } from 'vuex'
import CertificateDialog from './components/certificateDialog'
import { approval_message, success_message } from '../message/constant'
export default {
  name: 'UserInfoIndex',
  components: { Paging, CertificateDialog },
  data() {
    return {
      isEdit: true,
      dialogVisible: false,
      searchForm: Object.assign({}, defaultSearchForm),
      total: 0,
      rankIndex: null,
      jobIndex: null,
      jobList: [],
      rankList: [],
      certificateList: [],
      loading: false,
      loading1: false,
      userInfo: {},
      certForm: Object.assign({}, defaultCertForm)
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  created() {
    this.getCommonList()
    this.getUserInfo()
  },
  mounted() {
    this.handleUserInfo()
    this.getCertificateList()
  },
  methods: {
    getCertificateList() {
      this.loading1 = true
      api_certificate_userId(this.$store.getters.userId, this.searchForm).then(response => {
        const { total, certificateList } = response.data
        this.certificateList = certificateList
        this.total = total
        this.loading1 = false
      }).catch(() => {
        this.loading1 = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getCertificateList()
    },
    async getCommonList() {
      this.rankList = this.$store.getters.commonCode.rank || rank_cd
      this.jobList = this.$store.getters.commonCode.job || job_cd
    },
    async getUserInfo() {
      this.loading = true
      api_user_info(this.$route.query.userId).then(response => {
        this.loading = false
        this.userInfo = response.data.info
      }).catch(() => {
        this.loading = false
      })
    },
    async handleUserInfo() {
      for (let i = 0; i < this.rankList.length; i++) {
        if (this.rankList[i].detailCode === this.userInfo.rankCd) {
          this.rankIndex = i
        }
      }
      for (let i = 0; i < this.jobList.length; i++) {
        if (this.jobList[i].detailCode === this.userInfo.jobCd) {
          this.jobIndex = i
        }
      }
    },
    handleApprove() {
      this.dialogVisible = false
      this.certForm.userId = this.userInfo.userId
      api_certificate_request(this.certForm).then(() => {
        this.$message({
          message: approval_message,
          type: 'success'
        })
        this.handleFilter()
      })
      this.certForm = Object.assign({}, defaultCertForm)
    },
    handlePrint(id) {
      api_certificate_print(id).then(response => {
        const printWin = window.open('about:blank')
        printWin.document.write(response.data.html)
        setTimeout(() => {
          printWin.print()
        }, 500)
        api_certificate_print_success(id).then(() => {
          this.$message({
            message: success_message,
            type: 'success'
          })
          this.handleFilter()
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
