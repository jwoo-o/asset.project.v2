<template>
  <div class="app-container">
    <el-row>
      <div style="min-width: 700px; width: 60%;">
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
            </el-row>
          </div>
        </el-card>
      </div>
    </el-row>
  </div>
</template>

<script>
import { api_notice_list, api_notice_count_up } from '@/api/board/board'

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
  name: 'Popup',
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
