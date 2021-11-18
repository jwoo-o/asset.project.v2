<template>
  <div v-loading="loading" class="app-container scroll" style="overflow: auto">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="12">
        <div style="border: 2px solid black; width: 959px;margin-left: 7px;">
          <table style="width: 100%;" border="1" class="orgTable">
            <thead>
              <tr>
                <td align="center" colspan="6">
                  <b> 부서별현황({{ seatTag.filter(value => value.userId !== null ).length }}/{{ seatTag.length }})</b>
                </td>
              </tr>
            </thead>
            <tbody v-html="orgData" />
          </table>

        </div>
        <div class="seat" :style="seatStyle">
          <div
            v-for="item in seatTag"
            :id="item.id"
            :key="item.id"
            :class="item.className"
            :style="{left:item.left,top:item.top,backgroundColor: item.orgCode === '00000000' ? '#FFFFCC' :item.color}"
            @click="handleSeat(item)"
          >
            <p>{{ item.userName }}<br>{{ item.ex !== null ? '('+item.ex+')' : '' }}</p>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="dialogVisible"
      :width="width < 768 ? '95%' : width < 1200 ? '70%' : '30%'"
      center
    >
      <user-info :user-info="userInfo" />
    </el-dialog>
  </div>
</template>

<script>
import seat from '@/assets/joeunins.jpg'
import { api_user_seat_list } from '@/api/user/user'
import { mapGetters } from 'vuex'
import UserInfo from './components/user-info'
export default {
  name: 'SeatIndex',
  components: { UserInfo },
  // eslint-disable-next-line vue/require-prop-types
  props: ['type'],
  data() {
    return {
      seatTag: [],
      orgList: [],
      seatStyle: {},
      loading: false,
      userInfo: {},
      dialogVisible: false,
      orgData: ''
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  created() {
    this.init()
  },
  methods: {
    async init() {
      this.loading = true
      this.seatStyle = {
        backgroundImage: 'url(' + seat + ')'
      }
      await api_user_seat_list().then(response => {
        const { seatList, orgList } = response.data
        this.seatTag = seatList
        this.orgList = orgList
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
      this.orgData = '<tr>'
      let count = 1
      const orgList = []
      for (const org of this.orgList.filter(value => value.orgCode !== '-1')) {
        for (const subOrg of this.orgList.filter(value => value.orgCode !== '-1')) {
          if (org.orgCode === subOrg.orgPaCode && org.color !== subOrg.color) {
            orgList.push(subOrg)
          }
        }
      }
      for (const org of orgList) {
        this.orgData += `<td align="center" style="background:${org.color}">${org.orgName}</td><td width="5%" align="center">${this.seatTag.filter(value => value.fullCode !== null && value.fullCode.includes(org.orgCode) && value.color === org.color).length}</td>`
        if (count++ % 3 === 0) {
          this.orgData += `</tr><tr>`
        }
      }
      this.orgData += `</tr>`
    },
    handleSeat(e) {
      if (this.type === 'user') {
        if (e.userId === null) {
          this.$emit('getSeatId', e.id)
        }
      } else {
        if (e.userId !== null) {
          this.dialogVisible = true
          this.userInfo = e
        }
      }
    }
  }
}
</script>

<style scoped>
    .seat {
        width: 975px;
        height: 800px;
        position: relative;
    }
    .seat2 {
      width: 55px;
      height: 70px;
      border: 1px solid #666666;
      position:absolute;
      padding-top: 15px;
      background-color: #BABABA;
      text-align: center;
      font-size: x-small;
    }
    .seat1 {
      width: 70px;
      height: 55px;
      border: 1px solid #666666;
      position:absolute;
      padding-top: 12px;
      background-color:#BABABA;
      text-align: center;
      font-size: x-small;
    }
    .seat2:hover {
      border: 2px solid #003399
    }
    .seat1:hover {
      border: 2px solid #003399
    }
    .orgTable {
      border-spacing: 0;
      border-collapse: collapse;
      border: 1px solid #dddddd;
      font-size: 14px;
    }
</style>
