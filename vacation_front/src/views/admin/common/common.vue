<template>
  <div class="app-container">
    <el-row type="flex" justify="center" style="margin-bottom: 10px">
      <el-col :xs="24" :sm="24" :lg="16">
        <el-card shadow="never" class="grid-wrapper" style="margin-bottom: 10px">
          <el-row :gutter="20">
            <el-col :xs="12" :sm="4" :lg="3">
              <el-select
                v-model="searchForm.searchType"
                style="margin-bottom: 10px;width: 100%;"
                size="small"
                @change="searchForm.searchWord=''"
              >
                <el-option value="" label="전체">전체</el-option>
                <el-option value="groupCode" label="그룹코드">그룹코드</el-option>
                <el-option value="groupName" label="그룹코드명">그룹코드명</el-option>
              </el-select>
            </el-col>
            <el-col :xs="12" :sm="6" :lg="8">
              <el-input
                v-model="searchForm.searchWord"
                :disabled="searchForm.searchType === ''"
                size="small"
                @keyup.enter.native="handleFilter"
              />
            </el-col>
            <el-col :xs="24" :sm="6" :lg="8">
              <el-button
                size="small"
                style="margin-right: 10px"
                icon="el-icon-search"
                @click="handleFilter"
              >검색
              </el-button>
              <router-link :to="{name: 'common-edit'}" class="link-type">
                <el-button size="small" icon="el-icon-folder-add">등록</el-button>
              </router-link>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="16">
        <el-card shadow="never" :body-style="{padding:'10px'}" style="height: 765px;">
          <el-table
            v-loading="loading"
            :header-cell-style="{background:'#F5F7FA'}"
            height="650px"
            style="width: 100%;"
            :data="commonGroupList"
            size="small"
          >
            <el-table-column
              label="그룹코드"
              align="center"
              min-width="100"
            >
              <template slot-scope="{row}">
                <span>{{ row.groupCode }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="groupName"
              label="그룹코드명"
              align="center"
              min-width="100"
            >
              <template slot-scope="{row}">
                <span>{{ row.groupName }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="groupInfo"
              label="설명"
              align="center"
              min-width="200"
            >
              <template slot-scope="{row}">
                <span>{{ row.groupDesc }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="최종변경시간"
              align="center"
              min-width="150"
            >
              <template slot-scope="{row}">
                <span>{{ row.modifiedAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="편집"
              align="center"
              width="70"
            >
              <template slot-scope="{row}">
                <router-link
                  :to="{name: 'common-edit', query:{groupCode : row.groupCode}}"
                  class="link-type"
                >
                  <el-button
                    type="primary"
                    size="small"
                    icon="el-icon-edit-outline"
                  />
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <paging
            :total="total"
            :page.sync="searchForm.page"
            :limit.sync="searchForm.limit"
            @pagination="getCommonGroupList"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { api_common_code_list } from '@/api/manager/common'
import { defaultSearchForm } from '@/default/data'

const searchForms = defaultSearchForm
export default {
  name: 'Common',
  components: { Paging },
  data() {
    return {
      searchForm: Object.assign({}, searchForms),
      commonGroupList: [],
      total: 0,
      loading: false
    }
  },
  created() {
    this.getCommonGroupList()
  },
  methods: {
    async getCommonGroupList() {
      this.loading = true
      await api_common_code_list(this.searchForm).then(response => {
        const { commonGroupList, total } = response.data
        this.commonGroupList = commonGroupList
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getCommonGroupList()
    }
  }
}
</script>

<style scoped>
</style>
