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
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">소속: {{ this.$store.getters.orgName }}
                </el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">성명: {{ this.$store.getters.userName }}
                </el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">직위: {{ this.$store.getters.rankNm }}
                </el-col>
                <el-col :xs="24" :sm="6" :lg="6" style="font-size: 14px;margin-bottom: 5px">작성일자: {{ defaultDay }}
                </el-col>
              </el-card>
              <el-card shadow="never" class="grid-wrapper">
                <el-form-item :label="mobile ? '' : '휴가 구분'" :label-width="mobile ? '0px' : '100px'" size="small" prop="vacationKind">
                  <el-radio-group v-model="vacationForm.vacationKind">
                    <el-radio label="MORNING" :style="{width: mobile ? '750px' : '', marginBottom: mobile ? '10px' : ''}" @change="handelVacationUseCnt">오전반차</el-radio>
                    <el-radio label="AFTERNOON" :style="{width: mobile ? '750px' : '', marginBottom: mobile ? '10px' : ''}" @change="handelVacationUseCnt">오후반차</el-radio>
                    <el-radio label="YEAR" :style="{width: mobile ? '750px' : '', marginBottom: mobile ? '10px' : ''}" @change="handelVacationUseCnt">연차</el-radio>
                    <el-radio label="GYEONGJOSA" :style="{width: mobile ? '750px' : '', marginBottom: mobile ? '10px' : ''}" @change="handelVacationUseCnt">경조사</el-radio>
                    <el-select
                      v-model="vacationForm.vacationType"
                      :disabled="vacationForm.vacationKind !== 'GYEONGJOSA' "
                      size="small"
                      style="margin-right: 20px; margin-bottom: 10px;width: 200px"
                      @change="handelVacationType"
                    >
                      <el-option
                        v-for="item in this.$store.getters.commonCode.Gyeongjosa"
                        :key="item.detailCode"
                        :label="item.detailDesc"
                        :value="item.detailCode"
                        :style="{width : mobile ? '300px' : '' }"
                      />
                    </el-select>
                    <el-radio label="ETC" :style="{width: mobile ? '750px' : '', marginBottom: mobile ? '10px' : ''}" @change="handelVacationUseCnt">기타</el-radio>
                    <el-input
                      v-model="vacationForm.etc"
                      :disabled="vacationForm.vacationKind !== 'ETC' "
                      size="small"
                      style="width: 200px"
                    />
                  </el-radio-group>
                </el-form-item>
              </el-card>
              <el-card shadow="never" class="grid-wrapper scroll" style="overflow: auto" :body-style="{padding:'5px'}">
                <table class="tg">
                  <thead>
                    <tr>
                      <th class="tg-qxe1" rowspan="2">일시</th>
                      <th class="tg-43cr" colspan="2">시작일 / 종료일</th>
                      <th class="tg-43cr">신청일수</th>
                    </tr>
                    <tr>
                      <td class="tg-i3ry" colspan="2" style="min-width: 270px">
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
                                :default-value="vacationForm.startDay"
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
                      <td class="tg-i3ry" style="padding: 15px;width: 100px; min-width: 100px">
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
                      <td class="scroll tg-i3ry" colspan="3" style="height: 150px; overflow: auto">
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
                  </tbody>
                </table>
              </el-card>
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
              <el-button :disabled="vacationForm.vacationKind === ''" style="margin: 10px; float:right;" :loading="loading1" @click="insertVacation">상신
              </el-button>
            </el-col>
          </el-card>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { api_vacation_insert, api_vacation_file_upload, api_vacation_file_delete, api_vacation_date_check } from '@/api/vacation/vacation'
import {
  necessary_message,
  file_necessary_message,
  fail_message,
  approval_vacationReason_message, approval_vacationOver_message
} from '../message/constant'
import { mapGetters } from 'vuex'
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
  computed: {
    ...mapGetters([
      'holidayList',
      'width',
      'height',
      'mobile'
    ])
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
  },
  methods: {
    disabledDate(date) {
      const d = this.$moment(date).format('YYYY-MM-DD')
      // const cur = this.$moment(Date.now()).format('YYYY-MM-DD')
      return/* d < cur ||*/ date.getDay() < 1 || date.getDay() > 5 || this.holidayList.includes(d)
    },
    disabledDate1(date) {
      const d = this.$moment(date).format('YYYY-MM-DD')
      const std = this.$moment(this.vacationForm.startDay).format('YYYY-MM-DD')
      const end = this.$moment(this.vacationForm.endDay).format('YYYY-MM-DD')
      if (this.vacationForm.vacationKind === 'MORNING' || this.vacationForm.vacationKind === 'AFTERNOON') {
        return d !== std
      } else if (this.vacationForm.vacationKind === 'GYEONGJOSA') {
        return (date.getDay() < 1 || date.getDay() > 5) || end > d || this.holidayList.includes(d)
      } else {
        return date.getDay() < 1 || date.getDay() > 5 || std > d || this.holidayList.includes(d)
      }
    },
    async insertVacation() {
      const isConfirm = confirm('상신하시겠습니까?')
      if (isConfirm) {
        this.$refs.vacationForm.validate(async valid => {
          if (valid) {
            this.loading1 = true
            this.vacationForm.orgCode = this.$store.getters.orgCode
            this.vacationForm.userId = this.$store.getters.userId
            this.vacationForm.userName = this.$store.getters.userName
            let vacationDateCheck = false
            this.dateFormatChange()
            await api_vacation_date_check(this.vacationForm.userId, this.vacationForm.startDay, this.vacationForm.endDay)
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
              await api_vacation_insert(this.vacationForm).then(response => {
                this.$message({
                  type: 'success',
                  message: '상신을 완료하였습니다'
                })
              }).catch(() => {
                this.loading1 = false
              })
              this.$router.push({ name: 'user-home' })
            }
          }
        })
      }
      /* this.$confirm('', '상신하시겠습니까?', {
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
            await api_vacation_date_check(this.vacationForm.userId, this.vacationForm.startDay, this.vacationForm.endDay)
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
              await api_vacation_insert(this.vacationForm).then(response => {
                this.$message({
                  type: 'success',
                  message: '상신을 완료하였습니다'
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
        let countDay = this.vacationForm.countDay
        const date = new Date(value)
        while (countDay !== 0) {
          const d = this.$moment(date).format('YYYY-MM-DD')
          if (date.getDay() > 0 && date.getDay() < 6 && !this.holidayList.includes(d)) {
            countDay--
          }
          if (countDay !== 0) {
            date.setDate(date.getDate() + 1)
          }
        }
        this.vacationForm.endDay = date
      } else if (this.vacationForm.vacationKind === 'MORNING' || this.vacationForm.vacationKind === 'AFTERNOON') {
        this.vacationForm.endDay = this.vacationForm.startDay
      } else {
        this.vacationForm.endDay = ''
        this.vacationForm.countDay = ''
      }
    },
    handleChangeEndDay(value) {
      const std = new Date(this.vacationForm.startDay)
      const end = new Date(value)
      const cur = this.$moment(Date.now()).format('YYYY-MM-DD')
      this.vacationReason = approval_vacationReason_message
      this.vacationForm.vacationTel = this.$store.getters.tel
      if (this.$moment(std).format('YYYY-MM-DD') < cur) {
        this.vacationReason = approval_vacationOver_message
      }
      if (this.vacationForm.vacationKind === 'YEAR' || this.vacationForm.vacationKind === 'ETC') {
        let countDay = 1
        let week = 0
        while (std.getTime() !== end.getTime()) {
          const d = this.$moment(std).format('YYYY-MM-DD')
          if (std.getDay() === 0 || std.getDay() === 6 || this.holidayList.includes(d)) {
            week++
          }
          countDay++
          std.setDate(std.getDate() + 1)
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
      this.vacationForm.vacationType = ''
      this.vacationForm.startDay = ''
      this.vacationForm.endDay = ''
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
      this.vacationForm.startDay = ''
      this.vacationForm.endDay = ''
    },
    handleChange(file, fileList) {
      file.upload = false
      this.uploadList.push(file.raw)
    },
    beforeRemove(file) {
      if (file.upload === undefined) {
        return confirm(`삭제하시겠습니까? ${file.name} )`)
      /*  return this.$confirm(`삭제하시겠습니까? (${file.name} )`)*/
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
    min-width: 120px
  }

  .tg .tg-43cr {
    background-color: #F5F7FA;
    border-color: #d1d1d1;
    font-size: 14px;
    text-align: center;
    vertical-align: top;
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
