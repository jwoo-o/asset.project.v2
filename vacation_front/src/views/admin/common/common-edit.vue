<template>
  <div class="app-container">
    <el-row :gutter="15" type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card shadow="never" class="grid-wrapper">
          <div slot="header" class="grid-header">
            <span>공통그룹코드</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :lg="24">
              <el-form ref="groupCodeForm" :model="groupCodeForm" :rules="groupCodeRule">
                <el-form-item label-width="100px" label="그룹코드" size="small" prop="groupCode">
                  <el-input v-model="groupCodeForm.groupCode" :readonly="isEdit" />
                </el-form-item>
                <el-form-item label-width="100px" label="그룹코드명" size="small" prop="groupName">
                  <el-input v-model="groupCodeForm.groupName" />
                </el-form-item>
                <el-form-item label-width="100px" label="설명" size="small">
                  <el-input v-model="groupCodeForm.groupDesc" />
                </el-form-item>
                <el-form-item label-width="100px" label="사용여부" size="small">
                  <el-select v-model="groupCodeForm.useYn">
                    <el-option :value="true" label="사용">사용</el-option>
                    <el-option :value="false" label="사용안함">사용안함</el-option>
                  </el-select>
                </el-form-item>
              </el-form>
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
                  <router-link :to="{name: 'common'}">
                    <el-button size="small" icon="el-icon-folder">목록</el-button>
                  </router-link>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card shadow="never" class="grid-wrapper">
          <div slot="header" class="grid-header">
            <span>세부코드</span>
          </div>
          <div style="margin-bottom: 20px; text-align: right">
            <el-button
              ref="handleAddDetail"
              icon="el-icon-plus"
              size="small"
              :disabled="!isEdit"
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

          <el-table
            ref="detailTable"
            v-loading="loading"
            :header-cell-style="{background:'#F5F7FA'}"
            height="350"
            style="width: 100%;"
            border
            :data="detailCodes"
            row-key="id"
            fit
            highlight-current-row
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
              label="세부코드*"
              align="center"
              min-width="50"
            >
              <template slot-scope="{row}">
                <el-input v-model="row.detailCode" />
              </template>
            </el-table-column>
            <el-table-column
              label="세부코드명*"
              align="center"
              min-width="100"
            >
              <template slot-scope="{row}">
                <el-input v-model="row.detailName" />
              </template>
            </el-table-column>
            <el-table-column
              label="설명*"
              align="center"
              min-width="100"
            >
              <template slot-scope="{row}">
                <el-input v-model="row.detailDesc" />
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
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  api_common_code_detail,
  api_common_code_register,
  api_common_code_edit,
  api_common_code_check
} from '@/api/manager/common'
import { alreadyCode_message, edit_message, necessary_message, register_message } from '../message/constant'

const detail = {
  detailCode: '',
  detailName: '',
  detailDesc: '',
  groupCode: ''
}
export default {
  name: 'AdminLineCreate2',
  data() {
    const validate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    const validateGroupCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        if (!this.isGroupCodeValid) {
          api_common_code_check(value).then(response => {
            const isAlready = response.data
            if (isAlready) {
              callback(new Error(alreadyCode_message))
            } else {
              this.isGroupCodeValid = true
              callback()
            }
          })
        } else {
          callback()
        }
      }
    }

    return {
      groupCodeForm: {
        groupCode: '',
        groupName: '',
        groupDesc: '',
        useYn: true,
        detailCodes: []
      },
      groupCodeRule: {
        groupCode: [{ required: true, trigger: 'blur', validator: validateGroupCode }],
        groupName: [{ required: true, trigger: 'blur', validator: validate }]
      },
      detailCodes: [],
      selectRow: '',
      total: null,
      loading: false,
      loading1: false,
      isEdit: false,
      isAdd: false,
      detailCodeForm: Object.assign({}, detail),
      isGroupCodeValid: false
    }
  },
  /** 세부코드 추가후 데이터가 비여 있을 경우 추가버튼 비활성화*/
  watch: {
    detailCodes: {
      deep: true,
      handler(detail) {
        if (detail.length > 0) {
          /* for (let i = 0; i < detail.length; i++) {
                for (let j = i + 1; j < detail.length; j++) {
                  if (detail[i].detailCode === detail[j].detailCode) {
                    this.$message({
                      type: 'warning',
                      message: aleadyCode_message
                    })
                    setTimeout(() => { detail[j].detailCode = '' }, 500)
                  }
                }
              }*/

          const v = detail[detail.length - 1]
          const { detailCode, detailDesc, detailName } = v
          if (detailCode !== '' && detailName !== '' && detailDesc !== '') {
            this.isAdd = false
          }
        }
      }
    }
  },
  created() {
    if (this.$route.query.groupCode) {
      this.isEdit = true
      this.getCommonDetailList(this.$route.query.groupCode)
    }
  },
  methods: {
    async getCommonDetailList(groupCode) {
      this.loading = true
      this.isGroupCodeValid = true
      await api_common_code_detail(groupCode).then(response => {
        this.loading = false
        this.groupCodeForm = response.data
        this.detailCodes = this.groupCodeForm.detailCodes
      }).catch(() => {
        this.loading = false
      })
    },
    handleAddDetail() {
      if (!this.isAdd) {
        this.detailCodeForm = Object.assign({}, detail)
        this.detailCodes.push(this.detailCodeForm)
        this.isAdd = true
      }
    },
    handleDeleteDetail(index, rows) {
      rows.splice(index, 1)
      this.selectRow = ''
    },
    handleCommonConfirm() {
      this.detailCodes.forEach((v, i, a) => {
        const { detailCode, detailDesc, detailName } = v
        if (detailCode === '' || detailName === '' || detailDesc === '') {
          a.splice(i, 1)
        }
      })
      this.$refs.groupCodeForm.validate(valid => {
        if (valid) {
          this.loading1 = true
          if (this.isEdit) {
            if (this.handleDetailCodeOverlap()) {
              this.groupCodeForm.detailCodes = this.detailCodes
              api_common_code_edit(this.groupCodeForm).then(() => {
                this.loading1 = false
                this.$message({
                  type: 'success',
                  message: edit_message
                })
                this.$store.dispatch('app/setCommonCode', 'all')
              }).catch(() => {
                this.loading1 = false
              })
            } else {
              this.loading1 = false
            }
          } else {
            api_common_code_register(this.groupCodeForm).then(() => {
              this.loading1 = false
              this.$message({
                type: 'success',
                message: register_message
              })
              this.isEdit = true
              this.$router.replace({
                path: '/manager/common-code/edit',
                query: { groupCode: this.groupCodeForm.groupCode }
              })
            }).catch(() => {
              this.loading1 = false
            })
          }
        }
      })
    },
    handleDetailCodeOverlap() {
      const detail = this.detailCodes
      for (let i = 0; i < detail.length; i++) {
        for (let j = i + 1; j < detail.length; j++) {
          if (detail[i].detailCode === detail[j].detailCode) {
            this.$message({
              type: 'warning',
              message: alreadyCode_message
            })
            return false
          }
        }
      }
      return true
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
    handleDoLayOut() {
      this.detailCodeForm = Object.assign({}, detail)
      this.detailCodes.push(this.detailCodeForm)
      this.detailCodes.splice(this.detailCodes.length - 1, 1)
    }
  }
}
</script>

<style scoped>
  .common-header span {
    display: block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    font-size: 15px;
    color: #595959;
    font-weight: bold;
  }
</style>
<style>
  .sortable-ghost {
    opacity: .8;
    color: #fff !important;
    background: #42b983 !important;
  }
</style>
