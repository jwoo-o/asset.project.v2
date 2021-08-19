<template>
  <div class="app-container">
    <el-row v-if="!isEdit" :gutter="15">
      <el-col :xs="24" :sm="24" :lg="6">
        <org-tree @orgTree="getOrgInfo" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="10">
        <el-card :body-style="{padding : '0px'}" style="height: 350px;overflow: visible" shadow="never" class="grid-wrapper">
          <div class="header">
            <span>부서 정보</span>
          </div>
          <div class="content">
            <el-form label-width="100px" style="margin-top: 20px;" size="small">
              <el-form-item label="부서코드">
                <el-input v-model="selectOrg.orgCode" readonly />
              </el-form-item>
              <el-form-item label="부서이름">
                <el-input v-model="selectOrg.orgName" :readonly="selectOrg.orgCode === '-1' || selectOrg.orgCode === '00000000' || selectOrg.orgCode === ''" />
              </el-form-item>
              <el-form-item v-if="selectOrg.orgCode !== '00000000' && selectOrg.orgCode !== '-1' &&selectOrg.orgCode !== '' " label="상위부서">
                <treeselects
                  v-model="selectOrg.orgPaCode"
                  :options="$store.getters.orgList"
                  placeholder="선택"
                  :clearable="false"
                  :default-expand-level="1"
                  style="font-size: small;width: 100%"
                  @select="handleNodeClick"
                />
              </el-form-item>
              <el-form-item v-if="colorShow" label="좌석 색상표">
                <el-color-picker v-model="selectOrg.color" />
              </el-form-item>
            </el-form>
            <el-row>
              <el-col align="right">
                <el-button
                  v-if="isUpdate"
                  :loading="loading2"
                  type="warning"
                  icon="el-icon-check"
                  size="small"
                  @click="updateOrg"
                >부서 수정
                </el-button>
                <el-button
                  type="primary"
                  icon="el-icon-plus"
                  size="small"
                  @click="createOrg"
                >하위부서 추가
                </el-button>
                <el-button
                  v-if="isTop"
                  type="danger"
                  size="small"
                  icon="el-icon-minus"
                  :loading="loading1"
                  @click="handleDeleteOrganization"
                >부서 삭제
                </el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row v-else>
      <el-card shadow="never" class="grid-wrapper" :body-style="{padding:0}">
        <div class="header">
          <span>하위부서 추가</span>
        </div>
        <div class="content">
          <el-row>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-form ref="orgForm" :model="orgForm" :rules="orgRule">
                <el-form-item label="상위 부서코드" label-width="120px">
                  <el-input v-model="orgForm.orgPaCode" disabled />
                </el-form-item>
                <el-form-item label="상위 부서명" label-width="120px">
                  <el-input v-model="orgForm.orgPaName" disabled />
                </el-form-item>
                <el-form-item label="부서코드" label-width="120px" prop="orgCode">
                  <el-input v-model="orgCode" disabled @blur="isOrgCodeValid=false" />
                </el-form-item>
                <el-form-item label="부서이름" label-width="120px" prop="orgName">
                  <el-input v-model="orgName" />
                </el-form-item>
                <el-form-item label="전체이름" label-width="120px">
                  <el-input v-model="orgForm.orgFullName" disabled />
                </el-form-item>
                <el-form-item label="전체코드" label-width="120px">
                  <el-input v-model="orgForm.orgFullCode" disabled />
                </el-form-item>
                <el-form-item label="순서" label-width="120px">
                  <el-input v-model="orgForm.order" disabled />
                </el-form-item>
              </el-form>
            </el-col>
            <el-col :xs="24" :sm="24" :lg="24">
              <div style="text-align:right;">
                <el-button
                  type="primary"
                  :loading="loading"
                  size="small"
                  icon="el-icon-check"
                  @click="handleAddOrganization"
                >확인
                </el-button>
                <el-button size="small" icon="el-icon-close" @click="handleClose">취소</el-button>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'
import orgTree from '@/components/OrgTree'
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  alreadyCode_message,
  delete_message,
  necessary_message,
  org_select_message,
  register_message,
  edit_message
} from '../message/constant'
import {
  api_organization_org_code_check,
  api_organization_register,
  api_organization_delete,
  api_organization_org_code,
  api_organization_update,
  // eslint-disable-next-line no-unused-vars
  api_organization_order
} from '@/api/organization/organization'
const defaultForm = {
  orgName: '',
  orgCode: '',
  orgPaCode: '',
  orgPaName: '',
  order: 0,
  orgFullName: '',
  orgFullCode: ''
}
export default {
  name: 'Organization',
  directives: { elDragDialog },
  components: { orgTree, Treeselects },
  data() {
    const validateCommon = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    const validateOrgCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        if (!this.isOrgCodeValid) {
          api_organization_org_code_check(value).then(response => {
            const isAlready = response.data
            if (isAlready) {
              callback(new Error(alreadyCode_message))
            } else {
              this.isOrgCodeValid = true
              callback()
            }
          })
        } else {
          callback()
        }
      }
    }
    return {
      isTop: false,
      options: null,
      colorShow: false,
      dialogVisible: false,
      orgForm: Object.assign({}, defaultForm),
      orgRule: {
        orgName: [{ required: true, trigger: 'blur', validator: validateCommon }],
        orgCode: [{ required: true, trigger: 'change', validator: validateOrgCode }]
      },
      isEdit: false,
      isUpdate: false,
      orgCode: '',
      orgName: '',
      orgFullName: '',
      orgFullCode: '',
      selectOrg: {
        orgName: '',
        orgCode: '',
        orgPaCode: '',
        orgPaName: '',
        order: 0,
        orgFullName: '',
        orgFullCode: '',
        color: ''
      },
      orgList: [],
      orgCash: {},
      selectPaOrgList: [],
      loading: false,
      loading1: false,
      loading2: false,
      loading3: false,
      isOrgCodeValid: false
    }
  },
  watch: {
    'selectOrg.color': function(val) {
      if (val !== this.orgCash.color) {
        this.isUpdate = true
      } else {
        this.isUpdate = false
      }
    },
    'selectOrg.orgName': function(val) {
      if (val !== this.orgCash.orgName) {
        this.isUpdate = true
      } else {
        this.isUpdate = false
      }
      this.selectOrg.orgFullName = this.orgFullName + '>' + val
    },
    'selectOrg.orgPaCode': function(val) {
      if (val === this.orgCash.orgCode) {
        setTimeout(() => {
          Object.assign(this.selectOrg, this.orgCash)
        }, 200)
        this.$message({
          type: 'warning',
          message: '상위부서를 같은부서로 선택할 수 없습니다'
        })
      } else {
        if (val !== this.orgCash.orgPaCode || this.selectOrg.orgName !== this.orgCash.orgName) {
          this.isUpdate = true
        } else {
          this.isUpdate = false
        }
      }
    },
    orgCode: {
      handler(value) {
        this.orgForm.orgCode = value
        if (value !== '') {
          this.orgForm.orgFullCode = this.orgFullCode + ',' + value
        } else {
          this.orgForm.orgFullCode = this.orgFullCode
        }
      }
    },
    orgName: {
      handler(value) {
        this.orgForm.orgName = value
        if (value !== '') {
          this.orgForm.orgFullName = this.orgFullName + '>' + value
        } else {
          this.orgForm.orgFullName = this.orgFullName
        }
      }
    }
  },
  methods: {
    createOrg() {
      if (this.selectOrg.orgCode === '') {
        this.$message({
          type: 'warning',
          message: org_select_message
        })
      } else {
        api_organization_org_code().then(response => {
          this.dialogVisible = true
          this.orgForm.orgPaCode = this.selectOrg.orgCode
          this.orgForm.orgPaName = this.selectOrg.orgName
          this.orgCode = response.data
          this.orgName = ''
          this.isOrgCodeValid = true
          this.orgForm.order = this.selectOrg.order + 1
          this.orgForm.orgFullCode = this.selectOrg.orgFullCode
          this.orgFullCode = this.selectOrg.orgFullCode
          this.orgForm.orgFullName = this.selectOrg.orgFullName
          this.orgFullName = this.selectOrg.orgFullName
          this.isEdit = true
        })
      }
    },
    updateOrg() {
      this.loading3 = true
      api_organization_update(this.selectOrg).then(() => {
        this.$message({
          message: edit_message,
          type: 'success'
        })
        this.loading2 = false
        this.isUpdate = false
        this.$store.dispatch('app/setOrgList', this.$store.getters.mgrOrgCode)
        this.orgCash = Object.assign(this.orgCash, this.selectOrg)
      }).catch(() => {
        this.loading2 = false
      })
    },
    getOrgInfo(data) {
      this.selectOrg = Object.assign(this.selectOrg, data)
      this.orgCash = Object.assign(this.orgCash, data)
      this.isUpdate = false
      this.isTop = !(data.orgCode === '00000000' || data.orgCode === '-1' || data.orgCode === this.$store.getters.mgrOrgCode)
      this.colorShow = data.orgCode !== '00000000' && data.orgCode !== '-1'
    },
    handleAddOrganization() {
      this.$refs.orgForm.validate(async valid => {
        if (valid) {
          this.loading = true
          await api_organization_register(this.orgForm).then(() => {
            this.loading = false
            this.$message({
              message: register_message,
              type: 'success'
            })
            this.handleClose()
            this.$store.dispatch('app/setOrgList', this.$store.getters.mgrOrgCode)
            this.orgName = ''
            this.orgCode = ''
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    handleDeleteOrganization() {
      this.$confirm('해당 부서에 관리자나 사용자가 있을 경우 미확인 부서로 이동이 됩니다', '삭제 하시겠습니까?', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'warning'
      })
        .then(async() => {
          this.loading1 = true
          await api_organization_delete(this.selectOrg.orgCode).then(() => {
            this.$message({
              type: 'success',
              message: delete_message
            })
            this.$store.dispatch('app/setOrgList', this.$store.getters.mgrOrgCode)
            this.loading1 = false
          }).catch(() => {
            this.loading1 = false
          })
        }, function() {
        })
    },
    handleClose() {
      this.orgForm = Object.assign({}, defaultForm)
      this.isEdit = false
    },
    handleNodeClick(data) {
      this.selectOrg.orgFullCode = data.orgFullCode + ',' + this.selectOrg.orgCode
      this.selectOrg.orgFullName = data.orgFullName + '>' + this.selectOrg.orgName
      this.orgFullName = data.orgFullName
      this.selectOrg.order = data.order + 1
      if (data.orgCode !== '00000000') {
        this.selectOrg.color = data.color
      }
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
