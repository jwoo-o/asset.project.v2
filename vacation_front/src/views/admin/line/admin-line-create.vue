<template>
  <div class="app-container">
    <el-row :gutter="15" type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="6">
        <el-card shadow="never" class="grid-wrapper" style="overflow: visible;">
          <div slot="header" class="grid-header">
            <span>결재부서지정</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :lg="24">
              <el-form ref="groupCodeForm" :model="groupCodeForm" :rules="groupCodeRule" style="height: 300px">
                <el-form-item label-width="100px" label="부서코드" size="small" prop="orgCode">
                  <treeselects
                    v-model="groupCodeForm.orgCode"
                    :options="$store.getters.orgList"
                    :default-expand-level="1"
                    placeholder="부서 선택"
                    style="font-size: small;"
                    @select="handleNodeClick"
                  />
                </el-form-item>
                <el-form-item label-width="100px" label="직책" size="small" prop="jobCode">
                  <el-select
                    v-model="groupCodeForm.jobCode"
                    placeholder="직책 선택"
                    size="small"
                    style="width: 100%;"
                    @change="handleJob"
                  >
                    <el-option
                      v-for="item in this.$store.getters.commonCode.job.filter(value => value.detailName !== '대표이사')"
                      :key="item.detailCode"
                      :label="item.detailName"
                      :value="item.detailCode"
                    >
                      <span style="float: left">{{ item.detailName }}</span>
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-row>
                  <el-col :xs="24" :sm="24" :lg="24" align="right">
                    <el-button
                      align="right"
                      size="small"
                      :loading="loading1"
                      style="margin-right: 10px"
                      @click="handleCommonConfirm"
                    >저장
                    </el-button>
                    <router-link :to="{name: 'admin-approve-line'}">
                      <el-button size="small" icon="el-icon-folder">목록</el-button>
                    </router-link>
                  </el-col>
                </el-row>
              </el-form>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card shadow="never" class="grid-wrapper">
          <div slot="header" class="grid-header">
            <span>결재라인지정</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :lg="24" align="right">
              <div style="margin-bottom: 20px;">
                <el-col :xs="5" :sm="4" :lg="4">
                  <el-select
                    v-model="detail.division"
                    size="small"
                    placeholder="권한 선택"
                    style="width: 100%;"
                    @change="handleDivision"
                  >
                    <el-option value="approver" label="결재권자">결재권자</el-option>
                    <el-option value="cc" label="참조자">참조자</el-option>
                  </el-select>
                </el-col>
                <el-col :xs="5" :sm="4" :lg="4">
                  <el-select
                    v-model="userId"
                    :disabled="groupCodeForm.orgCode === undefined || groupCodeForm.jobCode === ''"
                    placeholder="직원 선택"
                    size="small"
                    style="width: 100%;"
                    @change="handleUserData"
                  >
                    <el-option
                      v-for="item in userList.filter(item => (item.jobCd > groupCodeForm.jobCode) && item.useYn)"
                      :key="item.userId"
                      :label="item.name"
                      :value="item.userId"
                    />
                  </el-select>
                </el-col>
                <el-button
                  :disabled="userId === ''"
                  icon="el-icon-plus"
                  size="small"
                  @click="handleAddDetail"
                />
                <el-button
                  v-if="selectRow !== '' && selectRow !== 0"
                  size="small"
                  icon="el-icon-top"
                  @click.native.prevent="handleOrderChange(selectRow, detailCodes,'UP')"
                />
                <el-button
                  v-if="selectRow !== '' && selectRow !== detailCodes.length-1"
                  size="small"
                  icon="el-icon-bottom"
                  @click.native.prevent="handleOrderChange(selectRow, detailCodes,'DOWN')"
                />
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-table
                ref="detailTable"
                v-loading="loading"
                style="width: 100%;"
                height="300"
                :header-cell-style="{background:'#F5F7FA'}"
                border
                :data="detailCodes"
                size="small"
              >
                <el-table-column
                  width="55"
                >
                  <template slot-scope="scope">
                    <el-checkbox v-model="scope.row.selectRow" @change="handleSelection(scope.$index,scope.row)" />
                  </template>
                </el-table-column>
                <el-table-column
                  label="구분"
                  align="center"
                  min-width="50"
                >
                  <template slot-scope="{row}">
                    <span v-if="row.division === 'cc'">참조자</span>
                    <span v-else>결재권자</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="부서"
                  align="center"
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.orgName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="직급"
                  align="center"
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.rankNm }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="직책"
                  align="center"
                  min-width="100"
                >
                  <template slot-scope="{row}">
                    <span>{{ row.jobName }}</span>
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
                <el-table-column
                  label="삭제"
                  width="70"
                  align="center"
                >
                  <template slot-scope="scope">
                    <el-button
                      size="small"
                      icon="el-icon-minus"
                      @click.native.prevent="handleDeleteDetail(scope.$index, detailCodes)"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { necessary_message, register_message } from '@/views/admin/message/constant'
import {
  api_approver_check,
  api_approver_detail,
  api_approver_edit,
  api_approver_register
} from '@/api/approverGroup/approverGroup'
import { mapGetters } from 'vuex'

export default {
  name: 'AdminLineCreate',
  components: {
    Treeselects
  },
  data() {
    const validate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    return {
      selectRow: '',
      groupCodeForm: {
        approverGroupCode: '',
        orgCode: undefined,
        orgName: '',
        useYn: true,
        detailCodes: [],
        jobCode: '',
        jobName: '',
        approver: '',
        cc: ''
      },
      groupCodeRule: {
        orgCode: [{ required: true, trigger: 'change', validator: validate }],
        jobCode: [{ required: true, trigger: 'change', validator: validate }]
      },
      detail: {
        approverDetailCode: '',
        division: '',
        approverGroupCode: '',
        orgCode: '',
        orgName: '',
        rankCd: '',
        rankNm: '',
        order: 1,
        userId: '',
        userName: '',
        jobName: '',
        jobCode: '',
        email: ''
      },
      orgPaCode: '',
      userId: '',
      detailCodeForm: {},
      detailCodes: [],
      handleUserInfo: {},
      list: [],
      division: '',
      job: '',
      jobName: '',
      total: 0,
      loading: false,
      loading1: false,
      isEdit: false,
      isAdd: false,
      isOrgCodeValid: false,
      checkApproverGroupFlag: false
    }
  },
  computed: {
    ...mapGetters([
      'userList'
    ])
  },
  created() {
    if (this.$route.query.approverGroupCode) {
      this.isEdit = true
      this.getApproverDetailList(this.$route.query.approverGroupCode)
      this.detail.approverGroupCode = this.$route.query.approverGroupCode
    } else {
      this.groupCodeForm.orgCode = this.$route.params.orgCode
      this.groupCodeForm.orgName = this.$route.params.orgName
    }
  },
  methods: {
    handleOrderChange(index, rows, direction) {
      if (direction === 'UP') {
        const tmp = rows[index]
        rows[index] = rows[index - 1]
        rows[index - 1] = tmp
        this.selectRow = index - 1
      } else {
        const tmp = rows[index]
        rows[index] = rows[index + 1]
        rows[index + 1] = tmp
        this.selectRow = index + 1
      }
      this.handleDoLayOut()
    },
    handleSelection(index, row) {
      if (row.selectRow) {
        this.selectRow = index
        for (const i of this.detailCodes.keys()) {
          if (i !== index) {
            this.detailCodes[i].selectRow = false
          }
        }
      } else {
        this.selectRow = ''
      }
      this.handleDoLayOut()
    },
    handleDoLayOut() {
      this.detailCodeForm = Object.assign({}, this.detail)
      this.detailCodes.push(this.detailCodeForm)
      this.detailCodes.splice(this.detailCodes.length - 1, 1)
    },
    handleNodeClick(data) {
      this.groupCodeForm.orgName = data.orgName
      this.groupCodeForm.orgCode = data.orgCode
      this.orgPaCode = data.orgPaCode
      this.groupCodeForm.jobCode = ''
    },
    handleJob(data) {
      for (let i = 0; i < this.$store.getters.commonCode.job.length; i++) {
        if (data === this.$store.getters.commonCode.job[i].detailCode) {
          this.groupCodeForm.jobName = this.$store.getters.commonCode.job[i].detailName
        }
      }
      this.groupCodeForm.jobCode = data
    },
    handleUserData(data) {
      for (const user of this.userList) {
        if (user.userId === data) {
          this.detail.userId = user.userId
          this.detail.userName = user.name
          this.detail.rankCd = user.rankCd
          this.detail.rankNm = user.rankNm
          this.detail.orgCode = user.orgCode
          this.detail.orgName = user.orgName
          this.detail.jobName = user.jobNm
          this.detail.jobCode = user.jobCd
          this.detail.email = user.email
          break
        }
      }
    },
    handleDivision(data) {
      this.detail.division = data
    },
    async getApproverDetailList(groupCode) {
      this.loading = true
      await api_approver_detail(groupCode).then(response => {
        this.loading = false
        this.groupCodeForm = response.data
        if (this.groupCodeForm.detailCodes !== null) {
          this.detailCodes = this.groupCodeForm.detailCodes
        }
        this.orgPaCode = this.groupCodeForm.orgPaCode
      }).catch(() => {
        this.loading = false
      })
    },
    handleAddDetail() {
      if (this.handleDetailCodeOverlap()) {
        this.detailCodeForm = Object.assign({}, this.detail)
        this.detailCodes.push(this.detailCodeForm)
        this.detail.order = this.detail.order + 1
        this.userId = ''
        this.detail.division = ''
      }
    },
    handleDeleteDetail(index, rows) {
      rows.splice(index, 1)
    },
    handleCommonConfirm() {
      this.detailCodes.forEach((v, i, a) => {
        const { division, orgCode, orgName, rankCd, rankNm, userId, userName } = v
        if (division === '' || orgCode === '' || orgName === '' || rankCd === '' || rankNm === '' || userId === '' || userName === '') {
          a.splice(i, 1)
        }
      })
      this.$refs.groupCodeForm.validate(async valid => {
        if (valid) {
          this.loading1 = true
          api_approver_check(this.groupCodeForm.orgCode, this.groupCodeForm.jobCode)
            .then(response => {
              if (response.data) {
                this.$confirm('이미 결재라인이 지정된 부서의 직책입니다. 저장하시겠습니까?', 'Warning', {
                  confirmButtonText: '확인',
                  cancelButtonText: '취소',
                  type: 'warning'
                }).then(() => {
                  this.createApproverLineName()
                  api_approver_edit(this.groupCodeForm).then(() => {
                    this.loading1 = false
                    this.$message({
                      type: 'success',
                      message: register_message
                    })
                    this.isEdit = true
                    /*
                          this.$router.push({ name: 'admin-approve-line' })
        */
                  }).catch(() => {
                    this.loading1 = false
                  })
                }).catch(() => {
                  this.loading1 = false
                  this.$message({
                    type: 'info',
                    message: '취소되었습니다.'
                  })
                })
              } else {
                this.createApproverLineName()
                api_approver_register(this.groupCodeForm).then(() => {
                  this.loading1 = false
                  this.$message({
                    type: 'success',
                    message: register_message
                  })
                  this.isEdit = true
                  this.$router.push({ name: 'admin-approve-line' })
                }).catch(() => {
                  this.loading1 = false
                })
              }
            }).catch(() => {})
        }
      })
    },
    handleDetailCodeOverlap() {
      for (let i = 0; i < this.detailCodes.length; i++) {
        if (this.detailCodes[i].userId === this.detail.userId) {
          this.$message({
            type: 'warning',
            message: '이미 설정된 결재자 입니다.(' + this.detail.userName + ')'
          })
          return false
        }
      }
      return true
    },
    createApproverLineName() {
      this.groupCodeForm.detailCodes = this.detailCodes
      this.groupCodeForm.approver = ''
      this.groupCodeForm.cc = ''
      const approver = []
      const cc = []
      for (let i = 0; i < this.groupCodeForm.detailCodes.length; i++) {
        if (this.groupCodeForm.detailCodes[i].division === 'cc') {
          cc.push(this.groupCodeForm.detailCodes[i])
        } else {
          approver.push(this.groupCodeForm.detailCodes[i])
        }
      }
      for (let i = 0; i < approver.length; i++) {
        if (i === approver.length - 1) {
          this.groupCodeForm.approver = this.groupCodeForm.approver + approver[i].userName
        } else {
          this.groupCodeForm.approver = this.groupCodeForm.approver + approver[i].userName + '->'
        }
      }

      for (let i = 0; i < cc.length; i++) {
        if (i === cc.length - 1) {
          this.groupCodeForm.cc = this.groupCodeForm.cc + cc[i].userName
        } else {
          this.groupCodeForm.cc = this.groupCodeForm.cc + cc[i].userName + '->'
        }
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
