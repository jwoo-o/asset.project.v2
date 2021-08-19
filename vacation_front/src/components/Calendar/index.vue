<template>
  <div>
    <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
      <el-row>
        <el-col :xs="24" :sm="24" :lg="14">
          <div>
            <el-button
              size="small"
              round
              @click="handleMove('today')"
            >Today</el-button>
            <el-button
              size="small"
              circle
              icon="el-icon-arrow-left"
              @click="handleMove('prev')"
            />
            <el-button
              size="small"
              circle
              icon="el-icon-arrow-right"
              style="margin-right: 10px"
              @click="handleMove('next')"
            />
            <el-date-picker
              v-model="dateRange"
              type="month"
              value-format="yyyy-MM-dd"
              :editable="false"
              :clearable="false"
              size="small"
              style="width: 140px"
              @change="setDate"
            />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="10">

          <div v-for="item in calendarList" :key="item.id" style="float: left;margin-right: 10px">
            <label style="float: left">
              <input v-model="viewList" type="checkbox" class="tui-full-calendar-checkbox-round" :value="item.id" @change="handleChange">
              <span :style="{borderColor: item.borderColor,backgroundColor: item.bgColor}" />
            </label>
            <span style="color: #8C8C8C;font-size: 12px;">{{ item.name }}</span>
          </div>
        </el-col>
      </el-row>

    </el-card>
    <el-card v-loading="loading" shadow="never" class="grid-wrapper">
      <calendar
        ref="tuiCal"
        style="height: 100%"
        :use-detail-popup="useDetailPopup"
        :view="selectedView"
        :calendars="calendarList"
        :schedules="scheduleList"
        :template="template"
        :theme="theme"
        :task-view="true"
        :schedule-view="true"
        :month="month"
        :week="week"
        :timezones="timezones"
        :disable-click="disableClick"
        :disable-dbl-click="disableDblClick"
        :use-creation-popup="false"
        :is-read-only="isReadOnly"
        :usage-statistics="false"
        @beforeCreateSchedule="onClickDeadline"
        @clickSchedule="onClickSchedule"
        @clickDayname="onClickDayname"
        @beforeDeleteSchedule="onBeforeDeleteSchedule"
        @afterRenderSchedule="onAfterRenderSchedule"
        @clickTimezonesCollapseBtn="onClickTimezonesCollapseBtn"
      />
    </el-card>

  </div>
</template>
<script>
import 'tui-time-picker/dist/tui-time-picker.css'
import 'tui-date-picker/dist/tui-date-picker.css'
import 'tui-calendar/dist/tui-calendar.css'
import { Calendar } from '@toast-ui/vue-calendar'
import { api_vacation_calendar_list } from '@/api/vacation/vacation'
import { mapGetters } from 'vuex'

export default {
  name: 'App',
  components: {
    'calendar': Calendar
  },
  props: {
    deleteDeadline: {
      type: Object,
      default() {
        return {}
      }
    },
    deadlines: {
      type: Array,
      default() {
        return []
      }
    },
    isUpdate: {
      type: Boolean,
      default: false
    },
    year: {
      type: String,
      default: new Date().getFullYear().toString()
    }
  },
  data() {
    return {
      dateRange: '',
      selectedView: 'month',
      html: '',
      viewList: ['MORNING', 'AFTERNOON', 'YEAR', 'GYEONGJOSA', 'ETC'],
      theme: {
        // month schedule style
        'month.schedule.borderRadius': '5px',
        'month.schedule.height': '18px',
        'month.schedule.marginTop': '2px',
        'month.schedule.marginLeft': '10px',
        'month.schedule.marginRight': '10px',

        // month day grid cell 'day'
        'month.holidayExceptThisMonth.color': '#f3acac',
        'month.dayExceptThisMonth.color': '#bbb',
        'month.weekend.backgroundColor': '#fafafa',
        'month.day.fontSize': '16px',
        'common.saturday.color': '#3162ea'
      },
      calendarList: [
        {
          id: 'MORNING',
          name: '오전반차',
          bgColor: '#9e5fff',
          borderColor: '#9e5fff'
        },
        {
          id: 'AFTERNOON',
          name: '오후반차',
          bgColor: '#00a9ff',
          borderColor: '#00a9ff'
        },
        {
          id: 'YEAR',
          name: '연차',
          bgColor: '#A1B56C',
          borderColor: '#A1B56C'
        },
        {
          id: 'GYEONGJOSA',
          name: '경조사',
          bgColor: '#ffed7d',
          borderColor: '#ffed7d'
        },
        {
          id: 'ETC',
          name: '기타',
          bgColor: '#ff9a93',
          borderColor: '#ff9a93'
        }
      ],
      scheduleList: [],
      timezones: [{
        timezoneOffset: 540,
        displayLabel: 'GMT+09:00',
        tooltip: 'Seoul'
      }, {
        timezoneOffset: -420,
        displayLabel: 'GMT-08:00',
        tooltip: 'Los Angeles'
      }],
      template: {
        milestone(schedule) {
          return `<span style="color:#fff;background-color: ${schedule.bgColor};">${schedule.title}</span>`
        },
        milestoneTitle() {
          return 'Milestone'
        },
        allday(schedule) {
          return `<i class="el-icon-user-solid"></i>${schedule.title}`
        },
        alldayTitle() {
          return 'All Day'
        }
      },
      month: {
        startDayOfWeek: 0,
        daynames: ['일', '월', '화', '수', '목', '금', '토']
      },
      week: {
        showTimezoneCollapseButton: true,
        timezonesCollapsed: true
      },
      taskView: true,
      scheduleView: true,
      useDetailPopup: true,
      disableDblClick: true,
      disableClick: true,
      isReadOnly: true,
      loading: false
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'holidayList'
    ])
  },
  watch: {
    selectedView(newValue) {
      this.$refs.tuiCal.invoke('changeView', newValue, true)
      this.setRenderRangeText()
    },
    deleteDeadline: {
      handler: function(val) {
        this.scheduleList.map(value => {
          if (value.start === val.start && value.end === val.end) {
            this.scheduleList.splice(this.scheduleList.indexOf(value), 1)
          }
        })
      },
      deep: true
    },
    isUpdate: {
      handler: function(val) {
        this.isReadOnly = val
        this.disableClick = val
      }
    },
    $route: {
      handler: function(route) {
        if (route.name.indexOf('annual-deadline') > -1) {
          this.isReadOnly = false
          this.disableClick = false
          this.calendarList = [{
            id: 'DEADLINE',
            name: '사용예정',
            bgColor: '#d8b4bf',
            borderColor: '#d8b4bf'
          }]
          this.viewList = ['DEADLINE']
        }
      },
      immediate: true
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.setRenderRangeText()
      this.setData()
    },
    setDate(value) {
      const { invoke } = this.$refs.tuiCal
      invoke('setDate', new Date(value))
      invoke('changeView', 'month', true)
      this.setData()
    },
    setRenderRangeText() {
      const { invoke } = this.$refs.tuiCal
      const view = invoke('getViewName')
      const calDate = invoke('getDate')
      const rangeStart = invoke('getDateRangeStart')
      const rangeEnd = invoke('getDateRangeEnd')
      let year = calDate.getFullYear()
      let month = calDate.getMonth() + 1
      month = month >= 10 ? month : '0' + month
      let date = calDate.getDate()
      let dateRangeText = ''
      let endMonth, endDate, start, end
      switch (view) {
        case 'month':
          dateRangeText = `${year}-${month}-01`
          break
        case 'week':
          year = rangeStart.getFullYear()
          month = rangeStart.getMonth() + 1
          date = rangeStart.getDate()
          endMonth = rangeEnd.getMonth() + 1
          endDate = rangeEnd.getDate()
          start = `${year}-${month}-${date}`
          end = `${endMonth}-${endDate}`
          dateRangeText = `${start} ~ ${end}`
          break
        default:
          dateRangeText = `${year}-${month}-${date}`
      }
      this.dateRange = dateRangeText
    },
    handleMove(move) {
      this.$refs.tuiCal.invoke(move)
      this.setRenderRangeText()
      this.setData()
    },
    onClickDeadline(res) {
      const date = new Date()
      date.setHours(0, 0, 0, 0)
      const selDate = new Date(res.start)
      selDate.setHours(0, 0, 0, 0)
      let isCheck = false
      /* this.scheduleList.map(value => {
        isCheck = selDate >= value.start && selDate <= value.end
      })*/
      for (const schedule of this.scheduleList) {
        const start = new Date(schedule.start)
        const end = new Date(schedule.end)
        if (start <= selDate && selDate <= end) {
          isCheck = true
          break
        }
      }
      if (selDate.getFullYear().toString() === this.year) {
        if (res.start > date && !isCheck && selDate.getDay() > 0 && selDate.getDay() < 6 && !this.holidayList.includes(this.$moment(selDate).format('YYYY-MM-DD'))) {
          this.$emit('onClickDeadline', res)
        }
      } else {
        this.$message({
          type: 'warning',
          message: '선택할 수 있는 일자가 초과되었습니다'
        })
      }
      const { invoke } = this.$refs.tuiCal
      invoke('render')
    },
    onClickSchedule(res) {

    },
    onClickDayname(res) {
    },
    onBeforeDeleteSchedule(res) {
    },
    onAfterRenderSchedule(res) {
      res.schedule.isReadOnly = true
      this.handleChange()
    },
    onClickTimezonesCollapseBtn(timezonesCollapsed) {
      // view : week, day
      console.group('onClickTimezonesCollapseBtn')
      console.log('Is Collapsed Timezone? ', timezonesCollapsed)
      console.groupEnd()
      if (timezonesCollapsed) {
        this.theme['week.timegridLeft.width'] = '100px'
        this.theme['week.daygridLeft.width'] = '100px'
      } else {
        this.theme['week.timegridLeft.width'] = '50px'
        this.theme['week.daygridLeft.width'] = '50px'
      }
    },
    handleChange() {
      const val = this.viewList
      const { invoke } = this.$refs.tuiCal
      this.calendarList.map(value => {
        if (val.indexOf(value.id) > -1) {
          value.bgColor = value.borderColor
          invoke('toggleSchedules', value.id, false, false)
        } else {
          value.bgColor = ''
          invoke('toggleSchedules', value.id, true, false)
        }
      })
      invoke('render')
    },
    async setData() {
      if (this.$route.name.indexOf('annual-deadline') > -1) {
        this.scheduleList = this.deadlines
      } else {
        const data = {
          orgCode: this.$store.getters.orgCode,
          month: this.dateRange
        }
        this.loading = true
        await api_vacation_calendar_list(data).then(resource => {
          this.scheduleList = resource.data
          this.loading = false
        }).catch(() => { this.loading = false })
      }
    }
  }
}
</script>
<style scoped>
  .render-range {
    margin: 10px 0 0 20px;
  }
</style>
