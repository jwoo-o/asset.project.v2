<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-col :xs="24" :sm="24" :lg="6">
        <org-tree :org-code="searchForm.orgCode" @orgTree="getOrgInfo" />
      </el-col>
      <el-col :xs="24" :sm="24" :lg="18">
        <el-row>
          <el-col :xs="24" :sm="24" :lg="24">
            <el-card
              shadow="never"
              class="grid-wrapper scroll"
              :body-style="{padding:'10px'}"
              style="margin-bottom: 10px;height: 850px; overflow: auto"
            >
              <div
                class="grid-header"
                style="border-bottom: 1px solid #E6EBF5;margin-bottom: 10px;padding-bottom: 5px"
              >
                <el-row>
                  <el-col :xs="12" :sm="14" :lg="16">
                    <span style="margin: 10px">자산현황</span>
                  </el-col>
                </el-row>
              </div>
              <el-card shadow="never" class="grid-wrapper">
                <el-form :model="searchForm">
                  <el-row>
                    <el-col :xs="24" :sm="20" :lg="21">
                      <el-row>
                        <el-col :xs="24" :sm="8" :lg="8">
                          <el-form-item label="구매일" label-width="100px">
                            <el-date-picker
                              v-model="purchaseDate"
                              :editable="false"
                              type="daterange"
                              value-format="yyyy-MM-dd"
                              style="width: 100%"
                            />
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="자산번호" label-width="100px">
                            <el-input
                              v-model="searchForm.assetId"
                              size="small"
                            />
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="사용자" label-width="100px">
                            <el-input
                              v-model="searchForm.userName"
                              size="small"
                            />
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="검색 부서" label-width="100px">
                            <el-input v-model="searchForm.orgName" disabled size="small" />
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="종류" label-width="100px">
                            <el-select v-model="searchForm.category" style="margin-right: 10px;width: 100%" size="small">
                              <el-option label="ALL" value="" />
                              <el-option
                                v-for="item in commonCode.assetCategory"
                                :key="item.detailCode"
                                :label="item.detailName"
                                :value="item.detailCode"
                              />
                            </el-select>
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="직급" label-width="100px">
                            <el-select v-model="searchForm.rankCd" style="margin-right: 10px;width: 100%" size="small">
                              <el-option label="ALL" value="" />
                              <el-option
                                v-for="item in commonCode.rank"
                                :key="item.detailCode"
                                :label="item.detailName"
                                :value="item.detailCode"
                              />
                            </el-select>
                          </el-form-item>
                        </el-col>
                        <el-col :xs="12" :sm="8" :lg="8">
                          <el-form-item label="상태" label-width="100px">
                            <el-select v-model="searchForm.status" style="margin-right: 10px;width: 100%" size="small">
                              <el-option label="ALL" value="" />
                              <el-option
                                v-for="item in commonCode.assetStatus"
                                :key="item.detailCode"
                                :label="item.detailName"
                                :value="item.detailCode"
                              />
                            </el-select>
                          </el-form-item>
                        </el-col>
                        <el-col :xs="24" :sm="16" :lg="16">
                          <el-form-item label="참고사항" label-width="100px">
                            <el-input
                              v-model="searchForm.note"
                              size="small"
                            />
                          </el-form-item>
                        </el-col>
                      </el-row>
                    </el-col>
                    <el-col :xs="24" :sm="4" :lg="3">
                      <div style="margin-top:30px;text-align: center">
                        <el-button
                          icon="el-icon-search"
                          size="small"
                          style="width: 100px;height: 100px;"
                          @click.native.prevent="handleFilter"
                        >검색</el-button>
                      </div>
                    </el-col>
                  </el-row>
                </el-form>
              </el-card>
              <el-card shadow="never" class="grid-wrapper" :body-style="{padding:'10px'}">
                <div style="margin-bottom:10px;text-align: right">
                  <el-button
                    icon="el-icon-download"
                    size="small"
                    @click="excelDown"
                  >다운로드</el-button>
                </div>
                <el-table
                  v-loading="loading"
                  :data="assetList"
                  :header-cell-style="{background:'#F5F7FA'}"
                  height="350"
                  style="width: 100%"
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
                    label="사용자"
                    align="center"
                    min-width="50"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.userName }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="직위"
                    align="center"
                    min-width="50"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.rankName }}</span>
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
                    label="구매일"
                    align="center"
                    min-width="70"
                  >
                    <template slot-scope="{row}">
                      <span>{{ row.purchaseDate }}</span>
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
                <paging
                  :total="total"
                  :page.sync="searchForm.page"
                  :limit.sync="searchForm.limit"
                  @pagination="getAssetInfoList"
                />
                <div style="text-align: right">
                  <router-link :to="{name: 'admin-asset-edit'}">
                    <el-button
                      icon="el-icon-plus"
                      size="small"
                    >등록</el-button>
                  </router-link>
                </div>
              </el-card>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import XLSX from 'xlsx'
import Paging from '@/components/Pagination/index'
import { defaultAssetSearchForm } from '@/default/data'
import OrgTree from '@/components/OrgTree/index'
import { mapGetters } from 'vuex'
import { api_asset_list, api_excel_asset_list } from '@/api/asset/asset'
import { excelHeader, filekind } from '@/default/exceldown'
export default {
  name: 'AssetList',
  components: {
    OrgTree,
    Paging
  },
  data() {
    return {
      searchForm: Object.assign({}, defaultAssetSearchForm),
      loading: false,
      assetList: [],
      purchaseDate: [],
      total: 0,
      totalExcel: 0,
      header: [],
      excelList: [],
      fileKind: '',
      sheetName: 1,
      wb: ''
    }
  },
  computed: {
    ...mapGetters([
      'commonCode',
      'role'
    ])
  },
  created() {
    this.getAssetInfoList()
  },
  methods: {
    getOrgInfo(data) {
      if (this.searchForm.orgCode !== data.orgCode) {
        this.searchForm = Object.assign({}, defaultAssetSearchForm)
        this.searchForm.orgCode = data.orgCode
        this.searchForm.orgName = data.orgName
        this.searchForm.order = data.order
        this.purchaseDate = []
        this.handleFilter()
      }
    },
    async getAssetInfoList() {
      if (this.searchForm.orgCode === '') {
        this.searchForm.orgCode = this.$store.getters.mgrOrgCode
        this.searchForm.order = this.$store.getters.order
        this.searchForm.orgName = this.$store.getters.mgrOrgName
      }
      if (this.purchaseDate.length !== 0) {
        this.searchForm.startDate = this.purchaseDate[0]
        this.searchForm.endDate = this.purchaseDate[1]
      }
      this.loading = true
      await api_asset_list(this.searchForm).then(response => {
        const { total, assetList } = response.data
        this.assetList = assetList
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getAssetInfoList()
    },
    handleSelectionAsset(info) {
      if (this.role === 'ROLE_ADMIN') {
        this.$router.push({ name: 'admin-asset-edit', query: { assetId: info.assetId }})
      } else {
        this.$router.push({ name: 'user-asset-edit', query: { assetId: info.assetId }})
      }
    },
    async excelDown() {
      this.searchForm.limit = 1000
      await api_excel_asset_list(this.searchForm).then(response => {
        const { assetList, total } = response.data
        Array.prototype.push.apply(this.excelList, assetList)
        this.totalExcel = total
        this.excelDownCount()
      }).catch(() => {
        this.loading = false
      })
    },
    excelDownCount() {
      if (this.searchForm.offset === 1) {
        if (this.totalExcel === 0) {
          this.$message({
            type: 'warning',
            message: '데이터가 없습니다.'
          })
          this.loading = false
          return
        }
      }
      if (this.searchForm.limit === this.totalExcel) {
        this.searchForm.offset += 1
        this.excelDown()
      } else {
        this.excelFileDown()
      }
    },
    excelFileDown() {
      this.header = excelHeader.asset
      this.fileKind = filekind.asset
      const dataWS = XLSX.utils.json_to_sheet(this.excelList)
      this.header.forEach((x, idx) => {
        const cellAdd = XLSX.utils.encode_cell({ c: idx, r: 0 })
        dataWS[cellAdd].v = x
      })
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, dataWS, this.fileKind)
      this.currentDate = this.$moment(this.currentDate).format('YYYY-MM-DD HH:mm:ss')
      this.fileName = this.currentDate + ' ' + this.fileKind + '.xlsx'
      XLSX.writeFile(wb, this.fileName)
      this.$message({
        type: 'success',
        message: '다운로드 완료'
      })
      this.searchForm.offset = 1
      this.totalExcel = 0
      this.excelList = []
      this.searchForm.limit = 10
      this.loading = false
    }
  }
}
</script>

<style scoped>
</style>
