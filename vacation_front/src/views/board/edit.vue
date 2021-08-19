<template>
  <div class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="24" :lg="12">
        <el-card v-loading="loading1" shadow="never" style="padding: 30px">
          <el-form ref="noticeForm" size="small" :model="noticeForm" :rules="noticeRules">
            <el-row>
              <el-col :span="18">
                <el-form-item style="margin-bottom: 40px;" prop="subject" align="left">
                  <el-input v-model="noticeForm.subject" name="title" placeholder="제목" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-form-item prop="content" style="margin-bottom: 30px;">
                <Tinymce id="editor" v-model="noticeForm.content" :height="400" />
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="18" align="left">
                <el-upload
                  ref="upload"
                  :file-list="fileList"
                  :auto-upload="false"
                  :on-change="handleChange"
                  :before-remove="beforeRemove"
                  :on-remove="handleRemove"
                  action=""
                >
                  <el-button slot="trigger" size="small">파일 업로드</el-button>
                  <div slot="tip" class="el-upload__tip"> {{ fileMessage }}</div>
                </el-upload>
              </el-col>
            </el-row>
          </el-form>
          <el-row>
            <el-col :span="24" align="right">
              <el-button
                icon="el-icon-check"
                size="small"
                :loading="loading"
                style="margin-right: 10px"
                @click="handleConfirm"
              >저장
              </el-button>
              <router-link :to="{name: 'board-notice'}">
                <el-button size="small" icon="el-icon-folder">목록</el-button>
              </router-link>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { edit_message, necessary_message, register_message, fail_message } from './message/constant'
import {
  api_notice_register,
  api_notice_info,
  api_notice_file_delete,
  api_notice_edit,
  api_notice_file_upload
} from '@/api/board/board'
import { maxFileUploadMessage, maxFileUploadSize } from '@/default/data'

export default {
  name: 'BoardEdit',
  components: { Tinymce },
  data() {
    const validateCommon = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    return {
      isEdit: false,
      id: null,
      noticeForm: {
        adminId: this.$store.getters.adminId,
        subject: '',
        content: '',
        fileIds: null
      },
      fileList: [],
      uploadList: [],
      forms: new FormData(),
      noticeRules: {
        subject: [{ required: true, trigger: 'blur', validator: validateCommon }]
      },
      loading: false,
      loading1: false,
      fileSize: null,
      fileMessage: null
    }
  },
  created() {
    this.getMaxFileList()
    this.id = this.$route.query.id
    if (this.id !== undefined) {
      this.isEdit = true
      this.getNoticeInfo()
    }
  },
  methods: {
    handleChange(file, fileList) {
      if (file.size > this.fileSize) {
        for (let i = 0; i < fileList.length; i++) {
          if (fileList[i].size > this.fileSize) {
            fileList.splice(i, 1)
          }
        }
      } else {
        file.upload = false
        this.uploadList.push(file.raw)
        // this.forms.append('files', file.raw)
      }
    },
    handleConfirm() {
      this.$refs.noticeForm.validate(async valid => {
        if (valid) {
          this.loading = true
          if (this.uploadList.length > 0) {
            await this.handleFileUpload()
          }
          if (this.isEdit) {
            api_notice_edit(this.id, this.noticeForm).then(() => {
              this.loading = false
              this.$message({
                type: 'success',
                message: edit_message
              })
              this.$router.push({ name: 'board-notice' })
            }).catch(() => {
              this.loading = false
            })
          } else {
            api_notice_register(this.noticeForm).then(() => {
              this.loading = false
              this.$message({
                type: 'success',
                message: register_message
              })
              this.$router.push({ name: 'board-notice' })
            }).catch(() => {
              this.loading = false
            })
          }
        } else {
          return false
        }
      })
    },
    beforeRemove(file) {
      if (file.upload === undefined) {
        return this.$confirm(`삭제하시겠습니까? (${file.name} )`)
      }
    },
    async handleRemove(file) {
      if (file.upload === undefined) {
        await api_notice_file_delete(file.id)
      } else {
        for (let i = 0; i < this.uploadList.length; i++) {
          if (this.uploadList[i].name === file.name) {
            this.uploadList.splice(i, 1)
          }
        }
      }
    },
    async getNoticeInfo() {
      this.loading1 = true
      await api_notice_info(this.id).then(response => {
        const { adminId, subject, content, fileList } = response.data
        this.noticeForm.adminId = adminId
        this.noticeForm.content = content
        this.noticeForm.subject = subject
        this.fileList = fileList
        this.loading1 = false
      }).catch(() => {
        this.loading1 = false
      })
    },
    async handleFileUpload() {
      for (let i = 0; i < this.uploadList.length; i++) {
        this.forms.append('files', this.uploadList[i])
      }
      await api_notice_file_upload(this.forms).then(response => {
        this.noticeForm.fileIds = response.data
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
  @import "../../styles/mixin.scss";
</style>
