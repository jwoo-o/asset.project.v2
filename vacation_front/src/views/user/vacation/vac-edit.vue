<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-form ref="vacationForm" :model="vacationForm" :rules="vacationRules">
          <el-card
            v-loading="loading"
            shadow="never"
            class="grid-wrapper"
            :body-style="{padding:'10px'}"
            style="margin-bottom: 10px;"
          >
            <div
              class="grid-header"
              style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
            >
              <el-row>
                <el-col>
                  <span style="margin: 10px"> 휴가상신</span>
                </el-col>
              </el-row>
            </div>
            <el-col :xs="24" :sm="24" :lg="24">
              <el-card shadow="never" class="grid-wrapper" style="padding-bottom: 15px">
                <el-col :xs="12" :sm="6" :lg="6" style="font-size: 14px">소속: {{ this.$store.getters.orgName }}
                </el-col>
                <el-col :xs="12" :sm="6" :lg="6" style="font-size: 14px">성명: {{ this.$store.getters.userName }}
                </el-col>
                <el-col :xs="12" :sm="6" :lg="6" style="font-size: 14px">직위: {{ this.$store.getters.rankNm }}
                </el-col>
                <el-col :xs="12" :sm="6" :lg="6" style="font-size: 14px">작성일자: {{ defaultDay }}
                </el-col>
              </el-card>
              <el-card shadow="never" class="grid-wrapper">
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="휴가 구분" label-width="100px" size="small" prop="vacationKind">
                    <el-row>
                      <el-col :xs="24" :sm="24" :lg="24">
                        <el-radio-group v-model="vacationForm.vacationKind">
                          <el-radio size="small" label="MORNING" @change="handelVacationUseCnt">오전반차</el-radio>
                          <el-radio size="small" label="AFTERNOON" @change="handelVacationUseCnt">오후반차</el-radio>
                          <el-radio size="small" label="YEAR" @change="handelVacationUseCnt">연차</el-radio>
                          <el-radio size="small" label="GYEONGJOSA" @change="handelVacationUseCnt">경조사</el-radio>
                          <el-select
                            v-model="vacationForm.vacationType"
                            :disabled="vacationForm.vacationKind !== 'GYEONGJOSA' "
                            size="small"
                            style="margin-right: 20px;width: 200px; margin-bottom: 10px"
                            @change="handelVacationType"
                          >
                            <el-option
                              v-for="item in this.$store.getters.commonCode.Gyeongjosa"
                              :key="item.detailCode"
                              :label="item.detailDesc"
                              :value="item.detailCode"
                            />
                          </el-select>
                          <el-radio label="ETC" @change="handelVacationUseCnt">기타</el-radio>
                          <el-input
                            v-model="vacationForm.etc"
                            :disabled="vacationForm.vacationKind !== 'ETC' "
                            size="small"
                            style="width: 200px"
                          />
                        </el-radio-group>
                      </el-col>
                    </el-row>
                  </el-form-item>
                </el-col>
              </el-card>
              <el-card shadow="never" class="grid-wrapper">
                <el-col :xs="24" :sm="24" :lg="24">
                  <table class="tg">
                    <thead>
                      <tr>
                        <th class="tg-qxe1" rowspan="2">일시</th>
                        <th class="tg-43cr" colspan="2">시작일 / 종료일</th>
                        <th class="tg-43cr">신청일수</th>
                      </tr>
                      <tr>
                        <td class="tg-i3ry" colspan="2" style="min-width: 250px">
                          <el-row :gutter="20">
                            <el-col :xs="12" :sm="12" :lg="12">
                              <el-form-item size="small" prop="startDay">
                                <el-date-picker
                                  v-model="vacationForm.startDay"
                                  type="date"
                                  :picker-options="pickerOptions"
                                  :clearable="false"
                                  :editable="false"
                                  :disabled="vacationForm.vacationKind === ''"
                                  size="small"
                                  format="yyyy-MM-dd"
                                  placeholder="시작일"
                                  style="width: 100%"
                                  @change="handleChangeStartDay"
                                />
                              </el-form-item>
                            </el-col>
                            <el-col :xs="12" :sm="12" :lg="12">
                              <el-form-item size="small" prop="endDay">
                                <el-date-picker
                                  v-model="vacationForm.endDay"
                                  type="date"
                                  :picker-options="pickerOptions1"
                                  :clearable="false"
                                  :editable="false"
                                  :disabled="vacationForm.startDay === ''"
                                  size="small"
                                  format="yyyy-MM-dd"
                                  placeholder="종료일"
                                  style="width: 100%"
                                  @change="handleChangeEndDay"
                                />
                              </el-form-item>
                            </el-col>
                          </el-row>
                        </td>
                        <td class="tg-i3ry" style="padding: 15px">
                          <el-form-item style="height: 30px" prop="countDay">
                            <el-input
                              v-model="vacationForm.countDay"
                              class="inputTag"
                              type="number"
                              placeholder=""
                            />
                          </el-form-item>
                        </td>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td class="tg-qxe1">사유</td>
                        <td class="tg-i3ry" colspan="3" style="height: 150px; overflow: auto">
                          <el-form-item style="height: 30px" prop="vacationReason">
                            <el-input
                              v-model="vacationReason"
                              type="textarea"
                              :rows="5"
                              maxlength="100"
                              show-word-limit
                              resize="none"
                              placeholder="사유를 입력해 주세요"
                            />
                          </el-form-item>
                        </td>
                      </tr>
                      <tr>
                        <td class="tg-qxe1">인수인계</td>
                        <td class="tg-i3ry" colspan="3">
                          <el-form-item style="height: 30px" prop="takeOver">
                            <el-input
                              v-model="vacationForm.takeOver"
                              class="inputTag"
                              placeholder="인수인계자를 입력해 주세요"
                            />
                          </el-form-item>
                        </td>
                      </tr>
                      <tr>
                        <td class="tg-qxe1">비상연락망</td>
                        <td class="tg-i3ry" colspan="3">
                          <el-form-item style="height: 30px" prop="vacationTel">
                            <el-input
                              v-model="vacationForm.vacationTel"
                              class="inputTag"
                              placeholder="비상연락망을 입력해 주세요"
                            />
                          </el-form-item>
                        </td>
                      </tr>
                      <tr>
                        <td class="tg-qxe1">첨부파일</td>
                        <td class="tg-i3ry" colspan="4">
                          <el-upload
                            ref="upload"
                            :file-list="vacationForm.fileList"
                            :auto-upload="false"
                            :on-change="handleChange"
                            :before-remove="beforeRemove"
                            :on-remove="handleRemove"
                            :on-preview="handlePreview"
                            action=""
                          />
                        </td>
                      </tr>
                    </tbody>
                  </table>
                  <el-upload
                    ref="upload"
                    :file-list="fileList"
                    :auto-upload="false"
                    :on-change="handleChange"
                    :before-remove="beforeRemove"
                    :on-remove="handleRemove"
                    action=""
                    style="margin-top: 20px; text-align: right"
                  >
                    <el-button slot="trigger" size="small">파일 업로드</el-button>
                    <div slot="tip" class="el-upload__tip"> {{ fileMessage }}</div>
                  </el-upload>
                  <el-button :disabled="vacationForm.vacationKind === ''" style="margin: 10px; float:right;" :loading="loading1" @click="updateVacation">수정
                  </el-button>
                </el-col>
              </el-card>
            </el-col>
          </el-card>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { api_vacation_file_upload, api_vacation_file_delete, api_vacation_date_check, api_vacation_detail, api_vacation_update } from '@/api/vacation/vacation'
import { necessary_message, file_necessary_message, fail_message } from '../message/constant'
import { maxFileUploadMessage, maxFileUploadSize } from '@/default/data'

export default {
  name: 'VacApproval',
  data() {
    const validate = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    const validateKind = (rule, value, callback) => {
      if (value === 'ETC' && this.vacationForm.etc === '') {
        callback(new Error(necessary_message))
      } else if (value === 'ETC' && this.uploadList.length === 0) {
        callback(new Error(file_necessary_message))
      } else {
        callback()
      }
    }
    return {
      pickerOptions: {
        disabledDate: this.disabledDate
      },
      pickerOptions1: {
        disabledDate: this.disabledDate1
      },
      loading: false,
      loading1: false,
      radio: '0',
      countDay: '',
      defaultDay: '',
      vacationReason: '',
      vacationForm: {
        approveState: 'WAIT',
        startDay: '',
        endDay: '',
        countDay: '',
        destination: '',
        userId: '',
        userName: '',
        orgCode: '',
        vacationKind: '',
        vacationReason: '',
        vacationTel: '',
        takeOver: '',
        vacationType: '',
        etc: '',
        fileIds: []
      },
      vacationRules: {
        vacationKind: [{ required: true, trigger: 'blur', validator: validateKind }],
        startDay: [{ required: true, trigger: 'change', validator: validate }],
        endDay: [{ required: true, trigger: 'change', validator: validate }],
        vacationReason: [{ required: true, trigger: 'blur', validator: validate }],
        takeOver: [{ required: true, trigger: 'blur', validator: validate }],
        vacationTel: [{ required: true, trigger: 'blur', validator: validate }],
        countDay: [{ required: true, trigger: 'change', validator: validate }]
      },
      fileList: [],
      uploadList: [],
      forms: new FormData(),
      fileSize: null,
      fileMessage: null
    }
  },
  watch: {
    vacationReason: {
      handler(val, old) {
        const numberOfLines = (val.match(/\n/g) || []).length + 1
        const maxRows = 5
        if (numberOfLines === maxRows) {
          this.vacationReason = old
        }
        this.vacationForm.vacationReason = this.vacationReason
      }
    }
  },
  created() {
    this.getDate()
    this.getVacationDetail()
    this.getMaxFileList()
  },
  methods: {
    disabledDate(date) {
      const d = this.$moment(date).format('YYYY-MM-DD')
      const cur = this.$moment(Date.now()).format('YYYY-MM-DD')
      return d < cur || date.getDay() < 1 || date.getDay() > 5
    },
    disabledDate1(date) {
      const d = this.$moment(date).format('YYYY-MM-DD')
      const std = this.$moment(this.vacationForm.startDay).format('YYYY-MM-DD')
      const end = this.$moment(this.vacationForm.endDay).format('YYYY-MM-DD')
      if (this.vacationForm.vacationKind === 'MORNING' || this.vacationForm.vacationKind === 'AFTERNOON') {
        return d !== std
      } else if (this.vacationForm.vacationKind === 'GYEONGJOSA') {
        return (date.getDay() < 1 || date.getDay() > 5) || end > d
      } else {
        return (date.getDay() < 1 || date.getDay() > 5) || std > d
      }
    },
    async getVacationDetail() {
      this.loading = true
      this.vacationId = this.$route.query.vacationId
      api_vacation_detail(this.vacationId).then(response => {
        const { vacationDetail } = response.data
        this.vacationForm = vacationDetail
        if (this.vacationForm.vacationKind === '오전반차') {
          this.vacationForm.vacationKind = 'MORNING'
        } else if (this.vacationForm.vacationKind === '오후반차') {
          this.vacationForm.vacationKind = 'AFTERNOON'
        } else if (this.vacationForm.vacationKind === '연차') {
          this.vacationForm.vacationKind = 'YEAR'
        } else if (this.vacationForm.vacationKind === '경조사') {
          this.vacationForm.vacationKind = 'GYEONGJOSA'
        } else if (this.vacationForm.vacationKind === '기타') {
          this.vacationForm.vacationKind = 'ETC'
        }
        this.vacationReason = vacationDetail.vacationReason
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    async updateVacation() {
      const confirm = confirm('수정하시겠습니까?')
      if (confirm) {
        this.$refs.vacationForm.validate(async valid => {
          if (valid) {
            this.loading1 = true
            this.vacationForm.orgCode = this.$store.getters.orgCode
            this.vacationForm.userId = this.$store.getters.userId
            this.vacationForm.userName = this.$store.getters.userName
            let vacationDateCheck = false
            this.dateFormatChange()
            await api_vacation_date_check(this.vacationForm.userId, this.vacationForm.startDay, this.vacationForm.endDay, this.vacationId)
              .then(response => {
                vacationDateCheck = response.data
              }).catch(() => {})
            if (vacationDateCheck) {
              this.$message({
                type: 'error',
                message: '이미 상신된 날짜입니다.'
              })
              this.loading1 = false
            } else {
              if (this.uploadList.length > 0) {
                await this.handleFileUpload()
              }
              await api_vacation_update(this.vacationForm).then(response => {
                this.$message({
                  type: 'success',
                  message: '수정을 완료하였습니다'
                })
              }).catch(() => {
                this.loading1 = false
              })
              this.$router.push({ name: 'user-home' })
            }
          }
        })
      }
      /* this.$confirm('', '수정하시겠습니까?', {
        confirmButtonText: '확인',
        cancelButtonText: '취소',
        type: 'primary'
      }).then(() => {
        this.$refs.vacationForm.validate(async valid => {
          if (valid) {
            this.loading1 = true
            this.vacationForm.orgCode = this.$store.getters.orgCode
            this.vacationForm.userId = this.$store.getters.userId
            this.vacationForm.userName = this.$store.getters.userName
            let vacationDateCheck = false
            this.dateFormatChange()
            await api_vacation_date_check(this.vacationForm.userId, this.vacationForm.startDay, this.vacationForm.endDay, this.vacationId)
              .then(response => {
                vacationDateCheck = response.data
              }).catch(() => {})
            if (vacationDateCheck) {
              this.$message({
                type: 'error',
                message: '이미 상신된 날짜입니다.'
              })
              this.loading1 = false
            } else {
              if (this.uploadList.length > 0) {
                await this.handleFileUpload()
              }
              await api_vacation_update(this.vacationForm).then(response => {
                this.$message({
                  type: 'success',
                  message: '수정을 완료하였습니다'
                })
              }).catch(() => {
                this.loading1 = false
              })
              this.$router.push({ name: 'user-home' })
            }
          }
        })
      }, function() {})*/
    },
    getDate() {
      const day = new Date()
      this.defaultDay = this.$moment(day).format('YYYY-MM-DD')
    },
    handleChangeStartDay(value) {
      if (this.vacationForm.vacationKind === 'GYEONGJOSA') {
        let countDay = 0
        const date = new Date(value.valueOf())
        for (let i = 0; i < this.vacationForm.countDay; i++) {
          if (i === 0) {
            date.setDate(date.getDate() + 0)
          } else {
            date.setDate(date.getDate() + 1)
          }
          if (date.getDay() < 1 || date.getDay() > 5) {
            countDay++
          }
        }
        date.setDate(date.getDate() + countDay)
        if (date.getDay() === 0) {
          date.setDate(date.getDate() + 1)
        }
        this.vacationForm.endDay = date
      } else if (this.vacationForm.vacationKind === 'MORNING' || this.vacationForm.vacationKind === 'AFTERNOON') {
        this.vacationForm.endDay = this.vacationForm.startDay
      }
    },
    handleChangeEndDay(value) {
      if (this.vacationForm.vacationKind === 'YEAR' || this.vacationForm.vacationKind === 'ETC') {
        const std = new Date(this.vacationForm.startDay)
        const end = new Date(value)
        let countDay = 1
        let week = 0
        while (std.getDate() !== end.getDate()) {
          countDay++
          std.setDate(std.getDate() + 1)
          if (std.getDay() < 1 || std.getDay() > 5) {
            week++
          }
        }
        this.vacationForm.countDay = countDay - week
      }
    },
    dateFormatChange() {
      this.vacationForm.startDay = this.$moment(this.vacationForm.startDay).format('YYYY-MM-DD')
      this.vacationForm.endDay = this.$moment(this.vacationForm.endDay).format('YYYY-MM-DD')
    },
    handelVacationUseCnt(data) {
      this.vacationForm.vacationKind = data
      if (data === 'MORNING' || data === 'AFTERNOON') {
        this.vacationForm.countDay = 0.5
      } else {
        this.vacationForm.countDay = ''
      }
      if (data === 'ETC') {
        this.vacationForm.vacationType = this.vacationForm.etc
      }
    },
    handelVacationType(data) {
      for (let i = 0; i < this.$store.getters.commonCode.Gyeongjosa.length; i++) {
        if (data === this.$store.getters.commonCode.Gyeongjosa[i].detailCode) {
          this.vacationForm.countDay = this.$store.getters.commonCode.Gyeongjosa[i].detailName
          /* this.vacationForm.vacationType = this.$store.getters.commonCode.Gyeongjosa[i].detailDesc*/
        }
      }
    },
    handleChange(file, fileList) {
      file.upload = false
      this.uploadList.push(file.raw)
    },
    beforeRemove(file) {
      if (file.upload === undefined) {
        return confirm(`삭제하시겠습니까? ${file.name} )`)
        /* return this.$confirm(`삭제하시겠습니까? (${file.name} )`)*/
      }
    },
    async handleRemove(file) {
      if (file.upload === undefined) {
        await api_vacation_file_delete(file.id)
      } else {
        for (let i = 0; i < this.uploadList.length; i++) {
          if (this.uploadList[i].name === file.name) {
            this.uploadList.splice(i, 1)
          }
        }
      }
    },
    handlePreview(file) {
      const link = document.createElement('a')
      link.href = file.url
      link.download = file.name
      document.body.appendChild(link)
      link.click()
    },
    async handleFileUpload() {
      for (let i = 0; i < this.uploadList.length; i++) {
        this.forms.append('files', this.uploadList[i])
      }
      await api_vacation_file_upload(this.forms).then(response => {
        this.vacationForm.fileIds = response.data
      }).catch(() => {
        this.forms.delete('files')
        this.loading = false
        this.$message({
          type: 'error',
          message: fail_message
        })
      })
    },
    getMaxFileList() {
      // await this.$store.dispatch('app/setCommonCode', 'rank')
      this.fileSize = Number(this.$store.getters.commonCode.uploadSize[0].detailName) || maxFileUploadSize
      this.fileMessage = this.$store.getters.commonCode.uploadSize[0].detailDesc || maxFileUploadMessage
    }
  }
}
</script>

<style lang="scss" scoped>
  .tg {
    border-collapse: collapse;
    border-spacing: 0;
    width: 100%;
  }

  .tg td {
    border-color: black;
    border-style: solid;
    border-width: 1px;
    font-family: Arial, sans-serif;
    font-size: 14px;
    overflow: hidden;
    padding: 10px 20px;
    word-break: normal;
  }

  .tg th {
    border-color: black;
    border-style: solid;
    border-width: 1px;
    font-family: Arial, sans-serif;
    font-size: 14px;
    font-weight: normal;
    overflow: hidden;
    padding: 10px 20px;
    word-break: normal;
  }

  .tg .tg-qxe1 {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    width: 150px
  }

  .tg .tg-43cr {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    vertical-align: top;
    width: 400px
  }

  .tg .tg-i3ry {
    background-color: #ffffff;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: left;
    vertical-align: top;
    width: 400px
  }

  .inputTag {
    width: 100%;
    border: none;
    outline: none;
  }

  input::placeholder {
    color: #d1d1d1;
  }
</style>
