<template>
  <div class="login-container">
    <div class="login-div">
      <el-form
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        autocomplete="on"
        label-position="left"
      >

        <div class="title-container">
          <h3 class="title">관리자 로그인 페이지</h3>
        </div>

        <el-form-item prop="adminId">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="adminId"
            v-model="loginForm.adminId"
            placeholder="아이디"
            name="adminId"
            type="text"
            tabindex="1"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="비밀번호"
            name="password"
            tabindex="2"
            autocomplete="off"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
        <el-button
          :loading="loading"
          type="primary"
          style="width:100%;margin-bottom:30px;"
          @click.native.prevent="handleLogin"
        >로그인
        </el-button>
      </el-form>
    </div>
    <el-dialog :visible.sync="showDialog" custom-class="dialog-div">
      <div class="title-container">
        <h3 class="title">패스워드 변경</h3>
      </div>
      <el-form
        ref="passwordForm"
        :model="passwordForm"
        :rules="passwordRules"
        class="change-form"
        autocomplete="on"
        label-position="left"
      >
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="changePasswordType"
            ref="changePasswordType"
            v-model="passwordForm.password"
            :type="changePasswordType"
            placeholder="새 비밀번호"
            name="password"
            tabindex="3"
          />
          <span class="show-pwd" @click="showPwd1">
            <svg-icon :icon-class="changePasswordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-form-item prop="rePassword">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="changeRePasswordType"
            ref="changeRePasswordType"
            v-model="passwordForm.rePassword"
            :type="changeRePasswordType"
            placeholder="새 비밀번호 확인"
            name="password"
            tabindex="4"
            @keyup.enter.native="handleChange"
          />
          <span class="show-pwd" @click="showPwd2">
            <svg-icon :icon-class="changeRePasswordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
        <el-button
          :loading="loading1"
          type="primary"
          style="width:100%;margin-bottom:30px;"
          @click.native.prevent="handleChange"
        >변경
        </el-button>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { api_admin_pwd_edit } from '@/api/manager/admin'
import { changeFail, changeSuccess } from './message/constant'
import CryptoJS from 'crypto-js'

export default {
  name: 'Login',
  data() {
    const validateAdminId = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('아이디를 입력해주세요'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('비밀번호를 입력해주세요'))
      } else {
        callback()
      }
    }
    const validatePasswordCheck = (rule, value, callback) => {
      const { regex } = this.$store.getters.commonCode
      const pwd_regex = regex.map(e => {
        if (e.detailCode === 'pwd') {
          return e.detailName
        }
      }) || '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$'
      const pwd_desc = regex.map(e => {
        if (e.detailCode === 'pwd') {
          return e.detailDesc
        }
      }) || '영어대문자, 소문자, 숫자 포함 8자 이상'
      if (value === '') {
        callback(new Error('필수 정보입니다'))
      } else if (value === this.loginForm.adminId) {
        callback(new Error('아이디로만 이루어진 비밀번호는 사용 불가 합니다'))
      } else if (!value.match(pwd_regex)) {
        callback(new Error(pwd_desc))
      } else {
        callback()
      }
    }
    const validateRePasswordCheck = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('필수 정보입니다'))
      } else if (value !== this.passwordForm.password) {
        callback(new Error('비밀번호가 일치하지 않습니다'))
      } else {
        this.passwordForm.adminId = this.loginForm.adminId
        callback()
      }
    }
    return {
      loginForm: {
        adminId: '',
        password: ''
      },
      passwordForm: {
        password: '',
        rePassword: '',
        adminId: ''
      },
      loginRules: {
        adminId: [{ required: true, trigger: 'blur', validator: validateAdminId }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordRules: {
        password: [{ required: true, trigger: 'blur', validator: validatePasswordCheck }],
        rePassword: [{ required: true, trigger: 'blur', validator: validateRePasswordCheck }]
      },
      passwordType: 'password',
      changePasswordType: 'password',
      changeRePasswordType: 'password',
      capsTooltip: false,
      loading: false,
      loading1: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
    /* this.$store.dispatch('app/setCommonCode', 'regex')*/
  },
  mounted() {
    if (this.loginForm.adminId === '') {
      this.$refs.adminId.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    showPwd1() {
      if (this.changePasswordType === 'password') {
        this.changePasswordType = ''
      } else {
        this.changePasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.changePasswordType.focus()
      })
    },
    showPwd2() {
      if (this.changeRePasswordType === 'password') {
        this.changeRePasswordType = ''
      } else {
        this.changeRePasswordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.changeRePasswordType.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          const encrypt_key = process.env.VUE_APP_ENCRYPT_KEY
          const data = {
            adminId: this.loginForm.adminId,
            password: CryptoJS.AES.encrypt(this.loginForm.password, encrypt_key).toString()
          }
          this.loading = true
          await this.$store.dispatch('admin/login', data)
            .then(() => {
              this.$router.push({ name: 'admin' })
              this.loading = false
            })
            .catch((data) => {
              if (data.pwdChange) {
                this.showDialog = true
                this.passwordForm.password = ''
                this.passwordForm.rePassword = ''
              }
              this.loading = false
            })
        } else {
          return false
        }
      })
    },
    handleChange() {
      this.$refs.passwordForm.validate(async valid => {
        if (valid) {
          this.loading1 = true
          const encrypt_key = process.env.VUE_APP_ENCRYPT_KEY
          const data = {
            adminId: this.passwordForm.adminId,
            password: CryptoJS.AES.encrypt(this.passwordForm.password, encrypt_key).toString()
          }
          await api_admin_pwd_edit(data).then(() => {
            this.$message({
              message: changeSuccess,
              type: 'success'
            })
            this.loading1 = false
            this.showDialog = false
          }).catch(() => {
            this.loading1 = false
            this.$message({
              message: changeFail,
              type: 'warning'
            })
          })
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
    /* async licenseCheck() {
        await api_license_check().then(response => {
          const { success } = response
          if (!success) {
            this.$router.push('/license-register')
          }
        })
      }*/
  }
}
</script>

<style lang="scss">

  $bg: #283443;
  $light_gray: #fff;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
      color: $cursor;
    }
  }

  /* reset element-ui css */
  .login-div {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        margin: 0px 0px 0px 10px;
        padding: 12px 0px 12px 0px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }

  .dialog-div {
    background: #283443;

    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;

        &:-webkit-autofill {
          box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }

    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: white;
    }
  }
</style>

<style lang="scss" scoped>
  $bg: #2d3a4b;
  $dark_gray: #889aa4;
  $light_gray: #eee;

  .login-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;

    .login-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 160px 35px 0;
      margin: 0 auto;
      overflow: hidden;
    }

    .change-form {
      position: relative;
      width: 520px;
      max-width: 100%;
      padding: 30px 35px 0;
      margin: 0 auto;
      overflow: hidden;
    }

    .tips {
      font-size: 14px;
      color: #fff;
      margin-bottom: 10px;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }

    .title-container {
      position: relative;

      .title {
        font-size: 26px;
        color: $light_gray;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }

    .thirdparty-button {
      position: absolute;
      right: 0;
      bottom: 6px;
    }

    @media only screen and (max-width: 470px) {
      .thirdparty-button {
        display: none;
      }
    }
  }
</style>
