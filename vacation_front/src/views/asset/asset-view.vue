<template>
  <div class="app-container scroll" style="overflow: auto">
    <el-row :gutter="15" type="flex" justify="center">
      <el-col :span="20">
        <el-tabs v-loading="loading1" type="card">
          <el-tab-pane>
            <span slot="label"><i class="el-icon-date" />자산정보</span>
            <el-form :model="form" size="small" label-position="left">
              <el-card class="grid-wrapper" shadow="never">
                <div slot="header" class="grid-header">
                  <span>자산정보</span>
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
                        <el-input v-model="form.category" readonly />
                      </el-form-item>
                      <el-form-item label="자산번호">
                        <el-input v-model="form.assetId" readonly />
                      </el-form-item>
                      <el-form-item label="제조사">
                        <el-input v-model="form.manufacture" readonly />
                      </el-form-item>
                      <el-form-item label="모델명">
                        <el-input v-model="form.modelName" readonly />
                      </el-form-item>
                      <el-form-item label="시리얼번호">
                        <el-input v-model="form.serialNumber" readonly />
                      </el-form-item>
                      <el-form-item label="구매일">
                        <el-input v-model="form.purchaseDate" readonly />
                      </el-form-item>
                      <el-form-item label="가격" prop="price">
                        <el-input v-model="form.price" type="number" readonly />
                      </el-form-item>
                      <el-form-item label="상태" prop="status">
                        <el-input v-model="form.status" readonly />
                      </el-form-item>
                      <el-form-item label="참고사항" prop="note">
                        <el-input v-model="form.note" readonly />
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
                        <el-input v-model="form.orgFullName" readonly />
                      </el-form-item>
                      <el-form-item label="관리자" prop="userId">
                        <el-input v-model="form.userName" readonly />
                      </el-form-item>

                    </el-card>
                    <el-card
                      class="grid-wrapper scroll"
                      shadow="never"
                      style="height: 360px;overflow: auto"
                    >
                      <div slot="header" class="grid-header">
                        <span>사양</span>
                      </div>
                      <pc-info v-if="form.category === 'PC'" :info="form.assetInfo" :view="true" />
                      <monitor-info v-if="form.category === 'MT'" :info="form.assetInfo" :view="true" />
                      <notebook-info v-if="form.category === 'NT'" :info="form.assetInfo" :view="true" />
                      <server-info v-if="form.category === 'SV'" :info="form.assetInfo" :view="true" />
                    </el-card>
                  </el-col>
                </el-row>
              </el-card>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PcInfo from './components/pc-info'
import MonitorInfo from './components/monitor-info'
import NotebookInfo from './components/notebook-info'
import ServerInfo from './components/server-info'
import { api_asset_detail_no_token } from '@/api/asset/asset'
import CryptoJS from 'crypto-js'
export default {
  name: 'AssetEdit',
  components: { ServerInfo, NotebookInfo, MonitorInfo, PcInfo },
  data() {
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
      isImage: false,
      uploadList: [],
      fileList: [],
      userList: [],
      dialogVisible: false,
      normalizer(node) {
        return {
          id: node.userId,
          label: node.userName,
          children: ''
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'commonCode'
    ])
  },
  created() {
    if (this.$route.query.encrypt !== undefined) {
      const encrypt_key = process.env.VUE_APP_ENCRYPT_KEY
      const bytes = CryptoJS.AES.decrypt(decodeURI(this.$route.query.encrypt.replace(' ', '+')), encrypt_key)
      const assetId = bytes.toString(CryptoJS.enc.Utf8)
      if (assetId === '') {
        window.close() // 일반적인 현재 창 닫기
        window.open('about:blank', '_self').self.close()
      } else {
        this.getAssetInfo(assetId)
      }
    } else {
      window.close() // 일반적인 현재 창 닫기
      window.open('about:blank', '_self').self.close()
    }
  },
  methods: {
    readURL(value) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.form.imgSrc = e.target.result
      }
      reader.readAsDataURL(value)
    },
    async getAssetInfo(assetId) {
      this.loading1 = true
      await api_asset_detail_no_token(assetId).then(response => {
        this.loading1 = false
        const { data } = response
        this.form = data
        this.fileList = data.fileList
        if (this.fileList.length > 0) {
          this.isImage = true
        }
        this.handleUserFind(data)
      }).catch(() => {
        this.loading1 = false
      })
    }
  }
}
</script>

<style scoped>

</style>
