<template>
  <div class="app-container" style="overflow: auto">
    <el-row :gutter="15" type="flex" justify="center">
      <el-col :span="20">
        <el-tabs v-loading="loading1" type="card">
          <el-tab-pane>
            <span slot="label"><i class="el-icon-date" />자산정보</span>
            <el-form ref="form" :rules="rules" :model="form" size="small" label-position="left">

              <el-card class="grid-wrapper" shadow="never">
                <div slot="header" class="grid-header">
                  <span>자산정보</span>
                </div>
                <div v-if="isUpdate" style="text-align: right;margin-bottom: 10px">
                  <el-button @click="handleQrCreate">QR-Code Create</el-button>
                </div>
                <el-row :gutter="15">
                  <el-col :xs="24" :sm="12" :lg="12">
                    <el-card
                      class="grid-wrapper scroll"
                      shadow="never"
                      style="height: 650px;overflow: auto"
                    >
                      <div slot="header" class="grid-header">
                        <span>기본항목</span>
                      </div>
                      <div v-if="form.imgSrc" style="text-align: center">
                        <el-image
                          style="width: 150px; height: 170px"
                          :src="form.imgSrc"
                        />
                      </div>
                      <el-form-item label="종류" prop="category">
                        <el-select
                          v-model="form.category"
                          style="margin-right: 10px;width: 100%"
                          :disabled="isUpdate"
                          @change="handleNextId"
                        >
                          <el-option
                            v-for="item in commonCode.assetCategory"
                            :key="item.detailCode"
                            :label="item.detailName"
                            :value="item.detailCode"
                          />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="자산번호" prop="assetId">
                        <el-input v-model="form.assetId" readonly />
                      </el-form-item>
                      <el-form-item label="제조사" prop="manufacturer">
                        <el-input v-model="form.manufacture" />
                      </el-form-item>
                      <el-form-item label="모델명" prop="modelName">
                        <el-input v-model="form.modelName" />
                      </el-form-item>
                      <el-form-item label="시리얼번호" prop="serialNumber">
                        <el-input v-model="form.serialNumber" />
                      </el-form-item>
                      <el-form-item label="구매일" prop="purchaseDate">
                        <el-date-picker
                          v-model="form.purchaseDate"
                          type="date"
                          style="width: 100%"
                          :editable="false"
                          :clearable="false"
                        />
                      </el-form-item>
                      <el-form-item label="가격" prop="price">
                        <el-input v-model="form.price" type="number" />
                      </el-form-item>
                      <el-form-item label="상태" prop="status">
                        <el-select
                          v-model="form.status"
                          style="margin-right: 10px;width: 100%"
                        >
                          <el-option
                            v-for="item in commonCode.assetStatus"
                            :key="item.detailCode"
                            :label="item.detailName"
                            :value="item.detailCode"
                          />
                        </el-select>
                      </el-form-item>
                      <el-form-item label="참고사항" prop="note">
                        <el-input v-model="form.note" />
                      </el-form-item>
                      <el-form-item label="자산이미지">
                        <el-upload
                          ref="upload"
                          :file-list="fileList"
                          :auto-upload="false"
                          :on-change="handleChange"
                          :before-remove="beforeRemove"
                          :on-remove="handleRemove"
                          action=""
                          :limit="1"
                          style="text-align: right"
                        >
                          <el-button v-if="!isImage" slot="trigger" size="small">사진 등록
                          </el-button>
                        </el-upload>
                      </el-form-item>
                    </el-card>
                  </el-col>
                  <el-col :xs="24" :sm="12" :lg="12">
                    <el-card
                      class="grid-wrapper scroll"
                      shadow="never"
                      style="height: 270px;overflow: visible;"
                    >
                      <div slot="header" class="grid-header" style="position: relative">
                        <span>관리주체</span>
                      </div>
                      <el-form-item label="부서" prop="orgCode">
                        <treeselects
                          v-model="form.orgCode"
                          :options="$store.getters.orgList"
                          :clearable="false"
                          placeholder="선택"
                          :default-expand-level="1"
                          style="font-size: small;"
                          @select="handleUserFind"
                        >
                          <div slot="value-label" slot-scope="{ node }">{{ node.raw.orgFullName }}</div>
                        </treeselects>
                      </el-form-item>
                      <el-form-item label="관리자" prop="userId">
                        <treeselects
                          v-model="form.userId"
                          :options="userList"
                          placeholder="선택"
                          :default-expand-level="1"
                          style="font-size: small;"
                          :normalizer="normalizer"
                          :disabled="userList.length === 0"
                        />
                      </el-form-item>

                    </el-card>
                    <el-card
                      v-if="form.category !== ''"
                      class="grid-wrapper scroll"
                      shadow="never"
                      style="height: 360px;overflow: auto"
                    >
                      <div slot="header" class="grid-header">
                        <span>사양</span>
                      </div>
                      <pc-info v-if="form.category === 'PC'" :info="form.assetInfo" />
                      <monitor-info v-if="form.category === 'MT'" :info="form.assetInfo" />
                      <notebook-info v-if="form.category === 'NT'" :info="form.assetInfo" />
                      <server-info v-if="form.category === 'SV'" :info="form.assetInfo" />
                    </el-card>
                  </el-col>
                </el-row>
              </el-card>
            </el-form>
          </el-tab-pane>
          <!-- <el-tab-pane :disabled="form.price === '' && form.purchaseDate === ''">
            <span slot="label"><i class="el-icon-date" />감가상각</span>
            <el-card class="grid-wrapper" shadow="never">
              <div slot="header" class="grid-header">
                <span>감가상각</span>
              </div>
            </el-card>
          </el-tab-pane>-->
          <div style="text-align: right">
            <router-link :to="{name: 'admin-asset-list'}" style="margin-right: 10px">
              <el-button size="small">목록</el-button>
            </router-link>
            <el-button v-if="!isUpdate" :loading="loading" type="primary" size="small" @click="handleRegister">등록</el-button>
            <el-button v-else :loading="loading" type="warning" size="small" @click="handleUpdate">수정</el-button>
          </div>
        </el-tabs>
      </el-col>
      <qr-code-create :dialog-visible="dialogVisible" :data="qrData" :info="form" @handleClose="handleClose" />
    </el-row>
  </div>
</template>

<script>
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { mapGetters } from 'vuex'
import { api_asset_id_next } from '@/api/asset/asset'
import { validImage } from '@/utils/validate'
import PcInfo from './components/pc-info'
import MonitorInfo from './components/monitor-info'
import NotebookInfo from './components/notebook-info'
import ServerInfo from './components/server-info'
import { edit_message, fail_message, necessary_message, register_message } from './message/constant'
import { api_asset_image_delete, api_asset_image_upload, api_asset_register, api_asset_detail, api_asset_update } from '@/api/asset/asset'
import { api_org_user_list } from '@/api/user/user'
import QrCodeCreate from './components/qr-code-create'
import CryptoJS from 'crypto-js'
export default {
  name: 'AssetEdit',
  components: { QrCodeCreate, ServerInfo, NotebookInfo, MonitorInfo, PcInfo, Treeselects },
  data() {
    const validateCommon = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      loading1: false,
      form: {
        assetId: '',
        imgSrc: '',
        category: '',
        price: '',
        status: '',
        note: '',
        modelName: '',
        manufacture: '',
        purchaseDate: '',
        orgCode: null,
        userId: null,
        assetInfo: {}
      },
      qrData: '',
      forms: new FormData(),
      rules: {
        modelName: [{ required: true, trigger: 'blur', validator: validateCommon }],
        orgCode: [{ required: true, trigger: 'change', validator: validateCommon }],
        userId: [{ required: true, trigger: 'change', validator: validateCommon }],
        assetId: [{ required: true, trigger: 'blur', validator: validateCommon }],
        manufacture: [{ required: true, trigger: 'blur', validator: validateCommon }],
        purchaseDate: [{ required: true, trigger: 'change', validator: validateCommon }],
        status: [{ required: true, trigger: 'change', validator: validateCommon }],
        price: [{ required: true, trigger: 'blur', validator: validateCommon }],
        category: [{ required: true, trigger: 'change', validator: validateCommon }]
      },
      isImage: false,
      isUpdate: false,
      uploadList: [],
      fileList: [],
      userList: [],
      dialogVisible: false,
      normalizer(node) {
        return {
          id: node.userId,
          label: node.userName + ' ' + node.rankNm,
          children: ''
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'commonCode',
      'role'
    ])
  },
  created() {
    if (this.$route.query.assetId !== undefined) {
      this.isUpdate = true
      this.getAssetInfo(this.$route.query.assetId)
    }
  },
  methods: {
    handleNextId(val) {
      api_asset_id_next(val).then(response => {
        const { data } = response
        this.form.assetId = data
        this.form.assetInfo = {}
      })
    },
    handleChange(file, fileList) {
      if (this.beforeAvatarUpload(file)) {
        file.upload = false
        this.uploadList.push(file.raw)
        this.readURL(file.raw)
        this.isImage = true
      } else {
        fileList.splice(fileList.indexOf(file), 1)
      }
    }, beforeRemove(file) {
      if (file.upload === undefined) {
        return this.$confirm(`삭제하시겠습니까? (${file.name} )`)
      }
    },
    beforeAvatarUpload(file) {
      const isJPG = validImage(file.name)
      if (!isJPG) {
        this.$message.error('사진파일만 등록 가능합니다')
      }
      return isJPG
    },
    async handleRemove(file) {
      if (file.upload === undefined) {
        await api_asset_image_delete(this.form.assetId)
      }
      this.uploadList = []
      this.isImage = false
      this.form.imgSrc = ''
    },
    readURL(value) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.form.imgSrc = e.target.result
      }
      reader.readAsDataURL(value)
    },
    handleRegister() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.form.info = this.info
          this.loading = true
          await api_asset_register(this.form).then(async() => {
            this.$message({
              type: 'success',
              message: register_message
            })
            if (this.uploadList.length > 0) {
              await this.handleFileUpload()
            } else {
              this.loading = false
            }
            if (this.role === 'ROLE_ADMIN') {
              this.$router.push({ name: 'admin-asset-list' })
            } else {
              this.$router.push({ name: 'user-asset-list' })
            }
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    async handleFileUpload() {
      this.forms = new FormData()
      for (let i = 0; i < this.uploadList.length; i++) {
        this.forms.append('files', this.uploadList[i])
      }
      this.forms.append('assetId', this.form.assetId)
      await api_asset_image_upload(this.forms).then(() => {
        this.loading = false
      }).catch(() => {
        this.loading = false
        this.$message({
          type: 'error',
          message: fail_message
        })
      })
    },
    async handleUserFind(org) {
      await api_org_user_list(org.orgCode).then(response => {
        const { data } = response
        this.userList = data.userList
        this.userList.sort(function(a, b) {
          return b.rankCd < a.rankCd ? -1 : b.rankCd > a.rankCd ? 1 : 0
        })
        this.loading1 = false
      }).catch(() => {
        this.loading1 = false
      })
    },
    async getAssetInfo(assetId) {
      this.loading1 = true
      await api_asset_detail(assetId).then(response => {
        const { data } = response
        if (data.assetInfo.networks) {
          data.assetInfo.address = data.assetInfo.networks[0].address
        }
        this.form = data
        this.fileList = data.fileList
        if (this.fileList.length > 0) {
          this.isImage = true
        }
        this.handleUserFind(data)
      }).catch(() => {
        this.loading1 = false
      })
    },
    handleUpdate() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          this.form.info = this.info
          this.loading = true
          await api_asset_update(this.form).then(async() => {
            this.$message({
              type: 'success',
              message: edit_message
            })
            if (this.uploadList.length > 0) {
              await this.handleFileUpload()
            } else {
              this.loading = false
            }
            if (this.role === 'ROLE_ADMIN') {
              this.$router.push({ name: 'admin-asset-list' })
            } else {
              this.$router.push({ name: 'user-asset-list' })
            }
          }).catch(() => {
            this.loading = false
          })
        } else {
          return false
        }
      })
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleQrCreate() {
      const encrypt_key = process.env.VUE_APP_ENCRYPT_KEY
      const assetId = CryptoJS.AES.encrypt(this.form.assetId, encrypt_key).toString()
      this.dialogVisible = true
      this.qrData = `${location.origin}/asset-info?encrypt=${assetId}`
    }
  }
}
</script>

<style scoped>

</style>
