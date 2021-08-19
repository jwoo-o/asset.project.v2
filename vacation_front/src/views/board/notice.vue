<template>
  <div class="app-container">
    <el-row v-if="!isView" type="flex" justify="center" style="margin-bottom: 10px">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-card shadow="never" class="grid-wrapper" style="margin-bottom: 10px">
          <el-row :gutter="20">
            <el-col :xs="12" :sm="4" :lg="3">
              <el-select
                v-model="searchForm.searchType"
                style="margin-bottom: 10px"
                size="small"
                @change="searchForm.searchWord=''"
              >
                <el-option label="전체" value="" />
                <el-option label="작성자" value="name" />
                <el-option label="제목" value="subject" />
                <el-option label="내용" value="content" />
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
              <el-button size="small" icon="el-icon-search" @click="handleFilter">검색</el-button>
              <router-link :to="{name: 'board-edit'}" style="margin-left: 10px">
                <el-button size="small" icon="el-icon-plus">등록</el-button>
              </router-link>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row v-if="!isView" type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-card shadow="never" :body-style="{padding:'10px'}" style="height: 705px;">
          <el-table
            v-loading="loading"
            :header-cell-style="{background:'#F5F7FA'}"
            style="width: 100%"
            height="615px"
            :data="noticeList"
            size="small"
            :class="'scroll'"
            @row-click="handleNoticeInfo"
          >
            <el-table-column
              width="70"
              label="등록번호"
              align="center"
            >
              <template slot-scope="{row}">
                <span>{{ row.id }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="제목"
              align="center"
            >
              <template slot-scope="{row}">
                <span>{{ row.subject }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="작성자"
              align="center"
            >
              <template slot-scope="{row}">
                <span>{{ row.writer }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="첨부파일"
              width="80"
              align="center"
            >
              <template slot-scope="{row}">
                <span v-if="row.isAttach > 0" class="el-icon-s-claim" style="font-size: 20px" />
                <span v-else class="el-icon-s-release" style="font-size: 20px" />
              </template>
            </el-table-column>
            <el-table-column
              label="등록일"
              align="center"
            >
              <template slot-scope="{row}">
                <span>{{ row.createdAt | moment('YYYY-MM-DD HH:mm:ss') }}</span>
              </template>
            </el-table-column>
            <el-table-column
              label="조회수"
              width="80"
              align="center"
            >
              <template slot-scope="{row}">
                <span>{{ row.count }}</span>
              </template>
            </el-table-column>
          </el-table>
          <paging
            :total="total"
            :page.sync="searchForm.page"
            :limit.sync="searchForm.limit"
            @pagination="getNoticeList"
          />
        </el-card>
      </el-col>
    </el-row>
    <el-row v-else type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-card shadow="never" class="grid-wrapper" :body-style="{padding:0}">
          <div class="content">
            <el-row>
              <el-col :xs="24" :sm="24" :lg="24">
                <el-form>
                  <el-form-item label="작성자" label-width="100px">
                    <el-input :value="notice.writer" />
                  </el-form-item>
                  <el-form-item label="제목" label-width="100px">
                    <el-input :value="notice.subject" />
                  </el-form-item>
                  <el-form-item label="내용" label-width="100px">
                    <div
                      class="content-div"
                      v-html="notice.content"
                    />
                  </el-form-item>
                  <el-form-item label-width="100px" label="첨부파일">
                    <el-upload
                      ref="upload"
                      :file-list="notice.fileList"
                      :auto-upload="false"
                      :disabled="true"
                      :on-preview="handlePreview"
                      action=""
                    />
                  </el-form-item>
                  <el-form-item label="등록일" label-width="100px">
                    <el-input :value="notice.createdAt | moment('YYYY-MM-DD HH:mm:ss')" />
                  </el-form-item>
                  <el-form-item label="수정일" label-width="100px">
                    <el-input :value="notice.modifiedAt | moment('YYYY-MM-DD HH:mm:ss')" />
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col :xs="24" :sm="24" :lg="24">
                <div style="text-align:right;">
                  <router-link :to="{name:'board-edit',query:{id:notice.id}}" style="margin-right: 10px">
                    <el-button type="primary" size="small" icon="el-icon-edit-outline">
                      수정
                    </el-button>
                  </router-link>
                  <el-button
                    type="danger"
                    size="small"
                    icon="el-icon-delete"
                    @click="handleDelete"
                  >삭제
                  </el-button>
                  <el-button size="small" icon="el-icon-folder" @click="handleClose">목록</el-button>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Paging from '@/components/Pagination/index'
import { api_notice_list, api_notice_count_up, api_notice_delete } from '@/api/board/board'
import { delete_message } from './message/constant'

const defaultSearchForm = {
  searchType: '',
  searchWord: '',
  page: 1,
  limit: 10
}
const defaultNotice = {
  writer: '',
  id: '',
  subject: '',
  content: '',
  createdAt: '',
  modifiedAt: '',
  fileList: []
}
export default {
  name: 'Notice',
  components: { Paging },
  data() {
    return {
      noticeList: [],
      notice: Object.assign({}, defaultNotice),
      total: 0,
      searchForm: Object.assign({}, defaultSearchForm),
      loading: false,
      loading1: false,
      isView: false
    }
  },
  created() {
    this.getNoticeList()
  },
  methods: {
    async getNoticeList() {
      this.loading = true
      await api_notice_list(this.searchForm).then(response => {
        this.loading = false
        const { noticeList, total } = response.data
        this.noticeList = noticeList
        this.total = total
      }).catch(() => {
        this.loading = false
      })
    },
    handleFilter() {
      this.searchForm.page = 1
      this.getNoticeList()
    },
    handlePreview(file) {
      const link = document.createElement('a')
      link.href = file.url
      link.download = file.name
      document.body.appendChild(link)
      link.click()
    },
    handleClose() {
      this.isView = false
      this.notice = Object.assign({}, defaultNotice)
    },
    async handleDelete() {
      await api_notice_delete(this.notice.id).then(response => {
        this.$message({
          type: 'success',
          message: delete_message
        })
        this.isView = false
        this.getNoticeList()
      })
    },
    async handleNoticeInfo(row) {
      this.loading1 = true
      await api_notice_count_up(row.id).then(response => {
        this.notice = response.data
        this.isView = true
        this.loading1 = false
      }).catch(() => {
        this.loading1 = false
      })
    }
  }
}
</script>

<style>
  .content-div {
    overflow: auto;
    min-height: 300px;
    border: 1px solid #DCDFE6;
    border-radius: 5px;
    padding-left: 15px;
  }

  .content-div p {
    margin-top: 0px;
  }

  .content-div::-webkit-scrollbar {
    width: 10px;
  }

  .content-div::-webkit-scrollbar-thumb {
    background-color: #626ea6;
    border-radius: 10px;
    background-clip: padding-box;
    border: 3px solid transparent;
  }

  .content-div::-webkit-scrollbar-track {
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: inset 0px 0px 5px white;
  }
</style>
