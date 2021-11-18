<template>
  <div v-loading="loading" class="app-container scroll" style="overflow: auto">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="14" :lg="12">
        <el-card
          shadow="never"
          class="grid-wrapper"
          :body-style="{padding:'10px'}"
          :style="{marginBottom: '10px', height: $route.name === 'admin-annual-deadline' ? '850px' : '850px'}"
        >
          <div
            class="grid-header"
            style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
          >
            <el-row>
              <el-col :xs="12" :sm="14" :lg="16">
                <span style="margin: 10px">미사용 연차유급휴가 사용시기 지정</span>
              </el-col>
            </el-row>
          </div>
          <CustomCalendar :year="year" :is-update="isUpdate" :deadlines="deadlines" :delete-deadline="deleteDeadline" @onClickDeadline="onClickDeadline" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="10" :lg="8">
        <div ref="deadlineInfo">
          <el-card
            shadow="never"
            class="grid-wrapper scroll"
            :body-style="{padding:'10px'}"
            :style="{
              marginBottom: '10px',
              height: $route.name === 'admin-annual-deadline' ? '850px' : '850px',
              overflow: 'auto'
            }"
          >
            <div
              class="grid-header"
              style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
            >
              <el-row>
                <el-col :xs="16" :sm="16" :lg="18">
                  <span style="margin: 10px">{{ userInfo.userName }} - 예정/미사용 : {{ checkedVacation }}/{{ isUpdate ? checkedVacation : unusedVacation }}</span>
                </el-col>
                <el-col v-if="checkedVacation === unusedVacation && !isUpdate" :xs="8" :sm="8" :lg="6" align="right">
                  <el-button :disabled="this.$route.query.writer === 'USER' ? sign !== userInfo.userName : false" :loading="loading1" size="small" icon="el-icon-check" type="info" plain @click="handleRegister" />
                </el-col>
              </el-row>
            </div>
            <el-row>
              <el-card
                v-if="this.$route.query.writer === 'USER' ? true : checkedVacation !== unusedVacation && !isUpdate"
                shadow="never"
                class="grid-wrapper"
                :body-style="{padding:'10px'}"
                style="margin-bottom: 0px;"
              >
                <div v-show="checkedVacation !== unusedVacation && !isUpdate">
                  <el-row>
                    <el-col :span="11">
                      <el-input
                        v-model="start"
                        readonly
                        size="small"
                      />
                    </el-col>
                    <el-col class="line" :span="2" align="center" style="padding-top: 6px">~</el-col>
                    <el-col :span="11">
                      <el-input
                        v-model="end"
                        readonly
                        size="small"
                      />
                    </el-col>
                  </el-row>

                </div>
                <div v-show="isUpdate ? this.$route.query.writer === 'USER' : checkedVacation === unusedVacation">
                  <p style="font-size: 10pt; font-weight: inherit">
                    상기인은 미사용 연차유급휴가 사용시기를 상기와 같이 지정하여 통보하며, 지정된 휴가일에 출근하여 근로를 제공한 경우, 해당 연도 내에 연차유급휴가를 사용하여 소멸시키겠으며 그렇지 못한 경우에는 미사용 연차유급휴가에 대한 연차휴가수당을 지급받지 않는 것에 동의합니다.
                  </p>
                  <div align="right">
                    <span style="font-size: 10pt; font-weight: inherit">동의자 : </span>
                    <el-input v-model="sign" size="small" style="width: 100px" placeholder="성명" :readonly="isUpdate" />
                  </div>
                </div>
              </el-card>
            </el-row>
            <div ref="deadlines">
              <el-card
                shadow="never"
                class="grid-wrapper"
                style="margin-top:20px;height: 370px"
                :body-style="{padding:'10px'}"
              >
                <el-tag
                  v-for="tag in deadlines"
                  :key="tag.name"
                  effect="dark"
                  type="info"
                  size="medium"
                  :closable="!isUpdate"
                  style="margin: 10px"
                  @close="handleClose(tag)"
                >
                  {{ tag.name }}
                </el-tag>
              </el-card>
            </div>
            <img src="@/assets/image004.png" style="width: 100%">
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import html2canvas from 'html2canvas'
import CustomCalendar from '@/components/Calendar/index'
import { api_user_deadline_list, api_user_deadline_register,api_user_deadline_confirm } from '@/api/deadline/deadline'
import { defaultSearchForm } from '@/default/data'
import { api_user_vacation_list } from '@/api/vacation/vacation'
import { mapGetters } from 'vuex'
import { exceeded_message, register_message } from './message/constant'
export default {
  name: 'DeadlineCheck',
  components: {
    CustomCalendar
  },
  data() {
    return {
      deadlines: [],
      deadline: {},
      deleteDeadline: {},
      userInfo: {},
      searchForm: Object.assign({}, defaultSearchForm),
      year: '',
      writer: '',
      start: '',
      end: '',
      isUpdate: false,
      sign: '',
      unusedVacation: '',
      checkedVacation: 0,
      loading: false,
      loading1: false

    }
  },
  computed: {
    ...mapGetters([
      'holidayList',
      'height'
    ])
  },
  watch: {
    start: {
      handler(val) {
        this.end = undefined
      }
    }
  },
  async created() {
    if (this.$route.query.year && this.$route.query.writer) {
      this.year = this.$route.query.year
      this.writer = this.$route.query.writer
      new Promise(this.getVacationList).then(this.getDeadlineList)
    } else {
      this.$router.push('/')
    }
  },
  methods: {
    async handleRegister() {
      const data = {
        year: this.$route.query.year,
        userId: this.$route.query.userId,
        writer: this.$route.query.writer,
        deadlines: this.deadlines
      }
      if (this.$route.query.writer === 'USER') {
        await html2canvas(this.$refs.deadlineInfo).then(function(canvas) {
          const img = canvas.toDataURL('image/png').replace('data:image/png;base64,', '')
          data.image = img
        })
      }
      this.loading1 = true
      const token = this.$route.query.checkKey
      await api_user_deadline_register(data, token).then(() => {
        this.isUpdate = true
        this.loading1 = false
        this.$message({
          type: 'success',
          message: register_message
        })
      }).catch(() => {
        this.loading1 = false
        this.isUpdate = false
      })
    },
    getVacationList(resolve, reject) {
      this.searchForm.year = this.$route.query.year
      const token = this.$route.query.checkKey
      api_user_vacation_list(this.$route.query.userId, this.searchForm, token).then(response => {
        const { userInfo } = response.data
        this.userInfo = userInfo
        this.sign = this.userInfo.userName
        this.unusedVacation = Number(userInfo.vacationTotalCnt) - Number(userInfo.vacationUseCnt)
        resolve()
      }).catch(() => {
        reject()
      })
    },
    getDeadlineList() {
      const data = {
        year: this.$route.query.year,
        userId: this.$route.query.userId,
        writer: this.$route.query.writer
      }
      this.loading = true
      const token = this.$route.query.checkKey
      api_user_deadline_list(data, token).then(response => {
        this.loading = false
        const { USER, ADMIN } = response.data
        if (this.writer === 'USER') {
          if (USER.deadlines) {
            this.isUpdate = true
            this.sign = this.userInfo.userName
            this.deadlines = USER.deadlines
            this.deadlines.map(value => { this.checkedVacation += value.countDay })
          } else {
            this.sign = ''
          }
        } else {
          if (ADMIN) {
            this.isUpdate = true
            this.deadlines = ADMIN.deadlines
            this.deadlines.map(value => { this.checkedVacation += value.countDay })
            if (token) {
              this.handleDeadlineConfirm()
            }
          } else {
            if (USER.deadlines) {
              this.deadlines = USER.deadlines
              this.deadlines.map(value => { this.checkedVacation += value.countDay })
            }
          }
        }
      }).catch(() => {
        this.loading = false
      })
    },
    handleDeadlineConfirm() {
      const data = {
        year: this.$route.query.year,
        userId: this.$route.query.userId,
        writer: this.$route.query.writer
      }
      const token = this.$route.query.checkKey
      api_user_deadline_confirm(data, token)
    },
    onClickDeadline(res) {
      const date = new Date(res.start)
      const formatDate = this.$moment(date).format('YYYY-MM-DD')
      if (this.end === undefined) {
        if (this.start > formatDate) {
          this.start = formatDate
        } else {
          this.end = formatDate

          const sdt = new Date(this.start)
          const edt = new Date(this.end)
          edt.setHours(23, 59, 59)
          sdt.setHours(0, 0, 0)
          let countDay = this.handleChangeEndDay(sdt, edt)
          if (this.unusedVacation - this.checkedVacation === 0.5 && countDay === 1) {
            countDay = 0.5
          }
          this.checkedVacation += countDay
          if (this.checkedVacation > this.unusedVacation) {
            this.checkedVacation -= countDay
            this.$message({
              type: 'warning',
              message: exceeded_message
            })
          } else {
            this.deadline = {
              calendarId: 'DEADLINE',
              category: countDay === 1 || countDay === 0.5 ? 'time' : 'allday',
              end: edt,
              start: sdt,
              title: this.userInfo.userName,
              userId: this.userInfo.userId,
              name: this.start + ' ~ ' + this.end,
              countDay: countDay
            }
            this.deadlines.map(value => {
              if (value.end < this.deadline.end && this.deadline.start < value.end) {
                this.checkedVacation -= value.countDay
                this.deadlines.splice(value, 1)
                this.deleteDeadline = value
              }
            })
            this.deadlines.push(this.deadline)
          }
        }
      } else {
        this.start = this.$moment(date).format('YYYY-MM-DD')
      }
    },
    handleChangeEndDay(s, e) {
      const std = new Date(s)
      const end = new Date(e)
      end.setHours(0, 0, 0)
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
      return countDay - week
    },
    handleClose(tag) {
      this.deadlines.splice(this.deadlines.indexOf(tag), 1)
      this.deleteDeadline = tag
      this.checkedVacation -= tag.countDay
    }
  }
}
</script>

<style scoped>

</style>
