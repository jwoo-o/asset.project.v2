<template>
  <div v-loading="loading" class="app-container">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="10" :lg="8">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span><span style="float: left">자산종합관리 </span><span style="font-size: 9pt;float: left;padding-top: 8px;margin-left: 5px">{{ '[ '+ before + ' ~ ' + now +' ]' }}</span></span>
          </div>
          <div style="height: 330px">
            <div>
              <el-form label-width="150px" label-position="left">
                <el-form-item class="">
                  <template slot="label">
                    <em class="el-icon-folder card-panel" style="background-color: #409EFF" />
                    <span>
                      전체
                    </span>
                  </template>
                  <div style="padding-top: 4px;text-align: right">
                    <span style="font-size: 20px">
                      {{ total }}
                    </span>
                  </div>
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <template slot="label">
                    <em class="el-icon-folder-add card-panel" style="background-color: #67C23A" />
                    <span>
                      신규
                    </span>
                  </template>
                  <div style="padding-top: 4px;text-align: right">
                    <span style="font-size: 20px">
                      {{ created }}
                    </span>
                  </div>
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <template slot="label">
                    <em class="el-icon-folder-checked card-panel" style="background-color: #E6A23C" />
                    <span>
                      변경
                    </span>
                  </template>
                  <div style="padding-top: 4px;text-align: right">
                    <span style="font-size: 20px">
                      {{ modified }}
                    </span>
                  </div>
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <template slot="label">
                    <em class="el-icon-folder-delete card-panel" style="background-color:#F56C6C" />
                    <span>
                      폐기
                    </span>
                  </template>
                  <div style="padding-top: 4px;text-align: right">
                    <span style="font-size: 20px">
                      {{ disposal }}
                    </span>
                  </div>
                </el-form-item>
                <el-divider />
              </el-form>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="14" :lg="16">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span>최근 업데이트 이력</span>
          </div>
          <div style="height: 330px">
            <el-table
              :data="assetList"
              height="300"
              size="small"
              @row-click="handleSelectionAsset"
            >
              <el-table-column
                label="자산번호"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.assetId }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="종류"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.category }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="모델명"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.modelName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="변경일"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ $moment(row.modifiedAt).format('YYYY-MM-DD HH:mm:ss') }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="상태"
                align="center"
                min-width="100"
              >
                <template slot-scope="{row}">
                  <span>{{ commonCode.assetStatus.filter(value => value.detailCode === row.status)[0].detailName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="참고사항"
                align="center"
                min-width="70"
              >
                <template slot-scope="{row}">
                  <span>{{ row.note }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span>그룹별 현황</span>
          </div>
          <div>
            <el-row :gutter="10">
              <el-col :xs="24" :sm="12" :lg="12">
                <pie-chart :chart-data="groupData" />
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12">
                <bar-chart :chart-data="groupData" />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span>부서별 현황</span>
          </div>
          <div>
            <el-row :gutter="10">
              <el-col :xs="24" :sm="12" :lg="12">
                <pie-chart :chart-data="orgData" />
              </el-col>
              <el-col :xs="24" :sm="12" :lg="12">
                <bar-chart :chart-data="orgData" />
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="12">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span>OS 현황</span>
          </div>
          <el-row :gutter="10">
            <el-col :xs="24" :sm="12" :lg="12">
              <pie-chart :chart-data="osData" />
            </el-col>
            <el-col :xs="24" :sm="12" :lg="12">
              <el-table
                :data="osData"
                size="small"
                height="300"
                show-summary
                :summary-method="getSummaries"
              >
                <el-table-column label="name" prop="name" />
                <el-table-column label="count" prop="value" />
              </el-table>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="12">
        <el-card class="grid-wrapper" shadow="never">
          <div slot="header" class="grid-header">
            <span>프로그램 설치 현황</span>
          </div>
          <el-table
            v-el-table-infinite-scroll="load1"
            v-loading="loading1"
            :data="programData.slice(0,limit1)"
            size="small"
            height="300"
          >
            <el-table-column label="name" prop="name" />
            <el-table-column label="count" prop="value" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PieChart from './components/pieChart'
import BarChart from './components/barChart'
import { api_asset_total } from '@/api/asset/asset'
import { mapGetters } from 'vuex'
import elTableInfiniteScroll from 'el-table-infinite-scroll'
export default {
  name: 'AssetDashboard',
  directives: {
    'el-table-infinite-scroll': elTableInfiniteScroll
  },
  components: { BarChart, PieChart },
  data() {
    return {
      groupData: [],
      orgData: [],
      now: '',
      before: '',
      total: 0,
      created: 0,
      modified: 0,
      disposal: 0,
      osData: [],
      programData: [],
      assetList: [],
      loading: false,
      loading1: false,
      limit1: 10
    }
  },
  computed: {
    ...mapGetters([
      'commonCode',
      'mgrOrgCode',
      'role'
    ])
  },
  mounted() {
    this.$nextTick(() => {
      this.getAssetTotal()
    })
  },
  methods: {
    async load1() {
      this.loading1 = true
      await setTimeout(() => {
        this.limit1 += 10
        this.loading1 = false
      }, 500)
    },
    getAssetTotal() {
      this.loading = true
      const orgCode = this.mgrOrgCode
      api_asset_total(orgCode).then(response => {
        const { data } = response
        this.now = data.now
        this.before = data.before
        this.total = data.total
        this.created = data.created
        this.modified = data.modified
        this.disposal = data.disposal
        this.groupData = data.group
        this.orgData = data.org
        this.osData = data.os
        this.programData = data.program
        this.assetList = data.assetList
        this.programData.sort(function(a, b) {
          return b.value - a.value
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleSelectionAsset(info) {
      let name = 'admin-asset-edit'
      if (this.role === 'ROLE_USER') {
        name = 'user-asset-edit'
      }
      this.$router.push({ name: name, query: { assetId: info.assetId }})
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = 'total'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
        }
      })

      return sums
    }
  }
}
</script>

<style scoped lang="scss">
  .card-panel {
    width: 40px;
    height:40px;
    font-size: 20px;
    text-align: center;
    border-radius: 5px;
    padding-top: 10px;
    margin-right: 10px;
    color: white;
  }
</style>
