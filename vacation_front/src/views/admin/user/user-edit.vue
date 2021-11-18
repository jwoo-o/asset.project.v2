<template>
  <div v-if="!isSeat" class="app-container">
    <el-row type="flex" justify="center">
      <el-col :xs="24" :sm="12" :lg="8">
        <el-card
          v-loading="loading"
          class="grid-wrapper scroll"
          shadow="never"
          :body-style="{padding:0}"
        >
          <div class="header">
            <span>사용자 정보</span>
          </div>
          <el-form ref="userForm" size="small" :model="userForm" :rules="userRules">
            <div class="content">
              <el-row>
                <el-col
                  v-if="userForm.imgSrc !== ''"
                  :xs="24"
                  :sm="24"
                  :lg="24"
                  align="center"
                  style="margin-bottom: 10px"
                >
                  <el-image
                    style="width: 150px; height: 170px"
                    :src="userForm.imgSrc"
                  />
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="아이디" label-width="120px" prop="userId">
                    <el-input
                      v-model="userId"
                      :disabled="isEdit"
                      @blur="isIdValid=false"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="이름" label-width="120px" prop="name">
                    <el-input v-model="userForm.name" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="소속부서" label-width="120px" prop="orgCode">
                    <treeselects
                      v-model="userForm.orgCode"
                      :default-expand-level="1"
                      :options="$store.getters.orgList"
                      :clearable="false"
                      placeholder="선택"
                    >
                      <div slot="value-label" slot-scope="{ node }">{{ node.raw.orgFullName }}</div>
                    </treeselects>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="직급" label-width="120px" prop="rankIndex">
                    <el-select
                      v-model="rankIndex"
                      placeholder="선택"
                      style="width: 100%"
                      @change="handleSelectRank"
                    >
                      <el-option
                        v-for="(item,index) in rankList"
                        :key="item.detailCode"
                        :label="item.detailName"
                        :value="index"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="직책" label-width="120px" prop="jobIndex">
                    <el-select
                      v-model="jobIndex"
                      placeholder="선택"
                      style="width: 100%"
                      @change="handleSelectJob"
                    >
                      <el-option
                        v-for="(item,index) in jobList"
                        :key="item.detailCode"
                        :label="item.detailName"
                        :value="index"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="담당업무" label-width="120px" prop="responsibilities">
                    <el-input v-model="userForm.responsibilities" />
                  </el-form-item>
                </el-col>
                <el-col v-if="!isEdit" :xs="24" :sm="24" :lg="24">
                  <el-form-item label="패스워드" label-width="120px">
                    <span> 초기 비밀번호는 사용자 아이디와 동일합니다</span>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="생년월일" label-width="120px" prop="birthDate">
                    <el-date-picker
                      v-model="userForm.birthDate"
                      type="date"
                      placeholder="생년월일 선택"
                      :picker-options="pickerOptions"
                      value-format="yyyy-MM-dd"
                      :editable="false"
                      :clearable="false"
                      size="small"
                      style="float: left;width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="거주지" label-width="120px" prop="address">
                    <el-input ref="address" v-model="userForm.address" placeholder="기본주소" @focus="showApi" />
                    <el-input ref="address1" v-model="userForm.address1" placeholder="상세주소" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="이메일" label-width="120px" prop="email">
                    <el-input v-model="userForm.email" readonly :disabled="isEdit" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="연락처" label-width="120px" prop="tel">
                    <el-input v-model="userForm.tel" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="입사일" label-width="120px" prop="hireDate">
                    <el-date-picker
                      v-model="userForm.hireDate"
                      type="date"
                      placeholder="입사일 선택"
                      :picker-options="pickerOptions"
                      value-format="yyyy-MM-dd"
                      :editable="false"
                      :clearable="false"
                      size="small"
                      style="float: left;width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="내선번호" label-width="120px" prop="ex">
                    <el-input v-model="userForm.ex" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24">
                  <el-form-item label="좌석" label-width="120px" prop="seatId">
                    <el-input v-model="userForm.seatId" @focus="isSeat=true" />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24" align="right" style="margin-bottom: 10px">
                  <el-upload
                    ref="upload"
                    :file-list="fileList"
                    :auto-upload="false"
                    :on-change="handleChange"
                    :before-remove="beforeRemove"
                    :on-remove="handleRemove"
                    action=""
                    :limit="1"
                  >
                    <el-button v-if="!isImage" slot="trigger" size="small">사진 등록</el-button>
                  </el-upload>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24" align="right" style="margin-bottom: 10px">
                  <el-form-item label="자산관리자" label-width="120px" prop="admin">
                    <el-switch
                      v-model="userForm.assetAdmin"
                      :active-value="true"
                      :inactive-value="false"
                      @change="handleAssetAdminChange"
                    />
                  </el-form-item>
                </el-col>
                <el-col v-if="userForm.assetAdmin" :xs="24" :sm="24" :lg="24">
                  <el-form-item label="자산관리부서" label-width="120px" prop="mgrOrgCode">
                    <treeselects
                      v-model="userForm.mgrOrgCode"
                      :default-expand-level="1"
                      :options="$store.getters.orgList"
                      placeholder="선택"
                      :clearable="false"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24" align="right" style="margin-bottom: 10px">
                  <el-form-item label="관리자" label-width="120px" prop="admin">
                    <el-switch
                      v-model="userForm.admin"
                      :active-value="true"
                      :inactive-value="false"
                      @change="handleAdminChange"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="24" :lg="24" align="right">
                  <el-button
                    v-if="isEdit"
                    type="primary"
                    :loading="loading1"
                    icon="el-icon-check"
                    size="small"
                    @click="handleUserEdit"
                  >수정
                  </el-button>
                  <el-button
                    v-else
                    type="primary"
                    :loading="loading1"
                    icon="el-icon-check"
                    size="small"
                    @click="handleUserRegister"
                  >등록
                  </el-button>
                  <router-link :to="{name:'user-list'}" style="margin-left: 10px">
                    <el-button icon="el-icon-folder" size="small">목록</el-button>
                  </router-link>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
  <seat-index v-else :type="'user'" @getSeatId="getUserId" />
</template>

<script>
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import {
  alreadyId_message,
  email_message,
  id_message,
  name_message,
  necessary_message,
  tel_message, user_edit_message, user_register_message, fail_message, alreadyCode_message
} from '../message/constant'
import { validId, validEmail, validName, validTel } from '@/utils/validate'
import { rank_cd, job_cd } from '@/default/data'
import {
  api_user_check,
  api_user_register,
  api_user_edit,
  api_user_info,
  api_user_image_delete,
  api_user_image_upload
} from '@/api/user/user'
import { validImage } from '@/utils/validate'
import { mapGetters } from 'vuex'
import SeatIndex from '../../seat/seat-index'

export default {
  name: 'UserEdit',
  components: { SeatIndex, Treeselects },
  data() {
    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else if (!validEmail(value)) {
        callback(new Error(email_message))
      } else {
        callback()
      }
    }
    const validateUserId = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else if (!validId(value)) {
        callback(new Error(id_message))
      } else {
        if (!this.isIdValid) {
          api_user_check(value).then(response => {
            const isAlready = response.data
            if (isAlready) {
              callback(new Error(alreadyId_message))
            } else {
              this.isIdValid = true
              callback()
            }
          })
        } else {
          callback()
        }
      }
    }
    const validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else if (!validName(value)) {
        callback(new Error(name_message))
      } else {
        callback()
      }
    }
    const validateTel = (rule, value, callback) => {
      if (value === '') {
        callback(new Error(necessary_message))
      } else if (!validTel(value)) {
        callback(new Error(tel_message))
      } else {
        callback()
      }
    }
    const validateCommon = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    const validateMgrCode = (rule, value, callback) => {
      if ((value === '' || value === null || value === undefined) && this.userForm.assetAdmin) {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        }
      },
      selectOrgCode: '',
      conMgrCode: [],
      userForm: {
        userId: '',
        name: '',
        orgCode: null,
        rankCd: '',
        rankNm: '',
        jobCd: '',
        jobNm: '',
        email: '',
        responsibilities: '',
        tel: '',
        hireDate: '',
        birthDate: '',
        address: '',
        address1: '',
        imgSrc: '',
        seatId: '',
        admin: false,
        assetAdmin: false,
        mgrOrgCode: null,
        ex: ''
      },
      userId: '',
      rankIndex: null,
      jobIndex: null,
      userRules: {
        userId: [{ required: true, trigger: 'blur', validator: validateUserId }],
        name: [{ required: true, trigger: 'blur', validator: validateName }],
        email: [{ required: true, trigger: 'blur', validator: validateEmail }],
        tel: [{ required: true, trigger: 'blur', validator: validateTel }],
        address: [{ required: true, trigger: 'change', validator: validateCommon }],
        hireDate: [{ required: true, trigger: 'change', validator: validateCommon }],
        birthDate: [{ required: true, trigger: 'change', validator: validateCommon }],
        orgCode: [{ required: true, trigger: 'change', validator: validateCommon }],
        mgrOrgCode: [{ required: true, trigger: 'change', validator: validateMgrCode }],
        rankIndex: [{ required: true, trigger: 'change', validator: validateCommon }],
        jobIndex: [{ required: true, trigger: 'change', validator: validateCommon }]
      },
      forms: new FormData(),
      isEdit: false,
      isSeat: false,
      isIdValid: false,
      loading: false,
      loading1: false,
      isImage: false,
      rankList: [],
      jobList: [],
      fileList: [],
      uploadList: [],
      companyEmail: ''
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  watch: {
    userId: {
      handler(data) {
        this.userForm.userId = data
        this.userForm.email = data + this.companyEmail
      }
    }
  },
  async created() {
    this.getCommonList()
    const userId = this.$route.query.userId
    if (userId !== undefined) {
      this.isEdit = true
      await this.getUserInfo(userId)
    }
  },
  methods: {
    async getCommonList() {
      this.rankList = this.$store.getters.commonCode.rank || rank_cd
      this.jobList = this.$store.getters.commonCode.job || job_cd
      this.companyEmail = this.$store.getters.commonCode.email[0].detailName
    },
    handleSelectRank() {
      this.userForm.rankCd = this.rankList[this.rankIndex].detailCode
      this.userForm.rankNm = this.rankList[this.rankIndex].detailName
    },
    handleSelectJob() {
      this.userForm.jobCd = this.jobList[this.jobIndex].detailCode
      this.userForm.jobNm = this.jobList[this.jobIndex].detailName
    },
    handleUserRegister() {
      this.$refs.userForm.validate(async valid => {
        if (valid) {
          this.loading1 = true
          await api_user_register(this.userForm).then(async() => {
            this.loading1 = false
            this.$message({
              type: 'success',
              message: user_register_message
            })
            if (this.uploadList.length > 0) {
              await this.handleFileUpload()
            }
            this.$router.push({ name: 'user-list' })
          }).catch(() => {
            this.loading1 = false
          })
        } else {
          return false
        }
      })
    },
    handleUserEdit() {
      this.$refs.userForm.validate(async valid => {
        if (valid) {
          if (this.uploadList.length > 0) {
            await this.handleFileUpload()
          }
          this.loading1 = true
          await api_user_edit(this.userForm).then(async() => {
            this.loading1 = false
            this.$message({
              type: 'success',
              message: user_edit_message
            })
            if (this.uploadList.length > 0) {
              await this.handleFileUpload()
            }
            this.$router.push({ name: 'user-list' })
          }).catch(() => {
            this.loading1 = false
          })
        } else {
          return false
        }
      })
    },
    async getUserInfo(userId) {
      this.loading = true
      await api_user_info(userId).then(response => {
        const { info, fileList } = response.data
        this.userForm = info
        this.fileList = fileList
        this.isImage = fileList.length > 0
        this.isIdValid = true
        this.userId = info.userId
        for (let i = 0; i < this.rankList.length; i++) {
          if (this.rankList[i].detailCode === info.rankCd) {
            this.rankIndex = i
            break
          }
        }
        for (let i = 0; i < this.jobList.length; i++) {
          if (this.jobList[i].detailCode === info.jobCd) {
            this.jobIndex = i
            break
          }
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleChange(file, fileList) {
      if (this.beforeAvatarUpload(file)) {
        file.upload = false
        this.uploadList.push(file.raw)
        this.readURL(file.raw)
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
        await api_user_image_delete(this.userForm.userId)
      }
      this.uploadList = []
      this.isImage = false
      this.userForm.imgSrc = ''
    },
    readURL(value) {
      const reader = new FileReader()
      reader.onload = (e) => {
        this.userForm.imgSrc = e.target.result
      }
      reader.readAsDataURL(value)
    },
    async handleFileUpload() {
      this.forms = new FormData()
      for (let i = 0; i < this.uploadList.length; i++) {
        this.forms.append('files', this.uploadList[i])
      }
      this.forms.append('userId', this.userForm.userId)
      await api_user_image_upload(this.forms).then(() => {

      }).catch(() => {
        this.loading = false
        this.$message({
          type: 'error',
          message: fail_message
        })
      })
    },
    getUserId(value) {
      this.userForm.seatId = value
      this.isSeat = false
    },
    handleAssetAdminChange() {
      if (!this.userForm.assetAdmin) {
        this.userForm.mgrOrgCode = null
      } else {
        this.userForm.admin = false
      }
    },
    handleAdminChange() {
      if (this.userForm.admin) {
        this.userForm.mgrOrgCode = null
        this.userForm.assetAdmin = false
      }
    },
    handleNodeClick(data) {
      const tag = {
        value: data.orgCode,
        name: data.orgName,
        orgPaCode: data.orgPaCode
      }
      for (const code of this.conMgrCode) {
        if (code.value === data.orgCode) {
          this.$message({
            type: 'warning',
            message: alreadyCode_message
          })
          return false
        }
        if (code.orgPaCode === data.orgCode) {
          this.conMgrCode.splice(this.conMgrCode.indexOf(code), 1)
        }
        if (code.value === data.orgPaCode) {
          return false
        }
      }
      this.conMgrCode.push(tag)
      setTimeout(() => {
        this.selectOrgCode = ''
      }, 200)
    },
    handleClose(tag) {
      this.conMgrCode.splice(this.conMgrCode.indexOf(tag), 1)
    },
    showApi() {
      this.$refs.address.blur()
      new window.daum.Postcode({
        oncomplete: (data) => {
          let fullRoadAddr = data.roadAddress
          let extraRoadAddr = ''
          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname
          }
          if (data.buildingName !== '' && data.apartment === 'Y') { extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName) }
          if (extraRoadAddr !== '') { extraRoadAddr = ' (' + extraRoadAddr + ')' }
          if (fullRoadAddr !== '') { fullRoadAddr += extraRoadAddr }
          this.userForm.address = fullRoadAddr
        },
        onclose: (state) => {
          if (state === 'COMPLETE_CLOSE') {
            this.userForm.address1 = ''
            this.$refs.address1.focus()
          }
        }
      }).open()
    }
  }
}
</script>

<style scoped>

</style>
